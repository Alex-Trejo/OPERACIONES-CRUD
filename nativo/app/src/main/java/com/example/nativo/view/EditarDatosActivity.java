package com.example.nativo.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nativo.R;
import com.example.nativo.controller.PolizaController;
import com.example.nativo.database.DatabaseHelper;

import java.util.Arrays;
import java.util.List;

public class EditarDatosActivity extends AppCompatActivity {

    EditText etNombre, etCedula, etValorVehiculo, etAccidentes;
    Spinner spModelo, spEdad;
    Button btnGuardar;
    DatabaseHelper dbHelper;
    String cedulaOriginal;
    int idPoliza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_datos);
        //mapeo de vistas
        etNombre = findViewById(R.id.et_nombre_edit);
        etCedula = findViewById(R.id.et_cedula_edit);
        etValorVehiculo = findViewById(R.id.txt_valor_vehiculo_edit);
        etAccidentes = findViewById(R.id.txt_accidentes_edit);
        spModelo = findViewById(R.id.sp_modelo_edit);
        spEdad = findViewById(R.id.sp_edad_edit);
        btnGuardar = findViewById(R.id.btn_guardar_edit);
        dbHelper = new DatabaseHelper(this);


        //obtener datos de la intent
        Intent intent = getIntent();
        if (intent != null) {
            idPoliza = intent.getIntExtra("id", -1);
            etNombre.setText(intent.getStringExtra("nombre"));
            etCedula.setText(intent.getStringExtra("cedula"));
            etValorVehiculo.setText(String.valueOf(intent.getDoubleExtra("valor", 0)));
            etAccidentes.setText(String.valueOf(intent.getIntExtra("accidentes", 0)));

            // Guardar la cédula original por si se requiere
            cedulaOriginal = intent.getStringExtra("cedula");

            // Configurar Spinners
            List<String> modelos = Arrays.asList("Modelo A", "Modelo B", "Modelo C");
            ArrayAdapter<String> modeloAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelos);
            modeloAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spModelo.setAdapter(modeloAdapter);
            spModelo.setSelection(modelos.indexOf(intent.getStringExtra("modelo")));

            List<String> edades = Arrays.asList("18-23", "24-55", "Mayor de 55");
            ArrayAdapter<String> edadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, edades);
            edadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEdad.setAdapter(edadAdapter);
            spEdad.setSelection(edades.indexOf(intent.getStringExtra("edad")));
        }

        //evento para guardar cambios
        btnGuardar.setOnClickListener(v -> {
            actualizarDatos();
        });

    }

    private void actualizarDatos() {
        String nombre = etNombre.getText().toString().trim();
        String cedula = etCedula.getText().toString().trim();
        String valorStr = etValorVehiculo.getText().toString().trim();
        String accidentesStr = etAccidentes.getText().toString().trim();
        String modelo = spModelo.getSelectedItem().toString();
        String edad = spEdad.getSelectedItem().toString();

        // Validaciones
        if (nombre.isEmpty() || cedula.isEmpty() || valorStr.isEmpty() || accidentesStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validarCedulaEcuatoriana(cedula)) {
            Toast.makeText(this, "Cédula ecuatoriana no válida", Toast.LENGTH_LONG).show();
            return;
        }

        double valorVehiculo;
        int accidentes;

        try {
            valorVehiculo = Double.parseDouble(valorStr);
            if (valorVehiculo <= 0) {
                Toast.makeText(this, "El valor del vehículo debe ser mayor a 0", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un valor de vehículo válido", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            accidentes = Integer.parseInt(accidentesStr);
            if (accidentes < 0) {
                Toast.makeText(this, "El número de accidentes no puede ser negativo", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un número de accidentes válido", Toast.LENGTH_LONG).show();
            return;
        }

        // Calcular el costo de la póliza
        double costoTotal = Double.parseDouble(PolizaController.calcularCostoPoliza(nombre, valorVehiculo, accidentes, modelo, edad));

        // Actualizar solo el registro con el ID específico
        boolean actualizado = dbHelper.actualizarPoliza(idPoliza, nombre, cedula, valorVehiculo, accidentes, modelo, edad, costoTotal);

        if (actualizado) {
            Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar los datos", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validarCedulaEcuatoriana(String cedula) {
        if (cedula.length() != 10) {
            return false;
        }

        try {
            int provincia = Integer.parseInt(cedula.substring(0, 2));
            int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int suma = 0;

            if (provincia < 1 || provincia > 24 || tercerDigito >= 6) {
                return false;
            }

            for (int i = 0; i < coeficientes.length; i++) {
                int valor = Integer.parseInt(cedula.substring(i, i + 1)) * coeficientes[i];
                if (valor >= 10) {
                    valor -= 9;
                }
                suma += valor;
            }

            int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));
            int decenaSuperior = ((suma + 9) / 10) * 10;
            return digitoVerificador == (decenaSuperior - suma) % 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }




}