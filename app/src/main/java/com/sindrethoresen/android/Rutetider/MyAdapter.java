package com.sindrethoresen.android.Rutetider;

/**
 * Created by st97m_000 on 07.08.2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private TilkobletLinjeSingel[] forslag;
    private Context context;
    private String dag;
    private String stasjon;

    public MyAdapter(TilkobletLinjeSingel[] forslag, Context context, String dag, String stasjon) {
        this.forslag = forslag;
        this.context = context;
        this.dag=dag;
        this.stasjon=stasjon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.avgang_forslag, parent, false);
        return new ViewHolder(v);
        }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TilkobletLinjeSingel hent = forslag[position];

        holder.txtTid.setText(hent.singelTid.toString());
        holder.txtLinje.setText(hent.mor.fullRuteID + " " + hent.mor.header);
        holder.txtRetning.setText("Neste stasjon: " + hent.mor.nesteStasjon);
        holder.txtEnd.setText(    "Endestasjon  : " + hent.mor.endeStasjon);
        holder.txtLinje.setBackgroundColor(hent.mor.linjeFarge);
        holder.logo.setImageResource(hent.mor.avatarID);

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeRuteActivity.class);
                intent.putExtra("stasjon", stasjon);
                intent.putExtra("dag", dag);
                intent.putExtra("TilkobletLinje", hent.mor);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return forslag.length;
        }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTid;
        public TextView txtLinje;
        public TextView txtRetning;
        public TextView txtEnd;
        public ImageView logo;
        public TextView info;

        private final float[] NEGATIVE= {
                -1.0f,     0,     0,    0, 255, // red
                0, -1.0f,     0,    0, 255, // green
                0,     0, -1.0f,    0, 255, // blue
                0,     0,     0, 1.0f,   0  // alpha
        };

        public ViewHolder(View itemView){
            super(itemView);

            txtTid = (TextView) itemView.findViewById(R.id.txtTid);
            txtLinje = (TextView) itemView.findViewById(R.id.txtMain);
            txtRetning = (TextView) itemView.findViewById(R.id.txtRetning);
            txtEnd = (TextView) itemView.findViewById(R.id.txtEnd);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            logo.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
            info = (TextView) itemView.findViewById(R.id.txtInfo);
        }

    }
}