 package com.example.controldeservicios_oficinademediosaudiovisuales

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.R.id.recycler_view_entregados
import com.example.controldeservicios_oficinademediosaudiovisuales.adapter.EntregaAdapter
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EntregaModelClass
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

 class EntregadosActivity : Fragment(R.layout.activity_entregados) {

        private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("control_servicios")
     lateinit var objetoAdapter2: ArrayAdapter<String>

     var prestamosAdapter: EntregaAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_entregados, container, false)

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //codigo de sheetdialog
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        val view1 = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        val Objetos = arrayOf("Lugar", "Equipo prestado", "Accesorios", "Tipo de actividad", "Nombre del docente", "Grupo", "Email del ténico")
        val lugar = arrayOf("Ala A", "Ala B", "Sala de usos multiples")
        val accesorios_lista = arrayOf("Control remoto","Extensiones AC","Adaptador multiple","Cable de poder","Cable VGA","Cable USB","Cable Y",
                "Cargador de audio","Parlantes externos","Parlantes","Borrador","Lapices electronicos","Pantalla Smart","Tablero","Softwares",
                "Proyector smart","Cargador portatil","Cable HDMI")
        val equipo_prestado = arrayOf("Data show", "Pizarra smart", "Pc portatil", "Proyector interactivo")

        val objetoAdapter = ArrayAdapter(requireContext() , R.layout.list_item_2,Objetos)
        val buscar_por = view1.findViewById<AutoCompleteTextView>(R.id.textView_accesorios_filtro)
        buscar_por.setAdapter(objetoAdapter)
        buscar_por.setText(Objetos[0], false)
        val lista_nombres: MutableList<String> = mutableListOf()
        val lista_tipoActividad: MutableList<String> = mutableListOf()
        val lista_grupo: MutableList<String> = mutableListOf()
        val lista_email: MutableList<String> = mutableListOf()

        val barra_buscador = view1.findViewById<AutoCompleteTextView>(R.id.textView_accesorios_buscador)
        setear_texto(lugar, barra_buscador, ArrayAdapter(requireContext(), R.layout.list_item_2, lugar))
        bottomSheetDialog.setContentView(view1)

        db.collection("control_servicios").whereNotEqualTo("hora_final", null).get().addOnSuccessListener{
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
                if(document.getString("email_tecnico") !in lista_email){
                    lista_email.add(document.getString("email_tecnico").toString())
                }
            }
        }

        //Calendario
        lista_tipoActividad.sorted()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var inicio = ""
        var final = ""

        setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null), inicio, final)

        //Boton de filtro
        view.findViewById<ImageButton>(R.id.imageButton_filtro).setOnClickListener{
            bottomSheetDialog.show()
        }

        //Boton reporte
        view.findViewById<ImageButton>(R.id.imageButton_reporte).setOnClickListener{
            startActivity(Intent(requireContext(), Activity_informes::class.java))
        }

        val f = DecimalFormat("00")
        //Boton para la fecha
        view1.findViewById<Button>(R.id.button_calendario).setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                inicio = "${f.format(mDay) }/${f.format(mMonth + 1)}/$mYear"
                final = "${f.format(mDay + 1) }/${f.format(mMonth + 1)}/$mYear"
                view1.findViewById<Button>(R.id.button_calendario).setText(inicio)
            }, year, month, day)
            datePicker.setButton(DialogInterface.BUTTON_NEUTRAL, "Quitar", DialogInterface.OnClickListener {
                dialog, which ->  view1.findViewById<Button>(R.id.button_calendario).setText("Fecha")
                inicio = ""
                final = ""
            })

            datePicker.show()
        }

        //Consultas de las busquedas
        view1.findViewById<ImageButton>(R.id.button_filtrarInfo).setOnClickListener{
            when{
                buscar_por.text.toString() == "Lugar" -> when{
                    barra_buscador.text.toString() == "Ala A" -> setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereEqualTo("ala", "A"), inicio, final)
                    barra_buscador.text.toString() == "Ala B" -> setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereEqualTo("ala", "B"), inicio, final)
                    else -> setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereEqualTo("sala_usos_multiples", true), inicio, final)
                }
                buscar_por.text.toString() == "Equipo prestado" -> setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereEqualTo(when{
                    barra_buscador.text.toString() == "Data show" -> "data_show"
                    barra_buscador.text.toString() == "Pizarra smart" -> "pizarra_smart"
                    barra_buscador.text.toString() == "Pc portatil" -> "pc_portatil"
                    else -> "proyector_interactivo" }, true), inicio, final)
                buscar_por.text.toString() == "Accesorios" -> setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereArrayContains("accesorios", barra_buscador.text.toString()), inicio, final)
                buscar_por.text.toString() == "Tipo de actividad" -> setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereEqualTo("tipo_actividad_atendida", barra_buscador.text.toString()), inicio, final)
                buscar_por.text.toString() == "Nombre del docente" -> setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereEqualTo("nombre_docente", barra_buscador.text.toString()), inicio, final)
                buscar_por.text.toString() == "Grupo" -> setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereEqualTo("grupo", barra_buscador.text.toString()), inicio, final)
                else ->  setUpRecyclerView(collectionReference.whereNotEqualTo("hora_final", null).whereEqualTo("email_tecnico", barra_buscador.text.toString()), inicio, final)
            }
            bottomSheetDialog.hide()
            onStart()
        }

        //Buscador de prestamos
        buscar_por.setOnItemClickListener { parent, view1, position, id ->
            when{
                buscar_por.text.toString() == "Lugar" -> setear_texto(lugar, barra_buscador, ArrayAdapter(requireContext(), R.layout.list_item_2, lugar))
                buscar_por.text.toString() == "Equipo prestado" -> setear_texto(equipo_prestado, barra_buscador, ArrayAdapter(requireContext(), R.layout.list_item_2, equipo_prestado))
                buscar_por.text.toString() == "Accesorios" -> setear_texto(accesorios_lista, barra_buscador, ArrayAdapter(requireContext(), R.layout.list_item_2, accesorios_lista))
                buscar_por.text.toString() == "Tipo de actividad" -> setear_texto(lista_tipoActividad.toTypedArray(), barra_buscador, ArrayAdapter(requireContext(), R.layout.list_item_2, lista_tipoActividad.toTypedArray()))
                buscar_por.text.toString() == "Nombre del docente" -> setear_texto(lista_nombres.toTypedArray(), barra_buscador, ArrayAdapter(requireContext(), R.layout.list_item_2, lista_nombres.toTypedArray()))
                buscar_por.text.toString() == "Grupo" -> setear_texto(lista_grupo.toTypedArray(), barra_buscador, ArrayAdapter(requireContext(), R.layout.list_item_2, lista_grupo.toTypedArray()))
                else -> setear_texto(lista_email.toTypedArray(), barra_buscador, ArrayAdapter(requireContext(), R.layout.list_item_2, lista_email.toTypedArray()))
            }
        }
    }

     //Funcion para setear el texto a la barra de busqueda
     @SuppressLint("InflateParams")
     fun setear_texto(lista: Array<String>, barra_buscador: AutoCompleteTextView, objetoAdapter: ArrayAdapter<String>){
         barra_buscador.setAdapter(objetoAdapter)
         barra_buscador.setText(lista[0], false)
     }

     //Función para elegir consulta con fecha o sin fecha
     @RequiresApi(Build.VERSION_CODES.N)
     fun elegir_consulta(query: Query, inicio: String, final: String): Query{
         val dateFormat = SimpleDateFormat("dd/MM/yyyy")
       var query2 =  when{
             inicio != "" && final != "" -> query.whereGreaterThanOrEqualTo("hora_final", Timestamp(dateFormat.parse(inicio))).whereLessThan("hora_final", Timestamp(dateFormat.parse(final))).orderBy("hora_final",  Query.Direction.DESCENDING)
            else -> query.orderBy("hora_final",  Query.Direction.DESCENDING)
         }
         return query2
     }

     //Funcion para hacer consultas
     @RequiresApi(Build.VERSION_CODES.N)
     fun setUpRecyclerView(query: Query, inicio: String, final: String){
             val firestoreRecyclerOption: FirestoreRecyclerOptions<EntregaModelClass> =  FirestoreRecyclerOptions.Builder<EntregaModelClass>()
                     .setQuery(elegir_consulta(query, inicio, final), object : SnapshotParser<EntregaModelClass> {
                         override fun parseSnapshot(snapshot: DocumentSnapshot): EntregaModelClass {
                             return snapshot.toObject(EntregaModelClass::class.java)!!.also {
                                 it.id = snapshot.id
                             }
                         }
                     })
                     .build()
             prestamosAdapter = EntregaAdapter(firestoreRecyclerOption)
             view?.findViewById<RecyclerView>(recycler_view_entregados)?.layoutManager = LinearLayoutManager(requireContext())
             view?.findViewById<RecyclerView>(recycler_view_entregados)?.adapter = prestamosAdapter
     }

     override fun onStart() {
         super.onStart()
         prestamosAdapter!!.startListening()
     }

     override fun onDestroy() {
         super.onDestroy()
         prestamosAdapter!!.stopListening()
     }
}