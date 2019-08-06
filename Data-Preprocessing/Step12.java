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
import java.util.Set;
import java.util.HashSet;

//legger til informasjonen i FINAL STOPS, om hvilken linje forste og siste occurence er
//dvs indexen peker til der den relevant ruten starter
class Step12{

  public static void main(String[] args) throws Exception {

    System.out.println("Indekserer stops-filen...");

    Scanner inn = new Scanner(new File("FINAL_REL_RUTETIDER.txt"));
    HashMap<Integer,Occurence> hm = new HashMap<>(); //key= stasjonid, value=first and last
    int linjeTeller=-1;
    String currentRute=null;
    int currentRelevantRuteIndex=0;
    while(inn.hasNextLine()){
      linjeTeller++;
      String[] ar = inn.nextLine().split(",");
      if(currentRute==null || !ar[0].equals("")){
        currentRute= ar[0].substring(0, ar[0].indexOf("."));
        currentRelevantRuteIndex=linjeTeller;
      }
      int id = Integer.parseInt(ar[2]);
      if(hm.keySet().contains(id)){
          hm.get(id).last=currentRelevantRuteIndex;
      }else{
          hm.put(id, new Occurence(currentRelevantRuteIndex, currentRelevantRuteIndex));
      }
    }

    ArrayList<String> linjer = new ArrayList<>();
    inn = new Scanner(new File("FINAL_STOPS.txt"));
    while(inn.hasNextLine()){
      String linje = inn.nextLine();
      int id = Integer.parseInt( linje.substring(0, linje.indexOf(",")) );
      Occurence o = hm.get(id);
      linjer.add(linje + "," + o.first + "," + o.last );
    }

    PrintWriter ut = new PrintWriter(new File("FINAL_STOPS_I.txt"));
    for(String s : linjer){
      ut.println(s);
    }
    ut.close();

    System.out.println("#### Step12 er ferdig ####");




  }

}

class Occurence{
  int first=-1;
  int last=-1;
  public Occurence(int first, int last){
    this.first=first;
    this.last=last;
  }

}
