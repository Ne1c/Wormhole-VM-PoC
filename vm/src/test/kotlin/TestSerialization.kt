import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import x1.vm.bytecode.Function
import x1.vm.bytecode.StackFrame
import x1.vm.bytecode.TValue
import x1.vm.serializer.StackSerializer
import x1.vm.vm.VM

@RunWith(JUnit4::class)
class TestSerialization {
    @Test
    fun serializeIntValueToJson_isNotBlank() {
        val intValue = TValue.IntValue(5)
        val json = Json.encodeToString(intValue)

        assert(json.isNotBlank())
    }

    @Test
    fun serializeVmStateToJson_isNotBlank() {
        val vmState = VM.State()
        vmState.ip = 5
        vmState.sp = 1
        vmState.bpr[0] = TValue.StringValue("I'm String!")
        vmState.stack.push(StackFrame(Function("main", 1, 1, 1), 1))

        val json = Json.encodeToString(vmState)

        assert(json.isNotBlank())
    }

    @Test
    fun deserializeStackFromJson_isNotEmpty() {
        val serializedData = """
            [
                {
                  "function": {
                    "name": "main",
                    "nargs": 1,
                    "nlocals": 1,
                    "address": 1
                  },
                  "retAddress": 1,
                  "registers": [
                    null,
                    null,
                    null
                  ]
                }
            ]
        """.trimIndent()

        val stack = Json.decodeFromString(StackSerializer(serializer<StackFrame>()), serializedData)

        assert(stack.isNotEmpty())
    }
}