package com.example.nativo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nativo.R;
import com.example.nativo.controller.PolizaController;
import com.example.nativo.database.DatabaseHelper;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //declarar objetos
    EditText txt_nombre, txt_valor_vehiculo, txt_accidentes, txt_cedula;
    Button btn_calcular_costo, btn_limpiar, btn_salir, btn_listar_datos;
    TextView lbl_costo_total;
    Spinner cboModelo, cboEdad;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mapeo
        txt_nombre = findViewById(R.id.et_nombre);
        txt_valor_vehiculo = findViewById(R.id.txt_valor_vehiculo);
        txt_accidentes = findViewById(R.id.txt_accidentes);

        txt_cedula = findViewById(R.id.etCedula);




        lbl_costo_total = findViewById(R.id.lblCostoPoliza);

        cboModelo = findViewById(R.id.sp_edad);
        cboEdad = findViewById(R.id.sp_modelo);

        btn_calcular_costo = findViewById(R.id.btn_calcular_costo);
        btn_limpiar = findViewById(R.id.btn_limpiar);
        btn_salir = findViewById(R.id.btn_salir);
        btn_listar_datos = findViewById(R.id.btn_listar_datos);





        dbHelper = new DatabaseHelper(this);
        //cargar
        //metodo para cargar datos dentro del spinner
        cargarDatos();
        //metodo para calcular poliza
        //calcularPoliza();


        //eventos de los botones


        btn_calcular_costo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularPoliza();

            }
            });


        btn_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
            });


        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
            });

        btn_listar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListarDatosActivity.class);
                lbl_costo_total.setText("");
                startActivity(intent);

            }
        });











        //metodo para cargar datos dentro del spinner
        //cargarDatos();
        //metodo para calcular poliza
        //calcularPoliza();
    }


    private void cargarDatos(){
        //spinner cargar datos de modelos
        List<String> modelos = Arrays.asList("Modelo A", "Modelo B", "Modelo C");
        ArrayAdapter<String> modeloAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelos);
        modeloAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cboModelo.setAdapter(modeloAdapter);



        //spinner cargar datos edades
        List<String> edades = Arrays.asList("18-23", "24-55", "Mayor de 55");
        ArrayAdapter<String> edadesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, edades);
        edadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cboEdad.setAdapter(edadesAdapter);


    }


    //calcular poliza
    private void calcularPoliza(){
        //Validar campos vacios
        // Validación de campos vacíos
        if (txt_nombre.getText().toString().trim().isEmpty() ||
                txt_cedula.getText().toString().trim().isEmpty() ||
                txt_valor_vehiculo.getText().toString().trim().isEmpty() ||
                txt_accidentes.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }


        //validar que los spinner

        if (cboModelo.getSelectedItem()==null || cboEdad.getSelectedItem()==null){
            Toast.makeText(this, "Seleccione un modelo o una edad", Toast.LENGTH_LONG).show();

        }

        //declarar variables
        String nombre = txt_nombre.getText().toString();
        String cedula = txt_cedula.getText().toString();

        // Validación de cédula ecuatoriana
        if (!validarCedulaEcuatoriana(cedula)) {
            Toast.makeText(this, "Cédula ecuatoriana no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar valores numéricos
        double valorAuto = Double.parseDouble(txt_valor_vehiculo.getText().toString());
        int accidentes = Integer.parseInt(txt_accidentes.getText().toString());

        try {
            valorAuto = Double.parseDouble(txt_valor_vehiculo.getText().toString().trim());
            accidentes = Integer.parseInt(txt_accidentes.getText().toString().trim());

            if (valorAuto <= 0) {
                Toast.makeText(this, "El valor del vehículo debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (accidentes < 0) {
                Toast.makeText(this, "El número de accidentes no puede ser negativo", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        //obtener los valores de los spinner
        String modelo = cboModelo.getSelectedItem().toString();
        String edad = cboEdad.getSelectedItem().toString();


        //calculo de la poliza

        double costoTotal = Double.parseDouble(PolizaController.calcularCostoPoliza(nombre, valorAuto, accidentes, modelo, edad));

        lbl_costo_total.setText("Costo de Póliza: $" + String.format("%.2f", costoTotal));



        //guardar en la base de datos
        boolean insertado = dbHelper.insertarPoliza(nombre, cedula ,valorAuto, accidentes, modelo, edad, costoTotal);

        if (insertado){
            Toast.makeText(this, "Poliza guardada correctamente", Toast.LENGTH_LONG).show();
            //limpiar campos
            limpiarCampos();
        }else {
            Toast.makeText(this, "Error al guardar la poliza", Toast.LENGTH_LONG).show();

        }
    }

    private boolean validarCedulaEcuatoriana(String cedula) {
        if (cedula.length() != 10 || !cedula.matches("\\d+")) {
            return false; // Debe tener 10 dígitos y ser solo números
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            return false; // Provincia inválida
        }

        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito < 0 || tercerDigito > 5) {
            return false; // Solo se permiten cédulas de personas naturales
        }

        // Algoritmo de verificación del último dígito
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;

        for (int i = 0; i < 9; i++) {
            int valor = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            suma += (valor >= 10) ? valor - 9 : valor;
        }

        int verificadorCalculado = (suma % 10 == 0) ? 0 : (10 - (suma % 10));
        int verificadorReal = Character.getNumericValue(cedula.charAt(9));

        return verificadorCalculado == verificadorReal;
    }


    private void limpiarCampos(){
        txt_nombre.setText("");
        txt_cedula.setText("");
        txt_valor_vehiculo.setText("");
        txt_accidentes.setText("");
        cboModelo.setSelection(0);
        cboEdad.setSelection(0);

        Toast.makeText(this, "Campos limpiados", Toast.LENGTH_LONG).show();
    }


}