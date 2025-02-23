package com.example.nativo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.nativo.R;
import com.example.nativo.database.DatabaseHelper;
import com.example.nativo.model.Poliza;

import java.util.List;

public class PolizaAdapter extends ArrayAdapter<Poliza> {
    Context context;
    List<Poliza> polizas;
    DatabaseHelper databaseHelper;
    ListarDatosActivity activity;

    public PolizaAdapter(Context context, List<Poliza> polizas, ListarDatosActivity activity) {
        super(context, R.layout.item_poliza, polizas);
        this.context = context;
        this.polizas = polizas;
        this.databaseHelper = new DatabaseHelper(context);
        this.activity = activity;  // Guardamos la referencia a ListarDatosActivity
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_poliza, parent, false);
        }

        TextView tvDatos = convertView.findViewById(R.id.tvDatos);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        Poliza poliza = polizas.get(position);
        tvDatos.setText("Nombre: " + poliza.getNombre() + "\nCédula: " + poliza.getCedula() +
                "\nModelo: " + poliza.getModelo() + "\nCosto: $" + poliza.getCostoPoliza());

        btnEliminar.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar Registro")
                    .setMessage("¿Seguro que desea eliminar esta póliza?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        boolean eliminado = databaseHelper.eliminarPoliza(poliza.getId());
                        if (eliminado) {
                            polizas.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Registro eliminado", Toast.LENGTH_SHORT).show();
                            activity.actualizarLista();  // Refrescar la lista en ListarDatosActivity

                        } else {
                            Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        return convertView;
    }
}

