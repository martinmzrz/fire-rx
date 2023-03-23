package com.fireflyfirerxdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firefly.fire_rx.FireRx
import com.firefly.fire_rx.defaultSubscribe
import com.firefly.fire_rx.onSuccess
import io.reactivex.Single

class MainActivity : AppCompatActivity() {
    private val fireRx = FireRx()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Single.just("Hola mundo").onSuccess { result ->
            Log.d("Fire-Rx-Demo", result)
        }.defaultSubscribe(fireRx)
    }

    override fun onDestroy() {
        super.onDestroy()

        fireRx.dispose()
    }
}