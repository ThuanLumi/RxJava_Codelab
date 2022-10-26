package com.jraska.rx.codelab

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

class Task6SingleCompletableMaybe {

  @Test
  fun helloSingle() {
    // TODO: Create Single emitting one item and subscribe to it printing onSuccess value,
    // TODO: Convert Single to completable and print message about completion.
    val single = Single.just("dsaabb")

    single.subscribe(Consumer { println(it) })

    single.ignoreElement().subscribe { println("Success") }
  }

  @Test
  fun maybe() {
    // TODO: Create a Single with one value to emit and convert it to maybe
    val single = Single.just("dnadnsadsa")

    single.toMaybe().subscribe { println(it) }
  }

  @Test
  fun transformObservableToCompletable() {
    // TODO: Create Observable emitting values 1 .. 10 and make it completable (ignoreElements), subscribe and print
    val observable = Observable.range(1, 10)

    observable.ignoreElements().subscribe { println("Success") }
  }

  @Test
  fun intervalRange_firstOrError_observableToSingle() {
    // TODO: Create Observable emitting 5 items each 10 ms (intervalRange)
    // TODO: Get first element (firstOrError)
    // TODO: Play around with skip operator, implement error handling for skip(5)
    val observable = Observable.intervalRange(0, 5, 0, 10, TimeUnit.MILLISECONDS)

    observable.firstOrError().subscribe(Consumer { println(it) })
  }

  @After
  fun after() {
    // to see easily time dependent operations, because we are in unit tests
    Thread.sleep(100)
  }
}
