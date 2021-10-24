package x1.vm.bytecode

import kotlinx.serialization.Serializable
import x1.hypervisor.api.utils.U4
import x1.hypervisor.api.utils.U1

@Serializable
data class Function(
    val name: String,
    val nargs: U1, // max 256
    val nlocals: U1, // max 256
    val address: U4
)