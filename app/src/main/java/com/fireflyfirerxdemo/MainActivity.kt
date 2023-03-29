package com.fireflyfirerxdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firefly.fire_rx.FireCompletable.Companion.onSuccess
import com.firefly.fire_rx.FireRx
import com.firefly.fire_rx.FireSingle.Companion.onFailure
import com.firefly.fire_rx.FireSingle.Companion.onSuccess
import com.firefly.fire_rx.defaultSubscribe

import io.reactivex.Completable
import io.reactivex.Single

class MainActivity : AppCompatActivity() {
    private val fireRx = FireRx()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Single.just("Hello World").onSuccess { result ->
            Log.d("Fire-Rx", result)
        }.onFailure {
            Log.d("Fire-Rx", "code not executed")
        }.defaultSubscribe(fireRx)

        Single.error<String>(Exception("Your circuit's dead")).onSuccess {
            Log.d("Fire-Rx", "code not executed")
        }.onFailure {
            Log.e("Fire-Rx", "code executed", it)
        }.defaultSubscribe(fireRx)

        Completable.complete().onSuccess {
            Log.d("Fire-Rx", "Task completed")
        }.defaultSubscribe(fireRx)
    }

    override fun onDestroy() {
        super.onDestroy()
        fireRx.dispose()
    }
}