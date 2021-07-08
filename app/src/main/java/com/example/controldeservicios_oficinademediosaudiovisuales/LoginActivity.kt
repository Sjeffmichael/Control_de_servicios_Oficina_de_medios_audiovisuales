package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var txtUsuario: TextInputEditText
    private lateinit var txtContrasena: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.tema_personalizado)
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
                .addOnCompleteListener(this) { task ->

                    if(task.isSuccessful){
                        action()
                    }
                    else
                    {
                        Toast.makeText(this, "No se pudo iniciar seción", Toast.LENGTH_SHORT).show()
                        verificarEmailInFirebase(usuario)
                        progressBar.visibility = View.GONE
                    }
                }
        }
    }
    private fun action(){
        val usuario:String = txtUsuario.text.toString()
        val intent = Intent(this, PrincipalActivity::class.java)
        intent.putExtra("pos",usuario)
        startActivity(intent)
        //startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }
    //Cambiar color a negro del textView Registrarse cada que se abre el activity
    override fun onStart() {
        super.onStart()
        val textView_registrarse: TextView = findViewById(R.id.textView_registrarse)
        textView_registrarse.setTextColor(Color.parseColor("#000000"))
        progressBar.visibility = View.GONE
    }

    fun verificarEmailInFirebase(email: String?) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val check = !task.result!!.signInMethods!!.isEmpty()
                        if (check) {
                            //Toast.makeText(applicationContext, "El email esta en uso", Toast.LENGTH_LONG).show()
                            txtContrasena.setError("Contraseña incorrecta")
                        } else {
                            //Toast.makeText(applicationContext, "El email no esta en uso, por ende el usuario no existe", Toast.LENGTH_LONG).show()
                            txtUsuario.setError("Verifique su correo")
                        }
                    }
                }
    }

}