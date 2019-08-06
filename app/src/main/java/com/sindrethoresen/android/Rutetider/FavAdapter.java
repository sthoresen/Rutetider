package com.sindrethoresen.android.Rutetider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by st97m_000 on 13.08.2017.
 */

class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder>{

    private ArrayList<Favoritt> data;
    private Context context;
    private SharedPreferences mSharedPreferences;

    FavAdapter(ArrayList<Favoritt> data, Context context, SharedPreferences mSharedPreferences) {
        this.data = data;
        this.context = context;
        this.mSharedPreferences = mSharedPreferences;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favoritt_boks, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Favoritt relevant = data.get(position);
        holder.txtMain.setText(relevant.getText()+  ", " + relevant.getDag());
        if(relevant.isStopp()){
            holder.favLogo.setImageResource(R.drawable.big_signpost);
        }else{
            int logo = relevant.getLogo();
            if(logo==0){
                holder.favLogo.setImageResource(R.drawable.tram);
            }else if(logo==1){
                holder.favLogo.setImageResource(R.drawable.metro);
            }else if(logo==2){
                holder.favLogo.setImageResource(R.drawable.train);
            }else if(logo==3){
                holder.favLogo.setImageResource(R.drawable.bus);
            }else if(logo==4){
                holder.favLogo.setImageResource(R.drawable.boat);
            }else{
                Log.e("kritisk", "det er mer enn 4 ruteIDer");
                holder.favLogo.setImageResource(R.drawable.bus);
            }
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relevant.isStopp()){
                    mSharedPreferences.edit().remove("Stopp," + relevant.getText()  + ",dag," + relevant.getDag()).apply();
                }else{
                    mSharedPreferences.edit().remove("Rute," + relevant.getText()  + ",dag," + relevant.getDag()).apply();
                }
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relevant.isStopp()){
                    Intent intent = new Intent(context, ResultsActivity.class);
                    intent.putExtra("stasjon",  relevant.getText());


                    //finner neste dato som stemmer med dagen
                    String intentDag = relevant.getDag();
                    Calendar c = Calendar.getInstance();
                    String iterDag;
                    int dagKode = c.get(Calendar.DAY_OF_WEEK);
                    switch (dagKode) { //init før for loop, i tilfelle det er det spesifiserte dagen
                        case Calendar.MONDAY:
                            iterDag=context.getString(R.string.ma);
                            break;

                        case Calendar.TUESDAY:
                            iterDag=context.getString(R.string.ti);;
                            break;

                        case Calendar.WEDNESDAY:
                            iterDag=context.getString(R.string.on);
                            break;

                        case Calendar.THURSDAY:
                            iterDag=context.getString(R.string.to);
                            break;

                        case Calendar.FRIDAY:
                            iterDag=context.getString(R.string.fr);
                            break;

                        case Calendar.SATURDAY:
                            iterDag=context.getString(R.string.lø);
                            //Log.e("flyt", "hit");
                            break;

                        case Calendar.SUNDAY:
                            iterDag=context.getString(R.string.sø);
                            break;

                        default :
                            iterDag=null;
                            Log.e("kritisk error", "unexcpected in FavAdapter");
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
                                iterDag=context.getString(R.string.ma);
                                break;

                            case Calendar.TUESDAY:
                                iterDag=context.getString(R.string.ti);;
                                break;

                            case Calendar.WEDNESDAY:
                                iterDag=context.getString(R.string.on);
                                break;

                            case Calendar.THURSDAY:
                                iterDag=context.getString(R.string.to);
                                break;

                            case Calendar.FRIDAY:
                                iterDag=context.getString(R.string.fr);
                                break;

                            case Calendar.SATURDAY:
                                iterDag=context.getString(R.string.lø);
                                break;

                            case Calendar.SUNDAY:
                                iterDag=context.getString(R.string.sø);
                                break;
                        }
                    }
                    Log.e("flyt", "risk end");
                    intent.putExtra("dato", c.getTime());
                    intent.putExtra("tid", GlobaleMetoder.getSystemTime());
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, RuteTiderActivity.class);
                    intent.putExtra("linje",  relevant.getText());

                    //finner neste dato som stemmer med dagen
                    String intentDag = relevant.getDag();
                    Calendar c = Calendar.getInstance();
                    String iterDag;
                    int dagKode = c.get(Calendar.DAY_OF_WEEK);
                    switch (dagKode) { //init før for loop, i tilfelle det er det spesifiserte dagen
                        case Calendar.MONDAY:
                            iterDag=context.getString(R.string.ma);
                            break;

                        case Calendar.TUESDAY:
                            iterDag=context.getString(R.string.ti);;
                            break;

                        case Calendar.WEDNESDAY:
                            iterDag=context.getString(R.string.on);
                            break;

                        case Calendar.THURSDAY:
                            iterDag=context.getString(R.string.to);
                            break;

                        case Calendar.FRIDAY:
                            iterDag=context.getString(R.string.fr);
                            break;

                        case Calendar.SATURDAY:
                            iterDag=context.getString(R.string.lø);
                            //Log.e("flyt", "hit");
                            break;

                        case Calendar.SUNDAY:
                            iterDag=context.getString(R.string.sø);
                            break;

                        default :
                            iterDag=null;
                            Log.e("kritisk error", "unexcpected in FavAdapter");
                    }


                    Log.e("flyt", "risk start");
                    while(!intentDag.equals(iterDag)){
                        Log.e("e test", intentDag + "-" + iterDag);
                        c.add(Calendar.DAY_OF_YEAR, 1);
                        dagKode = c.get(Calendar.DAY_OF_WEEK);
                        switch (dagKode) {
                            case Calendar.MONDAY:
                                iterDag=context.getString(R.string.ma);
                                break;

                            case Calendar.TUESDAY:
                                iterDag=context.getString(R.string.ti);;
                                break;

                            case Calendar.WEDNESDAY:
                                iterDag=context.getString(R.string.on);
                                break;

                            case Calendar.THURSDAY:
                                iterDag=context.getString(R.string.to);
                                break;

                            case Calendar.FRIDAY:
                                iterDag=context.getString(R.string.fr);
                                break;

                            case Calendar.SATURDAY:
                                iterDag=context.getString(R.string.lø);
                                break;

                            case Calendar.SUNDAY:
                                iterDag=context.getString(R.string.sø);
                                break;
                        }
                    }
                    Log.e("flyt", "risk end");
                    intent.putExtra("dato", c.getTime());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
         TextView txtMain;
         ImageView favLogo;
         ImageView delete;
         CardView card;

         ViewHolder(View itemView){
            super(itemView);
            txtMain = (TextView) itemView.findViewById(R.id.txtMain);
            favLogo = (ImageView) itemView.findViewById(R.id.favLogo);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            card = (CardView) itemView.findViewById(R.id.card);
        }

    }
}