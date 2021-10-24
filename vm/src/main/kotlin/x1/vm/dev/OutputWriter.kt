package x1.vm.dev

import x1.hypervisor.api.utils.INT_16_BITS_ARRAY_SIZE
import x1.hypervisor.api.utils.toByteArray
import x1.vm.vm.MAJOR_VERSION
import x1.vm.vm.MINOR_VERSION
import java.io.FileOutputStream

object OutputWriter {
    fun write(bytecodeFile: BytecodeFile) {
        val outStream = FileOutputStream(bytecodeFile.name)

        // write major version
        outStream.write(MAJOR_VERSION.toByteArray(INT_16_BITS_ARRAY_SIZE))

        // write minor version
        outStream.write(MINOR_VERSION.toByteArray(INT_16_BITS_ARRAY_SIZE))

        // write constant pool size
        outStream.write(bytecodeFile.constantPool.size.toByteArray(INT_16_BITS_ARRAY_SIZE))

        bytecodeFile.constantPool.forEach {
            outStream.write(it.toByteArray())
        }

        bytecodeFile.instructions.forEach {
            outStream.write(it.toByteArray())
        }

        outStream.close()
    }
}