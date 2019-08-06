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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//bytter ut defualt stasjonsIDs med egne, kortere
class Step7{

  public static void main(String[] args) throws Exception {


    //Kartlegger hyppigheten til stoppene
    //Lagrer dataen i HashMapet forekomster der key=originalID, value=forekomster


    Scanner improved = new Scanner(new File("stop_times.txt"));
    HashMap<String,Integer> forekomster = new HashMap<>();
    System.out.println("genererer nye IDer..");
    improved.nextLine();
    int max=0;
    int leste=0;
    while(improved.hasNextLine()){
      leste++;
      //if((leste % 1000)==0) System.out.println("leste linjer " + leste);
      String[] ar = improved.nextLine().split(",");
      if(forekomster.keySet().contains(ar[3])){
        int nesteVerdi= forekomster.get(ar[3]).intValue()+1;
        forekomster.put(ar[3], new Integer(nesteVerdi));
        if(nesteVerdi>max) max=nesteVerdi;
      }else{
        forekomster.put(ar[3], new Integer(0));
      }
    }

    //Lager nye IDer med bakgrunn av hyppigheten, legger de i HashMapet hm
    //der key=ruterID, value=nyID

    HashMap<String,Integer> hm = new HashMap<>();
    int lagtTil=0;
    for(int i =max; i>=0; i--){
      //System.out.println("iterasjon " + (max-i) + " av " + max);
      for(String s : forekomster.keySet()){
        if(forekomster.get(s).intValue()==i){
          hm.put(s , new Integer(lagtTil++));
          //forekomster.remove(s);
        }
      }
    }

    //ny end


    //key=RuterID, value=minID
    Scanner inn = new Scanner(new File("stops_gen.txt"));
    PrintWriter en = new PrintWriter(new File("stops_nesten_final.txt"));
    PrintWriter to = new PrintWriter(new File("rute_tider_nesten_final.txt"));

    System.out.println("fyller IDer i stops-filen..");
    //fyller alle linjer-to-go inn i en ArrayList
    ArrayList<Linjez> linjer = new ArrayList<>();

    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");

      try{
        linjer.add(new Linjez(hm.get(ar[0]) + "," + ar[1] + "," + ar[2]));
      }catch(Exception e){
        linjer.add(new Linjez(hm.get(ar[0]) + "," + ar[1] + ","));
      }
    }

    //Sorterer dem
    Collections.sort(linjer);

    //Skriver naa til fil
    for(Linjez l : linjer){
      en.println(l.linje);
    }


    /*
    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");

      try{
        en.println(hm.get(ar[0]) + "," + ar[1] + "," + ar[2]);
      }catch(Exception e){
        en.println(hm.get(ar[0]) + "," + ar[1] + ",");
      }
    }
    */
    en.close();

    System.out.println("fyller IDer i rutetid-filen..");
    inn= new Scanner(new File("step6.txt"));
    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");

      try{
        String t = ar[5];
      }catch(Exception e){
        to.println(ar[0] + "," + ar[1] + "," + hm.get(ar[2]) + "," + ar[3] + "," + ar[4] + ",,,");
        continue;
      }

      try{
        String t = ar[6];
      }catch(Exception e){
        to.println(ar[0] + "," + ar[1] + "," + hm.get(ar[2]) + "," + ar[3] + "," + ar[4] + "," + ar[5] + ",,");
        continue;
      }

      try{
        to.println(ar[0] + "," + ar[1] + "," + hm.get(ar[2]) + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," + ar[7]);
        continue;
      }catch(Exception e){
        to.println(ar[0] + "," + ar[1] + "," + hm.get(ar[2]) + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + ",");
        continue;
      }
    }
    to.close();
    System.out.println("#### Step7 er ferdig ####");



  }



}

class Linjez implements Comparable<Linjez> {
  String linje;
  Integer nyRuteID;

  public Linjez(String linje){
    this.linje=linje;
    String[] ar = linje.split(",");
    this.nyRuteID = Integer.parseInt(ar[0]);
  }

  @Override
  public int compareTo(Linjez other){
    return nyRuteID.compareTo(other.nyRuteID);
  }

}
