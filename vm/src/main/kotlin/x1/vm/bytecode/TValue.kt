package x1.vm.bytecode

import kotlinx.serialization.Serializable

@Serializable
sealed class TValue {
    @Serializable
    class IntValue(val value: Int) : TValue() {
        fun sum(intValue: IntValue): IntValue {
            return IntValue(this.value + intValue.value)
        }

        fun minus(intValue: IntValue): IntValue {
            return IntValue(this.value - intValue.value)
        }

        override fun toString(): String {
            return "IntValue(value = $value)"
        }
    }

    @Serializable
    class DoubleValue(val value: Double) : TValue() {
        override fun toString(): String {
            return "DoubleValue(value = $value)"
        }
    }

    @Serializable
    class StringValue(val value: String) : TValue() {
       override fun toString(): String {
            return "StringValue(value = $value)"
        }
    }

    fun asInt() = this as IntValue

    fun asDouble() = this as DoubleValue

    fun asString() = this as StringValue
}