package com.company;

import java.io.*;
import java.util.*;

public class AprioriAlgorithm {

    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        try {
            AprioriAlgorithm aprioriAlgorithm = new AprioriAlgorithm();
            aprioriAlgorithm.configuration(args);
            aprioriAlgorithm.implementAlgorithm();
            long endTime = System.currentTimeMillis();
        }
        catch(Exception e){
            e.printStackTrace();
        }
       // System.out.println("Execution Time: " + (endTime-startTime));
    }
    //minimum support by user
    private int min_support;
    //no of objects in a row to be entered by user
    private int k;
    //input file path
    private String inputFilePath;
    //output file path
    private String outputFilePath;

    //list of transactions
    List<Set<String>> transactionList = new ArrayList<Set<String>>();

    private PrintStream ps;

    private void configuration(String[] args) throws Exception{
        if(args.length!=0){
            min_support = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);
            inputFilePath = args[2];
            outputFilePath = args[3];
        }else{
            //default values
            min_support = 4;
            k=3;
            inputFilePath ="/home/ashna/Desktop/transactionDB.txt";
            outputFilePath = "/home/ashna/Desktop/output.txt";
        }

        //loading the file;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath));

        ps = new PrintStream(new File(outputFilePath));
        System.setOut(ps);

        //reading the file
        List<String> oneLine = new ArrayList<String>();
        while(bufferedReader.ready()){
            String line = bufferedReader.readLine();
            if(line != null){
                oneLine = Arrays.asList(line.split(" "));
                transactionList.add(new HashSet<>(oneLine));
            }
        }
        bufferedReader.close();
    }

    private void implementAlgorithm()throws Exception{
        FrequentItemset<String> data = generate(transactionList,min_support);
        /*PrintStream ps = new PrintStream(new File(outputFilePath));
        System.setOut(ps);*/
        for(Set<String> frequentdataset: data.getFrequentItemsetList()){
            for(String itemset:frequentdataset){
                System.out.print(itemset+" ");
            }
            System.out.print("("+data.getSupport(frequentdataset)+")\n");
        }
    }

    //generate method to obtain first scan of data
    public FrequentItemset<String> generate(List<Set<String>> transactionList,int min_support){

        // System.out.println("In Generate");
        Map<Set<String>,Integer> minSupportCountMap = new HashMap<Set<String>,Integer>();

        List<Set<String>> frequentItemsetList = findFrequentItemSets(transactionList,min_support,minSupportCountMap);

        //number of iteratiors(k) map


        int iterations =1;
        List<Set<String>> candidate_set;
        List<Set<String>> candidate_Set2;
        do{
//            Runtime.getRuntime().gc();
            iterations++;
            candidate_set = generate_candidates(frequentItemsetList);
            frequentItemsetList.clear();
            for(Set<String> oneLine : transactionList){

                candidate_Set2 = subset(candidate_set,oneLine);
                for(Set<String> candidate_2: candidate_Set2){
                    minSupportCountMap.put(candidate_2,minSupportCountMap.getOrDefault(candidate_2,0)+1);
                }

            }
            frequentItemsetList = filterCandidateSet(candidate_set,min_support,minSupportCountMap);
            candidate_set.clear();
        }while (iterations == k);
        return new FrequentItemset<>(extractFrequentItemSets(frequentItemsetList),minSupportCountMap,min_support);
    }

    //method to find frequent itemsets according to minimum support provided by user
    private List<Set<String>> findFrequentItemSets(List<Set<String>> transactionList,int min_support,Map<Set<String>,Integer> minSupportCountMap){
        //System.out.println("In findFrequentItemSets");
        Map<String,Integer> wordMap = new HashMap<String, Integer>();

        //count frequency of each item
        for(Set<String> oneLine : transactionList){

            for(String productIds : oneLine){

                Set<String> tempSet = new HashSet<>(1);
                tempSet.add(productIds);

                if(minSupportCountMap.containsKey(tempSet)){
                    minSupportCountMap.put(tempSet, minSupportCountMap.get(tempSet) + 1);
                }
                else{
                    minSupportCountMap.put(tempSet,1);
                }
                wordMap.put(productIds,wordMap.getOrDefault(productIds,0)+1);
            }
        }

        List<Set<String>> frequentItemsetList = new ArrayList<>();
        for(Map.Entry m:wordMap.entrySet()){

            if(Integer.parseInt(m.getValue().toString()) >= min_support){
                Set<String> itemSet = new HashSet<>(1);
                itemSet.add(m.getKey().toString());
                frequentItemsetList.add(itemSet);
            }
        }

        return frequentItemsetList;
    }

    private List<Set<String>> generate_candidates(List<Set<String>> frequencyItemsetList){
        //System.out.println("In Generate Candidates");
        List<List<String>> newList = new ArrayList<>(frequencyItemsetList.size());

        for(Set<String> setOfItems : frequencyItemsetList){
            List<String> tempList  = new ArrayList<>(setOfItems);
            Collections.<String>sort(tempList, Sort_Comparator);
            newList.add(tempList);
        }

        int newListSize =newList.size();

        List<Set<String>> mergedList = new ArrayList<>(newListSize);

        for(int i=0; i<newListSize; i++){
            for(int j=i+1;j<newListSize;j++){

                Set<String> candidate = mergeLists(newList.get(i),newList.get(j));

                if(candidate !=null){
                    mergedList.add(candidate);
                }
            }
        }
        newList.clear();
        return mergedList;
    }

    private static final Comparator Sort_Comparator = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };
    private Set<String> mergeLists(List<String> firstList,List<String> secondList){
        int firstListSize = firstList.size();

        for(int i=0;i<firstListSize-1;++i){
            //if there is no intersection between lists
            if(!firstList.get(i).equals(secondList.get(i))){
                return  null;
            }
        }

        //if last element of both lists are same
        if(firstList.get(firstListSize-1).equals(secondList.get(firstListSize-1))){
            return null;
        }

        Set<String> mergedSets = new HashSet<>(1);
        for(int i=0;i<firstListSize -1;++i){
            //adding all elements which are common to both lists
            mergedSets.add(firstList.get(i));
        }

        //adding last elements of lists if different
        mergedSets.add(firstList.get(firstListSize-1));
        mergedSets.add(secondList.get(firstListSize-1));

        return mergedSets;
    }

    private List<Set<String>> subset(List<Set<String>> candidate_set,Set<String> oneLine){
        List<Set<String>> candidate_Set_2 = new ArrayList<>(candidate_set.size());

        for(Set<String> candidate : candidate_set){

            if(oneLine.containsAll(candidate)){
                candidate_Set_2.add(candidate);
            }
        }

        return candidate_Set_2;

    }

    private List<Set<String>> filterCandidateSet(List<Set<String>> candidate_set,int min_support,
                                                 Map<Set<String>,Integer> minSupportCountMap){

        //System.out.println("In getFilterCandidateSet");
        List<Set<String>> frequencyItemSetList = new ArrayList<>(candidate_set.size());

        for(Set<String> candidate : candidate_set){

            if(minSupportCountMap.containsKey(candidate)){
                int frequency = minSupportCountMap.get(candidate);
                if(frequency >= min_support){
                    frequencyItemSetList.add(candidate);
                }
            }
        }

        return  frequencyItemSetList;
    }


    private List<Set<String>> extractFrequentItemSets(List<Set<String>> iteratorList){
        //System.out.println("In extractFrequentItemSets");
        List<Set<String>> finalItems = new ArrayList<>();
        finalItems.addAll(iteratorList);
        return finalItems;
    }

    //Initialize the class to generate getter methods of variables
    public class FrequentItemset<T>{

        private final List<Set<T>> frequentItemsetList;
        private final Map<Set<T>,Integer> minSupportCountMap;
        private final int minimumSupport;

        FrequentItemset(List<Set<T>> frequentItemsetList,Map<Set<T>,Integer> minSupportCountMap,int minimumSupport){
            this.frequentItemsetList = frequentItemsetList;
            this.minSupportCountMap = minSupportCountMap;
            this.minimumSupport = minimumSupport;
        }

        public List<Set<T>> getFrequentItemsetList() {
            return frequentItemsetList;
        }

        public Map<Set<T>, Integer> getMinSupportCountMap() {
            return minSupportCountMap;
        }

        public int getMinimumSupport() {
            return minimumSupport;
        }

        public int getSupport(Set<T> itemset)
        {
            return minSupportCountMap.get(itemset);
        }
    }
}
