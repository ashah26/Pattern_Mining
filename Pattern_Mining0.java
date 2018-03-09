import java.io.*;
import java.util.*;
public class Pattern_Mining0{

	public static void main(String[] args) throws IOException
	{
		int min_supp=Integer.parseInt(args[0]);
		int k=Integer.parseInt(args[1]);
		String input_file=args[2];
		String output_file=args[3];
		try
		{
			//loading the file
			File file = new File("/home/ashna/Desktop/sample.txt");
			//reading the file
			BufferedReader b= new BufferedReader(new FileReader(file));
			//output file
			PrintStream ps = new PrintStream(new File("Output.txt"));
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
		 	List<Set<Integer>> frequencySet1 = new ArrayList<Set<Integer>>();
			frequencySet1=first_frequencyset_generation(candidateSet1,frequencySet1,min_supp);

			//generating other candidateSet with frequency
			Map<TreeSet<Integer>,Integer> candidate_set = new TreeMap <TreeSet<Integer>,Integer>();
			candidate_generation(frequencySet1,candidate_set,transactionsList);
			//generating other frequencySet

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

static List<Set<Integer>> first_frequencyset_generation(Map<Integer,Integer> candidateSet1,
List<Set<Integer>> frequencySet1,int min_supp){
	for(Map.Entry m:candidateSet1.entrySet()){
		int value1 = Integer.parseInt(m.getValue().toString());
		if(value1 >= min_supp){
			Set<Integer> newSet = new TreeSet<Integer>();
			newSet.add(Integer.parseInt(m.getKey().toString()));
			frequencySet1.add(newSet);
		}
	}

	return frequencySet1;
}

static void candidate_generation(List<Set<Integer>> frequencySet1,
Map<TreeSet<Integer>,Integer> candidate_set,List<Set<Integer>> transactionsList){
List<Set<Integer>> newList = new ArrayList<Set<Integer>>();
for(Set s : frequencySet1){
	for(int i=0;i<frequencySet1.size();i++){
		if(!s.containsAll(frequencySet1.get(i))){
		Set<Integer> unionSet = new TreeSet<Integer>(s);
		unionSet.addAll(frequencySet1.get(i));
		if(!newList.contains(unionSet))
			newList.add(unionSet);
	}
	}
}
	for(int i=0;i<transactionsList.size();i++){
		for(int j=0;j<newList.size();j++){
				TreeSet<Integer> newListElement = new TreeSet<Integer>(newList.get(j));
				TreeSet<Integer> intersectionSet = new TreeSet<Integer>(transactionsList.get(i));
				intersectionSet.retainAll(newListElement);

				for(Map.Entry m:candidate_set.entrySet()){
					TreeSet<Integer> comparingSet = new TreeSetSet<Integer>(m.getKey());

					if(comparingSet.containsAll(newListElement)){
					candidate_set.put(newListElement,candidate_set.get(newListElement)+1);
				}else{
					candidate_set.put(newListElement,1);
				}
			}
		}

	}

System.out.println("gggg");
for(Map.Entry m:candidate_set.entrySet()){
TreeSet<Integer> newListElement = new TreeSet<Integer>(m.getKey());
newListElement.forEach(System.out::println);
System.out.println("ddddd"+m.getValue().toString());
		//System.out.println(m.getKey().toString()+"----"+m.getValue().toString());
 }


//return candidate_set;
}

// static ArrayList<Integer[]> candidateGeneration(List<Integer[]> frequencySet1,int n,int r){
// 	Integer[] data=new Integer[r];
// 	ArrayList<Integer[],Integer> candidateSet = new ArrayList<Integer[],Integer>();
// 	int count=1;
// 	combinationUtil(frequencySet1,candidateSet,data,0,n-1,0,r,count);
// 	return candidateSet;
// }
//  static void combinationUtil(List<Integer[]> frequencySet1,ArrayList<Integer[],Integer> candidateSet,
//  Integer[] data,int start,int end,int index,int r,int count){
// 	 if(index == r){
// 		 candidateSet.add(Arrays.copyOf(data,data.length),count);
// 		 count++;
// 		 return;
// 		}
// 	 	for(int i=start;i<=end && end-i+1>=r-index;i++){
// 			Integer[] newArray = frequencySet1.get(i);
// 			for(int j=0;j<newArray.length;j++){
// 					data[index] =newArray[j];
// 					//index++;
// 			}
//
// 	 		combinationUtil(frequencySet1,candidateSet,data,i+1,end,index+1,r);
// 		}
// }

}
