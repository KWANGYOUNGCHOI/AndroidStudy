package com.kwang0.androidwisywig.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kwang0.androidwisywig.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputMain
    }
}
