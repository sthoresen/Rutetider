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

class TestForMinkendeTid{

  public static void main(String[] args) throws Exception {

    //sjekkstoptimes();
    sjekkstep3(); //sjekker bare innenfor samme sekvens. dvs ikke sekvens 1 mot sekvens 2 osv.

  }


  public static void sjekkstep3() throws Exception {
    Scanner inn = new Scanner(new File("step3.txt"));
    System.out.println("sjekker step3");

    String currentRuteID=null;
    String currentSekvens=null;
    String currentLinje=inn.nextLine();
    String[] ar = currentLinje.split(",");

    while(inn.hasNextLine()){

      currentRuteID=ar[0];
      currentSekvens=ar[2];
      TidTest sisteTid=null;
      while(ar[2].equals(currentSekvens) && ar[0].equals(currentRuteID) && inn.hasNextLine()){
        if(sisteTid==null){
          sisteTid = new TidTest(ar[4]);
        }else{
          TidTest x =new TidTest(ar[4]);
          //System.out.println("sammenligner " + x.toString() + " og " + sisteTid.toString());
          if(x.compareTo(sisteTid) <0  ){
            System.out.println("feil ved linje: " + currentLinje);
            System.exit(0);
          }
          sisteTid = x;
        }
        currentLinje=inn.nextLine();
        //System.out.println(currentLinje);
        ar= currentLinje.split(",");
      }


    }
  }



  public static void sjekkstoptimes() throws Exception {
    Scanner inn = new Scanner(new File("stop_times.txt"));
    inn.nextLine();
    System.out.println("sjekker stop_times");

    String currentRuteID=null;
    String currentLinje=inn.nextLine();
    String[] ar = currentLinje.split(",");

    while(inn.hasNextLine()){

      currentRuteID=ar[0];
      TidTest sisteTid=null;
      while(ar[0].equals(currentRuteID) && inn.hasNextLine()){
        if(sisteTid==null){
          sisteTid = new TidTest(ar[2]);
        }else{
          TidTest x =new TidTest(ar[2]);
          //System.out.println("sammenligner " + x.toString() + " og " + sisteTid.toString());
          if(x.compareTo(sisteTid) <0  ){
            System.out.println("feil ved linje: " + currentLinje);
            System.exit(0);
          }
          sisteTid = x;
        }
        currentLinje=inn.nextLine();
        //System.out.println(currentLinje);
        ar= currentLinje.split(",");
      }


    }
  }
}

class TidTest implements Comparable<TidTest> {
    String tid;
    int time;
    int minutt;

    public TidTest(String tid){

        this.tid=tid;
        String[] ar = tid.split(":");

        this.time= Integer.parseInt(ar[0]);
        this.minutt=Integer.parseInt(ar[1]);
    }

    public TidTest tidEtter(int intervall){

        int nyTime;
        int nyMinutt;

        if(intervall>=0){
            nyTime=this.time;
            nyMinutt=this.minutt+intervall;
        }else{
            nyTime=this.time;

            if(minutt < Math.abs(intervall)){
                nyTime--;
            }

            nyMinutt=this.minutt - Math.abs(intervall);
        }


        while(nyMinutt > 59){
            nyMinutt=nyMinutt-60;
            nyTime++;
        }
        if(nyTime <10 && nyMinutt<10 ){
            return new TidTest("" + "0" + nyTime + ":" + "0" + nyMinutt);
        }else if(nyTime <10 && nyMinutt>=10 ){
            return new TidTest("" + "0" + nyTime + ":" + nyMinutt);
        }else if(nyTime >=10 && nyMinutt>=10 ){
            return new TidTest(nyTime + ":" + nyMinutt);
        }else{
            return new TidTest(nyTime + ":" + "0" + nyMinutt);
        }

    }



    @Override
    public int compareTo(TidTest other){
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
        TidTest realOther = (TidTest) other;
        return time==realOther.time && minutt==realOther.minutt;
    }

    @Override
    public int hashCode(){
        return tid.hashCode();
    }

    public boolean erMindreEllerLik(TidTest other){
        int minTime=time;
        while(minTime>23){
            minTime=minTime-24;
        }

        int otherTime=other.time;
        while(otherTime>23){
            otherTime=otherTime-24;
        }

        if(minTime > otherTime) return false;
        if(minutt < other.minutt || minutt==other.minutt){
            return true;
        }
        return false;
    }

}
