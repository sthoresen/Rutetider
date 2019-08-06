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

//Sorterer linjene i en fornuftig rekkefolge, samt fjerner duplikater


//IKKE LENGER GYLDIG
//VIKTIG. ETTER LATE DEBUGGING BEHOLDER JEG NAA DUPLIKATER. DETTE ER FORDI
//TID MELLOM STASJONER PER LINJE FAKTISK IKKE ER KONSTANT
//IKKE LENGER GYLDIG

class Step3{

  public static void main(String[] args) throws Exception {

      Scanner inn = new Scanner(new File("step2.txt"));
      ArrayList<Linje> lines = new ArrayList<>();
          // SWITCHED  Set<Linje> unikelines = new LinkedHashSet<>();
      Set<Linje> unikelines = new LinkedHashSet<>();
      //ArrayList<Linje> unikelines = new ArrayList<>(); // <-- DEBUGGED

      /*
      Integer a = new Integer(15);
      Integer b = new Integer(5);
      System.out.println(a.compareTo(b)); //1
      System.out.println(b.compareTo(a)); //-1
      System.out.println(a > b);
      */
      double startTid = System.currentTimeMillis();

System.out.println("leser inn..");
      int teller=0;
      while(inn.hasNextLine()){
        teller++;
        //if((teller % 1000) ==0) System.out.println("leser inn linje " + teller);
        Linje l = new Linje(inn.nextLine());
        unikelines.add(l);
      }
      //System.out.println("innlesning ferdig "+ (int) (System.currentTimeMillis()-startTid));

      //List<Linje> unik = new ArrayList<Linje>(new LinkedHashSet<Linje>(lines));
      //int fjernetLike = lines.size() - unik.size();
      //lines.clear();
      lines.addAll(unikelines);
      System.out.println("add all ferdig");

      System.out.println("sorting..");
      Collections.sort(lines);
      //System.out.println("sortet list/set "+ (int) (System.currentTimeMillis()-startTid));

      /*
      Linje test = lines.get(0);
      for(int i =1; i<lines.size(); i++){
        Linje l=lines.get(i);
        if(l == test){
          System.out.println("match ved " + i);
          System.out.println(test);
          System.out.println(l);
          System.exit(0);
        }
      }
      System.out.println("resultat: == ikke overridet");
      System.exit(0);
      */

      /*
      int presize=lines.size();
      System.out.println("presize " + presize + " "+ (int) (System.currentTimeMillis()-startTid));
      Set<Linje> unik = new LinkedHashSet<>();
      System.out.println("lagde hashset "+ (int) (System.currentTimeMillis()-startTid));
      unik.addAll(lines);
      System.out.println("satt inn i hashset "+ (int) (System.currentTimeMillis()-startTid));
      lines.clear();
      System.out.println("cleared arraylist "+ (int) (System.currentTimeMillis()-startTid));
      lines.addAll(unik);
      System.out.println("refilled arraylis "+ (int) (System.currentTimeMillis()-startTid));
      //Collections.sort(lines);
      //System.out.println("sortet arraylist "+ (int) (System.currentTimeMillis()-startTid));
      System.out.println("fjernet: " + (presize-lines.size()));
      System.exit(0);
      */

      PrintWriter ut = new PrintWriter(new File("step3.txt"));

      System.out.println("skriver til output fil..");
      for(int i=0; i<lines.size();i++){
      //  if(((i+1) % 1000) ==0) System.out.println("skriver ut linje " + (i+1));
        ut.println(lines.get(i).linje);
        //unik.set(i, null);
      }

      /*
      int teller2=0;
      for(Linje l : unikelines){
        teller2++;
        ut.println(l.linje);
        if((teller2 % 1000) ==0) System.out.println("la til linje: " + teller2);
      }
      */
      ut.println("696969,xxxxxx,0,696969696969,69:69:69"); //trengs i step4
      ut.close();
      System.out.println("ferdig, antall output linjer: " + lines.size());
      System.out.println("#### Step3 er ferdig ####");
      //System.out.println("ferdig, fjernet like: " + fjernetLike);




  }


}

class Linje implements Comparable<Linje>{
  String linje;
  /*
  String tripID;
  String linjeNavn;
  String sekvens;
  String stasjon;
  String tid;
  */
  //String[] arr;
  public Linje(String linje){
    //this.arr = linje.split(",");
    //this.tripID=arr[0];
    //this.linjeNavn=arr[1];
    //this.sekvens=arr[2];
    //this.stasjon=arr[3];
    //this.tid=arr[4];
    this.linje=linje;
  }
  @Override
  public int compareTo(Linje other){
    String[] arr = linje.split(",");
    String[] otherArr = other.linje.split(",");
    if(Integer.parseInt(arr[0].substring(0, arr[0].indexOf("."))) - Integer.parseInt(otherArr[0].substring(0, otherArr[0].indexOf(".")))==0){
        if(Integer.parseInt(otherArr[2]) - Integer.parseInt(arr[2])==0){
          String[] otherTid = otherArr[4].split(":");
          int otherTime=Integer.parseInt(otherTid[0]);
          int otherMinutt=Integer.parseInt(otherTid[1]);
          int otherSekund=Integer.parseInt(otherTid[2]);

          String[] denneTid = arr[4].split(":");
          int time=Integer.parseInt(denneTid[0]);
          int minutt=Integer.parseInt(denneTid[1]);
          int sekund=Integer.parseInt(denneTid[2]);

          if(time-otherTime==0){
            if(minutt-otherMinutt==0){
              if(sekund-otherSekund==0){
                return 0;
              }else{
                return sekund-otherSekund;
              }
            }else{
              return minutt-otherMinutt;
            }
          }else{
            return time-otherTime;
          }
        }else{
          return Integer.parseInt(arr[2]) - Integer.parseInt(otherArr[2]);
          //return arr[2].compareTo(otherArr[2]);
        }
    }else{
      return Integer.parseInt(arr[0].substring(0, arr[0].indexOf("."))) - Integer.parseInt(otherArr[0].substring(0, otherArr[0].indexOf(".")));
      //return arr[0].compareTo(otherArr[0]);
    }
  }

  @Override
  public boolean equals(Object other){
    //if(!(other instanceof Linje)) return false;
    Linje riktigOther = (Linje) other;
    //String[] min = linje.split(",");
    //String[] andres = riktigOther.linje.split(",");
    if(linje==null || riktigOther.linje==null){
      System.out.println("error");
      System.out.println(linje + " @ " + riktigOther.linje);
      System.exit(0);
    }
    //if(min[0].equals(andres[0]) && min[1].equals(andres[1]) && min[2].equals(andres[2]) && min[3].equals(andres[3]) && min[4].equals(andres[4])){
    //if(linje.equals(riktigOther.linje)){
    if(linje.equals(riktigOther.linje)){
      //System.out.println("match");
      //System.exit(0);
      //System.out.println("indre match");
      return true;
    }else{
      return false;
    }
  //  return min[0].equals(andres[0]) && min[1].equals(andres[1]) && min[2].equals(andres[2]) && min[3].equals(andres[3]) && min[4].equals(andres[4]);
  }
  @Override
  public int hashCode(){
    return linje.hashCode();
  }



}
