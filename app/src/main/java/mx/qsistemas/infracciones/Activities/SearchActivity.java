package mx.qsistemas.infracciones.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.qsistemas.infracciones.DataManagement.Adapters.OnlineSearchAdapter;
import mx.qsistemas.infracciones.DataManagement.Adapters.SearchAdapter;
import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Database.DBHelper;
import mx.qsistemas.infracciones.DataManagement.Models.Catalogs;
import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;
import mx.qsistemas.infracciones.DataManagement.Models.SearchTerms;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.DataManagement.Webservices.HttpRequester;
import mx.qsistemas.infracciones.DataManagement.Webservices.ParseContent;
import mx.qsistemas.infracciones.InfraBase;
import mx.qsistemas.infracciones.R;
import mx.qsistemas.infracciones.Utils.Utils;

public class SearchActivity extends InfraBase implements AdapterView.OnItemClickListener {

    public DBHelper dbHelper;
    public Button btnSearchSearch;
    public EditText etSearchNumId, etSearchFolio;
    public ListView lvSearchList;
    public Spinner spSearchIdentificador;
    public SearchTerms searchterms;

    public Activity mActivity;
    public ArrayList<GetSaveInfra> searchResults;
    public ArrayList<SearchTerms> onlineSearchResults;
    public SearchAdapter searchAdapter;
    public OnlineSearchAdapter onlineSearchAdapter;
    public ArrayList<Catalogs> marcalist, submarcalist, tipolist, colorlist, idlist, estadolist, tipodoclist, articulolist,
            fraccionlist, disposicionlist, seretienelist, municipiolist, tipoliclist, configlist, lastinfralist, diasnohabileslist;
    public ArrayAdapter<Catalogs> idadapter;
    ArrayList<ArrayList<Catalogs>> catalogs;
    boolean isOnline;
    PreferenceHelper pHelper;
    ParseContent pContent;

