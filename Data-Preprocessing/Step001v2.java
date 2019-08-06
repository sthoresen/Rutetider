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

//Minimaliserer calendar.txt og matcher forandringene med trips.txt
//v2 endret til aa ta hensyn til exceptions i calendar_dates.txt
class Step001v2{


  public static void main(String[] args) throws Exception {

    System.out.println("v2 pre operasjoner...");
    Scanner inn = new Scanner(new File("calendar_dates.txt"));
    inn.nextLine();
    //beholder forste linjen i calendar
    HashMap<String,String> exceptions = new HashMap<>(); //key=ruteID, value = (Exception datoer+type) (spacet) , exception type
    while(inn.hasNextLine()){
      String linje = inn.nextLine();
      String[] ar = linje.split(",");
      if(ar[1].length()!=8){
        System.out.println(linje + ". har ikke 8-sifret dato. Change format");
        System.exit(0);
      }
      if(exceptions.keySet().contains(ar[0])){
        exceptions.put(ar[0], exceptions.get(ar[0]) + " " +ar[1] + ar[2]);
      }else{
        exceptions.put(ar[0], ar[1] + ar[2]);
      }
    }


    inn = new Scanner(new File("calendar.txt"));
    ArrayList<String> output = new ArrayList<>();
    inn.nextLine();

    int last=0;
    while(inn.hasNextLine()){
      String linje = inn.nextLine();
      String id = linje.substring(0, linje.indexOf(","));
      String ex = exceptions.get(id);
      if(ex==null){
        output.add(linje + ",");
      }else{
        output.add(linje + "," + exceptions.get(id));
      }
      if(last!=Integer.parseInt(id)-1){
        System.out.println("service IDER øker ikke med 1 ved: " + last);
        System.exit(0);
      }
      last=Integer.parseInt(id);
    }

    PrintWriter outpre = new PrintWriter(new File("FINAL_SERVICE_IDS.txt"));
    for(String s : output){
      outpre.println(s);
    }
    outpre.close();


    System.out.println("#### Step001 er ferdig ####");
  }


}
