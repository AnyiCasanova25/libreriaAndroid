package com.example.biblioteca

import android.media.audiofx.DynamicsProcessing.Config
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.biblioteca.config.config
import com.example.biblioteca.models.libro
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.Exception
import java.security.Guard
import java.util.Base64

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [guardarLibroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class guardarLibroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var txtTitulo:EditText
    private lateinit var txtAutor:EditText
    private lateinit var txtISBN:EditText
    private lateinit var txtGenero:EditText
    private lateinit var txtEjemplaresDisponibles:EditText
    private lateinit var txtEjemplaresOcupados:EditText
    private var id:String=""
    private lateinit var btnGuardar: Button
    /*
     * metodo encargado de traer la informacion del libro
     */
    fun consultarLibro(){
        /*
        * solo debemos consultar si tenemos id
        */
        if (id!=""){
            var request=JsonObjectRequest(
                Method.GET,
                config.urllibro+id, //url
                null, //parametros
                {response->
                    //variable response contiene la respuesta de la api
                    //se convierte en json a un objeto tip libro
                    val gson= Gson()
                    val libro:libro=gson.fromJson(response.toString(),libro::class.java)
                    txtAutor.setText(response.getString("autor"))
                    txtTitulo.setText(response.getString("titulo"))
                    txtISBN.setText(response.getString("isbn"))
                    txtGenero.setText(response.getString("genero"))
                    txtEjemplaresDisponibles.setText(response.getString("ejemplaresDisponibles"))
                    txtEjemplaresOcupados.setText(response.getString("ejemplaresOcupados"))

                var prueba=response
                }, //respuesta correcta
                { error->
                    Toast.makeText(context, "Error al consultar", Toast.LENGTH_LONG).show()
                }//error en la petición
            )
            var queue=Volley.newRequestQueue(context)
            queue.add(request)
        }
    }
    fun guardarLibro(){
        try {
            if (id==""){// se crea el libro
                var parametros=JSONObject()
                parametros.put("titulo",txtTitulo.text.toString())
                parametros.put("autor",txtAutor.text.toString())
                parametros.put("isbn",txtISBN.text.toString())
                parametros.put("genero",txtGenero.text.toString())
                parametros.put("ejemplaresDisponibles",txtEjemplaresDisponibles.text.toString())
                parametros.put("ejemplaresOcupados",txtEjemplaresOcupados.text.toString())
//uno por cada dato que se requiere

                var request=JsonObjectRequest(
                    Request.Method.POST, //método
                    config.urllibro,//url
                    parametros,//datos de la petición
                    {response->
                        Toast.makeText(
                            context,
                            "Se guardo correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                    },//cuando la respuesta es correcta
                    {error->
                       /* val decodeByte= Base64.getDecoder().decode(error.networkResponse.data)
                        val decoderString=String(decodeByte, Charsets.UTF_8)
                        var json= JSONObject(decoderString)*/
                        Toast.makeText(
                            context,
                            "Se genero un error {${error.networkResponse.data}}",
                            Toast.LENGTH_LONG
                        ).show()
                    } //cuando es incorrecta
                )
//se crea la cola de trabajo y se añade la petición
                var queue=Volley.newRequestQueue(context)
//se añade la petición
                queue.add(request)
            } else {
                // se actualiza el libro
                val request = object : StringRequest(
                    Request.Method.PUT,
                    config.urllibro +  id,
                    Response.Listener {
                        Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_LONG).show()
                    },
                    Response.ErrorListener {
                        Toast.makeText(context, "Error al actualizar el libro", Toast.LENGTH_LONG).show()
                    }
                ) {
                    override fun getParams(): Map<String, String> {
                        var parametros=HashMap<String, String>()
                        parametros.put("titulo", txtTitulo.text.toString())
                        parametros.put("autor", txtAutor.text.toString())
                        parametros.put("ISBN", txtISBN.text.toString())
                        parametros.put("genero", txtGenero.text.toString())
                        parametros.put("ejemplaresDisponibles", txtEjemplaresDisponibles.text.toString())
                        parametros.put("ejemplaresOcupados", txtEjemplaresOcupados.text.toString())
                        //uno por casa dato que requiera
                        return parametros
                    }
                }
                // se crea la cola de trabajo
                val queue = Volley.newRequestQueue(context)
                // se añade la petición a la cola
                queue.add(request)
            }
        }catch (error:Exception){

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_guardar_libro, container,false)
        txtTitulo=view.findViewById(R.id.txtTitulo)
        txtAutor=view.findViewById(R.id.txtAutor)
        txtISBN=view.findViewById(R.id.txtISBN)
        txtGenero=view.findViewById(R.id.txtGenero)
        txtEjemplaresDisponibles=view.findViewById(R.id.txtEjemplaresDisponibles)
        txtEjemplaresOcupados=view.findViewById(R.id.txtEjemplaresOcupados)
        btnGuardar=view.findViewById(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            guardarLibro()
        }
        consultarLibro()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment guardarLibroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            guardarLibroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}