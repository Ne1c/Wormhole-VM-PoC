package wh.hypervisor.api.client

import wh.hypervisor.api.*
import wh.hypervisor.api.utils.Interruptible
import java.net.Socket

class HypervisorClientThread(
    private val socketPort: Int,
    private val interruptible: Interruptible
) : Thread() {
    private val reader = Reader()
    private var finished = false

    override fun run() {
        val socket = Socket("localhost", socketPort)
        val writer = Writer(socket.getOutputStream())

        writer.write(RequestSyncCommand())

        while (reader.read(socket.getInputStream())) {
        }

        val command = reader.parse()

        if (command is SyncDataResponse) {
            interruptible.interruption(command)
        }

        writer.write(SyncFinished())

        socket.close()

        finished = true
    }

    fun isFinished() = finished
}