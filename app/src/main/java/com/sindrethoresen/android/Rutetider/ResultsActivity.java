package com.sindrethoresen.android.Rutetider;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by st97m_000 on 22.07.2017. f
 */

public class ResultsActivity extends AppCompatActivity {

    TextView title;
    TextView txtHeaderDagTid;
    String intentStasjon;
    String intentTid;
    Tid realTid;
    Date dato;
    String dag;
    TilkobletLinjeSingel[] data;
    private RecyclerView rv;
    RecyclerView.Adapter adapter;
    HeavyDuty heavy;
    LinearLayoutManager manager;
    LinearLayout skjerm;

    Toast lastToast;
    private SharedPreferences mSharedPreferences;
    boolean favorittMerket;
    boolean merketInngang;

    RelativeLayout toppen;
    ImageView favoritt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_testing);

        intentStasjon = getIntent().getExtras().getString("stasjon");
        intentTid = getIntent().getExtras().getString("tid");
        realTid= new Tid(intentTid);
        dato = (Date) getIntent().getExtras().getSerializable("dato");
        title = (TextView) findViewById(R.id.txtTittel);
        title.setText(intentStasjon);


        Calendar c = Calendar.getInstance();
        c.setTime(dato);
        int day = c.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                dag=getString(R.string.ma);
                break;

            case Calendar.TUESDAY:
                dag=getString(R.string.ti);
                break;

            case Calendar.WEDNESDAY:
                dag=getString(R.string.on);
                break;

            case Calendar.THURSDAY:
                dag=getString(R.string.to);
                break;

            case Calendar.FRIDAY:
                dag=getString(R.string.fr);
                break;

            case Calendar.SATURDAY:
                dag=getString(R.string.lø);
                break;

            case Calendar.SUNDAY:
                dag=getString(R.string.sø);
                break;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());

        txtHeaderDagTid = (TextView) findViewById(R.id.txtHeaderDagTid);
        txtHeaderDagTid.setText(intentTid + " " + dag + " " + formattedDate);
        skjerm = (LinearLayout) findViewById(R.id.skjerm);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        favoritt = (ImageView) findViewById(R.id.favoritt);

        //set riktig fylt/ikke fylt
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        favorittMerket = mSharedPreferences.contains("Stopp," + intentStasjon + ",dag," + dag );
        merketInngang=favorittMerket;
        if(merketInngang) favoritt.setImageResource(R.drawable.star100);

        toppen = (RelativeLayout) findViewById(R.id.toppen);

        toppen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data!=null) {
                    if (favorittMerket) {
                        favorittMerket = !favorittMerket;
                        favoritt.setImageResource(R.drawable.add_to_favorite);

                        CharSequence text = "Fjernet fra favoritter";
                        if (lastToast != null) lastToast.cancel();
                        int duration = Toast.LENGTH_SHORT;
                        lastToast = Toast.makeText(ResultsActivity.this, text, duration);
                        lastToast.show();
                    } else {
                        favorittMerket = !favorittMerket;
                        favoritt.setImageResource(R.drawable.star100);
                        CharSequence text = "La til i favoritter";
                        if (lastToast != null) lastToast.cancel();
                        int duration = Toast.LENGTH_SHORT;
                        lastToast = Toast.makeText(ResultsActivity.this, text, duration);
                        lastToast.show();

                    }
                }
            }
        });


        new Thread(){
            @Override
            public void run(){
                Log.e("flyt", "heavy call");
                heavy = new HeavyDuty();
                heavy.execute(intentStasjon, intentTid);
            }
        }.start();

    }


    @Override
    protected  void onPause(){
        super.onPause();

        if(merketInngang!=favorittMerket){
            if(merketInngang){
                mSharedPreferences.edit().remove("Stopp," + intentStasjon  + ",dag," + dag).apply();
            }else{
                mSharedPreferences.edit().putString("Stopp," + intentStasjon  + ",dag," + dag  , "0").apply();
            }

        }

    }

    public void crash(){
        rv.setVisibility(View.GONE);
        TextView tom = new TextView(this);
        tom.setText(getString(R.string.galt));
        tom.setTypeface(null, Typeface.ITALIC);
        tom.setTextSize(30);
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int int40 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        params.setMargins(int40,(int40+int40),int40,0);
        //params.addRule(RelativeLayout.BELOW, txtHeaderDag.getId());
        //relo.addView(tom, params);
        skjerm.addView(tom, params);
    }

    public void ingenTreff(){
        rv.setVisibility(View.GONE);
        TextView tom = new TextView(this);
        tom.setText(R.string.ingenreiser);
        tom.setTypeface(null, Typeface.ITALIC);
        tom.setTextSize(30);
        tom.setTextColor(Color.BLACK);
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int int40 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        params.setMargins(int40,(int40+int40),int40,0);
        //params.addRule(RelativeLayout.BELOW, txtHeaderDag.getId());
        //relo.addView(tom, params);
        skjerm.addView(tom, params);
    }



    private class HeavyDuty extends AsyncTask<String, String, TilkobletLinjeSingel[]> {
        boolean crash=false;

        @Override
        protected TilkobletLinjeSingel[] doInBackground(String... params) {
            Log.e("flyt", "heavyduty start");
            TilkobletLinjeSingel[] gen;
            try {
                gen = new HovedProgramv2().linjerVedIndeksert(params[0], params[1], dato , ResultsActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
                crash=true;
                gen=null;
            }
            return gen;

        }

        @Override
        protected void onPostExecute(TilkobletLinjeSingel[] reiser) {
            Log.e("flyt", "heavyduty post");
            if(crash){
                data=null;
                crash();
            }else if(reiser==null || reiser.length==0){
                data=null;
                ingenTreff();
            }else {
                data = reiser;
                adapter = new MyAdapter(reiser, ResultsActivity.this, dag, intentStasjon);
                rv.setAdapter(adapter);
                for (int i = 0; i < data.length; i++) {
                    if (realTid.erMindreEllerLik(data[i].singelTid)) {
                        manager.scrollToPosition(i);
                        break;
                    }
                }
            }
            Log.e("flyt", "heavyduty signal all");

        }

    }

}
