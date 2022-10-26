package com.jraska.rx.codelab

import com.jraska.rx.codelab.server.Log
import com.jraska.rx.codelab.server.RxServer
import com.jraska.rx.codelab.server.RxServerFactory
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Test

class Task10Backpressure {
  private val rxServer: RxServer = RxServerFactory.create()

  @Test
  fun backpressureFail() {
    // TODO: Subscribe to rxServer.allLogsHot on different thread (observeOn), use reallySlowLogConsumer
    rxServer.allLogsHot()
      .observeOn(Schedulers.newThread())
      .subscribe(reallySlowLogConsumer())
  }

  @Test
  fun noBackpressure() {
    // TODO: Modify example above to ignore backpressure and continue forever (toObservable())
    rxServer.allLogsHot()
      .toObservable()
      .observeOn(Schedulers.newThread())
      .subscribe(reallySlowLogConsumer())
  }

  @Test
  fun onBackpressureDrop() {
    // TODO: Drop values on backpressure with logging which values are dropped (onBackpressureDrop), use slowLogConsumer
    rxServer.allLogsHot()
      .onBackpressureDrop { println("Drop $it") }
      .observeOn(Schedulers.newThread())
      .subscribe(slowLogConsumer())
  }

  @Test
  fun buffer_backpressureBatching() {
    // TODO: batch values and process them with batchLogsConsumer()
    // TODO: Experiment with different sizes of buffer
    rxServer.allLogsHot()
      .buffer(7)
      .observeOn(Schedulers.newThread())
      .subscribe(batchLogsConsumer())
  }

  @Test
  fun onBackpressureBuffer() {
    // TODO: Try different sizes of backpressure buffer to better understand how internal buffers work
    rxServer.allLogsHot()
      .onBackpressureBuffer(512)
      .observeOn(Schedulers.newThread())
      .subscribe(slowLogConsumer())
  }

  private fun slowLogConsumer(): Consumer<Log> {
    return Consumer { log ->
      Thread.sleep(25)
      println(log)
    }
  }

  private fun reallySlowLogConsumer(): Consumer<Log> {
    return Consumer { log ->
      Thread.sleep(100)
      println(log)
    }
  }

  private fun batchLogsConsumer(): Consumer<List<Log>> {
    return Consumer { logs ->
      Thread.sleep(100)
      println(logs)
    }
  }

  @After
  fun after() {
    Thread.sleep(3000)
  }
}
