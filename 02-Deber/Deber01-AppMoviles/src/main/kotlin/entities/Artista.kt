package entities

import java.io.*
import java.time.LocalDate
import java.util.Scanner

data class Artista(
    var id: Int,
    var nombre: String,
    var descripcion: String,
    var reproducciones: Int,
    var rating: Int,
    var canciones: MutableList<Cancion>
) : Serializable {
    companion object {
        private const val FILE_NAME = "src/main/kotlin/files/artistas.txt"

        // Guarda la lista de artistas en un archivo
        fun guardarArtistas(artistas: List<Artista>) {
            try {
                ObjectOutputStream(FileOutputStream(FILE_NAME)).use { it.writeObject(artistas) }
            } catch (e: IOException) {
                println("Error al guardar los artistas: ${e.message}")
            }
        }

        // Carga la lista de artistas desde un archivo
        fun cargarArtistas(): MutableList<Artista> {
            val file = File(FILE_NAME)
            return if (file.exists() && file.length() > 0) {
                try {
                    ObjectInputStream(FileInputStream(FILE_NAME)).use { it.readObject() as MutableList<Artista> }
                } catch (e: IOException) {
                    println("Error al cargar los artistas: ${e.message}")
                    mutableListOf()
                }
            } else {
                mutableListOf()
            }
        }

        // Crea un nuevo artista y lo agrega a la lista de artistas
        fun crearArtista(artistas: MutableList<Artista>, artista: Artista) {
            if (artista.nombre.isNotBlank() && artista.descripcion.isNotBlank()) {
                artistas.add(artista)
                guardarArtistas(artistas)
            } else {
                println("Nombre y biografía no pueden estar vacíos.")
            }
        }

        // Actualiza un artista existente en la lista de artistas
        fun actualizarArtista(artistas: MutableList<Artista>, id: Int, nuevoArtista: Artista) {
            val artista = artistas.find { it.id == id }
            if (artista != null) {
                if (nuevoArtista.nombre.isNotBlank() && nuevoArtista.descripcion.isNotBlank()) {
                    artista.nombre = nuevoArtista.nombre
                    artista.descripcion = nuevoArtista.descripcion
                    artista.canciones = nuevoArtista.canciones
                    artista.reproducciones = nuevoArtista.reproducciones
                    artista.rating = nuevoArtista.rating
                    guardarArtistas(artistas)
                } else {
                    println("Nombre y biografía no pueden estar vacíos.")
                }
            }
        }

        // Elimina un artista de la lista de artistas
        fun eliminarArtista(artistas: MutableList<Artista>, id: Int) {
            artistas.removeIf { it.id == id }
            guardarArtistas(artistas)
        }

        // Muestra todos los artistas en la lista de artistas
        fun leerArtistas(artistas: List<Artista>) {
            artistas.forEach { artista ->
                println("Artista ID: ${artista.id}, Nombre: ${artista.nombre}, Biografía: ${artista.descripcion}")
                leerCanciones(artista.canciones)
            }
        }

        // Muestra todas las canciones en una lista de canciones
        fun leerCanciones(canciones: List<Cancion>) {
            canciones.forEach { cancion ->
                println("\tCanción ID: ${cancion.id}, Título: ${cancion.titulo}, Duración: ${cancion.duracion}, Fecha de Lanzamiento: ${cancion.fechaLanzamiento}")
            }
        }
    }
}

fun actualizarArtista(scanner: Scanner, artistas: MutableList<Artista>) {
    println("Ingrese el ID del Artista a actualizar:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val id = scanner.nextInt()
    // Comprobar si el ID ya existe
    if (artistas.none { it.id == id }) {
        println("Error: No existe un artista con el ID proporcionado.")
        return
    }
    scanner.nextLine() // Consume newline
    println("Ingrese el Nuevo Nombre del Artista:")
    val nombre = scanner.nextLine()
    println("Ingrese la Nueva Descripción del Artista:")
    val descripcion = scanner.nextLine()
    val artista = artistas.find { it.id == id }
    if (artista != null) {
        if (nombre.isNotBlank() && descripcion.isNotBlank()) {
            artista.nombre = nombre
            artista.descripcion = descripcion
            Artista.guardarArtistas(artistas)
        } else {
            println("El nombre y la descripción no pueden estar vacíos.")
        }
    }
}
