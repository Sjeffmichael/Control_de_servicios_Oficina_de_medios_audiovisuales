package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.controldeservicios_oficinademediosaudiovisuales.adapter.EsperaAdapter
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EsperaModelClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class RegistroEspera : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrio_espera)

        setSupportActionBar(findViewById(R.id.toolbar_registro))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        //val objetoIntent: Intent = intent
        val pos = intent.getStringExtra("pos").toString()
        //val mydataset = EsperaModelClass().id

        val docente:TextView=findViewById(R.id.docente_Nombre)
        val grupo:TextView=findViewById(R.id.grupo)
        val ala:TextView=findViewById(R.id.Ala)
        val tecnico:TextView=findViewById(R.id.tecnico)
        val observacion:TextView=findViewById(R.id.observacion)
        val h_inicio:TextView=findViewById(R.id.hora_inicio)
        val actividad:TextView=findViewById(R.id.Actividad_atendida)
        val datashow:TextView=findViewById(R.id.data_prestado)
        //val pizarra:TextView=findViewById(R.id.pizarra_prestado)
        //val pc:TextView=findViewById(R.id.pc_prestado)
        //val proyector:TextView=findViewById(R.id.proyector_prestado)
        val accesorios:TextView=findViewById(R.id.accesorios_prestados)
        var datos=""
        var equipos=""

        val sdf = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US)

            db.collection("control_servicios").document(pos).get().addOnSuccessListener {

                val nombre = it.getString("nombre_docente")
                val dato_grupo = it.getString("grupo")
                val dato_ala = it.getString("ala")
                val dato_tecnico = it.getString("email_tecnico")
                val dato_observa = it.getString("observacion")

                val dato_h_inico = it.getTimestamp("hora_inicio")
                val fecha:String =sdf.format(dato_h_inico?.toDate()).toString()

                val act_atendida=it.getString("tipo_actividad_atendida")

                val data = it.getBoolean("data_show")
                val pizar = it.getBoolean("pizarra_smart")
                val pc_prestada = it.getBoolean("pc_portatil")
                val proy_prestado = it.getBoolean("proyector_interactivo")

                var listaprestados: MutableList<String> = mutableListOf()

                if(data!=false){
                    listaprestados.add("Data show")
                }
                if(pizar!=false){
                    listaprestados.add("Pizarra smart")
                }
                if(pc_prestada!=false){
                    listaprestados.add("Pc portatil")
                }
                if(proy_prestado!=false){
                    listaprestados.add("Proyector interactivo")
                }

                var lista = arrayListOf<String>()
                lista = it.get("accesorios") as ArrayList<String>

                for(doc in lista){
                    datos+= "${doc}\n"
                }
                for(doc in listaprestados){
                    equipos+= "${doc}\n"
                }

                docente.text=nombre
                grupo.text=dato_grupo
                ala.text=dato_ala
                tecnico.text=dato_tecnico
                observacion.text=dato_observa
                h_inicio.text= fecha
                actividad.text=act_atendida
                datashow.text=equipos
                //pizarra.text=pizar.toString()
                //pc.text=pc_prestada.toString()
                //proyector.text=proy_prestado.toString()
                accesorios.text = datos
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}