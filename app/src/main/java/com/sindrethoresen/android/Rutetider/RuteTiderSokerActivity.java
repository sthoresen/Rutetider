package com.sindrethoresen.android.Rutetider;

import android.content.Intent;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;

/**
 * Created by st97m_000 on 30.07.2017.
 */

public class RuteTiderSokerActivity extends AppCompatActivity {

    private AutoCompleteTextView txtSearch;
    private CheckBox cidag;
    private CheckBox cimorgen;
    private CheckBox cvelgdato;
    private TextView txtshowdato;
    private Toast lastToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutetidersoker);

        txtSearch = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        cidag= (CheckBox) findViewById(R.id.idag);
        cimorgen= (CheckBox) findViewById(R.id.imorgen);
        cvelgdato= (CheckBox) findViewById(R.id.velgdato);
        txtshowdato = (TextView) findViewById(R.id.txtshowdato);

        if(GlobaleMetoder.getStopArray()==null) GlobaleMetoder.lagStopData(this);
        if(GlobaleMetoder.getRuteArray()==null) GlobaleMetoder.lagRuteData(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, GlobaleMetoder.getRuteArray());

        txtSearch.setHint("Søk etter en linje...");
        txtSearch.setAdapter(adapter);
        txtSearch.setText("");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        txtshowdato.setText(formattedDate);

        final DatePickerDialog.OnDateSetListener t = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d){
                txtshowdato.setText(String.format("%02d/%02d/%04d", d, m+1 ,y));
            }
        };

        cvelgdato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cidag.setChecked(false);
                cimorgen.setChecked(false);
                cvelgdato.setChecked(true);
                txtshowdato.setVisibility(View.VISIBLE);
                //launch selector
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(RuteTiderSokerActivity.this, t, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                Date[] minMax = GlobaleMetoder.minMax(RuteTiderSokerActivity.this);
                //dpd.getDatePicker().setMinDate(minMax[0].getTime());
                //dpd.getDatePicker().setMaxDate(minMax[1].getTime());
                dpd.show();
            }
        });


    }

    public void finnRuteTid(View v){
        String typed = txtSearch.getText().toString();
        boolean finnes = false;
        for(String s : GlobaleMetoder.getRuteArray()){
            if(s.equalsIgnoreCase(typed)){
                finnes=true;
                break;
            }
        }
        Calendar calendar = Calendar.getInstance();
        Date date = null;

        if(cidag.isChecked()){
            date = calendar.getTime();
        }else if(cimorgen.isChecked()){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            date = calendar.getTime();
        }else{
            //custom dag
            String txtTid = txtshowdato.getText().toString();
            String[] ar = txtTid.split("/");
            calendar.set(Integer.parseInt(ar[2]), Integer.parseInt(ar[1])-1, Integer.parseInt(ar[0]));
            date = calendar.getTime();
        }

        if(finnes) {
            Intent intent = new Intent(this, RuteTiderActivity.class);
            intent.putExtra("linje", txtSearch.getText().toString());
            intent.putExtra("dato", date);
            startActivity(intent);
        }else{
            //brukeren har skrevet inn et ugyldig navn
            CharSequence text = "Ugyldig linje. Velg forslag fra drop-down lista";
            int duration = Toast.LENGTH_LONG;
            if(lastToast!=null) lastToast.cancel();
            lastToast = Toast.makeText(this, text, duration);
            lastToast.show();
        }
    }

    public void checkTrykk(View v){

        if(v.getId()==R.id.idag || v.getId()==R.id.txtidag ){
            cimorgen.setChecked(false);
            cidag.setChecked(true);
            cvelgdato.setChecked(false);
            txtshowdato.setVisibility(View.INVISIBLE);
        }else {
            cimorgen.setChecked(true);
            cidag.setChecked(false);
            cvelgdato.setChecked(false);
            txtshowdato.setVisibility(View.INVISIBLE);
        }
    }


}
