import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

import java.util.LinkedHashSet;
import java.util.List;

//Analyserer identiske reiser og lager nye ruteIDer
//PER 05.08, ENDRET TIL Å SEPARERE DAG-SENSITIVE REISER
class Step2{

  public static void main(String[] args) throws Exception {

    /*
    //Step 1. Legger alle Trip_IDs inn en arraylista tripIDs
    Scanner pre = new Scanner(new File("mine stop_times.txt"));
    ArrayList<String> tripIDs = new ArrayList<>();

    double startTid = System.currentTimeMillis();
    int linjeteller1=0;
    while(pre.hasNextLine()){
      String[] ar = pre.nextLine().split(",");
      linjeteller1++;
      System.out.println("phase 1, linje: " + linjeteller1);
      if(!tripIDs.contains(ar[0])) tripIDs.add(ar[0]);
    }

    System.out.println("TripIDs sorted after " + (System.currentTimeMillis()-startTid)/1000 + "s. size: " + tripIDs.size());

    try{
      Thread.sleep(10000);
    }catch(Exception e){}
    */


    //Step 2. Iterer gjennom fila igjen og legger til min og max index, i klassen TripID
    int luketUt=0;
    int adda=0;
    ArrayList<TripID> tripIDs2 = new ArrayList<>();
    Scanner middle = new Scanner(new File("step1.txt"));
    int lineCount=0;
    String linje = middle.nextLine();
    lineCount++;
    String[] ar = linje.split(",");
    String id = ar[0];
    System.out.println("Oppretter TripIDs..");
    while(middle.hasNextLine()){
      int min=lineCount;
      int max=lineCount;
      String sekvens="";
      String benchmark = ar[0];
      int benchmarkService=Integer.parseInt(ar[0].substring(ar[0].indexOf(".")+1, ar[0].length()));
      while(id.equals(benchmark) && middle.hasNextLine()){
        max=lineCount;
        sekvens=sekvens+ar[3];
        ar = middle.nextLine().split(",");
        lineCount++;
        id=ar[0];
      }

      boolean unik=true;
      for(TripID t : tripIDs2){
        if(t.sekvens.equals(sekvens) && t.serviceID==benchmarkService){
          unik=false;
          luketUt++;
          break;
        }
      }
      if(unik){
        //System.out.println("la til: " + benchmark + " " + min + " " + max + " " + sekvens);
        adda++;
        //System.out.println("lagt til: " + adda);
        tripIDs2.add(new TripID(benchmark, sekvens, min, max));
      }

    }


    System.out.println(tripIDs2.size() + " unike. Fjernet " + luketUt);

    /*
    for(TripID t : tripIDs2){
      if(t.id.equals("100010001")){
        System.out.println("treff");
      }
    }
    System.exit(0);
    */


    //step 3. Gaar ny igjennom stop_times.txt og endrer trip_IDene (i en ny fil)
    PrintWriter ut = new PrintWriter(new File("step2.txt"));
    Scanner siste = new Scanner(new File("step1.txt"));
    Scanner follow = new Scanner(new File("step1.txt"));

    int progress=0;
    int error=0;
    boolean first=true;
    String[] savedArr=null;
    String linje2 = siste.nextLine();
    String[] arr = linje2.split(",");
    String id2 = arr[0];
    System.out.println("Skriver fil med TripIDs..");
    while(siste.hasNextLine()){
      String sekvens="";
      String benchmark = arr[0];
      //bygger opp en sekvens
      while(id2.equals(benchmark) && siste.hasNextLine()){
        sekvens=sekvens+arr[3];
        arr = siste.nextLine().split(",");
        id2=arr[0];
      }
      //sekvensen er funnet
      //finner tilhorende id.
      boolean funnet = false;
      int nyID=-1;
      String nyIDmedS=null;
      sok : for(TripID t : tripIDs2){
        if(t.sekvens.equals(sekvens) && t.serviceID==Integer.parseInt(benchmark.substring(benchmark.indexOf(".")+1, benchmark.length()))){         //<-----------------------------
          funnet=true;
          nyID=tripIDs2.indexOf(t);
          nyIDmedS=nyID + "." + t.serviceID;
          break sok;
        }
      }
        if(funnet==false){
          System.out.println("logisk feil");
          System.exit(0);
        }

        //skriver naa til fil med vaar nye ID.
        String linjex;
        String[] nyArr;
        if(first){
          first=false;
          linjex=follow.nextLine();
          nyArr = linjex.split(",");
        }else{
          nyArr = savedArr;
        }
        String originalID = nyArr[0];
        while(originalID.equals(nyArr[0])){
          ut.println(nyIDmedS + "," + nyArr[1] + "," + nyArr[2] + "," + nyArr[3] + "," + nyArr[4]);
          progress++;
          //System.out.println("linjer outputta: " + progress);
          try {
            String nyLinje=follow.nextLine();
          //  System.out.println(nyLinje);
            nyArr = nyLinje.split(",");
            savedArr=nyArr;

          }catch(Exception e){
            error++;
            //System.out.println("error: " + e);
            //System.out.println("ferdig");
            ut.close();
            System.exit(0);
          }
        }
    }

    ut.close();
    System.out.println("#### Step2 er ferdig ####");



  }

}

class TripID{
  public int max=-1;
  public int min=-1;
  public String sekvens;
  public String id;
  public int serviceID;

  public TripID(String trip_ID, String sekvens, int min, int max){
    this.id=trip_ID;
    this.sekvens=sekvens;
    this.min=min;
    this.max=max;
    //String[] ar = trip_ID.split(".");
    //System.out.println(trip_ID);
    //System.out.println(ar.length);
    //this.serviceID=Integer.parseInt(ar[1]);
    this.serviceID=Integer.parseInt(trip_ID.substring(trip_ID.indexOf(".")+1, trip_ID.length()));
    //System.out.println(serviceID);
    //System.exit(0);

  }
}



class StasjonsSekvens{
  static Lock laas = new ReentrantLock();
  private static HashMap<String, Integer> sekvenser = new HashMap<>();
  private static int idMax=0;
  private int id;
  private String s;

  public StasjonsSekvens(String sekvens){
    laas.lock();
    this.s=sekvens;
    if(sekvenser.keySet().contains(sekvens)){
      this.id=sekvenser.get(sekvens);
    }else{
      sekvenser.put(sekvens, idMax);
      this.id=idMax;
      idMax++;
    }
    laas.unlock();
  }

  public StasjonsSekvens(boolean signal) throws Exception {
    PrintWriter skriver = new PrintWriter(new File("StasjonsSekvenser.txt"));
    for(String s : sekvenser.keySet()){
      skriver.println(sekvenser.get(s) + "," + s);
    }
    skriver.close();
  }

  public int id(){
    return id;
  }
  public String toString(){
    return s;
  }

}
