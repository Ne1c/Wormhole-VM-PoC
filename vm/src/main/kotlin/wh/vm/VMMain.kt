package wh.vm

import wh.vm.dev.BytecodeFile
import wh.vm.dev.OutputReader
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
