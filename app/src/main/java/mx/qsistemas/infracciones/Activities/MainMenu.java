package mx.qsistemas.infracciones.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Database.DBHelper;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.DataManagement.Webservices.ParseContent;
import mx.qsistemas.infracciones.Functions.SyncManual;
import mx.qsistemas.infracciones.InfraBase;
import mx.qsistemas.infracciones.R;
import mx.qsistemas.infracciones.Utils.Utils;
import mx.qsistemas.infracciones.DataManagement.Webservices.HttpRequester;

public class MainMenu extends InfraBase {

    private MenuDrawer mDrawer;
    public PreferenceHelper pHelper;
    public DBHelper dbHelper;
    public int[] permissions;
    public LinearLayout llMenuIP, llMenuPrint, llMenuKey, llMenuConfig, llMenuSync, llMenuSyncInf, llMenuLogout;
    public ImageButton ibMenuNew, ibMenuSearch;
    public TextView tvMenuNew, tvMenuSearch;
    public Dialog dialog;
    public Intent i;
    public ParseContent pContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawer = MenuDrawer.attach(this, Position.TOP);
        mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mDrawer.setContentView(R.layout.activity_main_menu);
        mDrawer.setMenuView(R.layout.custom_menu);
        mDrawer.setMenuSize(400);
        setRightImage(R.drawable.icn_menu);
        setCenterText(getResources().getString(R.string.app_name), Color.WHITE);
        setBarColor("#000000");

        pHelper = new PreferenceHelper(this);
        dbHelper = new DBHelper(this);
        pContent = new ParseContent(this);

        llBaseRight.setOnClickListener(this);

        llMenuIP = findViewById(R.id.llMenuIP);
        llMenuPrint = findViewById(R.id.llMenuPrint);
        llMenuKey = findViewById(R.id.llMenuKey);
        llMenuConfig = findViewById(R.id.llMenuConfig);
        llMenuSync = findViewById(R.id.llMenuSync);
        llMenuLogout = findViewById(R.id.llMenuLogout);
        llMenuSyncInf = (LinearLayout) findViewById(R.id.llMenuSyncInf);
        llMenuSyncInf.setOnClickListener(this);
        llMenuIP.setOnClickListener(this);
        llMenuPrint.setOnClickListener(this);
        llMenuKey.setOnClickListener(this);
        llMenuConfig.setOnClickListener(this);
        llMenuSync.setOnClickListener(this);
        llMenuLogout.setOnClickListener(this);

        ibMenuNew = findViewById(R.id.ibMenuNew);
        ibMenuSearch = findViewById(R.id.ibMenuSearch);
        ibMenuNew.setOnClickListener(this);
        ibMenuSearch.setEnabled(true);
        ibMenuSearch.setOnClickListener(this);

        tvMenuNew = findViewById(R.id.tvMenuNew);
        tvMenuSearch = findViewById(R.id.tvMenuSearch);

        setMenuData();

