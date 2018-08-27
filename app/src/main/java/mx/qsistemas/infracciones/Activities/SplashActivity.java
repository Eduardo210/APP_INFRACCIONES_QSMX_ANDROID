package mx.qsistemas.infracciones.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.basewin.services.ServiceManager;

import java.util.Timer;
import java.util.TimerTask;

import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000;
    public PreferenceHelper pHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ServiceManager.getInstence().init(getApplicationContext());
        pHelper = new PreferenceHelper(this);

        TimerTask task = new TimerTask(){

            @Override
            public void run() {

                /*if (pHelper.getIdPersonaAyuntamiento() == null){
                    Intent i = new Intent().setClass(SplashActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    Intent i = new Intent().setClass(SplashActivity.this, MainMenu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }*/


                Intent i = new Intent().setClass(SplashActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();


            }

        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_DELAY);


    }
}
