package x1.hypervisor.api.server

import x1.hypervisor.api.Reader
import x1.hypervisor.api.SyncDataResponse
import x1.hypervisor.api.Writer
import x1.hypervisor.api.utils.Interruptible
import java.net.ServerSocket

class HypervisorServerThread(
    private val socketPort: Int,
    private val interruptible: Interruptible
) : Thread() {
    private val reader = Reader()
    private var writer: Writer? = null

    override fun run() {
        val serverSocket = ServerSocket(socketPort)

        println("Hypervisor server started on the $socketPort port")

        while (true) {
            try {
                val socket = serverSocket.accept()

                writer = Writer(socket.getOutputStream())

                do {
                    while (!reader.read(socket.getInputStream())) { }

                    val command = reader.parse()

                    interruptible.interruption(command)
                } while (socket.isClosed)

                socket.close()
            } catch (e: Exception) {
                System.err.println("Hypervisor server exception occurred on the $socketPort port; e = $e")
            }
        }
    }

    fun send(syncDataResponse: SyncDataResponse) {
        writer?.write(syncDataResponse)
    }
}