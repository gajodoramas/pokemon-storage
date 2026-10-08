import kotlinx.serialization.Serializable

//Eliminadas las tildes y la ñ por problemas al añadir a BBDD
@Serializable
enum class NaturalezaPokemon(val naturaleza: String = "") {
    Activa("Activa"),
    Huranya("Huranya"),
    Afable("Afable"),
    Ingenua("Ingenua"),
    Agitada("Agitada"),
    Mansa("Mansa"),
    Alegre("Alegre"),
    Miedosa("Miedosa"),
    Alocada("Alocada"),
    Modesta("Modesta"),
    Amable("Amable"),
    Osada("Osada"),
    Audaz("Audaz"),
    Picara("Picara"),
    Cauta("Cauta"),
    Placida("Placida"),
    Docil("Docil"),
    Rara("Rara"),
    Firme("Firme"),
    Serena("Serena"),
    Floja("Floja"),
    Seria("Seria"),
    Fuerte("Fuerte"),
    Timida("Timida"),
    Grosera("Grosera");

    companion object {
        fun fromString(naturalezaString: String): NaturalezaPokemon? {
            return values().find { it.naturaleza.equals(naturalezaString, ignoreCase = true) }
        }

        fun getEnumValuesAsArray(): Array<String> {
            return NaturalezaPokemon.values().map { it.name }.toTypedArray()
        }
    }
}