        try {
            pHelper.putIsSearch(null);
            Utils.setMobileDataState(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setMenuData() {
        if (dbHelper.getUserPermissions(pHelper.getIdPersona(), this)){
            if (pHelper.getHasAll().equals("0")){

                llMenuKey.setVisibility(View.GONE);

                if (pHelper.getCanInsert().equals("0")){
                    ibMenuNew.setVisibility(View.GONE);
                    tvMenuNew.setVisibility(View.GONE);
                }

                if (pHelper.getCanConsult().equals("0")){
                    ibMenuSearch.setVisibility(View.GONE);
                    tvMenuSearch.setVisibility(View.GONE);
                }

                if (pHelper.getCanSync().equals("0")){
                    llMenuSync.setVisibility(View.GONE);
                    llMenuConfig.setVisibility(View.GONE);
                }

                llMenuIP.setVisibility(View.GONE);
                llMenuPrint.setVisibility(View.GONE);
            } else {
                llMenuConfig.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBaseRight:
                mDrawer.toggleMenu();
                break;
            case R.id.llMenuIP:
                if (mDrawer.isMenuVisible()){
                    mDrawer.toggleMenu();
                }
                String currservice = pHelper.getServiceIP();
                String texttwo;
                if (currservice == null){
                    texttwo = (getResources().getString(R.string.no_current_ip));
                } else {
                    texttwo = (getResources().getString(R.string.current) + ": " + currservice);
                }
                Utils.showAlertDialogOk(this, getResources().getString(R.string.input_a_ip) , texttwo, getResources().getString(R.string.example_service), true, 1);
                break;
            case R.id.llMenuPrint:
                i = new Intent().setClass(MainMenu.this, PrintConfigActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.llMenuKey:
                if (mDrawer.isMenuVisible()){
                    mDrawer.toggleMenu();
                }
                Utils.showAlertDialogOk(this, "¿Deseas borrar la base de datos?", null, null, false, 3);
                break;
            case R.id.llMenuConfig:
                if (mDrawer.isMenuVisible()){
                    mDrawer.toggleMenu();
                }
                if (Utils.isNetworkAvailable(this)){
                    Utils.showCustomProgressDialog(this, false, null);
                    if (pHelper.getServiceIP() != null){
                        new HttpRequester(this, Const.Typedata.IMAGESYNC, this, Const.ServiceCode.SYNCIMAGES, pHelper.getServiceIP(), null);
                    } else {
                        Utils.removeCustomProgressDialog();
                        Utils.showToast("No has introducido un servicio", this);
                        llMenuIP.callOnClick();
                    }
                } else {
                    Utils.showToast("No hay internet", this);
                }
                break;
            case R.id.llMenuSync:
                if (mDrawer.isMenuVisible()){
                    mDrawer.toggleMenu();
                }
                if (Utils.isNetworkAvailable(this)){
                    Utils.showCustomProgressDialog(this, false, null);
                    if (pHelper.getServiceIP() != null){
                        new HttpRequester(this, Const.Typedata.CATALOGS, this, Const.ServiceCode.SYNCCATALOGS, pHelper.getServiceIP(), null);
                    } else {
                        Utils.removeCustomProgressDialog();
                        Utils.showToast("No has introducido un servicio", this);
                        llMenuIP.callOnClick();
                    }
                } else {
                    Utils.showToast("No hay internet", this);
                }
                break;


            case R.id.llMenuSyncInf:
                if (mDrawer.isMenuVisible()){
                    mDrawer.toggleMenu();
                }
                if (Utils.isNetworkAvailable(this)){
                    Utils.showCustomProgressDialog(this, false, null);
                    if (pHelper.getServiceIP() != null){
                        //new SyncManual(this);
                    } else {
                        Utils.removeCustomProgressDialog();
                        Utils.showToast("No has introducido un servicio", this);
                        llMenuIP.callOnClick();
                    }
                } else {
                    Utils.showToast("No hay internet", this);
                }
                break;

            case R.id.llMenuLogout:
                if (mDrawer.isMenuVisible()){
                    mDrawer.toggleMenu();
                }
                Utils.showAlertDialogOk(this, "¿Deseas cerrar sesión?", null, null, false, 0);
                break;
            case R.id.ibMenuNew:

                    if (pHelper.getLastFecha() != null){
                        if (!pHelper.getIdPersona().equals("0")) {
                            ibMenuNew.setEnabled(false);
                            i = new Intent().setClass(MainMenu.this, InfraccionActivity.class);
                            i.putExtra("idInfraccion", "0");
                            startActivity(i);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {
                            Utils.showToast("El administrador no puede dar de alta infracciónes", this);
                        }
                    } else {
                        Utils.showToast("Se realizará primero la sincronización de catálogos", this);
                        llMenuSync.callOnClick();
                    }
                break;
            case R.id.ibMenuSearch:
                if (pHelper.getLastFecha() != null) {
                    Utils.showAlertDialog(this, "Seleccióna el tipo de búsqueda", R.drawable.ic_internet, R.drawable.ic_local);
                } else {
                    Utils.showToast("Se realizará primero la sincronización de catálogos", this);
                    llMenuSync.callOnClick();
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ibMenuNew.setEnabled(true);
        ibMenuSearch.setEnabled(true);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) throws JSONException {
        switch (serviceCode){
            case Const.ServiceCode.SYNCCATALOGS:
                if (!response.equals("")){
                    if (pContent.isSuccess(response)){
                        JSONObject object = new JSONObject(response);
                        boolean insert = dbHelper.setCatalogs(object, this);
                        if (!insert){
                            Utils.showToast("Error al guardar los catálogos", this);
                        } else {
                            Utils.showToast("Catálogos guardados correctamente", this);
                        }
                    } else {
                        Utils.showToast(pContent.getError(response), this);
                    }
                } else {
                    Utils.showToast("Error al conectar con el servidor", this);
                }
                Utils.removeCustomProgressDialog();
                break;
            case Const.ServiceCode.SYNCIMAGES:
                if (!response.equals("")) {


                    if (pContent.isSuccess(response)) {
                        Utils.showToast("Imágenes guardadas correctamente", this);
                    } else {
                        Utils.showToast(pContent.getError(response), this);
                    }

                    JSONObject object = new JSONObject(response);

                   if (!object.getString("Ids").contains("null")){
                        dbHelper.removeCorrectImg(pContent.errorImages(response));
                   }


                } else {
                    Utils.showToast("Error al conectar con el servidor", this);
                }
                Utils.removeCustomProgressDialog();
                break;
        }
    }
}
