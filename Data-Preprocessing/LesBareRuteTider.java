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

class LesBareRuteTider{

  public static void main(String[] args) throws Exception {

    PrintWriter ut = new PrintWriter(new File("FINAL_LESBARE_RUTETIDER.txt"));

    System.out.println("henter alle stasjons navn til minne..");
    //key=vanilla StopID, value=stopNavn
    HashMap<String,String> translate = new HashMap<>();
    Scanner inn = new Scanner(new File("stops.txt"));
    inn.nextLine();
    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");
      translate.put(ar[0], ar[1].substring(1, ar[1].length()-1));
    }


    System.out.println("skriver filen..");
    inn = new Scanner(new File("step4.txt"));
    while(inn.hasNextLine()){
      String[] ar = inn.nextLine().split(",");
      try{
        ut.println(ar[0] + "," + ar[1] + "," + ar[2] + "," + translate.get(ar[3])
         + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," +ar[7] + "," +ar[8]);
         continue;
      }catch(Exception e){}

      try{
        ut.println(ar[0] + "," + ar[1] + "," + ar[2] + "," + translate.get(ar[3])
         + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," +ar[7] + ",");
         continue;
      }catch(Exception e){}

      try{
        ut.println(ar[0] + "," + ar[1] + "," + ar[2] + "," + translate.get(ar[3])
         + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," + ",");
         continue;
      }catch(Exception e){
        ut.println(ar[0] + "," + ar[1] + "," + ar[2] + "," + translate.get(ar[3])
         + "," + ar[4] + "," + ar[5] + "," + "," + ",");
      }


    }
    ut.close();

  }



}
