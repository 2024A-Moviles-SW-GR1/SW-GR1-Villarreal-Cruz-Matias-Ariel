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
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Artistas : AppCompatActivity() {

    val callbackFormularioArtista=registerForActivityResult( ///////////////////////////////////
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val artistaModificado = result.data!!.getParcelableExtra<ArtistaEntity>("artistaModificado")
                val artistaNuevo = result.data!!.getParcelableExtra<ArtistaEntity>("artistaNuevo")

                if (artistaModificado != null) {
                    Memoria.artistas.removeAt(index)
                    Memoria.artistas.add(index, artistaModificado)
                }else if(artistaNuevo != null ){
                    Memoria.artistas.add(artistaNuevo)
                }

                val listView = findViewById<ListView>(R.id.list_artista)
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    Memoria.artistas
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
        Log.d("ArtistasActivity", "Artistas iniciales: ${Memoria.artistas}")
        //colocar daots en la lista
        val listView = findViewById<ListView>(R.id.list_artista)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            Memoria.artistas
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        //Botones
        val botonCrearArtista = findViewById<Button>(
            R.id.id_btn_crear_artista
        )
        botonCrearArtista.setOnClickListener{
            crearArtista()
        }
        registerForContextMenu(listView)
    }

    private fun crearArtista(){
        val intentCrear = Intent(
            this,
            activity_beditar_artista::class.java
        )

        callbackFormularioArtista.launch(intentCrear)
    }

    var index = 1

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //opciones
        menuInflater.inflate(R.menu.menu_artista, menu)

        //opcion seleccionada
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        index = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.id_mi_editar_artista -> {
                val intentEditar = Intent(
                    this,
                    activity_beditar_artista::class.java
                )
                intentEditar.putExtra("artista", Memoria.artistas.get(index))
                callbackFormularioArtista.launch(intentEditar)

                return true
            }
            R.id.id_mi_eliminar_artista->{

                val listView = findViewById<ListView>(R.id.list_artista)
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    Memoria.artistas
                )
                listView.adapter = adaptador
                abrirDialogo(index, adaptador)
                return true
            }
            R.id.id_mi_ver_canciones->{
                val artistaSeleccionado = Memoria.artistas.get(index)
                Log.d("ArtistasActivity", "Artista seleccionado: $artistaSeleccionado")
                Log.d("ArtistasActivity", "Canciones en el artista: ${artistaSeleccionado.canciones}")

                val intent = Intent(this, activity_bcanciones::class.java)
                intent.putExtra("artista", artistaSeleccionado)
                startActivity(intent)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirDialogo(index:Int, adapter: ArrayAdapter<ArtistaEntity>){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea Eliminar?")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener{
                    dialog, which ->
                Memoria.artistas.removeAt(index)
                adapter.notifyDataSetChanged()
            }
        )
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }
}