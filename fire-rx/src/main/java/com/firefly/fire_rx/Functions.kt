package com.firefly.fire_rx

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.onSuccess(successCallback: ((T) -> Unit)): FireSingle<T>{
    return FireSingle(this).apply {
        this.successCallback = successCallback
    }
}

fun <T> Single<T>.onComplete(onComplete: (T?, Throwable?) -> Unit): FireSingle<T>{
    return FireSingle(this).apply {
        this.onComplete = onComplete
    }
}

fun <T> Single<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireSingle<T>{
    return FireSingle(this).apply {
        this.failureCallback = failureCallback
    }
}

fun <T> FireSingle<T>.onSuccess(successCallback: ((T) -> Unit)): FireSingle<T>{
    return this.apply {
        this.successCallback = successCallback
    }
}

fun <T> FireSingle<T>.onFailure(failureCallback: ((Throwable) -> Unit)): FireSingle<T>{
    return this.apply {
        this.failureCallback = failureCallback
    }
}

fun Completable.onSuccess(successCallback: () -> Unit): FireCompletable{
    return FireCompletable(this).apply {
        this.successCallback = successCallback
    }
}

fun Completable.onFailure(failureCallback: (Throwable) -> Unit): FireCompletable {
    return FireCompletable(this).apply {
        this.failureCallback = failureCallback
    }
}

fun Completable.onComplete(onComplete: (Throwable?) -> Unit): FireCompletable {
    return FireCompletable(this).apply {
        this.onComplete = onComplete
    }
}

fun Completable.defaultSubscribe(rx: FireRx){
    rx.execute(FireCompletable(this), Schedulers.io(), AndroidSchedulers.mainThread())
}

fun FireCompletable.onSuccess(successCallback: () -> Unit): FireCompletable{
    return this.apply {
        this.successCallback = successCallback
    }
}

fun FireCompletable.onFailure(failureCallback: (Throwable) -> Unit): FireCompletable {
    return this.apply {
        this.failureCallback = failureCallback
    }
}

fun FireDisposable.defaultSubscribe(rx: FireRx){
    rx.execute(this, Schedulers.io(), AndroidSchedulers.mainThread())
}

fun <T> Single<T>.subscribeOnMain(): Single<T> {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.subscribeOnIO(): Single<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Single<T>.observeOnMain(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.subscribeOnIOAndObserveOnMain(): Single<T> {
    return this.subscribeOnIO()
        .observeOnMain()
}

fun <T> Single<T>.defaultSubscribe(onCallback: ((T?, Throwable?) -> Unit)): Disposable {
    return this.subscribeOnIOAndObserveOnMain()
        .subscribe(onCallback)
}

fun <T> Single<T>.onErrorResume(resumeFunction: (Throwable) -> T): Single<T> {
    return this.onErrorResumeNext {
        Single.just(resumeFunction.invoke(it))
    }
}

fun <T> Flowable<T>.subscribeOnIOAndObserveOnMain(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.subscribeOnIOAndObserveOnMain(): Maybe<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.subscribeOnIO(): Completable {
    return this.subscribeOn(Schedulers.io())
}

fun Completable.observeOnMain(): Completable {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun Completable.subscribeOnIOAndObserveOnMain(): Completable {
    return subscribeOnIO()
        .observeOnMain()
}

fun Completable.defaultSubscribe(onCallback: (Throwable?) -> Unit): Disposable {
    return this.subscribeOnIOAndObserveOnMain().subscribe({
        onCallback(null)
    }, { throwable ->
        onCallback(throwable)
    })
}

fun <T> Observable<T>.subscribeOnIO(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Observable<T>.observeOnMain(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.subscribeOnIOAndObserveOnMain(): Observable<T> {
    return this.subscribeOnIO()
        .observeOnMain()
}

fun Disposable?.disposeIfOpen() {
    if (this?.isDisposed == false) {
        this.dispose()
    }
}

fun Iterable<Disposable?>.disposeAll() {
    forEach { it.disposeIfOpen() }
}