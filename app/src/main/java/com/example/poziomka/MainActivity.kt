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
    private lateinit var sensorManager: SensorManager
    private var animatedView: AnimatedView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)

        animatedView = AnimatedView(this)
        setContentView(animatedView)
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
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
    }

    class AnimatedView(context: Context) : View(context) {
        private val paint = Paint().apply { color = Color.GREEN }
        private val textPaint = Paint().apply {
            color = Color.BLUE
            textSize = 48f
            textAlign = Paint.Align.CENTER
        }

        private var x: Int = 0
        private var y: Int = 0

        private var prevX: Int = 0
        private var prevY: Int = 0

        private var height: Int = 0
        private var width: Int = 0

        companion object {
            private const val CIRCLE_RADIUS = 50
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            height = h
            width = w

            // Set initial position at the center
            x = width / 2
            y = height / 2

            prevX = x
            prevY = y
        }

        fun onSensorEvent(event: SensorEvent) {
            prevX = x
            prevY = y

            x -= event.values[1].toInt()
            y -= event.values[0].toInt()

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

            updateGravity(x, y)

            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            // Change color based on movement and boundary conditions
            if (x != prevX || y != prevY || x == CIRCLE_RADIUS || x == width - CIRCLE_RADIUS || y == CIRCLE_RADIUS || y == height - CIRCLE_RADIUS) {
                paint.color = Color.RED
            } else {
                paint.color = Color.GREEN
            }

            canvas.drawCircle(x.toFloat(), y.toFloat(), CIRCLE_RADIUS.toFloat(), paint)

            val centerX = width / 2.0f
            canvas.drawText("x: $x", centerX, 80.0f, textPaint)
            canvas.drawText("y: $y", centerX, 140.0f, textPaint)
        }

        private var gravity = arrayOf(0, 0)

        private fun updateGravity(x: Int, y: Int) {
            gravity = arrayOf(x, y)
        }
    }
}