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

//godgjor TidSortert
class Step6{

  public static void main(String[] args) throws Exception {

      Scanner inn = new Scanner(new File("step4.txt"));
      PrintWriter help = new PrintWriter(new File("step6Help.txt"));

      //minimaliserer ruteNavn samt. fjerner sekvensmarkering
      System.out.println("minimaliserer ruteNavn samt. fjerner sekvensmarkering..");
      String[] ar = inn.nextLine().split(",");
      while(inn.hasNextLine()){
        String currentLinje=ar[1];
        boolean forsteIterasjon=true;
        while(ar[1].equals(currentLinje) && inn.hasNextLine()){
          String bygg=null;
          if(forsteIterasjon){
              try{
                String s=ar[6];
              }catch(Exception e){
                bygg=ar[0] + "," + currentLinje + "," + ar[3] + "," + ar[4] + "," + ar[5] + ",,,";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
              try{
                String s=ar[7];
              }catch(Exception e){
                //System.out.println("first");
                bygg=ar[0] + "," + currentLinje + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + ",,";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
              try{
                bygg=ar[0] + "," + currentLinje + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," + ar[7] + "," + ar[8];
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }catch(Exception e){
                bygg=ar[0] + "," + currentLinje + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," + ar[7] + ",";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
          }else{
              try{
                String s=ar[6];
              }catch(Exception e){
                bygg=ar[0] + "," + "," + ar[3] + "," + ar[4] + "," + ar[5] + ",,,";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
              try{
                String s=ar[7];
              }catch(Exception e){
                bygg=ar[0] + "," + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + ",,";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
              try{
                bygg=ar[0] + "," + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," + ar[7] + "," + ar[8];
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }catch(Exception e){
                bygg=ar[0] + "," + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," + ar[7] + ",";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
          }

        }
      }
      help.println("69696969,696969,69696969,69:69,69:69,69,,"); //placeholder
      help.close();


      //minimaliserer ruteID
      inn = new Scanner(new File("step6Help.txt"));
      help = new PrintWriter(new File("step6.txt"));

      System.out.println("minimaliserer ruteID..");
      ar = inn.nextLine().split(",");
      while(inn.hasNextLine()){
        String currentTripID=ar[0];
        boolean forsteIterasjon=true;
        while(ar[0].equals(currentTripID) && inn.hasNextLine()){
          String bygg=null;
          if(forsteIterasjon){
              try{
                String s=ar[5];
              }catch(Exception e){
                bygg=currentTripID + "," + ar[1] + "," + ar[2] + "," + ar[3] + "," + ar[4] + ",,,";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
              try{
                String s=ar[6];
              }catch(Exception e){
                bygg=currentTripID + "," + ar[1] + "," + ar[2] + "," + ar[3] + "," + ar[4] + "," + ar[5] + ",,";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
              try{
                bygg=currentTripID + "," + ar[1] + "," + ar[2] + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6]  + "," + ar[7];
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }catch(Exception e){
                bygg=currentTripID + "," + ar[1] + "," + ar[2] + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + ",";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
          }else{
              try{
                String s=ar[5];
              }catch(Exception e){
                bygg="," + ar[1] + "," + ar[2] + "," + ar[3] + "," + ar[4] + ",,,";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
              try{
                String s=ar[6];
              }catch(Exception e){
                bygg="," + ar[1] + "," + ar[2] + "," + ar[3] + "," + ar[4] + "," + ar[5] + ",,";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
              try{
                bygg="," + ar[1] + "," + ar[2] + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + "," + ar[7];
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }catch(Exception e){
                bygg="," + ar[1] + "," + ar[2] + "," + ar[3] + "," + ar[4] + "," + ar[5] + "," + ar[6] + ",";
                forsteIterasjon=false;
                help.println(bygg);
                ar = inn.nextLine().split(",");
                //System.out.println("la til: " + bygg);
                continue;
              }
          }
        }
      }
      help.close();
      System.out.println("#### Step6 er ferdig ####");

  }
}
