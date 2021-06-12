package com.example.controldeservicios_oficinademediosaudiovisuales

import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class NuevosActivity : Fragment(R.layout.activity_nuevo_registro) {

    var elementos: MutableList<String> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_nuevo_registro, container, false)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Boton:Button=view.findViewById(R.id.AgregarBoton)
        val spinner: Spinner =view.findViewById(R.id.spinner)
        val spinnertexto: TextView =view.findViewById(R.id.spinner_texto)
        val listview:ListView=view.findViewById(R.id.listview)
        val NuevoRegistro:Button=view.findViewById(R.id.NuevoRegistro)


        val Objetos = arrayOf("Control remoto","Extensiones AC","Adaptador multiple","Cable de poder","Cable VGA","Cable USB","Cable Y",
            "Cargador de audio","Parlantes externos","Parlantes","Borrador","Lapices electronicos","Pantalla Smart","Tablero","Softwares",
            "Proyector smart","Cargador portatil","Cable HDMI")

        val objetoAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,Objetos)

      spinner.adapter=objetoAdapter

        spinner.onItemSelectedListener=object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnertexto.text=Objetos[position]
            }

        }
        Boton.setOnClickListener() {
            elementos.add(spinnertexto.text.toString())
            listview.adapter=ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,elementos)

        }

        NuevoRegistro.setOnClickListener() {

            val db = FirebaseFirestore.getInstance()

            val auth = FirebaseAuth.getInstance().currentUser

            //Guarda los datos en la base de datos
            db.collection("control_servicios").document().set(
                hashMapOf(
                    "ala" to obtenerAla(view.findViewById(R.id.radioGroup)),
                    "sala_usos_multiples" to obtenerCheckBox(view.findViewById(R.id.Sala_multiples)),
                    "grupo" to obtenerEditText(view.findViewById(R.id.editText_grupo)),
                    "data_show" to obtenerCheckBox(view.findViewById(R.id.checkBox_dataShow)),
                    "pc_portatil" to obtenerCheckBox(view.findViewById(R.id.checkBox_pc)),
                    "pizarra_smart" to obtenerCheckBox(view.findViewById(R.id.checkBox_pizarraSmart)),
                    "proyector_interactivo" to obtenerCheckBox(view.findViewById(R.id.checkBox_proyectorInteractivo)),
                    "accesorios" to elementos,
                    "tipo_actividad_atendida" to obtenerEditText(view.findViewById(R.id.editText_tipoActividad)),
                    "observacion" to view.findViewById<EditText>(R.id.editTextTextMultiLine_observacion).text.toString(),
                    "nombre_docente" to obtenerEditText(view.findViewById(R.id.editText_nombreDocente)),
                    "email_tecnico" to auth?.email.toString(),
                    "hora_inicio" to FieldValue.serverTimestamp()

                )
            )
        }
    }
    //Funcion para obtener la ala
    fun obtenerAla(radioGroupAla: RadioGroup):String{
        var radioId_ala = radioGroupAla.checkedRadioButtonId
        var ala = when {
            R.id.AlaA == radioId_ala -> "A"
            else -> "B"
        }
        return ala
    }

    //Funcion para obtener valor de los checkBox
    fun obtenerCheckBox(checkBoxAla: CheckBox): Boolean{
        var estado: Boolean = checkBoxAla.isChecked
        return estado
    }

    //obterner datos de los editText
    fun obtenerEditText(textInputEditText: TextInputEditText): String{
        return textInputEditText.text.toString()
    }


}