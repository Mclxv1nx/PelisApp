package com.example.pelisapp;

import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static final String BASE_URL = "https://api-peliculas-0ryj.onrender.com/";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private static final Gson gson = new Gson();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // ==========================================
    // RUTAS MAESTRO (PELÍCULAS)
    // ==========================================

    // GET /api/peliculas
    public static List<Pelicula> getPeliculas() throws Exception {
        Request request = new Request.Builder().url(BASE_URL + "api/peliculas").build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
            Type listType = new TypeToken<List<Pelicula>>(){}.getType();
            return gson.fromJson(response.body().string(), listType);
        }
    }

    // GET /api/peliculas/:id
    public static Pelicula getPelicula(int id) throws Exception {
        Request request = new Request.Builder().url(BASE_URL + "api/peliculas/" + id).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 404) return null; // No encontrada
            validarRespuesta(response);
            return gson.fromJson(response.body().string(), Pelicula.class);
        }
    }

    // POST /api/peliculas
    public static Pelicula createPelicula(Pelicula pelicula) throws Exception {
        RequestBody body = RequestBody.create(gson.toJson(pelicula), JSON);
        Request request = new Request.Builder().url(BASE_URL + "api/peliculas").post(body).build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
            return gson.fromJson(response.body().string(), Pelicula.class);
        }
    }

    // PUT /api/peliculas/:id
    public static Pelicula updatePelicula(int id, Pelicula pelicula) throws Exception {
        RequestBody body = RequestBody.create(gson.toJson(pelicula), JSON);
        Request request = new Request.Builder().url(BASE_URL + "api/peliculas/" + id).put(body).build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
            return gson.fromJson(response.body().string(), Pelicula.class);
        }
    }

    // DELETE /api/peliculas/:id
    public static void deletePelicula(int id) throws Exception {
        Request request = new Request.Builder().url(BASE_URL + "api/peliculas/" + id).delete().build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
        }
    }

    // ==========================================
    // RUTAS DETALLE (ACTORES DE UNA PELÍCULA)
    // ==========================================

    // GET /api/peliculas/:id/actores
    public static List<Actor> getActoresPorPelicula(int peliculaId) throws Exception {
        Request request = new Request.Builder().url(BASE_URL + "api/peliculas/" + peliculaId + "/actores").build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
            Type listType = new TypeToken<List<Actor>>(){}.getType();
            return gson.fromJson(response.body().string(), listType);
        }
    }

    // POST /api/peliculas/:id/actores
    public static Actor createActor(int peliculaId, Actor actor) throws Exception {
        RequestBody body = RequestBody.create(gson.toJson(actor), JSON);
        Request request = new Request.Builder().url(BASE_URL + "api/peliculas/" + peliculaId + "/actores").post(body).build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
            return gson.fromJson(response.body().string(), Actor.class);
        }
    }

    // PUT /api/peliculas/:id/actores/:actor_id
    public static Actor updateActor(int peliculaId, int actorId, Actor actor) throws Exception {
        RequestBody body = RequestBody.create(gson.toJson(actor), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "api/peliculas/" + peliculaId + "/actores/" + actorId)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
            return gson.fromJson(response.body().string(), Actor.class);
        }
    }

    // DELETE /api/peliculas/:id/actores/:actor_id
    public static void deleteActor(int peliculaId, int actorId) throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL + "api/peliculas/" + peliculaId + "/actores/" + actorId)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
        }
    }

    // ==========================================
    // MÉTODOS DE UTILIDAD
    // ==========================================

    private static void validarRespuesta(Response response) throws Exception {
        if (!response.isSuccessful()) {
            String errorBody = response.body() != null ? response.body().string() : "Error desconocido";
            throw new Exception("Error HTTP " + response.code() + ": " + errorBody);
        }
    }
    // GET /api/peliculas/con-actores/listado
    public static List<Pelicula> getPeliculasConActores() throws Exception {
        Request request = new Request.Builder().url(BASE_URL + "api/peliculas/con-actores/listado").build();
        try (Response response = client.newCall(request).execute()) {
            validarRespuesta(response);
            Type listType = new TypeToken<List<Pelicula>>(){}.getType();
            return gson.fromJson(response.body().string(), listType);
        }
    }
}