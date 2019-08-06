package com.sindrethoresen.android.Rutetider;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by st97m_000 on 13.08.2017.
 */

public class FavorittActivity extends AppCompatActivity implements Serializable {


    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    LinearLayoutManager manager;
    private SharedPreferences mSharedPreferences;
    LinearLayout skjerm;
    ArrayList<Favoritt> favorittListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritter);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        skjerm = (LinearLayout) findViewById(R.id.skjerm);
        manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        favorittListe = hentFavoritter();
        if(favorittListe.size()==0){
            ingenFavoritter();
        }else{
            Log.e("flyt", "favoritter funnet. Starter adapter");
            adapter = new FavAdapter(favorittListe, this, mSharedPreferences);
            rv.setAdapter(adapter);
        }

    }

    public ArrayList<Favoritt> hentFavoritter(){
        ArrayList<Favoritt> favoritter = new ArrayList<>();
        for(String key : mSharedPreferences.getAll().keySet()){
            Log.e("flyt", "favoritt key funnet:" + key + ".");
            String[] ar = key.split(",");
            boolean stopp=false;
            int type=-1;
            if(ar[0].equals("Stopp")){
                stopp=true;
            }else{
                type=Integer.parseInt(mSharedPreferences.getAll().get(key).toString());
            }
            String text = ar[1];
            String dag=ar[3];

            favoritter.add(new Favoritt(stopp, text,dag,type));
        }
        return favoritter;
    }

    public void ingenFavoritter(){
        rv.setVisibility(View.GONE);
        TextView tom = new TextView(this);
        tom.setText("Oops! Du har ingen favoritter enda.");
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

}