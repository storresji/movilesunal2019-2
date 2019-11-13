package co.edu.unal.directorio.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.edu.unal.directorio.models.Clasificacion;
import co.edu.unal.directorio.models.Empresa;

public class EmpresaOperations {

    public static final String LOGTAG = "EMP_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            EmpresaDBHandler.COLUMN_ID,
            EmpresaDBHandler.COLUMNA_NOMBRE,
            EmpresaDBHandler.COLUMNA_URL_PAGINA,
            EmpresaDBHandler.COLUMNA_TELEFONO,
            EmpresaDBHandler.COLUMNA_CORREO,
            EmpresaDBHandler.COLUMNA_CLASIFICACION,
    };

    public EmpresaOperations(Context context) {
        dbhandler = new EmpresaDBHandler(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();


    }
    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }

    public Empresa addEmpresa(Empresa empresa){

        ContentValues values  = new ContentValues();
        values.put(EmpresaDBHandler.COLUMNA_NOMBRE, empresa.getNombre());
        values.put(EmpresaDBHandler.COLUMNA_URL_PAGINA, empresa.getUrlPaginaWeb());
        values.put(EmpresaDBHandler.COLUMNA_TELEFONO, empresa.getTelefono());
        values.put(EmpresaDBHandler.COLUMNA_CORREO, empresa.getCorreo());
        values.put(EmpresaDBHandler.COLUMNA_CLASIFICACION, empresa.getClasificacion().getId());

        System.out.println("Empresa a guardar: " + empresa);
        long insertid = database.insert(EmpresaDBHandler.TABLA_EMPRESAS,null,values);
        empresa.setEmpId(insertid);
        return empresa;

    }

    // Getting single Employee
    public Empresa getEmpresa(long id) {

        Cursor cursor = database.query(EmpresaDBHandler.TABLA_EMPRESAS, allColumns,EmpresaDBHandler.COLUMN_ID + "=?",new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if(cursor == null)
            System.out.println("CURSOR NULO!!!!!");
        else {

            System.out.println("CURSOR NO    NULO!!!!! "  + cursor.getCount());
        }
        Clasificacion clasificacion = Clasificacion.getById(cursor.getInt(5));
        System.out.println("Clasificacion: " + clasificacion.getId());
        System.out.println("Id GetEmpresa: " + id + " " + Long.parseLong(cursor.getString(0)) + " " + cursor.getString(1) + " " + cursor.getInt(5));
        Empresa empresa = new Empresa(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                Clasificacion.getById(cursor.getInt(5)));

        System.out.println("EMPRESA CREADA: " + empresa);
        return empresa;
        }

    public List<Empresa> getAllEmpresas() {

        Cursor cursor = database.query(EmpresaDBHandler.TABLA_EMPRESAS,allColumns,null,null,null, null, null);

        List<Empresa> empresas = new ArrayList<>();

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Empresa empresa = new Empresa();
                empresa.setEmpId(cursor.getLong(cursor.getColumnIndex(EmpresaDBHandler.COLUMN_ID)));
                empresa.setNombre(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMNA_NOMBRE)));
                empresa.setUrlPaginaWeb(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMNA_URL_PAGINA)));
                empresa.setTelefono(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMNA_TELEFONO)));
                empresa.setCorreo(cursor.getString(cursor.getColumnIndex(EmpresaDBHandler.COLUMNA_CORREO)));
                empresa.setClasificacion(Clasificacion.getById(cursor.getInt(cursor.getColumnIndex(EmpresaDBHandler.COLUMNA_CLASIFICACION))));
                empresas.add(empresa);
            }
        }
        // return All Employees
        return empresas;
    }

    // Updating Employee
    public int updateEmpresa(Empresa empresa) {

        ContentValues values = new ContentValues();
        values.put(EmpresaDBHandler.COLUMNA_NOMBRE, empresa.getNombre());
        values.put(EmpresaDBHandler.COLUMNA_URL_PAGINA, empresa.getUrlPaginaWeb());
        values.put(EmpresaDBHandler.COLUMNA_TELEFONO, empresa.getTelefono());
        values.put(EmpresaDBHandler.COLUMNA_CORREO, empresa.getCorreo());
        values.put(EmpresaDBHandler.COLUMNA_CLASIFICACION, empresa.getClasificacion().getId());

        // updating row
        return database.update(EmpresaDBHandler.TABLA_EMPRESAS, values,
                EmpresaDBHandler.COLUMN_ID + "=?",new String[] { String.valueOf(empresa.getEmpId())});
    }

    // Deleting Employee
    public void eliminarEmpresa(Empresa empresa) {
        System.out.println("Empresa a eliminar: " + empresa);
        database.delete(EmpresaDBHandler.TABLA_EMPRESAS, EmpresaDBHandler.COLUMN_ID + "=" + empresa.getEmpId(), null);
    }
}
