package com.uteq.software.app5
import org.conscrypt.Conscrypt
import java.security.Security
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uteq.software.app5.Adapters.NoticiaAdapter
import com.uteq.software.app5.Models.Noticia
import com.uteq.software.app5.services.NoticiasRequest
import com.uteq.software.app5.services.VolleySingleton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listNoticias = findViewById<ListView>(R.id.listNoticias)
        val progressNoticias = findViewById<ProgressBar>(R.id.progressNoticias)

        val header = layoutInflater.inflate(R.layout.header_logo, listNoticias, false)
        val footer = layoutInflater.inflate(R.layout.footer_uteq, listNoticias, false)
        listNoticias.addHeaderView(header)
        listNoticias.addFooterView(footer)

        progressNoticias.visibility = View.VISIBLE

        val request = NoticiasRequest(
            { response ->
                progressNoticias.visibility = View.GONE
                val tipoLista = object : TypeToken<List<Noticia>>() {}.type
                val noticias: List<Noticia> = Gson().fromJson(response, tipoLista)
                listNoticias.adapter = NoticiaAdapter(this@MainActivity, ArrayList(noticias))
            },
            { error ->
                progressNoticias.visibility = View.GONE
                val codigo = error.networkResponse?.statusCode
                val msg = if (codigo != null) "Error $codigo" else "Fallo de red: ${error.message}"
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        )

        VolleySingleton.getInstance(this).requestQueue.add(request)
    }
}