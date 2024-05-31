import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*
import entities.Artista
import entities.Cancion


fun main() {
    val scanner = Scanner(System.`in`)
    val artistas = Artista.cargarArtistas()

    while (true) {
        mostrarMenu()
        while (!scanner.hasNextInt()) {
            println("Por favor, ingrese un número válido.")
            scanner.next()
        }
        when (scanner.nextInt()) {
            1 -> crearArtista(scanner, artistas)
            2 -> crearCancion(scanner, artistas)
            3 -> Artista.leerArtistas(artistas)
            4 -> leerCancionesDeArtista(scanner, artistas)
            5 -> Cancion.leerCanciones(Cancion.cargarCanciones())
            6 -> actualizarArtista(scanner, artistas)
            7 -> actualizarCancion(scanner, artistas)
            8 -> eliminarArtista(scanner, artistas)
            9 -> eliminarCancion(scanner, artistas)
            10 -> {
                println("Gracias por preferirnos")
                break
            }
            else -> println("La opción ingresada no es válida. Por favor, intente de nuevo.")
        }
    }
}

fun mostrarMenu() {
    println("----------UR MUSIC APP----------")
    println("1. Crear un nuevo Artista")
    println("2. Registrar una nueva Canción")
    println("3. Visualizar todos los Artistas existentes junto con sus canciones")
    println("4. Visualizar las Canciones de un Artista específico")
    println("5. Visualizar todas las Canciones registradas")
    println("6. Actualizar la información de un Artista")
    println("7. Actualizar la información de una Canción")
    println("8. Eliminar un Artista existente")
    println("9. Eliminar una Canción registrada")
    println("10. Finalizar y salir del Programa")
    print("Por favor, seleccione una opción del menú: ")
}

