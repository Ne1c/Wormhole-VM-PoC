package dev

import bytecode.ConstantPoolEntry
import bytecode.Instruction

data class BytecodeFile(
    val name: String,
    val majorVersion: Int,
    val minorVersion: Int,
    val constantPool: List<ConstantPoolEntry>,
    val instructions: List<Instruction>
)