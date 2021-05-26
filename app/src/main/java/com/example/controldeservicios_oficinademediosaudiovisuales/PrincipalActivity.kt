package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlin.math.E

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val Nuevos:Button=findViewById(R.id.Ingresar_Registros)
        val Entregados:Button=findViewById(R.id.Ver_registros)
        val Espera:Button=findViewById(R.id.Registros_espera)

        Nuevos.setOnClickListener{
            startActivity(Intent(this, NuevosActivity::class.java))
        }

        Entregados.setOnClickListener{
            startActivity(Intent(this, EntregadosActivity::class.java))
        }

        Espera.setOnClickListener{
            startActivity(Intent(this, EsperaActivity::class.java))
        }
    }
}