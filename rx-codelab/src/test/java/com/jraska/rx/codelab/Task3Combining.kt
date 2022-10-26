package com.jraska.rx.codelab

import com.jraska.rx.codelab.http.*
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class Task3Combining {
  private val gitHubApi = HttpModule.mockedGitHubApi()

  @Before
  fun setUp() {
    RxLogging.enableObservableSubscribeLogging()
  }

  @Test
  fun zipWith_userWithRepos() {
    // TODO: Use gitHubApi to get user with `LOGIN` and his repos and print them. Use `GitHubConverter::convert` as zipper
    gitHubApi.getUser(LOGIN)
      .zipWith(gitHubApi.getRepos(LOGIN), GitHubConverter::convert)
      .subscribe { println(it) }
  }

  @Test
  fun startWith_userInCache() {
    // TODO: Get user with `LOGIN` and startWith a `UserCache.getUserSync(LOGIN)`, Subscribe and print both values
    gitHubApi.getUser(LOGIN)
      .map { GitHubConverter.convert(it) }
      .startWith(UserCache.getUser(LOGIN))
      .subscribe { println(it) }
  }

  @Test
  fun merge_userInCache() {
    // TODO: Get user `UserCache.getUserSync(LOGIN)` and mergeWith user with `LOGIN`, Subscribe and print both values
    UserCache.getUser(LOGIN)
      .mergeWith(
        gitHubApi.getUser(LOGIN).map { GitHubConverter.convert(it) }
      )
      .subscribe { println(it) }
  }

  @Test
  fun combineLatest_cachedUserWithRepos() {
    // TODO: Create observable of  `UserWithRepos` with `LOGIN` and use observable with cache from previous example - use Observable.combineLatest, GithubConverter::convert
    // TODO: Print the results - there should be two emission. Try to change order of passing into Observable.combineLatest - what happens?
    val observableUser = gitHubApi.getUser(LOGIN)
      .map { GitHubConverter.convert(it) }
      .startWith(UserCache.getUser(LOGIN))

    val observableListRepo = gitHubApi.getRepos(LOGIN)
      .map { GitHubConverter.convert(it) }

    observableListRepo.combineLatest(observableUser) { listRepo, user ->
      GitHubConverter.convert(listRepo, user)
    }
      .subscribe { println(it) }
  }

  companion object {
    private const val LOGIN = "defunkt"
  }
}
