package com.example.poziomka

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var accelerometer: Sensor
    private lateinit var accelerometerManager: SensorManager
    private var animatedView: AnimatedView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        accelerometerManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = accelerometerManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        accelerometerManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        animatedView = AnimatedView(this)
        setContentView(animatedView!!)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            animatedView?.onSensorEvent(event)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do something if the sensor accuracy changes
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

        companion object {
            private const val CIRCLE_RADIUS = 50
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            height = h
            width = w
        }

        fun onSensorEvent(event: SensorEvent) {
            x -= event.values[0].toInt()
            y -= event.values[1].toInt()

            if (x <= CIRCLE_RADIUS) {
                x = CIRCLE_RADIUS
            }
            if (x >= width - CIRCLE_RADIUS) {
                x = width - CIRCLE_RADIUS
            }
            if (y <= CIRCLE_RADIUS) {
                y = CIRCLE_RADIUS
            }
            if (y >= height - CIRCLE_RADIUS) {
                y = height - CIRCLE_RADIUS
            }

            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(Color.WHITE)
            canvas.drawCircle(x.toFloat(), y.toFloat(), CIRCLE_RADIUS.toFloat(), paint)
        }
    }
}