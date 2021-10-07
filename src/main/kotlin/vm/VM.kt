package vm

import bytecode.*
import java.util.*
import kotlin.system.exitProcess

class VM(
    private val instructions: List<Instruction>,
    private val constantPool: List<ConstantPoolEntry>
) {
    private var ip = 0
    private var fp = 0

    // base purpose registers
    private var bpr = Array<TValue<*>?>(256) { null }

    private var stack = Stack<StackFrame>()
    private var halted  = false

    fun fetchInstruction(): Instruction {
        if (isHalted()) throw IllegalStateException("VM is halted")

        return instructions[ip++]
    }

    fun execute(instruction: Instruction) {
        if (isHalted()) throw IllegalStateException("VM is halted")

        when (instruction.opcode) {
            OpCode.OP_ILOAD -> bpr[instruction.dst] = TValue.IntValue(instruction.src1)
            OpCode.OP_IADD -> {
                bpr[instruction.dst] =
                    bpr[instruction.src1]!!.asInt().sum(bpr[instruction.src2]!!.asInt())
            }
            OpCode.OP_PRINT -> println(bpr[instruction.src1])
            OpCode.OP_HALT -> halted = true
            OpCode.OP_CALL -> TODO()
            OpCode.OP_RETURN -> TODO()
        }
    }

    fun isHalted() = halted

}