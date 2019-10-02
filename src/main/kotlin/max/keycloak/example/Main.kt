package max.keycloak.example

import max.keycloak.example.configjetty.ServletBuilder
import mu.KLogger
import mu.KotlinLogging
import org.eclipse.jetty.server.Connector
import org.eclipse.jetty.server.LowResourceMonitor
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option

class Main(args: Array<String>) {
    @Option(name = "-port", usage = "port destination to start server")
    private var port = 8888

    init {
        try {
            CmdLineParser(this).parseArgument(*args)
        } catch (cmd: CmdLineException) {
        }
    }

    fun start() {
        val server = server(port)
        server.setRequestLog { request, response ->
            logger.info {
                var message = "HttpLog\t" +
                        "Time:${System.currentTimeMillis()}\t" +
                        "Status:${response.status}\t" +
                        "Proto:${request.method}::" +
                        request.originalURI
                request.headerNames.toList().forEach { key ->
                    if (key != "Authorization")
                        message += "\t$key:${request.getHeader(key)}"
                }
                message += "\tInputIpAddress:${request.remoteAddr}"
                message
            }
        }
        server.start()
        server.join()
    }

    companion object {
        private val logger = getLogger()
        @JvmStatic
        fun main(args: Array<String>) {
            Main(args).start()
        }
    }
}

fun server(port: Int): Server {

    return Server(threadPool).apply {
        handler = ServletBuilder.build()
        connectors = connectorFor(this, "0.0.0.0", port)
        addBean(lowResourceMonitorFor(this))
    }
}

private val MAX_THREADS = 500
private val MIN_THREADS = 50
private val IDLE_TIMEOUT = 6000

private val threadPool: QueuedThreadPool
    get() {
        val threadPool = QueuedThreadPool(MAX_THREADS, MIN_THREADS, IDLE_TIMEOUT)
        threadPool.isDaemon = true
        threadPool.isDetailedDump = false
        return threadPool
    }

private fun lowResourceMonitorFor(server: Server): LowResourceMonitor {
    val monitor = LowResourceMonitor(server)
    monitor.period = 1000
    monitor.lowResourcesIdleTimeout = 1000
    monitor.monitorThreads = true
    monitor.maxMemory = 0
    monitor.maxLowResourcesTime = 5000
    return monitor
}

private fun connectorFor(server: Server, host: String, port: Int): Array<Connector> {
    val connector = ServerConnector(server)
    connector.host = host
    connector.port = port
    connector.idleTimeout = 30000
    connector.acceptQueueSize = 3000
    return arrayOf(connector)
}

inline fun <reified T> T.getLogger(): KLogger {
    return KotlinLogging.logger { T::class.simpleName }
}
