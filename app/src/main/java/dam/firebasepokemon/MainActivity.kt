package com.gajodoramas.firebasepokemon

import NaturalezaPokemon
import TipoPokemon
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.gajodoramas.firebasepokemon.datos.LogicaListaPokemon.Companion.buscarPokemonNombre
import com.gajodoramas.firebasepokemon.datos.LogicaListaPokemon.Companion.buscarPokemonSuperiorANivel
import com.gajodoramas.firebasepokemon.datos.LogicaListaPokemon.Companion.ordenarPorNivel
import com.gajodoramas.firebasepokemon.datos.Pokemon
import com.gajodoramas.firebasepokemon.logica.AdaptadorPokemon
import com.gajodoramas.firebasepokemon.logica.EventsInterface
import okhttp3.internal.cache.DiskLruCache
import java.util.*


class MainActivity : AppCompatActivity() {

    // VARIABLES

    lateinit var botonBuscar: ImageButton
    lateinit var usuarioActivo : String

    companion object {
        var listaPokemon = ArrayList<Pokemon>()
        lateinit var bbdd: FirebaseFirestore // creamos la instancia de tipo Firebase que apunta a la BD
        lateinit var recyclerView: RecyclerView
        lateinit var adaptadorPokemon: AdaptadorPokemon
        lateinit var usuarioNombre : String
        lateinit  var appContext: Context
        lateinit var funcionBD : ListenerRegistration //Escuchante de la BBDD
        lateinit var inflater : LayoutInflater  //inflador para el RecyclerView

        //obligados a implementar, ignorar
        private val funcion = object : EventsInterface {
            override fun clickEnElemento(pos: Int) {
                println("hola")
            }
        }

        //funcion para editar pokemon, construye un dialogo
        fun editarPokemon(datosNuevos : Array<String>, position : Int) {

            val builder = AlertDialog.Builder(appContext)
            val dialogLayout = inflater.inflate(R.layout.dialogo_editar, null)
            //dialogo
            with(builder) {
                setTitle("Editar Pokémon")
                dialogLayout.findViewById<EditText>(R.id.editar_campoDuenio).setText(listaPokemon[position].duenio)
                dialogLayout.findViewById<EditText>(R.id.editar_NivelCapturado).setText(listaPokemon[position].nivelCapturado.toString())
                dialogLayout.findViewById<EditText>(R.id.editar_nivelActual).setText(listaPokemon[position].nivelActual.toString())
                setPositiveButton("Enviar") { dialog, which ->

                    val pokehash = hashMapOf(
                        "duenio" to dialogLayout.findViewById<EditText>(R.id.editar_campoDuenio).text.toString(),
                        "nivelCapturado" to Integer.parseInt(dialogLayout.findViewById<EditText>(R.id.editar_NivelCapturado).text.toString()),
                        "nivelActual" to Integer.parseInt(dialogLayout.findViewById<EditText>(R.id.editar_nivelActual).text.toString())
                    )
                    bbdd.collection("pokemon").document(listaPokemon[position].primaryK).update(pokehash as Map<String, Any>)
                    MainActivity.adaptadorPokemon.notifyItemChanged(position)
                    cargarRecycler()
                }

                setNegativeButton("Cancelar") { dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }
                setView(dialogLayout)
                show()

            }

        }

        //funcion para borrar pokemon
        fun borrarPokemon(position : Int) {
            MainActivity.bbdd.collection("pokemon").document(listaPokemon[position].primaryK).delete() // pokenon/pokemon01
            MainActivity.adaptadorPokemon.notifyItemRemoved(position)
            cargarRecycler()
        }

        //carga el recycler view
        fun cargarRecycler() {

            adaptadorPokemon.listaPokemon = listaPokemon

            adaptadorPokemon.notifyDataSetChanged()
        }

        //fija el nombre del usuario recogido del intent a una variable
        fun fijarNombre(nombre : String) {
            usuarioNombre = nombre
        }
    }

    //funcion onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        MainActivity.appContext = MainActivity@this //applicationContext
        listenerSMS()
        listenerAnnadirPokemon()
        buscarPokemon()
        listenerOrdenar()

        //se recoge y guarda el email para comparar con pokemons
        usuarioActivo = intent.getStringExtra("correo")?.lowercase()!!
        fijarNombre(usuarioActivo)
        MainActivity.inflater = layoutInflater

        //establecemos la conexion a la bbdd del Firestore asociada al proyecto
        bbdd = FirebaseFirestore.getInstance()

        /*añadimos un snapshot que esta pendiente de cualquier cambio lo metemos en una constante
        para poder liberar el recurso del listener al cerrar (OnStop(), Ondestroy()... */

