package com.sindrethoresen.android.Rutetider;

/**
 * Created by st97m_000 on 23.07.2017.
 */

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RuteTiderActivity extends AppCompatActivity {
    LinearLayout skjerm;
    TextView helpHeader;
    TextView txtHeaderDag;
    TextView txtHelpSingle;
    RelativeLayout relo;
    ImageView favoritt;
    Toast lastToast;
    ScrollView vScroll;
    RelativeLayout vScrollChild;
    LinearLayout vLin;
    HorizontalScrollView hScroll;
    RelativeLayout hScrollChild;
    TextView txtHelpBar;
    String inntxt;
    Date dato;
    String dag;
    ArrayList<ImageView> warnings = new ArrayList<>();
    DisplayMetrics displayMetrics;
    RuteTid ruteTid;

    private SharedPreferences mSharedPreferences;
    boolean favorittMerket;
    boolean merketInngang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutetider);

        skjerm = (LinearLayout) findViewById(R.id.skjerm);
        relo = (RelativeLayout) findViewById(R.id.relo);
        favoritt= (ImageView) findViewById(R.id.favoritt);

        helpHeader = (TextView) findViewById(R.id.helpHeader);
        vScroll = (ScrollView) findViewById(R.id.vScroll);
        vScrollChild = (RelativeLayout) findViewById(R.id.vScrollChild);
        vLin = (LinearLayout) findViewById(R.id.vLin);
        hScroll = (HorizontalScrollView) findViewById(R.id.hScroll);
        hScrollChild = (RelativeLayout) findViewById(R.id.hScrollChild);
        txtHelpBar = (TextView) findViewById(R.id.txtHelpBar);
        txtHeaderDag = (TextView) findViewById(R.id.txtHeaderDag);
        txtHelpSingle = (TextView) findViewById(R.id.txtHelpSingle);

        inntxt= getIntent().getExtras().getString("linje");
        dato= (Date) getIntent().getExtras().getSerializable("dato");
        helpHeader.setText(inntxt);

        Calendar c = Calendar.getInstance();
        c.setTime(dato);
        int day = c.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                dag=getString(R.string.ma);
                break;

            case Calendar.TUESDAY:
                dag=getString(R.string.ti);;
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
        txtHeaderDag.setText(dag + " " + formattedDate);

        //set riktig fylt/ikke fylt
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        favorittMerket = mSharedPreferences.contains("Rute," + inntxt + ",dag," + dag );
        merketInngang=favorittMerket;
        if(merketInngang) favoritt.setImageResource(R.drawable.star100);

        displayMetrics = getResources().getDisplayMetrics();

        vLin.getViewTreeObserver()
                .addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(vLin, displayMetrics));

        relo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ruteTid!=null && ruteTid.type!=-1) {
                    if (favorittMerket) {
                        favorittMerket = !favorittMerket;
                        favoritt.setImageResource(R.drawable.add_to_favorite);
                        CharSequence text = "Fjernet fra favoritter";
                        if (lastToast != null) lastToast.cancel();
                        int duration = Toast.LENGTH_SHORT;
                        lastToast = Toast.makeText(RuteTiderActivity.this, text, duration);
                        lastToast.show();
                    } else {
                        favorittMerket = !favorittMerket;
                        favoritt.setImageResource(R.drawable.star100);
                        CharSequence text = "La til i favoritter";
                        if (lastToast != null) lastToast.cancel();
                        int duration = Toast.LENGTH_SHORT;
                        lastToast = Toast.makeText(RuteTiderActivity.this, text, duration);
                        lastToast.show();

                    }
                }
            }
        });




        new Thread(){
            @Override
            public void run(){
                /*
                ScrollLinje[] reiser = new HovedProgramv2().linjerVed(getIntent().getExtras().getString("stasjon")
                        , getIntent().getExtras().getString("tid"), ResultsActivity.this);
                fyllInnInfo(reiser);
                syncerCondition.signalAll();
                */
                Log.e("flyt", "heavy call");
                new HeavyDuty().execute(inntxt);
            }
        }.start();


        /*
        TextView txtHelpSingle = (TextView) findViewById(R.id.txtHelpSingle);
        txtHelpSingle.setText("Stoppested");
        txtHelpSingle.setHeight( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()) );

        TextView txtHelpBar = (TextView) findViewById(R.id.txtHelpBar);
        txtHelpBar.setText("Avganger");
        txtHelpBar.setHeight( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()) );

        drawGrunnlag();
        */
    }

    @Override
    protected  void onPause(){
        super.onPause();

        if(merketInngang!=favorittMerket){
            if(merketInngang){
                mSharedPreferences.edit().remove("Rute," + inntxt + ",dag," + dag ).apply();
            }else{
                int relType=-1;
                if(ruteTid!=null) relType=ruteTid.type;
                mSharedPreferences.edit().putString("Rute," + inntxt + ",dag," + dag ,"" + relType).apply();
            }

        }

    }

    private class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private DisplayMetrics displayMetrics;
        private int maxWidth;//px
        private View view;

        public OnViewGlobalLayoutListener(View view, DisplayMetrics displayMetrics) {
            this.view = view;
            this.displayMetrics=displayMetrics;
            this.maxWidth=Math.round(100 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)); //100 dp in pixels
        }
        @Override
        public void onGlobalLayout() {
            //Log.e("flyt", "vLin auto vidde:" + view.getWidth());
            //Log.e("flyt", "maxwidth:" + maxWidth);
            if (view.getWidth() > maxWidth){
                //Log.e("flyt", "trigget");
                //view.getLayoutParams().width = 20;
                view.setLayoutParams(new RelativeLayout.LayoutParams(maxWidth,RelativeLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    public void fyllInnInfo(RuteTid rt){


        //Printer stasjoner for seg selv i vLin
        TextView txtfakie = new TextView(this);
        txtfakie.setTextColor(Color.parseColor("#000000"));
        txtfakie.setText("");
        txtfakie.setHeight( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()) );
        vLin.addView(txtfakie);

        for(String s : rt.stasjoner){
            TextView txt = new TextView(this);
            txt.setTextColor(Color.parseColor("#000000"));

            if(s.length()>30){
                txt.setTextSize(10);
            }
            //txt.setBackgroundColor(Color.WHITE);
            txt.setText(s);
            txt.setHeight( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()) );
            txt.setGravity(Gravity.CENTER_VERTICAL);
            //txt.setPadding(0,0,0,20);
            vLin.addView(txt);
        }



        //Printer alle tider kolonnevis:

        int sisteBoksIdTry=0;
        int teller=3000;
        int enterteller=0;
        int lastColumnID=-1;
        int ruteTeller=0;
        int coloriter=-1;
        for(TidKolonne tk : rt.tider) {

            if(tk.determineError()) advarsel();

            //Oppretter viewene og setter de enkle verdiene-------------------------------------------------------------------------------------------
            //----------------------------------------------------------------------------------------------------------------------------------------
            TextView txtKolonne = new TextView(this);
            TextView txtHeader = new TextView(this);
            ImageView logo = new ImageView(this);
            txtHeader.setId(++teller);
            logo.setId(++teller);
            txtKolonne.setId(++teller);
            String kolonneString=null;

            if(coloriter==-1 || coloriter==Color.TRANSPARENT){
                coloriter=Color.parseColor("#E8E8EE");
            }else{
                coloriter=Color.TRANSPARENT;
            }
            txtKolonne.setBackgroundColor(coloriter);
            txtHeader.setBackgroundColor(coloriter);
            logo.setBackgroundColor(coloriter);
            txtHeader.setTextColor(Color.parseColor("#000000"));
            txtHeader.setTextSize(10);
            txtHeader.setText("Reise\n#" + (++ruteTeller));

            if(tk.endestasjonIndex != rt.stasjoner.length-1) {
                logo.setImageResource(R.drawable.warning32);
            }else{
                txtKolonne.setTextColor(Color.parseColor("#000000"));
            }


                //Gir samtlige views onclicklisteners-----------------------------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------------------------
                final int forAnonindex = tk.endestasjonIndex;
                final int forAnonteller = ruteTeller;
                String[] arr43 = inntxt.split(" ");
                final String[] forAnonar = rt.stasjoner;
                final String forAnonString = arr43[0] + " " + tk.header;

                txtHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text;
                        if (forAnonindex == forAnonar.length - 1) {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nEndestasjon: " + forAnonar[forAnonindex]);
                        } else if (forAnonindex != -1) {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nAdvarsel: Siste stopp er " + forAnonar[forAnonindex]);
                        } else {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nEn feil har oppstått");
                        }
                        if(lastToast!=null) lastToast.cancel();
                        int duration = Toast.LENGTH_SHORT;
                        lastToast = Toast.makeText(RuteTiderActivity.this, text, duration);
                        lastToast.show();
                    }
                });
                txtKolonne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text;
                        if (forAnonindex == forAnonar.length - 1) {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nEndestasjon: " + forAnonar[forAnonindex]);
                        } else if (forAnonindex != -1) {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nAdvarsel: Siste stopp er " + forAnonar[forAnonindex]);
                        } else {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nEn feil har oppstått");
                        }
                        if(lastToast!=null) lastToast.cancel();
                        int duration = Toast.LENGTH_SHORT;
                        lastToast = Toast.makeText(RuteTiderActivity.this, text, duration);
                        lastToast.show();
                    }
                });
                logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text;
                        if (forAnonindex == forAnonar.length - 1) {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nEndestasjon: " + forAnonar[forAnonindex]);
                        } else if (forAnonindex != -1) {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nAdvarsel: Siste stopp er " + forAnonar[forAnonindex]);
                        } else {
                            text = ("Reise #" + (forAnonteller) + "\n\"" + forAnonString + "\"\nEn feil har oppstått");
                        }
                        if(lastToast!=null) lastToast.cancel();
                        int duration = Toast.LENGTH_SHORT;
                        lastToast = Toast.makeText(RuteTiderActivity.this, text, duration);
                        lastToast.show();
                    }
                });

                //Bygger opp det viktigste, tid-stringen i kolonnen---------------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------------------------
                for (Tid t : tk.tider) {

                    if (kolonneString == null) {
                        if (t == null) {
                            //txtTid.setText("  "  + "00:00");
                            kolonneString = "-";
                        } else {
                            kolonneString = t.toString();
                        }
                    } else {
                        if (t == null) {
                            kolonneString = kolonneString + "\n-";
                        } else {
                            kolonneString = kolonneString + "\n" + t.toString();
                        }
                    }

                }
                txtKolonne.setText(kolonneString);
                //Definerer params for alle views og plasser dem inn i det relative layoutet--------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------------------------

                //Header--------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------------------------

                //txtHeader.setPadding(0, 0, 0, 0);
                txtHeader.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 51, getResources().getDisplayMetrics()) );
                txtHeader.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
                txtHeader.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                RelativeLayout.LayoutParams txtHeaderParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                txtHeaderParams.addRule(RelativeLayout.BELOW, txtHelpBar.getId());

                //LOGO--------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------------------------

                RelativeLayout.LayoutParams logoParams = new RelativeLayout.LayoutParams(32, 32);
                int int5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                int int12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
                logoParams.setMargins(int12, 0, 0, int5); //l,t,r,b
                //logoParams.setMargins(int12, 0, 0, 0); //l,t,r,b
                logoParams.addRule(RelativeLayout.ALIGN_BOTTOM, txtHeader.getId());

                //KOLONNE-------------------------------------------------------------
                //----------------------------------------------------------------------------------------------------------------------------------------

                RelativeLayout.LayoutParams txtKolonneParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //txtTidParams.setMargins(0, 0, 10, 0);
                //txtTidParams.setMargins(5,0,5,0);
                //txtKolonne.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
                txtKolonne.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
                txtKolonne.setGravity(Gravity.CENTER);
                txtKolonne.setLineSpacing( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)23.5, getResources().getDisplayMetrics()) ,1);

                if (teller == 3003) {
                    //txtTidParams.addRule(RelativeLayout.BELOW, txtHelpBar.getId());
                    txtHeaderParams.addRule(RelativeLayout.RIGHT_OF, vLin.getId());
                    logoParams.addRule(RelativeLayout.RIGHT_OF, vLin.getId());
                    txtKolonneParams.addRule(RelativeLayout.RIGHT_OF, vLin.getId());
                } else {
                    //txtTidParams.addRule(RelativeLayout.BELOW, sisteBoksId);
                    txtHeaderParams.addRule(RelativeLayout.RIGHT_OF, (teller-5) );
                    logoParams.addRule(RelativeLayout.RIGHT_OF, (teller-5) );
                    txtKolonneParams.addRule(RelativeLayout.RIGHT_OF, (teller-5) );
                }

                txtKolonneParams.addRule(RelativeLayout.BELOW, txtHeader.getId());

                hScrollChild.addView(txtHeader, txtHeaderParams);
                hScrollChild.addView(logo, logoParams);
                hScrollChild.addView(txtKolonne, txtKolonneParams);
                warnings.add(logo);
        }


        for(ImageView iv : warnings){
            iv.bringToFront();
        }
        warnings.clear();

    }

    public void ingenTreff(){
        vScroll.setVisibility(View.GONE);
        txtHelpBar.setText("");
        txtHelpSingle.setText("");
        TextView tom = new TextView(this);
        tom.setText("Ruten har ingen reiser denne dagen");
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

    public void error(){
        vScroll.setVisibility(View.GONE);
        txtHelpBar.setText("");
        txtHelpSingle.setText("");
        TextView tom = new TextView(this);
        tom.setText("Æsj! Noe gikk galt. Kontakt gjerne utvikleren om hvilken rute du søkte på");
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

    public void advarsel(){
        CharSequence text = "Advarsel: Klarte ikke å bestemme riktig stasjons-rekkfølge";
        int duration = Toast.LENGTH_LONG;
        if(lastToast!=null) lastToast.cancel();
        lastToast = Toast.makeText(RuteTiderActivity.this, text, duration);
        lastToast.show();

    }

    private class HeavyDuty extends AsyncTask<String, String, RuteTid> {
        boolean crash=false;

        @Override
        protected RuteTid doInBackground(String... params) {
            Log.e("flyt", "heavyduty start");
            RuteTid gen;
            try {
                gen= new HovedProgramv2().genererRuteTider(params[0], dato , RuteTiderActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
                crash=true;
                gen=null;
            }
            ruteTid=gen;
            return gen;
        }

        @Override
        protected void onPostExecute(RuteTid rt) {
            Log.e("flyt", "heavyduty post");

            //Log.e("flyt", "rt printtest");
            /*
            if(rt!=null) {
                int iter=0;
                for (String s : rt.stasjoner) {
                    Log.e("stasjoner", "id:" + (iter++) + ":" + s);
                }
                for (TidKolonne tk : rt.tider) {
                    String build=null;
                    for(Tid t : tk.tider) {
                        if(build==null) {
                            if (t == null) {
                                build="--:--";
                            } else {
                                build=t.toString();
                            }
                        }else{
                            if (t == null) {
                                build = build + "," + "--:--";
                            } else {
                                build = build + "," + t.toString();
                            }
                        }
                    }
                    //Log.e("tidstream (horisontal)", build);
                }
            }else{
                Log.e("flyt", "oops, rt er null");
            }
            */
            if(crash){
                error();
            }else if(rt==null){
                ingenTreff();
            }else{
                fyllInnInfo(rt);
            }
            Log.e("flyt", "heavyduty signal all");
        }
    }



}
