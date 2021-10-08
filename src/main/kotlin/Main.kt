import bytecode.*
import dev.BytecodeFile
import dev.OutputReader
import dev.OutputWriter
import vm.MAJOR_VERSION
import vm.MINOR_VERSION
import vm.VM
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    writeSimpleProgram()

    val bytecodeFile = readSimpleProgram()

    println(bytecodeFile)

    val vm = VM(bytecodeFile.instructions, bytecodeFile.constantPool)

    while (!vm.isHalted()) {
        vm.execute(vm.fetchInstruction())
    }

    exitProcess(0)
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

fun readSimpleProgram(): BytecodeFile {
    return OutputReader.read("test.o")
}