package mx.qsistemas.infracciones;

import org.json.JSONException;

/**
 * Developed by ingmtz on 10/17/16.
 */

public interface AsyncTaskCompleteListener {
    void onTaskCompleted(String response, int serviceCode) throws JSONException;
}
