package wh.vm.dev

import wh.vm.bytecode.ConstantPoolEntry
import wh.vm.bytecode.Instruction

data class BytecodeFile(
    val name: String,
    val majorVersion: Int,
    val minorVersion: Int,
    val constantPool: List<ConstantPoolEntry>,
    val instructions: List<Instruction>
)