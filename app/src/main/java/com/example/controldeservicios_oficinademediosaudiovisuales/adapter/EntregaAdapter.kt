package com.example.controldeservicios_oficinademediosaudiovisuales.adapter

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.R
import com.example.controldeservicios_oficinademediosaudiovisuales.RegistroEspera
import com.example.controldeservicios_oficinademediosaudiovisuales.RegistroFinalizado
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EntregaModelClass
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EsperaModelClass
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.util.*

class EntregaAdapter(options: FirestoreRecyclerOptions<EntregaModelClass>) :
        FirestoreRecyclerAdapter<EntregaModelClass, EntregaAdapter.EntregaAdapterVH>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntregaAdapterVH {
        return EntregaAdapterVH(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.row_entrega,
                        parent,
                        false
                )
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EntregaAdapterVH, position: Int, model: EntregaModelClass) {
        val sdf = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.US)


        holder.nombre_docente.text = model.nombre_docente
        holder.email_tecnico.text =  model.email_tecnico
        holder.hora_final.text = sdf.format(model.hora_final?.toDate())
        holder.id = model.id.toString()
    }

    class EntregaAdapterVH(view: View) : RecyclerView.ViewHolder(view) {
        var nombre_docente = view.findViewById<TextView>(R.id.nombre_docente_entrega)
        var email_tecnico = view.findViewById<TextView>(R.id.email_entrega)
        var hora_final = view.findViewById<TextView>(R.id.fecha_entrega)
        var id = ""


        init {
            itemView.setOnClickListener{ v: View ->
                val id2 = id
                val intent = Intent(view.context, RegistroFinalizado::class.java)
                intent.putExtra("pos",id2)
                v.context.startActivity(intent)

            }
        }

    }
}