package dev.catsuperberg.pexels.app.domain.async

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class JobQueue(
    private val scope: CoroutineScope,
    private val onStart: () -> Unit,
    private val onFinished: () -> Unit
) {
    private val queue = Channel<Job>(Channel.UNLIMITED)
    @OptIn(ExperimentalCoroutinesApi::class)
    val isEmpty: Boolean
        get() = queue.isEmpty

    init {
        scope.launch() {
            for (job in queue) {
                job.join()
                if (isEmpty)
                    onFinished()
            }
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    fun submit(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        synchronized(block) {
            onStart()
            val job = scope.launch(context, CoroutineStart.LAZY, block)
            queue.trySend(job)
        }
    }

    fun cancel() {
        queue.cancel()
        scope.cancel()
    }
}
