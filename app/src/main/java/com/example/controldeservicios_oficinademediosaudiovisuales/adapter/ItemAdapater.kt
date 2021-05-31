package com.example.crearcuenta.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.controldeservicios_oficinademediosaudiovisuales.R
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.RegistroEspera
import com.example.controldeservicios_oficinademediosaudiovisuales.frases.Registros_en_espera

class ItemAdapater(
    private val context: Context,
    private val dataset:List<Registros_en_espera>
    ):RecyclerView.Adapter<ItemAdapater.ItemViewHolder>() {
    class ItemViewHolder(private val view: View):RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_titulo)
        /*val imageView: ImageView = view.findViewById(R.id.item_image)*/

        init {
            view.setOnClickListener() {v:View->
                var pos: Int = adapterPosition
                val intent = Intent(view.context, RegistroEspera::class.java)
                intent.putExtra("pos", pos)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout=LayoutInflater.from(parent.context)
                .inflate(R.layout.registrio_espera,parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount()=dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item=dataset[position]
        holder.textView.text=context.resources.getString(item.stringResuerceId)
        /*holder.imageView.setImageResource(item.imageResuerceId)*/
        /*holder.bindView(dataset[position],Listener)*/
    }
}