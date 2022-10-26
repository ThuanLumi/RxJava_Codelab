package com.jraska.rx.codelab

import io.reactivex.Observable
import org.junit.Test
import java.util.*

class Task1Basics {
  @Test
  fun dummyObservable() {
    // TODO:  Create Observable with single String value, subscribe to it and print it to console (Observable.just)
    val observable: Observable<String> = Observable.just("dasbthtnt")
    observable.subscribe { println(it.toString()) }
  }

  @Test
  fun methodIntoObservable() {
    // TODO:  Create Observable getting current time, subscribe to it and print value to console (Observable.fromCallable)
    val observable: Observable<Date> = Observable.fromCallable { Calendar.getInstance().time }
    observable.subscribe { println(it.toString()) }
  }

  @Test
  fun helloOperator() {
    // TODO:  Create Observable with ints 1 .. 10 subscribe to it and print only odd values (Observable.range, observable.filter)
    val observable: Observable<Int> = Observable.range(1, 10)
    observable.filter { it % 2 != 0 }
      .subscribe { println(it.toString()) }
  }

  @Test
  fun receivingError() {
    // TODO:  Create Observable which emits an error and print the console (Observable.error), subscribe with onError handling
    val observable: Observable<Int> = Observable.error(RuntimeException("This is an exception"))
    observable.subscribe({ println(it.toString()) }, { println(it.toString()) })
  }

  companion object {
    fun isOdd(value: Int): Boolean {
      return value % 2 == 1
    }
  }
}
