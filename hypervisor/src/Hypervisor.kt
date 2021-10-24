import wh.hypervisor.api.*
import java.net.ServerSocket
import java.net.Socket

/**
 * This is entry point for WH Hypervisor
 */

/**
 * Start new vm instance with program placed by path
 */
const val START_CMD = "start"

/**
 * Stop VM instance with this process id
 */
const val STOP_CMD = "stop"

/**
 * Sync VM instance process with id with another process
 */
const val SYNC_CMD = "sync"

class VMInstance(val process: Process, val socketPort: Int)

fun main(args: Array<String>) {
    val vms = mutableListOf<VMInstance>()

    while (true) {
        val line = readLine() ?: continue
        val command = line.split(' ')

        when (command[0]) {
            START_CMD -> {
                val path = command[1]
                val socketPort = getFreePort()

                val process = ProcessBuilder("./VM", path, socketPort.toString()).start()

                vms.add(VMInstance(process, socketPort))

                println("VM started with PID ${vms.size}")
            }
            STOP_CMD -> {
                val pid = command[1].toInt()

                vms[pid].process.destroy()
                vms.removeAt(pid)

                println("VM with PID $pid stopped")
            }
            SYNC_CMD -> {
                val whoPid = command[1].toInt()
                val whomPid = command[2].toInt()

                val whoProcess = vms[whoPid]
                val whomProcess = vms[whomPid]

                println("Sync $whoPid with $whomPid")

                val reader = Reader()

                val whomSocket = Socket("localhost", whomProcess.socketPort)
                val whomWriter = Writer(whomSocket.getOutputStream())

                whomWriter.write(RequestSyncCommand())

                while(reader.read(whomSocket.getInputStream())) {}

                val dataResponse = reader.parse() as SyncDataResponse

                whomWriter.write(SyncFinished())

                whomSocket.close()

                println("Sync data received from $whomPid")
                println("Transmitting to $whoPid...")

                val whoSocket = Socket("localhost", whoProcess.socketPort)
                val whoWriter = Writer(whoSocket.getOutputStream())

                whoWriter.write(dataResponse)

                whoSocket.close()
            }
            else -> println("Wrong command, lol :|")
        }
    }
}

private fun getFreePort(): Int {
    val serverSocket = ServerSocket(0)
    val port = serverSocket.localPort
    serverSocket.close()

    return port
}
