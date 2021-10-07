package bytecode

sealed class TValue<T>(val value: T) {
    class IntValue(int: Int): TValue<Int>(int) {
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

    class DoubleValue(double: Double): TValue<Double>(double) {
        override fun toString(): String {
            return "DoubleValue(value = $value)"
        }
    }

    class StringValue(string: String): TValue<String>(string) {
        override fun toString(): String {
            return "StringValue(value = $value)"
        }
    }

    fun asInt() = this as IntValue

    fun asDouble() = this as DoubleValue

    fun asString() = this as StringValue
}