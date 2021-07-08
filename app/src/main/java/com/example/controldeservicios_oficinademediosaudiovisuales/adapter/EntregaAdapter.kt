package com.example.controldeservicios_oficinademediosaudiovisuales.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.R
import com.example.controldeservicios_oficinademediosaudiovisuales.RegistroFinalizado
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EntregaModelClass
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
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

    public fun runLayoutAnimation(recyclerView: RecyclerView?) {
        val context = recyclerView?.context
        val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        recyclerView?.layoutAnimation = animation
        recyclerView?.adapter?.notifyDataSetChanged()
        recyclerView?.scheduleLayoutAnimation()
    }

    private fun setZoomInAnimation(view: View, recyclerView: RecyclerView?) {
        val context = recyclerView?.context
        //val zoomIn = AnimationUtils.loadAnimation(view.findViewById<RecyclerView>(R.id.recycler_view_entregados), R.anim.layout_animation) // animation file
        //view.startAnimation(zoomIn)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EntregaAdapterVH, position: Int, model: EntregaModelClass) {
        val sdf = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.US)
        setAnimation(holder.itemView, position)
        holder.nombre_docente.text = model.nombre_docente
        holder.email_tecnico.text =  model.email_tecnico
        holder.hora_final.text = sdf.format(model.hora_final?.toDate())
        holder.id = model.id.toString()
    }

    class EntregaAdapterVH(view: View) : RecyclerView.ViewHolder(view) {
        var nombre_docente = view.findViewById<TextView>(R.id.nombre_docente_entrega)
        var email_tecnico = view.findViewById<TextView>(R.id.email_entrega)
        var hora_final = view.findViewById<TextView>(R.id.fecha_entrega)
        var borrar = view.findViewById<ImageButton>(R.id.imagen_borrar2)
        var id = ""


        init {
            itemView.setOnClickListener{ v: View ->
                val id2 = id
                val intent = Intent(view.context, RegistroFinalizado::class.java)
                intent.putExtra("pos", id2)
                v.context.startActivity(intent)

            }

            borrar.setOnClickListener {
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

                builder.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }

        }
    }
}