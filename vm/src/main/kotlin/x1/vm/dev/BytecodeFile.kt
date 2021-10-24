package x1.vm.dev

import x1.vm.bytecode.ConstantPoolEntry
import x1.vm.bytecode.Instruction

data class BytecodeFile(
    val name: String,
    val majorVersion: Int,
    val minorVersion: Int,
    val constantPool: List<ConstantPoolEntry>,
    val instructions: List<Instruction>
)