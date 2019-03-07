package mx.qsistemas.infracciones;

import org.json.JSONObject;

public interface OnVolleyResponse {
    void onServiceCompleted(Boolean success, String message, JSONObject response, int serviceCode);
}
