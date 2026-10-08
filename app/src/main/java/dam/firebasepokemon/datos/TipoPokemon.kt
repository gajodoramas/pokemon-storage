/**
 * Info: https://pokemon.fandom.com/es/wiki/Lista_de_Pok%C3%A9mon_de_la_primera_generaci%C3%B3n
 * Los pokémon tipo hada van a ser tipo normal.
 */

//Eliminé las tildes por problemas al añadir a la base de datos
//enum con los tipos de pokemon
enum class TipoPokemon(val tipo: String) {
    Planta("Planta"),
    Fuego("Fuego"),
    Agua("Agua"),
    Bicho("Bicho"),
    Normal("Normal"),
    Veneno("Veneno"),
    Tierra("Tierra"),
    Lucha("Lucha"),
    Psiquico("Psiquico"),
    Volador("Volador"),
    Roca("Roca"),
    Electrico("Electrico"),
    Fantasma("Fantasma"),
    Hielo("Hielo"),
    Dragon("Dragon"),
    Ninguno("Ninguno");

    //devuelve los tipos
    companion object{
        fun fromString(tipoString: String): TipoPokemon? {
            return values().find { it.tipo.equals(tipoString, ignoreCase = true) }
        }
        fun getEnumValuesAsArray(): Array<String> {
            return TipoPokemon.values().map { it.name }.toTypedArray()
        }
    }
}