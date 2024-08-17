package com.example.deber02_appmoviles

import android.util.Log

class Memoria {
    companion object {
        val artistas = arrayListOf<ArtistaEntity>()
        val canciones = arrayListOf<CancionEntity>()

        init {
            initCanciones()
            initArtistas()
        }

        fun initCanciones() {
            canciones.add(CancionEntity(1, "See You Again", 2015, 3.49))
            canciones.add(CancionEntity(2, "Bad Guy", 2019, 3.14))
            canciones.add(CancionEntity(3, "Hello", 2015, 4.55))
            canciones.add(CancionEntity(4, "God's Plan", 2018, 3.18))
            canciones.add(CancionEntity(5, "Formation", 2016, 3.26))
            canciones.add(CancionEntity(6, "HUMBLE.", 2017, 2.57))
            canciones.add(CancionEntity(7, "Blinding Lights", 2020, 3.20))
            canciones.add(CancionEntity(8, "Shape of You", 2017, 4.24))
            canciones.add(CancionEntity(9, "Uptown Funk", 2014, 4.30))
            canciones.add(CancionEntity(10, "Rolling in the Deep", 2010, 3.48))
            canciones.add(CancionEntity(11, "Old Town Road", 2019, 2.37))
            canciones.add(CancionEntity(12, "Despacito", 2017, 3.48))
            canciones.add(CancionEntity(13, "Shallow", 2018, 3.36))
            canciones.add(CancionEntity(14, "Stay", 2021, 2.21))
            canciones.add(CancionEntity(15, "Peaches", 2021, 3.18))
            canciones.add(CancionEntity(16, "Levitating", 2020, 3.23))
            canciones.add(CancionEntity(17, "Drivers License", 2021, 4.02))
            canciones.add(CancionEntity(18, "Dynamite", 2020, 3.19))
            Log.d("Memoria", "Canciones iniciales: $canciones")
        }

        fun initArtistas() {
            artistas.add(ArtistaEntity(1, "Tyler, the Creator", "1991-03-06", "Los Angeles", mutableListOf(1, 2, 3)))
            artistas.add(ArtistaEntity(2, "Billie Eilish", "2001-12-18", "Los Angeles", mutableListOf(4, 5, 6)))
            artistas.add(ArtistaEntity(3, "Adele", "1988-05-05", "London", mutableListOf(7, 8, 9)))
            artistas.add(ArtistaEntity(4, "Drake", "1986-10-24", "Toronto", mutableListOf(10, 11, 12)))
            artistas.add(ArtistaEntity(5, "Beyoncé", "1981-09-04", "Houston", mutableListOf(13, 14, 15)))
            artistas.add(ArtistaEntity(6, "Kendrick Lamar", "1987-06-17", "Compton", mutableListOf(16, 17, 18)))
            Log.d("Memoria", "Artistas iniciales: $artistas")
        }

        fun agregarCancionArtista(id: Int, cancion: CancionEntity) {
            artistas.forEach { artista ->
                if (artista.id == id) {
                    artista.canciones?.add(cancion.id)
                }
            }
        }

        fun idNuevoArtista(): Int {
            return artistas.lastOrNull()?.id?.plus(1) ?: 1
        }

        fun idNuevoCancion(): Int {
            return canciones.lastOrNull()?.id?.plus(1) ?: 1
        }

        fun actualizarCancion(cancion: CancionEntity) {
            val cancionIndex = canciones.indexOfFirst { it.id == cancion.id }
            if (cancionIndex != -1) {
                canciones[cancionIndex] = cancion
            }
        }

        fun eliminarCancionArtista(idArtista: Int, idCancion: Int) {
            val artistaIndex = artistas.indexOfFirst { it.id == idArtista } // Corrección aquí
            if (artistaIndex != -1) {
                val canciones = artistas[artistaIndex].canciones
                canciones?.let {
                    val cancionIndex = it.indexOfFirst { it == idCancion }
                    if (cancionIndex != -1) {
                        it.removeAt(cancionIndex)
                    }
                }
            }
        }
    }
}
