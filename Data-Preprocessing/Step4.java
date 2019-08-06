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

//finner monster i tider og forkorter ved det
class Step4{

  public static void main(String[] args) throws Exception {

    //DEBUG!!!!! sekvens under (11.08)
    //key=ruteID+ sekvens +Stasjon, value= tid-data (first,last,commons,extra,doesntExist)
    HashMap<String,String> hm = new HashMap<>();
    Scanner inn = new Scanner(new File("step3.txt"));
    int tidLinjerFerdige=0;

    String linje = inn.nextLine();
    linje=linje.substring(0, linje.length()-3); //fjerner sekunder
    String[] ar = linje.split(",");
    String ruteID=ar[0];
    System.out.println("Analyserer tider..");
    while(inn.hasNextLine()){
      String benchmarkRuteID=ar[0];
      String benchmarkStasjon=ar[3]; //fiks denne da jeg fjeren sekvens
      String benchmarkSekvens=ar[2]; // <------11.08 debug
      ArrayList<Tid> tider = new ArrayList<>();
      while(ar[0].equals(benchmarkRuteID) && ar[3].equals(benchmarkStasjon) && ar[2].equals(benchmarkSekvens) && inn.hasNextLine()){
        Tid t = new Tid(linje);
        //if(ar[0].equals("2078.4")) System.out.println("la til tiden: " + t.toString());    //debug
        tider.add(t);
        linje=inn.nextLine();
        linje=linje.substring(0, linje.length()-3); //fjerner sekunder
        ar=linje.split(",");
      }

      //-DEBUG-
      if(!inn.hasNextLine()) tider.add(new Tid(linje));
      //-DEBUG-

      //Har naa alle tidene i en blokk
      ArrayList<Integer> commons = new ArrayList<>();
      ArrayList<Tid> extra = new ArrayList<>();
      ArrayList<Tid> doesntExist = new ArrayList<>();
      int[] basetider = new int[60];
      int sisteTime=tider.get(tider.size()-1).time;
      int forsteTime=tider.get(0).time;

      //registrerer forekomster av minutttider uavhengig av time
      for(int base=0; base<basetider.length; base++){
        for(Tid t : tider){
          if(t.minutt==base){
            basetider[base]++;
          }
        }
      }

      //beslutter commom verdier paa bakgrunn av forekomster
      for(int i=0; i<basetider.length; i++){
        int lengde;
        if((sisteTime-forsteTime)==0){
          lengde=0;
        }else if((sisteTime-forsteTime)==1){
          lengde=1;
        }else{
          lengde=(int)((sisteTime-forsteTime)/2);
        }
        if(basetider[i]>lengde){
          commons.add(new Integer(i));
        }
      }

      //finner extra tider ikke beskrevet av commons eller start(slutt)
      for(Tid t : tider){
        if(!(commons.contains(new Integer(t.minutt))) && t!=tider.get(0) && t!=tider.get(tider.size()-1)){
          extra.add(t);
        }
      }

      //Finner tider beskrevet av commons som faktisk ikke eksisterer
      for(Integer ex : commons){
        for(int i= forsteTime; i<=sisteTime; i++){
          /*  -------------ORIGINAL PRE HYTTA TIL LIN VELDIG LATE DEBUGGING
          if(!(ex.intValue()<=tider.get(0).minutt ||ex.intValue()>=tider.get(tider.size()-1).minutt)){
            String rawTid;
            if(i<10 && ex.intValue()<10){
              rawTid= "x,x,x,x," + "0" + i +":" + "0" +ex;
            }else if(i<10 && ex.intValue()>=10){
              rawTid= "x,x,x,x," + "0" + i +":" +ex;
            }else if(i>=10 && ex.intValue()>=10){
              rawTid= "x,x,x,x," + i +":" +ex;
            }else{
              //i=>10 ex.intValue()<10
              rawTid= "x,x,x,x," + i +":0" +ex;
            }
            if(!tider.contains(new Tid(rawTid))) doesntExist.add(new Tid(rawTid));
          }
          */

          //-DEBUG-
          String rawTid;
          int commonVerdi = ex.intValue();
          if(i<10 && commonVerdi<10){
            rawTid= "x,x,x,x," + "0" + i +":" + "0" +commonVerdi;
          }else if(i<10 && commonVerdi>=10){
            rawTid= "x,x,x,x," + "0" + i +":" +commonVerdi;
          }else if(i>=10 && commonVerdi>=10){
            rawTid= "x,x,x,x," + i +":" +commonVerdi;
          }else{
            //i=>10 ex.intValue()<10
            rawTid= "x,x,x,x," + i +":0" +commonVerdi;
          }
          Tid genTid = new Tid(rawTid);
          Tid forsteTid = tider.get(0);
          Tid sisteTid = tider.get(tider.size()-1);
          if(genTid.erMindreEnn(sisteTid) && genTid.erStorreEnn(forsteTid) && !tider.contains(genTid)){
            doesntExist.add(genTid);
          }
          //-DEBUG-

        }
      }

      //end logikk. Bygger naa opp en infostring av informasjonen
      //(first,last,commons,extra,doesntExist)
      String tidInfo = "" +tider.get(0).tid + "," + tider.get(tider.size()-1).tid + ",";
      String commonsString = "";
      for(Integer i : commons){
        int jj=i.intValue();
        if(jj<10){
          commonsString=commonsString + "0" + jj + " ";
        }else{
          commonsString=commonsString + jj + " ";
        }
      }
      if(!commonsString.equals("")){
        //fjerner siste spacing
        commonsString=commonsString.substring(0, commonsString.length()-1);
      }

      String extraString="";
      for(Tid t : extra){
        extraString=extraString + t.tid + " ";
      }
      if(!extraString.equals("")){
        //fjerner siste spacing
        extraString=extraString.substring(0, extraString.length()-1);
      }

      String doesntExistString ="";
      for(Tid t : doesntExist){
        doesntExistString=doesntExistString + t.tid + " ";
      }
      if(!doesntExistString.equals("")){
        //fjerner siste spacing
        doesntExistString=doesntExistString.substring(0, doesntExistString.length()-1);
      }

      tidInfo=tidInfo + commonsString + "," + extraString + "," + doesntExistString;
      tidLinjerFerdige++;
      //System.out.println(tidLinjerFerdige + ".ste tidInfo: " + tidInfo);
      //if(tidLinjerFerdige==51) System.exit(0);

      hm.put(benchmarkRuteID+ ","  +benchmarkSekvens + "," + benchmarkStasjon, tidInfo);
    }
    //skriver til et Set for aa fjerne duplikater
    Scanner follow = new Scanner(new File("step3.txt"));
    Set<String> unikelines = new LinkedHashSet<>();

    System.out.println("setter inn i HashSet..");
    while(follow.hasNextLine()){
      String[] arr = follow.nextLine().split(",");
      String improvedLine = arr[0] + "," + arr[1] + "," + arr[2] + "," + arr[3] + "," + hm.get(arr[0] + "," + arr[2]+ "," +arr[3]); //fix da jeg fjerner sekvens
      if(!improvedLine.equals("696969,xxxxxx,0,696969696969,null")) unikelines.add(improvedLine);
    }
    //skriver naa endelig til fil
    System.out.println("skriver til fil..");
    PrintWriter skriver = new PrintWriter("step4.txt");
    for(String s : unikelines){
      skriver.println(s);
    }
    //System.out.println("done");
    skriver.println("69696969,@@@@@@,696969,696969696969,69:69,69:69,69,,"); //placeholder
    skriver.close();
    System.out.println("#### Step4 er ferdig ####");


  }
}

class Tid{
  int time;
  int minutt;
  String tid;
  public Tid(String linje){
    String[] ar = linje.split(",");
    tid=ar[4];
    String[] tidAr = ar[4].split(":");
    time=Integer.parseInt(tidAr[0]);
    minutt=Integer.parseInt(tidAr[1]);
  }

  public boolean erMindreEnn(Tid other){
    //"this er mindre enn parameter tiden"
    if(tid.equals(other.tid)) return false;
    if(time < other.time) return true;
    if(time==other.time && minutt < other.minutt) return true;
    return false;
  }

  public boolean erStorreEnn(Tid other){
    //"this er storre enn parameter tiden"
    if(tid.equals(other.tid)) return false;
    if(time > other.time) return true;
    if(time==other.time && minutt > other.minutt) return true;
    return false;
  }


  @Override
  public boolean equals(Object other){
    Tid realOther = (Tid) other;
    return tid.equals(realOther.tid);
  }

  @Override
  public String toString(){
    return "" + time + ":" + minutt;
  }

}
