package bytecode

class StackFrame(
    val function: Function,
    val retAddress: Long,
) {
    // ret address + args count + locals count
    val registers = Array<TValue<*>?>(1 + function.nargs + function.nlocals) { null }
}