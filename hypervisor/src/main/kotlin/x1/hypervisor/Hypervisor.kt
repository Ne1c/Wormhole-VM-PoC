import x1.hypervisor.api.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
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
    Runtime.getRuntime().exec("pwd").run {
        waitFor()

        val runningIn = BufferedReader(InputStreamReader(inputStream)).readLine()
        println("Running in $runningIn")
    }

    val vms = hashMapOf<Int, VMInstance>()
    var idCounter = 0

    while (true) {
        val line = readLine() ?: continue
        val command = line.split(' ')

        when (command[0]) {
            START_CMD -> {
                val path = command[1]
                val socketPort = getFreePort()

                idCounter++

                val logFile = File("process_$idCounter.log")
                logFile.delete()
                logFile.createNewFile()

                val process = ProcessBuilder(
                    "java",
                    "-jar",
                    "out/artifacts/X1_VM_vm_main_jar/X1-VM.vm.main.jar",
                    path,
                    socketPort.toString()
                ).redirectOutput(logFile)
                    .redirectError(logFile)
                    .start()

                vms[idCounter] = VMInstance(process, socketPort)

                println("VM started with PID $idCounter on the port $socketPort")
            }
            STOP_CMD -> {
                val arg1 = command[1]

                if (arg1 == "all") {
                    vms.forEach {
                        it.value.process.destroy()

                        println("VM with PID ${it.key} stopped")
                    }
                    vms.clear()
                } else {
                    val pid = command[1].toInt()

                    vms[pid]?.process?.destroy()
                    vms.remove(pid)

                    println("VM with PID $pid stopped")
                }

            }
            SYNC_CMD -> {
                val whoPort = command[1].toInt()
                val whomPort = command[2].toInt()

//                val whoProcess = vms[whoPid] ?: run {
//                    println("Who PID not found")
//                    return
//                }
//                val whomProcess = vms[whomPid] ?: run {
//                    println("Whom PID not found")
//                    return
//                }

                println("Sync $whoPort with $whomPort")

                val reader = Reader()

                val whomSocket = Socket("localhost", whomPort)
                val whomWriter = Writer(whomSocket.getOutputStream())

                whomWriter.write(RequestSyncCommand())

                while (reader.read(whomSocket.getInputStream())) {
                }

                val dataResponse = reader.parse() as SyncDataResponse

                whomWriter.write(SyncFinished())

                whomSocket.close()

                println("Sync data received from $whomPort")
                println("Transmitting to $whoPort...")

                val whoSocket = Socket("localhost", whoPort)
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
