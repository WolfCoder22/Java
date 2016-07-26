package chacheshw8;

import java.util.ArrayList;

public class fullyAssoc {
	String tagMask="11111111111111111111111111110000";
	String set= "0";
	
	int hit=0;
	int miss=0;
	
	ArrayList<String> tagsInCache= new ArrayList<String>();
	
	fullyAssoc(){
		
	}
	
	//read chache with given address
	//return the specific string output to output file
	public String read(String address){
		
				
			//get tag from address
			String tag= andl(address, tagMask);
			tag= tag.substring(0, 28);
				
			//get tag in hexidecimal
			int decimal = Integer.parseInt(tag,2);
			String hexTag = Integer.toString(decimal,16);
			
			//get line number 
			int decimalValuetagNum = Integer.parseInt(tag, 2);
			String Line= Integer.toString(decimalValuetagNum);
				
			//get address in hexidecimal
			long dec = Long.parseLong(address,2);
			String Address = Long.toString(dec,16);
				
			//loop through all tags in the one set
			if(tagsInCache.contains(tag)){
				
				hit+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+set+"; found it in line "+Line+";  hit!";
			}
			//add it to the cache if its not there
			else{
				tagsInCache.add(tag);
				
				miss+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+set+"; miss! line "+Line+" empty, adding there!";
			}

	}
	
	//method that takes two string bits and returns the and of them
			public String andl(String bits1, String bits2){
				String[] bits1Array= bits1.split("");
				String[] bits2Array= bits2.split("");
				
				String anded="";
				
				//loop through all bits of each
				for(int i=0; i<bits1Array.length; i++){
					
					
					//if bits same and 1 output 1, if not 0
					if((bits1Array[i].equals("1"))& (bits2Array[i].equals("1"))){
						
						anded=anded+"1";
					}
					else{
						anded=anded+"0";
					}	
				}
				return anded;
			}
}
