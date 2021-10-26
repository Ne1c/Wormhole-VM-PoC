package x1.vm.vm

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import x1.hypervisor.api.Command
import x1.hypervisor.api.HypervisorService
import x1.hypervisor.api.RequestSyncCommand
import x1.hypervisor.api.SyncDataResponse
import x1.hypervisor.api.utils.Interruptible
import x1.vm.bytecode.*
import x1.vm.bytecode.Function
import x1.vm.serializer.StackSerializer
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class VM(
    private val instructions: List<Instruction>,
    private val constantPool: List<ConstantPoolEntry>,
    socketPort: Int
) : Interruptible {
    @Serializable
    internal class State {
        var ip = 0
        var sp = 0

        // base purpose registers
        val bpr = Array<TValue?>(256) { null }

        @Serializable(with = StackSerializer::class)
        val stack = Stack<StackFrame>()

        var halted = false
    }

    private val hypervisorService = HypervisorService(socketPort, this)

    private var state = State()

    private val lock = ReentrantLock()

    init {
        // find main function
        val funEntry =
            constantPool.firstOrNull {
                if (it.type == ConstantPoolEntry.Type.FUNCTION) {
                    val funEntry = it.asFunEntry()

                    constantPool[funEntry.nameIndex].asStringEntry().string == MAIN_FUNCTION_NAME
                } else {
                    false
                }
            }?.asFunEntry()

        val mainFunction = funEntry?.toFunction(constantPool[funEntry.nameIndex].asStringEntry().string)
            ?: Function(MAIN_FUNCTION_NAME, 0, 0, 0)

        state.stack.push(StackFrame(mainFunction, 0))

        hypervisorService.start()
    }

    fun fetchInstruction(): Instruction {
        if (isHalted()) throw IllegalStateException("VM is halted")

        return instructions[state.ip++]
    }

    fun execute(instruction: Instruction) {
        if (isHalted()) throw IllegalStateException("VM is halted")

        lock.lock()

        when (instruction.opcode) {
            OpCode.OP_LOADC -> {
                val constantPoolIndex = (instruction.src1 shl 8) or instruction.src2
                state.stack[state.sp].registers[instruction.dst] = constantPool[constantPoolIndex]
            }
            OpCode.OP_ILOAD -> state.bpr[instruction.dst] = constantPool[instruction.src1].asIntEntry().value
            OpCode.OP_IADD -> {
                state.bpr[instruction.dst] =
                    state.bpr[instruction.src1]!!.asInt().sum(state.bpr[instruction.src2]!!.asInt())
            }
            OpCode.OP_PRINT -> println(state.bpr[instruction.src1])
            OpCode.OP_HALT -> state.halted = true
            OpCode.OP_CALL -> call(instruction.src1)
            OpCode.OP_PAUSE -> {
                val pause = (instruction.src1 shl 16 and 0xFF) or (instruction.src2 shl 8 and 0xFF) or (instruction.dst and 0xFF)
                sleep(pause.toLong())

                // Hack to test hypervisor
                // Do jmp to IADD instruction

                state.ip = 2
            }
            OpCode.OP_RETURN -> TODO()
        }

        lock.unlock()
    }

    fun isHalted() = state.halted

    private fun call(index: Int) {
        val funEntry = constantPool[index].asFunEntry()
        val function = funEntry.toFunction(constantPool[funEntry.nameIndex].asStringEntry().string)
        val callingFrame = state.stack[state.sp]

        state.stack.push(StackFrame(function, 1))

        state.sp++

        state.ip = function.address
    }

    /**
     * Should be called in safe place
     */
    private fun serialize(): String {
        return Json.encodeToString(state)
    }

    /**
     * Should be called in safe place
     */
    private fun deserialize(data: String) {
        state = Json.decodeFromString(data)
    }

    override fun interruption(reason: Command) {
        println("Interrupted with $reason")

        lock.lock()

        if (reason is RequestSyncCommand) {
            hypervisorService.sendSyncData(serialize().also {
                println("Serialized state:\n$it")
            })
        } else if (reason is SyncDataResponse) {
            deserialize(String(reason.data).also {
                println("Deserialized state:\n$it")
            })
        }

        lock.unlock()
    }
}