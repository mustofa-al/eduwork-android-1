package com.app.todebug

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
            val setTime: EditText = findViewById(R.id.textfield_time)
            val fieldsetTime: String = setTime.text.toString()
            if(fieldsetTime.isEmpty()){
                setTime.error = "Required"
                Toast.makeText(applicationContext, "Time to Count Required ", Toast.LENGTH_SHORT).show()
            }else{
            timer?.cancel()
            timer = null
            timeText.text = currentTime.toString()

            //1
            isCounting = false
            startButton.text = "start"
            }
        }
        setTimeButton.setOnClickListener {
            val setTime: EditText = findViewById(R.id.textfield_time)
            val fieldsetTime: String = setTime.text.toString()
            if(fieldsetTime.isEmpty()){
                setTime.error = "Required"
                Toast.makeText(applicationContext, "Time to Count Required ", Toast.LENGTH_SHORT).show()
            }else{
                setCounterActive(false)
            }
        }
    }

    private fun pauseCounter() {
        timer?.cancel()
        startButton.text = "Start"
    }

    private fun startCounter() {
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
                }
            }
            timer?.start()
        }
        startButton.text = "Pause"
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