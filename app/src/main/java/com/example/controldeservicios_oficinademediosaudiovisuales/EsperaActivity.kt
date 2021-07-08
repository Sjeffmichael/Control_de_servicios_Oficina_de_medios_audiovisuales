package com.example.controldeservicios_oficinademediosaudiovisuales

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeservicios_oficinademediosaudiovisuales.R.id.recycler_view
import com.example.controldeservicios_oficinademediosaudiovisuales.adapter.EsperaAdapter
import com.example.controldeservicios_oficinademediosaudiovisuales.datos.EsperaModelClass
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.api.Quota
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class   EsperaActivity : Fragment(R.layout.activity_registros_espera) {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("control_servicios")

    var esperaAdapter: EsperaAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_registros_espera, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton_add).setOnClickListener(){
            startActivity(Intent(requireContext(), NuevosActivity::class.java))
        }
    }

    fun setUpRecyclerView(){
        val query: Query = collectionReference.whereEqualTo("hora_final", null)
        val firestoreRecyclerOption: FirestoreRecyclerOptions<EsperaModelClass> =  FirestoreRecyclerOptions.Builder<EsperaModelClass>()
                .setQuery(query.orderBy("hora_inicio", Query.Direction.DESCENDING),object : SnapshotParser<EsperaModelClass> {
                    override fun parseSnapshot(snapshot: DocumentSnapshot): EsperaModelClass {
                        return snapshot.toObject(EsperaModelClass::class.java)!!.also {
                            it.id = snapshot.id
                        }
                    }
                })
                .build()
        esperaAdapter = EsperaAdapter(firestoreRecyclerOption)

        view?.findViewById<RecyclerView>(recycler_view)?.layoutManager = LinearLayoutManager(requireContext())
        //(view?.findViewById<RecyclerView>(recycler_view)?.scrollToPosition(view?.findViewById<RecyclerView>(recycler_view)?.adapter.itemCount() - 1)
        view?.findViewById<RecyclerView>(recycler_view)?.adapter = esperaAdapter
        //view?.findViewById<RecyclerView>(recycler_view)?.adapter?.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        //view?.findViewById<RecyclerView>(R.id.recycler_view)?.scheduleLayoutAnimation()
        esperaAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        esperaAdapter!!.stopListening()
    }
}