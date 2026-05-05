# PelisApp

Este repositorio contiene una aplicación Android sencilla para gestionar un catálogo de películas y sus actores mediante un API REST.

## Resumen

La app ofrece las siguientes funcionalidades:
- Listar todas las películas disponibles en el API.
- Crear, buscar (por ID), actualizar y eliminar películas.
- Listar actores asociados a películas, crear, actualizar y eliminar actores ligados a una película.

La comunicación con el backend se realiza mediante `ApiClient` (OkHttp + Gson).

## Estructura principal (lo desarrollado)

- Actividades principales:
  - `MainActivity` — Menú principal con botones para ir a las vistas de Películas y Actores.
  - `PeliculasActivity` — Interfaz para crear, buscar, actualizar, borrar y listar películas.
  - `ActoresActivity` — Interfaz para crear, actualizar, borrar y listar actores (por película o el catálogo completo).

- Modelos:
  - `Pelicula` — POJO con campos: `id`, `titulo`, `director`, `anio`, `genero`, `duracion_minutos`, `calificacion`, `creada_en`, `actualizada_en`, `actores` (lista de `Actor`).
  - `Actor` — POJO con campos: `id`, `nombre_actor`, `rol`, `creada_en`.

- Cliente HTTP:
  - `ApiClient` — Implementa llamadas al API usando OkHttp y Gson. Contiene métodos para las rutas de películas y actores (GET/POST/PUT/DELETE). La base URL configurada es `https://api-peliculas-0ryj.onrender.com/`.

- Layouts XML:
  - `activity_main.xml` — Menú con dos botones.
  - `activity_peliculas.xml` — Formulario y botones para CRUD de películas y un TextView para resultados.
  - `activity_actores.xml` — Formulario y botones para CRUD de actores y TextView para resultados.

## Archivos clave (ubicaciones)

- Código Java:
  - app/src/main/java/com/example/pelisapp/ApiClient.java
  - app/src/main/java/com/example/pelisapp/Pelicula.java
  - app/src/main/java/com/example/pelisapp/Actor.java
  - app/src/main/java/com/example/pelisapp/MainActivity.java
  - app/src/main/java/com/example/pelisapp/PeliculasActivity.java
  - app/src/main/java/com/example/pelisapp/ActoresActivity.java

- Layouts:
  - app/src/main/res/layout/activity_main.xml
  - app/src/main/res/layout/activity_peliculas.xml
  - app/src/main/res/layout/activity_actores.xml

## Detalles de `ApiClient`

`ApiClient` usa `OkHttpClient` con tiempo de espera de 30s y `Gson` para (de)serializar JSON. Métodos implementados (resumen):

- Películas:
  - `getPeliculas()` → GET `/api/peliculas`
  - `getPelicula(id)` → GET `/api/peliculas/:id`
  - `createPelicula(pelicula)` → POST `/api/peliculas`
  - `updatePelicula(id, pelicula)` → PUT `/api/peliculas/:id`
  - `deletePelicula(id)` → DELETE `/api/peliculas/:id`
  - `getPeliculasConActores()` → GET `/api/peliculas/con-actores/listado` (usar en listado de actores)

- Actores (anidados bajo una película):
  - `getActoresPorPelicula(peliculaId)` → GET `/api/peliculas/:id/actores`
  - `createActor(peliculaId, actor)` → POST `/api/peliculas/:id/actores`
  - `updateActor(peliculaId, actorId, actor)` → PUT `/api/peliculas/:id/actores/:actor_id`
  - `deleteActor(peliculaId, actorId)` → DELETE `/api/peliculas/:id/actores/:actor_id`

`ApiClient.validarRespuesta` lanza excepción si el servidor devuelve código HTTP no exitoso.

## Requisitos y dependencias

- Android Studio (recomendado) con un SDK Android compatible.
- Permiso de Internet: declarado en `AndroidManifest.xml`.
- Dependencias (ver `app/build.gradle.kts`):
  - OkHttp (okhttp3)
  - Gson
  - Material Components (layouts usan TextInputLayout)

Comprueba `app/build.gradle.kts` para las versiones exactas.

## Configuración necesaria para ejecutar

1. Abrir el proyecto en Android Studio (`File > Open` en la carpeta raíz del repo).
2. Asegurarse de tener un emulator o dispositivo físico conectado.
3. El `AndroidManifest.xml` ya incluye `<uses-permission android:name="android.permission.INTERNET" />`.
4. La app se conecta a `https://api-peliculas-0ryj.onrender.com/`. Si usas otro backend, actualiza `BASE_URL` en `ApiClient.java`.
5. Si tu API es HTTP (no HTTPS), revisa `android:usesCleartextTraffic` en el `AndroidManifest.xml` (actualmente configurado en `true`).

## Ejecución

En Android Studio:

1. Selecciona un dispositivo/emulador.
2. Run > Run 'app' (o botón ▶️). La app compilará y desplegará.

## Puntos importantes y recomendaciones

- Errores de red se muestran como Toasts o mensajes en los TextView.
- El código ejecuta las llamadas de red en un `ExecutorService` (hilo en segundo plano) y luego actualiza la UI con `runOnUiThread`.
- Validaciones de entrada en formularios son básicas (números para ID, año, duración, decimales para calificación): mejorar validación/UX si es necesario.
- Manejo de errores del API: `ApiClient` lanza excepciones con mensajes HTTP; se recomienda mejorar el parsing de errores para mostrar mensajes más amigables.

## Mejoras posibles

- Usar Retrofit (con OkHttp y Gson) para simplificar llamadas HTTP y manejo de responses.
- Añadir RecyclerView para mostrar listas de películas/actores en vez de usar un `TextView` grande.
- Añadir progreso/loader para operaciones de red.
- Manejo avanzado de errores y retry/backoff.

## Contacto / Siguientes pasos

Si quieres, puedo:
- Añadir screenshots y una guía rápida de uso.
- Convertir las listas a RecyclerView y diseñar adaptadores.
- Integrar Retrofit y migrar `ApiClient`.

---
Generado automáticamente: documentación inicial de la app basada en el código fuente presente.
