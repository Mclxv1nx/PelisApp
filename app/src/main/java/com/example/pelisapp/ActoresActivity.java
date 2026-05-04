package com.example.pelisapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActoresActivity extends AppCompatActivity {

    EditText txtActPeliculaId, txtActActorId, txtActNombre, txtActRol;
    Button btnCrearActor, btnListarActores, btnActualizarActor, btnEliminarActor, btnVolverMenuActores;
    TextView lblResultadosActores;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actores);

        txtActPeliculaId = findViewById(R.id.txtActPeliculaId);
        txtActActorId = findViewById(R.id.txtActActorId);
        txtActNombre = findViewById(R.id.txtActNombre);
        txtActRol = findViewById(R.id.txtActRol);
        lblResultadosActores = findViewById(R.id.lblResultadosActores);

        btnCrearActor = findViewById(R.id.btnCrearActor);
        btnListarActores = findViewById(R.id.btnListarActores);
        btnActualizarActor = findViewById(R.id.btnActualizarActor);
        btnEliminarActor = findViewById(R.id.btnEliminarActor);
        btnVolverMenuActores = findViewById(R.id.btnVolverMenuActores);

        // Eventos
        btnCrearActor.setOnClickListener(v -> crearActor());
        btnListarActores.setOnClickListener(v -> listarTodosLosActores()); // Cambiado para listar TODOS
        btnActualizarActor.setOnClickListener(v -> actualizarActor());
        btnEliminarActor.setOnClickListener(v -> eliminarActor());
        btnVolverMenuActores.setOnClickListener(v -> finish());

        listarTodosLosActores();
    }

    // NUEVO MÉTODO: Trae todas las películas, extrae sus actores y los muestra
    private void listarTodosLosActores() {
        lblResultadosActores.setText("Cargando catálogo completo de actores...");

        executor.execute(() -> {
            try {
                // Llamamos a la nueva ruta que trae películas con actores incluidos
                List<Pelicula> peliculas = ApiClient.getPeliculasConActores();

                runOnUiThread(() -> {
                    StringBuilder sb = new StringBuilder();

                    // Recorremos cada película
                    for (Pelicula p : peliculas) {
                        // Si la película tiene actores, los listamos
                        if (p.actores != null && !p.actores.isEmpty()) {
                            sb.append("🎬 PELÍCULA: ").append(p.titulo).append(" (ID: ").append(p.id).append(")\n");
                            for (Actor a : p.actores) {
                                sb.append("   ↳ Actor ID: ").append(a.id)
                                        .append(" | ").append(a.nombre_actor)
                                        .append(" (").append(a.rol).append(")\n");
                            }
                            sb.append("\n");
                        }
                    }

                    if (sb.length() == 0) {
                        lblResultadosActores.setText("No hay actores registrados en la base de datos.");
                    } else {
                        lblResultadosActores.setText(sb.toString());
                    }
                });
            } catch (Exception e) {
                mostrarMensaje("Error al cargar actores: " + e.getMessage());
            }
        });
    }

    private void crearActor() {
        String peliIdStr = txtActPeliculaId.getText().toString();
        if (peliIdStr.isEmpty()) {
            Toast.makeText(this, "Se requiere el ID de la película para asignar el actor", Toast.LENGTH_SHORT).show();
            return;
        }

        Actor actor = new Actor();
        actor.nombre_actor = txtActNombre.getText().toString();
        actor.rol = txtActRol.getText().toString();

        executor.execute(() -> {
            try {
                Actor creado = ApiClient.createActor(Integer.parseInt(peliIdStr), actor);
                mostrarMensaje("Actor creado exitosamente");
                runOnUiThread(() -> {
                    limpiarCampos();
                    listarTodosLosActores(); // Refrescamos la lista general
                });
            } catch (Exception e) {
                mostrarMensaje("Error: " + e.getMessage());
            }
        });
    }

    private void actualizarActor() {
        String peliIdStr = txtActPeliculaId.getText().toString();
        String actorIdStr = txtActActorId.getText().toString();

        if (peliIdStr.isEmpty() || actorIdStr.isEmpty()) {
            Toast.makeText(this, "Se requieren ID de película y de actor", Toast.LENGTH_SHORT).show();
            return;
        }

        Actor actor = new Actor();
        actor.nombre_actor = txtActNombre.getText().toString();
        actor.rol = txtActRol.getText().toString();

        executor.execute(() -> {
            try {
                ApiClient.updateActor(Integer.parseInt(peliIdStr), Integer.parseInt(actorIdStr), actor);
                mostrarMensaje("Actor actualizado correctamente");
                runOnUiThread(() -> {
                    limpiarCampos();
                    listarTodosLosActores(); // Refrescamos la lista general
                });
            } catch (Exception e) {
                mostrarMensaje("Error: " + e.getMessage());
            }
        });
    }

    private void eliminarActor() {
        String peliIdStr = txtActPeliculaId.getText().toString();
        String actorIdStr = txtActActorId.getText().toString();

        if (peliIdStr.isEmpty() || actorIdStr.isEmpty()) {
            Toast.makeText(this, "Se requieren ID de película y de actor", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            try {
                ApiClient.deleteActor(Integer.parseInt(peliIdStr), Integer.parseInt(actorIdStr));
                mostrarMensaje("Actor eliminado");
                runOnUiThread(() -> {
                    limpiarCampos();
                    listarTodosLosActores(); // Refrescamos la lista general
                });
            } catch (Exception e) {
                mostrarMensaje("Error: " + e.getMessage());
            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        runOnUiThread(() -> Toast.makeText(ActoresActivity.this, mensaje, Toast.LENGTH_LONG).show());
    }

    private void limpiarCampos() {
        txtActActorId.setText("");
        txtActNombre.setText("");
        txtActRol.setText("");
    }
}