package com.example.controldeservicios_oficinademediosaudiovisuales.adapter

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.R
import com.example.controldeservicios_oficinademediosaudiovisuales.RegistroEspera
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EsperaModelClass
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.security.AccessController.getContext
import java.util.*


class  EsperaAdapter(options: FirestoreRecyclerOptions<EsperaModelClass>) :
    FirestoreRecyclerAdapter<EsperaModelClass, EsperaAdapter.EsperaAdapterVH>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EsperaAdapterVH {
        return EsperaAdapterVH(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.row_espera,
                        parent,
                        false
                )
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EsperaAdapterVH, position: Int, model: EsperaModelClass) {
        val sdf = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US)

        holder.nombre_docente.text = model.nombre_docente
        holder.email_tecnico.text = model.email_tecnico
        holder.hora_inicio.text = sdf.format(model.hora_inicio?.toDate())
    }

    class EsperaAdapterVH(view: View) : RecyclerView.ViewHolder(view) {
        var nombre_docente = view.findViewById<TextView>(R.id.nombre_docente)
        var email_tecnico = view.findViewById<TextView>(R.id.email)
        var hora_inicio = view.findViewById<TextView>(R.id.fecha)

        init {
            itemView.setOnClickListener{ v: View ->
                /*val pos = adapterPosition
                val intent = Intent(requireContext(), RegistroEspera::class.java)
                //intent.putExtra("pos", pos)
                v.context.startActivity(intent)*/
            }
        }
    }
}