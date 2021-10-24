import x1.vm.bytecode.ConstantPoolEntry
import x1.vm.bytecode.Instruction
import x1.vm.bytecode.OpCode
import x1.vm.dev.BytecodeFile
import x1.vm.dev.OutputWriter
import x1.vm.vm.MAJOR_VERSION
import x1.vm.vm.MINOR_VERSION

fun main(args: Array<String>) {
    writeSimpleProgram()
}

fun writeSimpleProgram() {
    val constantPool = mutableListOf<ConstantPoolEntry>()
    val instructions = mutableListOf<Instruction>()

    instructions.add(Instruction(OpCode.OP_ICONST, src1 = 0, src2 = 32, dst = 0))
    instructions.add(Instruction(OpCode.OP_ICONST, src1 = 0, src2 = 8, dst = 1))
    instructions.add(Instruction(OpCode.OP_IADD, src1 = 0, src2 = 1, dst = 1))
    instructions.add(Instruction(OpCode.OP_PRINT, src1 = 1))
    instructions.add(Instruction(OpCode.OP_HALT))

    val bytecodeFile = BytecodeFile("test.o", MAJOR_VERSION, MINOR_VERSION, constantPool, instructions)
    OutputWriter.write(bytecodeFile)
}
