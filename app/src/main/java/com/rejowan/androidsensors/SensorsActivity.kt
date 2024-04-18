package com.rejowan.androidsensors

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rejowan.androidsensors.databinding.ActivitySensorsBinding

class SensorsActivity : AppCompatActivity() {

    val binding : ActivitySensorsBinding by lazy {
        ActivitySensorsBinding.inflate(layoutInflater)
    }

    var sensor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sensor = intent.getStringExtra("sensor").toString()
        binding.title.text = sensor

    }



}