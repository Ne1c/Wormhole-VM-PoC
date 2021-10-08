package bytecode

import utils.toByteArray

enum class OpCode {
    OP_CALL,
    OP_RETURN,
    OP_ICONST,
    OP_ILOAD,
    OP_IADD,
    OP_PRINT,
    OP_HALT
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

