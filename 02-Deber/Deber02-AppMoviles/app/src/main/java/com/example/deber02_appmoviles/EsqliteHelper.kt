package com.example.deber02_appmoviles

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EsqliteHelper (
    contexto: Context?
): SQLiteOpenHelper(
    contexto,
    "app",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaCanciones = """
            CREATE TABLE Cancion(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(100),
                edad INTEGER,
                altura DOUBLE,
                artista_id INTEGER,
                FOREIGN KEY (artista_id) REFERENCES ARTISTA(id) ON DELETE SET NULL
            );
        """.trimIndent()
        val crearTablaArtistas = """
            CREATE TABLE ARTISTA(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(100),
                fechaCreacion VARCHAR(100),
                ciudad VARCHAR(100),
                ubicacionConcierto VARCHAR(50)
            );
        """.trimIndent()
        db?.execSQL(crearTablaCanciones)
        db?.execSQL(crearTablaArtistas)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearArtista(
        nombre: String,
        fechaCreacion: String,
        ciudad: String,
        ubicacionConcierto: String
    ): Boolean {
        val writeDB = writableDatabase

        val parametros = ContentValues()
        parametros.put("nombre", nombre)
        parametros.put("fechaCreacion", fechaCreacion)
        parametros.put("ciudad", ciudad)
        parametros.put("ubicacionConcierto", ubicacionConcierto)

        val resultadoGuardar = writeDB
            .insert(
                "ARTISTA",
                null,
                parametros
            )
        writeDB.close()
        return resultadoGuardar.toInt() != -1
    }

    fun crearCancion(
        nombre: String,
        edad: Int,
        altura: Double,
        artistaId: Int
    ): Boolean {
        val writeDB = writableDatabase

        val parametros = ContentValues()
        parametros.put("nombre", nombre)
        parametros.put("edad", edad)
        parametros.put("altura", altura)
        parametros.put("artista_id", artistaId)

        val resultadoGuardar = writeDB
            .insert(
                "CANCION",
                null,
                parametros
            )
        writeDB.close()
        return resultadoGuardar.toInt() != -1
    }


    fun getArtistas(): ArrayList<ArtistaEntity> {
        val lectureDB = readableDatabase

        val queryScript = """
        SELECT * FROM ARTISTA
    """.trimIndent()

        val queryResult = lectureDB.rawQuery(
            queryScript,
            null
        )

        val response = arrayListOf<ArtistaEntity>()

        if(queryResult.moveToFirst()) {
            do {
                response.add(
                    ArtistaEntity(
                        queryResult.getInt(0),
                        queryResult.getString(1),
                        queryResult.getString(2),
                        queryResult.getString(3),
                        queryResult.getString(4)
                    )
                )
            } while(queryResult.moveToNext())
        }
        queryResult.close()
        lectureDB.close()

        return response
    }

    fun getCancionesPorArtista(artistaId: Int): ArrayList<CancionEntity> {
        val lectureDB = readableDatabase

        val queryScript = """
            SELECT * FROM CANCION
            WHERE artista_id = ?
        """.trimIndent()

        val queryResult = lectureDB.rawQuery(
            queryScript,
            arrayOf(artistaId.toString())
        )
        val response = arrayListOf<CancionEntity>()

        if(queryResult.moveToFirst()) {
            do {
                response.add(
                    CancionEntity(
                        queryResult.getInt(0),
                        queryResult.getString(1),
                        queryResult.getInt(2),
                        queryResult.getDouble(3),
                        queryResult.getInt(4)
                    )
                )
            } while(queryResult.moveToNext())
        }
        queryResult.close()
        lectureDB.close()

        return response
    }

    fun eliminarArtista(id:Int): Boolean{
        val conexionEscritura = writableDatabase

        //Consulta SQL para eliminar
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = conexionEscritura.delete(
            "ARTISTA",
            "id=?",
            parametrosConsultaDelete
        )
        conexionEscritura.close()
        return resultadoEliminar.toInt() != -1
    }

    fun eliminarCancion(id:Int): Boolean{
        val conexionEscritura = writableDatabase

        //Consulta SQL para eliminar
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = conexionEscritura.delete(
            "Cancion",
            "id=?",
            parametrosConsultaDelete
        )
        conexionEscritura.close()
        return resultadoEliminar.toInt() != -1
    }

    fun actualizarCancion(
        id: Int,
        nombre: String,
        edad: Int,
        altura: Double,
        artistaId: Int
    ): Boolean {
        val writeDB = writableDatabase

        val parametros = ContentValues()
        parametros.put("nombre", nombre)
        parametros.put("edad", edad)
        parametros.put("altura", altura)
        parametros.put("artista_id", artistaId)

        val id_query = arrayOf(id.toString())
        val resultadosAtualizacion = writeDB.update(
            "Cancion",
            parametros,
            "id=?",
            id_query
        )
        writeDB.close()
        return resultadosAtualizacion.toInt() != -1
    }

    fun actualizarArtista(
        id: Int,
        nombre: String,
        fechaCreacion: String,
        ciudad: String,
        ubicacionConcierto: String
    ): Boolean {
        val writeDB = writableDatabase

        val parametros = ContentValues()
        parametros.put("nombre", nombre)
        parametros.put("fechaCreacion", fechaCreacion)
        parametros.put("ciudad", ciudad)
        parametros.put("ubicacionConcierto", ubicacionConcierto)

        val id_query = arrayOf(id.toString())
        val resultadosAtualizacion = writeDB.update(
            "ARTISTA",
            parametros,
            "id=?",
            id_query
        )
        writeDB.close()
        return resultadosAtualizacion.toInt() != -1
    }
}