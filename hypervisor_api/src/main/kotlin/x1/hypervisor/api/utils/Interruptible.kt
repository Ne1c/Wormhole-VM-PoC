package x1.hypervisor.api.utils

import x1.hypervisor.api.Command

interface Interruptible {
    fun interruption(reason: Command)
}