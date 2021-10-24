package wh.vm.bytecode

import kotlinx.serialization.Serializable
import wh.hypervisor.api.utils.INT_32_BITS_ARRAY_SIZE

@Serializable
sealed class TValue {
    class IntValue(val value: Int) : TValue() {
        fun sum(intValue: IntValue): IntValue {
            return IntValue(this.value + intValue.value)
        }

        fun minus(intValue: IntValue): IntValue {
            return IntValue(this.value - intValue.value)
        }

        override fun sizeInBytes(): Int = INT_32_BITS_ARRAY_SIZE

//        override fun serialize(): ByteArray = value.toByteArray(INT_32_BITS_ARRAY_SIZE)

        override fun toString(): String {
            return "IntValue(value = $value)"
        }
    }

    class DoubleValue(val value: Double) : TValue() {
        override fun sizeInBytes(): Int = Double.SIZE_BYTES

//        override fun serialize(): ByteArray {
//            TODO("Not yet implemented")
//        }

        override fun toString(): String {
            return "DoubleValue(value = $value)"
        }
    }

    class StringValue(val value: String) : TValue() {
        // size of string + bytes
        private val _sizeInBytes = 2 + value.length

        override fun sizeInBytes(): Int = _sizeInBytes

//        override fun serialize(): ByteArray = value.toByteArray()

        override fun toString(): String {
            return "StringValue(value = $value)"
        }
    }

    fun asInt() = this as IntValue

    fun asDouble() = this as DoubleValue

    fun asString() = this as StringValue

    open fun sizeInBytes(): Int = 0
}