package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.graphics.Color
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class NuevosActivity : AppCompatActivity() {

    val elementos = mutableListOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_registro)

        val Boton:Button=findViewById(R.id.AgregarBoton)
        val spinner: Spinner =findViewById(R.id.spinner)
        val spinnertexto: TextView =findViewById(R.id.spinner_texto)
        val listview:ListView=findViewById(R.id.listview)
        val NuevoRegistro:Button=findViewById(R.id.NuevoRegistro)


        val Objetos = arrayOf("Control remoto","Extensiones AC","Adaptador multiple","Cable de poder","Cable VGA","Cable USB","Cable Y",
            "Cargador de audio","Parlantes externos","Parlantes","Borrador","Lapices electronicos","Pantalla Smart","Tablero","Softwares",
            "Proyector smart","Cargador portatil","Cable HDMI")

        val objetoAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,Objetos)

        spinner.adapter=objetoAdapter

        spinner.onItemSelectedListener=object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnertexto.text=Objetos[position]
            }

        }
        Boton.setOnClickListener() {
            elementos.add(spinnertexto.text.toString())
            listview.adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,elementos)
        }

        NuevoRegistro.setOnClickListener() {
            val dataformat=SimpleDateFormat("dd/m/yyyy hh:mm:ss")
            val horaSalida = dataformat.format(Date())
        }
    }
}