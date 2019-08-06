package com.sindrethoresen.android.Rutetider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.widget.TimePicker;
import android.widget.Toast;

public class SokerActivity extends AppCompatActivity {

    private AutoCompleteTextView txtSearch;
    private CheckBox checkNå;
    private CheckBox checkVelgTid;
    private TextView txtShowTid;
    private TextView txtShowDato;

    private Toast lastToast;

    private CheckBox checkIDag;
    private CheckBox checkIMorgen;
    private CheckBox checkVelgDato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soker);

        checkNå = (CheckBox) findViewById(R.id.checkNå);
        checkVelgTid = (CheckBox) findViewById(R.id.checkVelgTid);

        txtShowTid = (TextView) findViewById(R.id.txtshowtid);
        txtShowDato = (TextView) findViewById(R.id.txtshowdato);
        checkIDag = (CheckBox) findViewById(R.id.cbidag);
        checkIMorgen = (CheckBox) findViewById(R.id.cbimorgen);
        checkVelgDato = (CheckBox) findViewById(R.id.cbvelgdato);

        Calendar showinit = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(showinit.getTime());
        txtShowDato.setText(formattedDate);



        final TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                txtShowTid.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        };

        final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d){
                txtShowDato.setText(String.format("%02d/%02d/%04d", d, m+1 ,y));
            }
        };

        checkNå.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVelgTid.setChecked(false);
                checkNå.setChecked(true);
                txtShowTid.setVisibility(View.INVISIBLE);
            }
        });

        checkVelgTid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNå.setChecked(false);
                checkVelgTid.setChecked(true);
                txtShowTid.setVisibility(View.VISIBLE);
                if("00:00".equals(txtShowTid.getText().toString())) txtShowTid.setText(GlobaleMetoder.getSystemTime()); //init
                //launch selector
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(SokerActivity.this, t, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });


        checkIDag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIMorgen.setChecked(false);
                checkVelgDato.setChecked(false);
                checkIDag.setChecked(true);
                txtShowDato.setVisibility(View.INVISIBLE);
            }
        });

        checkIMorgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIMorgen.setChecked(true);
                checkVelgDato.setChecked(false);
                checkIDag.setChecked(false);
                txtShowDato.setVisibility(View.INVISIBLE);
            }
        });

        checkVelgDato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIDag.setChecked(false);
                checkIMorgen.setChecked(false);
                checkVelgDato.setChecked(true);
                txtShowDato.setVisibility(View.VISIBLE);
                //launch selector
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(SokerActivity.this, d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                Date[] minMax = GlobaleMetoder.minMax(SokerActivity.this);
                //dpd.getDatePicker().setMinDate(minMax[0].getTime());
                //dpd.getDatePicker().setMaxDate(minMax[1].getTime());
                dpd.show();
            }
        });



        if(GlobaleMetoder.getStopArray()==null) GlobaleMetoder.lagStopData(this);
        if(GlobaleMetoder.getRuteArray()==null) GlobaleMetoder.lagRuteData(this);

        txtSearch = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, GlobaleMetoder.getStopArray());
        txtSearch.setThreshold(2);
        txtSearch.setHint("Søk etter en stasjon...");
        txtSearch.setAdapter(adapter);
        txtSearch.setText("");
    }

    public void finnAvgang(View v){
        String typed = txtSearch.getText().toString();
        boolean finnes = false;
        for(String s : GlobaleMetoder.getStopArray()){
            if(s.equalsIgnoreCase(typed)){
                finnes=true;
                break;
            }
        }


        Calendar calendar = Calendar.getInstance();
        Date date = null;

        if(checkIDag.isChecked()){
            date = calendar.getTime();
        }else if(checkIMorgen.isChecked()){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            date = calendar.getTime();
        }else{
            //custom dag
            String txtTid = txtShowDato.getText().toString();
            String[] ar = txtTid.split("/");
            calendar.set(Integer.parseInt(ar[2]), Integer.parseInt(ar[1])-1, Integer.parseInt(ar[0]));
            date = calendar.getTime();
        }

        if(finnes) {

            Intent intent = new Intent(SokerActivity.this, ResultsActivity.class);
            intent.putExtra("stasjon", txtSearch.getText().toString());
            intent.putExtra("dato", date);
            if (checkVelgTid.isChecked()) {
                intent.putExtra("tid", txtShowTid.getText().toString());
            } else {
                intent.putExtra("tid", GlobaleMetoder.getSystemTime());
            }
            startActivity(intent);
        }else{
            //brukeren har skrevet inn et ugyldig navn
            Context context = this;
            CharSequence text = "Ugyldig stoppested. Velg forslag fra drop-down lista";
            int duration = Toast.LENGTH_SHORT;

            if(lastToast!=null) lastToast.cancel();
            lastToast = Toast.makeText(context, text, duration);
            lastToast.show();
        }

    }

}




