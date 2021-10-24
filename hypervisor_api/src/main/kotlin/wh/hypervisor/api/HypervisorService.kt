package wh.hypervisor.api

import wh.hypervisor.api.client.HypervisorClientThread
import wh.hypervisor.api.server.HypervisorServerThread
import wh.hypervisor.api.utils.Interruptible

class HypervisorService(private val port: Int, private val interruptible: Interruptible) {
    private val serverThread = HypervisorServerThread(port, interruptible)
    private var clientThread: HypervisorClientThread? = null

    fun start() {
        serverThread.start()
    }

    fun sendSyncData(data: String) {
        serverThread.send(SyncDataResponse(data.toByteArray()))
    }

    fun requestSync() {
        if (clientThread?.isFinished() == false) throw IllegalStateException("Sync is already requested")

        clientThread = HypervisorClientThread(port, interruptible)

        clientThread?.start()
    }
}