package com.example.nativo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //definir el nombre de la base de datos y version

    private static final String DATABASE_NAME = "poliza2.db";
    private static final int DATABASE_VERSION = 1;

    //definir el nombre de la tabla y columnas
    private static final String Table_Name = "poliza";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_VALOR = "valor_auto";
    private static final String COLUMN_ACCIDENTES = "accidentes";
    private static final String COLUMN_MODELO = "modelo";
    private static final String COLUMN_EDAD = "edad";
    private static final String COLUMN_COSTO = "costo_poliza";
    private static final String COLUMN_CEDULA = "cedula";


    //crear tabla para sql
    private static String TABLE_CREATE =
            "CREATE TABLE " + Table_Name + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOMBRE + " TEXT NOT NULL, " +
                    COLUMN_CEDULA + " TEXT NOT NULL, " +
                    COLUMN_VALOR + " REAL NOT NULL, " +
                    COLUMN_ACCIDENTES + " INTEGER NOT NULL, " +
                    COLUMN_MODELO + " TEXT NOT NULL, " +
                    COLUMN_EDAD + " INTEGER NOT NULL, " +
                    COLUMN_COSTO + " REAL NOT NULL) "  ;



    //contructor

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int /*oldVersion*/i, int /*newVersion*/ il) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }


    //insertar datos en la base de datos
    public boolean insertarPoliza(String nombre, String cedula ,double valorAuto, int accidentes, String modelo, String edad, double costoPoliza){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_CEDULA, cedula);
        values.put(COLUMN_VALOR, valorAuto);
        values.put(COLUMN_ACCIDENTES, accidentes);
        values.put(COLUMN_MODELO, modelo);
        values.put(COLUMN_EDAD, edad);
        values.put(COLUMN_COSTO, costoPoliza);

        long resultado = db.insert(Table_Name, null, values);
        db.close();


        return resultado != -1; //
    }


    //metodo para mostrar datos de la base de datos
    public Cursor obtenerPolizas(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + Table_Name, null);

    }

    // Buscar por cedula
    public Cursor obtenerPolizaPorCedula(String cedula) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + Table_Name + " WHERE cedula = ?", new String[]{cedula});
    }


    // CRUD: Actualizar un registro
    public boolean actualizarPoliza(int id, String nombre, String cedula, double valor, int accidentes, String modelo, String edad, double costo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("cedula", cedula);
        values.put("valor_auto", valor);
        values.put("accidentes", accidentes);
        values.put("modelo", modelo);
        values.put("edad", edad);
        values.put("costo_poliza", costo);

        int filasActualizadas = db.update(Table_Name, values, "id=?", new String[]{String.valueOf(id)});
        return filasActualizadas > 0;
    }


    // CRUD: Eliminar un registro
    public boolean eliminarPoliza(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(Table_Name, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas > 0;
    }




}
