# App5 — Listado de Noticias UTEQ con Volley

![Android](https://img.shields.io/badge/Android-API%2029%2B-3DDC84?style=flat&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Volley](https://img.shields.io/badge/Volley-1.2.1-4285F4?style=flat&logo=android&logoColor=white)
![Gson](https://img.shields.io/badge/Gson-2.11.0-007ACC?style=flat)
![Glide](https://img.shields.io/badge/Glide-4.16.0-18A303?style=flat)
![Conscrypt](https://img.shields.io/badge/Conscrypt-2.5.2-555555?style=flat)
![Android Studio](https://img.shields.io/badge/Android%20Studio-Panda%204-3DDC84?style=flat&logo=androidstudio&logoColor=white)

**Universidad:** Universidad Técnica Estatal de Quevedo (UTEQ)
**Facultad:** Facultad de Ciencias de la Computación (FCC)
**Carrera:** Software
**Asignatura:** Aplicaciones Móviles "A"
**Actividad:** Consumo de Web Services REST con Volley
**Estudiante:** Eduardo Reinoso Vélez
© 2026

---

## Objetivo

Implementar una interfaz Android en Kotlin que consuma el web service REST de noticias de la UTEQ (`apiws.uteq.edu.ec`) autenticado mediante token Bearer, mostrando el listado en un `ListView` con `ArrayAdapter`, carga dinámica de imágenes de portada con Glide y navegación al detalle de cada noticia en el navegador.

---

## Pantallas

| Pantalla | Descripción |
|---|---|
| **MainActivity** | `ListView` con header (logo UTEQ) y footer (créditos), donde cada ítem muestra categoría (con color dinámico según `gtColorIdentf`), titular, imagen de portada, fecha de publicación y enlace a la noticia completa |

---

## Tecnologías

| Tecnología | Versión | Rol |
|---|---|---|
| Android Studio | Panda 4 | IDE de desarrollo |
| Kotlin | 2.3.21 | Lenguaje de programación principal |
| Volley | 1.2.1 | Cliente HTTP para consumo del web service REST |
| Gson | 2.11.0 | Deserialización del JSON de respuesta a `List<Noticia>` |
| Glide | 4.16.0 | Carga de imágenes de portada desde URL |
| Conscrypt | 2.5.2 | Proveedor SSL/TLS para resolver cadenas de certificados incompletas en Android 10 |
| ConstraintLayout | 2.2.1 | Posicionamiento de vistas en el layout principal |
| Material Design | 1.14.0 | Componentes UI (`ProgressBar`, estilos base) |
| Gradle | 9.2.1 | Gestión de dependencias |

---

## Arquitectura

- **Models** — data classes (`Noticia`, `Departamento`, `CategoriaNoticia`) mapeadas desde la respuesta JSON del web service. `Noticia` expone las propiedades calculadas `urlPortadaCompleta` y `urlNoticiaCompleta`, que anteponen las rutas base a `ntUrlPortada` y `ntUrlNoticia` respectivamente.
- **Adapters** — `NoticiaAdapter` extiende `ArrayAdapter<Noticia>` e infla `item_noticia.xml`, pinta la categoría con el color hexadecimal (`gtColorIdentf`) recibido del backend, carga la portada con Glide y maneja el clic para abrir la noticia en el navegador.
- **Services**
  - `NoticiasRequest` — `StringRequest` de Volley que apunta al endpoint `functions/information/entity/1` y agrega el header `Authorization: Bearer <ACCESS_TOKEN>` mediante `getHeaders()`.
  - `VolleySingleton` — patrón Singleton que expone una única `RequestQueue` para toda la app.

---

## Estructura del proyecto

```
App5/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/uteq/software/app5/
│           │   ├── Adapters/
│           │   │   └── NoticiaAdapter.kt
│           │   ├── Models/
│           │   │   ├── Noticia.kt
│           │   │   ├── Departamento.kt
│           │   │   └── CategoriaNoticia.kt
│           │   ├── services/
│           │   │   ├── NoticiasRequest.kt
│           │   │   └── VolleySingleton.kt
│           │   └── MainActivity.kt
│           ├── res/
│           │   ├── layout/
│           │   │   ├── activity_main.xml
│           │   │   ├── item_noticia.xml
│           │   │   ├── header_logo.xml
│           │   │   └── footer_uteq.xml
│           │   ├── drawable/
│           │   │   ├── logouteq.png
│           │   │   ├── ic_calendar.xml
│           │   │   └── ic_link.xml
│           │   └── xml/
│           │       └── network_security_config.xml
│           └── AndroidManifest.xml
├── gradle/
│   └── libs.versions.toml
├── local.properties         # No incluido en el repositorio
└── app/build.gradle.kts
```

---

## Web Service consumido

**URL:** `https://apiws.uteq.edu.ec/h6RPoSoRaah0Y4Bah28eew/functions/information/entity/1`
**Método:** `GET`
**Autenticación:** header `Authorization: Bearer <accessToken>`

### Mapeo de campos

| Campo JSON | Uso |
|---|---|
| `ntTitular` | Titular de la noticia |
| `ntFecha` | Fecha de publicación |
| `ntUrlPortada` | Se antepone `https://uteq.edu.ec/assets/images/news/pagina/` para formar la URL de la imagen de portada |
| `ntUrlNoticia` | Se antepone `https://uteq.edu.ec/es/comunicacion/noticia/` para formar la URL de la noticia completa |
| `objCategoriaNotc.gtTitular` | Nombre de la categoría (Institucional, Académica, etc.) |
| `objCategoriaNotc.gtColorIdentf` | Color hexadecimal aplicado al texto de la categoría |
| `objDepartamento.dpNombre` | Departamento que publica la noticia |

---

## Configuración de credenciales

El `accessToken` del web service de autenticación se almacena en `local.properties` (excluido del repositorio por `.gitignore`) e inyectado en tiempo de compilación mediante `BuildConfig`:

```properties
ACCESS_TOKEN=<token-jwt-obtenido-del-web-service-de-autenticacion>
```

---

## Requisitos previos

- Android Studio Panda 4 o superior
- JDK 11
- Dispositivo o emulador con Android 10+ (API 29)

---

## Instalación y ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/ereinosov/App5.git
```

2. Abrir en Android Studio: **File → Open → seleccionar carpeta `App5`**
3. Agregar el `accessToken` en `local.properties`
4. Sincronizar Gradle: **File → Sync Project with Gradle Files**
5. Ejecutar en dispositivo: **Run → Run 'app'**

---

## Repositorio

https://github.com/ereinosov/App5
