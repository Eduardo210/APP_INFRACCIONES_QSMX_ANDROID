package mx.qsistemas.infracciones.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.basewin.aidl.OnBarcodeCallBack;
import com.basewin.interfaces.OnDetectListener;
import com.basewin.services.CardBinder;
import com.basewin.services.ServiceManager;
import com.basewin.utils.BCDHelper;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.pos.sdk.card.PosCardInfo;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mx.qsistemas.infracciones.DataManagement.Adapters.FraccionAdapter;
import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Database.DBHelper;
import mx.qsistemas.infracciones.DataManagement.Models.ArtFraccion;
import mx.qsistemas.infracciones.DataManagement.Models.Catalogs;
import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;
import mx.qsistemas.infracciones.DataManagement.Models.Persona;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.Functions.PrintInfraNew;
import mx.qsistemas.infracciones.Functions.SyncProcess;
import mx.qsistemas.infracciones.InfraBase;
import mx.qsistemas.infracciones.R;
import mx.qsistemas.infracciones.Utils.Utils;

public class InfraccionActivity extends InfraBase implements CompoundButton.OnCheckedChangeListener, LocationListener {

    public ImageButton ibInfraMenuCancel, ibInfraMenuPayments, ibInfraMenuPrint, ibInfraMenuEdit, ibInfraMenuSave;
    public boolean isUpdate, isOnline;
    public DBHelper dbHelper;
    public Spinner spInfraMarca, spInfraSubMarca, spInfraType, spInfraColor, spInfraID, spInfraExpedido, spInfraTipoDoc, spInfraArticulo,
            spInfraFraccion, spInfraDisposicion, spInfraRetiene, spInfraInfraEntidad, spInfraInfraDelMun, spInfraLicTipo, spInfraLicExpedida;
    public EditText etInfraSubMarca, etInfraAno, etInfraNumID, etInfraTarjetaCirc, etInfraCalle, etInfraEntre, etInfraStreetY, etInfraColonia,
            etInfraMotivacion, etInfraInfraNombre, etInfraInfraPaterno, etInfraInfraMaterno, etInfraInfraRFC, etInfraInfraCalle, etInfraInfraExterior,
            etInfraInfraInterior, etInfraInfraColonia, etInfraLicNum;
    public TextView tvInfraDias, tvInfraTotal, tvInfraPuntos;
    public Button btnInfraArticulos;
    public ExpandableHeightListView lvInfraArticulos;
    public ImageView ivInfraCapId, ivInfraCapIdtwo;
    public LinearLayout llInfraSubmarcaOtra, llInfraListFraccion, llInfraDisposicion, llInfraInfraAusente, llInfraFraccion, llInfraEdit;
    public RadioButton rbInfraAusenteSi, rbInfraAusenteNo, rbInfraRemSi, rbInfraRemNo;
    public ScrollView svInfraMain;
    public String folioInfraccion, idInfraccion;
    public Activity mActivity;
    public Space spaceinfra;
    public ArrayList<Catalogs> marcalist, submarcalist, tipolist, colorlist, idlist, estadolist, tipodoclist, articulolist, fraccionlist, disposicionlist,
            seretienelist, municipiolist, tipoliclist, configlist, lastinfralist, diasnohabileslist;

    public ArrayAdapter<Catalogs> marcaadapter, submarcaadapter, tipoadapter, coloradapter, idadapter, estadoadapter, tipodocadapter, articuloadapter, fraccionadapter, disposicionadapter,
            seretieneadapter, municipioadapter, tipolicadapter;

    public ArrayList<ArtFraccion> artlist;
    public FraccionAdapter adapter;
    public GetSaveInfra saveData;
    public File photofile, photopath;
    public Uri imageUri;
    public int selectedPhoto, sumasalarios, sumapuntos;
    public float sumatotal;
    public ByteArrayOutputStream bos;
    public byte[] ba;
    public PreferenceHelper pHelper;
    public Context context;
    private Persona persona;
    double latitude;
    double longitude;
    private LocationManager locationManager;
    int count;

