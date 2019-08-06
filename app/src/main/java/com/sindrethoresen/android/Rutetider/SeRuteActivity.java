package com.sindrethoresen.android.Rutetider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by st97m_000 on 22.07.2017.
 */

public class SeRuteActivity extends AppCompatActivity implements Serializable {

    TextView title;
    String intentStasjon;
    String intentDag;
    TilkobletLinje mor;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    LinearLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_se_rute);
        //getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        intentStasjon = getIntent().getExtras().getString("stasjon");
        intentDag = getIntent().getExtras().getString("dag");
        title = (TextView) findViewById(R.id.txtTittel);
        mor= (TilkobletLinje) getIntent().getSerializableExtra("TilkobletLinje");
        title.setText(mor.fullRuteID + " " + mor.fullRuteNavn);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);


        adapter = new SeRuteAdapter(mor, this, intentStasjon);
        rv.setAdapter(adapter);

        for(int i=0; i<mor.stasjonsFrekvens.length; i++){
            if(mor.stasjonsFrekvens[i].navn.equals(intentStasjon)){
                manager.scrollToPosition(i);
                break;
            }
        }

    }

    public void ruteTabell(View v){
        Intent intent = new Intent(this, RuteTiderActivity.class);
        intent.putExtra("linje", mor.fullRuteID + " " + mor.fullRuteNavn);
        //finner neste dato som stemmer med dagen
        Calendar c = Calendar.getInstance();
        String iterDag;
        int dagKode = c.get(Calendar.DAY_OF_WEEK);
        switch (dagKode) { //init før for loop, i tilfelle det er det spesifiserte dagen
            case Calendar.MONDAY:
                iterDag=getString(R.string.ma);
                break;

            case Calendar.TUESDAY:
                iterDag=getString(R.string.ti);;
                break;

            case Calendar.WEDNESDAY:
                iterDag=getString(R.string.on);
                break;

            case Calendar.THURSDAY:
                iterDag=getString(R.string.to);
                break;

            case Calendar.FRIDAY:
                iterDag=getString(R.string.fr);
                break;

            case Calendar.SATURDAY:
                iterDag=getString(R.string.lø);
                //Log.e("flyt", "hit");
                break;

            case Calendar.SUNDAY:
                iterDag=getString(R.string.sø);
                break;

            default :
                iterDag=null;
                Log.e("kritisk error", "unexcpected in SeRuteActivity");
        }


        Log.e("flyt", "risk start if 'e test'");
        while(!intentDag.equals(iterDag)){
            Log.e("e test", intentDag + "-" + iterDag);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DAY_OF_YEAR, 1);
            dagKode = c.get(Calendar.DAY_OF_WEEK);
            switch (dagKode) {
                case Calendar.MONDAY:
                    iterDag=getString(R.string.ma);
                    break;

                case Calendar.TUESDAY:
                    iterDag=getString(R.string.ti);;
                    break;

                case Calendar.WEDNESDAY:
                    iterDag=getString(R.string.on);
                    break;

                case Calendar.THURSDAY:
                    iterDag=getString(R.string.to);
                    break;

                case Calendar.FRIDAY:
                    iterDag=getString(R.string.fr);
                    break;

                case Calendar.SATURDAY:
                    iterDag=getString(R.string.lø);
                    break;

                case Calendar.SUNDAY:
                    iterDag=getString(R.string.sø);
                    break;
            }
        }
        Log.e("flyt", "risk (if exists) end");
        intent.putExtra("dato", c.getTime());
        startActivity(intent);
    }


}
