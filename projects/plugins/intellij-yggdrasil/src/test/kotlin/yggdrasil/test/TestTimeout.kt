package yggdrasil.test

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/** Bounded execution for lexer/parser smoke tests. */
object TestTimeout {
    const val DEFAULT_TIMEOUT_MS: Long = 10_000

    fun <T> run(timeoutMs: Long = DEFAULT_TIMEOUT_MS, block: () -> T): T {
        require(timeoutMs > 0) { "timeoutMs must be positive, got $timeoutMs" }
        val caller = Thread.currentThread()
        val timedOut = AtomicBoolean(false)
        val watchdog = Executors.newSingleThreadScheduledExecutor { runnable ->
            Thread(runnable, "yggdrasil-test-timeout-watchdog").apply { isDaemon = true }
        }
        val future = watchdog.schedule({
            timedOut.set(true)
            caller.interrupt()
        }, timeoutMs, TimeUnit.MILLISECONDS)
        return try {
            val result = try {
                block()
            } catch (interrupted: InterruptedException) {
                throw AssertionError(
                    "Test exceeded ${timeoutMs}ms (possible hang in lexer/parser)",
                    interrupted,
                )
            }
            if (timedOut.get()) {
                throw AssertionError("Test exceeded ${timeoutMs}ms (possible hang in lexer/parser)")
            }
            result
        } finally {
            future.cancel(false)
            watchdog.shutdownNow()
            Thread.interrupted()
        }
    }
}
