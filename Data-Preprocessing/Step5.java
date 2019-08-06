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

//forbedrer stops filen
class Step5{

  public static void main(String[] rags) throws Exception {

    Scanner hoved = new Scanner(new File("stop_times.txt"));
    Scanner stopScanner = new Scanner(new File("stops.txt"));
    PrintWriter skriver = new PrintWriter(new File("stops_gen.txt"));

    Set<String> stopIDs = new HashSet<>();
    HashMap<String, String> allStops = new HashMap<>();

System.out.println("leser hovedfil..");
    hoved.nextLine();
    while(hoved.hasNextLine()){
      String[] ar = hoved.nextLine().split(",");
        stopIDs.add(ar[3]);
    }

System.out.println("leser stopfil..");
    stopScanner.nextLine();
    while(stopScanner.hasNextLine()){
      String[] ar = stopScanner.nextLine().split(",");
      allStops.put(ar[0], ar[1].substring(1, ar[1].length()-1) + "," + ar[4] );
    }

System.out.println("parer");
    for(String s : stopIDs){
      skriver.println(s + "," + allStops.get(s));
    }
    skriver.close();
    System.out.println("#### Step5 er ferdig ####");


  }

}
