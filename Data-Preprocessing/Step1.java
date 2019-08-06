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

//endrer bare rekkefolge og fjerner overflodig info
//PER 05.08, ENDRET TIL Å LEGGE TIL EN SERVICEID ETTER RUTEIDEENE
class Step1{


  public static void main(String[] args) throws Exception {


  HashMap<String,String> ruteTilService = new HashMap<>(); //rute, serviceID
  Scanner inn = new Scanner(new File("trips.txt"));
  inn.nextLine();
  while(inn.hasNextLine()){
    String[] ar = inn.nextLine().split(",");
    ruteTilService.put(ar[2], ar[1]);
  }

  Scanner leser = new Scanner(new File("stop_times.txt"));
  PrintWriter skriver = new PrintWriter(new File("step1.txt"));

  leser.nextLine();
  System.out.println("leser gjennom og omformaterer stop_times.txt..");
  int ferdigLinjer=0;
  while(leser.hasNextLine()){
      String[] ar = leser.nextLine().split(",");
      String ruteID=ar[0];
      String service = ruteTilService.get(ruteID);
      if(service==null){
        System.out.println("fant ikke ruteIDeen:" + ruteID + ".");
        System.exit(0);
      }
      skriver.println(ar[0] + "." + service + "," + ar[5].substring(1,ar[5].length()-1) + "," + ar[4] + "," + ar[3] + "," + ar[2]);
      //ferdigLinjer++;
      //if((ferdigLinjer % 10000) == 0) System.out.println("progress: " + ferdigLinjer);
  }
  skriver.close();
  System.out.println("#### Step1 er ferdig ####");

  }


}
