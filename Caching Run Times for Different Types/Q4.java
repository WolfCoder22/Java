package chacheshw8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Q4 {
	
	static ArrayList<String> binaryAddressesVer1;
	static ArrayList<String> binaryAddressesVer2;

	
	Q4(){
	binaryAddressesVer1= new ArrayList<String>();
	binaryAddressesVer2= new ArrayList<String>();
	}
	
		//read the hex Ver1 Address to cache
		public void readAddressVer1(String hexAddresses) throws IOException{
			
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
				
				binaryAddressesVer1.add(finalLine);
				
			}
		}
		
		//read the hex Ver1 Address to cache
		public void readAddressVer2(String hexAddresses) throws IOException{
					
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
						
				binaryAddressesVer2.add(finalLine);
						
					}
				}
		
		//method to turn Hex to Binary
		public static String hexToBinary(String hex) {
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
			Q4 main= new Q4();
			String hits;
			String misses;
			
			//create two set associtte cache
			TwoWaySetCache twoWay= new TwoWaySetCache();
			
			PrintWriter writer = new PrintWriter("/Users/alexwolf/Desktop/HW8Q4Output.txt", "UTF-8");
			
			//convert address to binary
			main.readAddressVer1("/Users/alexwolf/Desktop/ver1.txt");
			main.readAddressVer2("/Users/alexwolf/Desktop/ver2.txt");
			
			//do Two way Set Associtte caching for all addresses in Ver1
			writer.write("Two way Set Chache Ver1\n");
			for(int i=0; i<binaryAddressesVer1.size(); i++){
				twoWay.read(binaryAddressesVer1.get(i));
		
			}
			
			//write hits and misses
			hits= Integer.toString(twoWay.hit);
			misses= Integer.toString(twoWay.miss);
			
			writer.write("\n");
			writer.write("Version 1 had" +hits+ "hits, "+misses+" misses"+"\n\n\n\n\n");
			
			//reset hit miss count
			twoWay= new TwoWaySetCache();
			
			//do Two way Set Associtte caching for all addresses in Ver2
			for(int i=0; i<binaryAddressesVer2.size(); i++){
				twoWay.read(binaryAddressesVer2.get(i));
		
			}
			
			//write hits and misses
			hits= Integer.toString(twoWay.hit);
			misses= Integer.toString(twoWay.miss);
			
			writer.write("\n");
			writer.write("Version 2 had "+hits+ "hits, "+misses+" misses with speed ");
			writer.close();
			
		}
}