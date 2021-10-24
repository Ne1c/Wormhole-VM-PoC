package x1.vm

import x1.vm.dev.BytecodeFile
import x1.vm.dev.OutputReader
import x1.vm.vm.VM
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
