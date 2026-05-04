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

public class PeliculasActivity extends AppCompatActivity {

    EditText txtId, txtTitulo, txtDirector, txtAnio, txtGenero, txtDuracion, txtCalificacion;
    Button btnCrear, btnBuscar, btnActualizar, btnEliminar, btnListarPeliculas, btnVolverMenuPeliculas;
    TextView lblResultadosPeliculas;
    // Hilo secundario para las peticiones de red
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas);

        // Enlaces UI
        txtId = findViewById(R.id.txtId);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDirector = findViewById(R.id.txtDirector);
        txtAnio = findViewById(R.id.txtAnio);
        txtGenero = findViewById(R.id.txtGenero);
        txtDuracion = findViewById(R.id.txtDuracion);
        txtCalificacion = findViewById(R.id.txtCalificacion);
        btnListarPeliculas = findViewById(R.id.btnListarPeliculas);
        lblResultadosPeliculas = findViewById(R.id.lblResultadosPeliculas);


        btnCrear = findViewById(R.id.btnCrear);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolverMenuPeliculas = findViewById(R.id.btnVolverMenuPeliculas);


        // Asignación de eventos
        btnCrear.setOnClickListener(v -> crearPelicula());
        btnBuscar.setOnClickListener(v -> buscarPelicula());
        btnActualizar.setOnClickListener(v -> actualizarPelicula());
        btnEliminar.setOnClickListener(v -> eliminarPelicula());
        btnListarPeliculas.setOnClickListener(v -> listarPeliculas());
        btnVolverMenuPeliculas.setOnClickListener(v -> finish());

    }

    private void listarPeliculas() {
        // Mostramos un texto de carga
        lblResultadosPeliculas.setText("Cargando catálogo desde el API...");

        executor.execute(() -> {
            try {
                // Llamamos a tu ApiClient que ya tiene el método getPeliculas() configurado
                // Asegúrate de importar java.util.List en la parte superior si no lo está
                List<Pelicula> peliculas = ApiClient.getPeliculas();

                runOnUiThread(() -> {
                    if (peliculas.isEmpty()) {
                        lblResultadosPeliculas.setText("No hay películas registradas en el API.");
                        return;
                    }

                    // Usamos StringBuilder para ir uniendo el texto de cada película
                    StringBuilder sb = new StringBuilder();
                    for (Pelicula p : peliculas) {
                        sb.append("ID: ").append(p.id).append(" | ").append(p.titulo).append("\n");
                        sb.append("Director: ").append(p.director).append("\n");
                        sb.append("Año: ").append(p.anio).append(" | Género: ").append(p.genero).append("\n");
                        sb.append("Duración: ").append(p.duracion_minutos).append(" min | Nota: ").append(p.calificacion).append("\n");
                        sb.append("---------------------------------------\n");
                    }

                    // Mostramos toda la información formateada en la pantalla
                    lblResultadosPeliculas.setText(sb.toString());
                });
            } catch (Exception e) {
                runOnUiThread(() -> lblResultadosPeliculas.setText("Error de conexión: " + e.getMessage()));
            }
        });
    }

    private void crearPelicula() {
        try {
            Pelicula p = new Pelicula();
            // Al ser Integer, el id empieza en null y el API lo ignora perfectamente
            p.titulo = txtTitulo.getText().toString().trim();
            p.director = txtDirector.getText().toString().trim();
            p.anio = Integer.parseInt(txtAnio.getText().toString().trim());
            p.genero = txtGenero.getText().toString().trim();
            p.duracion_minutos = Integer.parseInt(txtDuracion.getText().toString().trim());

            // CONVERSIÓN A DOUBLE:
            p.calificacion = Double.parseDouble(txtCalificacion.getText().toString().trim());

            executor.execute(() -> {
                try {
                    Pelicula creada = ApiClient.createPelicula(p);
                    mostrarMensaje("Película creada con ID: " + creada.id);

                    runOnUiThread(() -> {
                        limpiarCampos();
                        listarPeliculas(); // Refresca la lista
                    });
                } catch (Exception e) {
                    mostrarMensaje("Error al crear: " + e.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Revisa que Año, Duración y Calificación sean números válidos", Toast.LENGTH_LONG).show();
        }
    }


    private void actualizarPelicula() {
        String idStr = txtId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa un ID para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Pelicula p = new Pelicula();
            p.titulo = txtTitulo.getText().toString().trim();
            p.director = txtDirector.getText().toString().trim();
            p.anio = Integer.parseInt(txtAnio.getText().toString().trim());
            p.genero = txtGenero.getText().toString().trim();
            p.duracion_minutos = Integer.parseInt(txtDuracion.getText().toString().trim());

            // CONVERSIÓN A DOUBLE:
            p.calificacion = Double.parseDouble(txtCalificacion.getText().toString().trim());

            executor.execute(() -> {
                try {
                    Pelicula actualizada = ApiClient.updatePelicula(id, p);
                    mostrarMensaje("Película actualizada: " + actualizada.titulo);

                    runOnUiThread(() -> {
                        limpiarCampos();
                        listarPeliculas();
                    });
                } catch (Exception e) {
                    mostrarMensaje("Error al actualizar: " + e.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Revisa que Año, Duración y Calificación sean números válidos", Toast.LENGTH_LONG).show();
        }
    }

    private void eliminarPelicula() {
        String idStr = txtId.getText().toString();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa un ID para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);

        executor.execute(() -> {
            try {
                ApiClient.deletePelicula(id);
                mostrarMensaje("Película eliminada correctamente");

                runOnUiThread(() -> {
                    limpiarCampos();
                    listarPeliculas(); // Refrescamos la lista
                });
            } catch (Exception e) {
                mostrarMensaje("Error al eliminar: " + e.getMessage());
            }
        });
    }

    private void buscarPelicula() {
        // Aprovechamos para ponerle .trim() aquí también por si se va un espacio en el ID
        String idStr = txtId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa un ID para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);

        executor.execute(() -> {
            try {
                Pelicula p = ApiClient.getPelicula(id);
                runOnUiThread(() -> {
                    if (p != null) {
                        txtTitulo.setText(p.titulo);
                        txtDirector.setText(p.director);
                        txtAnio.setText(String.valueOf(p.anio));
                        txtGenero.setText(p.genero);
                        txtDuracion.setText(String.valueOf(p.duracion_minutos));
                        txtCalificacion.setText(String.valueOf(p.calificacion));

                        Toast.makeText(this, "Película encontrada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Película no encontrada", Toast.LENGTH_SHORT).show();
                        limpiarCamposSinId();
                    }
                });
            } catch (Exception e) {
                mostrarMensaje("Error en la búsqueda: " + e.getMessage());
            }
        });
    }

    // Utilidad para mostrar Toast desde un hilo secundario hacia el hilo principal (UI)
    private void mostrarMensaje(String mensaje) {
        runOnUiThread(() -> Toast.makeText(PeliculasActivity.this, mensaje, Toast.LENGTH_LONG).show());
    }

    private void limpiarCampos() {
        txtId.setText("");
        limpiarCamposSinId();
    }

    private void limpiarCamposSinId() {
        txtTitulo.setText("");
        txtDirector.setText("");
        txtAnio.setText("");
        txtGenero.setText("");
        txtDuracion.setText("");
        txtCalificacion.setText("");
    }
}