
package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.E

@Suppress("DEPRECATION")
class PrincipalActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_principal)
        setSupportActionBar(findViewById(R.id.toolbar_menuPrincipal))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottonNavigation:BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val fragmentoRegistros=EntregadosActivity()
        val fragmentoEspera=EsperaActivity()

        setCurrentFragment(fragmentoRegistros)

        bottonNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Registros -> setCurrentFragment(fragmentoRegistros)
                R.id.Registro_en_espera -> setCurrentFragment(fragmentoEspera)
            }
            true
        }

        //val Nuevos:Button=findViewById(R.id.Ingresar_Registros)
        //val Entregados:Button=findViewById(R.id.Ver_registros)
        //val Espera:Button=findViewById(R.id.Registros_espera)
        //val Logout: ImageButton = findViewById(R.id.imageButton_Usuario)

/*
        Nuevos.setOnClickListener{
            startActivity(Intent(this, NuevosActivity::class.java))
        }

        Entregados.setOnClickListener{
            startActivity(Intent(this, EntregadosActivity::class.java))
        }

        Espera.setOnClickListener{
            startActivity(Intent(this, EsperaActivity::class.java))
        }

        Logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cuenta, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flfragment,fragment)
            commit()
        }
}