package com.example.controldeservicios_oficinademediosaudiovisuales

import com.example.controldeservicios_oficinademediosaudiovisuales.R
import com.example.controldeservicios_oficinademediosaudiovisuales.frases.Registros_en_espera

class Recursos {
    fun cargarFrases():List<Registros_en_espera>{
        return listOf<Registros_en_espera>(
                Registros_en_espera(R.string.Probando1),
                Registros_en_espera(R.string.Probando2)
        )
    }
}