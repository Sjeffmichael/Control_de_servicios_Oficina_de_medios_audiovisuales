package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

class RegistroEspera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrio_espera)

        val objeto: Intent =intent
        var pos=objeto.getIntExtra("pos",1)
        val myDataset=Recursos().cargarFrases()
        val item=myDataset[pos]
        val content_descripcion=findViewById<TextView>(R.id.item_titulo)
        val titulo= resources.getString(item.stringResuerceId)

        content_descripcion.text= resources.getString(item.stringResuerceId)
    }
}