          funcionBD = bbdd.collection("pokemon")
            .addSnapshotListener { snapshot, excepcion ->
                if (excepcion == null) {
                    if (snapshot != null) {
                        //si hay cambios limpia la lista de pokemon para volver a cargarla
                        listaPokemon.clear()
                        snapshot.forEach { documento ->
                            println(documento.id)
                            val p = documento.toObject(Pokemon::class.java)
                            //le asignamos el id antes de meterlo en el arraylist
                            p.primaryK = documento.id
                            listaPokemon.add(p)
                        }
                        cargarRecycler() //metodo que carga las vistas de los pokemon en el recyclerView
                    }
                } else {
                    println("Ha habido un error: " + excepcion.localizedMessage)
                }
            }
        // FIND VIEW BY ID
        recyclerView = findViewById(R.id.recyclerView)
        botonBuscar = findViewById(R.id.btnBuscar)

        // AÑADIR POKEMON PARA TESTEAR RECYCLER
        adaptadorPokemon = AdaptadorPokemon(listaPokemon, Companion.funcion)
        recyclerView.layoutManager = LinearLayoutManager(appContext)
        recyclerView.addItemDecoration(DividerItemDecoration(appContext, DividerItemDecoration.VERTICAL))

        // RECYCLER VIEW
        cargarRecycler()
        recyclerView.adapter = adaptadorPokemon
        // TEST REVERSE ORDER
        Collections.sort(listaPokemon, reverseOrder())

    }

    //encargado de ordenar pokemon por nivel
    fun listenerOrdenar() {
        findViewById<ToggleButton>(R.id.btnOrdenarNivel).setOnClickListener {
            if (findViewById<ToggleButton>(R.id.btnOrdenarNivel).isChecked) {

                bbdd.collection("pokemon").orderBy("nivelActual").get().addOnSuccessListener {

                    //si hay cambios limpia la lista de pokemon para volver a cargarla
                    listaPokemon.clear()
                    it.documents.forEach { documento ->
                        println(documento.id)
                        val p = documento.toObject(Pokemon::class.java)
                        //le asignamos el id antes de meterlo en el arraylist
                        if (p != null) {
                            p.primaryK = documento.id
                            listaPokemon.add(p)
                        }


                    }
                    cargarRecycler()
                }

            } else {
                reiniciarRecyclerView()
            }
        }
    }

    //implementacion obligada, ignorar
    private val funcion = object : EventsInterface {
        override fun clickEnElemento(pos: Int) {

        }
    }

    //sobrescribimos la funcion OnDestroy() para liberar el snapshotListener
    override fun onDestroy() {
        super.onDestroy()
        funcionBD.remove()
    }

    //funcion para añadir nuevos pokemon
    fun listenerAnnadirPokemon() {

        //lineas que rellenaran el spinner de nombres
        var listaNombres = ArrayList<String>()
        for (Elemento in Pokemon.datosOficialesPokemon) {
            listaNombres.add(Elemento[1])
            println(Elemento[1])
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, listaNombres)

        //rellena el spinner de naturalezas
        val adapter2: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, NaturalezaPokemon.getEnumValuesAsArray())

        //click listener
        findViewById<ImageButton>(R.id.btnAdd).setOnClickListener() {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialogo_annadir, null)
            val spinnerNombres = dialogLayout.findViewById<Spinner>(R.id.annadir_spinnerNombresPokemon)
            val spinnerNatus = dialogLayout.findViewById<Spinner>(R.id.spinnerNaturalezas)
            val campoNivelCap = dialogLayout.findViewById<EditText>(R.id.nivelCapturado)
            val campoNivelAct = dialogLayout.findViewById<EditText>(R.id.nivelActual)
            spinnerNombres.setAdapter(adapter);
            spinnerNatus.setAdapter(adapter2);


            //dialogo
            with(builder) {
                setTitle("Añadir nuevo Pokémon")
                setPositiveButton("Enviar") { dialog, which ->

                    val poke = Pokemon(
                        spinnerNombres.selectedItemPosition + 1,
                        spinnerNombres.selectedItem.toString(),
                        Integer.parseInt(campoNivelAct.text.toString()),
                        Integer.parseInt(campoNivelCap.text.toString()),
                        TipoPokemon.fromString(Pokemon.datosOficialesPokemon[spinnerNombres.selectedItemPosition][2])!!,
                        TipoPokemon.fromString(Pokemon.datosOficialesPokemon[spinnerNombres.selectedItemPosition][3])!!,
                        usuarioActivo,
                        NaturalezaPokemon.fromString(spinnerNatus.selectedItem.toString()),

                    )

                    annadirPokemonaBD(poke)
                }
                setNegativeButton("Cancelar") { dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }
                setView(dialogLayout)
                show()
            }

        }
    }

    //funcion para buscar pokemon
    fun buscarPokemon() {

        //lineas que rellenaran el spinner de nombres
        var listaNombres = ArrayList<String>()
        for (Elemento in Pokemon.datosOficialesPokemon) {
            listaNombres.add(Elemento[1])
            println(Elemento[1])
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, listaNombres)

        //click listener
        findViewById<ImageButton>(R.id.btnBuscar).setOnClickListener() {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialogo_busquedas, null)
            val spinnerNombres = dialogLayout.findViewById<Spinner>(R.id.campo_Buscar_por_nombre)
            val campoNivel = dialogLayout.findViewById<EditText>(R.id.campo_Buscar_por_nivel)
            val boton_buscar_nombres = dialogLayout.findViewById<Button>(R.id.btn_Buscar_por_nombre)
            val boton_buscar_nivel = dialogLayout.findViewById<Button>(R.id.btn_Buscar_por_nivel)
            val boton_limpiar_filtros = dialogLayout.findViewById<Button>(R.id.btn_Limpiar_filtros)

            spinnerNombres.setAdapter(adapter)

            //dialogo
            val dialog = with(builder) {
                setTitle("Buscar Pokémon")

                setView(dialogLayout)
                show()



            }

            boton_buscar_nombres.setOnClickListener {
                var nombrePruebas = spinnerNombres.selectedItem.toString()   //pendiente de crear ventana emergente y obtener el valor de un spinner
                var listaFiltrada = buscarPokemonNombre(nombrePruebas, listaPokemon)
                adaptadorPokemon.listaPokemon = listaFiltrada
                adaptadorPokemon.notifyDataSetChanged()
                dialog.dismiss()
            }

            boton_buscar_nivel.setOnClickListener {
                var nivelPokemon = Integer.parseInt(campoNivel.text.toString())   //pendiente de crear ventana emergente y obtener el valor de un spinner
                var listaFiltrada = buscarPokemonSuperiorANivel(nivelPokemon, listaPokemon)
                adaptadorPokemon.listaPokemon = listaFiltrada
                adaptadorPokemon.notifyDataSetChanged()
                dialog.dismiss()
            }

            boton_limpiar_filtros.setOnClickListener {
                reiniciarRecyclerView()
                dialog.dismiss()
            }

        }
    }

    //recarga la lista de elementos
    fun reiniciarRecyclerView() {
        Toast.makeText(this, "Limpiar filtros", Toast.LENGTH_SHORT).show()
        bbdd.collection("pokemon").get().addOnSuccessListener {

            listaPokemon.clear()
            it.documents.forEach { documento ->
                println(documento.id)
                val p = documento.toObject(Pokemon::class.java)
                //le asignamos el id antes de meterlo en el arraylist
                if (p != null) {
                    p.primaryK = documento.id
                    listaPokemon.add(p)
                }


            }
            cargarRecycler()
        }
    }

    //crea un pokemon
    fun annadirPokemonaBD(poke : Pokemon) {

        val pokehash = hashMapOf(
            "numero" to poke.numero,
            "nombre" to poke.nombre,
            "duenio" to poke.duenio,
            "naturaleza" to poke.naturaleza,
            "nivelActual" to poke.nivelActual,
            "nivelCapturado" to poke.nivelCapturado,
            "tipo1" to poke.tipo1,
            "tipo2" to poke.tipo2
        )

        bbdd.collection("pokemon").document().set(pokehash)
    }

    //para enviar sms
    fun listenerSMS() {
        //listener para el boton de SMS
        findViewById<ImageButton>(R.id.botonSMS).setOnClickListener() {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialogo_sms, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.editTextNumber)

            with(builder) {
                setTitle("Envio de SMS")
                setPositiveButton("Enviar") { dialog, which ->
                    if (editText.text.toString().length == 9) {
                        comprobarPermisosSMS()
                        enviarSMS(editText.text.toString())
                    }
                }
                setNegativeButton("Cancelar") { dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }
                setView(dialogLayout)
                show()
            }

        }
    }

    //para enviar sms
    fun enviarSMS(numero: String) {
        var mejorPoke : Pokemon = listaPokemon[0]
        for (Pokemon in listaPokemon) {
            if (Pokemon.nivelActual > mejorPoke.nivelActual)
                mejorPoke = Pokemon
        }
        SmsManager.getDefault().sendTextMessage(numero, null, "El ${mejorPoke.nombre} de ${mejorPoke.duenio}, con nivel ${mejorPoke.nivelActual}, es el mejor Pokémon de nuestra base de datos.", null, null)

    }

    //comprobar si la app tiene permisos para enviar sms
    fun comprobarPermisosSMS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 101)
        }
    }

}