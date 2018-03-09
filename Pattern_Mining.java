import java.io.*;
import java.util.*;
public class Pattern_Mining{

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
			//linenumber
			// int lineNumber = 0;
			// LineNumberReader lnr = new LineNumberReader(b);
			// //counting lineNumber
			// while(lnr.readLine() !=null){
			// 	lineNumber++;
			// }
			//System.out.println("Total number of lines:"+lineNumber);
			//assign user id to unique integer
			HashMap<String,Integer> idHashMap = new HashMap<String,Integer>();
			//unique ids
			int n=1;

			List<List<Integer>> transactionsList= new ArrayList<List<Integer>>();
			String readLine="";
			while((readLine = b.readLine()) != null){
				//for storing different ids of one line
				String[] ids = new String[20];
				ids = readLine.split(" ");
				ArrayList<Integer> oneLine = new ArrayList<Integer>();


				for(int i=0;i<ids.length;i++){
					if(!idHashMap.containsKey(ids[i])){
						idHashMap.put(ids[i],n);
						n++;
					}
					oneLine.add(idHashMap.get(ids[i]));
				}

				transactionsList.add(oneLine);

					// if(candidateSet1.containsKey(ids[i])){
					// 	candidateSet1.put(ids[i],candidateSet1.get(ids[i])+1);
					// }else{
					// 	candidateSet1.put(ids[i],1);
					// }

			}
			for(List a:transactionsList){
				System.out.println("oneline:"+a);
			}
			//frequency of each item in transactionsList
			Map<Integer,Integer> candidateSet1 = new TreeMap<Integer,Integer>();
			for(int i=0;i<transactionsList.size();i++){
				List<Integer> oneline = transactionsList.get(i);
				for(int j=0;j<oneline.size();j++){
					if(candidateSet1.containsKey(oneline.get(j))){
						candidateSet1.put(oneline.get(j),candidateSet1.get(oneline.get(j))+1);
					}else{
						candidateSet1.put(oneline.get(j),1);
					}
				}
			}
			//generating candidateSet 1
			List<Integer[]> frequencySet1 = new ArrayList<Integer[]>();
			for(Map.Entry m:candidateSet1.entrySet()){
				int value1 = Integer.parseInt(m.getValue().toString());
				if(value1 >= min_supp){
					frequencySet1.add(new Integer[]{Integer.parseInt(m.getKey().toString())});
				}
		 }
		 for(Integer[] i:frequencySet1){
			 System.out.println("frequencySet1 SET 1:"+Arrays.toString(i));
		 }

		ArrayList<Integer[],Integer> candidateSet = candidateGeneration(frequencySet1,frequencySet1.size(),2);
		// for(Integer[] s:candidateSet){
		// 	System.out.println(Arrays.toString(s));
	 // }
Map<Integer[],Integer> candidateSet2 = new TreeMap<Integer[],Integer>();
	 for(int i=0;i<transactionsList.size();i++){
		 List<Integer> oneline = transactionsList.get(i);
		 for(int j=0;j<candidateSet.size();j++){
			 Integer[] newArray = candidateSet.get(j);
			 boolean flag =true;
			 for(int m=0; m <newArray.length; m++){
				 	 if(!oneline.contains(newArray[m])){
						 	flag = false;
							break;
					 }
			 }
			 if(flag){
				 if(candidateSet2.containsKey(newArray)){
					 System.out.println("ddeeeeee");
					candidateSet2.put(newArray,candidateSet2.get(newArray)+1);
				 }
				 else{
					 System.out.println("qwqqqqqq");
					candidateSet2.put(newArray,1);
				 }

			 }
			 // if(oneline.contains(newArray[0]) && oneline.contains(newArray[1])){
				//  System.out.println("kkkkkk");
				//  //candidateSet2.put(newArray[0],1);
			 // }
		 }
	 }
	 System.out.println("dffdf");
	 for(Map.Entry m:candidateSet2.entrySet()){
		System.out.println(m.getKey()+"--"+m.getValue());
	}

	 // for(Map.Entry m:candidateSet2 .entrySet()){
		//  Integer[] jjj = m.getValue(Integer.parseInt(m.getValue().toString()));
		//  System.out.println(Arrays.toString(jjj));
		//  //int newArray =Integer.parseInt(.toString());
		//  // int value= ;
		//  // System.out.println(value);
		//  // System.out.println(Arrays(m.getKey()).toString()+"-"+value);
	 //  }
		// 	// System.out.println("UNIQUE ID:");
			 // for(Map.Entry m:candidateSet1.entrySet()){
				// System.out.println(m.getKey()+"-"+m.getValue());
			 // }
			//generating candiadate set 1 and frequency set 1
			//
			// ArrayList<Integer> frequencySet1 = new ArrayList<Integer>();
      //
			// 	for(String s:candidateSet1){
      //
			// 			frequencySet1.add(idHashMap.get(s));
			// 	}
      //
      //
			// System.out.println("frequency SET 1:"+frequencySet1);
			// /* Candidateset 2 and frequencySet2 starts */
			// ArrayList<Integer[]> candidateSet2 = new ArrayList<Integer[]>();
			// candidateSet2 =candidateGeneration(frequencySet1,frequencySet1.size(),2);
			//
      //
			// for(int i=0;i<candidateSet2.size();i++){
			// 	Integer[] idArray = candidateSet2.get(i);
			// 	System.out.println("valuee == "+idHashMap.get(idArray[0]));
			// }

			/* Candidateset 2 and frequencySet2 ends */

}catch(IOException e){
			e.printStackTrace();
		}
	}



static ArrayList<Integer[]> candidateGeneration(List<Integer[]> frequencySet1,int n,int r){
	Integer[] data=new Integer[r];
	ArrayList<Integer[],Integer> candidateSet = new ArrayList<Integer[],Integer>();
	int count=1;
	combinationUtil(frequencySet1,candidateSet,data,0,n-1,0,r,count);
	return candidateSet;
}
 static void combinationUtil(List<Integer[]> frequencySet1,ArrayList<Integer[],Integer> candidateSet,
 Integer[] data,int start,int end,int index,int r,int count){
	 if(index == r){
		 candidateSet.add(Arrays.copyOf(data,data.length),count);
		 count++;
		 return;
		}
	 	for(int i=start;i<=end && end-i+1>=r-index;i++){
			Integer[] newArray = frequencySet1.get(i);
			for(int j=0;j<newArray.length;j++){
					data[index] =newArray[j];
					//index++;
			}

	 		combinationUtil(frequencySet1,candidateSet,data,i+1,end,index+1,r);
		}
}

}
