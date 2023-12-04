package com.example.stateprogress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.stateprogress.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        initStateProgressbar() }


    private fun initStateProgressbar() {
        val list = ArrayList<Int>()
        list.add(0)
        list.add(50)
        list.add(100)
        list.add(180)
        binding?.semiCircile?.setData(list)
        binding?.semiCircile?.setProgressBarColor(resources.getColor(R.color.blue))
        binding?.semiCircile?.setPercentWithAnimation(70)

    }
}