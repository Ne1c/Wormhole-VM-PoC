package x1.vm.dev

import x1.hypervisor.api.utils.ByteArrayNavigator
import x1.hypervisor.api.utils.INT_16_BITS_ARRAY_SIZE
import x1.hypervisor.api.utils.INT_32_BITS_ARRAY_SIZE
import x1.vm.bytecode.*
import java.io.File
import java.io.FileInputStream

object OutputReader {
    fun read(path: String): BytecodeFile {
        val file = File(path)
        val inStream = FileInputStream(file)

        val buffer = ByteArray(file.length().toInt())
        inStream.read(buffer)

        val navigator = ByteArrayNavigator(buffer)

        val majorVersion = navigator.readNextBytesAsInt(INT_16_BITS_ARRAY_SIZE)
        val minorVersion = navigator.readNextBytesAsInt(INT_16_BITS_ARRAY_SIZE)
        val constantPoolSize = navigator.readNextBytesAsInt(INT_16_BITS_ARRAY_SIZE)

        // read constant pool
        val constantPool = mutableListOf<ConstantPoolEntry>()

        while (constantPool.size != constantPoolSize) {
            val type = ConstantPoolEntry.Type.values()[navigator.readNextBytesAsInt(1)]

            when (type) {
                ConstantPoolEntry.Type.INT -> {
                    constantPool.add(IntEntry.of(navigator.readNextBytes(IntEntry.SIZE_IN_BYTES)))
                }
                ConstantPoolEntry.Type.STRING -> {
                    val length = navigator.readNextByte().toInt()

                    constantPool.add(StringEntry.of(length, navigator.readNextBytes(length)))
                }
                ConstantPoolEntry.Type.FUNCTION -> {
                    constantPool.add(FunctionEntry.of(navigator.readNextBytes(FunctionEntry.SIZE_IN_BYTES)))
                }
            }
        }

        val instruction = mutableListOf<Instruction>()

        while (!navigator.isEnd()) {
            val instrWord = navigator.readNextBytes(INT_32_BITS_ARRAY_SIZE)

            instruction.add(Instruction.of(instrWord))
        }

        return BytecodeFile(file.name, majorVersion, minorVersion, constantPool, instruction)
    }
}