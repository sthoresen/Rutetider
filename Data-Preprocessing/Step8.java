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


//Samler like stasjoner under samme ID
class Step8{

  public static void main(String[] args) throws Exception {


    Scanner inn = new Scanner (new File("stops_nesten_final.txt"));
    ArrayList<RuteLiktNavn> liste = new ArrayList<>();

    System.out.println("Samler like RuteIDs..");
    s : while(inn.hasNextLine()){
      String denne = inn.nextLine();
      String[] ar = denne.split(",");
      boolean harDen=false;
      for(RuteLiktNavn rln : liste){
        if(rln.stops.contains(ar[1])){
          harDen=true;
          rln.addLinje(denne);
          continue s;
        }
      }
      if(!harDen){
        liste.add(new RuteLiktNavn(denne));
      }else{
        System.out.println("logisk feil");
        System.exit(0);
      }

    }

    for(RuteLiktNavn rln : liste){
      rln.VelgLinje();
    }

    System.out.println("Skriver til FINAL_STOPS..");
    PrintWriter ut = new PrintWriter(new File("FINAL_STOPS.txt"));
    HashMap<String,String> translate = new HashMap<>();
    int currentGenID=0;
    for(RuteLiktNavn rln : liste){
      String[] ar = rln.linje.split(",");
      try{
        ut.println((currentGenID++) + "," + ar[1] + "," + ar[2]);
      }catch(IndexOutOfBoundsException e){
        ut.println((currentGenID++) + "," + ar[1] + ",");
      }
      for(String s : rln.linjer){
        String[] arr = s.split(",");
        translate.put(arr[0],"" + (currentGenID-1));
      }
    }
    ut.close();

    System.out.println("Skriver til FINAL_RUTETIDER..");
    ut = new PrintWriter(new File("FINAL_RUTETIDER.txt"));
    inn = new Scanner(new File("rute_tider_nesten_final.txt"));

    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");
      String linjeEdit = null;
      try{
        linjeEdit = ar[0] + "," + ar[1] + "," + translate.get(ar[2]) + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," + ar[7];
        ut.println(linjeEdit);
        continue;
      }catch(Exception e){}
      try{
        linjeEdit = ar[0] + "," + ar[1] + "," + translate.get(ar[2]) + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + ",";
        ut.println(linjeEdit);
        continue;
      }catch(Exception e){}
      try{
        linjeEdit = ar[0] + "," + ar[1] + "," + translate.get(ar[2]) + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ",";
        ut.println(linjeEdit);
        continue;
      }catch(Exception e){
        linjeEdit = ar[0] + "," + ar[1] + "," + translate.get(ar[2]) + "," + ar[3] + "," + ar[4] + "," + "," + ",";
        ut.println(linjeEdit);
        continue;
      }
    }

    ut.close();
    System.out.println("#### Step9 er ferdig ####");
  }
}

class RuteLiktNavn{

  ArrayList<String> linjer = new ArrayList<>();
  ArrayList<String> stops = new ArrayList<>();
  String linje = null;
  String navn;

  public RuteLiktNavn(String linje){
    String[] ar = linje.split(",");
    this.navn=ar[1];
    linjer.add(linje);
    stops.add(ar[1]);
  }

  public void addLinje(String linje){
    linjer.add(linje);
    String[] ar = linje.split(",");
    stops.add(ar[1]);
  }

  public void VelgLinje(){
    if(linje!=null) return;
    for(String s : linjer){
      String[] ar = s.split(",");
      try{
        this.linje=(ar[0] + "," + ar[1] + "," + ar[2]);
        return;
      }catch(IndexOutOfBoundsException e){}
    }
    this.linje=linjer.get(0);
  }
}
