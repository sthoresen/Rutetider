package com.sindrethoresen.android.Rutetider;

import android.content.Context;
import android.graphics.Color;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

class GlobaleMetoder{ //båtedit
    private static String[] stopsIminne;
    private static String[] ruterIminne;
    private static VirituellFil routes;
    private static VirituellFil rutetider;
    private static VirituellFil rutetranslator;
    private static VirituellFil stops;
    private static VirituellFil service;
    private static VirituellFil update;
    private static ReentrantLock laas = new ReentrantLock();
    private static Date min;
    private static Date max;

    public static VirituellFil hentFil(String filnavn, Context myContext) {

        if(filnavn.equals("FINAL_ROUTES.txt") && routes!=null){
            routes.reset();
            return routes;
        }else if(filnavn.equals("FINAL_RUTETIDER.txt") && rutetider!=null){
            rutetider.reset();
            return rutetider;
        }else if(filnavn.equals("FINAL_RUTETRANSLATOR.txt") && rutetranslator!=null){
            rutetranslator.reset();
            return rutetranslator;
        }else if(filnavn.equals("FINAL_STOPS.txt") && stops!=null) {
            stops.reset();
            return stops;
        }else if(filnavn.equals("FINAL_SERVICE_IDS.txt") && service!=null) {
            service.reset();
            return service;
        }else if(filnavn.equals("FINAL_UPDATE_DATE.txt") && update!=null) {
            update.reset();
            return update;
        }

        ArrayList<String> linjer = new ArrayList<>();
        VirituellFil done=null;
        try {
            if((filnavn.equals("FINAL_RUTETIDER.txt"))) filnavn = "FINAL_REL_RUTETIDER.txt";
            if((filnavn.equals("FINAL_STOPS.txt"))) filnavn = "FINAL_STOPS_I.txt";
            BufferedReader reader = new BufferedReader(new InputStreamReader(myContext.getAssets().open(filnavn)));
            String line;
            while ((line = reader.readLine()) != null) {
                linjer.add(line);
            }
            String[] ar = new String[linjer.size()];
            for(int i =0; i<linjer.size(); i++){
                ar[i]=linjer.get(i);
            }
            done = new VirituellFil(ar);

        } catch (IOException ex) {
            Log.e("min error", "krasj i hentFil metode");
            ex.printStackTrace();
            Toast.makeText(myContext, "Finner ikke rutetid-dataene (har du slettet dem)? Vennligst reinstaller appen", Toast.LENGTH_LONG).show();
        }

        if(filnavn.equals("FINAL_ROUTES.txt")){
            routes=done;
        }else if(filnavn.equals("FINAL_REL_RUTETIDER.txt")){
            rutetider=done;
        }else if(filnavn.equals("FINAL_RUTETRANSLATOR.txt")){
            rutetranslator=done;
        }else if(filnavn.equals("FINAL_STOPS_I.txt")) {
            stops=done;
        }else if(filnavn.equals("FINAL_SERVICE_IDS.txt")) {
            service=done;
        }else if(filnavn.equals("FINAL_UPDATE_DATE.txt")) {
            update=done;
        }
        return done;
    }
    /*
    //lager en tilfeldig String av lengde numchars
    private static String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
    */

