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


//Gjor tidsystemet relativt for sikrere resultater og kraftigere kompresjon
class Step11RelTid{

  public static void main(String[] args) throws Exception {

    Scanner inn = new Scanner(new File("FINAL_RUTETIDER.txt"));

    System.out.println("Analyserer tid-linjene og omdanner dem til relative constructs...");

    ArrayList<String> linjer = new ArrayList<>();
    ArrayList<String> lokaleLinjer = new ArrayList<>();
    String rawLinje = inn.nextLine();
    String[] ar = rawLinje.split(",");
    String ruteID=ar[0];
    ArrayList<SekvensTid> sekvensTider = new ArrayList<>();
    int sekvens;
    while(inn.hasNextLine()){
      sekvensTider.clear();
      lokaleLinjer.clear();
      sekvens=0;
      ruteID=ar[0];

      while( (ar[0].equals("") || ar[0].equals(ruteID)) && inn.hasNextLine() ){
        sekvensTider.add(new SekvensTid((++sekvens), forsteTid(rawLinje), rawLinje ));
        lokaleLinjer.add(rawLinje);
        rawLinje=inn.nextLine();
        ar=rawLinje.split(",");
      }

      if(!inn.hasNextLine()){
        sekvensTider.add(new SekvensTid((++sekvens), forsteTid(rawLinje), rawLinje ));
        lokaleLinjer.add(rawLinje);
      }

      //har naa alle SekvensTid-objekter i lista

      //Finner overgangstider
      ArrayList<SekvensOvergang> overganger = new ArrayList<>();
      for(int i=0; i<(sekvensTider.size()-1); i++){
        overganger.add(new SekvensOvergang(sekvensTider.get(i), sekvensTider.get(i+1) ));
      }

      //Bytter ut fulle lokale linjer med funnet relativ tid
      for(int i=0; i<lokaleLinjer.size(); i++){
        String s = lokaleLinjer.get(i);
        for(SekvensOvergang so : overganger){
          if(so.tilRaw.equals(s)){
            String[]  arr = s.split(",");
            lokaleLinjer.set(i, ",," + arr[2] + "," + so.forsteOvergangTid);
          }

        }
      }

      linjer.addAll(lokaleLinjer);

    }

    PrintWriter ut = new PrintWriter(new File("FINAL_REL_RUTETIDER.txt"));
    for(String s : linjer){
      ut.println(s);
    }
    ut.close();

    System.out.println("#### Step11RelTid er ferdig ####");

  }



  private static TidAS forsteTid(String linje){
      String[] ar =linje.split(",");
      return new TidAS(ar[3]);
  }

}

class SekvensTid{

  public int sekvens;
  public TidAS forste;
  public String rawLinje;

  public SekvensTid(int sekvens, TidAS forste, String rawLinje){
    this.sekvens=sekvens;
    this.forste=forste;
    this.rawLinje=rawLinje;
  }
}

class SekvensOvergang{
  public int fra;
  public int til;
  public int forsteOvergangTid;
  public String fraRaw;
  public String tilRaw;
  public SekvensOvergang(SekvensTid fra, SekvensTid til){
    this.fra=fra.sekvens;
    this.til=til.sekvens;
    this.forsteOvergangTid=til.forste.difference(fra.forste);
    this.fraRaw=fra.rawLinje;
    this.tilRaw=til.rawLinje;
  }

}

class TidAS implements Comparable<TidAS> {
int time;
int minutt;
String tid;
public TidAS(String tid){

    if(tid.length()!=5){
        System.out.println("hmmmm");
        System.out.println(tid);
        System.exit(0);
    }

    this.tid=tid;
    String[] ar = tid.split(":");
    this.time= Integer.parseInt(ar[0]);
    this.minutt=Integer.parseInt(ar[1]);
}

public boolean erMindreEnn(TidAS other){
  //"this er mindre enn parameter tiden"
  if(tid.equals(other.tid)) return false;
  if(time < other.time) return true;
  if(time==other.time && minutt < other.minutt) return true;
  return false;
}

public boolean erStorreEnn(TidAS other){
  //"this er storre enn parameter tiden"
  if(tid.equals(other.tid)) return false;
  if(time > other.time) return true;
  if(time==other.time && minutt > other.minutt) return true;
  return false;
}

public int difference(TidAS other){
  if(time==other.time){
    return (minutt-other.minutt);
  }else{
    int timedif = time-other.time;
    timedif=timedif*60;
    return (timedif + (minutt-other.minutt));
  }
}



@Override
public boolean equals(Object other){
  TidAS realOther = (TidAS) other;
  return tid.equals(realOther.tid);
}

@Override
public int compareTo(TidAS other){
    if((time-other.time)!=0) return (time-other.time)*61;
    return (minutt-other.minutt);
}

@Override
public String toString(){
    if(time>23){
        String help= "" + (time-24);
        if(help.length()==1){
            if(minutt>9){
                return "0" + help + ":" + minutt;
            }else{
                return "0" + help + ":0" + minutt;
            }

        }else{
            if(minutt>9){
                return "" + help + ":" + minutt;
            }else{
                return "" + help + ":0" + minutt;
            }
        }
    }else{
        return tid;
    }
}

@Override
public int hashCode(){
    return tid.hashCode();
}

}

/*
//finner mest vanlige antall tider
HashMap<Integer,Integer> antall = new HashMap<>(); //key= lengde, value = forekomster
int mestvanligeLengde=-1;
for(SekvensTidAS st : sekvensTidASer){
  int lokalLengde= st.tider.length;
  if(antall.keySet().contains(lokalLengde)){
    antall.put(lokalLengde, antall.get(lokalLengde)+1);
  }else{
    antall.put(lokalLengde, 1);
  }
}

int benchMax=0;
for(Integer i : antall.keySet()){
  if(antall.get(i).intValue() > benchMax ){
    benchMax=antall.get(i).intValue();
    mestvanligeLengde=i;
  }
}
*/
