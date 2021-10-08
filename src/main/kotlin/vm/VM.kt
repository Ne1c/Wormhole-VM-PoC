package vm

import bytecode.*
import bytecode.Function
import java.util.*

class VM(
    private val instructions: List<Instruction>,
    private val constantPool: List<ConstantPoolEntry>
) {
    private var ip = 0
    private var sp = 0

    // base purpose registers
    private var bpr = Array<TValue<*>?>(256) { null }

    private var stack = Stack<StackFrame>()
    private var halted = false

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

        stack.push(StackFrame(mainFunction, 0))
    }

    fun fetchInstruction(): Instruction {
        if (isHalted()) throw IllegalStateException("VM is halted")

        return instructions[ip++]
    }

    fun execute(instruction: Instruction) {
        if (isHalted()) throw IllegalStateException("VM is halted")

        when (instruction.opcode) {
            OpCode.OP_ICONST -> {
                val value = (instruction.src1 shl 8) or instruction.src2

                bpr[instruction.dst] = TValue.IntValue(value)
            }
            OpCode.OP_ILOAD -> bpr[instruction.dst] = constantPool[instruction.src1].asIntEntry().value
            OpCode.OP_IADD -> {
                bpr[instruction.dst] =
                    bpr[instruction.src1]!!.asInt().sum(bpr[instruction.src2]!!.asInt())
            }
            OpCode.OP_PRINT -> println(bpr[instruction.src1])
            OpCode.OP_HALT -> halted = true
            OpCode.OP_CALL -> call(instruction.src1)
            OpCode.OP_RETURN -> TODO()
        }
    }

    fun isHalted() = halted

    private fun call(index: Int) {
        val funEntry = constantPool[index].asFunEntry()
        val function = funEntry.toFunction(constantPool[funEntry.nameIndex].asStringEntry().string)
        val callingFrame = stack[sp]

        stack.push(StackFrame(function, 1))

        sp++

        ip = function.address
    }
}