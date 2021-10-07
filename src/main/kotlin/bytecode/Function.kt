package bytecode

data class Function(
    val name: String,
    val args: Int, // max 256
    val locals: Int, // max 256
    val address: Int, // ret address ??
    val constants: Array<TValue<*>>,

    // debug information
    val line: Int,
    val source: String
)