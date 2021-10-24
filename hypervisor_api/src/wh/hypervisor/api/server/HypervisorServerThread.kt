package wh.hypervisor.api.server

import wh.hypervisor.api.*
import wh.hypervisor.api.utils.Interruptible
import java.net.ServerSocket

class HypervisorServerThread(
    private val socketPort: Int,
    private val interruptible: Interruptible
) : Thread() {
    private val reader = Reader()
    private var writer: Writer? = null

    override fun run() {
        val serverSocket = ServerSocket(socketPort)

        while (true) {
            val socket = serverSocket.accept()

            writer = Writer(socket.getOutputStream())

            var closed = false

            do {
                if (reader.read(socket.getInputStream())) {
                    val command = reader.parse()

                    if (command is SyncFinished) {
                        closed = true
                    } else {
                        interruptible.interruption(command)
                    }
                }
            } while (closed)

            socket.close()
        }
    }

    fun send(syncDataResponse: SyncDataResponse) {
        writer?.write(syncDataResponse)
    }
}