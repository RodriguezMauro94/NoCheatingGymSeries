package com.maurosergiorodriguez.nocheatinggymseries

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton

class CounterOverlayService: Service() {
    private lateinit var overlayView: ViewGroup
    private lateinit var overlayLayoutParams: WindowManager.LayoutParams
    private var layoutType: Int? =null
    private lateinit var windowManager: WindowManager

    private lateinit var counterButton: Button
    private lateinit var restartButton: ImageButton
    private lateinit var minusButton: ImageButton

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.counter_overlay,null) as ViewGroup

        layoutType = if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else WindowManager.LayoutParams.TYPE_TOAST

        overlayLayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSPARENT
        )

        overlayLayoutParams.gravity = Gravity.TOP or Gravity.START
        overlayLayoutParams.x = 0
        overlayLayoutParams.y = 0

        //Views init
        counterButton = overlayView.findViewById(R.id.series_conter_button)
        restartButton = overlayView.findViewById(R.id.restart_button)
        minusButton = overlayView.findViewById(R.id.minus_button)

        counterButton.setOnClickListener {
            val actual: Int = counterButton.text.toString().toInt()
            if (actual < 99) {
                counterButton.text = "${actual + 1}"
            }
        }

        restartButton.setOnClickListener {
            counterButton.setText(R.string.series_initial_value)
        }

        minusButton.setOnClickListener {
            val actual: Int = counterButton.text.toString().toInt()
            if (actual > 0) {
                counterButton.text = "${actual - 1}"
            }
        }

        windowManager.addView(overlayView, overlayLayoutParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayView)
    }
}