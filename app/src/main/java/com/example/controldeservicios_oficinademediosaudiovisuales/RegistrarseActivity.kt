package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class RegistrarseActivity : AppCompatActivity() {

    private lateinit var txtUsuario:TextInputEditText
    private lateinit var txtCorreo:TextInputEditText
    private lateinit var txtContrasena:TextInputEditText
    private lateinit var pbRegistrarse:ProgressBar
    private lateinit var dbReferences: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        txtUsuario = findViewById(R.id.editText_usario)
        txtContrasena = findViewById(R.id.editText_contrasena)
        txtCorreo = findViewById(R.id.editText_correo)
        txtUsuario = findViewById(R.id.editText_usario)

        pbRegistrarse = findViewById(R.id.progressBar)

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReferences = db.reference.child("Usuarios")


        /*val button_registrarse: Button = findViewById(R.id.button_registrarse)
        val id: TextInputEditText = findViewById(R.id.editText_correo)
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

        }*/
    }

    fun register(view: View){
        crearNuevaCuenta()
    }

    private fun crearNuevaCuenta(){
        val usuario:String = txtUsuario.text.toString()
        val correo:String = txtCorreo.text.toString()
        val contrasena:String = txtContrasena.text.toString()

        if(!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contrasena))
        {
            pbRegistrarse.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this) {
                    task ->

                    if(task.isComplete){
                        val user:FirebaseUser? = auth.currentUser
                        verificarEmail(user)

                        val userDB = user?.uid?.let { dbReferences.child(it) }

                        userDB?.child("Nombre")?.setValue(usuario)
                        //userDB.child("Contraseña").setValue(contrasena)
                        action()

                    }
                }
        }
    }

    private fun action(){
        startActivity(Intent(this, PrincipalActivity::class.java))
    }

    private fun verificarEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) {
                task ->
                if(task.isComplete){
                    Toast.makeText(this, "Registrado exitosamente", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "Error al registrarse", Toast.LENGTH_LONG).show()
                }

            }
    }
}