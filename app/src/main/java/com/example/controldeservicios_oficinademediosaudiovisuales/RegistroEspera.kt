package com.example.controldeservicios_oficinademediosaudiovisuales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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

        var datos=""
        val docente:TextView=findViewById(R.id.docente_Nombre)

        db.collection("control_servicios")
            .get()
            .addOnSuccessListener { resultado->
                for (documento in resultado){
                    val nombre=documento["nombre_docente"].toString()
                    //datos+="${documento.id}: ${documento.data}\n"
                    datos+="${documento.id}: ${nombre}\n"
                }
                docente.text=datos
            }
            .addOnFailureListener{ exception ->
                docente.text="No se ha podido conectar"
            }
    }
}