package com.gajodoramas.firebasepokemon.datos

import NaturalezaPokemon
import TipoPokemon


/**
 * Añadido 15:20h 11/01/2022
 */
data class Pokemon(
    var numero: Int = -1,
    var nombre: String = "",
    var nivelActual: Int = -1,
    var nivelCapturado: Int = -1,
    var tipo1: TipoPokemon ? = TipoPokemon.Planta, //Añado los ? porque si no peta al crear pokémon
    var tipo2: TipoPokemon ? = TipoPokemon.Planta,
    var duenio: String = "",
    var naturaleza: NaturalezaPokemon ? = NaturalezaPokemon.Timida
) : Comparable<Pokemon> {
    /*este campo es para la PK de la base de datos, lo usamos para identificar el pokemon en la BD
    y poder borrar, modificar, añadir... en la BD */
    var primaryK : String = ""


    init {
        modoOrdenar = TipoOrden.NUMERO
    }

    /**
     * Comparación de pokémon por nivel.
     */
    override fun compareTo(other: Pokemon): Int {
        return if (modoOrdenar == TipoOrden.NOMBRE) {
            this.nombre.compareTo(other.nombre)
        } else if (modoOrdenar == TipoOrden.NIVEL) {
            Integer.compare(this.nivelActual, other.nivelActual)
        } else if (modoOrdenar == TipoOrden.NUMERO) {
            Integer.compare(this.numero, other.numero)
        } else {
            Integer.compare(this.numero, other.numero)
        }
    }

    companion object {
        /**
         * Atributo estático que determina cómo comparar en compareTo()
         */
        private var modoOrdenar: TipoOrden = TipoOrden.NUMERO

        /**
         * Enum para determinar criterios de ordenación
         */
        private enum class TipoOrden {
            NOMBRE,
            NIVEL,
            NUMERO
        }

        fun ordenarPorNombre() {
            modoOrdenar = TipoOrden.NOMBRE
        }

        fun ordenarPorNumero() {
            modoOrdenar = TipoOrden.NUMERO
        }

        fun ordenarPorNivel() {
            modoOrdenar = TipoOrden.NIVEL
        }

        //LISTA DE DATOS OFICIALES DE LOS POKEMON, PARA QUE EN LA CREACION/EDICION NO HAYA DATOS ERRONEOS
        val datosOficialesPokemon = arrayOf(
            arrayOf("1", "Bulbasaur", "Planta", "Veneno"),
            arrayOf("2", "Ivysaur", "Planta", "Veneno"),
            arrayOf("3", "Venusaur", "Planta", "Veneno"),
            arrayOf("4", "Charmander", "Fuego", "Ninguno"),
            arrayOf("5", "Charmeleon", "Fuego", "Ninguno"),
            arrayOf("6", "Charizard", "Fuego", "Volador"),
            arrayOf("7", "Squirtle", "Agua", "Ninguno"),
            arrayOf("8", "Wartortle", "Agua", "Ninguno"),
            arrayOf("9", "Blastoise", "Agua", "Ninguno"),
            arrayOf("10", "Caterpie", "Bicho", "Ninguno"),
            arrayOf("11", "Metapod", "Bicho", "Ninguno"),
            arrayOf("12", "Butterfree", "Bicho", "Volador"),
            arrayOf("13", "Weedle", "Bicho", "Veneno"),
            arrayOf("14", "Kakuna", "Bicho", "Veneno"),
            arrayOf("15", "Beedrill", "Bicho", "Veneno"),
            arrayOf("16", "Pidgey", "Normal", "Volador"),
            arrayOf("17", "Pidgeotto", "Normal", "Volador"),
            arrayOf("18", "Pidgeot", "Normal", "Volador"),
            arrayOf("19", "Rattata", "Normal", "Ninguno"),
            arrayOf("20", "Raticate", "Normal", "Ninguno"),
            arrayOf("21", "Spearow", "Normal", "Volador"),
            arrayOf("22", "Fearow", "Normal", "Volador"),
            arrayOf("23", "Ekans", "Veneno", "Ninguno"),
            arrayOf("24", "Arbok", "Veneno", "Ninguno"),
            arrayOf("25", "Pikachu", "Electrico", "Ninguno"),
            arrayOf("26", "Raichu", "Electrico", "Ninguno"),
            arrayOf("27", "Sandshrew", "Tierra", "Ninguno"),
            arrayOf("28", "Sandslash", "Tierra", "Ninguno"),
            arrayOf("29", "Nidoran♀", "Veneno", "Ninguno"),
            arrayOf("30", "Nidorina", "Veneno", "Ninguno"),
            arrayOf("31", "Nidoqueen", "Veneno", "Tierra"),
            arrayOf("32", "Nidoran♂", "Veneno", "Ninguno"),
            arrayOf("33", "Nidorino", "Veneno", "Ninguno"),
            arrayOf("34", "Nidoking", "Veneno", "Tierra"),
            arrayOf("35", "Clefairy", "Normal", "Ninguno"),
            arrayOf("36", "Clefable", "Normal", "Ninguno"),
            arrayOf("37", "Vulpix", "Fuego", "Ninguno"),
            arrayOf("38", "Ninetales", "Fuego", "Ninguno"),
            arrayOf("39", "Jigglypuff", "Normal", "Normal"),
            arrayOf("40", "Wigglytuff", "Normal", "Normal"),
            arrayOf("41", "Zubat", "Veneno", "Volador"),
            arrayOf("42", "Golbat", "Veneno", "Volador"),
            arrayOf("43", "Oddish", "Planta", "Veneno"),
            arrayOf("44", "Gloom", "Planta", "Veneno"),
            arrayOf("45", "Vileplume", "Planta", "Veneno"),
            arrayOf("46", "Paras", "Bicho", "Planta"),
            arrayOf("47", "Parasect", "Bicho", "Planta"),
            arrayOf("48", "Venonat", "Bicho", "Veneno"),
            arrayOf("49", "Venomoth", "Bicho", "Veneno"),
            arrayOf("50", "Diglett", "Tierra", "Ninguno"),
            arrayOf("51", "Dugtrio", "Tierra", "Ninguno"),
            arrayOf("52", "Meowth", "Normal", "Ninguno"),
            arrayOf("53", "Persian", "Normal", "Ninguno"),
            arrayOf("54", "Psyduck", "Agua", "Ninguno"),
            arrayOf("55", "Golduck", "Agua", "Ninguno"),
            arrayOf("56", "Mankey", "Lucha", "Ninguno"),
            arrayOf("57", "Primeape", "Lucha", "Ninguno"),
            arrayOf("58", "Growlithe", "Fuego", "Ninguno"),
            arrayOf("59", "Arcanine", "Fuego", "Ninguno"),
            arrayOf("60", "Poliwag", "Agua", "Ninguno"),
            arrayOf("61", "Poliwhirl", "Agua", "Ninguno"),
            arrayOf("62", "Poliwrath", "Agua", "Lucha"),
            arrayOf("63", "Abra", "Psiquico", "Ninguno"),
            arrayOf("64", "Kadabra", "Psiquico", "Ninguno"),
            arrayOf("65", "Alakazam", "Psiquico", "Ninguno"),
            arrayOf("66", "Machop", "Lucha", "Ninguno"),
            arrayOf("67", "Machoke", "Lucha", "Ninguno"),
            arrayOf("68", "Machamp", "Lucha", "Ninguno"),
            arrayOf("69", "Bellsprout", "Planta", "Veneno"),
            arrayOf("70", "Weepinbell", "Planta", "Veneno"),
            arrayOf("71", "Victreebel", "Planta", "Veneno"),
            arrayOf("72", "Tentacool", "Agua", "Veneno"),
            arrayOf("73", "Tentacruel", "Agua", "Veneno"),
            arrayOf("74", "Geodude", "Roca", "Tierra"),
            arrayOf("75", "Graveler", "Roca", "Tierra"),
            arrayOf("76", "Golem", "Roca", "Tierra"),
            arrayOf("77", "Ponyta", "Fuego", "Ninguno"),
            arrayOf("78", "Rapidash", "Fuego", "Ninguno"),
            arrayOf("79", "Slowpoke", "Agua", "Psiquico"),
            arrayOf("80", "Slowbro", "Agua", "Psiquico"),
            arrayOf("81", "Magnemite", "Electrico", "Ninguno"),
            arrayOf("82", "Magneton", "Electrico", "Ninguno"),
            arrayOf("83", "Farfetchd", "Normal", "Volador"),
            arrayOf("84", "Doduo", "Normal", "Volador"),
            arrayOf("85", "Dodrio", "Normal", "Volador"),
            arrayOf("86", "Seel", "Agua", "Ninguno"),
            arrayOf("87", "Dewgong", "Agua", "Hielo"),
            arrayOf("88", "Grimer", "Veneno", "Ninguno"),
            arrayOf("89", "Muk", "Veneno", "Ninguno"),
            arrayOf("90", "Shellder", "Agua", "Ninguno"),
            arrayOf("91", "Cloyster", "Agua", "Hielo"),
            arrayOf("92", "Gastly", "Fantasma", "Veneno"),
            arrayOf("93", "Haunter", "Fantasma", "Veneno"),
            arrayOf("94", "Gengar", "Fantasma", "Veneno"),
            arrayOf("95", "Onix", "Roca", "Tierra"),
            arrayOf("96", "Drowzee", "Psiquico", "Ninguno"),
            arrayOf("97", "Hypno", "Psiquico", "Ninguno"),
            arrayOf("98", "Krabby", "Agua", "Ninguno"),
            arrayOf("99", "Kingler", "Agua", "Ninguno"),
            arrayOf("100", "Voltorb", "Electrico", "Ninguno"),
            arrayOf("101", "Electrode", "Electrico", "Ninguno"),
            arrayOf("102", "Exeggcute", "Planta", "Psiquico"),
            arrayOf("103", "Exeggutor", "Planta", "Psiquico"),
            arrayOf("104", "Cubone", "Tierra", "Ninguno"),
            arrayOf("105", "Marowak", "Tierra", "Ninguno"),
            arrayOf("106", "Hitmonlee", "Lucha", "Ninguno"),
            arrayOf("107", "Hitmonchan", "Lucha", "Ninguno"),
            arrayOf("108", "Lickitung", "Normal", "Ninguno"),
            arrayOf("109", "Koffing", "Veneno", "Ninguno"),
            arrayOf("110", "Weezing", "Veneno", "Ninguno"),
            arrayOf("111", "Rhyhorn", "Tierra", "Roca"),
            arrayOf("112", "Rhydon", "Tierra", "Roca"),
            arrayOf("113", "Chansey", "Normal", "Ninguno"),
            arrayOf("114", "Tangela", "Planta", "Ninguno"),
            arrayOf("115", "Kangaskhan", "Normal", "Ninguno"),
            arrayOf("116", "Horsea", "Agua", "Ninguno"),
            arrayOf("117", "Seadra", "Agua", "Ninguno"),
            arrayOf("118", "Goldeen", "Agua", "Ninguno"),
            arrayOf("119", "Seaking", "Agua", "Ninguno"),
            arrayOf("120", "Staryu", "Agua", "Ninguno"),
            arrayOf("121", "Starmie", "Agua", "Psiquico"),
            arrayOf("122", "Mr. Mime", "Psiquico", "Ninguno"),
            arrayOf("123", "Scyther", "Bicho", "Volador"),
            arrayOf("124", "Jynx", "Hielo", "Psiquico"),
            arrayOf("125", "Electabuzz", "Electrico", "Ninguno"),
            arrayOf("126", "Magmar", "Fuego", "Ninguno"),
            arrayOf("127", "Pinsir", "Bicho", "Ninguno"),
            arrayOf("128", "Tauros", "Normal", "Ninguno"),
            arrayOf("129", "Magikarp", "Agua", "Ninguno"),
            arrayOf("130", "Gyarados", "Agua", "Volador"),
            arrayOf("131", "Lapras", "Agua", "Hielo"),
            arrayOf("132", "Ditto", "Normal", "Ninguno"),
            arrayOf("133", "Eevee", "Normal", "Ninguno"),
            arrayOf("134", "Vaporeon", "Agua", "Ninguno"),
            arrayOf("135", "Jolteon", "Electrico", "Ninguno"),
            arrayOf("136", "Flareon", "Fuego", "Ninguno"),
            arrayOf("137", "Porygon", "Normal", "Ninguno"),
            arrayOf("138", "Omanyte", "Roca", "Agua"),
            arrayOf("139", "Omastar", "Roca", "Agua"),
            arrayOf("140", "Kabuto", "Roca", "Agua"),
            arrayOf("141", "Kabutops", "Roca", "Agua"),
            arrayOf("142", "Aerodactyl", "Roca", "Volador"),
            arrayOf("143", "Snorlax", "Normal", "Ninguno"),
            arrayOf("144", "Articuno", "Hielo", "Volador"),
            arrayOf("145", "Zapdos", "Electrico", "Volador"),
            arrayOf("146", "Moltres", "Fuego", "Volador"),
            arrayOf("147", "Dratini", "Dragon", "Ninguno"),
            arrayOf("148", "Dragonair", "Dragon", "Ninguno"),
            arrayOf("149", "Dragonite", "Dragon", "Volador"),
            arrayOf("150", "Mewtwo", "Psiquico", "Ninguno"),
            arrayOf("151", "Mew", "Psiquico", "Ninguno")
        )
    }
}