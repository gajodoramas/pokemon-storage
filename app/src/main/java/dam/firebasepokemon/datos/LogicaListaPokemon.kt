package com.gajodoramas.firebasepokemon.datos

import android.os.Build
import java.util.Collections


abstract class LogicaListaPokemon {
    companion object {
        private var listaPokemonOriginal: ArrayList<Pokemon> = ArrayList()

        fun ordenarPorNivel(listaPokemon: ArrayList<Pokemon>){
            Pokemon.ordenarPorNivel()
            Collections.sort(listaPokemon)
        }
        fun ordenarPorNombre(listaPokemon: ArrayList<Pokemon>){
            Pokemon.ordenarPorNombre()
            Collections.sort(listaPokemon)
        }
        fun ordenarPorID(listaPokemon: ArrayList<Pokemon>){
            Pokemon.ordenarPorNumero()
            Collections.sort(listaPokemon)
        }

        /**
         * Función que crea un nuevo arraylist solo con los pokemon que superan cierto nivel
         * @param nivel el nivel que deben superar los pokemon que pasan el filtro
         * @param listaPokemon la lista de pokemon que hay que filtrar
         */
        fun buscarPokemonSuperiorANivel(nivel: Int, listaPokemon: ArrayList<Pokemon>): ArrayList<Pokemon>{

            var listaFiltrada: ArrayList<Pokemon> = ArrayList()
            for (pokemon in listaPokemon) listaFiltrada.add(pokemon)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listaFiltrada.removeIf{it.nivelActual < nivel}
            }
            return listaFiltrada
        }

        /**
         * Funcion para buscar pokemon por nombre
         * crea un arrayList con todas las coincidencias y usa el ReciclerView para mostrar???
         */
        fun buscarPokemonNombre (nombrePokemon: String, listaPokemon: ArrayList<Pokemon>): ArrayList<Pokemon>{
            var listaFiltrada: ArrayList<Pokemon> = ArrayList()
            for (pokemon in listaPokemon){
                if (pokemon.nombre.equals(nombrePokemon)){
                    listaFiltrada.add(pokemon)
                }
            }
            return listaFiltrada
        }


    }

}