package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUsuario: TextInputEditText
    private lateinit var txtContrasena: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val textView_registrarse: TextView = findViewById(R.id.textView_registrarse)
        val Ingresar_Boton: Button = findViewById(R.id.Acceder)
        txtUsuario = findViewById(R.id.editText_correoLogin)
        txtContrasena = findViewById(R.id.editText_contrasenaLogin)
        progressBar = findViewById(R.id.progressBarLogin)
        auth = FirebaseAuth.getInstance()



        textView_registrarse.setOnClickListener{
            textView_registrarse.setTextColor(Color.parseColor("#FF0000"))
            startActivity(Intent(this, RegistrarseActivity::class.java))
        }

        Ingresar_Boton.setOnClickListener{
            loginUser()

        }

    }
    private fun loginUser(){
        val usuario:String = txtUsuario.text.toString()
        val contrasena:String = txtContrasena.text.toString()

        if(!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(contrasena)){
            progressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(usuario, contrasena)
                .addOnCompleteListener(this) {
                    task ->

                    if(task.isSuccessful){
                        action()
                    }
                    else
                    {
                        Toast.makeText(this, "No se pudo iniciar seción", Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.GONE
                    }
                }
        }
    }
    private fun action(){
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }
    //Cambiar color a negro del textView Registrarse cada que se abre el activity
    override fun onStart() {
        super.onStart()
        val textView_registrarse: TextView = findViewById(R.id.textView_registrarse)
        textView_registrarse.setTextColor(Color.parseColor("#000000"))
        progressBar.visibility = View.GONE
    }

}