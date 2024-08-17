package com.example.deber02_appmoviles

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_bcanciones : AppCompatActivity() {

    private var canciones = arrayListOf<CancionEntity>()
    private var idArtista = 1
    private var index = -1

    private val callbackFormularioCancion = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val cancionModificado = it.getParcelableExtra<CancionEntity>("cancionModificado")
                val cancionNuevo = it.getParcelableExtra<CancionEntity>("cancionNuevo")

                cancionModificado?.let { cancion ->
                    canciones[index] = cancion
                    Memoria.actualizarCancion(cancion)
                } ?: cancionNuevo?.let { nuevoCancion ->
                    canciones.add(nuevoCancion)
                    Memoria.canciones.add(nuevoCancion)
                    Memoria.agregarCancionArtista(idArtista, nuevoCancion)
                }

                actualizarListaCanciones()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bcanciones)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_canciones)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val artista = intent.getParcelableExtra<ArtistaEntity>("artista")
        Log.d("CancionesActivity", "Artista recibido: $artista")

        artista?.let {
            findViewById<TextView>(R.id.id_artista_nom).text = it.nombre
            val cancionesArtista = it.canciones ?: mutableListOf()
            canciones = Memoria.canciones.filter { cancion -> cancionesArtista.contains(cancion.id) } as ArrayList<CancionEntity>
            idArtista = it.id
        } ?: run {
            Log.d("CancionesActivity", "No se recibió ningún artista.")
        }

        actualizarListaCanciones()

        findViewById<Button>(R.id.id_btn_crear_artista).setOnClickListener {
            crearCancion()
        }

        findViewById<Button>(R.id.btn_back).setOnClickListener {
            startActivity(Intent(this, Artistas::class.java))
        }

        registerForContextMenu(findViewById(R.id.list_canciones))
    }

    private fun actualizarListaCanciones() {
        val listView = findViewById<ListView>(R.id.list_canciones)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            canciones
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
    }

    private fun crearCancion() {
        val intentCrear = Intent(this, activity_deditar_canciones::class.java)
        callbackFormularioCancion.launch(intentCrear)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_cancion, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        index = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.id_mi_editar_cancion -> {
                val intentEditar = Intent(this, activity_deditar_canciones::class.java)
                intentEditar.putExtra("cancion", canciones[index])
                callbackFormularioCancion.launch(intentEditar)
                true
            }
            R.id.id_mi_eliminar_cancion -> {
                abrirDialogoEliminar(canciones[index])
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirDialogoEliminar(cancion: CancionEntity) {
        AlertDialog.Builder(this)
            .setTitle("¿Desea eliminar al cancion?")
            .setPositiveButton("Aceptar") { _, _ ->
                eliminarCancion(cancion)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarCancion(cancion: CancionEntity) {
        canciones.remove(cancion)
        Memoria.canciones.remove(cancion)
        Memoria.artistas.find { it.id == idArtista }?.canciones?.remove(cancion.id)
        actualizarListaCanciones()
    }
}
