package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.graphics.Color
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class NuevosActivity : Fragment(R.layout.activity_nuevo_registro) {

    val elementos = mutableListOf("")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_nuevo_registro, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Boton:Button=view.findViewById(R.id.AgregarBoton)
        val spinner: Spinner =view.findViewById(R.id.spinner)
        val spinnertexto: TextView =view.findViewById(R.id.spinner_texto)
        val listview:ListView=view.findViewById(R.id.listview)
        val NuevoRegistro:Button=view.findViewById(R.id.NuevoRegistro)


        val Objetos = arrayOf("Control remoto","Extensiones AC","Adaptador multiple","Cable de poder","Cable VGA","Cable USB","Cable Y",
            "Cargador de audio","Parlantes externos","Parlantes","Borrador","Lapices electronicos","Pantalla Smart","Tablero","Softwares",
            "Proyector smart","Cargador portatil","Cable HDMI")

        val objetoAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,Objetos)

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
            listview.adapter=ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,elementos)
        }

        NuevoRegistro.setOnClickListener() {
            val dataformat=SimpleDateFormat("dd/m/yyyy hh:mm:ss")
            val horaSalida = dataformat.format(Date())
        }
    }

}