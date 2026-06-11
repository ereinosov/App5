package com.uteq.software.app5.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.uteq.software.app5.Models.Noticia
import com.uteq.software.app5.R

class NoticiaAdapter(context: Context, var noticias: ArrayList<Noticia>) : ArrayAdapter<Noticia>(
    context,
    R.layout.item_noticia,
    noticias
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater
            .from(context)
            .inflate(R.layout.item_noticia, parent, false)

        val noticia = noticias[position]

        val imgPortada      = view.findViewById<ImageView>(R.id.imgPortada)
        val txtCategoria    = view.findViewById<TextView>(R.id.txtCategoria)
        val txtTitular      = view.findViewById<TextView>(R.id.txtTitular)
        val txtFecha        = view.findViewById<TextView>(R.id.txtFecha)
        val txtDepartamento = view.findViewById<TextView>(R.id.txtDepartamento)

        txtTitular.text = noticia.ntTitular
        txtFecha.text = "Publicada el: " + noticia.ntFecha
        txtDepartamento.text = noticia.objDepartamento?.dpNombre

        txtCategoria.text = noticia.objCategoriaNotc?.gtTitular
        try {
            val color = Color.parseColor(noticia.objCategoriaNotc?.gtColorIdentf ?: "#5A884B")
            txtCategoria.setTextColor(color)
        } catch (e: IllegalArgumentException) {
            txtCategoria.setTextColor(Color.DKGRAY)
        }

        Glide.with(context)
            .load(noticia.urlPortadaCompleta)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_gallery)
            .centerCrop()
            .into(imgPortada)

        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(noticia.urlNoticiaCompleta))
            context.startActivity(intent)
        }

        return view
    }
}