package com.example.nativo.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nativo.R;
import com.example.nativo.database.DatabaseHelper;
import com.example.nativo.model.Poliza;

import java.util.ArrayList;
import java.util.List;

public class ListarDatosActivity extends AppCompatActivity {

    ListView lv_datos;

    ListView lv_datos_cedula_seleccionada;

    EditText et_buscar_cedula;
    Button btn_buscar;
    Button btn_eliminar, btn_volver;
    List<Poliza> listaPolizas;
    String cedula;
    PolizaAdapter adapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listar_datos);

        lv_datos = findViewById(R.id.lv_datos);

        lv_datos_cedula_seleccionada = findViewById(R.id.lv_datos_cedula_seleccionada);
        //btn_eliminar = findViewById(R.id.btn_eliminar);
        btn_volver = findViewById(R.id.btn_volver);

        et_buscar_cedula = findViewById(R.id.et_buscar_cedula);
        btn_buscar = findViewById(R.id.btn_buscar);

        databaseHelper = new DatabaseHelper(this);

        btn_buscar.setOnClickListener(view -> buscarPorCedula());



        //mostrar datos
        mostrarDatos();





        // Botón para volver atrás
        btn_volver.setOnClickListener(view -> finish());

        //boton para buscar



        //editar elemento del lv_datos
        lv_datos.setOnItemClickListener((parent, view, position, id) -> {
            // Obtener los datos del elemento seleccionado
            Poliza polizaSeleccionada = (Poliza) parent.getItemAtPosition(position);

            // Crear el diálogo de confirmación
            AlertDialog.Builder builder = new AlertDialog.Builder(ListarDatosActivity.this);
            builder.setTitle("Actualizar Datos")
                    .setMessage("¿Quiere actualizar los datos?")
                    .setPositiveButton("Actualizar", (dialog, which) -> {
                        // Abrir la actividad de edición y enviar los datos
                        Intent intent = new Intent(ListarDatosActivity.this, EditarDatosActivity.class);
                        intent.putExtra("id", polizaSeleccionada.getId());  // Pasar ID
                        intent.putExtra("nombre", polizaSeleccionada.getNombre());
                        intent.putExtra("cedula", polizaSeleccionada.getCedula());
                        intent.putExtra("valor", polizaSeleccionada.getValorAuto());
                        intent.putExtra("modelo", polizaSeleccionada.getModelo());
                        intent.putExtra("accidentes", polizaSeleccionada.getAccidentes());
                        intent.putExtra("edad", polizaSeleccionada.getEdad());
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

            builder.create().show();

        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarDatos(); // Recargar los datos cuando la actividad se reanude
    }

    public void actualizarLista() {
        mostrarDatos();
    }


    private void buscarPorCedula() {
        String cedulaBuscada = et_buscar_cedula.getText().toString().trim();
        if (cedulaBuscada.isEmpty()) {
            Toast.makeText(this, "Ingrese una cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.obtenerPolizaPorCedula(cedulaBuscada);
        listaPolizas = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Poliza poliza = new Poliza(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getDouble(7)
                );
                listaPolizas.add(poliza);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        adapter = new PolizaAdapter(this, listaPolizas, this);
        lv_datos_cedula_seleccionada.setAdapter(adapter);


    }



    private void mostrarDatos(){
        //obtener datos de la base de datos
        Cursor cursor = databaseHelper.obtenerPolizas();

        // condicion cuano no hay datos


        if (cursor.getCount() == 0){
            Toast.makeText(this, "No hay datos", Toast.LENGTH_LONG).show();

        }

        //recorrer los datos del cursor

        //cursor.moveToFirst();




        // cargar datos en el listView
        List<Poliza> listaPoliza = new ArrayList<>();
        while (cursor.moveToNext()) {
            Poliza poliza = new Poliza(
                    cursor.getInt(0),     // ID
                    cursor.getString(1),  // Nombre
                    cursor.getString(2),  // Cédula
                    cursor.getDouble(3),  // Valor del vehículo
                    cursor.getInt(4),     // Accidentes
                    cursor.getString(5),  // Modelo
                    cursor.getString(6),  // Edad
                    cursor.getDouble(7)   // Costo total
            );
            listaPoliza.add(poliza);
        }

        cursor.close();

        //mostrar datos en el listView
        // Adaptador personalizado
        ArrayAdapter<Poliza> adapter = new ArrayAdapter<Poliza>(this, android.R.layout.simple_list_item_1, listaPoliza) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(Html.fromHtml(listaPoliza.get(position).toString(), Html.FROM_HTML_MODE_LEGACY));
                return textView;
            }
        };

        lv_datos.setAdapter(adapter);



    }







}