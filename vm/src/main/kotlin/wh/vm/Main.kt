package wh.vm

import wh.vm.bytecode.ConstantPoolEntry
import wh.vm.bytecode.Instruction
import wh.vm.bytecode.OpCode
import wh.vm.dev.BytecodeFile
import wh.vm.dev.OutputReader
import wh.vm.dev.OutputWriter
import wh.vm.vm.MAJOR_VERSION
import wh.vm.vm.MINOR_VERSION
import wh.vm.vm.VM
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val bytecodeFile = loadProgram(getPath(args))

    val vm = VM(bytecodeFile.instructions, bytecodeFile.constantPool, getSocket(args))

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

fun loadProgram(path: String): BytecodeFile {
    return OutputReader.read(path)
}

fun getPath(args: Array<String>): String {
    return if (args.isEmpty()) {
        println("No path to executable")
        exitProcess(0)
    } else {
        args[0]
    }
}

fun getSocket(args: Array<String>): Int {
    return if (args.size < 2) {
        println("No socket")
        exitProcess(0)
    } else {
        args[1].toInt()
    }
}
