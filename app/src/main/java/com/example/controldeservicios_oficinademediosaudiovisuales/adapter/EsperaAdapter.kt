package com.example.controldeservicios_oficinademediosaudiovisuales.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.R
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EsperaModelClass
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.util.*

class EsperaAdapter(options: FirestoreRecyclerOptions<EsperaModelClass>) :
    FirestoreRecyclerAdapter<EsperaModelClass, EsperaAdapter.EsperaAdapterVH>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EsperaAdapterVH {
        return EsperaAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.row_espera, parent, false))
    }

    override fun onBindViewHolder(holder: EsperaAdapterVH, position: Int, model: EsperaModelClass) {
        holder.nombre_docente.text = model.nombre_docente
        //holder.fecha.text = model.fecha
        holder.hora_inicio.text = model.hora_inicio?.toDate().toString()
    }

    class EsperaAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre_docente = itemView.findViewById<TextView>(R.id.nombre_docente)
        //var fecha = itemView.findViewById<TextView>(R.id.fecha)
        var hora_inicio = itemView.findViewById<TextView>(R.id.hora)
    }
}