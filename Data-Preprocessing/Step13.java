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

//rutetranslatoren er ikke skrevet i rekkefolge og inneholder duplikater. Fikser det
class Step13{

  public static void main(String[] args) throws Exception {

    Scanner inn = new Scanner(new File("nestenFINAL_RUTETRANSLATOR.txt"));

    LinkedHashSet<TranslatorLinje> linjer = new LinkedHashSet<>();
    while(inn.hasNextLine()){
      linjer.add(new TranslatorLinje(inn.nextLine()));
    }

    ArrayList<TranslatorLinje> step2= new ArrayList<>();

    step2.addAll(linjer);

    Collections.sort(step2);

    PrintWriter ut = new PrintWriter(new File("FINAL_RUTETRANSLATOR.txt"));
    int lastRute=-1;
    for(TranslatorLinje tl : step2){
      if(lastRute!=-1 && tl.rutetiderID!=lastRute+1){
        System.out.println("error funnet ved linje:" + tl.linje + "\n force closer");
        System.exit(0);
      }
      ut.println(tl.linje);
      lastRute=tl.rutetiderID;
    }
    ut.close();

  }

}
class TranslatorLinje implements Comparable<TranslatorLinje>{
  String linje;
  int rutetiderID;
  int index;

  public TranslatorLinje(String linje){
    this.linje=linje;
    String[] ar = linje.split(",");
    this.rutetiderID = Integer.parseInt(ar[1]);
    this.index= Integer.parseInt(ar[3]);
  }

  @Override
  public int compareTo(TranslatorLinje other){
    if((rutetiderID - other.rutetiderID)!=0 ){
      return rutetiderID - other.rutetiderID;
    }else{
      return index - other.index;
    }
  }
  @Override
  public boolean equals(Object other){
    TranslatorLinje realOther = (TranslatorLinje) other;
    //return linje.equals(realOther.linje);
    return rutetiderID ==realOther.rutetiderID;
  }
  @Override
  public int hashCode(){
    //return linje.hashCode();
    return rutetiderID;
  }


}
