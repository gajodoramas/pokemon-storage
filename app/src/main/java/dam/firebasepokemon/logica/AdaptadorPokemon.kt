package com.gajodoramas.firebasepokemon.logica


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.gajodoramas.firebasepokemon.MainActivity
import com.gajodoramas.firebasepokemon.R
import com.gajodoramas.firebasepokemon.datos.Pokemon
import kotlin.collections.ArrayList


class AdaptadorPokemon (var listaPokemon: ArrayList<Pokemon>, private val mOnClickListener: EventsInterface)
    : RecyclerView.Adapter<AdaptadorPokemon.PokemonViewHolder>(){

        inner class PokemonViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener{
            var nombrePokemon: TextView
            var nivelPokemon: TextView
            var posicion: TextView
            var imagenPokemon: ImageView
            var tipo1: ImageView
            var tipo2: ImageView
            var fondo: ConstraintLayout
            var duenio: TextView
            var borrar: Button
            var editar: Button

            //ignorar, implementacion obligada
            override fun onClick(v: View?) {
                println("hola")
            }

            init {
                nombrePokemon = itemView.findViewById(R.id.nombrePokemon)
                nivelPokemon = itemView.findViewById(R.id.nivelPokemon)
                imagenPokemon = itemView.findViewById(R.id.imagenPokemon)
                tipo1 = itemView.findViewById(R.id.tipo1)
                tipo2 = itemView.findViewById(R.id.tipo2)
                posicion = itemView.findViewById(R.id.posicion)
                fondo = itemView.findViewById(R.id.fondo)
                duenio = itemView.findViewById(R.id.duenio)
                borrar = itemView.findViewById(R.id.borrar)
                editar = itemView.findViewById(R.id.editar)

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.layout_pokemon, parent, false)
        return PokemonViewHolder(item)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemonActual = listaPokemon[position]
        if (MainActivity.usuarioNombre.lowercase() == listaPokemon[position].duenio.lowercase()) {

            //Las siguientes 4 lineas muestran los botones de borrar/editar si eres dueño
            holder.borrar.isEnabled = true
            holder.borrar.visibility = RecyclerView.VISIBLE
            holder.editar.isEnabled = true
            holder.editar.visibility = RecyclerView.VISIBLE

            holder.fondo.setBackgroundResource(R.drawable.estilovista_duenio)
            holder.duenio.text = "Dueño"
        }

        //este else evita el bug de que algunos pokemon que no son de tu propiedad aparezcan visualmente como que si, tmbn los botones borrar/editar
        //aparentemente es innecesario pero NO TOCAR
        else{
            holder.fondo.setBackgroundResource(R.drawable.estilovista_noduenio)
            holder.duenio.text = ""
            //Las siguientes 4 lineas esconden los botones de borrar/editar si no eres dueño
            holder.borrar.isEnabled = false
            holder.borrar.visibility = RecyclerView.INVISIBLE
            holder.editar.isEnabled = false
            holder.editar.visibility = RecyclerView.INVISIBLE
        }
        //ponemos los datos de cada pokemon
        val posicionPokemon : String = (position + 1).toString()
        holder.posicion.text = "Posición : $posicionPokemon"
        holder.nombrePokemon.text = pokemonActual.nombre
        holder.nivelPokemon.text = "Nivel ${pokemonActual.nivelActual.toString()}"
        Picasso.get().load(imagenURL(pokemonActual.numero)).into(holder.imagenPokemon)
        //cambiamos las imagenes de tipos
        when(pokemonActual.tipo1.toString()){
            "Planta" -> holder.tipo1.setImageResource(R.drawable.tipo_planta)
            "Fuego" -> holder.tipo1.setImageResource(R.drawable.tipo_fuego)
            "Agua" -> holder.tipo1.setImageResource(R.drawable.tipo_agua)
            "Bicho" -> holder.tipo1.setImageResource(R.drawable.tipo_bicho)
            "Normal" -> holder.tipo1.setImageResource(R.drawable.tipo_normal)
            "Veneno" -> holder.tipo1.setImageResource(R.drawable.tipo_veneno)
            "Tierra" -> holder.tipo1.setImageResource(R.drawable.tipo_tierra)
            "Lucha" -> holder.tipo1.setImageResource(R.drawable.tipo_lucha)
            "Psiquico" -> holder.tipo1.setImageResource(R.drawable.tipo_psiquico)
            "Roca" -> holder.tipo1.setImageResource(R.drawable.tipo_roca)
            "Electrico" -> holder.tipo1.setImageResource(R.drawable.tipo_electrico)
            "Fantasma" -> holder.tipo1.setImageResource(R.drawable.tipo_fantasma)
            "Hielo" -> holder.tipo1.setImageResource(R.drawable.tipo_hielo)
            "Dragon" -> holder.tipo1.setImageResource(R.drawable.tipo_dragon)
            "Ninguno" -> holder.tipo1.setImageResource(R.drawable.supertrickprofenoteenfades)
            else -> println("Sin tipo")
        }
        when(pokemonActual.tipo2.toString()){
            "Planta" -> holder.tipo2.setImageResource(R.drawable.tipo_planta)
            "Fuego" -> holder.tipo2.setImageResource(R.drawable.tipo_fuego)
            "Agua" -> holder.tipo2.setImageResource(R.drawable.tipo_agua)
            "Bicho" -> holder.tipo2.setImageResource(R.drawable.tipo_bicho)
            "Normal" -> holder.tipo2.setImageResource(R.drawable.tipo_normal)
            "Veneno" -> holder.tipo2.setImageResource(R.drawable.tipo_veneno)
            "Tierra" -> holder.tipo2.setImageResource(R.drawable.tipo_tierra)
            "Lucha" -> holder.tipo2.setImageResource(R.drawable.tipo_lucha)
            "Psiquico" -> holder.tipo2.setImageResource(R.drawable.tipo_psiquico)
            "Roca" -> holder.tipo2.setImageResource(R.drawable.tipo_roca)
            "Electrico" -> holder.tipo2.setImageResource(R.drawable.tipo_electrico)
            "Fantasma" -> holder.tipo2.setImageResource(R.drawable.tipo_fantasma)
            "Hielo" -> holder.tipo2.setImageResource(R.drawable.tipo_hielo)
            "Dragon" -> holder.tipo2.setImageResource(R.drawable.tipo_dragon)
            "Ninguno" -> holder.tipo2.setImageResource(R.drawable.supertrickprofenoteenfades)
            else -> println("Sin tipo")
        }

        //listener para el boton de borrar
        holder.borrar.setOnClickListener {
            MainActivity.borrarPokemon(position)
        }

        //listener para el boton de editar
        holder.editar.setOnClickListener {
            var datosNuevos = arrayOf(listaPokemon[position].duenio, listaPokemon[position].nivelCapturado.toString(), listaPokemon[position].nivelActual.toString())

            MainActivity.editarPokemon(datosNuevos, position)
        }

    }

    //devuelve el numero de elementos en la lista
    override fun getItemCount() = listaPokemon.size

    //devuelve la url preparada para obtener la foto del pokemon con picasso
    fun imagenURL(numero: Int): String {
        return when (numero.toString().length) {
            1 -> "https://assets.pokemon.com/assets/cms2/img/pokedex/full/00$numero.png"
            2 -> "https://assets.pokemon.com/assets/cms2/img/pokedex/full/0$numero.png"
            else -> "https://assets.pokemon.com/assets/cms2/img/pokedex/full/$numero.png"
        }
    }

}