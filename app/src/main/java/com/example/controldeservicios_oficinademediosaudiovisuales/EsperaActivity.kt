package com.example.controldeservicios_oficinademediosaudiovisuales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.crearcuenta.adapter.ItemAdapater

class EsperaActivity : Fragment(R.layout.activity_registros_espera) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_registros_espera, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myDataset= Recursos().cargarFrases()
        val recyclerView= view.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.adapter= ItemAdapater(requireContext(), myDataset)
        recyclerView.setHasFixedSize(true)
    }
}