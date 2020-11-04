package mx.tecnm.tepic.ladm_u2_tarea2_procpara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var calculando = 0
    var hiloSuerte=Hilo(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        felicidades.text=""
        mensaje.text=""
        numero.text=""


        iniciar.setOnClickListener {
            try {
                if(hiloSuerte.unavez==true)//Si es la primera vez que se "juega"
                {
                    hiloSuerte.start()
                    mensaje.text="Calculando..."
                }
                else
                {
                    AlertDialog.Builder(this)
                        .setMessage("PARA TENTAR LA SUERTE DE NUEVO, USA EL BOTON REPETIR")
                        .setTitle("Atención")
                        .setPositiveButton("OK"){d,i->
                            d.dismiss()
                        }
                        .show()
                }
            }
            catch (e:Exception)
            {
                AlertDialog.Builder(this)
                    .setMessage("LA SUERTE NO ESTÁ DE TU LADO")
                    .setTitle("Atención")
                    .setPositiveButton("OK"){d,i->
                        d.dismiss()
                    }
                    .show()
            }
        }

        pausar.setOnClickListener {
            hiloSuerte.pausar()
        }

        detener.setOnClickListener {
            hiloSuerte.detenerHilo()
            felicidades.text="¡FELICIDADES!"
            mensaje.text="Tu número de la suerte es:"
            numero.text=""+hiloSuerte.suerte
        }

        reiniciar.setOnClickListener {
            //Un hilo que fue terminado (kill) NO SE PUEDE REINICIAR, pero se puede crear otro con el mismo metodo
            hiloSuerte.mantener=true
            calculando = 0
            felicidades.text=""
            mensaje.text="Calculando..."
            hiloSuerte.start()
        }
    }
}

//Clase HILO. Esta contendrá los metodos para manipular el hilo

class Hilo(p:MainActivity):Thread(){
    var puntero = p
    var mantener = true //Bandera
    var despausar = true //Bandera
    var unavez = true //Bandera
    val rNumero = Random() //Objeto random
    var suerte:Int = 0 //Numero ENTERO random

    //Metodo para terminar el hilo
    fun detenerHilo()
    {
        mantener=false //Mantener permite verificar si el hilo sigue corriendo
        unavez=false //Esto verifica que el programa INICIE una vez, y fuerce a REPETIR
    }


    //Metodo para pausar el hilo
    fun pausar()
    {
        despausar = !despausar //Es decir, si está true, la pondrá en false y viceversa
    }


    //Metodo para iniciar el hilo
    override fun run()
    {
        super.run()
        while(mantener) //Mientras mantener no cambie a false
        {
            if(despausar==true)//si no se ha pausado el hilo
            {
                puntero.calculando++
                puntero.runOnUiThread {
                    suerte = rNumero.nextInt(5000)
                    puntero.numero.text=""+puntero.calculando
                }
            }
            sleep(100)
        }
    }
}