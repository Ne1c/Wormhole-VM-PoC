package bytecode

import utils.U4
import utils.U1

data class Function(
    val name: String,
    val nargs: U1, // max 256
    val nlocals: U1, // max 256
    val address: U4
)