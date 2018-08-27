package mx.qsistemas.infracciones.DataManagement.Database;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import mx.qsistemas.infracciones.DataManagement.Models.Catalogs;
import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;
import mx.qsistemas.infracciones.DataManagement.Models.Persona;
import mx.qsistemas.infracciones.DataManagement.Models.SearchTerms;
import mx.qsistemas.infracciones.DataManagement.Models.UserLogin;

/**
 * Developed by ingmtz on 10/17/16.
 */

public class DBHelper {

    private DBAdapter dbAdapter;

    public DBHelper(Context context){
        dbAdapter = new DBAdapter(context);
    }

    public UserLogin getUser(String s) {
        dbAdapter.open();
        UserLogin user = dbAdapter.getUserLogin(s);
        dbAdapter.close();
        return user;
    }

    public boolean getUserPermissions(String idPersonaAyuntamiento, Context context) {
        dbAdapter.open();
        boolean permissions= dbAdapter.getUserPermissions(idPersonaAyuntamiento, context);
        dbAdapter.close();
        return permissions;
    }

    public ArrayList<ArrayList<Catalogs>> getCatalogs(Context context) {
        dbAdapter.open();
        ArrayList<ArrayList<Catalogs>> catalogs = dbAdapter.getCatalogs(context);
        dbAdapter.close();
        return catalogs;
    }

    public GetSaveInfra savedata(GetSaveInfra saveData) {
        dbAdapter.open();
        GetSaveInfra response = dbAdapter.savedata(saveData);
        dbAdapter.close();
        return response;
    }

    public ArrayList<GetSaveInfra> getInfracciones(SearchTerms searchterms, boolean is_sync) {
        dbAdapter.open();
        ArrayList<GetSaveInfra> infracciones = dbAdapter.getInfracciones(searchterms, is_sync);
        dbAdapter.close();
        return infracciones;
    }

    public GetSaveInfra updateData(Persona persona, GetSaveInfra saveData) {
        dbAdapter.open();
        GetSaveInfra response = dbAdapter.updateData(persona, saveData);
        dbAdapter.close();
        return response;
    }

    public ArrayList<JSONObject> getSync(Context context) {
        dbAdapter.open();
        ArrayList<JSONObject> list = dbAdapter.getSync(context);
        dbAdapter.close();
        return list;
    }

    public boolean updateSync(String[] ids_got, String[] ids_sent) {
        dbAdapter.open();
        boolean response = dbAdapter.updateSync(ids_got, ids_sent);
        dbAdapter.close();
        return response;
    }

    public String getLastSync(Context context) {
        dbAdapter.open();
        String response = dbAdapter.getLastSync(context);
        dbAdapter.close();
        return response;
    }

    public boolean setCatalogs(JSONObject object, Context context) {
        dbAdapter.open();
        boolean response = dbAdapter.setCatalogs(object, context);
        dbAdapter.close();
        return response;
    }

    public String[] getImageSync() {
        dbAdapter.open();
        String[] response = dbAdapter.getImageSync();
        dbAdapter.close();
        return response;
    }

    public void savephotos(String imageone, String imagetwo) {
        dbAdapter.open();
        dbAdapter.savephotos(imageone, imagetwo);
        dbAdapter.close();
    }

    public void removeCorrectImg(String[] strings) {
        dbAdapter.open();
        dbAdapter.removeCorrectImg(strings);
        dbAdapter.close();
    }

    public String[] getOficial(String idayun) {
        dbAdapter.open();
        String[] response = dbAdapter.getOficial(idayun);
        dbAdapter.close();
        return response;    }
}
