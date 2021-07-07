package com.example.controldeservicios_oficinademediosaudiovisuales

import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RegistroEspera : AppCompatActivity() {

    var s = null
    lateinit var dato_h_inico: com.google.firebase.Timestamp
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrio_espera)

        setSupportActionBar(findViewById(R.id.toolbar_registro))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        //val objetoIntent: Intent = intent
        val pos = intent.getStringExtra("pos").toString()
        //val mydataset = EsperaModelClass().id

        val docente:TextView=findViewById(R.id.docente_Nombre)
        val grupo:TextView=findViewById(R.id.grupo)
        val ala:TextView=findViewById(R.id.Ala)
        val sala:TextView=findViewById(R.id.sala_de_usos)
        val tecnico:TextView=findViewById(R.id.tecnico)
        val h_inicio:TextView=findViewById(R.id.hora_inicio)
        val actividad:TextView=findViewById(R.id.Actividad_atendida)
        //val datashow:TextView=findViewById(R.id.data_prestado)
        //val accesorios:TextView=findViewById(R.id.accesorios_prestados)
        val boton:Button=findViewById(R.id.Finalizar_registro)
        val texto:EditText=findViewById(R.id.editText_carneDocente1)
        val equipos_prestados=findViewById<AutoCompleteTextView>(R.id.prestados_auto)
        val accesorios_prestados = findViewById<AutoCompleteTextView>(R.id.accesorios_auto)

        var datos=""
        var equipos=""
        var base_carnet=""
        val edit_Observacion:EditText=findViewById(R.id.editTextTextMultiLine_observacion)

        val sdf = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.US)

            db.collection("control_servicios").document(pos).get().addOnSuccessListener {

                val nombre = it.getString("nombre_docente")
                val dato_grupo = it.getString("grupo")
                val dato_ala = it.getString("ala")
                val dato_tecnico = it.getString("email_tecnico")
                val carnet=it.getString("carne_docente")
                dato_h_inico = it.getTimestamp("hora_inicio") as com.google.firebase.Timestamp
                val fecha:String =sdf.format(dato_h_inico.toDate()).toString()
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
                h_inicio.text= fecha
                actividad.text=act_atendida
                //datashow.text=equipos

                val objetoAdapter = ArrayAdapter(this, R.layout.list_item_2, listaprestados)
                equipos_prestados.setAdapter(objetoAdapter)
                equipos_prestados.setText(listaprestados[0], false)

                val objetoAdapter2 = ArrayAdapter(this, R.layout.list_item_2, lista)
                accesorios_prestados.setAdapter(objetoAdapter2)
                accesorios_prestados.setText(lista[0], false)

                //accesorios.text = datos

                if(dato_sala==false){
                    sala.text="No"
                }
                else{
                    sala.text="Si"
                }
                //val millis: Long = cal.getTimeInMillis()

                base_carnet=carnet.toString()


            }

        texto.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(texto.text.toString().equals("")){
                    texto.setError("Campo vacio")
                }
                else
                {

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })


        boton.setOnClickListener {
            val tex =findViewById<AutoCompleteTextView>(R.id.editText_carneDocente1).text.toString()
            val auth = FirebaseAuth.getInstance().currentUser
            val myTimestampAsDate = Timestamp.now()

            if(base_carnet.toString().equals(tex.toString())){

                if(edit_Observacion.text.toString()!=""){
                    db.collection("control_servicios").document(pos).update("observacion", edit_Observacion.text.toString())
                }
                else{
                    db.collection("control_servicios").document(pos).update("observacion", "Sin Observación")
                }

                db.collection("control_servicios").document(pos)
                        .update("hora_final", myTimestampAsDate, "total_horas",Math.round(((myTimestampAsDate.seconds.toFloat()
                                - dato_h_inico.seconds.toFloat()) / 3600) * 100.0) / 100.0).addOnSuccessListener {

                }

                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Numero de trabajador incorrecto", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    /*

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_desplegable,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val correo:String=findViewById(R.id.correo)


        when(item.itemId){
            R.id.correo -> {}
            R.id.Cerrar -> {Toast.makeText(this,"cerrar sesion",Toast.LENGTH_SHORT).show()}

        }
        return super.onOptionsItemSelected(item)
    }

     */
}