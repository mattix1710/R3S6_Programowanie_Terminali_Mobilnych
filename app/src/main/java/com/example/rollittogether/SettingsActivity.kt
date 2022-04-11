package com.example.rollittogether

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class SettingsActivity : AppCompatActivity() {
    lateinit var gridView: GridView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        gridView = findViewById(R.id.cubePreview)
        gridView.adapter = CubeAdapter(this)

        gridView.setOnItemClickListener(OnItemClickListener { parent, v, position, id ->
            val tag: Int = v.getTag() as Int
            val bgId: Int = R.drawable.border
            if(tag != null && tag == bgId) {
                v.setBackgroundResource(R.drawable.border_inactive)
                v.setTag(R.drawable.border_inactive)
            }
            else{
                v.setBackgroundResource(R.drawable.border)
                v.setTag(R.drawable.border)
            }
        })
    }
}