    private Button btnLeerLic, btnLeerTC, btnValidaDocs;
    private CardBinder cardService;
    private PosCardInfo posCardInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infraccion);
        setBarColor("#000000");
        saveData = new GetSaveInfra();
        clearsavedata();
        Intent intent = getIntent();
        if (intent.getStringExtra("idInfraccion").equals("0")) {
            setCenterText(getString(R.string.new_violation), Color.WHITE);
            isUpdate = false;
        } else {
            saveData = (GetSaveInfra) intent.getSerializableExtra("data");
            idInfraccion = saveData.getInfraid();
            folioInfraccion = saveData.getInfraFolio();
            setCenterText(getString(R.string.violation_number) + folioInfraccion, Color.WHITE);
            isUpdate = true;
            isOnline = saveData.isOnline();
        }

        if (!isOnline) {
            try {
                Utils.setMobileDataState(this, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        dbHelper = new DBHelper(this);
        pHelper = new PreferenceHelper(this);
        context = InfraccionActivity.this;

        if (!isUpdate) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            count = 0;
        }



        ibInfraMenuCancel = findViewById(R.id.ibInfraMenuCancel);
        ibInfraMenuPayments = findViewById(R.id.ibInfraMenuPayments);
        ibInfraMenuPrint = findViewById(R.id.ibInfraMenuPrint);
        ibInfraMenuEdit = findViewById(R.id.ibInfraMenuEdit);
        ibInfraMenuSave = findViewById(R.id.ibInfraMenuSave);

        ibInfraMenuCancel.setOnClickListener(this);
        ibInfraMenuPayments.setOnClickListener(this);
        ibInfraMenuPrint.setOnClickListener(this);
        ibInfraMenuEdit.setOnClickListener(this);
        ibInfraMenuSave.setOnClickListener(this);

        svInfraMain = findViewById(R.id.svInfraMain);

        checkHeaderFooter();
        initViewComplete();
        loadData();

    }


    private void clearsavedata() {
        saveData.setInfraid("");
        saveData.setInfraFolio("");
        saveData.setInfradir_calle("");
        saveData.setInfradir_entre("");
        saveData.setInfradir_y("");
        saveData.setInfradir_colonia("");
        saveData.setAno("");
        saveData.setIdentificacion_num("");
        saveData.setTarjeta_circ("");
        saveData.setMotivacion("");
        saveData.setSalariosminimos("");
        saveData.setImporte("");
        saveData.setPuntossancion("");
        saveData.setLinea_uno("");
        saveData.setLinea_dos("");
        saveData.setLinea_tres("");
        saveData.setFecha_uno("");
        saveData.setFecha_dos("");
        saveData.setFecha_tres("");
        saveData.setImporte_uno("");
        saveData.setImporte_dos("");
        saveData.setImporte_tres("");
        saveData.setId_persona_ayun("");
        saveData.setFechahora("");
        saveData.setMarcatext("");
        saveData.setSubmarcatext("");
        saveData.setTipotext("");
        saveData.setColortext("");
        saveData.setIdentificaciontext("");
        saveData.setTipo_doctext("");
        saveData.setExpedido_entext("");
        saveData.setTaza_salario("");
        saveData.setSubmarca_otra("");
        saveData.setInfractor_nombre("");
        saveData.setInfractor_paterno("");
        saveData.setInfractor_materno("");
        saveData.setInfractor_rfc("");
        saveData.setInfractor_calle("");
        saveData.setInfractor_exterior("");
        saveData.setInfractor_interior("");
        saveData.setInfractor_colonia("");
        saveData.setTipolic_num("");
        saveData.setDisposicion_text("");
        saveData.setSe_retienetext("");
        saveData.setInfractor_entidadtext("");
        saveData.setInfractor_delmuntext("");
        saveData.setTipolic_tipotext("");
        saveData.setTipolic_expedidatext("");
        Intent msgIntent = new Intent(InfraccionActivity.this, SyncProcess.class);
        startService(msgIntent);
    }

    private void checkHeaderFooter() {
        if (pHelper.getHeader1() == null)
            pHelper.putHeader1("Head1");
        if (pHelper.getHeader2() == null)
            pHelper.putHeader2("Head2");
        if (pHelper.getHeader3() == null)
            pHelper.putHeader3("Head3");
        if (pHelper.getHeader4() == null)
            pHelper.putHeader4("Head4");
        if (pHelper.getHeader5() == null)
            pHelper.putHeader5("Head5");
        if (pHelper.getHeader6() == null)
            pHelper.putHeader6("Head6");
        if (pHelper.getFooter1() == null)
            pHelper.putFooter1("Foot1");
        if (pHelper.getFooter2() == null)
            pHelper.putFooter2("Foot2");
        if (pHelper.getFooter3() == null)
            pHelper.putFooter3("Foot3");
    }

    private void initViewComplete() {
        spInfraMarca = findViewById(R.id.spInfraMarca);
        spInfraSubMarca = findViewById(R.id.spInfraSubMarca);
        spInfraType = findViewById(R.id.spInfraType);
        spInfraColor = findViewById(R.id.spInfraColor);
        spInfraID = findViewById(R.id.spInfraID);
        spInfraExpedido = findViewById(R.id.spInfraExpedido);
        spInfraTipoDoc = findViewById(R.id.spInfraTipoDoc);
        spInfraArticulo = findViewById(R.id.spInfraArticulo);
        spInfraFraccion = findViewById(R.id.spInfraFraccion);
        spInfraDisposicion = findViewById(R.id.spInfraDisposicion);
        spInfraRetiene = findViewById(R.id.spInfraRetiene);
        spInfraInfraEntidad = findViewById(R.id.spInfraInfraEntidad);
        spInfraInfraDelMun = findViewById(R.id.spInfraInfraDelMun);
        spInfraLicTipo = findViewById(R.id.spInfraLicTipo);
        spInfraLicExpedida = findViewById(R.id.spInfraLicExpedida);

        etInfraSubMarca = findViewById(R.id.etInfraSubMarca);
        etInfraAno = findViewById(R.id.etInfraAno);
        etInfraNumID = findViewById(R.id.etInfraNumID);
        etInfraTarjetaCirc = findViewById(R.id.etInfraTarjetaCirc);
        etInfraCalle = findViewById(R.id.etInfraCalle);
        etInfraEntre = findViewById(R.id.etInfraEntre);
        etInfraStreetY = findViewById(R.id.etInfraStreetY);
        etInfraColonia = findViewById(R.id.etInfraColonia);
        etInfraMotivacion = findViewById(R.id.etInfraMotivacion);
        etInfraInfraNombre = findViewById(R.id.etInfraInfraNombre);
        etInfraInfraPaterno = findViewById(R.id.etInfraInfraPaterno);
        etInfraInfraMaterno = findViewById(R.id.etInfraInfraMaterno);
        etInfraInfraRFC = findViewById(R.id.etInfraInfraRFC);
        etInfraInfraCalle = findViewById(R.id.etInfraInfraCalle);
        etInfraInfraExterior = findViewById(R.id.etInfraInfraExterior);
        etInfraInfraInterior = findViewById(R.id.etInfraInfraInterior);
        etInfraInfraColonia = findViewById(R.id.etInfraInfraColonia);
        etInfraLicNum = findViewById(R.id.etInfraLicNum);

        tvInfraDias = findViewById(R.id.tvInfraDias);
        tvInfraTotal = findViewById(R.id.tvInfraTotal);
        tvInfraPuntos = findViewById(R.id.tvInfraPuntos);

        btnInfraArticulos = findViewById(R.id.btnInfraArticulos);
        btnInfraArticulos.setOnClickListener(this);

        ivInfraCapId = findViewById(R.id.ivInfraCapId);
        ivInfraCapIdtwo = findViewById(R.id.ivInfraCapIdtwo);
        ivInfraCapId.setOnClickListener(this);
        ivInfraCapIdtwo.setOnClickListener(this);

        llInfraSubmarcaOtra = findViewById(R.id.llInfraSubmarcaOtra);
        llInfraSubmarcaOtra.setVisibility(View.GONE);
        llInfraListFraccion = findViewById(R.id.llInfraListFraccion);
        llInfraDisposicion = findViewById(R.id.llInfraDisposicion);
        llInfraInfraAusente = findViewById(R.id.llInfraInfraAusente);
        llInfraFraccion = findViewById(R.id.llInfraFraccion);
        llInfraEdit = findViewById(R.id.llInfraEdit);

        rbInfraAusenteSi = findViewById(R.id.rbInfraAusenteSi);
        rbInfraAusenteNo = findViewById(R.id.rbInfraAusenteNo);
        rbInfraRemSi = findViewById(R.id.rbInfraRemSi);
        rbInfraRemNo = findViewById(R.id.rbInfraRemNo);

        rbInfraAusenteNo.setChecked(true);
        rbInfraRemSi.setChecked(true);

        rbInfraAusenteSi.setOnCheckedChangeListener(this);
        rbInfraAusenteNo.setOnCheckedChangeListener(this);
        rbInfraRemSi.setOnCheckedChangeListener(this);
        rbInfraRemNo.setOnCheckedChangeListener(this);

        spaceinfra = findViewById(R.id.spaceinfra);

        lvInfraArticulos = findViewById(R.id.lvInfraArticulos);
        artlist = new ArrayList<>();
        adapter = new FraccionAdapter(this, artlist);
        lvInfraArticulos.setAdapter(adapter);
        lvInfraArticulos.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {
                if (lvInfraArticulos.getCount() != 0){
                    sumasalarios = 0;
                    sumapuntos = 0;
                    for (int i = 0; i < lvInfraArticulos.getCount(); i++){
                        ArtFraccion artFraccion = adapter.getItem(i);
                        sumasalarios = sumasalarios + Integer.parseInt(artFraccion.getSalarios());
                        sumapuntos = sumapuntos + Integer.parseInt(artFraccion.getPuntos());
                    }
                    saveData.setTaza_salario(configlist.get(0).getDataone());
                    sumatotal = Float.parseFloat(configlist.get(0).getDataone()) * sumasalarios;
                    tvInfraDias.setText(getString(R.string.minsal) + sumasalarios);
                    tvInfraTotal.setText(getString(R.string.totalsum) + sumatotal);
                    tvInfraPuntos.setText(getString(R.string.pointssum)+ sumapuntos);
                } else {
                    tvInfraDias.setText(getString(R.string.minsal));
                    tvInfraTotal.setText(getString(R.string.totalsum));
                    tvInfraPuntos.setText(getString(R.string.pointssum));
                }
            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                onChildViewAdded(view, view1);
            }
        });

        lvInfraArticulos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        if (!isUpdate){
            ibInfraMenuEdit.setVisibility(View.GONE);
            ibInfraMenuPrint.setVisibility(View.GONE);
            ibInfraMenuPayments.setVisibility(View.GONE);
        } else {
            ibInfraMenuEdit.setVisibility(View.GONE);
            ibInfraMenuCancel.setImageResource(R.drawable.ic_infra_back);
            ibInfraMenuSave.setImageResource(R.drawable.ic_infra_edit);
            if (pHelper.getIdPersona().equals("0")){
                ibInfraMenuSave.setVisibility(View.GONE);
            }
            ibInfraMenuPayments.setVisibility(View.GONE);
            spInfraMarca.setEnabled(false);
            spInfraSubMarca.setEnabled(false);
            spInfraType.setEnabled(false);
            spInfraColor.setEnabled(false);
            spInfraID.setEnabled(false);
            spInfraExpedido.setEnabled(false);
            spInfraTipoDoc.setEnabled(false);
            spInfraArticulo.setEnabled(false);
            spInfraFraccion.setEnabled(false);
            spInfraDisposicion.setEnabled(false);
            spInfraRetiene.setEnabled(false);
            spInfraInfraEntidad.setEnabled(false);
            spInfraInfraDelMun.setEnabled(false);
            spInfraLicTipo.setEnabled(false);
            spInfraLicExpedida.setEnabled(false);
            etInfraSubMarca.setEnabled(false);
            etInfraAno.setEnabled(false);
            etInfraNumID.setEnabled(false);
            etInfraTarjetaCirc.setEnabled(false);
            etInfraCalle.setEnabled(false);
            etInfraEntre.setEnabled(false);
            etInfraStreetY.setEnabled(false);
            etInfraColonia.setEnabled(false);
            etInfraMotivacion.setEnabled(false);
            if (pHelper.getCanEdit().equals("1")){
                etInfraInfraNombre.setEnabled(true);
                etInfraInfraPaterno.setEnabled(true);
                etInfraInfraMaterno.setEnabled(true);
            } else {
                etInfraInfraNombre.setEnabled(false);
                etInfraInfraPaterno.setEnabled(false);
                etInfraInfraMaterno.setEnabled(false);
            }
            etInfraInfraRFC.setEnabled(false);
            etInfraInfraCalle.setEnabled(false);
            etInfraInfraExterior.setEnabled(false);
            etInfraInfraInterior.setEnabled(false);
            etInfraInfraColonia.setEnabled(false);
            etInfraLicNum.setEnabled(false);



            ivInfraCapId.setClickable(false);
            ivInfraCapIdtwo.setClickable(false);

            if (saveData.getIsAusente() == 1 || saveData.getIsAusente() == 2){
                rbInfraAusenteSi.setChecked(true);
            } else {
                rbInfraAusenteNo.setChecked(true);
            }

            if (saveData.getIsRemision() == 1){
                rbInfraRemSi.setChecked(true);
            } else {
                rbInfraRemNo.setChecked(true);
            }

            rbInfraRemSi.setEnabled(false);
            rbInfraRemNo.setEnabled(false);
            rbInfraAusenteSi.setEnabled(false);
            rbInfraAusenteNo.setEnabled(false);
            llInfraFraccion.setVisibility(View.GONE);
            llInfraListFraccion.setVisibility(View.VISIBLE);

            if (pHelper.getCanEdit().equals("0")){
                ibInfraMenuSave.setVisibility(View.GONE);
            }
        }


        //READER
        btnLeerLic = findViewById(R.id.btnLeerLic);
        btnLeerTC = findViewById(R.id.btnLeerTC);
        btnValidaDocs = findViewById(R.id.btnValidaDocs);

        btnValidaDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valida = new Intent();
                valida.setComponent(new ComponentName("mx.qsistemas.democards", "mx.qsistemas.democards.ScanActivity"));
                startActivity(valida);
            }
        });

        if (!isUpdate){
            btnLeerTC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(InfraccionActivity.this, R.style.DialogTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_exit);
                    dialog.setCanceledOnTouchOutside(false);
                    TextView tvCatDialTitle = dialog.findViewById(R.id.tvCatDialTitle);
                    ImageButton ibCatDialSave = dialog.findViewById(R.id.ibExitDialSave);
                    ImageButton ibCatDialCancel = dialog.findViewById(R.id.ibExitDialCancel);
                    ibCatDialSave.setImageDrawable(ContextCompat.getDrawable(InfraccionActivity.this, R.drawable.ic_card_picker));
                    ibCatDialCancel.setImageDrawable(ContextCompat.getDrawable(InfraccionActivity.this,R.drawable.ic_qr_picker));
                    tvCatDialTitle.setText(R.string.qr_or_card);
                    ibCatDialCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ServiceManager.getInstence().getScan().startScan(60 * 1000, new OnBarcodeCallBack() {
                                    @Override
                                    public void onScanResult(String s) throws RemoteException {
                                        setDataIntoInfra(1);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFinish() throws RemoteException {
                                        Utils.showToast(getString(R.string.cancelled_read), InfraccionActivity.this);
                                        dialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utils.showToast(getString(R.string.error_unknown_reading), InfraccionActivity.this);
                                dialog.dismiss();
                            }
                        }
                    });
                    ibCatDialSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Utils.showToast(getString(R.string.close_card_to_terminal), InfraccionActivity.this);

                                cardService = ServiceManager.getInstence().getCard();

                                cardService.openM1AndDetect(60 * 1000, new OnDetectListener() {
                                    @Override
                                    public void onSuccess(int i) {
                                        posCardInfo = new PosCardInfo();
                                        try {
                                            ServiceManager.getInstence().getCard().getCardInfo(i, posCardInfo);
                                            Utils.showToast(getString(R.string.read_ok) + " ID: " + BCDHelper.bcdToString(posCardInfo.mSerialNum, 0, posCardInfo.mSerialNum.length), InfraccionActivity.this);
                                            setDataIntoInfra(1);
                                            dialog.dismiss();
                                        } catch (Exception e) {
                                            Utils.showToast(getString(R.string.error_unknown_reading), InfraccionActivity.this);

                                            dialog.dismiss();
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        Utils.showToast(getString(R.string.cancelled_read), InfraccionActivity.this);
                                        dialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utils.showToast(getString(R.string.error_unknown_reading), InfraccionActivity.this);
                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();
                }
            });
        }

        btnLeerLic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(InfraccionActivity.this, R.style.DialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_exit);
                dialog.setCanceledOnTouchOutside(false);
                TextView tvCatDialTitle = dialog.findViewById(R.id.tvCatDialTitle);
                ImageButton ibCatDialSave = dialog.findViewById(R.id.ibExitDialSave);
                ImageButton ibCatDialCancel = dialog.findViewById(R.id.ibExitDialCancel);
                ibCatDialSave.setImageDrawable(ContextCompat.getDrawable(InfraccionActivity.this, R.drawable.ic_card_picker));
                ibCatDialCancel.setImageDrawable(ContextCompat.getDrawable(InfraccionActivity.this,R.drawable.ic_qr_picker));
                tvCatDialTitle.setText(R.string.qr_or_card);
                ibCatDialCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ServiceManager.getInstence().getScan().startScan(60 * 1000, new OnBarcodeCallBack() {
                                @Override
                                public void onScanResult(String s) throws RemoteException {
                                    setDataIntoInfra(2);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFinish() throws RemoteException {
                                    Utils.showToast(getString(R.string.cancelled_read), InfraccionActivity.this);
                                    dialog.dismiss();

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Utils.showToast(getString(R.string.error_unknown_reading), InfraccionActivity.this);
                            dialog.dismiss();
                        }
                    }
                });
                ibCatDialSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            Utils.showToast(getString(R.string.close_card_to_terminal), InfraccionActivity.this);
                            cardService = ServiceManager.getInstence().getCard();
                            cardService.openM1AndDetect(60 * 1000, new OnDetectListener() {
                                @Override
                                public void onSuccess(int i) {
                                    posCardInfo = new PosCardInfo();
                                    try {
                                        ServiceManager.getInstence().getCard().getCardInfo(i, posCardInfo);
                                        Utils.showToast(getString(R.string.read_ok) + " ID: " + BCDHelper.bcdToString(posCardInfo.mSerialNum, 0, posCardInfo.mSerialNum.length), InfraccionActivity.this);
                                        setDataIntoInfra(2);
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Utils.showToast(getString(R.string.cancelled_read), InfraccionActivity.this);
                                    dialog.dismiss();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Utils.showToast(getString(R.string.error_unknown_reading), InfraccionActivity.this);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });


    }

    //READER
    private void setDataIntoInfra(int type) {
        if (type == 1) {
            spInfraMarca.setSelection(getIndex(spInfraMarca.getAdapter(), 9));
            spInfraType.setSelection(getIndex(spInfraType.getAdapter(), 10));
            spInfraID.setSelection(getIndex(spInfraID.getAdapter(), 3));
            spInfraExpedido.setSelection(getIndex(spInfraExpedido.getAdapter(), 15));
            spInfraTipoDoc.setSelection(getIndex(spInfraTipoDoc.getAdapter(), 1));
            etInfraAno.setText("2012");
            etInfraNumID.setText("AU-C-3641855");
            etInfraTarjetaCirc.setText("3644888");
            etInfraSubMarca.setText("INFINITI");
        } else {
            etInfraInfraNombre.setText("MIGUEL");
            etInfraInfraPaterno.setText("PEÑALOZA");
            etInfraInfraMaterno.setText("GARCIA");
            etInfraInfraRFC.setText("PEGM840915");
            etInfraLicNum.setText("250000041252");
            spInfraLicTipo.setSelection(getIndex(spInfraTipoDoc.getAdapter(), 1));
            spInfraLicExpedida.setSelection(getIndex(spInfraLicExpedida.getAdapter(), 15));

        }
    }

    private void loadData() {
        Utils.showCustomProgressDialog(this, false, null);
        mActivity = InfraccionActivity.this;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new catalogsdata().execute();
            }
        });
    }

    private void updateDataSet() {

        artlist.addAll(saveData.getArticulos());
        adapter.notifyDataSetChanged();

        spInfraMarca.setSelection(getIndex(spInfraMarca.getAdapter(), saveData.getMarca()));
        spInfraType.setSelection(getIndex(spInfraType.getAdapter(), saveData.getTipo()));
        spInfraColor.setSelection(getIndex(spInfraColor.getAdapter(), saveData.getColor()));
        etInfraAno.setText(saveData.getAno());
        spInfraID.setSelection(getIndex(spInfraID.getAdapter(), saveData.getIdentificacion()));
        etInfraNumID.setText(saveData.getIdentificacion_num());

        File images = new File(Const.SD_PATH_IMG);
        if (images.exists()) {
            for (File f : images.listFiles()) {
                if (f.isFile()) {
                    String fullname = f.getName();
                    String[] name = fullname.split("\\.");
                    String[] foliofoto = name[0].split("_");
                    if (foliofoto.length == 4) {
                        if (foliofoto[2].equals(saveData.getInfraFolio())){
                            BitmapFactory.Options opc = new BitmapFactory.Options();
                            opc.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap bmp = BitmapFactory.decodeFile(Const.SD_PATH_IMG + f.getName(), opc);
                            Bitmap thumb = ThumbnailUtils.extractThumbnail(bmp, 640, 480);
                            switch (foliofoto[3]){
                                case "I":
                                    ivInfraCapId.setImageBitmap(thumb);
                                    break;
                                case "II":
                                    ivInfraCapIdtwo.setImageBitmap(thumb);
                                    break;
                            }
                        }
                    }
                }
            }
        }


        spInfraExpedido.setSelection(getIndex(spInfraExpedido.getAdapter(), saveData.getExpedido_en()));
        spInfraTipoDoc.setSelection(getIndex(spInfraTipoDoc.getAdapter(), saveData.getTipo_doc()));
        etInfraTarjetaCirc.setText(saveData.getTarjeta_circ());

        etInfraCalle.setText(saveData.getInfradir_calle());
        etInfraEntre.setText(saveData.getInfradir_entre());
        etInfraStreetY.setText(saveData.getInfradir_y());
        etInfraColonia.setText(saveData.getInfradir_colonia());

        if (saveData.getIsRemision() == 1){
            spInfraDisposicion.setSelection(getIndex(spInfraDisposicion.getAdapter(), saveData.getDisposicion()));
        }
        etInfraMotivacion.setText(saveData.getMotivacion());
        spInfraRetiene.setSelection(getIndex(spInfraRetiene.getAdapter(), saveData.getSe_retiene()));

        if (saveData.getIsAusente() == 0 || saveData.getIsAusente() == 2){
            etInfraInfraNombre.setText(saveData.getInfractor_nombre());
            etInfraInfraPaterno.setText(saveData.getInfractor_paterno());
            etInfraInfraMaterno.setText(saveData.getInfractor_materno());
            etInfraInfraRFC.setText(saveData.getInfractor_rfc());
            etInfraInfraCalle.setText(saveData.getInfractor_calle());
            etInfraInfraExterior.setText(saveData.getInfractor_exterior());
            etInfraInfraInterior.setText(saveData.getInfractor_interior());

            spInfraInfraEntidad.setSelection(getIndex(spInfraInfraEntidad.getAdapter(), saveData.getInfractor_entidad()));
            etInfraInfraColonia.setText(saveData.getInfractor_colonia());

            etInfraLicNum.setText(saveData.getTipolic_num());
            spInfraLicTipo.setSelection(getIndex(spInfraLicTipo.getAdapter(), saveData.getTipolic_tipo()));
            spInfraLicExpedida.setSelection(getIndex(spInfraLicExpedida.getAdapter(), saveData.getTipolic_expedida()));
        } else {
            llInfraEdit.setVisibility(View.GONE);
        }
    }

    private int getIndex(SpinnerAdapter adapter, int comparator) {
        Catalogs catalog;
        for (int i = 0; i < adapter.getCount(); i++){
            catalog = (Catalogs) adapter.getItem(i);
            if (catalog.getId().equals(String.valueOf(comparator))){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
        count++;
        if (count == 5){
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class catalogsdata extends AsyncTask<Boolean, Integer, String>{

        @Override
        protected String doInBackground(Boolean... booleen) {
            ArrayList<ArrayList<Catalogs>> catalogs;
            catalogs = dbHelper.getCatalogs(InfraccionActivity.this);

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

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(InfraccionActivity.this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_exit);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvCatDialTitle = dialog.findViewById(R.id.tvCatDialTitle);
        ImageButton ibCatDialSave = dialog.findViewById(R.id.ibExitDialSave);
        ibCatDialSave.setImageResource(R.drawable.ic_infra_ok);
        ImageButton ibCatDialCancel = dialog.findViewById(R.id.ibExitDialCancel);
        tvCatDialTitle.setText(R.string.should_go_back);
        ibCatDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ibCatDialSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isUpdate){
                    try {
                        pHelper.putIsSearch(null);
                        Utils.setMobileDataState(InfraccionActivity.this, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                locationManager.removeUpdates(InfraccionActivity.this);
                Intent i = new Intent().setClass(InfraccionActivity.this, MainMenu.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibInfraMenuCancel:
                onBackPressed();
                break;
            case R.id.ibInfraMenuPayments:
                break;
            case R.id.ibInfraMenuPrint:
                ibInfraMenuPrint.setEnabled(false);
                final Dialog dialog = new Dialog(InfraccionActivity.this, R.style.DialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_exit);
                dialog.setCanceledOnTouchOutside(false);
                TextView tvCatDialTitle = dialog.findViewById(R.id.tvCatDialTitle);
                ImageButton ibCatDialSave = dialog.findViewById(R.id.ibExitDialSave);
                ibCatDialSave.setImageResource(R.drawable.ic_infra_ok);
                ImageButton ibCatDialCancel = dialog.findViewById(R.id.ibExitDialCancel);
                tvCatDialTitle.setText(R.string.should_print);
                ibCatDialCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ibInfraMenuPrint.setEnabled(true);
                        dialog.dismiss();
                    }
                });
                ibCatDialSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.showCustomProgressDialog(InfraccionActivity.this,false,null);
                        new PrintInfraNew(InfraccionActivity.this, saveData, Const.ServiceCode.PRINT, InfraccionActivity.this);
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.ibInfraMenuEdit:
                break;
            case R.id.ibInfraMenuSave:
                if (!isUpdate){
                    resetBg();
                    if (isValidate()){
                        Utils.showCustomProgressDialog(this, false, null);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new saveData().execute();
                            }
                        });
                    }
                } else {
                    final Dialog dialog2 = new Dialog(InfraccionActivity.this, R.style.DialogTheme);
                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog2.setContentView(R.layout.custom_dialog_exit);
                    dialog2.setCanceledOnTouchOutside(false);
                    TextView tvCatDialTitle2 = dialog2.findViewById(R.id.tvCatDialTitle);
                    ImageButton ibCatDialSave2= dialog2.findViewById(R.id.ibExitDialSave);
                    ibCatDialSave2.setImageResource(R.drawable.ic_infra_ok);
                    ImageButton ibCatDialCancel2 = dialog2.findViewById(R.id.ibExitDialCancel);
                    tvCatDialTitle2.setText(R.string.make_changes);
                    ibCatDialCancel2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    ibCatDialSave2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isValidate()){
                                Utils.showCustomProgressDialog(InfraccionActivity.this, false, null);
                                persona.setNombre(etInfraInfraNombre.getText().toString().toUpperCase());
                                persona.setaPaterno(etInfraInfraPaterno.getText().toString().toUpperCase());
                                persona.setaMaterno(etInfraInfraMaterno.getText().toString().toUpperCase());
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new saveData().execute();
                                    }
                                });
                            }
                            dialog2.dismiss();
                        }
                    });
                    dialog2.show();
                }
                break;
            case R.id.btnInfraArticulos:
                if (spInfraArticulo.getSelectedItemPosition() != 0 && spInfraFraccion.getSelectedItemPosition() != 0){

                    boolean wasAddedBefore = false;

                    ArtFraccion article = new ArtFraccion();
                    Catalogs getfraccion = (Catalogs) spInfraFraccion.getSelectedItem();
                    Catalogs getarticle = (Catalogs) spInfraArticulo.getSelectedItem();
                    article.setId(getfraccion.getId());
                    article.setIdarticulo(getfraccion.getUnion());
                    article.setArticulo(getarticle.getValue());
                    article.setFraccion(getfraccion.getValue());
                    article.setDescripcion(getfraccion.getDataone());
                    article.setSalarios(getfraccion.getDatatwo());
                    article.setPuntos(getfraccion.getDatathree());
                    article.setIsSearch(0);

                    for (int i = 0; i < artlist.size(); i++){
                        ArtFraccion artFraccion = artlist.get(i);
                        if (artFraccion.getId().equals(getfraccion.getId())){
                            wasAddedBefore = true;
                        }
                    }

                    if (!wasAddedBefore){
                        artlist.add(article);
                        adapter.notifyDataSetChanged();
                        llInfraListFraccion.setVisibility(View.VISIBLE);
                    } else {
                        Utils.showToast(getString(R.string.added_before), this);
                    }


                } else {
                    Utils.showToast("Seleccióne un artículo y una fracción para agregar a la lista", this);
                }
                break;
            case R.id.ivInfraCapId:
                if (saveData.getImagen_1() == null){
                    if (validateDoc()){
                        selectedPhoto = 1;
                        intentcam();
                    } else {
                        Utils.showToast("Ingresa primero el documeto a fotografiar.", this);
                    }
                } else {
                    Utils.showToast("Ya se capturó el documento previamente.", this);
                }


                break;
            case R.id.ivInfraCapIdtwo:
                if (saveData.getImagen_2() == null){
                    if (validateDoc()){
                        selectedPhoto = 2;
                        intentcam();
                    } else {
                        Utils.showToast("Ingresa primero el documento a fotografiar.", this);
                    }
                } else {
                    Utils.showToast("Ya se capturó el documento previamente.", this);
                }
                break;
        }
    }

    private boolean validateDoc() {
        resetBg();
        boolean isvalidate = true;
        if (etInfraNumID.getText().toString().isEmpty()){
            etInfraNumID.setBackgroundResource(R.drawable.square_bg_input_invalid);
            isvalidate = false;
        }
        if (spInfraID.getSelectedItemPosition() == 0){
            spInfraID.setBackgroundResource(R.drawable.square_bg_input_invalid);
            isvalidate = false;
        }
        return isvalidate;
    }

    private class saveData extends AsyncTask<Boolean, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Boolean... booleen) {
            if (!isUpdate){
                createSaveItem();
                GetSaveInfra printable = dbHelper.savedata(saveData);
                savePhotos();
                if (printable != null){
                    saveData = printable;
                    return true;
                }
            } else {
                if (isOnline){
                    if (saveData.getInfraid() == null || saveData.getInfraid().equals("")){
                        saveData.setIsUpdate(0);
                        createSaveItem();
                        GetSaveInfra printable = dbHelper.savedata(saveData);
                        if (printable != null){
                            saveData = printable;
                            return true;
                        }
                    } else {
                        saveData.setIsUpdate(1);
                        GetSaveInfra printable = dbHelper.updateData(persona, saveData);
                        saveData = printable;
                    }
                } else {
                    GetSaveInfra printable = dbHelper.updateData(persona, saveData);
                    saveData = printable;
                }
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                try {
                    onTaskCompleted("true", Const.ServiceCode.SAVE_COMPLETE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    onTaskCompleted("false", Const.ServiceCode.SAVE_COMPLETE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void savePhotos() {
        File imgdir = new File(Const.SD_PATH_IMG);
        File img;
        BufferedOutputStream buffos;

        if (!imgdir.exists()){
            imgdir.mkdirs();
        }

        String imageone = saveData.getIdentificacion() + "_" + saveData.getIdentificacion_num() + "_" + saveData.getInfraFolio() + "_I.jpg";
        String imagetwo = saveData.getIdentificacion() + "_" + saveData.getIdentificacion_num() + "_" + saveData.getInfraFolio() + "_II.jpg";

        if (saveData.getImagen_1() != null){
            img = new File(imgdir, imageone);
            try{
                buffos = new BufferedOutputStream(new FileOutputStream(img));
                buffos.write(saveData.getImagen_1());
                buffos.flush();
                buffos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (saveData.getImagen_2() != null){
            img = new File(imgdir, imagetwo);
            try{
                buffos = new BufferedOutputStream(new FileOutputStream(img));
                buffos.write(saveData.getImagen_2());
                buffos.flush();
                buffos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dbHelper.savephotos(imageone, imagetwo);
    }

    private void createSaveItem() {
        Catalogs savecat;

        savecat = (Catalogs) spInfraMarca.getSelectedItem();
        saveData.setMarca(Integer.parseInt(savecat.getId()));
        saveData.setMarcatext(savecat.getValue().toUpperCase());

        if (spInfraSubMarca.getSelectedItemPosition() == 0){
            saveData.setSubmarca_otra(etInfraSubMarca.getText().toString().toUpperCase());
            saveData.setSubmarca(0);
        } else {
            savecat = (Catalogs) spInfraSubMarca.getSelectedItem();
            saveData.setSubmarca(Integer.parseInt(savecat.getId()));
            saveData.setSubmarcatext(savecat.getValue().toUpperCase());
        }

        savecat = (Catalogs) spInfraType.getSelectedItem();
        saveData.setTipo(Integer.parseInt(savecat.getId()));
        saveData.setTipotext(savecat.getValue().toUpperCase());

        savecat = (Catalogs) spInfraColor.getSelectedItem();
        saveData.setColor(Integer.parseInt(savecat.getId()));
        saveData.setColortext(savecat.getValue().toUpperCase());

        saveData.setAno(etInfraAno.getText().toString().toUpperCase());

        savecat = (Catalogs) spInfraID.getSelectedItem();
        saveData.setIdentificacion(Integer.parseInt(savecat.getId()));
        saveData.setIdentificaciontext(savecat.getValue().toUpperCase());

        saveData.setIdentificacion_num(etInfraNumID.getText().toString().toUpperCase());

        savecat = (Catalogs) spInfraExpedido.getSelectedItem();
        saveData.setExpedido_en(Integer.parseInt(savecat.getId()));
        saveData.setExpedido_entext(savecat.getValue().toUpperCase());

        savecat = (Catalogs) spInfraTipoDoc.getSelectedItem();
        saveData.setTipo_doc(Integer.parseInt(savecat.getId()));
        saveData.setTipo_doctext(savecat.getValue().toUpperCase());


        if (etInfraTarjetaCirc.getText().toString().isEmpty() || etInfraTarjetaCirc.getText().toString().equals("")){
            saveData.setTarjeta_circ("");
        } else {
            saveData.setTarjeta_circ(etInfraTarjetaCirc.getText().toString().toUpperCase());
        }

        // LUG INFRA

        saveData.setInfradir_calle(etInfraCalle.getText().toString().toUpperCase());
        saveData.setInfradir_entre(etInfraEntre.getText().toString().toUpperCase());
        saveData.setInfradir_y(etInfraStreetY.getText().toString().toUpperCase());
        saveData.setInfradir_colonia(etInfraColonia.getText().toString().toUpperCase());

        // Articulos

        saveData.setArticulos(artlist);

        saveData.setSalariosminimos(String.valueOf(sumasalarios));
        saveData.setImporte(String.valueOf(sumatotal));
        saveData.setPuntossancion(String.valueOf(sumapuntos));

        if(rbInfraRemSi.isChecked()){
            saveData.setIsRemision(1);
            savecat = (Catalogs) spInfraDisposicion.getSelectedItem();
            saveData.setDisposicion(Integer.parseInt(savecat.getId()));
            saveData.setDisposicion_text(savecat.getValue().toUpperCase());
        } else {
            saveData.setIsRemision(0);
        }

        saveData.setMotivacion(etInfraMotivacion.getText().toString().toUpperCase());

        savecat = (Catalogs) spInfraRetiene.getSelectedItem();
        saveData.setSe_retiene(Integer.parseInt(savecat.getId()));
        saveData.setSe_retienetext(savecat.getValue().toUpperCase());

        // Infractor

        if (rbInfraAusenteSi.isChecked()){
            saveData.setIsAusente(1);
        } else {
            saveData.setIsAusente(0);
            if (etInfraInfraNombre.getText().toString().isEmpty() || etInfraInfraNombre.getText().toString().equals("")){
                saveData.setInfractor_nombre("");
            } else {
                saveData.setInfractor_nombre(etInfraInfraNombre.getText().toString().toUpperCase());
            }

            if (etInfraInfraPaterno.getText().toString().isEmpty() || etInfraInfraPaterno.getText().toString().equals("")){
                saveData.setInfractor_paterno("");
            } else {
                saveData.setInfractor_paterno(etInfraInfraPaterno.getText().toString().toUpperCase());
            }

            if (etInfraInfraMaterno.getText().toString().isEmpty() || etInfraInfraMaterno.getText().toString().equals("")){
                saveData.setInfractor_materno("");
            } else {
                saveData.setInfractor_materno(etInfraInfraMaterno.getText().toString().toUpperCase());
            }

            if (etInfraInfraRFC.getText().toString().isEmpty() || etInfraInfraRFC.getText().toString().equals("")){
                saveData.setInfractor_rfc("");
            } else {
                saveData.setInfractor_rfc(etInfraInfraRFC.getText().toString().toUpperCase());
            }
            if (etInfraInfraCalle.getText().toString().isEmpty() || etInfraInfraCalle.getText().toString().equals("")){
                saveData.setInfractor_calle("");
            } else {
                saveData.setInfractor_calle(etInfraInfraCalle.getText().toString().toUpperCase());
            }
            if (etInfraInfraExterior.getText().toString().isEmpty() || etInfraInfraExterior.getText().toString().equals("")){
                saveData.setInfractor_interior("");
            } else {
                saveData.setInfractor_interior(etInfraInfraExterior.getText().toString().toUpperCase());
            }

            if (etInfraInfraInterior.getText().toString().isEmpty() || etInfraInfraInterior.getText().toString().equals("")){
                saveData.setInfractor_exterior("");
            } else {
                saveData.setInfractor_exterior(etInfraInfraInterior.getText().toString().toUpperCase());
            }


            savecat = (Catalogs) spInfraInfraEntidad.getSelectedItem();
            saveData.setInfractor_entidad(Integer.parseInt(savecat.getId()));
            saveData.setInfractor_entidadtext(savecat.getValue().toUpperCase());

            savecat = (Catalogs) spInfraInfraDelMun.getSelectedItem();
            saveData.setInfractor_delmun(Integer.parseInt(savecat.getId()));
            saveData.setInfractor_delmuntext(savecat.getValue().toUpperCase());

            saveData.setInfractor_colonia(etInfraInfraColonia.getText().toString().toUpperCase());

            if (etInfraLicNum.getText().toString().isEmpty() || etInfraLicNum.getText().toString().equals("")){
                saveData.setTipolic_num("");
                saveData.setTipolic_tipo(0);
                saveData.setTipolic_tipotext("");
                saveData.setTipolic_expedida(0);
                saveData.setTipolic_expedidatext("");
            } else {
                saveData.setTipolic_num(etInfraLicNum.getText().toString().toUpperCase());
                savecat = (Catalogs) spInfraLicTipo.getSelectedItem();
                saveData.setTipolic_tipo(Integer.parseInt(savecat.getId()));
                saveData.setTipolic_tipotext(savecat.getValue().toUpperCase());
                savecat = (Catalogs) spInfraLicExpedida.getSelectedItem();
                saveData.setTipolic_expedida(Integer.parseInt(savecat.getId()));
                saveData.setTipolic_expedidatext(savecat.getValue().toUpperCase());
            }

            if (isUpdate){
                saveData.setIsUpdate(1);
            } else {
                saveData.setIsUpdate(0);
            }
        }

        if (!isUpdate){

            if (!isOnline){

                saveData.setId_persona_ayun(pHelper.getIdPersonaAyuntamiento());

                saveData.setImporte_tres(String.format("%.2f", sumatotal));
                saveData.setImporte_dos(String.format("%.2f", sumatotal * 0.5));
                saveData.setImporte_uno(String.format("%.2f", sumatotal * 0.3));


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat datefull = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");

                // INSERT IF (DAYS NON NATURAL)
                String currdate = datef.format(calendar.getTime());
                String fifteen = getHabiles(15);
                String thirty = getHabiles(30);

                Date date = new Date();

                saveData.setFechahora(datefull.format(date));

                saveData.setFecha_tres(thirty);
                saveData.setFecha_dos(fifteen);
                saveData.setFecha_uno(currdate);


                String lastidfull = lastinfralist.get(0).getDataone();
                String[] lastidsplit = lastidfull.split("-");
                int lastidnum = Integer.parseInt(lastidsplit[1]);
                int currid = lastidnum + 1;
                String curridfull = pHelper.getPrefix() + currid;

                saveData.setLinea_tres(getLineaCaptura(curridfull, thirty, saveData.getImporte_tres(), "3"));
                saveData.setLinea_dos(getLineaCaptura(curridfull, fifteen, saveData.getImporte_dos(), "3"));
                saveData.setLinea_uno(getLineaCaptura(curridfull, currdate, saveData.getImporte_uno(), "3"));
                saveData.setInfraFolio(pHelper.getPrefix() + "-" + currid);

                saveData.setLatitude(latitude);
                saveData.setLongitude(longitude);

            }

        }

        if (isOnline){
            saveData.setIsAusente(0);
            if (etInfraInfraNombre.getText().toString().isEmpty() || etInfraInfraNombre.getText().toString().equals("")){
                saveData.setInfractor_nombre("");
            } else {
                saveData.setInfractor_nombre(etInfraInfraNombre.getText().toString().toUpperCase());
            }

            if (etInfraInfraPaterno.getText().toString().isEmpty() || etInfraInfraPaterno.getText().toString().equals("")){
                saveData.setInfractor_paterno("");
            } else {
                saveData.setInfractor_paterno(etInfraInfraPaterno.getText().toString().toUpperCase());
            }

            if (etInfraInfraMaterno.getText().toString().isEmpty() || etInfraInfraMaterno.getText().toString().equals("")){
                saveData.setInfractor_materno("");
            } else {
                saveData.setInfractor_materno(etInfraInfraMaterno.getText().toString().toUpperCase());
            }

        }
    }

    private String getHabiles(int days) {
        String datestring;
        Calendar calendar1 = Calendar.getInstance();
        int totaldias = 0;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");

        while (totaldias != days){
            if (calendar1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar1.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY){
                String addeddate = datef.format(calendar1.getTime());
                Catalogs catalogs;
                boolean isNoHabil = false;
                for (int i= 0; i < diasnohabileslist.size(); i++){
                    catalogs = diasnohabileslist.get(i);
                    if (catalogs.getDataone().equals(addeddate)){
                        isNoHabil = true;
                    }
                }
                if (!isNoHabil){
                    totaldias++;
                }
            }
            if (totaldias != days){
                calendar1.add(Calendar.DATE, 1);
            }
        }

        datestring = datef.format(calendar1.getTime());
        return datestring;
    }

    @SuppressLint("DefaultLocale")
    private String getLineaCaptura(String idinfra, String date, String importe, String constante) {

        String lineaCaptura = null;

        String firstpart = getIdCondensed(idinfra);
        String secondpart = getDateCondensed(date);
        String thirdpart = getImporteCondensed(importe);
        String fourthpart = getVerificationNumbers(firstpart + secondpart + thirdpart + constante);

        lineaCaptura = firstpart+secondpart+thirdpart+constante+fourthpart;

        for (int i = lineaCaptura.length(); i <= 19; i++){
            lineaCaptura = "0" + lineaCaptura;
        }

        return lineaCaptura;
    }

    private String getIdCondensed(String input) {

        String idinfra = input.toUpperCase();

        String c;
        String idCondensed = "";

        for (int i=0; i < idinfra.length(); i++) {

            c = idinfra.substring(i, i + 1);

            switch (c){
                case "A":
                    idCondensed = idCondensed + "2";
                    break;
                case "B":
                    idCondensed = idCondensed + "2";
                    break;
                case "C":
                    idCondensed = idCondensed + "2";
                    break;
                case "D":
                    idCondensed = idCondensed + "3";
                    break;
                case "E":
                    idCondensed = idCondensed + "3";
                    break;
                case "F":
                    idCondensed = idCondensed + "3";
                    break;
                case "G":
                    idCondensed = idCondensed + "4";
                    break;
                case "H":
                    idCondensed = idCondensed + "4";
                    break;
                case "I":
                    idCondensed = idCondensed + "4";
                    break;
                case "J":
                    idCondensed = idCondensed + "5";
                    break;
                case "K":
                    idCondensed = idCondensed + "5";
                    break;
                case "L":
                    idCondensed = idCondensed + "5";
                    break;
                case "M":
                    idCondensed = idCondensed + "6";
                    break;
                case "N":
                    idCondensed = idCondensed + "6";
                    break;
                case "O":
                    idCondensed = idCondensed + "6";
                    break;
                case "P":
                    idCondensed = idCondensed + "7";
                    break;
                case "Q":
                    idCondensed = idCondensed + "7";
                    break;
                case "R":
                    idCondensed = idCondensed + "7";
                    break;
                case "S":
                    idCondensed = idCondensed + "8";
                    break;
                case "T":
                    idCondensed = idCondensed + "8";
                    break;
                case "U":
                    idCondensed = idCondensed + "8";
                    break;
                case "V":
                    idCondensed = idCondensed + "9";
                    break;
                case "W":
                    idCondensed = idCondensed + "9";
                    break;
                case "X":
                    idCondensed = idCondensed + "9";
                    break;
                case "Y":
                    idCondensed = idCondensed + "0";
                    break;
                case "Z":
                    idCondensed = idCondensed + "0";
                    break;
                default:
                    idCondensed = idCondensed + c;
                    break;
            }
        }
        return idCondensed;
    }

    private String getDateCondensed(String currdate) {
        String[] fechasplit = currdate.split("/");
        int dia = Integer.parseInt(fechasplit[0]);
        int mes = Integer.parseInt(fechasplit[1]);
        int ano = Integer.parseInt(fechasplit[2]);

        ano = (ano - 2013) * 372;
        mes = (mes - 1) * 31;
        dia = dia - 1;

        return String.valueOf(ano + mes + dia);
    }

    private String getImporteCondensed(String importe) {
        importe = importe.replace("0", "");
        importe = importe.replace(".", "");
        importe = importe.replace(",", "");

        int x = 0;
        int count = importe.length() - 1;
        int[] array = new int[importe.length()];
        int suma = 0;

        for (int i = 0; i < importe.length(); i++){

            int digitnum = Integer.parseInt(importe.substring(count, count + 1));

            if (x == 0) {
                digitnum = digitnum * 7;
                x = x + 1;
            } else if (x == 1) {
                digitnum = digitnum * 3;
                x = x + 1;
            } else  if (x == 2) {
                x = 0;
            }

            array[i] = digitnum;

            count--;
        }

        for (int i = 0; i < array.length; i ++){
            suma = suma + array[i];
        }

        return String.valueOf(suma % 10);
    }

    private String getVerificationNumbers(String all) {
        int x = 0;
        int count = all.length() - 1;
        int[] array = new int[all.length()];
        int suma = 0;

        for (int i = 0; i < all.length(); i++){

            int digitnum = Integer.parseInt(all.substring(count, count + 1));

            if (x == 0) {
                digitnum = digitnum * 11;
                x = x + 1;
            } else  if (x == 1) {
                digitnum = digitnum * 13;
                x = x + 1;
            } else  if (x == 2) {
                digitnum = digitnum * 17;
                x = x + 1;
            } else  if (x == 3) {
                digitnum = digitnum * 19;
                x = x + 1;
            } else if (x == 4) {
                digitnum = digitnum * 23;
                x = 0;
            }

            array[i] = digitnum;

            count--;
        }

        for (int i = 0; i < array.length; i ++){
            suma = suma + array[i];
        }

        return String.valueOf((suma % 97)+1);
    }

    private boolean isValidate() {

        boolean isValid = true;

        if (!isUpdate){
            if (rbInfraAusenteNo.isChecked()){
                //LICENCIA
                if (!etInfraLicNum.getText().toString().isEmpty()){
                    if (spInfraLicExpedida.getSelectedItemPosition() == 0){
                        spInfraLicExpedida.setBackgroundResource(R.drawable.square_bg_input_invalid);
                        isValid = false;
                    }
                    if (spInfraLicTipo.getSelectedItemPosition() == 0){
                        spInfraLicTipo.setBackgroundResource(R.drawable.square_bg_input_invalid);
                        isValid = false;
                    }
                }
                //INFRACTOR
                /*if (etInfraInfraColonia.getText().toString().isEmpty()){
                    etInfraInfraColonia.setBackgroundResource(R.drawable.square_bg_input_invalid);
                    isValid = false;
                }
                if (spInfraInfraDelMun.getSelectedItemPosition() == 0){
                    spInfraInfraDelMun.setBackgroundResource(R.drawable.square_bg_input_invalid);
                    isValid = false;
                }
                if (spInfraInfraEntidad.getSelectedItemPosition() == 0){
                    spInfraInfraEntidad.setBackgroundResource(R.drawable.square_bg_input_invalid);
                    isValid = false;
                }*/

                if (etInfraInfraMaterno.getText().toString().isEmpty()){
                    etInfraInfraMaterno.setBackgroundResource(R.drawable.square_bg_input_invalid);
                    isValid = false;
                }
                if (etInfraInfraPaterno.getText().toString().isEmpty()){
                    etInfraInfraPaterno.setBackgroundResource(R.drawable.square_bg_input_invalid);
                    isValid = false;
                }
                if (etInfraInfraNombre.getText().toString().isEmpty()){
                    etInfraInfraNombre.setBackgroundResource(R.drawable.square_bg_input_invalid);
                    isValid = false;
                }
            }

            //REMISION A DEPOSITO

           /* if (spInfraRetiene.getSelectedItemPosition() == 0){
                spInfraRetiene.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }*/

            if (etInfraMotivacion.getText().toString().isEmpty()){
                etInfraMotivacion.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (rbInfraRemSi.isChecked()){
                if (spInfraDisposicion.getSelectedItemPosition() == 0){
                    spInfraDisposicion.setBackgroundResource(R.drawable.square_bg_input_invalid);
                    isValid = false;
                }
            }

            //INFRACCION
            if (artlist.isEmpty()){
                spInfraFraccion.setBackgroundResource(R.drawable.square_bg_input_invalid);
                spInfraArticulo.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            //LUGAR INFRACCION

            if (etInfraColonia.getText().toString().isEmpty()){
                etInfraColonia.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (etInfraStreetY.getText().toString().isEmpty()){
                etInfraStreetY.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (etInfraEntre.getText().toString().isEmpty()){
                etInfraEntre.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (etInfraCalle.getText().toString().isEmpty()){
                etInfraCalle.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            //VEHICULO

            if (spInfraTipoDoc.getSelectedItemPosition() == 0){
                spInfraTipoDoc.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (spInfraExpedido.getSelectedItemPosition() == 0){
                spInfraExpedido.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (etInfraNumID.getText().toString().isEmpty()){
                etInfraNumID.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (spInfraID.getSelectedItemPosition() == 0){
                spInfraID.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (etInfraAno.getText().toString().isEmpty()){
                etInfraAno.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (spInfraColor.getSelectedItemPosition() == 0){
                spInfraColor.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (spInfraType.getSelectedItemPosition() == 0){
                spInfraType.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (spInfraSubMarca.getSelectedItemPosition() == 0 && etInfraSubMarca.getText().toString().isEmpty()){
                spInfraSubMarca.setBackgroundResource(R.drawable.square_bg_input_invalid);
                etInfraSubMarca.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (spInfraMarca.getSelectedItemPosition() == 0){
                spInfraMarca.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (saveData.getImagen_1() == null){
                Utils.showToast(getString(R.string.insert_photo), this);
                return false;
            }

            if (saveData.getImagen_2() == null){
                Utils.showToast(getString(R.string.insert_phototwo), this);
                return false;
            }

        } else {
            persona = new Persona();
            if (etInfraInfraNombre.getText().toString().isEmpty()){
                etInfraInfraNombre.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (etInfraInfraPaterno.getText().toString().isEmpty()){
                etInfraInfraPaterno.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }

            if (etInfraInfraMaterno.getText().toString().isEmpty()){
                etInfraInfraMaterno.setBackgroundResource(R.drawable.square_bg_input_invalid);
                isValid = false;
            }
        }

        if (!isValid){
            Utils.showToast(getString(R.string.invalid_input), this);
            return false;
        }

        return true;
    }

    private void resetBg(){
        //INFRACTOR AUSENTE
        spInfraLicExpedida.setBackgroundResource(R.drawable.square_bg_input);
        spInfraLicTipo.setBackgroundResource(R.drawable.square_bg_input);
        etInfraLicNum.setBackgroundResource(R.drawable.square_bg_input);
        etInfraInfraColonia.setBackgroundResource(R.drawable.square_bg_input);
        spInfraInfraDelMun.setBackgroundResource(R.drawable.square_bg_input);
        spInfraInfraEntidad.setBackgroundResource(R.drawable.square_bg_input);
        etInfraInfraInterior.setBackgroundResource(R.drawable.square_bg_input);
        etInfraInfraExterior.setBackgroundResource(R.drawable.square_bg_input);
        etInfraInfraCalle.setBackgroundResource(R.drawable.square_bg_input);
        etInfraInfraRFC.setBackgroundResource(R.drawable.square_bg_input);
        etInfraInfraMaterno.setBackgroundResource(R.drawable.square_bg_input);
        etInfraInfraPaterno.setBackgroundResource(R.drawable.square_bg_input);
        etInfraInfraNombre.setBackgroundResource(R.drawable.square_bg_input);

        //REMISION A DEPOSITO
        spInfraRetiene.setBackgroundResource(R.drawable.square_bg_input);
        etInfraMotivacion.setBackgroundResource(R.drawable.square_bg_input);
        spInfraDisposicion.setBackgroundResource(R.drawable.square_bg_input);

        //INFRACCIÓN
        spInfraFraccion.setBackgroundResource(R.drawable.square_bg_input);
        spInfraArticulo.setBackgroundResource(R.drawable.square_bg_input);

        //LUGAR DE LA INFRACCION
        etInfraColonia.setBackgroundResource(R.drawable.square_bg_input);
        etInfraStreetY.setBackgroundResource(R.drawable.square_bg_input);
        etInfraEntre.setBackgroundResource(R.drawable.square_bg_input);
        etInfraCalle.setBackgroundResource(R.drawable.square_bg_input);

        //VEHICULO
        spInfraTipoDoc.setBackgroundResource(R.drawable.square_bg_input);
        spInfraExpedido.setBackgroundResource(R.drawable.square_bg_input);
        etInfraNumID.setBackgroundResource(R.drawable.square_bg_input);
        spInfraID.setBackgroundResource(R.drawable.square_bg_input);
        etInfraAno.setBackgroundResource(R.drawable.square_bg_input);
        spInfraColor.setBackgroundResource(R.drawable.square_bg_input);
        spInfraType.setBackgroundResource(R.drawable.square_bg_input);
        etInfraSubMarca.setBackgroundResource(R.drawable.square_bg_input);
        spInfraSubMarca.setBackgroundResource(R.drawable.square_bg_input);
        spInfraMarca.setBackgroundResource(R.drawable.square_bg_input);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
                    imageUri = Uri.fromFile(photofile);
                    startActivityForResult(camera, Const.ServiceCode.CAMERA_PICTURE);

                } else {
                    Utils.showToast("No hay permisos en la camara", this);
                }
            }
        }
    }

    private void intentcam() {
        photofile = new File(Const.SD_PATH_IMG + "/tmp/", "TMP.BMP");
        photopath = new File(Const.SD_PATH_IMG + "/tmp/");
        if (!photopath.exists()) {
            photopath.mkdirs();
        }
        int coderequest = 0;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, coderequest);
        } else {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageUri = Uri.fromFile(photofile);
            camera.putExtra("output", imageUri);
            this.startActivityForResult(camera, Const.ServiceCode.CAMERA_PICTURE);
        }
    }

    private byte[] getImgByteArray(Bitmap photo) {
        bos = new ByteArrayOutputStream();
        if (photo != null) {
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        }
        ba = bos.toByteArray();
        return ba;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.ServiceCode.CAMERA_PICTURE:
                if (resultCode == RESULT_OK) {
                    Bitmap photo = null;
                    try {
                        photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap preview = Bitmap.createScaledBitmap(photo, 640, 480, false);

                    switch (selectedPhoto) {
                        case 1:
                            ivInfraCapId.setImageBitmap(preview);
                            saveData.setImagen_1(getImgByteArray(photo));
                            break;
                        case 2:
                            ivInfraCapIdtwo.setImageBitmap(preview);
                            saveData.setImagen_2(getImgByteArray(photo));
                            break;
                    }
                } else {
                    Utils.showToast("Captura cancelada", this);
                }
                break;
        }    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) throws JSONException {
        switch (serviceCode){
            case Const.ServiceCode.SPINNER_DATABASE_LOAD:
                spinnersfill();
                if (isUpdate){
                    updateDataSet();
                }
                Utils.removeCustomProgressDialog();
                break;
            case Const.ServiceCode.SAVE_COMPLETE:
                if (response.equals("true")){
                    //new SyncManual(this);
                    printSaved();
                } else {
                    Utils.removeCustomProgressDialog();
                    Utils.showToast(getString(R.string.save_fail), this);
                }
                break;
            case Const.ServiceCode.PRINT:
                if (response.equals("true")){
                        if (!isUpdate){
                            Utils.showToast("Impresión iniciada correctamente.", this);
                            Utils.removeCustomProgressDialog();
                            reloadActivity();
                        } else {
                            Utils.showToast("Impresión iniciada correctamente.", this);
                            Utils.removeCustomProgressDialog();
                            ibInfraMenuPrint.setEnabled(true);
                        }
                } else {
                    if (isUpdate){
                        Utils.showToast("Falló la impresión, ¿Tiene papel la impresora?", this);
                        Utils.removeCustomProgressDialog();
                    } else {
                        Utils.showToast("Falló la impresión, puedes reintentarlo en el menú de busqueda", this);
                        reloadActivity();
                    }
                    ibInfraMenuPrint.setEnabled(true);
                }
                break;
            case Const.ServiceCode.PRINT_TWO:
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (response.equals("true")){
                    Utils.showToast("Impresión iniciada correctamente.", this);
                    Utils.removeCustomProgressDialog();
                } else {
                    Utils.showToast("Falló la impresión, ¿Tiene papel la impresora?", this);
                    Utils.removeCustomProgressDialog();
                }

                final Dialog dialog = new Dialog(InfraccionActivity.this, R.style.DialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_exit);
                dialog.setCanceledOnTouchOutside(false);
                TextView tvCatDialTitle = dialog.findViewById(R.id.tvCatDialTitle);
                ImageButton ibCatDialSave = dialog.findViewById(R.id.ibExitDialSave);
                ibCatDialSave.setImageResource(R.drawable.ic_infra_print);
                ImageButton ibCatDialCancel = dialog.findViewById(R.id.ibExitDialCancel);
                tvCatDialTitle.setText(getString(R.string.should_print) + ":" + saveData.getInfraFolio());
                ibCatDialCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isUpdate){
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            reloadActivity();
                        }

                    }
                });
                ibCatDialSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.showCustomProgressDialog(InfraccionActivity.this, false, null);
                        if(isUpdate){
                            rbInfraRemNo.setChecked(true);
                        }
                        new PrintInfraNew(InfraccionActivity.this, saveData, Const.ServiceCode.PRINT, InfraccionActivity.this);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }

    public void printSaved(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        EditText etCusDialServ = dialog.findViewById(R.id.etCusDialServ);
        etCusDialServ.setVisibility(View.GONE);

        TextView tvCusDialMessage = dialog.findViewById(R.id.tvCusDialMessage);
        tvCusDialMessage.setText("Infracción guardada correctamente.");
        TextView tvCusDialMessageTwo = dialog.findViewById(R.id.tvCusDialMessageTwo);
        tvCusDialMessageTwo.setText("Se imprimirá la boleta.");
        Button btnCusDialOK = dialog.findViewById(R.id.btnCusDialOK);
        ImageButton ibCusDialCancel = dialog.findViewById(R.id.ibCusDialCancel);
        ibCusDialCancel.setVisibility(View.GONE);

        btnCusDialOK.setText(context.getResources().getString(R.string.ok));
        btnCusDialOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showCustomProgressDialog(InfraccionActivity.this, false,null);
                new PrintInfraNew(InfraccionActivity.this, saveData, Const.ServiceCode.PRINT_TWO, InfraccionActivity.this);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void reloadActivity() {
        Intent i = new Intent().setClass(InfraccionActivity.this, InfraccionActivity.class);
        i.putExtra("idInfraccion", "0");
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void spinnersfill() {

        final ArrayList<Catalogs> emptylist = new ArrayList<>();

        final Catalogs empty = new Catalogs();
        empty.setId("0");
        empty.setUnion("0");
        empty.setValue("Selecciona un valor...");

        emptylist.add(empty);

        marcaadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,marcalist);
        marcaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraMarca.setAdapter(marcaadapter);
        spInfraMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    ArrayList<Catalogs> submarcalistok = new ArrayList<>();
                    submarcalistok.add(0, empty);
                    Catalogs c = (Catalogs) adapterView.getSelectedItem();
                    String union = c.getId();
                    for (int sm = 0; sm <  submarcalist.size(); sm++){
                        Catalogs submarca = submarcalist.get(sm);
                        if (submarca.getUnion().equals(union)){
                            submarcalistok.add(submarca);
                        }
                    }
                    submarcaadapter = new ArrayAdapter<>(InfraccionActivity.this, android.R.layout.simple_spinner_item,submarcalistok);
                    submarcaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    submarcaadapter.notifyDataSetChanged();
                    spInfraSubMarca.setAdapter(submarcaadapter);
                    if (isUpdate){
                        spInfraSubMarca.setSelection(getIndex(spInfraSubMarca.getAdapter(), saveData.getSubmarca()));
                    }
                } else {
                    submarcaadapter = new ArrayAdapter<>(InfraccionActivity.this, android.R.layout.simple_spinner_item,emptylist);
                    submarcaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spInfraSubMarca.setAdapter(submarcaadapter);
                    spInfraSubMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0 && spInfraMarca.getSelectedItemPosition() != 0){
                                llInfraSubmarcaOtra.setVisibility(View.VISIBLE);
                            } else {
                                llInfraSubmarcaOtra.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        tipoadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,tipolist);
        tipoadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraType.setAdapter(tipoadapter);

        coloradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,colorlist);
        coloradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraColor.setAdapter(coloradapter);

        idadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,idlist);
        idadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraID.setAdapter(idadapter);

        estadoadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,estadolist);
        estadoadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraExpedido.setAdapter(estadoadapter);
        spInfraInfraEntidad.setAdapter(estadoadapter);
        spInfraInfraEntidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    ArrayList<Catalogs> municipiook = new ArrayList<>();
                    municipiook.add(0, empty);
                    Catalogs c = (Catalogs) adapterView.getSelectedItem();
                    String union = c.getId();
                    for (int mu = 0; mu <  municipiolist.size(); mu++){
                        Catalogs municipio = municipiolist.get(mu);
                        if (municipio.getUnion().equals(union)){
                            municipiook.add(municipio);
                        }
                    }
                    municipioadapter = new ArrayAdapter<>(InfraccionActivity.this, android.R.layout.simple_spinner_item,municipiook);
                    municipioadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    municipioadapter.notifyDataSetChanged();
                    spInfraInfraDelMun.setAdapter(municipioadapter);
                    if (isUpdate){
                        spInfraInfraDelMun.setSelection(getIndex(spInfraInfraDelMun.getAdapter(), saveData.getInfractor_delmun()));
                    }
                } else {
                    municipioadapter = new ArrayAdapter<>(InfraccionActivity.this, android.R.layout.simple_spinner_item,emptylist);
                    municipioadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spInfraInfraDelMun.setAdapter(municipioadapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spInfraLicExpedida.setAdapter(estadoadapter);

        tipodocadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,tipodoclist);
        tipodocadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraTipoDoc.setAdapter(tipodocadapter);

        articuloadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,articulolist);
        articuloadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraArticulo.setAdapter(articuloadapter);
        spInfraArticulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    ArrayList<Catalogs> fraccionok = new ArrayList<>();
                    fraccionok.add(0, empty);
                    Catalogs c = (Catalogs) adapterView.getSelectedItem();
                    String union = c.getId();
                    for (int fl = 0; fl <  fraccionlist.size(); fl++){
                        Catalogs fraccion = fraccionlist.get(fl);
                        if (fraccion.getUnion().equals(union)){
                            fraccionok.add(fraccion);
                        }
                    }
                    fraccionadapter = new ArrayAdapter<>(InfraccionActivity.this, android.R.layout.simple_spinner_item,fraccionok);
                    fraccionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fraccionadapter.notifyDataSetChanged();
                    spInfraFraccion.setAdapter(fraccionadapter);
                } else {
                    fraccionadapter = new ArrayAdapter<>(InfraccionActivity.this, android.R.layout.simple_spinner_item,emptylist);
                    fraccionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spInfraFraccion.setAdapter(fraccionadapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        disposicionadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,disposicionlist);
        disposicionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraDisposicion.setAdapter(disposicionadapter);

        seretieneadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,seretienelist);
        seretieneadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraRetiene.setAdapter(seretieneadapter);

        tipolicadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,tipoliclist);
        tipolicadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfraLicTipo.setAdapter(tipolicadapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            switch (compoundButton.getId()){
                case R.id.rbInfraAusenteSi:
                    if (!isUpdate){
                        llInfraInfraAusente.setVisibility(View.GONE);
                    }
                    rbInfraAusenteNo.setChecked(false);
                    break;
                case R.id.rbInfraAusenteNo:
                    llInfraInfraAusente.setVisibility(View.VISIBLE);
                    rbInfraAusenteSi.setChecked(false);
                    svInfraMain.smoothScrollTo(0, llInfraInfraAusente.getTop());
                    break;
                case R.id.rbInfraRemSi:
                    llInfraDisposicion.setVisibility(View.VISIBLE);
                    rbInfraRemNo.setChecked(false);
                    svInfraMain.smoothScrollTo(0, llInfraDisposicion.getTop());
                    break;
                case R.id.rbInfraRemNo:
                    llInfraDisposicion.setVisibility(View.GONE);
                    rbInfraRemSi.setChecked(false);
                    break;
            }
        }
    }
}
