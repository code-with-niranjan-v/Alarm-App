package com.example.alarmapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.alarmapp.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import java.util.Calendar
import kotlin.math.PI
import android.Manifest;
import android.app.Instrumentation.ActivityResult
import androidx.activity.result.ActivityResultLauncher

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

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()


            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        askNotificationPermission(requestPermissionLauncher)




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




    private fun askNotificationPermission(requestPermissionLauncher:ActivityResultLauncher<String>) {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Notification permission already granted", Toast.LENGTH_SHORT).show()
            } else {

                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }



}