package x1.hypervisor.api.client

import x1.hypervisor.api.*
import x1.hypervisor.api.utils.Interruptible
import java.net.Socket

class HypervisorClientThread(
    private val socketPort: Int,
    private val interruptible: Interruptible
) : Thread() {
    private val reader = Reader()
    private var finished = false

    override fun run() {
        try {
            val socket = Socket("localhost", socketPort)

            println("Hypervisor client started on the $socketPort port")

            val writer = Writer(socket.getOutputStream())

            writer.write(RequestSyncCommand())

            while (reader.read(socket.getInputStream())) {
            }

            val command = reader.parse()

            if (command is SyncDataResponse) {
                interruptible.interruption(command)
            }

            socket.close()

            finished = true
        } catch (e: Exception) {
            System.err.println("Hypervisor client exception occurred on the $socketPort port; e = $e")
        }
    }

    fun isFinished() = finished
}