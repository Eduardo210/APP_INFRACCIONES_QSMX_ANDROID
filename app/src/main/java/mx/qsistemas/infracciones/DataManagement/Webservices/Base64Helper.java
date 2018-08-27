package mx.qsistemas.infracciones.DataManagement.Webservices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Database.DBHelper;
import mx.qsistemas.infracciones.DataManagement.Models.Images;

/**
 * Developed by ingmtz on 11/29/16.
 */

class Base64Helper {

    private Context context;
    private HashMap<String,String> map;
    private JSONObject object, arrayobject;
    private JSONArray array, arraysplit;
    private DBHelper dbHelper;
    private BitmapFactory.Options opc = new BitmapFactory.Options();
    private Bitmap bmp;
    private ByteArrayOutputStream bos;
    private byte[] ba;
    int count = 0;
    private ArrayList<Images> listimages;

     Base64Helper(Context context){
        this.context = context;
    }

    JSONObject imageSync(){
        opc.inPreferredConfig = Bitmap.Config.ARGB_8888;

        dbHelper = new DBHelper(context);
        object = new JSONObject();
        array = new JSONArray();
        arrayobject = new JSONObject();

        String[] tosync = dbHelper.getImageSync();
         if (tosync != null) {
             File file = new File(Const.SD_PATH_IMG);
             for (File f : file.listFiles()) {
                 if (f.isFile()) {
                     String fullname = f.getName();
                     //tosync.length
                     for (int i = 0; i < 37; i++) {
                         if (fullname.equals(tosync[i])) {
                             putobjects(f, fullname);
                             count++;
                             Log.d("Items Loaded", String.valueOf(count));
                         }
                     }
                 }
             }
         }

         if (array.length() <= 0){
             return null;
         }

        map = new HashMap<>();
        map.put("username", "Mobile");
        map.put("password", "CF2E3EF25C90EB567243ADFACD4AA868");



        object = new JSONObject(map);
        try {
            object.put("LoteImagenes", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    ArrayList<JSONObject> imageSyncRecursive() throws JSONException {

        ArrayList<JSONObject> list = new ArrayList<>();

        opc.inPreferredConfig = Bitmap.Config.ARGB_8888;
        dbHelper = new DBHelper(context);
        object = new JSONObject();
        array = new JSONArray();
        arrayobject = new JSONObject();
        listimages = new ArrayList<>();

        String[] tosync = dbHelper.getImageSync();
        if (tosync != null) {
            File file = new File(Const.SD_PATH_IMG);
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    String fullname = f.getName();
                    //tosync.length
                    for (int i = 0; i < tosync.length; i++) {
                        if (fullname.equals(tosync[i])) {
                            putobjects(f, fullname);
                            count++;
                            Log.d("Items Loaded", String.valueOf(count));
                        }
                    }
                }
            }
        }

        if (listimages.size() <= 0){
            list = null;
        } else {

            int reps = listimages.size() / 35;
            boolean isDivisible = true;
            int difference = listimages.size() % 35;

            if (difference != 0) {
                reps++;
                isDivisible = false;
            }


            for (int i = 0; i < reps; i++) {

                array = putArrayObjects(reps, i + 1, difference,isDivisible);

                map = new HashMap<>();
                map.put("username", "Mobile");
                map.put("password", "CF2E3EF25C90EB567243ADFACD4AA868");

                object = new JSONObject(map);
                try {
                    object.put("LoteImagenes", array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                list.add(object);

            }
        }

        return list;
    }

    private JSONArray putArrayObjects(int reps, int reptime, int difference, boolean isDivisible) {

        JSONArray array1 = new JSONArray();

        Images one;

        int start = reptime * 35 - 35;
        int end = reptime * 35;

        if (reps != reptime){
            for (int i = start; i < end; i++){

                map = new HashMap<>();
                one = listimages.get(i);
                map.put("Tipo", one.getTipo());
                map.put("ArchivoNombre", one.getNombre());
                map.put("ArchivoBase64", one.getBase());

                arrayobject = new JSONObject(map);
                array1.put(arrayobject);

            }
        } else {
            if (!isDivisible){
                for (int i = start; i < start + difference; i++){
                    map = new HashMap<>();
                    one = listimages.get(i);
                    map.put("Tipo", one.getTipo());
                    map.put("ArchivoNombre", one.getNombre());
                    map.put("ArchivoBase64", one.getBase());

                    arrayobject = new JSONObject(map);
                    array1.put(arrayobject);
                }
            }
        }

        return array1;
    }


    private void putobjects(File f, String name) {

        map = new HashMap<>();
        bmp = BitmapFactory.decodeFile(Const.SD_PATH_IMG + f.getName(), opc);
        bmp = Bitmap.createScaledBitmap(bmp, 600, 800, false);
        bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
        bmp.recycle();
        ba = bos.toByteArray();
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bos = null;


        String[] namesplit = name.split("\\.");

        Images images = new Images();
        images.setNombre(namesplit[0] + ".bmp");
        images.setBase(Base64.encodeToString(ba,Base64.DEFAULT));
        images.setTipo("4");
        listimages.add(images);



        /*String[] namesplit = name.split("\\.");
        map.put("Tipo", "4");
        map.put("ArchivoNombre", namesplit[0] + ".bmp");
        map.put("ArchivoBase64", Base64.encodeToString(ba,Base64.DEFAULT));

        arrayobject = new JSONObject(map);
        array.put(arrayobject);*/
    }
}
