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

class RegistroEspera : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrio_espera)
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
        val sdf = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US)

            db.collection("control_servicios").document(pos).get().addOnSuccessListener {
                val nombre = it.getString("nombre_docente")
                val dato_grupo = it.getString("grupo")
                val dato_ala = it.getString("ala")
                val dato_tecnico = it.getString("email_tecnico")
                val dato_observa = it.getString("observacion")
                val dato_h_inico = it.getTimestamp("hora_inicio")
                val fecha:String =sdf.format(dato_h_inico?.toDate()).toString()

                docente.text=nombre
                grupo.text=dato_grupo
                ala.text=dato_ala
                tecnico.text=dato_tecnico
                observacion.text=dato_observa
                h_inicio.text= fecha
                //Log.d("Datos doc: ", "$nombre")
                Toast.makeText (this, nombre , Toast.LENGTH_SHORT).show()
            }

        /*val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        var datos=""
        var dato_Grupo=""

        val docente:TextView=findViewById(R.id.docente_Nombre)
        val grupo:TextView=findViewById(R.id.grupo)

        var pos= intent.getStringExtra("pos").toString()
        Toast.makeText (this, pos.toString() , Toast.LENGTH_SHORT).show()

        db.collection("control_servicios")
            .get()
            .addOnSuccessListener { resultado->

                for (documento in resultado) {
                    val nombre = documento["nombre_docente"].toString()
                    val grupo = documento["grupo"].toString()
                    //datos+="${documento.id}: ${documento.data}\n"
                    if(pos.toString() == nombre){
                        datos+="${documento.id}: ${nombre}\n"
                        dato_Grupo+="${grupo}"
                    }
                }
                docente.text=datos
                grupo.text=dato_Grupo
            }

            .addOnFailureListener{ exception ->
                docente.text="No se ha podido conectar"
            }*/
    }
}