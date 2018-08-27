package mx.qsistemas.infracciones;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Developed by ingmtz on 10/17/16.
 */

abstract public class InfraBase extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener{

    public ActionBar abBaseActionBar;
    public ImageView ivBaseLeft, ivBaseRight, ivBaseCenter;
    public TextView tvBaseLeft, tvBaseRight, tvBaseCenter;
    public LinearLayout llBaseBg, llBaseLeft, llBaseRight, llBaseCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        abBaseActionBar = getSupportActionBar();
        abBaseActionBar.setDisplayShowHomeEnabled(false);
        abBaseActionBar.setDisplayShowCustomEnabled(true);
        abBaseActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = (LayoutInflater) abBaseActionBar.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customABView = inflater.inflate(R.layout.custom_actionbar, null);

        ivBaseLeft = (ImageView) customABView.findViewById(R.id.ivBaseLeft);
        ivBaseLeft.setVisibility(View.GONE);
        ivBaseRight = (ImageView) customABView.findViewById(R.id.ivBaseRight);
        ivBaseRight.setVisibility(View.GONE);
        ivBaseCenter = (ImageView) customABView.findViewById(R.id.ivBaseCenter);
        ivBaseCenter.setVisibility(View.GONE);

        tvBaseLeft = (TextView) customABView.findViewById(R.id.tvBaseLeft);
        tvBaseLeft.setVisibility(View.GONE);
        tvBaseRight = (TextView) customABView.findViewById(R.id.tvBaseRight);
        tvBaseRight.setVisibility(View.GONE);
        tvBaseCenter = (TextView) customABView.findViewById(R.id.tvBaseCenter);
        tvBaseCenter.setVisibility(View.GONE);

        llBaseBg = (LinearLayout) customABView.findViewById(R.id.llBaseBg);
        llBaseLeft = (LinearLayout) customABView.findViewById(R.id.llBaseLeft);
        llBaseLeft.setClickable(true);
        llBaseLeft.setOnClickListener(this);
        llBaseRight = (LinearLayout) customABView.findViewById(R.id.llBaseRight);
        llBaseRight.setClickable(true);
        llBaseRight.setOnClickListener(this);
        llBaseCenter = (LinearLayout) customABView.findViewById(R.id.llBaseCenter);
        llBaseCenter.setClickable(true);
        llBaseCenter.setOnClickListener(this);

        try {
            abBaseActionBar.setCustomView(customABView);
            Toolbar parent = (Toolbar) customABView.getParent();
            parent.setContentInsetsAbsolute(0,0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setLeftImage(int img){
        ivBaseLeft.setVisibility(View.VISIBLE);
        ivBaseLeft.setImageResource(img);
    }

    public void setRightImage(int img){
        ivBaseRight.setVisibility(View.VISIBLE);
        ivBaseRight.setImageResource(img);
    }

    public void setCenterImage(int img){
        ivBaseCenter.setVisibility(View.VISIBLE);
        ivBaseCenter.setImageResource(img);
    }

    public void setLeftText (String text, int color) {
        tvBaseLeft.setVisibility(View.VISIBLE);
        tvBaseLeft.setText(text);
    }

    public void setRightText (String text, int color) {
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText(text);
    }

    public void setCenterText (String text, int color) {
        tvBaseCenter.setVisibility(View.VISIBLE);
        tvBaseCenter.setTextColor(color);
        tvBaseCenter.setText(text);
    }

    public void setBarColor(String color){
        llBaseBg.setBackgroundColor(Color.parseColor(color));
        abBaseActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
    }
}
