package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.controldeservicios_oficinademediosaudiovisuales.adapter.EsperaAdapter
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EsperaModelClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RegistroEspera : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrio_espera)
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        //val objetoIntent: Intent = intent
        val pos = intent.getStringExtra("pos").toString()
        //val mydataset = EsperaModelClass().id


            db.collection("control_servicios").document(pos).get().addOnSuccessListener {
                val nombre = it.getString("nombre_docente")
                //Log.d("Datos doc: ", "$nombre")
                Toast.makeText (this, nombre , Toast.LENGTH_SHORT).show()
            }




        /*val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        var datos=""
        val docente:TextView=findViewById(R.id.docente_Nombre)


        var pos= intent.getStringExtra("pos").toString()
        Toast.makeText (this, pos.toString() , Toast.LENGTH_SHORT).show()

        db.collection("control_servicios")
            .get()
            .addOnSuccessListener { resultado->

                for (documento in resultado) {
                    val nombre = documento["nombre_docente"].toString()
                    //datos+="${documento.id}: ${documento.data}\n"
                    if(pos.toString() == nombre){
                        datos+="${documento.id}: ${nombre}\n"
                    }
                }
                docente.text=datos
            }

            .addOnFailureListener{ exception ->
                docente.text="No se ha podido conectar"
            }*/
    }
}