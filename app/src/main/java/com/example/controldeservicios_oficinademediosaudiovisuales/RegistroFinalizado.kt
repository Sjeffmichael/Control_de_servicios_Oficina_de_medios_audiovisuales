package com.example.controldeservicios_oficinademediosaudiovisuales

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class RegistroFinalizado : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_entregados)

        setSupportActionBar(findViewById(R.id.toolbar_registro2))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        //val objetoIntent: Intent = intent
        val pos = intent.getStringExtra("pos").toString()
        //val mydataset = EsperaModelClass().id

        val docente:TextView=findViewById(R.id.Mdocente_Nombre)
        val grupo:TextView=findViewById(R.id.Mgrupo)
        val ala:TextView=findViewById(R.id.MAla)
        val sala:TextView=findViewById(R.id.Msala_de_usos)
        val tecnico:TextView=findViewById(R.id.Mtecnico)
        val observacion:TextView=findViewById(R.id.Mobservacion)
        val h_inicio:TextView=findViewById(R.id.Mhora_inicio)
        val actividad:TextView=findViewById(R.id.MActividad_atendida)
        val datashow:TextView=findViewById(R.id.Mdata_prestado)
        val accesorios:TextView=findViewById(R.id.Maccesorios_prestados)
        var datos=""
        var equipos=""
        var base_carnet=""

        val sdf = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US)

        db.collection("control_servicios").document(pos).get().addOnSuccessListener {

            val nombre = it.getString("nombre_docente")
            val dato_grupo = it.getString("grupo")
            val dato_ala = it.getString("ala")
            val dato_tecnico = it.getString("email_tecnico")
            val dato_observa = it.getString("observacion")
            val dato_h_inico = it.getTimestamp("hora_inicio")
            val fecha:String =sdf.format(dato_h_inico?.toDate()).toString()
            val act_atendida=it.getString("tipo_actividad_atendida")
            val data = it.getBoolean("data_show")
            val pizar = it.getBoolean("pizarra_smart")
            val pc_prestada = it.getBoolean("pc_portatil")
            val proy_prestado = it.getBoolean("proyector_interactivo")
            val dato_sala=it.getBoolean("sala_usos_multiples")
            var listaprestados: MutableList<String> = mutableListOf()

            if(data!=false){
                listaprestados.add("Data show")
            }
            if(pizar!=false){
                listaprestados.add("Pizarra smart")
            }
            if(pc_prestada!=false){
                listaprestados.add("Pc portatil")
            }
            if(proy_prestado!=false){
                listaprestados.add("Proyector interactivo")
            }

            var lista = arrayListOf<String>()
            lista = it.get("accesorios") as ArrayList<String>

            for(doc in lista){
                datos+= "${doc}\n"
            }
            for(doc in listaprestados){
                equipos+= "${doc}\n"
            }

            docente.text=nombre
            grupo.text=dato_grupo
            ala.text=dato_ala
            tecnico.text=dato_tecnico
            observacion.text=dato_observa
            h_inicio.text= fecha
            actividad.text=act_atendida
            datashow.text=equipos
            accesorios.text = datos

            if(dato_sala==false){
                sala.text="No"
            }
            else{
                sala.text="Sí"
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}