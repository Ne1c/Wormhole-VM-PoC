package x1.vm.bytecode

import x1.hypervisor.api.utils.toByteArray

enum class OpCode {
    /** Function related operations */
    OP_CALL,
    OP_RETURN,

    /** Binary operations */
    OP_ADD,
    OP_SUB,
    OP_MUL,
    OP_MOD,
    OP_POW,
    OP_DIV,
    /* Bit-wise AND operator */
    OP_BAND,
    /* Bit-wise OR operator */
    OP_BOR,
    /* Bit-wise XOR operator */
    OP_BXOR,
    /* Shift bits left */
    OP_SHL,
    /* Shift bits right */
    OP_SHR,

    /** Unary operations */
    /* Unary minus */
    OP_UNM,
    /* Bit-wise NOT operator */
    OP_BNOT,
    /* Logical NOT operator */
    OP_NOT,

    /** Conditional operations */
    /* Equal */
    OP_EQ,
    /* Less Than */
    OP_LT,
    /* Less Equal */
    OP_LE,

    /** Other operations */
    /* Loads Constant from Constant Pool into register */
    OP_LOADC,
    /* Jumps to specific instruction */
    OP_JMP,
    /* Designates program end */
    OP_HALT,

    /** DEBUG operations */
    /* Does a delay on specific time in ms */
    OP_PAUSE,
    /* Prints specific register into default output */
    OP_PRINT
}

data class Instruction(
    val opcode: OpCode,
    val src1: Int = 0,
    val src2: Int = 0,
    val dst: Int = 0
) {
    companion object {
        fun of(arr: ByteArray): Instruction {
            return Instruction(
                opcode = OpCode.values()[arr[0].toInt()],
                src1 = arr[1].toInt(),
                src2 = arr[2].toInt(),
                dst = arr[3].toInt()
            )
        }
    }

    fun instrWord(): Int = (opcode.ordinal shl 24) or (src1 shl 16) or (src2 shl 8) or dst

    fun toByteArray(): ByteArray = instrWord().toByteArray()
}

