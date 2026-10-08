package com.gajodoramas.firebasepokemon

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import render.animations.Fade
import render.animations.Render
import kotlin.random.Random

class Login : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var botonRegistrar: Button
    lateinit var botonAcceder: Button
    lateinit var botonRecuperar: Button
    lateinit var campoCorreo: EditText
    lateinit var campoContrasena: EditText
    lateinit var imagen: ImageView
    var mediaPlayer: MediaPlayer = MediaPlayer() // musica
    var render: Render = Render(this@Login) //Animaciones


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        variables_y_listeners()
    }

    //cambia las imagenes del menu de login con un hilo
    fun cambiarImagen() {

        class GestionHilo : Runnable {
            override fun run() {
                while (true) {
                    runOnUiThread {
                        render.setAnimation(Fade().InRight(imagen))
                        render.start()
                        Picasso.get().load(stringURL(Random.nextInt(1, 1009))).into(imagen)
                    }
                    Thread.sleep(2500)
                }
            }
        }
        Thread(GestionHilo()).start()
    }

    //declaracion o inicializacion de variables y listeners
    fun variables_y_listeners() {

        mediaPlayer = MediaPlayer.create(this, R.raw.pokemon)
        mediaPlayer.start()
        mediaPlayer.isLooping = true

        botonRegistrar = findViewById(R.id.login_botonRegistrar)
        botonAcceder = findViewById(R.id.login_botonAcceder)
        botonRecuperar = findViewById(R.id.login_botonRecuperar)
        campoCorreo = findViewById(R.id.login_campoCorreo)
        campoContrasena = findViewById(R.id.login_campoContrasena)
        imagen = findViewById(R.id.login_imagen)
        cambiarImagen()
        auth = FirebaseAuth.getInstance()
        botonRegistrar.setOnClickListener {
            if (campoCorreo.text.isBlank() || campoContrasena.text.isBlank()) {
                Toast.makeText(
                    applicationContext,
                    "Por favor, rellene todos los campos.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                registrarUsuario()
            }
        }

        botonAcceder.setOnClickListener {
            if (campoCorreo.text.isBlank() || campoContrasena.text.isBlank()) {
                Toast.makeText(
                    applicationContext,
                    "Por favor, rellene todos los campos.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                logearUsuario()
            }
        }

        botonRecuperar.setOnClickListener {
            if (campoCorreo.text.isBlank()) {
                Toast.makeText(
                    applicationContext,
                    "Por favor, rellene el campo del correo electrónico.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                if (campoCorreo.text.toString()
                        .matches("[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}".toRegex())
                ) {
                    recuperarContrasena()

                } else {
                    Toast.makeText(
                        applicationContext,
                        "El campo correo electrónico no cumple con el formato necesario.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    //recuperacion de contraseña
    fun recuperarContrasena() {
        auth.sendPasswordResetEmail(campoCorreo.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Se le ha enviado un correo electrónico para restablecer su contraseña.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Hubo un error: " + (task.exception?.localizedMessage),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //registra al usuario
    fun registrarUsuario() {
        auth.createUserWithEmailAndPassword(
            campoCorreo.text.toString(),
            campoContrasena.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Se ha creado el usuario.", Toast.LENGTH_LONG)
                    .show()
                accederApp()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Hubo un error: " + (task.exception?.localizedMessage),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //inicia sesion
    fun logearUsuario() {
        auth.signInWithEmailAndPassword(
            campoCorreo.text.toString(),
            campoContrasena.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Se ha iniciado sesión correctamente",
                    Toast.LENGTH_LONG
                ).show()
                accederApp()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Hubo un error: " + (task.exception?.localizedMessage),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //lanza la siguiente actividad
    fun accederApp() {
        var i: Intent = Intent(this, MainActivity::class.java)
        i.putExtra("correo", campoCorreo.text.toString())
        startActivity(i)
    }

    //a partir del numero del pokemon devuelve la url de la foto
    fun stringURL(numero: Int): String {
        return when (numero.toString().length) {
            1 -> "https://assets.pokemon.com/assets/cms2/img/pokedex/full/00$numero.png"
            2 -> "https://assets.pokemon.com/assets/cms2/img/pokedex/full/0$numero.png"
            else -> "https://assets.pokemon.com/assets/cms2/img/pokedex/full/$numero.png"
        }
    }


}