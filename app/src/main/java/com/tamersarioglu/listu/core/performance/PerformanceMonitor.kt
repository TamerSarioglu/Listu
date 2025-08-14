package com.tamersarioglu.listu.core.performance

import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class PerformanceMonitor @Inject constructor() {

    private val _metrics = MutableStateFlow(PerformanceMetrics())
    val metrics: StateFlow<PerformanceMetrics> = _metrics.asStateFlow()

    private val startTimes = mutableMapOf<String, Long>()
    private val durations = mutableMapOf<String, MutableList<Long>>()

    fun startTracking(operation: String) {
        startTimes[operation] = SystemClock.elapsedRealtime()
        log("📊 Started tracking: $operation")
    }

    fun endTracking(operation: String) {
        val startTime = startTimes[operation]
        if (startTime != null) {
            val duration = SystemClock.elapsedRealtime() - startTime

            durations.getOrPut(operation) { mutableListOf() }.add(duration)
            startTimes.remove(operation)

            updateMetrics(operation, duration)
            log("⏱️ Completed $operation in ${duration}ms")
        }
    }

    suspend fun trackSuspendFunction(
        operation: String,
        block: suspend () -> Unit
    ) {
        val duration = measureTimeMillis {
            block()
        }

        durations.getOrPut(operation) { mutableListOf() }.add(duration)
        updateMetrics(operation, duration)
        log("⚡ Suspend operation $operation completed in ${duration}ms")
    }

    suspend fun <T> trackSuspend(operation: String, block: suspend () -> T): T {
        var result: T? = null
        val duration = measureTimeMillis {
            result = block()
        }
        durations.getOrPut(operation) { mutableListOf() }.add(duration)
        updateMetrics(operation, duration)
        log("⚡ Suspend operation $operation completed in ${duration}ms")
        @Suppress("UNCHECKED_CAST")
        return result as T
    }

    inline fun <T> trackFunction(
        operation: String,
        block: () -> T
    ): T {
        startTracking(operation)
        return try {
            block()
        } finally {
            endTracking(operation)
        }
    }

    inline fun <T> track(operation: String, block: () -> T): T {
        return trackFunction(operation, block)
    }

    private fun updateMetrics(operation: String, duration: Long) {
        val currentMetrics = _metrics.value
        val operationDurations = durations[operation] ?: emptyList()

        val updatedOperations = currentMetrics.operations.toMutableMap()
        updatedOperations[operation] = OperationMetrics(
            name = operation,
            count = operationDurations.size,
            averageTime = operationDurations.average().toLong(),
            minTime = operationDurations.minOrNull() ?: 0L,
            maxTime = operationDurations.maxOrNull() ?: 0L,
            lastDuration = duration
        )

        _metrics.value = currentMetrics.copy(
            operations = updatedOperations,
            totalOperations = currentMetrics.totalOperations + 1,
            lastUpdated = System.currentTimeMillis()
        )
    }

    fun getAverageTime(operation: String): Long {
        return durations[operation]?.average()?.toLong() ?: 0L
    }

    fun getOperationCount(operation: String): Int {
        return durations[operation]?.size ?: 0
    }

    fun clearMetrics() {
        durations.clear()
        startTimes.clear()
        _metrics.value = PerformanceMetrics()
        log("🧹 Performance metrics cleared")
    }

    fun logSummary() {
        val currentMetrics = _metrics.value
        log("📈 Performance Summary:")
        log("Total Operations: ${currentMetrics.totalOperations}")

        currentMetrics.operations.values.forEach { operation ->
            log("  ${operation.name}: avg=${operation.averageTime}ms, count=${operation.count}")
        }
    }

    private fun log(message: String) {
        Log.d("PerformanceMonitor", message)
    }
}

@Composable
fun TrackScreenPerformance(screenName: String) {
    val performanceMonitor = remember { PerformanceMonitor() }

    DisposableEffect(screenName) {
        performanceMonitor.startTracking("Screen_$screenName")

        onDispose {
            performanceMonitor.endTracking("Screen_$screenName")
        }
    }
}

data class PerformanceMetrics(
    val operations: Map<String, OperationMetrics> = emptyMap(),
    val totalOperations: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

data class OperationMetrics(
    val name: String,
    val count: Int,
    val averageTime: Long,
    val minTime: Long,
    val maxTime: Long,
    val lastDuration: Long
)