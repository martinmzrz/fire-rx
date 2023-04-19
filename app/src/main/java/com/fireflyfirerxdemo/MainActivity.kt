package com.fireflyfirerxdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firefly.fire_rx.FireCompletable.Companion.onSuccess
import com.firefly.fire_rx.FireDisposable.Companion.defaultSubscribe
import com.firefly.fire_rx.FireDisposable.Companion.onFailure
import com.firefly.fire_rx.FireFlowable.Companion.onSuccess
import com.firefly.fire_rx.FireMaybe.Companion.onSuccess
import com.firefly.fire_rx.FireObservable.Companion.onSuccess
import com.firefly.fire_rx.FireRx
import com.firefly.fire_rx.FireSingle.Companion.onSuccess
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val fireRx = FireRx()

    companion object {
        private const val LOG_TAG = "Fire-Rx"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Single.just("Hello Single").onSuccess { result ->
            Log.d(LOG_TAG, result)
        }.onFailure {
            Log.d(LOG_TAG, "code not executed")
        }.defaultSubscribe(fireRx)

        Single.error<String>(Exception("Your circuit's dead")).onSuccess {
            Log.d(LOG_TAG, "code not executed")
        }.onFailure {
            Log.e(LOG_TAG, "code executed", it)
        }.defaultSubscribe(fireRx)

        Completable.complete().onSuccess {
            Log.d(LOG_TAG, "Task completed")
        }.defaultSubscribe(fireRx)

        Flowable.interval(2, TimeUnit.SECONDS).onSuccess {
            Log.d(LOG_TAG, "Flowable -> $it")
        }.defaultSubscribe(fireRx)

        Observable.interval(2, TimeUnit.SECONDS).onSuccess {
            Log.d(LOG_TAG, "Observable -> $it")
        }.defaultSubscribe(fireRx)

        Maybe.just("Hello Maybe").onSuccess { result ->
            Log.d(LOG_TAG, result)
        }.defaultSubscribe(fireRx)
    }

    override fun onDestroy() {
        super.onDestroy()
        fireRx.dispose()
    }
}