    @Override
    public void onBackPressed() {
        if (isOnline){
            try {
                pHelper.putIsSearch(null);
                Utils.setMobileDataState(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setBarColor("#000000");
        setCenterText(getString(R.string.search), Color.WHITE);

        Intent intent = getIntent();
        isOnline = intent.getBooleanExtra("isOnline", false);

        dbHelper = new DBHelper(this);
        pHelper = new PreferenceHelper(this);

        mActivity = SearchActivity.this;
        searchterms = new SearchTerms();
        pContent = new ParseContent(this);

        btnSearchSearch = (Button) findViewById(R.id.btnSearchSearch);
        btnSearchSearch.setOnClickListener(this);
        etSearchNumId = (EditText) findViewById(R.id.etSearchNumId);
        etSearchFolio = (EditText) findViewById(R.id.etSearchFolio);
        lvSearchList = (ListView) findViewById(R.id.lvSearchList);
        lvSearchList.setOnItemClickListener(this);
        spSearchIdentificador = (Spinner) findViewById(R.id.spSearchIdentificador);

        if (isOnline){
            try {
                pHelper.putIsSearch("1");
                Utils.setMobileDataState(this, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Utils.showCustomProgressDialog(this, false, null);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new spinnersdata().execute();
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (isOnline){
            Utils.showCustomProgressDialog(this, false, null);
            SearchTerms terms = (SearchTerms) adapterView.getAdapter().getItem(position);
            new HttpRequester(this, Const.Typedata.ONLINEITEM, this, Const.ServiceCode.ONLINITEM, pHelper.getServiceIP(), terms);
        } else {
            GetSaveInfra data = (GetSaveInfra) adapterView.getAdapter().getItem(position);
            data.setOnline(false);
            Intent i = new Intent().setClass(SearchActivity.this, InfraccionActivity.class);
            i.putExtra("data", data);
            i.putExtra("idInfraccion", data.getInfraid());
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    private class spinnersdata extends AsyncTask<Boolean, Integer, String> {

        @Override
        protected String doInBackground(Boolean... booleen) {
            catalogs = dbHelper.getCatalogs(SearchActivity.this);
            marcalist = catalogs.get(0);
            submarcalist = catalogs.get(1);
            tipolist = catalogs.get(2);
            colorlist = catalogs.get(3);
            idlist = catalogs.get(4);
            estadolist = catalogs.get(5);
            tipodoclist = catalogs.get(6);
            articulolist = catalogs.get(7);
            fraccionlist = catalogs.get(8);
            disposicionlist = catalogs.get(9);
            seretienelist = catalogs.get(10);
            municipiolist = catalogs.get(11);
            tipoliclist = catalogs.get(12);
            configlist = catalogs.get(13);
            lastinfralist = catalogs.get(14);
            diasnohabileslist = catalogs.get(15);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                onTaskCompleted(s, Const.ServiceCode.SPINNER_DATABASE_LOAD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class searchinfra extends AsyncTask<Boolean, Integer, String>{

        @Override
        protected String doInBackground(Boolean... params) {
                searchResults = dbHelper.getInfracciones(searchterms, false);
            return "{success:true}";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                onTaskCompleted(s, Const.ServiceCode.SEARCH_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSearchSearch:
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (isValidate()){
                    Utils.showCustomProgressDialog(this, false, null);
                    if (isOnline){
                        new HttpRequester(this, Const.Typedata.ONLINESEARCH, this, Const.ServiceCode.SEARCH_ONLINE_SUCCESS, pHelper.getServiceIP(), searchterms);
                    } else {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new searchinfra().execute();
                            }
                        });
                    }
                } else {
                    Utils.showToast(getString(R.string.no_params), this);
                }
                break;
        }
    }

    private boolean isValidate() {
        int i = 0;

        searchterms = new SearchTerms();

        if (spSearchIdentificador.getSelectedItemPosition() != 0){
            if (!etSearchNumId.getText().toString().equals("") || !etSearchNumId.getText().toString().isEmpty()){
                searchterms.setNumidentifica(etSearchNumId.getText().toString());
                Catalogs cat = (Catalogs) spSearchIdentificador.getSelectedItem();
                searchterms.setIdentifica(cat.getId());
                i = 1;
            }
        }


        if (!etSearchFolio.getText().toString().equals("") || !etSearchFolio.getText().toString().isEmpty()){
            searchterms.setFolio(etSearchFolio.getText().toString());
            i = 1;
        }


        return i != 0;
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) throws JSONException {
        switch (serviceCode){
            case Const.ServiceCode.SPINNER_DATABASE_LOAD:
                spinnerfill();
                Utils.removeCustomProgressDialog();
                break;
            case Const.ServiceCode.SEARCH_SUCCESS:

                    if (searchResults != null){
                        searchAdapter = new SearchAdapter(this, searchResults);
                        lvSearchList.setAdapter(searchAdapter);
                    } else {
                        Utils.showToast(getResources().getString(R.string.noresults), this);
                    }

                Utils.removeCustomProgressDialog();
                break;
            case Const.ServiceCode.SEARCH_ONLINE_SUCCESS:
                if (!response.equals("")) {
                    if (pContent.isSuccess(response)) {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("Resultados");
                        JSONObject object1;
                        onlineSearchResults = new ArrayList<>();
                        SearchTerms search;
                        for (int i= 0; i < array.length(); i++){
                            object1 = array.getJSONObject(i);
                            search = new SearchTerms();
                            search.setFolio(object1.getString("Folio"));
                            search.setIdentifica(object1.getString("DocumentoIdentificador"));
                            search.setNumidentifica(object1.getString("NumeroDocumentoIdentificador"));
                            search.setIdinfra(object1.getString("IdInfraccion"));
                            onlineSearchResults.add(search);
                        }
                        onlineSearchAdapter = new OnlineSearchAdapter(this, onlineSearchResults);
                        lvSearchList.setAdapter(onlineSearchAdapter);
                    } else {
                        Utils.showToast(pContent.getError(response), this);
                    }
                } else {
                    Utils.showToast("Error al conectar con el servidor", this);
                }
                Utils.removeCustomProgressDialog();
                break;
            case Const.ServiceCode.ONLINITEM:
                if (!response.equals("")) {
                    if (pContent.isSuccess(response)) {
                        GetSaveInfra data = pContent.getdataitem(response, catalogs);
                        if (data != null){
                            try {
                                String[] oficial = dbHelper.getOficial(data.getId_persona_ayun());
                                data.setOficialcaptura(oficial[0]);
                                data.setOficialnum(oficial[1]);
                            } catch (Exception e){
                                e.printStackTrace();
                                data.setOficialcaptura("-");
                                data.setOficialnum("000000");
                            }
                            data.setOnline(true);
                            Intent i = new Intent().setClass(SearchActivity.this, InfraccionActivity.class);
                            i.putExtra("data", data);
                            i.putExtra("idInfraccion", "");
                            startActivity(i);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        } else {
                            Utils.showToast("Error al procesar la infromación", this);
                        }
                    } else {
                        Utils.showToast(pContent.getError(response), this);
                    }
                } else {
                    Utils.showToast("Error al conectar con el servidor", this);
                }
                Utils.removeCustomProgressDialog();
                break;
        }
    }

    private void spinnerfill() {
        idadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,idlist);
        idadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSearchIdentificador.setAdapter(idadapter);
    }
}
