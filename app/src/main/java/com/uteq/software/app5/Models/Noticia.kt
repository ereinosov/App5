package com.uteq.software.app5.Models

data class Noticia(
    val ntUrlPortada: String?,
    val ntTitular: String?,
    val ntUrlNoticia: String?,
    val ntFecha: String?,
    val objDepartamento: Departamento?,
    val objCategoriaNotc: CategoriaNoticia?
) {
    val urlPortadaCompleta: String
        get() = "https://uteq.edu.ec/assets/images/news/pagina/" + (ntUrlPortada ?: "")

    val urlNoticiaCompleta: String
        get() = "https://uteq.edu.ec/es/comunicacion/noticia/" + (ntUrlNoticia ?: "")
}