package wh.hypervisor.api.utils

import wh.hypervisor.api.Command

interface Interruptible {
    fun interruption(reason: Command)
}