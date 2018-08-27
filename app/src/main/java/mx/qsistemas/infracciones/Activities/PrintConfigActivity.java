package mx.qsistemas.infracciones.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.InfraBase;
import mx.qsistemas.infracciones.R;
import mx.qsistemas.infracciones.Utils.Utils;

public class PrintConfigActivity extends InfraBase {

    public EditText etPrintHeader1, etPrintHeader2, etPrintHeader3, etPrintHeader4, etPrintHeader5, etPrintHeader6, etPrintFooter1, etPrintFooter2, etPrintFooter3;
    public ImageButton ibPrintMenuCancel, ibPrintMenuSave;
    public PreferenceHelper pHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_config);
        setBarColor("#000000");
        setCenterText(getString(R.string.print_config), Color.WHITE);

        pHelper = new PreferenceHelper(this);

        etPrintHeader1 = (EditText) findViewById(R.id.etPrintHeader1);
        etPrintHeader2 = (EditText) findViewById(R.id.etPrintHeader2);
        etPrintHeader3 = (EditText) findViewById(R.id.etPrintHeader3);
        etPrintHeader4 = (EditText) findViewById(R.id.etPrintHeader4);
        etPrintHeader5 = (EditText) findViewById(R.id.etPrintHeader5);
        etPrintHeader6 = (EditText) findViewById(R.id.etPrintHeader6);
        etPrintFooter1 = (EditText) findViewById(R.id.etPrintFooter1);
        etPrintFooter2 = (EditText) findViewById(R.id.etPrintFooter2);
        etPrintFooter3 = (EditText) findViewById(R.id.etPrintFooter3);

        ibPrintMenuCancel = (ImageButton) findViewById(R.id.ibPrintMenuCancel);
        ibPrintMenuSave = (ImageButton) findViewById(R.id.ibPrintMenuSave);

        ibPrintMenuCancel.setOnClickListener(this);
        ibPrintMenuSave.setOnClickListener(this);
        fillet();
    }

    private void fillet() {
        if (pHelper.getHeader1() != null && !pHelper.getHeader1().equals("")){
            etPrintHeader1.setText(pHelper.getHeader1());
        }
        if (pHelper.getHeader2() != null && !pHelper.getHeader2().equals("")){
            etPrintHeader2.setText(pHelper.getHeader2());
        }
        if (pHelper.getHeader3() != null && !pHelper.getHeader3().equals("")){
            etPrintHeader3.setText(pHelper.getHeader3());
        }
        if (pHelper.getHeader4() != null && !pHelper.getHeader4().equals("")){
            etPrintHeader4.setText(pHelper.getHeader4());
        }
        if (pHelper.getHeader5() != null && !pHelper.getHeader5().equals("")){
            etPrintHeader5.setText(pHelper.getHeader5());
        }
        if (pHelper.getHeader6() != null && !pHelper.getHeader6().equals("")){
            etPrintHeader6.setText(pHelper.getHeader6());
        }
        if (pHelper.getFooter1() != null && !pHelper.getFooter1().equals("")){
            etPrintFooter1.setText(pHelper.getFooter1());
        }
        if (pHelper.getFooter2() != null && !pHelper.getFooter2().equals("")){
            etPrintFooter2.setText(pHelper.getFooter2());
        }
        if (pHelper.getFooter3() != null && !pHelper.getFooter3().equals("")){
            etPrintFooter3.setText(pHelper.getFooter3());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibPrintMenuCancel:
                onBackPressed();
                break;
            case R.id.ibPrintMenuSave:
                if (isValidate()){
                    pHelper.putHeader1(etPrintHeader1.getText().toString());
                    pHelper.putHeader2(etPrintHeader2.getText().toString());
                    pHelper.putHeader3(etPrintHeader3.getText().toString());
                    pHelper.putHeader4(etPrintHeader4.getText().toString());
                    pHelper.putHeader5(etPrintHeader5.getText().toString());
                    pHelper.putHeader6(etPrintHeader6.getText().toString());
                    pHelper.putFooter1(etPrintFooter1.getText().toString());
                    pHelper.putFooter2(etPrintFooter2.getText().toString());
                    pHelper.putFooter3(etPrintFooter3.getText().toString());
                    Utils.showToast(getString(R.string.saved_changes), this);
                } else {
                    Utils.showToast(getString(R.string.insert_a_value), this);
                }
                break;
        }
    }

    private boolean isValidate() {
        int h = 0;
        int f = 0;

        if (!etPrintHeader1.getText().toString().equals("") || !etPrintHeader1.getText().toString().isEmpty()){
            h = 1;
        }
        if (!etPrintHeader2.getText().toString().equals("") || !etPrintHeader2.getText().toString().isEmpty()){
            h = 1;
        }
        if (!etPrintHeader3.getText().toString().equals("") || !etPrintHeader3.getText().toString().isEmpty()){
            h = 1;
        }
        if (!etPrintHeader4.getText().toString().equals("") || !etPrintHeader4.getText().toString().isEmpty()){
            h = 1;
        }
        if (!etPrintHeader5.getText().toString().equals("") || !etPrintHeader5.getText().toString().isEmpty()){
            h = 1;
        }
        if (!etPrintHeader6.getText().toString().equals("") || !etPrintHeader6.getText().toString().isEmpty()){
            h = 1;
        }
        if (!etPrintFooter1.getText().toString().equals("") || !etPrintFooter1.getText().toString().isEmpty()){
            f = 1;
        }
        if (!etPrintFooter2.getText().toString().equals("") || !etPrintFooter2.getText().toString().isEmpty()){
            f = 1;
        }
        if (!etPrintFooter3.getText().toString().equals("") || !etPrintFooter3.getText().toString().isEmpty()){
            f = 1;
        }

        return !(h == 0 || f == 0);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) throws JSONException {

    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(PrintConfigActivity.this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_exit);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvCatDialTitle = (TextView) dialog.findViewById(R.id.tvCatDialTitle);
        ImageButton ibCatDialSave = (ImageButton) dialog.findViewById(R.id.ibExitDialSave);
        ImageButton ibCatDialCancel = (ImageButton) dialog.findViewById(R.id.ibExitDialCancel);
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
                Intent i = new Intent().setClass(PrintConfigActivity.this, MainMenu.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dialog.dismiss();
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        dialog.show();
    }
}
