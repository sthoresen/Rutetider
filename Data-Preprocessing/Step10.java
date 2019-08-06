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

class Step10{

  public static void main(String[] args) throws Exception {

    System.out.println("Forbedrer IDer i FINAL_STOPS...");

    Scanner inn = new Scanner(new File("FINAL_STOPS.txt"));
    ArrayList<String> linjer = new ArrayList<>();
    HashMap<String,Integer> oldToNew = new HashMap<>();

    int teller=0;
    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");
      oldToNew.put(ar[0], teller);
      try{
        linjer.add(teller + "," + ar[1] + "," + ar[2]);
      }catch(Exception e){
        linjer.add(teller + "," + ar[1] + ",");
      }
      teller++;
    }
    PrintWriter ut = new PrintWriter(new File("FINAL_STOPS.txt"));
    for(String s : linjer){
      ut.println(s);
    }
    ut.close();
    linjer=null;

    System.out.println("Matcher ID-forandringene i FINAL_RUTETIDER...");

    inn = new Scanner(new File("FINAL_RUTETIDER.txt"));
    linjer = new ArrayList<>();
    while(inn.hasNextLine()){
      String linje = inn.nextLine();
      int firstKomma= linje.indexOf(",");
      int secondKomma = linje.indexOf("," , firstKomma+1);
      int thirdKomma = linje.indexOf("," , secondKomma+1);
      String[] ar = linje.split(",");

      linjer.add(linje.substring(0,secondKomma+1) + (oldToNew.get(ar[2])) + linje.substring(thirdKomma));
    }

    ut = new PrintWriter(new File("FINAL_RUTETIDER.txt"));
    for(String s : linjer){
      ut.println(s);
    }
    ut.close();

    System.out.println("#### Step10 er ferdig ####");



  }

}
