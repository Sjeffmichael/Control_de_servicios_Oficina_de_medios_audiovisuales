package com.example.controldeservicios_oficinademediosaudiovisuales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.crearcuenta.adapter.ItemAdapater

class EsperaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registros_espera)

        val myDataset= Recursos().cargarFrases()
        val recyclerView= findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter= ItemAdapater(this,myDataset)
        recyclerView.setHasFixedSize(true)
    }
}