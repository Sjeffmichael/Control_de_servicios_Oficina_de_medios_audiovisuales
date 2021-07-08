package com.example.controldeservicios_oficinademediosaudiovisuales.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.nfc.Tag
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.R
import com.example.controldeservicios_oficinademediosaudiovisuales.RegistroEspera
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EsperaModelClass
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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

    /*override fun populateViewHolder(viewHolder: EsperaAdapterVH, model: EsperaModelClass, position: Int) {
        if(model.isRead) {
            viewholder.updateBackground()
        }
    }*/

    var mLastPosition = -1

    fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > mLastPosition) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration = 500//Random().nextInt(501).toLong() //to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim)
            mLastPosition = position
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EsperaAdapterVH, position: Int, model: EsperaModelClass) {
        val sdf = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.US)
        setAnimation(holder.itemView, position)
        holder.nombre_docente.text = model.nombre_docente
        holder.email_tecnico.text =  model.email_tecnico
        holder.hora_inicio.text = sdf.format(model.hora_inicio?.toDate())
        holder.id = model.id.toString()
    }

    class EsperaAdapterVH(view: View) : RecyclerView.ViewHolder(view) {
        var nombre_docente = view.findViewById<TextView>(R.id.nombre_docente)
        var email_tecnico = view.findViewById<TextView>(R.id.email)
        var hora_inicio = view.findViewById<TextView>(R.id.fecha)
        var id = ""


        init {
            itemView.setOnClickListener{ v: View ->
                val id2 = id
                val intent = Intent(view.context, RegistroEspera::class.java)
                intent.putExtra("pos",id2)
                v.context.startActivity(intent)

            }

            //borrar registro de la base de datos

            itemView.setOnLongClickListener{ v:View->
                val id2 = id

                val db = FirebaseFirestore.getInstance()

                val builder = AlertDialog.Builder(view.context)
                builder.setTitle("¿Estás seguro?")
                builder.setMessage("¿Quieres eliminar el registro?")
                builder.setPositiveButton("Si") { dialogInterface: DialogInterface, i: Int ->

                    db.collection("control_servicios").document(id2)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(view.context, "Eliminado", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(view.context, "fallo al eliminar", Toast.LENGTH_SHORT).show()
                            }

                }

                builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int -> })
                builder.show()

                return@setOnLongClickListener true
            }
        }

    }
}