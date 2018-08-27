package mx.qsistemas.infracciones.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.InputQueue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import org.json.JSONException;

import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Database.DBHelper;
import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;
import mx.qsistemas.infracciones.DataManagement.Models.UserLogin;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.Functions.PrintInfraNew;
import mx.qsistemas.infracciones.InfraBase;
import mx.qsistemas.infracciones.R;
import mx.qsistemas.infracciones.Utils.MD5;
import mx.qsistemas.infracciones.Utils.Utils;

public class LoginActivity extends InfraBase {

    public EditText etLoginUser, etLoginPassword;
    public Button btnLoginLog;
    public DBHelper dbHelper;
    PreferenceHelper pHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setBarColor("#000000");
        setCenterText(getResources().getString(R.string.LOG_IN), Color.WHITE);
        setCenterImage(R.drawable.icn_key);

        dbHelper = new DBHelper(this);
        pHelper = new PreferenceHelper(this);

        etLoginUser = (EditText) findViewById(R.id.etLoginUser);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        btnLoginLog = (Button) findViewById(R.id.btnLoginLog);

        btnLoginLog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoginLog:
                if (pHelper.getPrefix() != null){
                    if (isValidate()){
                        UserLogin userLogin = dbHelper.getUser(etLoginUser.getText().toString().toUpperCase());
                        if (userLogin.getUsername() == null){
                            Utils.showToast(getResources().getString(R.string.user_not_found), this);
                            break;
                        }
                        String pwtomd5 = MD5.toMD5(etLoginPassword.getText().toString()).toUpperCase();
                        String userpass = userLogin.getPassword();
                        if (!pwtomd5.equals(userpass)){
                            Utils.showToast(getResources().getString(R.string.password_missmatch), this);
                            break;
                        } else {
                            pHelper.putIdPersonaAyuntamiento(userLogin.getIdPersonaAyuntamiento());
                            pHelper.putIdPersona(userLogin.getIdPersona());
                            pHelper.putNombre(userLogin.getNombre());
                            pHelper.putAMaterno(userLogin.getaMaterno());
                            pHelper.putAPaterno(userLogin.getaPaterno());
                            Intent i = new Intent().setClass(this, MainMenu.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    }
                } else {
                    Utils.showAlertDialogOk(this, "Ingresa un prefijo para la terminal", null, null, true, 2);
                }
                break;
        }
    }

    private boolean isValidate() {
        String msg = null;
        if (TextUtils.isEmpty(etLoginPassword.getText().toString())){
            msg = getResources().getString(R.string.input_a_password);
        }
        if (TextUtils.isEmpty(etLoginUser.getText().toString())){
            msg = getResources().getString(R.string.input_a_username);
        }
        if (msg == null)
            return true;
        Utils.showToast(msg, this);
        return false;
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) throws JSONException {

    }
}
