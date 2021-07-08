
package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


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
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val pos = intent.getStringExtra("pos").toString()


        menu?.findItem(R.id.Mostrar_correo)!!.setTitle(Html.fromHtml("<font color='#003485'>${pos.toString()}</font>"))

        //val positionOfMenuItem = 0 // or whatever...
        //inflater.inflate(android.R.menu.your_menu, menu)
        //val positionOfMenuItem = 0 // or whatever...
        //val item = menu!!.getItem(1)
        //val s = SpannableString("My red MenuItem")
        //s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
        //item.title = s

        return super.onCreateOptionsMenu(menu)
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flfragment, fragment)
            commit()
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        auth = FirebaseAuth.getInstance()

        when(item.itemId){
            R.id.logout_app -> {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.item_cuenta -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }
}