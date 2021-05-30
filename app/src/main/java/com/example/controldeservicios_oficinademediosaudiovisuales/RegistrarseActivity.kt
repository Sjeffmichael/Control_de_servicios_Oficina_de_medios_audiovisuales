package com.example.controldeservicios_oficinademediosaudiovisuales

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore


class RegistrarseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val button_registrarse: Button = findViewById(R.id.button_registrarse)
        val id: TextInputEditText = findViewById(R.id.editText_id)
        val usuario: TextInputEditText = findViewById(R.id.editText_usario)
        val conatrasena: TextInputEditText = findViewById(R.id.editText_contrasena)
        val confContrasena: TextInputEditText = findViewById(R.id.editText_confContrasena)
        button_registrarse.setOnClickListener(){
            db.collection("usuarios").document(id.text.toString()).set(
                hashMapOf(
                    "nombre" to findViewById<TextInputEditText>(R.id.editText_usario).text.toString(),
                    "contraseña" to findViewById<TextInputEditText>(R.id.editText_contrasena).text.toString()
                )
            )

        }
    }
}