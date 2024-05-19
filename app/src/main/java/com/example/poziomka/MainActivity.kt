package com.example.poziomka

import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.ActivityId
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var accelerometer: Sensor
    private lateinit var accelerometerManager: SensorManager
//
private var animatedView: AnimatedView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        val accelerometerManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = accelerometerManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometerManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        animatedView = AnimatedView(this)
        setContentView(animatedView!!)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            animatedView?.onSensorEvent(event)
        }
        TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        super.onPause()
        accelerometerManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        accelerometerManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    class AnimatedView(context: Context) : View(context) {
        private val paint = Paint().apply { color = Color.GREEN }
        private val paint1 = Paint().apply { color = Color.RED }

        private var x: Int = 0
        private var y: Int = 0
        private var z: Int = 0

        private var height: Int = 0
        private var width: Int = 0

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            height = h
            width = w
        }

        fun onSensorEvent(event: SensorEvent) {
            x = x - event.values[0].toInt()
            y = y - event.values[1].toInt()

            if (x <= 0 + CIRCLE_RADIUS) {
                x = 0 + CIRCLE_RADIUS
            }
            if (x >= 0 - CIRCLE_RADIUS) {
                x = 0 - CIRCLE_RADIUS
            }
            if (y <= 0 + CIRCLE_RADIUS) {
                y = 0 + CIRCLE_RADIUS
            }
            if (y >= height - CIRCLE_RADIUS) {
                y = height - CIRCLE_RADIUS
            }
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(x, y, CIRCLERADIUS, paint)
            invalidate()
        }

        private fun invalidate() {
            TODO("Not yet implemented")
        }
    }

    private fun Any.onDraw(canvas: Canvas) {
        TODO("Not yet implemented")
    }
