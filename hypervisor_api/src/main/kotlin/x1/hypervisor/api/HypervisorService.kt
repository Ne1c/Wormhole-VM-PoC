package x1.hypervisor.api

import x1.hypervisor.api.server.HypervisorServerThread
import x1.hypervisor.api.utils.Interruptible

class HypervisorService(private val port: Int, private val interruptible: Interruptible) {
    private val serverThread = HypervisorServerThread(port, interruptible)

    fun start() {
        serverThread.start()
    }

    fun sendSyncData(data: String) {
        serverThread.send(SyncDataResponse(data.toByteArray()))
    }
}