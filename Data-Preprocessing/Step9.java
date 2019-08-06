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

//forbedrer routes.txt og trips.txt, putter alt inn i filen FINAL_ROUTES.txt
class Step9{

  public static void main(String[] args) throws Exception {

    //Lager forst et oversettelses-HashMap mellom gammel RuteID og ny ruteID

    //key=old, value=new
    HashMap<String,String> translate = new HashMap<>();

    Scanner old= new Scanner(new File("step1.txt"));
    Scanner ny= new Scanner(new File("step2.txt"));

    System.out.println("kartlegger ruteID-relations..");
    while(old.hasNextLine()){
      String[] oldAr = old.nextLine().split(",");
      String[] newAr =ny.nextLine().split(",");
      translate.put(oldAr[0].substring(0, oldAr[0].indexOf(".")), newAr[0].substring(0, newAr[0].indexOf(".")));
    }

    //------------------------------EXTRA---------------------------------
    //Legger til linjenummerert i FINAL_RUTETIDER til linjen jeg refererer
    HashMap<String,Integer> indexering = new HashMap<>(); //key=linje, value=index
    Scanner innExtra = new Scanner(new File("FINAL_RUTETIDER.txt"));
    int index=0;
    while(innExtra.hasNextLine()){
      String[] ar = innExtra.nextLine().split(",");
      if(!ar[0].equals("")){
        indexering.put(ar[0].substring(0,ar[0].indexOf(".")), index);
      }
      index++;
    }
    //------------------------------EXTRA---------------------------------


    //Lager en fil for oversetting av minID -> ruterpraktiskID
    System.out.println("skriver filen nestenFINAL_RUTETRANSLATOR.txt..");
    PrintWriter dataFil = new PrintWriter(new File("nestenFINAL_RUTETRANSLATOR.txt"));
    Set<String> fjernerLike = new LinkedHashSet<>();
    Scanner innHelp = new Scanner(new File("trips.txt"));
    innHelp.nextLine();
    while(innHelp.hasNextLine()){
      String[] ar = innHelp.nextLine().split(",");
      String rute = translate.get(ar[2]);
      fjernerLike.add(ar[0] + "," + rute + "," + ar[4] + "," + indexering.get(rute));
    }
    for(String s : fjernerLike){
      dataFil.println(s);
    }
    dataFil.close();


    Scanner inn;
    //oversetter naa OGPraktiskRuteIDer med mine ruteIDer
    //key=PraktiskRuteID , value= min ruteID
    /*
    HashMap<String,String> translaterHigher = new HashMap<>();

    Scanner inn = new Scanner(new File("trips_improved_2.txt"));

    System.out.println("oversetter de praktiske ruteIDene..");
    inn.nextLine();
    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");
      translaterHigher.put(ar[0], translate.get(ar[2]));
    }
    */
    translate=null; //saver minne


    //skriver til fil
    System.out.println("skriver til fil..");
    PrintWriter ut = new PrintWriter(new File("FINAL_ROUTES.txt"));
    inn = new Scanner(new File("routes.txt"));

    inn.nextLine();
    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");
      /*
      ut.println(translaterHigher.get(ar[0]) + "," + ar[2].substring(1, ar[2].length()-1) +
       "," + ar[3].substring(1, ar[3].length()-1) + "," + ar[4]);
      */
      ut.println(ar[0] + "," + ar[2].substring(1, ar[2].length()-1) +
       "," + ar[3].substring(1, ar[3].length()-1) + "," + ar[4]);
    }

    ut.close();
    System.out.println("#### Step8 er ferdig ####");

  }

}
