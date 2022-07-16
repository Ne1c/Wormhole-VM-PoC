import org.junit.Test
import x1.vm.bytecode.*
import x1.vm.vm.VM

class TestVM {
    private lateinit var vm: VM

    @Test
    fun `verify OP_IADD instruction`() {
        val instructionSet = listOf(
            Instruction(OpCode.OP_ILOAD, 0, 0, 0),
            Instruction(OpCode.OP_ILOAD, 1, 0, 1),
            Instruction(OpCode.OP_IADD, 0, 1, 2),
            Instruction(OpCode.OP_PRINT, 2, 0, 0),
            Instruction(OpCode.OP_HALT, 0, 0, 0)
        )

        val constantPool = listOf<ConstantPoolEntry>(
            IntEntry(TValue.IntValue(100)),
            IntEntry(TValue.IntValue(89))
        )

        vm = VM(instructionSet, constantPool, 80)

        simulateVMExecution(vm)

        assert((vm.state.bpr[2] as TValue.IntValue).value == 189)
    }

    @Test
    fun `verify method call`() {
        val instructionSet = listOf(
            Instruction(OpCode.OP_CALL, 1, 0, 0),
            Instruction(OpCode.OP_ILOAD, 2, 0, 0),
            Instruction(OpCode.OP_ILOAD, 3, 0, 1),
            Instruction(OpCode.OP_IADD, 0, 1, 2),
            Instruction(OpCode.OP_PRINT, 2, 0, 0),
            Instruction(OpCode.OP_HALT, 0, 0, 0)
        )

        val constantPool = listOf(
            StringEntry("sumTwoNumbers"),
            FunctionEntry(0, 0, 0, 1),
            IntEntry(TValue.IntValue(100)),
            IntEntry(TValue.IntValue(89))
        )

        vm = VM(instructionSet, constantPool, 80)

        simulateVMExecution(vm)

        assert(vm.state.stack.size == 2)
        assert((vm.state.bpr[2] as TValue.IntValue).value == 189)
    }

    private fun simulateVMExecution(vm: VM) {
        while (!vm.isHalted()) {
            vm.execute(vm.fetchInstruction())
        }
    }
}