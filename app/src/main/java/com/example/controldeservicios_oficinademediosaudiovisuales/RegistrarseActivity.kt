package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.properties.Delegates


class RegistrarseActivity : AppCompatActivity() {

    private lateinit var txtUsuario:TextInputEditText
    private lateinit var txtCorreo:TextInputEditText
    private lateinit var txtContrasena:TextInputEditText
    private lateinit var txtconfirmar:TextInputEditText
    private lateinit var pbRegistrarse:ProgressBar
    private lateinit var dbReferences: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private var verificacion2 by Delegates.notNull<Boolean>()
    private var verificacion by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        txtUsuario = findViewById(R.id.editText_usario)
        txtContrasena = findViewById(R.id.editText_contrasena)
        txtCorreo = findViewById(R.id.editText_correo)
        txtUsuario = findViewById(R.id.editText_usario)
        txtconfirmar = findViewById(R.id.editText_confContrasena)

        pbRegistrarse = findViewById(R.id.progressBar)

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbReferences = db.reference.child("Usuarios")
    }

    fun register(view: View){
        crearNuevaCuenta()
    }

    private fun crearNuevaCuenta(){
        val usuario:String = txtUsuario.text.toString()
        val correo:String = txtCorreo.text.toString()
        val contrasena:String = txtContrasena.text.toString()
        val confirmar:String = txtconfirmar.text.toString()



        if(!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contrasena) && !TextUtils.isEmpty(confirmar)) {
            pbRegistrarse.visibility = View.VISIBLE

            if (contrasena == confirmar){
                    auth.createUserWithEmailAndPassword(correo, contrasena)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val user: FirebaseUser? = auth.currentUser
                                    verificarEmail(user)

                                    val userDB = user?.uid?.let { dbReferences.child(it) }

                                    userDB?.child("Nombre")?.setValue(usuario)
                                    //userDB.child("Contraseña").setValue(contrasena)
                                    action()

                                } else {
                                    if(contrasena.length < 6){
                                        txtContrasena.setError("Contraseña muy corta")
                                    }
                                    if(validarCorreo(correo)==false){
                                        txtCorreo.setError("Verifique su correo")
                                    }

                                    Toast.makeText(this, "Error al registrarse", Toast.LENGTH_LONG).show()
                                    pbRegistrarse.visibility = View.GONE
                                }
                            }
            }else{
                txtconfirmar.setError("Verifique su contraseña")
                pbRegistrarse.visibility = View.GONE
            }
        }
    }

    private fun action(){

        val usuario:String = txtUsuario.text.toString()

        val caracteres: CharArray = usuario.toCharArray()
        caracteres[0] = Character.toUpperCase(caracteres[0])
        val cadena = usuario.length.toInt()
        var i=0

        while(i<cadena-2){
            if(caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',')
            {
                caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1])
            }
            i++
        }

        var usuarioMod = String(caracteres)

        val correo:String = txtCorreo.text.toString()
        val db = FirebaseFirestore.getInstance()
        //agregar datos a documento Usuarios
        db.collection("usuarios").document(txtCorreo.text.toString()).set(
                hashMapOf(
                        //"nombre" to findViewById<TextInputEditText>(R.id.editText_usario).text.toString(),
                        "nombre" to usuarioMod.toString()
                        //"contraseña" to findViewById<TextInputEditText>(R.id.editText_contrasena).text.toString()
                )
        )

        val intent = Intent(this, PrincipalActivity::class.java)
        intent.putExtra("pos", correo)
        startActivity(intent)
        finish()
    }

    private fun verificarEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
                ?.addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Registrado exitosamente", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Error al registrarse", Toast.LENGTH_LONG).show()
                    }

                }
    }


    fun validarCorreo(correo: String):Boolean{
        var patron : Pattern = Pattern.compile(" ^ [_ A-Za-z0-9- \\ +] + ( \\ . [_ A-Za-z0-9 -] +) * @ [A-Za-z0-9 -] + ( \\ . [A-Za-z0-9] +) * ( \\ . [A-Za-z] {2,}) $ ")
        var comparador : Matcher = patron.matcher(correo)
        return comparador.find()
    }

    fun verificarEmailInFirebase(email: String?):Boolean {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val check = !task.result!!.signInMethods!!.isEmpty()
                        if (check) {
                            verificacion=true
                            Toast.makeText(applicationContext, "El email esta en uso", Toast.LENGTH_LONG).show()
                        } else {
                            verificacion=false
                            Toast.makeText(applicationContext, "El email no esta en uso, por ende el usuario no existe", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        return verificacion
    }
}