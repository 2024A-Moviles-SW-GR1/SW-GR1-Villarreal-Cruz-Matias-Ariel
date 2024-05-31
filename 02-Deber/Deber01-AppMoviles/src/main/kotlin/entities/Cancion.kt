package entities

import java.io.*
import java.time.LocalDate

data class Cancion(
    var id: Int,
    var titulo: String,
    var duracion: Double,
    var fechaLanzamiento: LocalDate
) : Serializable {
    companion object {
        private const val FILE_NAME = "src/main/kotlin/files/canciones.txt"

        // Guarda la lista de canciones en un archivo
        fun guardarCanciones(canciones: List<Cancion>) {
            try {
                ObjectOutputStream(FileOutputStream(FILE_NAME)).use { it.writeObject(canciones) }
            } catch (e: IOException) {
                println("Error al guardar las canciones: ${e.message}")
            }
        }

        // Carga la lista de canciones desde un archivo
        fun cargarCanciones(): MutableList<Cancion> {
            val file = File(FILE_NAME)
            return if (file.exists() && file.length() > 0) {
                try {
                    ObjectInputStream(FileInputStream(FILE_NAME)).use { it.readObject() as MutableList<Cancion> }
                } catch (e: IOException) {
                    println("Error al cargar las canciones: ${e.message}")
                    mutableListOf()
                }
            } else {
                mutableListOf()
            }
        }

        // Crea una nueva canción y la agrega a la lista de canciones
        fun crearCancion(canciones: MutableList<Cancion>, cancion: Cancion) {
            if (cancion.titulo.isNotBlank() && cancion.duracion >= 0) {
                canciones.add(cancion)
                guardarCanciones(canciones)
            } else {
                println("El título no puede estar vacío y la duración no puede ser negativa.")
            }
        }

        // Actualiza una canción existente en la lista de canciones
        fun actualizarCancion(canciones: MutableList<Cancion>, id: Int, nuevaCancion: Cancion) {
            val cancion = canciones.find { it.id == id }
            if (cancion != null) {
                if (nuevaCancion.titulo.isNotBlank() && nuevaCancion.duracion >= 0) {
                    cancion.titulo = nuevaCancion.titulo
                    cancion.duracion = nuevaCancion.duracion
                    cancion.fechaLanzamiento = nuevaCancion.fechaLanzamiento
                    guardarCanciones(canciones)
                } else {
                    println("El título no puede estar vacío y la duración no puede ser negativa.")
                }
            } else {
                println("Error: No se encontró una canción con el ID proporcionado.")
            }
        }

        // Elimina una canción de la lista de canciones
        fun eliminarCancion(canciones: MutableList<Cancion>, id: Int) {
            if (canciones.removeIf { it.id == id }) {
                guardarCanciones(canciones)
            } else {
                println("Error: No se encontró una canción con el ID proporcionado.")
            }
        }

        // Elimina una canción de la lista de canciones por ID
        fun eliminarCancionPorId(id: Int) {
            val canciones = cargarCanciones()
            canciones.removeIf { it.id == id }
            guardarCanciones(canciones)
        }

        // Muestra todas las canciones en la lista de canciones
        fun leerCanciones(canciones: List<Cancion>) {
            if (canciones.isEmpty()) {
                println("No hay canciones para mostrar.")
            } else {
                canciones.forEach { cancion ->
                    println("Canción ID: ${cancion.id}, Título: ${cancion.titulo}, Duración: ${cancion.duracion}, Fecha de Lanzamiento: ${cancion.fechaLanzamiento}")
                }
            }
        }
    }
}
