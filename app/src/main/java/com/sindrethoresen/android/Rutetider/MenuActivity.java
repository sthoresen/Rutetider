package com.sindrethoresen.android.Rutetider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by st97m_000 on 22.07.2017.
 */

public class MenuActivity extends AppCompatActivity {

    //private ConstraintLayout mainContainer;
    private Toast lastToast;
    private TextView txtGyldig;
    private TextView txtUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        new Thread(){
            @Override
            public void run(){
                HeavyDuty heavy = new HeavyDuty();
                heavy.execute();
            }
        }.start();

        txtGyldig = (TextView) findViewById(R.id.txtgyldig);
        txtUpdate = (TextView) findViewById(R.id.txtlastupdate);

        Date[] range = GlobaleMetoder.minMax(this);
        Calendar c = Calendar.getInstance();
        c.setTime(range[0]);
        String startDato = " " + String.format("%02d.%02d.%04d", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1 ,c.get(Calendar.YEAR)) + " ";
        c.setTime(range[1]);
        String endDato = " " + String.format("%02d.%02d.%04d", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1 ,c.get(Calendar.YEAR));

        VirituellFil getUpdate = GlobaleMetoder.hentFil("FINAL_UPDATE_DATE.txt", this);
        String updateDato = " " + getUpdate.nextLine();

        txtGyldig.setText(getString(R.string.rutetideneerg) + endDato);
        txtUpdate.setText(getString(R.string.sisteoppdatering));

    }

    public void launchSøker(View v){
        Intent intent = new Intent(MenuActivity.this, SokerActivity.class);
        intent.putExtra("funksjon", "stopp modus");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.e("kritisk", "klarte ikke å starte søkerActivity");
            e.printStackTrace();
        }
    }

    public void launchRutetabeller(View v){
        Intent intent = new Intent(MenuActivity.this, RuteTiderSokerActivity.class);
        //try {
            startActivity(intent);
        //} catch (Exception e) {
            //Log.e("kritisk", "klarte ikke å starte søkerActivity");
          //  e.printStackTrace();
        //}
    }

    public void launchInfo(View v){
        startActivity(new Intent(MenuActivity.this, InfoActivity.class));
    }

    public void launchFavoritter(View v){
        startActivity(new Intent(MenuActivity.this, FavorittActivity.class));
    }

    public void launchGPS(View v){
        CharSequence text = "Kommer snart...";
        int duration = Toast.LENGTH_SHORT;
        if(lastToast!=null) lastToast.cancel();
        lastToast = Toast.makeText(this, text, duration);
        lastToast.show();
    }


    private class HeavyDuty extends AsyncTask<String, String, Object> {

        @Override
        protected Object doInBackground(String... params) {
            if(GlobaleMetoder.getStopArray()==null) GlobaleMetoder.lagStopData(MenuActivity.this);
            if(GlobaleMetoder.getRuteArray()==null) GlobaleMetoder.lagRuteData(MenuActivity.this);
            Log.e("flyt", "arrayer ferdig");
            return null;
        }

    }


}