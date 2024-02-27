package com.example.alarmapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import com.example.alarmapp.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import java.util.Calendar
import kotlin.math.PI

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var alarmManager: MyAlarmManager
    private var calendar: Calendar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alarmManager = MyAlarmManager(this)
        createNotificationChannel()
        binding.btnTime.setOnClickListener {
            showTimePicker()
        }

        binding.btnAlarm.setOnClickListener {
            if (calendar!=null){
                alarmManager.setAlarm(calendar = calendar!!)
            }
        }

        binding.btnCancel.setOnClickListener {
            alarmManager.cancelAlarm()
        }


    }

    @SuppressLint("SuspiciousIndentation")
    private fun showTimePicker(){
        picker = MaterialTimePicker
            .Builder()
            .setTimeFormat(CLOCK_12H)
            .setHour(CLOCK_12H)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        picker.show(supportFragmentManager,"MyPicker")

        picker.addOnPositiveButtonClickListener{
            val hourStr = if (picker.hour < 10) "0${picker.hour}" else "${picker.hour}"
            val minuteStr = if (picker.minute < 10) "0${picker.minute}" else "${picker.minute}"

                if (picker.hour>12){
                    binding.tvTime.text = "${hourStr}:${minuteStr} PM"
                }else{
                    binding.tvTime.text = "${hourStr}:${minuteStr} PM"
                }
                calendar = Calendar.getInstance()
                calendar?.set(Calendar.HOUR_OF_DAY, picker.hour)
                calendar?.set(Calendar.MINUTE,picker.minute)
                calendar?.set(Calendar.SECOND,0)
                calendar?.set(Calendar.MILLISECOND,0)



        }


    }


    private fun createNotificationChannel(){
        val channel = NotificationChannel("MyNotificationChannel","Alarm Channel",NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }






}