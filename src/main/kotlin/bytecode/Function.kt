package bytecode

import kotlinx.serialization.Serializable
import wh.hypervisor.api.utils.U4
import wh.hypervisor.api.utils.U1

@Serializable
data class Function(
    val name: String,
    val nargs: U1, // max 256
    val nlocals: U1, // max 256
    val address: U4
)