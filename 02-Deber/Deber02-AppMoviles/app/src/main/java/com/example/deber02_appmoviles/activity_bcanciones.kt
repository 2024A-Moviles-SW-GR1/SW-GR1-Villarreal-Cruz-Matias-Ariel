package com.example.deber02_appmoviles

import android.app.Activity
import android.content.DialogInterface
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
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_bcanciones : AppCompatActivity() {

    val callbackFormularioCancion = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {

                val listView = findViewById<ListView>(R.id.list_canciones)
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    Database.tables!!.getCancionesPorArtista(index)
                )
                listView.adapter = adaptador
                adaptador.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bcanciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_canciones)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Database.tables = EsqliteHelper(
            this
        )

        // Colocar datos en Lista
        val listView = findViewById<ListView>(R.id.list_canciones)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            Database.tables?.getCancionesPorArtista(index) ?: emptyList()
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        // Uso de Botones
        val btnCrearCancion = findViewById<Button>(
            R.id.id_btn_crear_cancion
        )
        btnCrearCancion.setOnClickListener {
            crearCancion(adaptador)
        }
        registerForContextMenu(listView)

    }

    private fun crearCancion(
        adapter: ArrayAdapter<CancionEntity>
    ) {
        val intentCrear = Intent(
            this,
            activity_deditar_canciones::class.java
        )

        callbackFormularioCancion.launch(intentCrear)

        adapter.notifyDataSetChanged()
    }

    var index = -1

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

                val intentEditar = Intent(
                    this,
                    activity_deditar_canciones::class.java
                )
                val canciones = Database.tables!!.getCancionesPorArtista(index)

                intentEditar.putExtra("cancion", canciones[index])
                callbackFormularioCancion.launch(intentEditar)

                true
            }
            R.id.id_mi_eliminar_cancion -> {
                abrirDialogo(index)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirDialogo(index: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar canción?")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                Database.tables!!.eliminarCancion(index + 1)

                val listView = findViewById<ListView>(R.id.list_canciones)
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    Database.tables!!.getCancionesPorArtista(index)
                )
                listView.adapter = adaptador
                adaptador.notifyDataSetChanged()
            }
        )
        builder.setNegativeButton("Cancelar", null)

        builder.create().show()
    }
}
