package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val textView_registrarse: TextView = findViewById(R.id.textView_registrarse)
        textView_registrarse.setOnClickListener{
            textView_registrarse.setTextColor(Color.parseColor("#FF0000"))
            startActivity(Intent(this, RegistrarseActivity::class.java))
        }

    }
}