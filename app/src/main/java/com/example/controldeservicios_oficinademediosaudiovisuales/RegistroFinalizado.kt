package com.example.controldeservicios_oficinademediosaudiovisuales

import android.icu.text.SimpleDateFormat
import android.nfc.Tag
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
import java.util.concurrent.TimeUnit


class RegistroFinalizado : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_entregados)

        setSupportActionBar(findViewById(R.id.toolbar_registro2))
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Préstamo"
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
        val h_final:TextView=findViewById(R.id.Mhora_entrega)
        val h_atendidas:TextView=findViewById(R.id.total_atendidas)
        val actividad:TextView=findViewById(R.id.MActividad_atendida)
        //val datashow:TextView=findViewById(R.id.Mdata_prestado)
        //val accesorios:TextView=findViewById(R.id.Maccesorios_prestados)
        val Mostrar_equipo=findViewById<AutoCompleteTextView>(R.id.Mprestados_auto)
        val Mostrar_accesorios = findViewById<AutoCompleteTextView>(R.id.Maccesorios_auto)
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
            val dato_h_entrega = it.getTimestamp("hora_final")

            var dato_h_atendidas= it.get("total_horas")
            var atendidas = dato_h_atendidas.toString()

            val fecha:String =sdf.format(dato_h_inico?.toDate()).toString()
            val fecha2:String=sdf.format(dato_h_entrega?.toDate()).toString()
            val act_atendida=it.getString("tipo_actividad_atendida")
            val data = it.getBoolean("data_show")
            val pizar = it.getBoolean("pizarra_smart")
            val pc_prestada = it.getBoolean("pc_portatil")
            val proy_prestado = it.getBoolean("proyector_interactivo")
            val dato_sala=it.getBoolean("sala_usos_multiples")
            var listaprestados: MutableList<String> = mutableListOf()

            if(atendidas.toFloat() < 1.0){
                var s = atendidas.toFloat()
                s = s * 60
                var valor = s.toInt()
                atendidas = "${valor} minutos"
            }
            else{
                atendidas = "${atendidas} hora"
            }

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
            h_final.text= fecha2
            h_atendidas.text = atendidas
            actividad.text=act_atendida
            //datashow.text=equipos
            //accesorios.text = datos

            val objetoAdapter = ArrayAdapter(this, R.layout.list_item_2, listaprestados)
            Mostrar_equipo.setAdapter(objetoAdapter)
            Mostrar_equipo.setText(listaprestados[0], false)

            val objetoAdapter2 = ArrayAdapter(this, R.layout.list_item_2, lista)
            Mostrar_accesorios.setAdapter(objetoAdapter2)
            Mostrar_accesorios.setText(lista[0], false)

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