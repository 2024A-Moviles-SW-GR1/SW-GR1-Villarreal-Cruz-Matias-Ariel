import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_deditar_canciones : AppCompatActivity() {

    var id: Int = -1
    var artistaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_deditar_canciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el Spinner para seleccionar el artista
        val spinnerArtistas: Spinner = findViewById(R.id.spiner_artistas)
        val artistas = Database.tables?.getArtistas() ?: listOf()
        val artistasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, artistas.map { it.nombre })
        artistasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerArtistas.adapter = artistasAdapter

        val cancion = intent.getParcelableExtra<CancionEntity>("cancion")

        if (cancion != null) {
            findViewById<EditText>(R.id.input_nombre_cancion).setText(cancion.nombre)
            findViewById<EditText>(R.id.input_duracion_cancion).setText(cancion.duracion.toString())
            findViewById<EditText>(R.id.input_genero_cancion).setText(cancion.genero)
            id = cancion.id
            artistaId = cancion.artistaId
            // Seleccionar el artista actual de la canción en el Spinner
            val artistaIndex = artistas.indexOfFirst { it.id == artistaId }
            if (artistaIndex != -1) {
                spinnerArtistas.setSelection(artistaIndex)
            }
        }

        val guardarBtn = findViewById<Button>(R.id.btn_save_cancion)
        guardarBtn.setOnClickListener {
            // Obtener el artista seleccionado
            val artistaSeleccionado = artistas[spinnerArtistas.selectedItemPosition]
            artistaId = artistaSeleccionado.id

            if (id != -1) {
                responseEditar()
            } else {
                responseCrear()
            }
        }
    }

    private fun responseEditar() {
        if (!validarDatos()) return

        val response = Intent()
        try {
            Database.tables?.actualizarCancion(
                id,
                findViewById<EditText>(R.id.input_nombre_cancion).text.toString(),
                findViewById<EditText>(R.id.input_duracion_cancion).text.toString().toDouble(),
                findViewById<EditText>(R.id.input_genero_cancion).text.toString(),
                artistaId
            )
            setResult(RESULT_OK, response)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al actualizar la canción: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun responseCrear() {
        val response = Intent()

        Database.tables?.crearCancion(
            findViewById<EditText>(R.id.input_nombre_cancion).text.toString(),
            findViewById<EditText>(R.id.input_duracion_cancion).text.toString().toDouble(),
            findViewById<EditText>(R.id.input_genero_cancion).text.toString(),
            artistaId
        )

        setResult(RESULT_OK, response)
        finish()
    }

    private fun validarDatos(): Boolean {
        val nombre = findViewById<EditText>(R.id.input_nombre_cancion).text.toString()
        val duracion = findViewById<EditText>(R.id.input_duracion_cancion).text.toString()
        val genero = findViewById<EditText>(R.id.input_genero_cancion).text.toString()

        if (nombre.isEmpty() || duracion.isEmpty() || genero.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return false
        }

        if (duracion.toDoubleOrNull() == null || duracion.toDouble() <= 0) {
            Toast.makeText(this, "La duración debe ser un número positivo", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
