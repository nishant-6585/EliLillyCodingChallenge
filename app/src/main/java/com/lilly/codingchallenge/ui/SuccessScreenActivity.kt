package com.lilly.codingchallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lilly.codingchallenge.databinding.ActivitySuccessScreenBinding

class SuccessScreenActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySuccessScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDismiss.setOnClickListener {
            finish()
        }
    }

}