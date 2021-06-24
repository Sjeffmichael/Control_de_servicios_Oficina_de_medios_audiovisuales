 package com.example.controldeservicios_oficinademediosaudiovisuales

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.adapter.EntregaAdapter
import com.example.controldeservicios_oficinademediosaudiovisuales.adapter.EsperaAdapter
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EntregaModelClass
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EsperaModelClass
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

 class EntregadosActivity : Fragment(R.layout.activity_entregados) {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("control_servicios")

     var prestamosAdapter: EntregaAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_entregados, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
    }

     @SuppressLint("CutPasteId")
     fun setUpRecyclerView(){
         val query: Query = collectionReference.whereNotEqualTo("hora_final", null)
         val firestoreRecyclerOption: FirestoreRecyclerOptions<EntregaModelClass> =  FirestoreRecyclerOptions.Builder<EntregaModelClass>()
                 .setQuery(query.orderBy("hora_final",  Query.Direction.DESCENDING), object : SnapshotParser<EntregaModelClass> {
                     override fun parseSnapshot(snapshot: DocumentSnapshot): EntregaModelClass {
                         return snapshot.toObject(EntregaModelClass::class.java)!!.also {
                             it.id = snapshot.id
                         }
                     }
                 })
                 .build()

         prestamosAdapter = EntregaAdapter(firestoreRecyclerOption)
         view?.findViewById<RecyclerView>(R.id.recycler_view_entregados)?.layoutManager = LinearLayoutManager(requireContext())
         view?.findViewById<RecyclerView>(R.id.recycler_view_entregados)?.adapter = prestamosAdapter
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