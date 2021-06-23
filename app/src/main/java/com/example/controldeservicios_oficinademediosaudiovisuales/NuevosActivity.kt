package com.example.controldeservicios_oficinademediosaudiovisuales

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class NuevosActivity : AppCompatActivity() {

    var elementos: MutableList<String> = mutableListOf()

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_registro)

        setSupportActionBar(findViewById(R.id.toolbar_nuevos))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val Boton:ImageButton= findViewById(R.id.imageButton_add)

        //val spinnertexto: TextView =view.findViewById(R.id.spinner_texto)
        val listview:ListView=findViewById(R.id.listview)
        val NuevoRegistro:Button=findViewById(R.id.NuevoRegistro)
        val BotonE:Button=findViewById(R.id.eliminarBoton)
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val nombre_docente = findViewById<AutoCompleteTextView>(R.id.editText_nombreDocente)
        val tipo_activida = findViewById<AutoCompleteTextView>(R.id.editText_tipoActividad)
        val grupo = findViewById<AutoCompleteTextView>(R.id.editText_grupo)
        val lista_nombres: MutableList<String> = mutableListOf()
        val lista_tipoActividad: MutableList<String> = mutableListOf()
        val lista_grupo: MutableList<String> = mutableListOf()
        val accesorios = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)

        db.collection("control_servicios").get().addOnSuccessListener{
            for(document in it){
                if (document.getString("nombre_docente") !in lista_nombres){
                    lista_nombres.add(document.getString("nombre_docente").toString())
                }
                if(document.getString("tipo_actividad_atendida") !in lista_tipoActividad){
                    lista_tipoActividad.add(document.getString("tipo_actividad_atendida").toString())
                }
                if(document.getString("grupo") !in lista_grupo){
                    lista_grupo.add(document.getString("grupo").toString())
                }
            }
        }
        //Autocompletado del campo nombre del docente
        val adapter_nombre_docente = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, lista_nombres)
        nombre_docente.threshold = 0
        nombre_docente.setAdapter(adapter_nombre_docente)

        //Autocompletado del compo tipo actividad
        val adapter_tipoActividad = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, lista_tipoActividad)
        tipo_activida.threshold = 0
        tipo_activida.setAdapter(adapter_tipoActividad)

        //Autocompletado del campo grupo
        val adapter_grupo = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, lista_grupo)
        grupo.threshold = 0
        grupo.setAdapter(adapter_grupo)

        val Objetos = arrayOf("Control remoto","Extensiones AC","Adaptador multiple","Cable de poder","Cable VGA","Cable USB","Cable Y",
            "Cargador de audio","Parlantes externos","Parlantes","Borrador","Lapices electronicos","Pantalla Smart","Tablero","Softwares",
            "Proyector smart","Cargador portatil","Cable HDMI")

        val objetoAdapter = ArrayAdapter(this , R.layout.list_item_2,Objetos)
        accesorios.setAdapter(objetoAdapter)
        accesorios.setText(Objetos[0], false)


        Boton.setOnClickListener {
            val texto:String = accesorios.text.toString()//spinner.selectedItem.toString()
            var band =false

            for(item in elementos){
                if(item.equals(texto)){
                    band=true
                }
            }
            if(band==false){
                elementos.add(accesorios.text.toString())
            }
            else {
                Toast.makeText (this, "El elemento ya está en la lista" , Toast.LENGTH_SHORT).show()
            }
            listview.adapter=ArrayAdapter(this,R.layout.list_item,elementos)
        }

        BotonE.setOnClickListener {
            val texto:String = accesorios.text.toString()
            var band =false
            for(item in elementos){
                if(item.equals(texto)){
                    band=true
                }
            }
            if(band==true){
                elementos.remove(accesorios.text.toString())
                Toast.makeText (this, "Accesorio eliminado de la lista " , Toast.LENGTH_SHORT).show()
            }
            listview.adapter=ArrayAdapter(this,R.layout.list_item,elementos)
        }

        NuevoRegistro.setOnClickListener {

            val db = FirebaseFirestore.getInstance()

            val auth = FirebaseAuth.getInstance().currentUser

            //Guarda los datos en la base de datos
            db.collection("control_servicios").document().set(
                hashMapOf(
                    "ala" to obtenerAla(findViewById(R.id.radioGroup)),
                    "sala_usos_multiples" to obtenerCheckBox(findViewById(R.id.Sala_multiples)),
                    "data_show" to obtenerCheckBox(findViewById(R.id.checkBox_dataShow)),
                    "pc_portatil" to obtenerCheckBox(findViewById(R.id.checkBox_pc)),
                    "pizarra_smart" to obtenerCheckBox(findViewById(R.id.checkBox_pizarraSmart)),
                    "proyector_interactivo" to obtenerCheckBox(findViewById(R.id.checkBox_proyectorInteractivo)),
                    "accesorios" to elementos,
                    "tipo_actividad_atendida" to tipo_activida.text.toString(),
                    "grupo" to grupo.text.toString(),
                    "observacion" to findViewById<EditText>(R.id.editTextTextMultiLine_observacion).text.toString(),
                    "nombre_docente" to nombre_docente.text.toString(),
                    "carne_docente" to findViewById<AutoCompleteTextView>(R.id.editText_carneDocente).text.toString(),
                    "email_tecnico" to auth?.email.toString(),
                    "hora_inicio" to FieldValue.serverTimestamp(),
                    "hora_final" to null

                )
            )
            Toast.makeText (this, "Registro guardado" , Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    //Funcion para obtener la ala
    fun obtenerAla(radioGroupAla: RadioGroup):String{
        val radioId_ala = radioGroupAla.checkedRadioButtonId
        val ala = when {
            R.id.AlaA == radioId_ala -> "A"
            else -> "B"
        }
        return ala
    }

    //Funcion para obtener valor de los checkBox
    fun obtenerCheckBox(checkBoxAla: CheckBox): Boolean{
        val estado: Boolean = checkBoxAla.isChecked
        return estado
    }
}