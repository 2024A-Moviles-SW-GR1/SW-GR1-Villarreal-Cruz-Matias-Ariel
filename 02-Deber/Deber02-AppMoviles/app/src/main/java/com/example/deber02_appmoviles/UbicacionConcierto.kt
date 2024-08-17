package com.example.deber_02

class UbicacionConcierto(
    val nombre: String,
    val latitud: Double,
    val longitud: Double
) {
    companion object {
        private val listUbicacionConcierto = arrayListOf<UbicacionConcierto>()

        init {
            listUbicacionConcierto.add(UbicacionConcierto("Spotify Camp Nou", 41.38077707719879, 2.1229734693948714))
            listUbicacionConcierto.add(UbicacionConcierto("Santiago Bernabéu", 40.44868401225792, -3.692270905948001))
            listUbicacionConcierto.add(UbicacionConcierto("Parque De Los Príncipes", 48.84128188624758, 2.2531790137677516))
            listUbicacionConcierto.add(UbicacionConcierto("Etihad Stadium", 53.48308483612204, -2.2005241036670737))
            listUbicacionConcierto.add(UbicacionConcierto("Allianz Arena", 48.21835323502699, 11.624885773093506))
            listUbicacionConcierto.add(UbicacionConcierto("Estadio Juventus", 45.11000091698978, 7.649845312071816))
        }

        fun getUbicaciones(): List<UbicacionConcierto> {
            return listUbicacionConcierto
        }
    }
}