    public static void lagStopData(Context myContext) {
        laas.lock();
        if(stopsIminne!=null){
            laas.unlock();
            return;
        }
        ArrayList<String> rawLinjer = new ArrayList<>();
        String[] ferdig = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(myContext.getAssets().open("FINAL_STOPS_I.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] ar = line.split(",");
                rawLinjer.add(ar[1]);
            }
            ferdig = new String[rawLinjer.size()];
            for (int i = 0; i < rawLinjer.size(); i++) {
                ferdig[i] = rawLinjer.get(i);
            }
        } catch (IOException ex) {
            Log.e("min error", "krasj i stops_init metode");
            Toast.makeText(myContext, "Finner ikke rutetid-dataene (har du slettet dem)? Vennligst reinstaller appen", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        stopsIminne = ferdig;
        laas.unlock();
    }
    public static String[] getStopArray(){
        return stopsIminne;
    }

    public static String getSystemTime(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return String.format("%02d:%02d", hour, minute);
    }

    public static String[] getRuteArray(){
        return ruterIminne;
    }

    public static void lagRuteData(Context myContext){
      laas.lock();
      if(ruterIminne!=null){
          laas.unlock();
          return;
      }
      ArrayList<String> rawLinjer = new ArrayList<>();
      String[] ferdig = null;
      try {
          BufferedReader reader = new BufferedReader(new InputStreamReader(myContext.getAssets().open("FINAL_ROUTES.txt")));
          String line;
          while ((line = reader.readLine()) != null) {
              String[] ar = line.split(",");
              String rf = safeFormatRuteNavn(ar[2]);
              rawLinjer.add(ar[1] + " " + rf);
              if(ar[4].equals("1")){
                  String twist=twistRuteNavn(rf);
                  rawLinjer.add(ar[1] + " " + twist);
              }
          }
          ferdig = new String[rawLinjer.size()];
          for (int i = 0; i < rawLinjer.size(); i++) {
              ferdig[i] = rawLinjer.get(i);
          }
      } catch (IOException ex) {
          Log.e("min error", "krasj i ruteArray_get metode");
          Toast.makeText(myContext, "Finner ikke rutetid-dataene (har du slettet dem)? Vennligst reinstaller appen", Toast.LENGTH_LONG).show();
          ex.printStackTrace();
      }
      ruterIminne=ferdig;
      laas.unlock();
    }

    public static String twistRuteNavn(String rute){
        if(!rute.contains(" - ")) {
            //spesialcase om den videre innholder en og bare en "-"
            int firstPosisjon=rute.indexOf("-");
            int lastPosisjon=rute.lastIndexOf("-");
            if(firstPosisjon==lastPosisjon && firstPosisjon!=-1 && firstPosisjon!=0 && lastPosisjon!=(rute.length()-1)){
                String[] ar = rute.split("-");
                return removeTrailingSpaces(ar[0]) + " - " + removeTrailingSpaces(ar[1]);
            }

            return removeTrailingSpaces(rute);
        }else {
            String[] ar = rute.split(" - ");
            if (ar.length == 2) {
                String new1st = removeTrailingSpaces(ar[1]);
                String new2nd = removeTrailingSpaces(ar[0]);
                return new1st + " - " + new2nd;
            } else {
                String new1st = removeTrailingSpaces(ar[ar.length - 1]);
                String newLast = removeTrailingSpaces(ar[0]);
                String midtDel = null;
                for (int i = 1; i < (ar.length - 1); i++) {
                    if (midtDel == null) {
                        midtDel = removeTrailingSpaces(ar[i]);
                    } else {
                        midtDel = midtDel + " - " + removeTrailingSpaces(ar[i]);
                    }
                }
                if (midtDel == null) {
                    return new1st + " - " + newLast;
                } else {
                    return new1st + " - " + midtDel + " - " + newLast;
                }


            }
        }

    }

    public static String safeFormatRuteNavn(String rute){
        if(!rute.contains(" - ")) {
            //spesialcase om den videre innholder en og bare en "-"
            int firstPosisjon=rute.indexOf("-");
            int lastPosisjon=rute.lastIndexOf("-");
            if(firstPosisjon==lastPosisjon && firstPosisjon!=-1 && firstPosisjon!=0 && lastPosisjon!=(rute.length()-1)){
                String[] ar = rute.split("-");
                return removeTrailingSpaces(ar[0]) + " - " + removeTrailingSpaces(ar[1]);
            }

            return removeTrailingSpaces(rute);
        }else {
            String[] ar = rute.split(" - ");
            String build=null;
            for(int i =0; i<ar.length; i++){
                if(build==null){
                    build= removeTrailingSpaces(ar[i]);
                }else{
                    build = build + " - " + removeTrailingSpaces(ar[i]);
                }
            }
            return build;
        }
    }

    private static String removeTrailingSpaces(String inn){
        while(true){
            if (inn.charAt(0) == ' ') {
                inn = inn.substring(1);
            }else{
                break;
            }

        }
        while(true){
            if (inn.charAt(inn.length()-1) == ' ') {
                inn = inn.substring(0, (inn.length() - 1));
            }else{
                break;
            }
        }
        //Log.e("flyt", "ut ifra removespaces:" + inn + ".");
        return inn;
    }

    public static Date[] minMax(Context myContext){
        Date[] dar = new Date[2];
        if(min!=null && max!=null){
            dar[0] = min;
            dar[1] = max;
            return dar;
        }else{
            VirituellFil sjekk = hentFil("FINAL_SERVICE_IDS.txt", myContext);
            String s = sjekk.nextLine();
            String[] ar = s.split(",");
            //måneden først
            String minS = ar[8];
            String maxS = ar[9];
            int maxy, maxm, maxd, miny, minm, mind;
            maxy=Integer.parseInt(maxS.substring(0,4));
            maxm=Integer.parseInt(maxS.substring(4,6));
            maxd=Integer.parseInt(maxS.substring(6,8));
            miny=Integer.parseInt(minS.substring(0,4));
            minm=Integer.parseInt(minS.substring(4,6));
            mind=Integer.parseInt(minS.substring(6,8));
            Calendar c = Calendar.getInstance();
            c.set(miny, minm-1 ,mind);
            dar[0] = c.getTime();
            c.set(maxy, maxm-1, maxd);
            dar[1] = c.getTime();
            min=dar[0];
            max=dar[1];
            return dar;
        }
    }


    public static void clearCache(){
      routes = null;
      rutetider = null;
      rutetranslator = null;
      stops = null;
    }



}



class HovedProgramv2 {

    /*
    //private String path;
    private Context myContext;

    public HovedProgramv2(Context myContext) {
        //this.path=path;
        this.myContext = myContext;
    }


    private File hentFil(String filnavn) {
        File construct = new File(myContext.getFilesDir(), "cache.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(myContext.getAssets().open(filnavn)));
            String line;
            PrintWriter constructor = new PrintWriter(construct);
            while ((line = reader.readLine()) != null) {
                constructor.println(line);
            }
            constructor.close();

        } catch (IOException ex) {
            Log.e("min error", "krasj i hentFil metode");
            ex.printStackTrace();
            construct = null;
        }
        return construct;
    }

    public String[] getStopData() {
        ArrayList<String> rawLinjer = new ArrayList<>();
        String[] ferdig = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(myContext.getAssets().open("FINAL_STOPS.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] ar = line.split(",");
                rawLinjer.add(ar[1]);
            }
            ferdig = new String[rawLinjer.size()];
            for (int i = 0; i < rawLinjer.size(); i++) {
                ferdig[i] = rawLinjer.get(i);
            }
        } catch (IOException ex) {
            Log.e("min error", "krasj i stops_init metode");
            ex.printStackTrace();
        }
        return ferdig;
    }
    */



    /*
    public String[] finnStasjoner(String typed, String[] stopsData) {

        typed=typed.toLowerCase();
        String[] matches = new String[5];

        if(typed.length()<3) return null;

        int funnet=0;
        ArrayList<String> funnetAllerede = new ArrayList<>();
        //soker forst etter en stasjon som matcher akkurat med typed
        for(int i=0; (i<stopsData.length) && funnet<5; i++){
            if(stopsData[i].equalsIgnoreCase(typed)){
                matches[funnet]=stopsData[i];
                funnet++;
                funnetAllerede.add(stopsData[i]);
                break;
            }
        }


        //soker saa etter stasjoner som starter med typed
        for(int i=0; (i<stopsData.length) && funnet<5; i++){
            if(stopsData[i].toLowerCase().startsWith(typed) && !funnetAllerede.contains(stopsData[i])){
                matches[funnet]=stopsData[i];
                funnet++;
                if(funnet==5) return matches;
                funnetAllerede.add(stopsData[i]);
            }
        }

        //soker naa etter stasjoner som innholder typed
        for(int i=0; (i<stopsData.length) && funnet<5; i++){
            if(stopsData[i].toLowerCase().contains(typed) && !funnetAllerede.contains(stopsData[i])){
                matches[funnet]=stopsData[i];
                funnet++;
                if(funnet==5) return matches;
            }
        }
        return matches;
    }
    */
    /*
    public ReiseLinje[] finnLinjer(String typed, Context myContext) {
        typed = typed.toLowerCase();
        VirituellFil inn = GlobaleMetoder.hentFil("FINAL_ROUTES.txt", myContext);

        ReiseLinje[] matches = new ReiseLinje[5];

        if (typed.length() < 1) return null;
        boolean opptagetNummerSok = false;
        if (typed.length() == 1) {
            opptagetNummerSok = true;
        } else {
            for (char c : typed.toCharArray()) {
                try {
                    char[] a = new char[1];
                    a[0] = c;
                    int test = Integer.parseInt(new String(a));
                    opptagetNummerSok = true;
                    break;
                } catch (Exception e) {
                }
            }
        }

        int funnet = 0;
        ArrayList<ReiseLinje> funnetAllerede = new ArrayList<>();
        //soker forst etter en line som matcher akkurat med typed
        while (opptagetNummerSok && funnet < 5 && inn.hasNextLine()) {
            String currentLinje = inn.nextLine();
            String[] ar = currentLinje.split(",");
            if (ar[1].equalsIgnoreCase(typed)) {
                ReiseLinje rl = new ReiseLinje(currentLinje);
                matches[funnet] = rl;
                funnet++;
                funnetAllerede.add(rl);
                break;
            }
        }
        inn = GlobaleMetoder.hentFil("FINAL_ROUTES.txt", myContext);

        //soker saa etter linjer som starter med typed
        while (opptagetNummerSok && funnet < 5 && inn.hasNextLine()) {
            String currentLinje = inn.nextLine();
            String[] ar = currentLinje.split(",");
            if (ar[1].toLowerCase().startsWith(typed) && !funnetAllerede.contains(new ReiseLinje(currentLinje))) {
                ReiseLinje rl = new ReiseLinje(currentLinje);
                matches[funnet] = rl;
                funnet++;
                if (funnet == 5) return matches;
                funnetAllerede.add(rl);
            }
        }

        //soker naa etter linjer som innholder typed
        inn = GlobaleMetoder.hentFil("FINAL_ROUTES.txt", myContext);

        while (opptagetNummerSok && funnet < 5 && inn.hasNextLine()) {
            String currentLinje = inn.nextLine();
            String[] ar = currentLinje.split(",");
            if (ar[1].toLowerCase().contains(typed) && !funnetAllerede.contains(new ReiseLinje(currentLinje))) {
                ReiseLinje rl = new ReiseLinje(currentLinje);
                matches[funnet] = rl;
                funnet++;
                if (funnet == 5) return matches;
            }
        }
        if (opptagetNummerSok) return matches;

        //Soker naa etter etter et linjenavn som er likt typed
        if (typed.length() > 8) {
            while (funnet < 5 && inn.hasNextLine()) {
                String currentLinje = inn.nextLine();
                String[] ar = currentLinje.split(",");
                if (ar[2].equalsIgnoreCase(typed)) {
                    ReiseLinje rl = new ReiseLinje(currentLinje);
                    matches[funnet] = rl;
                    funnet++;
                    funnetAllerede.add(rl);
                    break;
                }
            }
        }
        inn = GlobaleMetoder.hentFil("FINAL_ROUTES.txt", myContext);


        //soker saa etter linjenavn som starter med typed
        while (funnet < 5 && inn.hasNextLine()) {
            String currentLinje = inn.nextLine();
            String[] ar = currentLinje.split(",");
            if (ar[2].toLowerCase().startsWith(typed) && !funnetAllerede.contains(new ReiseLinje(currentLinje))) {
                ReiseLinje rl = new ReiseLinje(currentLinje);
                matches[funnet] = rl;
                funnet++;
                if (funnet == 5) return matches;
                funnetAllerede.add(rl);
            }
        }

        //soker naa etter linjenavn som innholder typed
        inn = GlobaleMetoder.hentFil("FINAL_ROUTES.txt", myContext);

        while (funnet < 5 && inn.hasNextLine()) {
            String currentLinje = inn.nextLine();
            String[] ar = currentLinje.split(",");
            if (ar[2].toLowerCase().contains(typed) && !funnetAllerede.contains(new ReiseLinje(currentLinje))) {
                ReiseLinje rl = new ReiseLinje(currentLinje);
                matches[funnet] = rl;
                funnet++;
                if (funnet == 5) return matches;
                funnetAllerede.add(rl);
            }
        }
        return matches;
    }
    */


    public TilkobletLinjeSingel[] linjerVedIndeksert(String stasjon, String tid, Date date, Context myContext) {

        boolean trengerOppdatering=false;

        String dag;
        String datoFormatert;
        //ordner først opp med datoer og dag
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dagKode = c.get(Calendar.DAY_OF_WEEK);
        switch (dagKode) {
            case Calendar.MONDAY:
                dag=myContext.getString(R.string.ma);
                break;

            case Calendar.TUESDAY:
                dag=myContext.getString(R.string.ti);;
                break;

            case Calendar.WEDNESDAY:
                dag=myContext.getString(R.string.on);
                break;

            case Calendar.THURSDAY:
                dag=myContext.getString(R.string.to);
                break;

            case Calendar.FRIDAY:
                dag=myContext.getString(R.string.fr);
                break;

            case Calendar.SATURDAY:
                dag=myContext.getString(R.string.lø);
                break;

            case Calendar.SUNDAY:
                dag=myContext.getString(R.string.sø);
                break;

            default:
                Log.e("kritisk feil", "feil i datosystemet!!!!!!!!");
                dag=myContext.getString(R.string.ma);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        datoFormatert = df.format(c.getTime());
        //datoer done


        String[] innTid = tid.split(":");
        int time = Integer.parseInt(innTid[0]);
        int minutt = Integer.parseInt(innTid[1]);

        //Finner forst stasjonIDen
        String stasjonID = null;
        String sone = null;
        int firstOccurance=-1;
        int lastOccurance=-1;
        VirituellFil inn = GlobaleMetoder.hentFil("FINAL_STOPS.txt", myContext);


        /*
        Log.e("kort", "start");
        while(inn.hasNextLine()){
            Log.d("spam", inn.nextLine());
        }
        Log.e("kort", "end");
        System.exit(0);
        */


        /*
        try {
            inn = new Scanner(GlobaleMetoder.hentFil("FINAL_STOPS.txt"));
        } catch (Exception e) {
            Log.e("min error", "FINAL_STOPS.txt ikke funnet 9785643");
            return null;
        }
        */
        while (inn.hasNextLine()) {
            String[] ar = inn.nextLine().split(",");
            if (ar[1].equals(stasjon)) {
                stasjonID = ar[0];
                if(!ar[2].equals("")) sone = ar[2];
                firstOccurance = Integer.parseInt(ar[3]);
                lastOccurance = Integer.parseInt(ar[4]);
                break;
            }
        }

        if (stasjonID == null) {
            Log.e("min error", "Error: Stoppet finnes ikke 114564");
            return null;
        }else{
            Log.e("flyt", "Funnet stoppID:" + stasjonID);
        }

        //Gaar til forste forekomst av stasjonen, og finner headeren om nødvendig
        inn = GlobaleMetoder.hentFil("FINAL_RUTETIDER.txt", myContext);
        inn.gotoIndex(firstOccurance);
        String currentLesteLinje = inn.nextLine();
        String[] ar = currentLesteLinje.split(",");
        String currentHeader=ar[1];
        if(currentHeader.equals("")){
            while(ar[1].equals("")){
                inn.goBack1();
                inn.goBack1();
                ar = inn.nextLine().split(",");
            }
            currentHeader=ar[1];
            inn.gotoIndex(firstOccurance);
            currentLesteLinje=inn.nextLine();
            ar=currentLesteLinje.split(",");
        }


        LinkedHashMap<String,String> currentSekvens = new LinkedHashMap<>(); //key=stasjon,int     , value= header
        ArrayList<TilkobletLinje> tilkobleteLinjer = new ArrayList<>();
        String currentTidLinje;
        Tid[] baseTider;
        ArrayList<Integer> intervaller = new ArrayList<>();
        String currentRuteID;

        hovedloop :
        while (inn.hasNextLine() && inn.holdiUnder(lastOccurance)) {
            currentSekvens.clear();
            intervaller.clear();
            if(!ar[1].equals("")) currentHeader = ar[1];
            String serviceID = ar[0].substring(ar[0].indexOf(".")+1, ar[0].length());
            VirituellFil serviceSjekk = GlobaleMetoder.hentFil("FINAL_SERVICE_IDS.txt", myContext);
            serviceSjekk.gotoIndex(Integer.parseInt(serviceID)-1);
            String[] sar = serviceSjekk.nextLine().split(",");


            //servicelogikk
            boolean aktiv = true; //bestemmes av date-excpeptions og enablet-rangen
            String startdato = sar[8];
            String enddato = sar[9];

            int starty = Integer.parseInt(startdato.substring(0,4));
            int startm = Integer.parseInt(startdato.substring(4,6));
            int startd = Integer.parseInt(startdato.substring(6,8));

            int endy = Integer.parseInt(enddato.substring(0,4));
            int endm = Integer.parseInt(enddato.substring(4,6));
            int endd = Integer.parseInt(enddato.substring(6,8));

            int datey = Integer.parseInt(datoFormatert.substring(0,4));
            int datem = Integer.parseInt(datoFormatert.substring(4,6));
            int dated = Integer.parseInt(datoFormatert.substring(6,8));

            if(!trengerOppdatering) {
                if (datey < starty) {
                    Log.e("flyt", "ignorerte en rute pga clash med startdate/enddate 1");
                    //aktiv = false;
                    trengerOppdatering = true;
                }
                if (aktiv && datem < startm && datey <= starty) {
                    Log.e("flyt", "ignorerte en rute pga clash med startdate/enddate 2");
                    //aktiv = false;
                    trengerOppdatering = true;
                }
                if (aktiv && dated < startd && datem <= startm && datey <= starty) {
                    Log.e("flyt", "ignorerte en rute pga clash med startdate/enddate 3");
                    //aktiv = false;
                    trengerOppdatering = true;
                }

                if (aktiv && datey > endy) {
                    Log.e("flyt", "ignorerte en rute pga clash med startdate/enddate 4");
                    //aktiv = false;
                    trengerOppdatering = true;
                }
                if (aktiv && datem > endm && datey >= endy) {
                    Log.e("flyt", "ignorerte en rute pga clash med startdate/enddate 5");
                    //aktiv = false;
                    trengerOppdatering = true;
                }
                if (aktiv && dated > endd && datem >= endm && datey >= endy) {
                    Log.e("flyt", "ignorerte en rute pga clash med startdate/enddate 6");
                    //aktiv = false;
                    trengerOppdatering = true;
                }
            }

            String exceptions = null;
            try{
                exceptions = sar[10];
            }catch (IndexOutOfBoundsException ex){}
            if(exceptions==null || exceptions.equals("")) exceptions = null;
            ArrayList<String> disabledDates = new ArrayList<>();
            if(exceptions!=null){
                if(exceptions.indexOf(" ")!=exceptions.lastIndexOf(" ")){
                    String[] not = exceptions.split(" ");
                    for(String s : not){
                        if(s.charAt(s.length()-1)=='2') disabledDates.add(s);
                    }
                }else{
                    if(exceptions.charAt(exceptions.length()-1)=='2') disabledDates.add(exceptions);
                }
            }

            for(String s : disabledDates){
                int disabledy = Integer.parseInt(s.substring(0,4));
                int disabledm = Integer.parseInt(s.substring(4,6));
                int disabledd = Integer.parseInt(s.substring(6,8));
                if(datey==disabledy && datem==disabledm && dated==disabledd){
                    Log.e("flyt", "ignorerte en rute pga excpetion dato y/m/d: " + disabledy + "/" + disabledm + "/" + disabledd);
                    aktiv = false;
                    break;
                }
            }

            boolean enabled=false;

            if(dag.equals("mandag")){
                if(sar[1].equals("1")) enabled=true;
            }else if(dag.equals("tirsdag")){
                if(sar[2].equals("1")) enabled=true;
            }else if(dag.equals("onsdag")){
                if(sar[3].equals("1")) enabled=true;
            }else if(dag.equals("torsdag")){
                if(sar[4].equals("1")) enabled=true;
            }else if(dag.equals("fredag")){
                if(sar[5].equals("1")) enabled=true;
            }else if(dag.equals("lørdag")){
                if(sar[6].equals("1")) enabled=true;
            }else if(dag.equals("søndag")){
                if(sar[7].equals("1")) enabled=true;
            }else{
                Log.e("kritisk error", "feil i dagsystemet");
            }

            if(!aktiv && enabled){
                Log.e("flyt", "overskrev enabled da aktiv==false");
                enabled = false;
            }

            if(!enabled){
                //ruten er ikke aktivert i dag og jeg looper til vi er kommet til neste rute
                if(inn.hasNextLine()){
                    currentLesteLinje=inn.nextLine();
                    ar=currentLesteLinje.split(",");
                    if(!ar[1].equals("")) currentHeader = ar[1];
                }else{
                    break hovedloop;
                }

                while(ar[0].equals("") && inn.hasNextLine()){
                    currentLesteLinje=inn.nextLine();
                    ar=currentLesteLinje.split(",");
                    if(!ar[1].equals("")) currentHeader = ar[1];
                }
                continue hovedloop;
            }

            //Da vi er kommet hit er ruten enablet for dagen

            currentRuteID = ar[0];
            currentTidLinje = currentLesteLinje;

            int makeUnik=0;
            while ( (ar[0].equals("") || ar[0].equals(currentRuteID)) && inn.hasNextLine() ) {
                if (!ar[1].equals("")) currentHeader = ar[1];
                currentSekvens.put(ar[2] + "," + (makeUnik++), currentHeader);
                if(currentSekvens.size()==1){
                    intervaller.add(-1);
                }else{
                    intervaller.add(Integer.parseInt(ar[3]));
                }
                currentLesteLinje=inn.nextLine();
                ar = currentLesteLinje.split(",");

            }
            //da vi er kommer hit har vi bygget opp sekvensen
            boolean inneHolderStasjonen = false;
            for(String s : currentSekvens.keySet()){
                if(s.substring(0, s.indexOf(",")).equals(stasjonID)){
                    inneHolderStasjonen=true;
                    break;
                }
            }
            if(!inneHolderStasjonen) continue  hovedloop;
            //nå er det sikkert at sekvensen inneholder stasjonen

            //PRINTTEST
            String printSekvens = null;
            for(String s : currentSekvens.keySet()){
                if(printSekvens==null){
                    printSekvens=s.substring(0, s.indexOf(","));
                }else{
                    printSekvens = printSekvens + "," + s.substring(0, s.indexOf(","));
                }
            }

            String printIntervaller = null;
            for(int i : intervaller){
                if(printIntervaller==null){
                    printIntervaller="" +i;
                }else{
                    printIntervaller = printIntervaller + "," + i;
                }
            }

            Log.e("sekvenshit", "sekvens:" + printSekvens);
            Log.e("sekvenshit", "tidlinje:" + currentTidLinje);
            Log.e("sekvenshit", "intervaller:" + printIntervaller);
            //PRINTTEST END

            int hitIndex = -1;
            int iter11=0;
            for(String s : currentSekvens.keySet()){
                if(stasjonID.equals(s.substring(0, s.indexOf(",")))){
                    hitIndex=iter11;
                    break;
                }
                iter11++;
            }
            if(hitIndex==(currentSekvens.keySet().size()-1)) continue hovedloop; //endestasjon
            baseTider = decompressTider(currentTidLinje);
            int tidFraBase=0;
            for(int i=1; i<=hitIndex; i++){
                tidFraBase=tidFraBase + intervaller.get(i);
            }
            /*
            Tid[] faktiskeTider = new Tid[baseTider.length];
            if(tidFraBase==0){
                faktiskeTider = baseTider;
            }else{
                for(int i=0; i<baseTider.length; i++){
                    faktiskeTider[i]=baseTider[i].tidEtter(tidFraBase);
                }
            }
            */
            String nesteStasjon="(Linjen starter her)";
            int iter44=0;
            for(String s : currentSekvens.keySet()){
                if(iter44==(hitIndex+1)){
                    nesteStasjon=s.substring(0, s.indexOf(","));
                }
                iter44++;
            }

            // new TilkobletLinje(String[] stasjonsFrekvens, String header, Tid[] baseTider, int intervall,
            //int[] intervaller, String nestestasjon, String ruteIDutenPunktum, String sone)
            String[] stasjonsFrekvens = new String[currentSekvens.size()];
            iter44=0;
            for(String s : currentSekvens.keySet()){
                stasjonsFrekvens[iter44++] = s.substring(0, s.indexOf(","));
            }
            String relevantHeader = currentSekvens.get(stasjonID + "," + hitIndex);
            String ruteIDutenPunktum = currentRuteID.substring(0, currentRuteID.indexOf("."));

            int[] arIntervaller = new int[intervaller.size()];
            for(int i=0; i<intervaller.size(); i++){
                arIntervaller[i]=intervaller.get(i);
            }

            TilkobletLinje tkl = new TilkobletLinje(stasjonsFrekvens, relevantHeader, baseTider,
                    tidFraBase, arIntervaller, nesteStasjon, ruteIDutenPunktum, sone);
            tilkobleteLinjer.add(tkl);


        }
        //oppretter singels og sorterer dem , HashSet for aa fjerne identiske
        LinkedHashSet<TilkobletLinjeSingel> singelsHS = new LinkedHashSet<>();
        for(TilkobletLinje tkl : tilkobleteLinjer){
            for(Tid t : tkl.avganger){
                singelsHS.add(new TilkobletLinjeSingel(t, tkl, new TidCustomSort(t.getTidString())));
            }
        }

        ArrayList<TilkobletLinjeSingel> singels = new ArrayList<>();
        singels.addAll(singelsHS);
        Collections.sort(singels);

        //skriver om til array
        TilkobletLinjeSingel[] nice = new TilkobletLinjeSingel[singels.size()];
        for (int i = 0; i < singels.size(); i++) {
            nice[i] = singels.get(i);
        }

        //finner fulle ruteIDer
        inn = GlobaleMetoder.hentFil("FINAL_RUTETRANSLATOR.txt", myContext);
        int ruterToTranslate = tilkobleteLinjer.size();
        int translatedRuter=0;
        while(translatedRuter<ruterToTranslate && inn.hasNextLine() ){
            ar = inn.nextLine().split(",");
            for(TilkobletLinje tkl : tilkobleteLinjer){
                if(tkl.setFullRuteID(ar[1], ar[0], Integer.parseInt(ar[2]))){
                    translatedRuter++;
                }
            }
        }
        if(inn.hasNextLine()) Log.e("flyt", "breaket oversetting tidlig 1/3");


        //finner fulle rutenavn ved hjelp av IDene
        inn = GlobaleMetoder.hentFil("FINAL_ROUTES.txt", myContext);
        translatedRuter=0;
        while(translatedRuter<ruterToTranslate && inn.hasNextLine() ){
            ar = inn.nextLine().split(",");
            for(TilkobletLinje tkl : tilkobleteLinjer){
                if(tkl.setFullRuteNavn(ar[0], ar[1], ar[2], ar[3])){
                    translatedRuter++;
                }
            }
        }
        if(inn.hasNextLine()) Log.e("flyt", "breaket oversetting tidlig 2/3");

        //oversetter stasjonsnavn
        inn = GlobaleMetoder.hentFil("FINAL_STOPS.txt", myContext);
        for(TilkobletLinje tkl : tilkobleteLinjer){
            tkl.oversettAlleStasjoner(inn);
        }
        /*
        int stasjonerToTranslate=0;
        int translatedStasjoner=0;
        for(TilkobletLinje tkl : tilkobleteLinjer){
            stasjonerToTranslate = stasjonerToTranslate + tkl.stasjonerTilOversetting();
        }
        Log.e("flyt", "fant ut at hvor mange stasjoner det er aa translate: "  +stasjonerToTranslate);

        inn = GlobaleMetoder.hentFil("FINAL_STOPS.txt", myContext);

        while(translatedStasjoner<stasjonerToTranslate && inn.hasNextLine()){
            ar = inn.nextLine().split(",");
            for(TilkobletLinje tkl : tilkobleteLinjer){
                if(ar.length==2){
                    translatedStasjoner = translatedStasjoner + tkl.setStasjonNavn(ar[0], ar[1], null);
                }else{
                    translatedStasjoner = translatedStasjoner + tkl.setStasjonNavn(ar[0], ar[1], ar[2]);
                }
            }
        }
        if(inn.hasNextLine()) Log.e("flyt", "breaket oversetting tidlig 3/3");
        */
        Log.e("flyt", "stasjon oversetting ferdig 3/3");

        for(TilkobletLinje tkl : tilkobleteLinjer){
            tkl.initGUIVerdier();
        }

        //**PRINT TEST
        for(TilkobletLinje tkl : tilkobleteLinjer){
            Log.e("TKL printtest start", "oppslagsruteID:" + tkl.ruteIDutenPunktum );
            Log.e("TKL printtest", "header:" + tkl.header );
            Log.e("TKL printtest", "fulltRutenavn:" + tkl.fullRuteNavn );
            String builderID = null;
            String builderNavn = null;
            for(Stasjon s : tkl.stasjonsFrekvens){
                if(builderID==null){
                    builderID=s.id;
                }else{
                    builderID= builderID + "," + s.id;
                }

                if(builderNavn==null){
                    builderNavn="" + s.navn;
                }else{
                    builderNavn= builderNavn + "," + s.navn;
                }
            }
            Log.e("TKL printtest", "sekvensID:" + builderID );
            Log.e("TKL printtest", "sekvensNavn:" + builderNavn );
            Log.e("TKL printtest slutt", "neste stasjon:" + tkl.nesteStasjon);
        }

        //**PRINT TEST END


        //naa er diverse oversetting ferdig og arrayen kan returneres
        /*
        if(trengerOppdatering){
            String txt = myContext.getString(R.string.update);
            Toast.makeText(myContext, txt, Toast.LENGTH_LONG).show();
        }
        */

        return nice;
    }








    public Stasjon[] visLinje(String stasjon, String header, Context myContext) {
        VirituellFil inn = GlobaleMetoder.hentFil("FINAL_RUTETIDER.txt", myContext);
        /*
        try {
            inn = new Scanner(GlobaleMetoder.hentFil("FINAL_RUTETIDER.txt"));
        } catch (Exception e) {
            Log.e("min error", "FINAL_RUTETIDER.txt ikke funnet 8930");
            return null;
        }
        */

        //samler alle frekvensene som inneholder header og stasjonen
        //Log.e("min error", "fikk inn " + stasjon + " " + header);
        Set<Sekvens> reiser = new HashSet<>();
        ArrayList<String> linjerUnderSammeRuteID = new ArrayList<>();
        String currentLinje = inn.nextLine();
        String[] ar = currentLinje.split(",");
        String currentRuteID = ar[0];
        String currentHeader = ar[1];
        String sekvens = null;
        boolean first = true;
        String forrigeHeader = null;
        String preHeader = null;
        while (inn.hasNextLine()) {
            linjerUnderSammeRuteID.clear();
            first = true;
            preHeader = null;
            while ((ar[0].equals(currentRuteID) || ar[0].equals(""))
                    && inn.hasNextLine()) {
                if (first) {
                    if (forrigeHeader != null && ar[1].equals("")) {
                        preHeader = forrigeHeader;
                        //if(currentLinje.equals("5,,402,06:47,19:02,02 17 32 47,,")){
                        //Log.e("min error", "ph: " + preHeader);
                        //}
                    }
                } else {
                    first = false;
                }
                linjerUnderSammeRuteID.add(currentLinje);
                currentLinje = inn.nextLine();
                ar = currentLinje.split(",");
                if (!ar[1].equals("")) {
                    forrigeHeader = currentHeader;
                    currentHeader = ar[1];
                }
            }

            currentRuteID = ar[0];
            if (inn.hasNextLine() && currentRuteID.equals("")) {
                Log.e("min error", "Error_74522");
               // System.exit(0);
            }

            if (!ar[1].equals("")) {
                forrigeHeader = currentHeader;
                currentHeader = ar[1];
            }


            LinjeSamling ls = null;
            if (preHeader == null) {
                ls = new LinjeSamling(linjerUnderSammeRuteID);
            } else {
                ls = new LinjeSamling(linjerUnderSammeRuteID, preHeader);
            }
        /*
        if(currentLinje.equals("6,Helsfyr,987,05:43,23:28,13 28 43 58,,")){
          Log.e("min error", "herbro");
          Log.e("min error", ls.inneholderStasjonOgHeader(stasjon, header));
          Log.e("min error", ls.preHeader);
        }
        */


            if (ls.inneholderStasjonOgHeader(stasjon, header)) {
                //Log.e("min error", "\nFant en sekvens som innholder h og s");
                //Log.e("min error", "Forste linje: " + linjerUnderSammeRuteID.get(0));
                reiser.add(ls.lagSekvens());
            }
        }
        Sekvens lengst = null;
        int lengste = 0;
        for (Sekvens s : reiser) {
            if (s.stasjoner.length > lengste) {
                lengst = s;
                lengste = s.stasjoner.length;
            }
        }
        lengst.oversett(myContext);
        return lengst.oversatt;


    }

    //public RuteTid genererRuteTider(String rutenavn, String dag, Context myContext){
    public RuteTid genererRuteTider(String rutenavn, Date date, Context myContext){

        boolean trengerOppdatering=false;

        String dag;
        String datoFormatert;
        //ordner først opp med datoer og dag
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dagKode = c.get(Calendar.DAY_OF_WEEK);
        switch (dagKode) {
            case Calendar.MONDAY:
                dag=myContext.getString(R.string.ma);
                break;

            case Calendar.TUESDAY:
                dag=myContext.getString(R.string.ti);;
                break;

            case Calendar.WEDNESDAY:
                dag=myContext.getString(R.string.on);
                break;

            case Calendar.THURSDAY:
                dag=myContext.getString(R.string.to);
                break;

            case Calendar.FRIDAY:
                dag=myContext.getString(R.string.fr);
                break;

            case Calendar.SATURDAY:
                dag=myContext.getString(R.string.lø);
                break;

            case Calendar.SUNDAY:
                dag=myContext.getString(R.string.sø);
                break;

            default:
                Log.e("kritisk feil", "feil i datosystemet!!!!!!!!");
                dag=myContext.getString(R.string.ma);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        datoFormatert = df.format(c.getTime());
        //datoer done


        VirituellFil inn = GlobaleMetoder.hentFil("FINAL_ROUTES.txt", myContext);

        String[] ar = rutenavn.split(" ");
        String innID = ar[0];
        String cleanNavn="";
        String twist =null;
        for(int i=1; i<ar.length; i++){
            cleanNavn = cleanNavn + " " + ar[i];
        }
        Log.e("namecheck", "cleannavn pre:" + cleanNavn + ".");
        cleanNavn = GlobaleMetoder.safeFormatRuteNavn(cleanNavn);
        Log.e("namecheck", "cleannavn post:" + cleanNavn + ".");
        twist = GlobaleMetoder.twistRuteNavn(cleanNavn);

        Log.e("flyt", "innID:" + innID);
        Log.e("flyt", "cleannavn:" + cleanNavn);
        Log.e("flyt", "twist:" + twist);

        String minGenRuteID=null;
        int retning=-1;
        int ruteType=-1;
        while(inn.hasNextLine()){
            String[] arr = inn.nextLine().split(",");
            String cleanCheck = GlobaleMetoder.safeFormatRuteNavn(arr[2]);
            if(arr[1].equals(innID) && (cleanCheck.equals(cleanNavn) || cleanCheck.equals(twist)) ){

                if(arr[4].equals("0")){
                    retning=2;
                }else if(cleanCheck.equals(cleanNavn)){
                    retning=1;
                }else{
                    retning=0;
                }
                minGenRuteID=arr[0];
                ruteType=Integer.parseInt(arr[3]);
                break;
            }
        }

        Log.e("flyt", "gen rute ID:" + minGenRuteID);
        Log.e("flyt", "retning:" + retning);

        //Har nå funnet ruteIDen i routes.txt. Finner videre mineruteIDer (indexer) i translator

        ArrayList<Integer> alleRuteIndekser = new ArrayList<>();
        inn = GlobaleMetoder.hentFil("FINAL_RUTETRANSLATOR.txt" , myContext);
        overloop:
        while(inn.hasNextLine()){
            String[] arr = inn.nextLine().split(",");
            if(arr[0].equals(minGenRuteID)){
                while(arr[0].equals(minGenRuteID)){
                    if(Integer.parseInt(arr[2])==retning || retning==2){
                        alleRuteIndekser.add(Integer.parseInt(arr[3]));
                        Log.e("flyt", "fant en relevant minRuteID:" + arr[1] + " med index:" + arr[3]);
                    }
                    arr = inn.nextLine().split(",");
                }
                break overloop;
            }
        }

        //Har nå alle ruteindexene. Iterer gjennom dem og bygger opp sekvenser samt henter info om tidene

        inn = GlobaleMetoder.hentFil("FINAL_RUTETIDER.txt" , myContext);
        LinkedHashMap<String,Tid[]> info = new LinkedHashMap<>(); //key="minRuteID,Stasjon,header,iter(random) int" , value=avganger
        ArrayList<String> sekvenser = new ArrayList<>();
        String build="";
        String currentID=null;
        boolean firstinit=true;
        int iter=0;

        indexIter:
        for(int index : alleRuteIndekser){
            //sjekker først om ruten går den dagen vi har valgt
            inn.gotoIndex(index);
            String rawLinje = inn.nextLine();
            Log.e("rawLinje ved index:" + index , rawLinje);
            ar = rawLinje.split(",");
            if(!ar[0].equals("")) currentID=ar[0];
            int serviceID = Integer.parseInt( ar[0].substring(ar[0].indexOf(".")+1, ar[0].length()) );
            VirituellFil serviceChecker = GlobaleMetoder.hentFil("FINAL_SERVICE_IDS.txt", myContext);
            serviceChecker.gotoIndex((serviceID-1));
            String[] sar = serviceChecker.nextLine().split(",");
            String startdato = sar[8];
            String enddato = sar[9];


            int starty = Integer.parseInt(startdato.substring(0,4));
            int startm = Integer.parseInt(startdato.substring(4,6));
            int startd = Integer.parseInt(startdato.substring(6,8));

            int endy = Integer.parseInt(enddato.substring(0,4));
            int endm = Integer.parseInt(enddato.substring(4,6));
            int endd = Integer.parseInt(enddato.substring(6,8));

            int datey = Integer.parseInt(datoFormatert.substring(0,4));
            int datem = Integer.parseInt(datoFormatert.substring(4,6));
            int dated = Integer.parseInt(datoFormatert.substring(6,8));

            if(datey < starty){
                Log.e("flyt", "ignorerte " + currentID + " pga clash med startdate/enddate 1");
                //continue  indexIter;
                trengerOppdatering=true;
            }
            if(datem < startm && datey<=starty){
                Log.e("flyt", "ignorerte " + currentID + " pga clash med startdate/enddate 2");
                //continue  indexIter;
                trengerOppdatering=true;
            }
            if(dated< startd && datem<=startm && datey<=starty){
                Log.e("flyt", "ignorerte " + currentID + " pga clash med startdate/enddate 3");
                //continue  indexIter;
                trengerOppdatering=true;
            }

            if(datey > endy){
                Log.e("flyt", "ignorerte " + currentID + " pga clash med startdate/enddate 4");
                //continue  indexIter;
                trengerOppdatering=true;
            }
            if(datem > endm && datey>=endy){
                Log.e("flyt", "ignorerte " + currentID + " pga clash med startdate/enddate 5");
                //continue  indexIter;
                trengerOppdatering=true;
            }
            if(dated > endd && datem>=endm && datey>=endy){
                Log.e("flyt", "ignorerte " + currentID + " pga clash med startdate/enddate 6");
                //continue  indexIter;
                trengerOppdatering=true;
            }


            String exceptions = null;
            try{
                exceptions = sar[10];
            }catch (IndexOutOfBoundsException ex){}
            if(exceptions==null || exceptions.equals("")) exceptions = null;
            ArrayList<String> disabledDates = new ArrayList<>();
            if(exceptions!=null){
                if(exceptions.indexOf(" ")!=exceptions.lastIndexOf(" ")){
                    String[] not = exceptions.split(" ");
                    for(String s : not){
                        if(s.charAt(s.length()-1)=='2') disabledDates.add(s);
                    }
                }else{
                    if(exceptions.charAt(exceptions.length()-1)=='2') disabledDates.add(exceptions);
                }
            }

            for(String s : disabledDates){
                int disabledy = Integer.parseInt(s.substring(0,4));
                int disabledm = Integer.parseInt(s.substring(4,6));
                int disabledd = Integer.parseInt(s.substring(6,8));
                if(datey==disabledy && datem==disabledm && dated==disabledd){
                    Log.e("flyt", "ignorerte "  + currentID + " pga excpetion dato y/m/d: " + disabledy + "/" + disabledm + "/" + disabledd);
                    continue  indexIter;
                }
            }




            boolean enabled=false;
            if(dag.equals("mandag")){
                if(sar[1].equals("1")) enabled=true;
            }else if(dag.equals("tirsdag")){
                if(sar[2].equals("1")) enabled=true;
            }else if(dag.equals("onsdag")){
                if(sar[3].equals("1")) enabled=true;
            }else if(dag.equals("torsdag")){
                if(sar[4].equals("1")) enabled=true;
            }else if(dag.equals("fredag")){
                if(sar[5].equals("1")) enabled=true;
            }else if(dag.equals("lørdag")){
                if(sar[6].equals("1")) enabled=true;
            }else if(dag.equals("søndag")){
                if(sar[7].equals("1")) enabled=true;
            }else{
                Log.e("kritisk error", "feil i dagsystemet");
            }
            if(!enabled) continue indexIter;

            //har gjort oss sikre på at ruten er enablet for dagen

            //finner naa headeren
            String currentHeader=null;
            if(!ar[1].equals("")){
                currentHeader=ar[1];
            }else{
                while(ar[1].equals("")){
                    ar = inn.goBack1().split(",");
                }
                currentHeader=ar[1];
            }
            inn.gotoIndex(index+1); //resetter etter søk. også safe om søk ikke trigget
            ar=rawLinje.split(",");
            currentID=ar[0];
            Tid[] mainTider =decompressTider(rawLinje);
            int tidMellom=0;
            info.put(currentID + "," + ar[2] + "," + currentHeader + "," + (iter++), mainTider);
            build=ar[2];
            Log.e("min error", "linjen indexering sendte meg til:" + rawLinje);

            rawLinje=inn.nextLine();
            ar=rawLinje.split(",");

            //har garantert headeren. Nå iterer jeg til jeg treffer en ny rute eller enden av fila
            //info : key="minRuteID,Stasjon,header,iter(random) int" , value=avganger
            while(ar[0].equals("") && inn.hasNextLine()){
                if(!ar[1].equals("")){
                    currentHeader=ar[1];
                }
                tidMellom = tidMellom + Integer.parseInt(ar[3]);
                info.put(currentID + "," + ar[2] + "," + currentHeader + "," + (iter++), genererTider(mainTider, tidMellom));
                if(build.equals("")){
                    build= ar[2];
                }else{
                    build = build + "," + ar[2];
                }

                rawLinje = inn.nextLine();
                ar = rawLinje.split(",");
            }

            if(!inn.hasNextLine()){ //sikrer mot end-of-file faults
                if(!ar[1].equals("")){
                    currentHeader=ar[1];
                }
                info.put(currentID + "," + ar[2] + "," + currentHeader + "," + (iter++), decompressTider(rawLinje));
                if(build.equals("")){
                    build= ar[2];
                }else{
                    build = build + "," + ar[2];
                }
            }

            sekvenser.add(build);
            build="";
        }

        //Har nå samlet alle sekvenser og all info i info-hashmappet

                    //--------for print
                    for(String s : info.keySet()){
                        Tid[] tar = info.get(s);
                        String builder=null;
                        for(Tid t : tar){
                            if(builder==null){
                                builder=t.toString();
                            }else{
                                builder = builder + "," + t.toString();
                            }
                        }
                        String fixedlengde= String.format("%1$"+25+ "s", s);
                        Log.e("info", fixedlengde + ":: " + builder);
                    }
                    //-------END


        //finner hovedsekvens
        int max=0;
        String hovedSekvens=null;
        for(String s : sekvenser){
            String[] fefe = s.split(",");
            if(fefe.length > max){
                hovedSekvens=s;
                max=fefe.length;
            }
        }

        if(hovedSekvens==null){
            //Det finnes ingen reiser ved denne linja denne dagen
            Log.e("flyt", "Det finnes ingen reiser ved denne linja denne dagen");
            return null;
        }

        Log.e("flyt", "hovedsekvens:" + hovedSekvens);
        for(String s : sekvenser){
            if(!s.equals(hovedSekvens)) Log.e("flyt", "undersekvens:" + s);
        }

        ArrayList<String> stasjonListe = new ArrayList<>();
        for(String s : hovedSekvens.split(",")){
            stasjonListe.add(s);
        }
        ArrayList<String> undersekListe = new ArrayList<>();
        for(String s : sekvenser){
            if(!hovedSekvens.equals(s) && !undersekListe.contains(s)) undersekListe.add(s);
        }
        if(undersekListe.size()!=0){
            String[] underSekvenser = new String[undersekListe.size()];
            for(int i=0; i<undersekListe.size(); i++){
                underSekvenser[i]=undersekListe.get(i);
            }
            simpelSekvensBygger(stasjonListe, underSekvenser); //gjor om stasjonListe til en generell sekvens
        }

        String forPrint=null;
        for(String s : stasjonListe){
            if(forPrint==null){
                forPrint=s;
            }else{
                forPrint=forPrint + "," + s;
            }
        }

        if(hovedSekvens.equals(forPrint)){
            Log.e("flyt", "hovedfrekvensen måtte ikke tilpasses");
        }else{
            Log.e("flyt", "tilpasset hovedfrekvens:" + forPrint);
        }


        //lager et par array for behandlig av multiple stasjoner i samme frekvens
        int[] par = new int[stasjonListe.size()];
        HashMap<String, Integer> besokte = new HashMap<>(); //key = stasjon, value= parID
        int parID=0;
        for(int i = 0; i<stasjonListe.size(); i++){
            String s = stasjonListe.get(i);
            if(besokte.keySet().contains(s)){
                par[i]=besokte.get(s);
            }else{
                besokte.put(s, parID++);
                par[i]=parID-1;
            }

        }

        //leter etter en common stasjon
        int commonIndex=-1;
        global :
        for(String s : stasjonListe){

            int funnet=0;
            national :
            for(String s1 : sekvenser){
                String[] arr = s1.split(",");
              local:
              for(String iarr : arr) {
                    if (iarr.equals(s)) {
                        funnet++;
                        break local;
                    }
                }
            }
            if(funnet==sekvenser.size()){
                commonIndex=stasjonListe.indexOf(s);
                break global;
            }
        }
        if(commonIndex==-1){
            Log.e("flyt", "fant ikke en common stasjon");
        }else{
            Log.e("flyt", "common stasjonID:" + stasjonListe.get(commonIndex));
            String[] translater = GlobaleMetoder.getStopArray();
            int i = Integer.parseInt(stasjonListe.get(commonIndex));
            Log.e("flyt", "common stasjon:" + translater[i]);
        }


        //oppretter tidkolonner
        //LinkedHashMap<String,Tid[]> info = new LinkedHashMap<>();  //key="minRuteID,Stasjon,header,int" , value=avganger
        ArrayList<TidKolonne> tidKs =new ArrayList<>();
        ArrayList<TidKolonne> lokaltidKs =new ArrayList<>();
        String currentRuteID=null;
        boolean first=true;
        for(String s : info.keySet()){
            String[] arr = s.split(",");
            String stasjon = arr[1];
            int plass=stasjonListe.indexOf(stasjon);

            if(plass==-1){
                Log.e("dårlig sekvenssetting", "en stasjon finnes ikke i sekvensen. Skipper den");
                continue;

            }

            if(currentRuteID==null || !currentRuteID.equals(arr[0])){
                currentRuteID = arr[0];
                first = true;
                if(!lokaltidKs.isEmpty()){
                    tidKs.addAll(lokaltidKs);
                    lokaltidKs.clear();
                }
            }

            Tid[] tider = info.get(s);
            for(int i =0; i<tider.length; i++){
                Tid t = tider[i];
                //if(t==null) Log.e("null", "null brur");
                if(first){
                    TidKolonne tk = new TidKolonne(stasjonListe.size(), par, commonIndex, arr[2]);
                    tk.settInnTid(t, plass);
                    lokaltidKs.add(tk);
                }else{
                    //Log.e("flyt", "setter inn der dette er fra før:" + lokaltidKs.get(i).tider[plass] );
                    lokaltidKs.get(i).settInnTid(t, plass);
                }
            }
            first=false;
        }
        tidKs.addAll(lokaltidKs);

        LinkedHashSet<TidKolonne> setter = new LinkedHashSet<>(); //fjerner unike
        setter.addAll(tidKs);
        tidKs.clear();
        tidKs.addAll(setter);
        setter=null;

        Collections.sort(tidKs);
        //siste step: går gjennom tidkolonnene og fjerner de som er nesten like (eneste forskjell er at den ene
        // har noen tider nulla og vi ikke har overlap)
        shaveOff(tidKs);


        TidKolonne[] finalT = new TidKolonne[tidKs.size()];
        for(int i = 0; i<tidKs.size(); i++){
            finalT[i]=tidKs.get(i);
            finalT[i].finnEndestasjonIndex();
        }


        String[] translater = GlobaleMetoder.getStopArray();
        String[] translated = new String[stasjonListe.size()];
        int iter2=0;
        for(String s : stasjonListe){
            int i = Integer.parseInt(s);
            translated[iter2++] = translater[i];
        }
        /*
        if(trengerOppdatering){
            String txt = myContext.getString(R.string.update);
            Toast.makeText(myContext, txt, Toast.LENGTH_LONG).show();
        }
        */


        return new RuteTid(translated, finalT, ruteType);
    }

    //siste step i genererRutetid
    private void shaveOff(ArrayList<TidKolonne> kolonner){

        for(int i= kolonner.size() -1 ; i>=1; i--){
            //Log.e("shaveOff", "i er 1 da vi er ferdig. Naa er i: " + i);
            TidKolonne siste = kolonner.get(i);
            TidKolonne forste = kolonner.get(i-1);
            boolean fjernEn=true;
            int indexToFjerne = -1;
            TidKolonne flestNuller=null;
            //sjekker tidene opp mot hverandre
            for(int jj=0; jj<forste.tider.length; jj++){
                Tid par = forste.tider[jj];
                Tid maker = siste.tider[jj];
                if(par==null && maker==null) continue;
                if(par==null){
                    if(flestNuller==null || flestNuller==forste){
                        flestNuller=forste;
                        indexToFjerne=(i-1);
                        continue;
                    }else{
                        //flestnuller har "bytta" og dermed skal ingen fjernes
                        fjernEn=false;
                        break;
                    }
                }
                if(maker==null){
                    if(flestNuller==null || flestNuller==siste){
                        flestNuller=siste;
                        indexToFjerne=i;
                        continue;
                    }else{
                        //flestnuller har "bytta" og dermed skal ingen fjernes
                        fjernEn=false;
                        break;
                    }
                }
                //ingen av tidene er null
                if(!par.equals(maker)){
                    fjernEn=false;
                    break;
                }

            }
            if(fjernEn){
                Log.e("shaveOff", "Fjerner en tidkolonne");
                kolonner.remove(indexToFjerne);
            }


        }
    }







    //Lager en ny stasjonsfrekvens som inneholder alle stasjoner
    private void simpelSekvensBygger(ArrayList<String> hovedSekvens, String[] underSekvenser){

        //Lager en ny stasjonsfrekvens som inneholder alle stasjoner
        ArrayList<String> alleredeLagtTil = new ArrayList<>(); //bare ideen til de som er lagt til
        int iterMainLoop=-1;
        for(String currentUnderSekvens : underSekvenser){
            iterMainLoop++;
            ArrayList<Unik> stasjonerIkkeIHovedSekvensen = new ArrayList<>();
            HashSet<String> alleredeLagtTilLocal = new HashSet<>(); //alleredeLagtTil lokalt
            String[] arr = currentUnderSekvens.split(",");
            String forrige=null;
            for(int i=0; i<arr.length; i++){
                String sta = arr[i];
                String neste = null;
                try{
                    neste=arr[i+1];
                }catch(IndexOutOfBoundsException e){}
                if(!hovedSekvens.contains(sta) && !alleredeLagtTil.contains(sta)){
                    stasjonerIkkeIHovedSekvensen.add( new Unik(forrige, neste, sta));
                    Log.e("sekvenslogikk","funnet outside hovedsek:" + sta + ". med forrige:" + forrige + ". og neste:" + neste + ".");
                }
                forrige=sta;
            }

            if(stasjonerIkkeIHovedSekvensen.isEmpty()) continue;
            if(stasjonerIkkeIHovedSekvensen.get(stasjonerIkkeIHovedSekvensen.size()-1).bak != null){
                for(int i =stasjonerIkkeIHovedSekvensen.size()-1 ; i>=0 ; i--){
                    Unik iter =stasjonerIkkeIHovedSekvensen.get(i);
                    if(iter.bak!=null  && hovedSekvens.contains(iter.bak)){
                        hovedSekvens.add(hovedSekvens.indexOf(iter.bak) , iter.id);
                        Log.e("sekvenslogikk","opt1 la til: " + iter.id + " for loop " + iterMainLoop);
                        alleredeLagtTilLocal.add(iter.id);
                        continue;
                    }
                    //skjer med uflaks (multiple grupper ukjente per sekvens).
                    if(iter.foran!=null && hovedSekvens.contains(iter.foran)){
                        hovedSekvens.add(hovedSekvens.indexOf(iter.foran)+1 , iter.id);
                        Log.e("sekvenslogikk"," (bad) opt2 la til: " + iter.id + " for loop " + iterMainLoop);
                        alleredeLagtTilLocal.add(iter.id);
                        continue;
                    }
                    //er vi kommet hit har jeg problemer. Velger aa ikke legge til ideen,
                    //da det krever en del mer logikk aa finne en fornuftig plass
                    Log.e("sekvenslogikk","ERROR - klarte ikke aa bestemme posisjon til en unikID i sekvenslogikken #1");

                }
            }else if(stasjonerIkkeIHovedSekvensen.get(0).foran != null){
                for(int i =0 ; i<stasjonerIkkeIHovedSekvensen.size(); i++){
                    Unik iter =stasjonerIkkeIHovedSekvensen.get(i);
                    if(iter.foran!=null && hovedSekvens.contains(iter.foran)){
                        hovedSekvens.add(hovedSekvens.indexOf(iter.foran)+1 , iter.id);
                        Log.e("sekvenslogikk","opt3 la til: " + iter.id + " for loop " + iterMainLoop);
                        alleredeLagtTilLocal.add(iter.id);
                        continue;
                    }
                    //skjer med uflaks (multiple grupper ukjente per sekvens).
                    if(iter.bak!=null && hovedSekvens.contains(iter.bak)){
                        hovedSekvens.add(hovedSekvens.indexOf(iter.bak) , iter.id);
                        Log.e("sekvenslogikk","opt4 la til: " + iter.id + " for loop " + iterMainLoop);
                        alleredeLagtTilLocal.add(iter.id);
                        continue;
                    }
                    //er vi kommet hit har jeg problemer. Velger aa ikke legge til ideen,
                    //da det krever en del mer logikk aa finne en fornuftig plass
                    Log.e("sekvenslogikk","klarte ikke aa bestemme posisjon til en unikID i sekvenslogikken #2");
                }
            }else{
                //er vi kommet hit har jeg problemer. Velger aa ikke legge til ideen,
                //da det krever en del mer logikk aa finne en fornuftig plass
                //Log.e("sekvenslogikk","klarte ikke aa bestemme posisjon til en unikID i sekvenslogikken #3");
                for(int i =stasjonerIkkeIHovedSekvensen.size()-1 ; i>=0 ; i--){
                    Unik iter =stasjonerIkkeIHovedSekvensen.get(i);
                    if(iter.bak!=null  && hovedSekvens.contains(iter.bak)){
                        hovedSekvens.add(hovedSekvens.indexOf(iter.bak) , iter.id);
                        Log.e("sekvenslogikk","opt ###BAD1 la til: " + iter.id + " for loop " + iterMainLoop);
                        alleredeLagtTilLocal.add(iter.id);
                        continue;
                    }
                    //skjer med uflaks (multiple grupper ukjente per sekvens).
                    if(iter.foran!=null && hovedSekvens.contains(iter.foran)){
                        hovedSekvens.add(hovedSekvens.indexOf(iter.foran)+1 , iter.id);
                        Log.e("sekvenslogikk"," (bad) ###BAD2 opt2 la til: " + iter.id + " for loop " + iterMainLoop);
                        alleredeLagtTilLocal.add(iter.id);
                        continue;
                    }
                    //er vi kommet hit har jeg problemer. Velger aa ikke legge til ideen,
                    //da det krever en del mer logikk aa finne en fornuftig plass
                    Log.e("sekvenslogikk","ERROR - klarte ikke aa bestemme posisjon til en unikID i sekvenslogikken #1");

                }


            }

            alleredeLagtTil.addAll(alleredeLagtTilLocal);
            //Log.e("sekvenslogikk",("kommet til end for loop " + iterMainLoop);

        }
        //fjerner like elementer som kommer direkte etter hverandre
        int size = hovedSekvens.size();
        for (int i = size - 1; i >= 1; i--) {
            if (hovedSekvens.get(i).compareTo(hovedSekvens.get(i - 1)) == 0) {
                hovedSekvens.remove(i);
            }
        }

    }




    private Tid[] genererTider(Tid[] base, int intervall){
        Tid[] gen = new Tid[base.length];
        for(int i=0; i<base.length; i++){
            gen[i]=base[i].tidEtter(intervall);
        }
        return gen;
    }



    private Tid[] decompressTider(String linje){

        Set<Tid> unike = new HashSet<>();
        String[] ar =linje.split(",");

        //Finner alle avganger
        Tid minTid=new Tid(ar[3]);
        Tid maxTid=new Tid(ar[4]);
        int minTime= minTid.time;
        int maxTime= maxTid.time;

        //Legger forst til start og slutt-tidene
        unike.add(minTid);
        unike.add(maxTid);

        //legger alle tider beskrevet av the pattern
        String[] commons=null;
        try{
            if(!ar[5].equals("")){
                String[] arr = ar[5].split(" ");
                //Log.e("min error", "ar5 test: " + ar[5]);
                commons=new String[arr.length];
                for(int i=0;i<arr.length;i++){
                    commons[i]=arr[i];
                    //Log.e("min error", "commons: setter inn: " + arr[i]);
                }
            }
        }catch(Exception e){}
        if(commons==null || commons.length==0){
            commons=null;
        }

        if(commons!=null){
            for(int i=minTime;i<=maxTime;i++){
                for(String s : commons){
                    if(i<10){
                        //  Log.e("min error", "4lagde ny tid med input " + "0" +i + ":" + s);
                        Tid t = new Tid("0" +i + ":" + s);
                        if(t.compareTo(minTid)>0 && t.compareTo(maxTid)<0) unike.add(t);
                    }else{
                        //Log.e("min error", "5lagde ny tid med input "  +i + ":" + s);
                        Tid t = new Tid("" +i + ":" + s);
                        if(t.compareTo(minTid)>0 && t.compareTo(maxTid)<0) unike.add(t);
                    }

                }
            }
        }
        //REMINDER: (first,last,commons,extra,doesntExist)

        //legger til extra tider
        Tid[] extra=null;
        try{
            if(!ar[6].equals("")){
                String[] arr = ar[6].split(" ");
                extra=new Tid[arr.length];
                for(int i=0;i<arr.length;i++){
                    //Log.e("min error", "test " + ar[6]);
                    //Log.e("min error", "6lagde ny tid med input " + arr[i]);
                    extra[i]=new Tid(arr[i]);
                }
            }
        }catch(Exception e){}
        if(extra==null || extra.length==0){
            extra=null;
        }
        if(extra!=null){
            for(Tid t : extra){
                unike.add(t);
            }
        }

        //fjerner overflodige tider
        Tid[] doesntExist=null;
        try{
            if(!ar[7].equals("")){
                String[] arr = ar[7].split(" ");
                doesntExist=new Tid[arr.length];
                for(int i=0;i<arr.length;i++){
                    //Log.e("min error", "7lagde ny tid med input " + arr[i]);
                    doesntExist[i]=new Tid(arr[i]);
                }
            }
        }catch(Exception e){}
        if(doesntExist==null || doesntExist.length==0){
            doesntExist=null;
        }
        if(doesntExist!=null){
            for(Tid t : doesntExist){
                //Log.e("min error", "fjerner");
                unike.remove(t);
            }
        }

        ArrayList<Tid> sortert = new ArrayList<>();
        sortert.addAll(unike);
        Collections.sort(sortert);
        Tid[] t = new Tid[sortert.size()];
        for(int i =0; i<sortert.size(); i++){
            t[i]=sortert.get(i);
        }

        /*
        String build=null;
        for(Tid t1 : t){
            if(build==null){
                build=t1.toString();
            }else{
                build = build + "," + t1.toString();
            }
        }
        Log.e("flyt", "tider ut fra decompress:" + build);
        */

        return t;
    }



    public String[] alleAvganger(String stasjon, String linje) {

        Log.e("min error", "ikke implentert!");
        return null;
    }

}

class RuteTid{
    String[] stasjoner;
    TidKolonne[] tider;
    int type;
    public RuteTid(String[] stasjoner, TidKolonne[] tider, int type){
        this.stasjoner = stasjoner;
        this.tider=tider;
        this.type=type;
    }
    //bruker for å signalisere crashes
    public RuteTid(boolean sigal){
        this.stasjoner = null;
        this.tider=null;
        this.type=-1;
    }
}


class LinjeSamling {
    ArrayList<String> linjer = new ArrayList<>();
    String preHeader;

    public LinjeSamling(ArrayList<String> linjer, String preHeader){
        this.linjer=linjer;
        this.preHeader=preHeader;
    }
    public LinjeSamling(ArrayList<String> linjer){
        this.linjer=linjer;
        this.preHeader=null;
    }
    public boolean inneholderStasjonOgHeader(String stasjonID, String header){
        boolean harStasjon=false;
        boolean harHeader=false;
        if(preHeader!=null && preHeader.equals(header)){
            harHeader=true;
        }
        for(String s : linjer){
            String[] ar = s.split(",");
            if(!harStasjon && ar[2].equals(stasjonID)) harStasjon=true;
            if(!harHeader && ar[1].equals(header)) harHeader=true;
            if(harHeader && harStasjon) return true;
        }
        return false;
    }
    public Sekvens lagSekvens(){
        String sekvensInn = "";
        for(String s : linjer){
            String[] ar = s.split(",");
            if(sekvensInn.equals("")){
                sekvensInn=ar[2];
            }else{
                sekvensInn=sekvensInn + "," + ar[2];
            }
        }
        return new Sekvens(sekvensInn);
    }


}


class Sekvens {
    String[] stasjoner;
    String stream;
    Stasjon[] oversatt=null;
    TidKolonne[] kolonner;
    String commonStasjon;

    public Sekvens(String ruteIDStream){
        stasjoner = ruteIDStream.split(",");
        this.stream=ruteIDStream;
    }
    @Override
    public boolean equals(Object genOther){
        Sekvens other = (Sekvens) genOther;
        return stream.equals(other.stream);
    }

    @Override
    public int hashCode(){
        return stream.hashCode();
    }

    public void oversett(Context myContext) {
        int funnet=0;
        int skalFinnes=stasjoner.length;
        oversatt=new Stasjon[skalFinnes];
        VirituellFil inn = GlobaleMetoder.hentFil("FINAL_STOPS.txt", myContext);
        /*
        try{
            inn = new Scanner(GlobaleMetoder.hentFil("FINAL_STOPS.txt"));
        }
        catch(Exception e){
            Log.e("min error", "FINAL_STOPS.txt ikke funnet 0976");
            return;
        }
        */

        while(funnet!=skalFinnes){
            String[] ar = inn.nextLine().split(",");
            boolean innholderEnAvMineIder=false;
            for(int i=0; i<stasjoner.length; i++){
                String s = stasjoner[i];
                if(s==null) continue;
                if(ar[0].equals(s)){
                    try{
                        oversatt[i]=new Stasjon(ar[1], ar[2]);
                    }catch(Exception e){
                        oversatt[i]=new Stasjon(ar[1]);
                    }
                    funnet++;
                    stasjoner[i]=null;
                }
            }
        }



    }
}

class TidKolonne implements Comparable<TidKolonne>{
    Tid[] tider;
    int[] par;
    int commonIndex;
    String header;
    int endestasjonIndex=-1;

    public TidKolonne(int size, int[] par, int common, String header){
        this.tider = new Tid[size];
        this.par=par;
        this.commonIndex=common;
        this.header=header;
    }

    public void settInnTid(Tid t, int plass){
        if(tider[plass]==null){
            tider[plass]=t;
        }else{
            int parID = par[plass];
            for(int i=plass+1; i<tider.length; i++){
                if(par[i]==parID){
                    tider[i]=t;
                    //Log.e("flyt", "overrideet settInnTid fra " + plass+ " til " + i );
                    return;
                }
            }
            for(int i=0; i<plass; i++){
                if(par[i]==parID){
                    tider[i]=t;
                    //Log.e("flyt", " 2 overrideet settInnTid fra " + plass+ " til " + i );
                    return;
                }
            }
        }

    }

    public void finnEndestasjonIndex(){
        for(int i = tider.length-1; i>0; i--){
            if(tider[i]!=null){
                this.endestasjonIndex=i;
                return;
            }
        }
    }

    public boolean determineError(){
        Tid siste=null;
        for(Tid t : tider){
            if(t==null) continue;
            if(siste!=null && siste.compareTo(t) > 0){
                return true;
            }
            siste=t;
        }
        return false;
    }

    /*
    @Override
    public String toString(){
        String build=null;
        for(Tid t : tider){
            if(build==null){
                build=t.toString();
            }else{
                build = build + "\n" + t.toString();
            }
        }
        return build;
    }
    */

    @Override
    public int compareTo(TidKolonne other){
        if(commonIndex != -1){
            return tider[commonIndex].compareTo(other.tider[commonIndex]);
        }else{
            //sammenlign første non-null
            Tid forsteMin=null;
            Tid forsteOther=null;
            for(Tid t : tider){
                if(t!=null){
                    forsteMin=t;
                    break;
                }
            }
            for(Tid t : other.tider){
                if(t!=null){
                    forsteOther=t;
                    break;
                }
            }
            return forsteMin.compareTo(forsteOther);
        }
    }

    @Override
    public boolean equals(Object genOther){
        TidKolonne other = (TidKolonne) genOther;
        boolean like=true;
        for(int i=0; i<tider.length; i++){
            if(tider[i]==null && other.tider[i]==null) continue;
            if(tider[i]!=null && other.tider[i]!=null) continue;
            if(tider[i]==null || other.tider[i]==null){
                like=false;
                break;
            }
            if(!tider[i].equals(other.tider[i])){
                like=false;
                break;
            }
        }
        return like;
    }

    @Override
    public int hashCode(){
        return Arrays.hashCode(tider);
    }


}

class Stasjon  implements Serializable {
    public String navn;
    public String sone;
    public String id;
    public int intervall;

    public Stasjon(String navn, String sone){
        this.navn=navn;
        this.sone=sone;
    }
    public Stasjon(String navn){
        this.navn=navn;
        this.sone=null;
    }
    public Stasjon(String id, int intervall){
        this.id=id;
        this.intervall=intervall;
    }
    /*
    public void oversett(Context myContext){
        VirituellFil inn = GlobaleMetoder.hentFil("FINAL_STOPS.txt", myContext);
        while(inn.hasNextLine()){
            String[] ar = inn.nextLine().split(",");
            if(ar[0].equals(id)){
                this.navn=ar[1];
                try {
                    this.sone=ar[2];
                } catch (IndexOutOfBoundsException e) {}
                break;
            }
        }
    }
    */
}


class ReiseLinje{
    String kode;
    String navn;
    int type;
    public ReiseLinje(String linje){
        String[] ar = linje.split(",");
        kode=ar[1];
        navn=ar[2];
        type=Integer.parseInt(ar[3]);
    }
    @Override
    public boolean equals(Object other){
        ReiseLinje realOther = (ReiseLinje) other;
        return kode.equals(realOther.kode) && navn.equals(realOther.navn) && type == realOther.type;
    }

    @Override
    public String toString(){
        return kode + " " + navn;
    }

}


class LinjeMedTider implements Comparable<LinjeMedTider> {
    String lookUpRuteID;
    String rawRuteID;
    String ruteFullNavn;
    String fysiskRuteID;
    String header;
    String sone;
    Tid[] tider = new Tid[3];
    int type;
    ArrayList<Tid> alleTider = new ArrayList<>();
    Set<Tid> unike = new HashSet<>();
    Tid innTid;
    Tid forste;
    String nesteStasjon;
    int retning;

    public LinjeMedTider(String rawLinje, String ruteID, String header,
                         String tid, String sone, Context myContext) {
        String[] ar =rawLinje.split(",");

        //finner naa den fysike ruteIDen
        VirituellFil inn = GlobaleMetoder.hentFil("FINAL_RUTETRANSLATOR.txt", myContext);
        /*
        try{
            inn = new Scanner(GlobaleMetoder.hentFil("FINAL_RUTETRANSLATOR.txt"));
        }
        catch(Exception e){
            Log.e("min error", "FINAL_RUTETRANSLATOR.txt ikke funnet");
            return;
        }
        */
        String hentaRuteID=null;
        while(inn.hasNextLine()){
            String[] arr = inn.nextLine().split(",");
            if(arr[1].equals(ruteID)){
                hentaRuteID=arr[0];
                this.fysiskRuteID=hentaRuteID;
                this.retning = Integer.parseInt(arr[2]);
                break;
            }
        }
        if(hentaRuteID==null){
            Log.e("min error", "Klarte ikke aa translate ruteID, sokte etter " + ruteID);
            //System.exit(0);
        }

        //finner rute typen
        boolean funnetType=false;
        inn = GlobaleMetoder.hentFil("FINAL_ROUTES.txt", myContext);
        /*
        try{
            inn = new Scanner(GlobaleMetoder.hentFil("FINAL_ROUTES.txt"));
        }
        catch(Exception e){
            Log.e("min error", "FINAL_ROUTES.txt ikke funnet 00006");
            return;
        }
        */
        while(inn.hasNextLine()){
            String[] arr = inn.nextLine().split(",");
            if(arr[0].equals(hentaRuteID)){
                this.type=Integer.parseInt(arr[3]);

                if(retning==1){
                  this.ruteFullNavn=arr[2];
                }else if(retning==0){
                  String[] bakvendt = arr[2].split("-");
                    this.ruteFullNavn = bakvendt[1] + "- " + bakvendt[0] + ".";
                  //this.ruteFullNavn = bakvendt[1].substring(1) + " - " + bakvendt[0].substring(0, bakvendt[0].length()-1) + ".";
                }else{
                  Log.e("kritisk error", "broken logic, unexpected direction");
                }

                funnetType=true;
                break;
            }
        }
        if(!funnetType){
            Log.e("min error", "klarte ikke aa finne rute type/ fullt navn");
            //System.exit(0);
        }

        //fyller inn resten av instansvariablene
        this.rawRuteID=ruteID;
        this.header=header;
        this.sone=sone;
        this.innTid=new Tid(tid);
        this.lookUpRuteID=ruteID;

        //legger til tidene
        addTider(rawLinje);

    }

    public void addTider(String rawLinje){
        String[] ar =rawLinje.split(",");

        //Finner alle avganger
        //Log.e("min error", "2lagde ny tid med input " + ar[3]);
        //Log.e("min error", "3lagde ny tid med input " + ar[4]);
        Tid minTid=new Tid(ar[3]);
        Tid maxTid=new Tid(ar[4]);
        int minTime= minTid.time;
        int maxTime= maxTid.time;


        //Legger forst til start og slutt-tidene
        unike.add(minTid);
        unike.add(maxTid);

        //legger alle tider beskrevet av the pattern
        String[] commons=null;
        try{
            if(!ar[5].equals("")){
                String[] arr = ar[5].split(" ");
                //Log.e("min error", "ar5 test: " + ar[5]);
                commons=new String[arr.length];
                for(int i=0;i<arr.length;i++){
                    commons[i]=arr[i];
                    //Log.e("min error", "commons: setter inn: " + arr[i]);
                }
            }
        }catch(Exception e){}
        if(commons==null || commons.length==0){
            commons=null;
        }

        if(commons!=null){
            for(int i=minTime;i<=maxTime;i++){
                for(String s : commons){
                    if(i<10){
                        //  Log.e("min error", "4lagde ny tid med input " + "0" +i + ":" + s);
                        unike.add(new Tid("0" +i + ":" + s));
                    }else{
                        //Log.e("min error", "5lagde ny tid med input "  +i + ":" + s);
                        unike.add(new Tid("" +i + ":" + s));
                    }

                }
            }
        }
        //REMINDER: (first,last,commons,extra,doesntExist)

        //legger til extra tider
        Tid[] extra=null;
        try{
            if(!ar[6].equals("")){
                String[] arr = ar[6].split(" ");
                extra=new Tid[arr.length];
                for(int i=0;i<arr.length;i++){
                    //Log.e("min error", "test " + ar[6]);
                    //Log.e("min error", "6lagde ny tid med input " + arr[i]);
                    extra[i]=new Tid(arr[i]);
                }
            }
        }catch(Exception e){}
        if(extra==null || extra.length==0){
            extra=null;
        }
        if(extra!=null){
            for(Tid t : extra){
                unike.add(t);
            }
        }

        //fjerner overflodige tider
        Tid[] doesntExist=null;
        try{
            if(!ar[7].equals("")){
                String[] arr = ar[7].split(" ");
                doesntExist=new Tid[arr.length];
                for(int i=0;i<arr.length;i++){
                    //Log.e("min error", "7lagde ny tid med input " + arr[i]);
                    doesntExist[i]=new Tid(arr[i]);
                }
            }
        }catch(Exception e){}
        if(doesntExist==null || doesntExist.length==0){
            doesntExist=null;
        }
        if(doesntExist!=null){
            for(Tid t : doesntExist){
                //Log.e("min error", "fjerner");
                unike.remove(t);
            }
        }
    }

    public void velgDeNaermeste3Tidene(){
        //Finner naa de tre naermeste tidene
        alleTider.addAll(unike);
        Collections.sort(alleTider);
/*
Log.e("min error", "printer arraylist");
for(Tid t : alleTider){
  Log.e("min error", t);
}
Log.e("min error", "printing ferdig");
*/

        int funnet=0;
        for(Tid t : alleTider){
            if(t.compareTo(innTid) >= 0){
                //Log.e("min error", "La til basic: " + t);
                tider[funnet]=t;
                funnet++;
                if(funnet==3) break;
            }
        }

        //Vi er naa kommet til dognets ende og har ikke 3 tider, begynner neste dag
        while(funnet!=3){
            for(Tid t : alleTider){
                tider[funnet]=t;
                //Log.e("min error", "la til " + t + " ved plass " + funnet);
                funnet++;
                if(funnet==3) break;
            }
        }
        this.forste=tider[0];
    }

    public void addNeste(String neste){
        this.nesteStasjon = neste;
    }



    @Override
    public String toString(){
        if(sone==null){
            return "" + type + "," + fysiskRuteID + " " + header +
                    " " + tider[0] + " " + tider[1] + " " + tider[2];
        }else{
            return "[" + sone + "]" + type + "," + fysiskRuteID + " " + header +
                    " " + tider[0] + " " + tider[1] + " " + tider[2];
        }
    }

    @Override
    public boolean equals(Object genOther){
        LinjeMedTider other = (LinjeMedTider) genOther;
        return header==other.header;
    }

    @Override //  Tid innTid;
    public int compareTo(LinjeMedTider other){
        Tid bedreforste=forste;
        Tid bedreotherforste=other.forste;
        if(forste.compareTo(innTid) < 0){
            bedreforste = new Tid("" + (60 + forste.time) + ":" + (10+forste.minutt));
        }
        if(other.forste.compareTo(innTid) < 0){
            bedreotherforste = new Tid("" + (60 + other.forste.time) + ":" + (10+other.forste.minutt));
        }

        return bedreforste.compareTo(bedreotherforste);
    }


}

class Tid implements Comparable<Tid>  ,Serializable {
    String tid;
    int time;
    int minutt;

    public Tid(String tid){

        if(tid.length()!=5){
            Log.e("min error", "KRITISK ERROR TID FIKK INN: " + tid);
           // System.exit(0);
        }

        this.tid=tid;
        String[] ar = tid.split(":");

        this.time= Integer.parseInt(ar[0]);
        this.minutt=Integer.parseInt(ar[1]);
    }

    public Tid tidEtter(int intervall){

        int nyTime;
        int nyMinutt;

        nyTime=this.time;
        nyMinutt=this.minutt+intervall;

        while(nyMinutt > 59){
            nyMinutt=nyMinutt-60;
            nyTime++;
        }
        if(nyTime <10 && nyMinutt<10 ){
            return new Tid("" + "0" + nyTime + ":" + "0" + nyMinutt);
        }else if(nyTime <10 && nyMinutt>=10 ){
            return new Tid("" + "0" + nyTime + ":" + nyMinutt);
        }else if(nyTime >=10 && nyMinutt>=10 ){
            return new Tid(nyTime + ":" + nyMinutt);
        }else{
            return new Tid(nyTime + ":" + "0" + nyMinutt);
        }

    }



    @Override
    public int compareTo(Tid other){
        if((time-other.time)!=0) return (time-other.time)*61;
        return (minutt-other.minutt);
    }

    @Override
    public String toString(){
        if(time>23){
            int lesBarTime = time;
            while(lesBarTime>23){
                lesBarTime=lesBarTime-24;
            }
            if(lesBarTime<10){
                if(minutt>9){
                    return "0" + lesBarTime + ":" + minutt;
                }else{
                    return "0" + lesBarTime + ":0" + minutt;
                }

            }else{
                if(minutt>9){
                    return "" + lesBarTime + ":" + minutt;
                }else{
                    return "" + lesBarTime + ":0" + minutt;
                }
            }
        }else{
            return tid;
        }
    }

    public String getTidString(){
        return this.tid;
    }

    @Override
    public boolean equals(Object other){
        Tid realOther = (Tid) other;
        return time==realOther.time && minutt==realOther.minutt;
    }

    @Override
    public int hashCode(){
        return tid.hashCode();
    }

    public boolean erMindreEllerLik(Tid other){
        int minTime=time;
        while(minTime>23){
            minTime=minTime-24;
        }

        int otherTime=other.time;
        while(otherTime>23){
            otherTime=otherTime-24;
        }

        if(minTime > otherTime) return false;
        if(minTime < otherTime) return true;
        if(minutt < other.minutt || minutt==other.minutt){
            return true;
        }
        return false;
    }

}

class TidCustomSort extends Tid  implements Serializable {

    public TidCustomSort(String tid){
        super(tid);
    }

    @Override
    public int compareTo(Tid other){
        int minTime=time;
        int mittMinutt=minutt;
        while(minTime>23){
            minTime=minTime-24;
        }

        int otherTime=other.time;
        int otherMinutt=other.minutt;
        while(otherTime>23){
            otherTime=otherTime-24;
        }

        if((minTime-otherTime)!=0) return (minTime-otherTime)*61;
        return (mittMinutt-otherMinutt);
    }

}


class VirituellFil{
    String[] linjer;
    private int i;

    public VirituellFil(String[] linjer){
        this.linjer=linjer;
    }
    public boolean hasNextLine(){
        return i<=(linjer.length-1);
    }
    public String nextLine(){
        return linjer[i++];
    }
    public void reset(){
        i=0;
    }
    public String goBack1() {return linjer[--i];}
    public void gotoIndex(int i){
        this.i=i;
    }
    public boolean holdiUnder(int inn){ return i <= inn;}
}


class ScrollLinje implements Comparable<ScrollLinje> { //samle dem i public HashMap<Tid, ScrollLinje> content= new HashMap<>();
    public String linje;
    public String ruteID;
    public String linjeFarge;
    public String nesteStasjon;
    public int avatarID;
    public Tid avgang;
    public String fulltRuteNavn;

    public ScrollLinje(Tid avgang, String linje, String ruteID , int type, String nesteStasjon, String fulltRuteNavn){
        this.avgang=avgang;
        this.linje=linje;
        this.ruteID=ruteID;
        this.nesteStasjon=nesteStasjon;
        this.fulltRuteNavn=fulltRuteNavn;
        if(type==0){
            this.linjeFarge = "#4286f4";
            this.avatarID = R.drawable.tram;
        }else if(type==1){
            this.linjeFarge = "#f4f141";
            this.avatarID = R.drawable.metro;
        }else if(type==2){
            this.linjeFarge = "#41f446";
            this.avatarID = R.drawable.train;
        }else if(type==3) {
            this.linjeFarge = "#f441dc";
            this.avatarID = R.drawable.bus;
        }else if(type==4){
            this.linjeFarge = "#b541f4";
            this.avatarID = R.drawable.boat;
        }else{
            Log.e("kritisk", "det er mer enn 4 ruteIDer");
            this.linjeFarge = "#b541f4";
            this.avatarID = R.drawable.metro;
        }
    }
    public ScrollLinje(boolean signal){

    }

    @Override
    public int compareTo(ScrollLinje other){
        if(!avgang.equals(other.avgang)){
            return avgang.compareTo(other.avgang);
        }else{
            return 0;
        }
    }


}

class TilkobletLinje implements Serializable {
    public Stasjon[] stasjonsFrekvens;
    public String header;
    public Tid[] avganger;
    public String nesteStasjon;
    public String fullRuteNavn;
    public String sone;
    public String ruteIDutenPunktum;
    private String rutetider_ruteID;
    public String fullRuteID;
    private int retning;
    public int type;
    public int linjeFarge;
    public int avatarID;
    public String endeStasjon;
    public int[] intervaller;


    public TilkobletLinje(String[] stasjonsFrekvens, String header, Tid[] baseTider, int intervall,
                          int[] intervaller, String nestestasjon, String ruteIDutenPunktum, String sone){

        this.stasjonsFrekvens=new Stasjon[stasjonsFrekvens.length];
        for(int i =0; i<stasjonsFrekvens.length; i++){
            this.stasjonsFrekvens[i]=new Stasjon(stasjonsFrekvens[i], intervaller[i]);
        }
        this.header=header;
        this.intervaller=intervaller;

        this.avganger = new Tid[baseTider.length];
        for(int i =0; i<baseTider.length; i++){
            this.avganger[i]=baseTider[i].tidEtter(intervall);
        }
        this.nesteStasjon=nestestasjon;
        this.sone=sone;
        this.ruteIDutenPunktum=ruteIDutenPunktum;
    }


    public boolean setFullRuteID(String id, String fullRuteID, int retning){
        if(id.equals(ruteIDutenPunktum)){
            this.rutetider_ruteID=fullRuteID;
            this.retning=retning;
            return true;
        }else{
            return false;
        }
    }

    public boolean setFullRuteNavn(String id, String fullRuteID, String fullRuteNavn, String type){
         if(id.equals(this.rutetider_ruteID)){
             this.fullRuteID=fullRuteID;
             if(retning==1){
                 this.fullRuteNavn = GlobaleMetoder.safeFormatRuteNavn(fullRuteNavn);
             }else{
                 this.fullRuteNavn = GlobaleMetoder.twistRuteNavn(fullRuteNavn);
             }
             this.type=Integer.parseInt(type);
             return true;
         }else{
             return false;
         }
    }

    /*

                if(tkl.setFullRuteNavn(ar[0], ar[1], ar[2], ar[3])){
    */

    public void oversettAlleStasjoner(VirituellFil inn){
        if(nesteStasjon!= null){
            inn.gotoIndex(Integer.parseInt(nesteStasjon));
            String ar[] = inn.nextLine().split(",");
            nesteStasjon=ar[1];
        }
        if(endeStasjon!= null){
            inn.gotoIndex(Integer.parseInt(endeStasjon));
            String ar[] = inn.nextLine().split(",");
            endeStasjon=ar[1];
        }

        for(Stasjon s :stasjonsFrekvens){
            inn.gotoIndex(Integer.parseInt(s.id));
            String ar[] = inn.nextLine().split(",");
            s.navn=ar[1];
            try{
                s.sone=ar[2];
            }catch (IndexOutOfBoundsException e){}

        }



    }


    public int setStasjonNavn(String id, String navn, String soneInn){
        int oversatte=0;
        if(nesteStasjon!=null && nesteStasjon.equals(id)){
            nesteStasjon=navn;
            oversatte++;
        }
        for(Stasjon s :stasjonsFrekvens){
            if(s.id.equals(id)){
                s.id=s.id+"translated";
                s.navn=navn;
                s.sone=soneInn;
                oversatte++;
            }
        }
        return oversatte;
    }
    public int stasjonerTilOversetting(){
        if(nesteStasjon==null){
            return stasjonsFrekvens.length;
        }else{
            return stasjonsFrekvens.length +1;
        }
    }

    public void initGUIVerdier(){
        if(type==0){
            this.linjeFarge = Color.parseColor("#84c1ff"); //light blue
            this.avatarID = R.drawable.tram;
        }else if(type==1){
            this.linjeFarge = Color.parseColor("#ffa500"); //orange
            this.avatarID = R.drawable.metro;
        }else if(type==2){
            this.linjeFarge = Color.parseColor("#191919"); //light black
            this.avatarID = R.drawable.train;
        }else if(type==3) {
            this.linjeFarge = Color.parseColor("#008000"); //green
            this.avatarID = R.drawable.bus;
        }else if(type==4){
            this.linjeFarge = Color.parseColor("#0000ff"); //classic blue
            this.avatarID = R.drawable.boat;
        }else{
            Log.e("kritisk", "det er mer enn 4 ruteIDer");
            this.linjeFarge = Color.parseColor("#f5f5dc"); //xx light gray
            this.avatarID = R.drawable.metro;
        }
        this.endeStasjon=stasjonsFrekvens[stasjonsFrekvens.length-1].navn;
    }

    @Override
    public boolean equals(Object genOther){
        TilkobletLinje other = (TilkobletLinje) genOther;
        if(header==null || other.header==null){
        }else{
            if(!header.equals(other.header)) return false;
        }

        if(endeStasjon==null || other.endeStasjon==null){
        }else{
            if(!endeStasjon.equals(other.endeStasjon)) return false;
        }
        if(nesteStasjon==null || other.nesteStasjon==null){
        }else{
            if(!nesteStasjon.equals(other.nesteStasjon)) return false;
        }
        return true; //loooooooooooooooool
    }

    @Override
    public int hashCode(){
        int sum=0;
        if(header!=null) sum=sum+ header.hashCode();
        if(endeStasjon!=null) sum=sum+ endeStasjon.hashCode();
        if(nesteStasjon!=null) sum=sum+ nesteStasjon.hashCode();
        return sum;
    }


}

class TilkobletLinjeSingel implements Comparable<TilkobletLinjeSingel>{
    public TilkobletLinje mor;
    public Tid singelTid;
    public TidCustomSort custom;

    public TilkobletLinjeSingel(Tid t, TilkobletLinje mor, TidCustomSort custom){
        this.mor=mor;
        this.singelTid=t;
        this.custom=custom;
    }

    @Override
    public int compareTo(TilkobletLinjeSingel other){
        return custom.compareTo(other.custom);
    }

    @Override
    public boolean equals(Object genOther){
        TilkobletLinjeSingel other = (TilkobletLinjeSingel) genOther;
        return singelTid.equals(other.singelTid) && mor.equals(other.mor);
    }

    @Override
    public int hashCode(){
        return singelTid.hashCode() + mor.hashCode();
    }

}

//for bruk i sekvens-manipulasjon
class Unik{
    public String foran;
    public String bak;
    public String id;
    public Unik(String foran, String bak, String id){
        this.foran=foran;
        this.bak=bak;
        this.id=id;
    }

}








/*
private static class RuteSamler {
  //key=header, value=Tider
  static HashMap<String, Tid[]> samler = new HashMap<>();
  String ruteID;

  public RuteSamler(String currentLesteLinje, String ruteID, String header,
  String tid, String sone){
    if(!samler.keySet().contains(header)){
      Tid[] ar = new Tid[1];
      ar[0]=new Tid(tid);
      samler.put(header,ar);
    }else{
      Tid[] gammel = samler.get(header);
      Tid[] ny = new Tid[gammel.length+1];
      for(int i =0; i<gammel.length;i++){
        ny[i]=gammel[i];
      }
      ny[ny.length-1]= new Tid(tid);
      samler.put(header, ny);
    }


  }

}
*/

//new LinjeMedTider(currentLesteLinje, currentRuteID,
//currentLinje, tid, sone));
