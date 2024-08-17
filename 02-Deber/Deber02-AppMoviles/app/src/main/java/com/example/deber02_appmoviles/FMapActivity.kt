package com.example.deber02_appmoviles

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class FMapActivity : AppCompatActivity() {
    private lateinit var mapa: GoogleMap
    var permisos=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FMapActivity", "onCreate llamado")
        enableEdgeToEdge()
        setContentView(R.layout.activity_fmap)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ubicacion = intent.getStringExtra("ubicacion")
        Log.d("FMapActivity", "Ubicación recibida: $ubicacion")
        solicitarPermisos()
        iniciarLogicaMapa(ubicacion!!)
    }

    fun solicitarPermisos(){
        val contexto = this.applicationContext
        val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
        val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        if(tienePermisos){
            Log.d("FMapActivity", "Permisos ya concedidos")
            permisos=true
        }else{
            Log.d("FMapActivity", "Solicitando permisos")
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine, nombrePermisoCoarse), 1
            )
        }
    }

    fun iniciarLogicaMapa(ubicacion: String){
        val fragmentoMapa =  supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        val info_ubicacion = UbicacionConcierto.getUbicaciones().find { it.nombre == ubicacion }
        val coordenadas = LatLng(info_ubicacion!!.latitud, info_ubicacion.longitud)
        fragmentoMapa.getMapAsync{googleMap ->
            with(googleMap){
                mapa = googleMap
                Log.d("FMapActivity", "Mapa inicializado")
                establecerConfiguracionMapa()
                moverCamaraConZoom(coordenadas)
                anadirMarcador(coordenadas, info_ubicacion.nombre )
            }

        }
    }

    fun establecerConfiguracionMapa(){
        val contexto = this.applicationContext

        with(mapa){
            val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
            val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
            val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
            val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
            val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                    permisoCoarse == PackageManager.PERMISSION_GRANTED
            if(tienePermisos){
                Log.d("FMapActivity", "Configuración del mapa establecida")
                mapa.isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }
    fun moverCamaraConZoom(latLang: LatLng, zoom: Float=17f){
        Log.d("FMapActivity", "Moviendo cámara a: $latLang con zoom: $zoom")
        mapa.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLang, zoom
            )
        )
    }

    fun anadirMarcador(laLang: LatLng, tittle: String): Marker {
        return mapa.addMarker(
            MarkerOptions().position(laLang)
                .title(tittle)
        )!!
    }

}