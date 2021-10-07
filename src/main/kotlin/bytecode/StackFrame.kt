package bytecode

class StackFrame(
    val function: Function,
    val retAddress: Long,
) {
    val regCount = function.args + function.locals
}