package chacheshw8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


//class that represents how caching works
//by Alex Wolf

public class Caching {
	static ArrayList<String> binaryAddresses;
	static ArrayList<String> output;
	
	Caching(){
		binaryAddresses= new ArrayList<String>();
		output= new ArrayList<String>();
	}
	
	//read the hex Address to cache
	public static void readAddress(String hexAddresses) throws IOException{
		
		//read Actors
		@SuppressWarnings("resource")
		BufferedReader input1= new BufferedReader(new FileReader(hexAddresses));
		String line;
		
		//read every line in file
		while((line=input1.readLine())!=null){
			
			//get string array
			String[]singleLine= line.split(" ");
			System.out.print(singleLine[1]+"\n");
			String[]lineArray= singleLine[1].split("");
			
			String finalLine= "";
					
			//go through every hexDig in address
			for(int i=0; i< singleLine[1].length(); i++){
				
				//convert hex to binary
				finalLine+=hexToBinary(lineArray[i]);
			}
			
			binaryAddresses.add(finalLine);
			
		}
	}
	
	
	 //method to turn Hex to Binary
	static String hexToBinary(String hex) {
	    int i = Integer.parseInt(hex, 16);
	    String bin = Integer.toBinaryString(i);
	    if(bin.length()==1){
	    	bin="000"+bin;
	    }
	    else if(bin.length()==2){
	    	bin="00"+bin;
	    }
	    else if(bin.length()==3){
	    	bin="0"+bin;
	    }
	    return bin;
	}
	
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException{
		Caching main= new Caching();
		DMC dmc= new DMC();
		TwoWaySetCache twoWay= new TwoWaySetCache();
		FourWayCache fourWay= new FourWayCache();
		fullyAssoc full= new fullyAssoc();
		
		String hits;
		String misses;
		
		//create output file
		PrintWriter writer = new PrintWriter("/Users/alexwolf/Desktop/HW8Q2Output.txt", "UTF-8");
		
		//create arraylist of address in binary
		main.readAddress("/Users/alexwolf/Desktop/HW8Q2Input.txt");
		
		writer.write("Alex Wolf Q2 Output \n");
		
		//do directed mapp caching for all addresses
		writer.write("Directly Mapped Cache \n");
		for(int i=0; i<binaryAddresses.size(); i++){
			String outputLine=Integer.toString(i+1)+":"+dmc.read(binaryAddresses.get(i));
			output.add(outputLine);
		}
		
		//loop through final outputs
		for(String line:output){
			
			//write line to file
			writer.write(line+"\n");
		}
		
		//write hits and misses
		hits= Integer.toString(dmc.hit);
		misses= Integer.toString(dmc.miss);
		writer.write("\n");
		writer.write(hits+ "hits, "+misses+" misses, 10 addresses \n\n\n\n\n");
		
		output.clear();
		
		//do Two way Set Associtte caching for all addresses
		writer.write("Two way Set Chache \n");
		for(int i=0; i<binaryAddresses.size(); i++){
			String outputLine=Integer.toString(i+1)+":"+twoWay.read(binaryAddresses.get(i));
			System.out.print(outputLine+"\n");
			output.add(outputLine);
			
		}
		
		//loop through final outputs
		for(String line:output){
			
			//write line to file
			writer.write(line+"\n");
		}
		
		//write hits and misses
		hits= Integer.toString(twoWay.hit);
		misses= Integer.toString(twoWay.miss);
		writer.write("\n");
		writer.write(hits+ "hits, "+misses+" misses, 10 addresses \n\n\n\n\n");
		
		output.clear();
		
		
		
		//do 4 way set associtte
		writer.write("Fuour way set Cache \n");
		for(int i=0; i<binaryAddresses.size(); i++){
			String outputLine=Integer.toString(i+1)+":"+fourWay.read(binaryAddresses.get(i));
			output.add(outputLine);
			
		}
		
		//loop through final outputs
		for(String line:output){
			
			//write line to file
			writer.write(line+"\n");
		}
		
		//write hits and misses
		hits= Integer.toString(fourWay.hit);
		misses= Integer.toString(dmc.miss);
		writer.write("\n");
		writer.write(hits+ "hits, "+misses+" misses, 10 addresses \n\n\n\n\n");
		
		output.clear();
		
		//do fully associte cache
		writer.write("Fully Associtte Cache \n");
		for(int i=0; i<binaryAddresses.size(); i++){
			String outputLine=Integer.toString(i+1)+":"+full.read(binaryAddresses.get(i));
			output.add(outputLine);
			
		}
		
		//loop through final outputs
		for(String line:output){
			
			//write line to file
			writer.write(line+"\n");
		}
		
		//write hits and misses
		hits= Integer.toString(full.hit);
		misses= Integer.toString(full.miss);
		writer.write("\n");
		writer.write(hits+ "hits, "+misses+" misses, 10 addresses \n\n\n\n\n\n\n\n\n");
		
		output.clear();
		
		Caching.binaryAddresses.clear();
		
		writer.write("Q3 \n\n");
		
		//read address from long file
		main.readAddress("/Users/alexwolf/Desktop/long-trace.txt");
		int addressNum= binaryAddresses.size();
		
		dmc.hit=0;
		dmc.miss=0;
		
		//do hit miss rate on large file for Direct Mapped Cache
		for(int i=0; i<binaryAddresses.size(); i++){
			dmc.read(binaryAddresses.get(i));	
		}
		
		//get the hit rate
		double rate1= (dmc.hit*100)/addressNum;
		
		writer.write("The direct-mapped cache had "+Integer.toString(dmc.hit)+" hits and "+Integer.toString(dmc.miss)+" misses \n");
		writer.write("Hit percentage of "+ String.valueOf(rate1)+"%\n");
		
		twoWay.hit=0;
		twoWay.miss=0;
		
		//do hit miss rate on large file for Two way set Associte
		for(int i=0; i<binaryAddresses.size(); i++){
			twoWay.read(binaryAddresses.get(i));	
		}
		
		//get the hit rate
		double rate2= (twoWay.hit*100)/addressNum;
		
		writer.write("The two way set associte cache had "+Integer.toString(twoWay.hit)+" hits and "+Integer.toString(twoWay.miss)+" misses \n");
		writer.write("Hit percentage of "+ String.valueOf(rate2)+"%\n");
		
		fourWay.hit=0;
		fourWay.miss=0;
		
		
		//do hit miss rate on large file for four way set associtte
		for(int i=0; i<binaryAddresses.size(); i++){
			fourWay.read(binaryAddresses.get(i));	
		}
		
		//get the hit rate
		double rate3= (fourWay.hit*100)/addressNum;
		
		writer.write("The four way set associte cache had "+Integer.toString(fourWay.hit)+" hits and "+Integer.toString(fourWay.miss)+" misses \n");
		writer.write("Hit percentage of "+ String.valueOf(rate3)+"%\n");
		
		
		full.hit=0;
		full.miss=0;
		
		//do hit miss rate on large file for Fully Associtte Cache
		for(int i=0; i<binaryAddresses.size(); i++){
			full.read(binaryAddresses.get(i));	
		}
		
		//get the hit rate
		double rate4= (full.hit*100)/addressNum;
		
		writer.write("The fully associte cache had "+Integer.toString(full.hit)+" hits and "+Integer.toString(full.miss)+" misses \n\n\n\n");
		writer.write("Hit percentage of "+ String.valueOf(rate4)+"%\n");
				
		writer.close();

	}

}
