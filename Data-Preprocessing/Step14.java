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

import java.util.Calendar;
import java.util.Date;

//Ikke alle ruter i FINAL_ROUTES skal kunne twistes. legger til en indikator i filen
//1=twistable, 0=not twistable
class Step14{

  public static void main(String[] args) throws Exception {

    System.out.println("Analyserer og lagrer alle ruters twistbarhet...");

    ArrayList<String> rutetranslator = new ArrayList<>();
    Scanner inn = new Scanner(new File("FINAL_RUTETRANSLATOR.txt"));

    while(inn.hasNextLine()){
      rutetranslator.add(inn.nextLine());
    }

    ArrayList<String> nyFinalRoutes = new ArrayList<>();
    inn = new Scanner(new File("FINAL_ROUTES.txt"));
    while(inn.hasNextLine()){
      String pureLinje = inn.nextLine();
      String[] ar = pureLinje.split(",");
      String translatorID = ar[0];

      //finner rutekoblingen i translatoren
      boolean funnet0=false;
      boolean funnet1=false;
      for(int i = 0; i< rutetranslator.size() && (funnet0==false || funnet1==false); i++){
        String[] rar = rutetranslator.get(i).split(",");
        if(rar[0].equals(translatorID)){
          if(rar[2].equals("0")){
            funnet0=true;
          }else if(rar[2].equals("1")){
            funnet1=true;
          }else{
            System.out.println("unexpected, force closer");
            System.exit(0);
          }
        }
      }
      if(funnet0 && funnet1){
        nyFinalRoutes.add(pureLinje + ",1");
      }else{
        nyFinalRoutes.add(pureLinje + ",0");
      }

    }

    PrintWriter ut = new PrintWriter(new File("FINAL_ROUTES.txt"));
    for(String s : nyFinalRoutes){
      ut.println(s);
    }
    ut.close();


    //newest

    System.out.println("Legger til info om siste oppdatering");

    Calendar c = Calendar.getInstance();

    int y = c.get(Calendar.YEAR);
    int m = c.get(Calendar.MONTH);
    m++;
    int d = c.get(Calendar.DAY_OF_MONTH);
    String dato = String.format("%02d.%02d.%04d", d, m ,y);
    ut = new PrintWriter(new File("FINAL_UPDATE_DATE.txt"));
    ut.println(dato);
    ut.close();
    System.out.println("datoen i dag (dobbelsjekk): " + dato);
    System.out.println("#### Step14 er ferdig ####");
  }

}
