package com.jraska.rx.codelab

import com.jraska.rx.codelab.http.HttpBinApi
import com.jraska.rx.codelab.http.HttpModule
import com.jraska.rx.codelab.http.RequestInfo
import com.jraska.rx.codelab.server.RxServer
import com.jraska.rx.codelab.server.RxServerFactory
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class Task7HotObservables {

  private val rxServer: RxServer = RxServerFactory.create()
  private val httpBinApi: HttpBinApi = HttpModule.httpBinApi()

  @Before
  fun before() {
    RxLogging.enableObservableSubscribeLogging()
  }

  @Test
  fun coldObservable() {
    val getRequest = httpBinApi.getRequest()
      .subscribeOn(Schedulers.io())

    // TODO: Subscribe twice to getRequest and print its values, how many http requests it triggers?
    // TODO: Delay first subscription by 250 ms - delaySubscription()
    // TODO: Modify getRequest to be able to perform only one http request - share()
    getRequest.delaySubscription(250, TimeUnit.MILLISECONDS).subscribe { println(it) }
    getRequest.subscribe { println(it) }

    val newGetRequest = httpBinApi.getRequest()
      .share()
      .subscribeOn(Schedulers.io())

    newGetRequest.subscribe { println(it) }
    newGetRequest.subscribe { println(it) }
  }

  @Test
  fun hotObservable() {
    // TODO: Subscribe twice to rxServer.debugLogsHot and print the logs
    // TODO: Delay first subscription by 250ms - delaySubscription(), how is this different than cold observable
    val observableDebugLog = rxServer.debugLogsHot()

    observableDebugLog.delaySubscription(250, TimeUnit.MILLISECONDS).subscribe { println(it) }
    observableDebugLog.subscribe { println(it) }
  }

  @Test
  fun createHotObservableThroughSubject() {
    val getRequest = httpBinApi.getRequest()

    // TODO: Create a PublishSubject<RequestInfo> and subscribe twice to it with printing the result
    // TODO: Subscribe to getRequest and publish its values to subject
    val publishSubject = PublishSubject.create<RequestInfo>()
    publishSubject.subscribe { println(it) }
    publishSubject.subscribe { println(it) }

    getRequest
      .subscribeOn(Schedulers.io())
      .subscribe(
        { requestInfo -> publishSubject.onNext(requestInfo) },
        { throwable -> publishSubject.onError(throwable) }
      )
  }

  @After
  fun after() {
    Thread.sleep(500)
    HttpModule.awaitNetworkRequests()
  }
}
