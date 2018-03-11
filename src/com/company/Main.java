package com.company;

import java.util.*;
import  java.io.*;
import java.util.concurrent.ConcurrentHashMap;


public class Main {

    public static void main(String[] args) {


        //int min_supp=Integer.parseInt(args[0]);
        int min_supp=2;
        //int k=Integer.parseInt(args[1]);
        int k =4;
//        String input_file=args[2];
        //      String output_file=args[3];

        try
        {
            //loading the file
            File file = new File("/home/ashna/IdeaProjects/PAttern_Mining/src/com/company/sample.txt");
            //File file = new File("/home/ashna/IdeaProjects/PAttern_Mining/src/com/company/sample.txt");
            //reading the file
            BufferedReader b= new BufferedReader(new FileReader(file));
            //output file
            PrintStream ps = new PrintStream(new File("/home/ashna/IdeaProjects/PAttern_Mining/src/com/company/Output.txt"));
            System.setOut(ps);
            //to store each string assigned to a unique integer
            Map<String,Integer> idHashMap = new TreeMap<String,Integer>();
            //unique ids
            int n=1;

            List<Set<Integer>> transactionsList= new ArrayList<Set<Integer>>();
            String readLine="";
            while((readLine = b.readLine()) != null){
                //for storing different ids of one line
                String[] ids = new String[20];
                ids = readLine.split(" ");
                Set<Integer> oneLine = new HashSet<Integer>();


                for(int i=0;i<ids.length;i++){
                    if(!idHashMap.containsKey(ids[i])){
                        idHashMap.put(ids[i],n);
                        n++;
                    }
                    oneLine.add(idHashMap.get(ids[i]));
                }
                transactionsList.add(oneLine);

            }
            for(Set a:transactionsList){
                System.out.println("oneline:"+a);
            }
            //frequency of each item in transactionsList
            Map<Integer,Integer> candidateSet1 = new TreeMap<Integer,Integer>();
            candidateSet1 = first_candidate_generation(transactionsList,candidateSet1);

            //generating frequencySet-1
            Map<Set<Integer>,Integer> frequencySet1 ;
            frequencySet1=first_frequencyset_generation(candidateSet1,min_supp);
candidateSet1.clear();

            //generating other candidateSet with frequency
            for(int i=2;i<k;i++){
                Map<TreeSet<Integer>,Integer> candidate_set ;

                    candidate_set =candidate_generation(frequencySet1,transactionsList);


                frequencySet1.clear();
                //generating other frequencySet

                frequencySet1 = frequency_generataion(candidate_set,min_supp);
                candidate_set.clear();
                for(Set<Integer> t1:frequencySet1.keySet()){
                    System.out.println("frequency set = "+t1+"value==="+frequencySet1.get(t1));
                }
            }



            //converting integer to strings to print
//            for(Set s:frequency_set.keySet()){
//                for(Iterator<Integer> it = s.iterator(); it.hasNext();){
//                   for(Map.Entry m:idHashMap.entrySet()){
//                       if(m.getValue()==it){
//                           System.out.print(m.getKey());
//                       }
//                   }
//                   System.out.print("("+frequency_set.get(s)+")");
//                }
//            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    static Map<Integer,Integer> first_candidate_generation(List<Set<Integer>> transactionsList,Map<Integer,Integer> candidateSet1){
        for(int i=0;i<transactionsList.size();i++){
            Set<Integer> oneline = transactionsList.get(i);
            for(Object obj:oneline){
                if(candidateSet1.containsKey((Integer)obj)){
                    candidateSet1.put((Integer)obj,candidateSet1.get((Integer)obj)+1);
                }else{
                    candidateSet1.put((Integer)obj,1);
                }
            }
        }
        return candidateSet1;
    }

    static Map<Set<Integer>,Integer> first_frequencyset_generation(Map<Integer,Integer> candidateSet1,int min_supp){
        Map<Set<Integer>,Integer> frequencySet1= new HashMap<Set<Integer>,Integer>();
        for(Map.Entry m:candidateSet1.entrySet()){
            int value1 = Integer.parseInt(m.getValue().toString());
            if(value1 >= min_supp){
                Set<Integer> newSet = new TreeSet<Integer>();
                newSet.add(Integer.parseInt(m.getKey().toString()));
                frequencySet1.put(newSet,value1);
            }
        }

        return frequencySet1;
    }

    static Map<TreeSet<Integer>,Integer> candidate_generation(Map<Set<Integer>,Integer> frequencySet1,
                                    List<Set<Integer>> transactionsList){
        Map<TreeSet<Integer>,Integer> candidate_set = new ConcurrentHashMap<TreeSet<Integer>,Integer>();
        List<TreeSet<Integer>> newList = new ArrayList<TreeSet<Integer>>();
        for(Set s : frequencySet1.keySet()){
               for(Set s1:frequencySet1.keySet()){
                   if(!s.containsAll(s1)){
                       TreeSet<Integer> unionSet = new TreeSet<Integer>(s);
                       unionSet.addAll(s1);
                       if(!newList.contains(unionSet))
                           newList.add(unionSet);
                   }
               }

        }

        for(Set s:newList){
            System.out.println("newList"+s);
        }

        candidate_set  = count_frequency(newList,candidate_set,transactionsList);









return candidate_set;
    }

    static Map<TreeSet<Integer>,Integer> count_frequency(List<TreeSet<Integer>> newList,Map<TreeSet<Integer>,Integer> candidate_set,
                                List<Set<Integer>> transactionsList) {
        for (TreeSet<Integer> s : newList) {
            for (Set ts : transactionsList) {
                Set<Integer> intersectionSet = new TreeSet<Integer>(ts);
                intersectionSet.retainAll(s);

                if (intersectionSet.containsAll(s)) {
                    if (candidate_set.isEmpty()) {
                        candidate_set.put(s, 1);
                    } else {


                        //printing_candidate_set(candidate_set);
                        if (candidate_set.keySet().contains(s)) {
                            for (TreeSet<Integer> m : candidate_set.keySet()) {
                                if (m.equals(s)) {
                                    int i = candidate_set.get(s);
                                    int j = i + 1;

                                    candidate_set.remove(s);
                                    //printing_candidate_set(candidate_set);
                                    candidate_set.put(s, j);
                                    //printing_candidate_set(candidate_set);

                                }
                            }
                        } else {
                            candidate_set.put(s, 1);
                        }
                    }

                }

            }
        }
        //printing_candidate_set(candidate_set);
return candidate_set;
    }



    static void printing_candidate_set(Map<TreeSet<Integer>,Integer> candidate_set){
        for(TreeSet<Integer> m1: candidate_set.keySet()){
            System.out.println("ssss---"+m1+"value= === "+candidate_set.get(m1));
        }
    }

    static Map<Set<Integer>,Integer> frequency_generataion(Map<TreeSet<Integer>,Integer> candidate_set,int min_supp){
        Map<Set<Integer>,Integer> frequencySet = new HashMap<Set<Integer>,Integer>();
        for(TreeSet<Integer> m1: candidate_set.keySet()){
            if(candidate_set.get(m1) >= min_supp){
                frequencySet.put(m1,candidate_set.get(m1));
            }
        }

        return  frequencySet;
    }
}
