package com.example.controldeservicios_oficinademediosaudiovisuales
import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Suppress("DEPRECATION", "NAME_SHADOWING")
class Activity_informes : AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @SuppressLint("NewApi", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informes)
        setSupportActionBar(findViewById(R.id.toolbar_informes))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl"
        );

        val button_inicio = findViewById<Button>(R.id.button_fechaInicio)
        val button_final = findViewById<Button>(R.id.button_fechaFinal)
        val button_fechaAtendidas = findViewById<Button>(R.id.button_fechaHAtendidas)
        val button_horaInicio = findViewById<Button>(R.id.button_horaInicio)
        val button_horaFinal = findViewById<Button>(R.id.button_horaFinal)

        //var fecha_final = ""

        button_inicio.setOnClickListener {
            elegir_fecha(button_inicio, "Fecha inicio")
        }

        button_final.setOnClickListener {
            elegir_fecha(button_final, "Fecha final")
        }

        button_fechaAtendidas.setOnClickListener {
            elegir_fecha(button_fechaAtendidas, "Fecha")
        }

        button_horaInicio.setOnClickListener {
            elegir_hora(button_horaInicio, "Hora inicio")
        }

        button_horaFinal.setOnClickListener {
            elegir_hora(button_horaFinal, "Hora final")
        }

        findViewById<Button>(R.id.button_generar).setOnClickListener {

            when{
                button_inicio.text.toString() == "Fecha inicio" || button_final.text.toString() == "Fecha final" || button_fechaAtendidas.text.toString() == "Fecha" || button_horaInicio.text.toString() == "Hora inicio" || button_horaFinal.text.toString() == "Hora final" -> Toast.makeText(this, "Debe seleccionar fechas y horas solicitadas", Toast.LENGTH_SHORT).show()
                else -> {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                    val dateFormat2 = SimpleDateFormat("dd/MM/yyyy h:mm a")
                    db.collection("control_servicios").whereGreaterThanOrEqualTo("hora_final", Timestamp(dateFormat.parse(button_inicio.text.toString()))).whereLessThanOrEqualTo("hora_final", Timestamp(dateFormat2.parse("${button_final.text.toString()} 11:59 PM"))).get().addOnSuccessListener{
                        var FEC = 0
                        var FARQ = 0
                        var FIQ = 0

                        for(document in it){
                            when{
                                document.getString("grupo").toString().contains("CO") || document.getString("grupo").toString().contains("EO") || document.getString("grupo").toString().contains("TL") || document.getString("grupo").toString().contains("EL") -> FEC = FEC + 1
                                document.getString("grupo").toString().contains("ARQ") -> FARQ = FARQ + 1
                                document.getString("grupo").toString().contains("IQ") -> FIQ = FIQ + 1
                            }
                        }

                        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        for(item in permission){
                            if (this.checkSelfPermission(item)!= PackageManager.PERMISSION_GRANTED){
                                this.requestPermissions(permission, 101)
                            }
                        }

                        val extDir = Environment.getExternalStorageDirectory()
                        val newFile = File(extDir.getAbsolutePath() + "/reporte4.xlsx")

                        var xlWb = XSSFWorkbook()
                        val xlWs = xlWb.createSheet()
                        xlWs.addMergedRegion(CellRangeAddress(0, 0, 0, 2))
                        xlWs.createRow(0).createCell(0).setCellValue("Prestamos por facultad")
                        xlWs.createRow(1).createCell(0).setCellValue("FEC")
                        xlWs.getRow(1).createCell(1).setCellValue("FARQ")
                        xlWs.getRow(1).createCell(2).setCellValue("FIQ")
                        xlWs.createRow(2).createCell(0).setCellValue("${FEC}")
                        xlWs.getRow(2).createCell(1).setCellValue("${FARQ}")
                        xlWs.getRow(2).createCell(2).setCellValue("${FIQ}")

                        xlWs.createRow(4).createCell(0).setCellValue("Total de horas atendidas")

                        val dateFormat = SimpleDateFormat("dd/MM/yyyy h:mm a")
                        db.collection("control_servicios").whereGreaterThanOrEqualTo("hora_final", Timestamp(dateFormat.parse("${button_fechaAtendidas.text.toString()} ${button_horaInicio.text.toString()}"))).whereLessThanOrEqualTo("hora_final", Timestamp(dateFormat.parse("${button_fechaAtendidas.text.toString()} ${button_horaFinal.text.toString()}"))).get().addOnSuccessListener{
                            var hora: Double = 0.0
                            for (documento in it) {
                                val horas = documento.getDouble("total_horas")
                                if (horas != null) {
                                    hora += horas
                                }
                            }
                            Log.d("Dato","${hora}")
                            xlWs.createRow(5).createCell(0).setCellValue("${hora}")

                            val output = FileOutputStream(newFile)
                            xlWb.write(output)
                            output.close()
                            xlWb.close()
                            finish()
                        }
                    }
                }
            }

            /*val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateFormat2 = SimpleDateFormat("dd/MM/yyyy h:mm a")
            db.collection("control_servicios").whereGreaterThanOrEqualTo("hora_final", Timestamp(dateFormat.parse(button_inicio.text.toString()))).whereLessThanOrEqualTo("hora_final", Timestamp(dateFormat2.parse("${button_final.text.toString()} 11:59 PM"))).get().addOnSuccessListener{
                var FEC = 0
                var FARQ = 0
                var FIQ = 0

                for(document in it){
                    when{
                        document.getString("grupo").toString().contains("CO") || document.getString("grupo").toString().contains("EO") || document.getString("grupo").toString().contains("TL") || document.getString("grupo").toString().contains("EL") -> FEC = FEC + 1
                        document.getString("grupo").toString().contains("ARQ") -> FARQ = FARQ + 1
                        document.getString("grupo").toString().contains("IQ") -> FIQ = FIQ + 1
                    }
                }

                val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                for(item in permission){
                    if (this.checkSelfPermission(item)!= PackageManager.PERMISSION_GRANTED){
                        this.requestPermissions(permission, 101)
                    }
                }

                val extDir = Environment.getExternalStorageDirectory()
                val newFile = File(extDir.getAbsolutePath() + "/reporte4.xlsx")

                var xlWb = XSSFWorkbook()
                val xlWs = xlWb.createSheet()
                xlWs.addMergedRegion(CellRangeAddress(0, 0, 0, 2))
                xlWs.createRow(0).createCell(0).setCellValue("Prestamos por facultad")
                xlWs.createRow(1).createCell(0).setCellValue("FEC")
                xlWs.getRow(1).createCell(1).setCellValue("FARQ")
                xlWs.getRow(1).createCell(2).setCellValue("FIQ")
                xlWs.createRow(2).createCell(0).setCellValue("${FEC}")
                xlWs.getRow(2).createCell(1).setCellValue("${FARQ}")
                xlWs.getRow(2).createCell(2).setCellValue("${FIQ}")

                xlWs.createRow(4).createCell(0).setCellValue("Total de horas atendidas")

                val dateFormat = SimpleDateFormat("dd/MM/yyyy h:mm a")
                db.collection("control_servicios").whereGreaterThanOrEqualTo("hora_final", Timestamp(dateFormat.parse("${button_fechaAtendidas.text.toString()} ${button_horaInicio.text.toString()}"))).whereLessThanOrEqualTo("hora_final", Timestamp(dateFormat.parse("${button_fechaAtendidas.text.toString()} ${button_horaFinal.text.toString()}"))).get().addOnSuccessListener{
                    var hora: Double = 0.0
                    for (documento in it) {
                        val horas = documento.getDouble("total_horas")
                        if (horas != null) {
                            hora += horas
                        }
                    }
                    Log.d("Dato","${hora}")
                    xlWs.createRow(5).createCell(0).setCellValue("${hora}")

                    val output = FileOutputStream(newFile)
                    xlWb.write(output)
                    output.close()
                    xlWb.close()
                }
            }*/
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun elegir_hora(button: Button, texto_boton: String){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            button.text = SimpleDateFormat("h:mm a").format(cal.time)
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun generar_reporte(){

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun elegir_fecha(button: Button, texto_boton: String){
        var fecha = ""
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val f = DecimalFormat("00")
        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
            fecha = "${f.format(mDay) }/${f.format(mMonth + 1)}/$mYear"
            button.setText(fecha)
        }, year, month, day)
        datePicker.setButton(DialogInterface.BUTTON_NEUTRAL, "Quitar", DialogInterface.OnClickListener {
                dialog, which -> button.setText(texto_boton)
        })
        datePicker.show()
        //Log.d("Data", "${fecha}")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}