fun crearArtista(scanner: Scanner, artistas: MutableList<Artista>) {
    println("Ingrese el ID del Artista:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val id = scanner.nextInt()
    scanner.nextLine()  // Consumir el newline
    // Comprobar si el ID ya existe
    if (artistas.any { it.id == id }) {
        println("Error: Ya existe un artista con el ID proporcionado.")
        return
    }
    println("Ingrese el Nombre del Artista:")
    val nombre = scanner.nextLine()
    println("Ingrese la Descripción del Artista:")
    val descripcion = scanner.nextLine()
    println("Ingrese la Reproducción del Artista:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val reproduccion = scanner.nextInt()
    println("Ingrese el Rating del Artista:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val rating = scanner.nextInt()
    scanner.nextLine()  // Consumir el newline
    val nuevoArtista = Artista(id, nombre, descripcion, reproduccion, rating, mutableListOf())
    Artista.crearArtista(artistas, nuevoArtista)
}

fun crearCancion(scanner: Scanner, artistas: MutableList<Artista>) {
    println("Ingrese el ID del Artista al que pertenece la Canción:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val artistaId = scanner.nextInt()
    val artista = artistas.find { it.id == artistaId }
    if (artista != null) {
        println("Ingrese el ID de la Canción:")
        while (!scanner.hasNextInt()) {
            println("Por favor, ingrese un número válido.")
            scanner.next()
        }
        val id = scanner.nextInt()
        // Comprobar si el ID ya existe
        if (artista.canciones.any { it.id == id }) {
            println("Error: Ya existe una canción con el ID proporcionado.")
            return
        }
        scanner.nextLine()
        println("Ingrese el Título de la Canción:")
        val titulo = scanner.nextLine()
        println("Ingrese la Duración de la Canción (en minutos):")
        while (!scanner.hasNextDouble()) {
            println("Por favor, ingrese un número válido.")
            scanner.next()
        }
        val duracion = scanner.nextDouble()
        scanner.nextLine()  // Consumir el newline
        var fechaLanzamiento: LocalDate? = null
        while (fechaLanzamiento == null) {
            println("Ingrese la Fecha de Lanzamiento de la Canción (formato AAAA-MM-DD):")
            try {
                fechaLanzamiento = LocalDate.parse(scanner.next())
            } catch (e: DateTimeParseException) {
                println("Fecha no válida. Por favor, intente de nuevo.")
            }
        }
        val nuevaCancion = Cancion(id, titulo, duracion, fechaLanzamiento)
        Cancion.crearCancion(artista.canciones, nuevaCancion)
        Artista.guardarArtistas(artistas) // Guardar cambios
    } else {
        println("Artista no encontrado.")
    }
}

fun leerCancionesDeArtista(scanner: Scanner, artistas: MutableList<Artista>) {
    println("Ingrese el ID del Artista cuyas canciones desea ver:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val artistaId = scanner.nextInt()
    val artista = artistas.find { it.id == artistaId }
    if (artista != null) {
        Artista.leerCanciones(artista.canciones)
    } else {
        println("Artista no encontrado.")
    }
}

fun actualizarArtista(scanner: Scanner, artistas: MutableList<Artista>) {
    println("Ingrese el ID del Artista a actualizar:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val id = scanner.nextInt()
    scanner.nextLine()  // Consumir el newline
    // Comprobar si el ID ya existe
    if (artistas.none { it.id == id }) {
        println("Error: No existe un artista con el ID proporcionado.")
        return
    }
    println("Ingrese el Nuevo Nombre del Artista:")
    val nombre = scanner.nextLine()
    println("Ingrese la Nueva Descripción del Artista:")
    val descripcion = scanner.nextLine()
    println("Ingrese la Nueva Reproducción del Artista:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val reproduccion = scanner.nextInt()
    println("Ingrese el Nuevo Rating del Artista:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val rating = scanner.nextInt()
    scanner.nextLine()  // Consumir el newline
    val artista = artistas.find { it.id == id }
    if (artista != null) {
        if (nombre.isNotBlank() && descripcion.isNotBlank()) {
            artista.nombre = nombre
            artista.descripcion = descripcion
            artista.reproducciones = reproduccion
            artista.rating = rating
            Artista.guardarArtistas(artistas)
        } else {
            println("El nombre y la descripción no pueden estar vacíos.")
        }
    }
}

fun actualizarCancion(scanner: Scanner, artistas: MutableList<Artista>) {
    println("Ingrese el ID del Artista al que pertenece la Canción:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val artistaId = scanner.nextInt()
    val artista = artistas.find { it.id == artistaId }
    if (artista != null) {
        println("Ingrese el ID de la Canción a actualizar:")
        while (!scanner.hasNextInt()) {
            println("Por favor, ingrese un número válido.")
            scanner.next()
        }
        val id = scanner.nextInt()
        val cancion = artista.canciones.find { it.id == id }
        if (cancion != null) {
            scanner.nextLine()  // Consumir el newline
            println("Ingrese el Nuevo Título de la Canción:")
            val titulo = scanner.nextLine()
            println("Ingrese la Nueva Duración de la Canción (en minutos):")
            while (!scanner.hasNextDouble()) {
                println("Por favor, ingrese un número válido.")
                scanner.next()
            }
            val duracion = scanner.nextDouble()
            scanner.nextLine()  // Consumir el newline
            var fechaLanzamiento: LocalDate? = null
            while (fechaLanzamiento == null) {
                println("Ingrese la Nueva Fecha de Lanzamiento de la Canción (formato AAAA-MM-DD):")
                try {
                    fechaLanzamiento = LocalDate.parse(scanner.next())
                } catch (e: DateTimeParseException) {
                    println("Fecha no válida. Por favor, intente de nuevo.")
                }
            }
            if (titulo.isNotBlank() && duracion > 0) {
                cancion.titulo = titulo
                cancion.duracion = duracion
                cancion.fechaLanzamiento = fechaLanzamiento
                Artista.guardarArtistas(artistas) // Guardar cambios
            } else {
                println("El título no puede estar vacío y la duración debe ser mayor que 0.")
            }
        } else {
            println("Canción no encontrada.")
        }
    } else {
        println("Artista no encontrado.")
    }
}

fun eliminarArtista(scanner: Scanner, artistas: MutableList<Artista>) {
    println("Ingrese el ID del Artista a eliminar:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val id = scanner.nextInt()
    val artista = artistas.find { it.id == id }
    if (artista != null) {
        artista.canciones.forEach { cancion ->
            Cancion.eliminarCancionPorId(cancion.id)
        }
        Artista.eliminarArtista(artistas, id)
    } else {
        println("Artista no encontrado.")
    }
}

fun eliminarCancion(scanner: Scanner, artistas: MutableList<Artista>) {
    println("Ingrese el ID del Artista al que pertenece la Canción:")
    while (!scanner.hasNextInt()) {
        println("Por favor, ingrese un número válido.")
        scanner.next()
    }
    val artistaId = scanner.nextInt()
    val artista = artistas.find { it.id == artistaId }
    if (artista != null) {
        println("Ingrese el ID de la Canción a eliminar:")
        while (!scanner.hasNextInt()) {
            println("Por favor, ingrese un número válido.")
            scanner.next()
        }
        val id = scanner.nextInt()
        Cancion.eliminarCancion(artista.canciones, id)
        Artista.guardarArtistas(artistas) // Guardar cambios
    } else {
        println("Artista no encontrado.")
    }
}
