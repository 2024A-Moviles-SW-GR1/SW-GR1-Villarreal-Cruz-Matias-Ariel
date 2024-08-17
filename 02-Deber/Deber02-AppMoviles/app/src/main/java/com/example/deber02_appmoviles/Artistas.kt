package com.example.deber02_appmoviles

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Artistas : AppCompatActivity() {

    var id_cancion = 0

    val callbackFormularioArtista = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val listView = findViewById<ListView>(R.id.list_artista)
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    Database.tables!!.getArtistas()
                )
                listView.adapter = adaptador
                adaptador.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_artistas)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cancion = intent.getStringExtra("cancion")
        id_cancion = intent.getIntExtra("id", 0)

        if (cancion != null) {
            findViewById<TextView>(R.id.id_nombre_cancion).setText(cancion)
        }

        // Colocar datos en Lista
        val listView = findViewById<ListView>(R.id.list_artista)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            Database.tables!!.getArtistas()
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        // Uso de Botones
        val btnCrearCancion = findViewById<Button>(
            R.id.id_btn_crear_artista
        )
        btnCrearCancion.setOnClickListener {
            crearArtista()
        }
        registerForContextMenu(listView)
    }

    private fun crearArtista() {
        val intentCrear = Intent(
            this,
            activity_beditar_artista::class.java
        )

        intentCrear.putExtra("id_cancion", id_cancion)  // Usa intentCrear aquí
        callbackFormularioArtista.launch(intentCrear)
    }

    var index = 1

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // Opciones
        menuInflater.inflate(R.menu.menu_artista, menu)

        // Opción seleccionada
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        index = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.id_mi_editar_artista -> {
                val intentEditar = Intent(
                    this,
                    activity_beditar_artista::class.java
                )
                val artistas = Database.tables!!.getArtistas()
                intentEditar.putExtra("artista", artistas[index])
                callbackFormularioArtista.launch(intentEditar)

                true
            }
            R.id.id_mi_eliminar_artista -> {
                abrirDialogo(index)
                true
            }
            R.id.id_mi_ver_canciones -> {
                val artistaSeleccionado = Database.tables!!.getCancionesPorArtista(index)

                val intent = Intent(this, activity_bcanciones::class.java)
                intent.putExtra("artista", artistaSeleccionado)
                startActivity(intent)
                return true
            }
            R.id.id_mi_ver_ubicacionConcierto -> {
                val ubicacion = Database.tables!!.getArtistas()[index].ubicacionConcierto

                val intent = Intent(this, FMapActivity::class.java)
                intent.putExtra("ubicacion", ubicacion)
                startActivity(intent)

                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirDialogo(index: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea Eliminar?")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                Database.tables!!.eliminarArtista(index + 1)
                val listView = findViewById<ListView>(R.id.list_artista)
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    Database.tables!!.getArtistas()
                )
                listView.adapter = adaptador
                adaptador.notifyDataSetChanged()
            }
        )
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

}
