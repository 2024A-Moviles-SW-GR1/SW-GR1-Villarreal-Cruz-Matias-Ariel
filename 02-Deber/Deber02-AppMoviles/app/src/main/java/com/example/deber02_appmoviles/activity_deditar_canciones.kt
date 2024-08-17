package com.example.deber02_appmoviles

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_deditar_canciones : AppCompatActivity() {

    var id: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_deditar_canciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cancion = intent.getParcelableExtra<CancionEntity>("cancion")

        if (cancion != null) {
            findViewById<EditText>(R.id.input_nombre_cancion).setText(cancion.nombre)
            findViewById<EditText>(R.id.input_edad_cancion).setText(cancion.edad.toString())
            findViewById<EditText>(R.id.input_altura_cancion).setText(cancion.altura.toString())
            id = cancion.id
        }

        val guardarBtn = findViewById<Button>(R.id.btn_save_mat)
        guardarBtn.setOnClickListener {
            if (cancion != null) {
                responseEditar()
            } else {
                responseCrear()
            }
        }
    }

    private fun responseEditar() {
        val response = Intent()
        val nombre = findViewById<EditText>(R.id.input_nombre_cancion).text.toString()
        val edadStr = findViewById<EditText>(R.id.input_edad_cancion).text.toString()
        val alturaStr = findViewById<EditText>(R.id.input_altura_cancion).text.toString()

        if (nombre.isNotEmpty() && edadStr.isNotEmpty() && alturaStr.isNotEmpty()) {
            val edad = edadStr.toIntOrNull() ?: 0
            val altura = alturaStr.toDoubleOrNull() ?: 0.0

            val cancionModificado = CancionEntity(id, nombre, edad, altura)
            Memoria.actualizarCancion(cancionModificado)

            response.putExtra("cancionModificado", cancionModificado)
            setResult(RESULT_OK, response)
            finish()
        } else {
            // Mostrar mensaje de error o validación
        }
    }

    private fun responseCrear() {
        val response = Intent()

        val nombre = findViewById<EditText>(R.id.input_nombre_cancion).text.toString()
        val edadStr = findViewById<EditText>(R.id.input_edad_cancion).text.toString()
        val alturaStr = findViewById<EditText>(R.id.input_altura_cancion).text.toString()

        if (nombre.isNotEmpty() && edadStr.isNotEmpty() && alturaStr.isNotEmpty()) {
            val edad = edadStr.toIntOrNull() ?: 0
            val altura = alturaStr.toDoubleOrNull() ?: 0.0

            val cancionNuevo = CancionEntity(Memoria.idNuevoCancion(), nombre, edad, altura)

            response.putExtra("cancionNuevo", cancionNuevo)

            setResult(RESULT_OK, response)
            finish()
}
    }
}
