package com.sindrethoresen.android.Rutetider;

/**
 * Created by st97m_000 on 07.08.2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SeRuteAdapter extends RecyclerView.Adapter<SeRuteAdapter.ViewHolder>{

    private TilkobletLinje mor;
    private Context context;
    private String hovedStasjon;

    public SeRuteAdapter(TilkobletLinje mor, Context context, String hovedStasjon) {
        this.mor = mor;
        this.context = context;
        this.hovedStasjon = hovedStasjon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.se_rute_boks, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stasjon stasjon = mor.stasjonsFrekvens[position];
        int intervall=0;
        for(int i=1; i<=position;i++){
            intervall=intervall+mor.intervaller[i];
        }
        //Log.e("pos pre", "" + position  );
        if(position<9){
            holder.txtNr.setText(" " + (position+1) + ".");
        }else{
            holder.txtNr.setText((position+1) + ".");
        }
        //Log.e("pos post", "" + position  );
        holder.txtStopp.setText(stasjon.navn);
        if(stasjon.sone!=null){
            if(stasjon.sone.length()==1){
                holder.txtSone.setText(" " + stasjon.sone);
            }else{
                holder.txtSone.setText(stasjon.sone);
            }
        }else{
            holder.txtSone.setText("  ");
        }
        if(position!=0){
            if(intervall<10){
                holder.txtIntervall.setText("+0" + intervall + " ");
            }else{
                holder.txtIntervall.setText("+" + intervall + " ");
            }
        }else{
            holder.txtIntervall.setText("    ");
        }

         //  ---noe innebygd er bugged---
        /*
        if(hovedStasjon.equals(mor.stasjonsFrekvens[holder.getAdapterPosition()].navn)){
            Log.e("treff", "hovedstasjon:" + hovedStasjon + ". stasjon.navn:" + stasjon.navn + "."  );
            holder.card.setBackgroundColor(Color.parseColor("#3D88EC"));

            final float[] NEGATIVE= {
                    -1.0f,     0,     0,    0, 255, // red
                    0, -1.0f,     0,    0, 255, // green
                    0,     0, -1.0f,    0, 255, // blue
                    0,     0,     0, 1.0f,   0  // alpha
            };
            int hvit = Color.WHITE;
            holder.sign.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
            holder.txtNr.setTextColor(hvit);
            holder.txtStopp.setTextColor(hvit);
            holder.txtSone.setTextColor(hvit);
            holder.txtIntervall.setTextColor(hvit);
        }
        */



    }

    @Override
    public int getItemCount() {
        return mor.stasjonsFrekvens.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //public CardView card;
        public TextView txtNr;
        public TextView txtStopp;
        public TextView txtSone;
        public TextView txtIntervall;
        //public ImageView sign;

        public ViewHolder(View itemView){
            super(itemView);
            //card = (CardView) itemView.findViewById(R.id.card);
            txtNr = (TextView) itemView.findViewById(R.id.txtNr);
            txtStopp = (TextView) itemView.findViewById(R.id.txtMain);
            txtSone = (TextView) itemView.findViewById(R.id.txtSone);
            txtIntervall = (TextView) itemView.findViewById(R.id.txtIntervall);
            //sign = (ImageView) itemView.findViewById(R.id.sign);
        }

    }
}