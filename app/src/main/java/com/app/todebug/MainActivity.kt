package com.app.todebug

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var timeField: EditText
    lateinit var timeText: TextView
    lateinit var startButton: Button
    lateinit var stopButton: Button
    lateinit var setTimeButton: Button

    var currentTime: Int = 0
    var remainingTime: Int = 0
    var timer: CountDownTimer? = null
    var isCounting: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
    }

    private fun initListener() {
        startButton.setOnClickListener {
            if (!isCounting) {
                startCounter()
            } else {
                pauseCounter()
            }
            isCounting = !isCounting
        }
        stopButton.setOnClickListener {
            timer?.cancel()
            timer = null
            timeText.text = currentTime.toString()

            //set is counting to false when stop button pressed
            isCounting = false
            startButton.setText(R.string.start)
        }
        setTimeButton.setOnClickListener {
            setCounterActive(false)
        }
    }

    private fun pauseCounter() {
        timer?.cancel()
        startButton.setText(R.string.start)
    }

    private fun startCounter() {
        //Validating time field to avoiding fc when it empty
        if (timeField.text.toString().trim().isEmpty()){
            timeField.error = R.string.field_required.toString()
            timeField.requestFocus()
            return
        }

        currentTime = timeField.text.toString().toInt()
        timeText.text = currentTime.toString()
        setCounterActive(true)
        if (timer == null) {
            timer = object : CountDownTimer(currentTime.toLong() * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished.toInt()
                    timeText.text = "${+ remainingTime / 1000}"
                }

                override fun onFinish() {
                    timeText.text = currentTime.toString()
                    timer = null

                    //set is counting to false when time is up
                    isCounting = false
                    startButton.setText(R.string.start)
                }
            }
            timer?.start()
        } else {
            timer = object : CountDownTimer(remainingTime.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished.toInt()
                    timeText.text = "${+ (remainingTime / 1000) - 1}"
                }

                override fun onFinish() {
                    timeText.text = currentTime.toString()
                    timer = null

                    //set is counting to false when time is up
                    isCounting = false
                    startButton.setText(R.string.start)
                }
            }
            timer?.start()
        }
        startButton.setText(R.string.pause)
    }

    private fun setCounterActive(active: Boolean) {
        timeText.visibility = if (active) VISIBLE else GONE
        timeField.visibility = if (!active) VISIBLE else GONE
        if (!active) timeField.requestFocus()
    }

    private fun initView() {
        timeField = findViewById(R.id.textfield_time)
        timeText = findViewById(R.id.text_time)
        startButton = findViewById(R.id.button_start)
        stopButton = findViewById(R.id.button_stop)
        setTimeButton = findViewById(R.id.button_set_time)
    }
}