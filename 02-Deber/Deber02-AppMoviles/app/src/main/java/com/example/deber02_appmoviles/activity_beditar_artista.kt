package com.example.deber02_appmoviles

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_beditar_artista : AppCompatActivity() {

    var id: Int = 1
    var ubicacionSpinner = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beditar_artista)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener el Spinner
        val spinner: Spinner = findViewById(R.id.sp_ubiaciones)
        ArrayAdapter.createFromResource(
            this,
            R.array.ubicaciones,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Configurar el listener para cuando se seleccione un elemento en el Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                ubicacionSpinner = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hace nada
            }
        }

        // Obtener los datos del artista desde el Intent
        val artista = intent.getParcelableExtra<ArtistaEntity>("artista")

        if (artista != null) {
            findViewById<EditText>(R.id.input_nombre_artista).setText(artista.nombre)
            findViewById<EditText>(R.id.input_fecha_creacion).setText(artista.fechaCreacion)
            findViewById<EditText>(R.id.input_ciudad).setText(artista.ciudad)
            id = artista.id

            // Configurar el Spinner para que seleccione la ubicación actual del artista
            val ubicacionesArray = resources.getStringArray(R.array.ubicaciones)
            val posicionInicial = ubicacionesArray.indexOf(artista.ubicacionConcierto)
            spinner.setSelection(posicionInicial)
            ubicacionSpinner = artista.ubicacionConcierto
        }
    }

    private fun responseEditar() {
        val response = Intent()

        Database.tables?.actualizarArtista(
            id,
            findViewById<EditText>(R.id.input_nombre_artista).text.toString(),
            findViewById<EditText>(R.id.input_fecha_creacion).text.toString(),
            findViewById<EditText>(R.id.input_ciudad).text.toString(),
            ubicacionSpinner
        )

        setResult(RESULT_OK, response)
        finish()
    }

    private fun responseCrear() {
        val response = Intent()

        Database.tables?.crearArtista(
            findViewById<EditText>(R.id.input_nombre_artista).text.toString(),
            findViewById<EditText>(R.id.input_fecha_creacion).text.toString(),
            findViewById<EditText>(R.id.input_ciudad).text.toString(),
            ubicacionSpinner
        )

        setResult(RESULT_OK, response)
        finish()
    }
}
