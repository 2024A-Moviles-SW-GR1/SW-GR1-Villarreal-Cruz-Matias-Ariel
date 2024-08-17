package com.example.deber02_appmoviles

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_beditar_artista : AppCompatActivity() {

    var id : Int = 1
    var canciones : MutableList<Int> ? = arrayListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beditar_artista)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var artista = intent.getParcelableExtra<ArtistaEntity>("artista")

        if(artista != null) {
            findViewById<EditText>(R.id.input_nombre_cancion).setText(artista.nombre)
            findViewById<EditText>(R.id.input_fecha_creacion).setText(artista.fechaCreacion)
            findViewById<EditText>(R.id.input_ciudad).setText(artista.ciudad)
            id = artista.id
            canciones = artista.canciones
        }

        val guardarBoton = findViewById<Button>(R.id.btn_save)
        guardarBoton.setOnClickListener{
            if(artista != null) {
                responseEditar()
            }else {
                responseCrear()
            }
        }
    }

    private fun responseEditar(){
        val response = Intent()

        val nombre = findViewById<EditText>(R.id.input_nombre_artista).text.toString()
        val fechaCreacion = findViewById<EditText>(R.id.input_fecha_creacion).text.toString()
        val ciudad = findViewById<EditText>(R.id.input_ciudad).text.toString()

        val artistaModificado = ArtistaEntity(id, nombre, fechaCreacion, ciudad, canciones)

        response.putExtra("ArtistaModificado", artistaModificado)

        setResult(RESULT_OK, response)
        finish()
    }
    private fun responseCrear(){
        val response = Intent()

        val nombre = findViewById<EditText>(R.id.input_nombre_artista).text.toString()
        val fechaCreacion = findViewById<EditText>(R.id.input_fecha_creacion).text.toString()
        val ciudad = findViewById<EditText>(R.id.input_ciudad).text.toString()

        val artistaModificado = ArtistaEntity(Memoria.idNuevoArtista(), nombre, fechaCreacion, ciudad, canciones)

        response.putExtra("ArtistaModificado", artistaModificado)

        setResult(RESULT_OK, response)
        finish()
    }
}