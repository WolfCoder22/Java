package chacheshw8;

import java.util.LinkedList;
import java.util.Queue;


/**
 * Class for a Directed-Mapped Cache
**/

public class DMC {
	String tagMask="11111111111111111111110000000000";
	String setMask="00000000000000000000001111110000";
	
	int hit=0;
	int miss=0;
	
	Queue<String[]> DMQueue= new LinkedList<String[]>();
	
	DMC(){
		
	}
	
	//read chache with given address
	//return the specific string output to output file
	public String read(String address){
		
		//get set number from address
		String set= andl(address, setMask);
		set= set.substring(22, 28);
		
		//get tag from address
		String tag= andl(address, tagMask);
		tag= tag.substring(0, 22);
		
		//get set number in decimal
		int decimalValueSetNum = Integer.parseInt(set, 2);
		String Set= Integer.toString(decimalValueSetNum);
		
		//get tag in hexidecimal
		int decimal = Integer.parseInt(tag,2);
		String hexTag = Integer.toString(decimal,16);
		
		//get address in hexidecimal
		long dec = Long.parseLong(address,2);
		String Address = Long.toString(dec,16);
		
		//loop through each set in the cache
		for(String[] cacheSet: DMQueue){
			
			//get the set num and tag
			String setNum= cacheSet[0];
			String setTag= cacheSet[1];
			
			//see if there's a hit
			if((setNum.equals(set))& (tag.equals(setTag))){
				
				hit+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; found it in line 0;  hit!";
			}
			
			//there is a miss and data in the set need to be overided
			else if(setNum.equals(set)){
				
				//create String array for Direct Mapped Chache containg setIndex and tag
				String[] newSet= {set, tag};
				
				//add to chache and remove old one
				DMQueue.remove(cacheSet);
				DMQueue.add(newSet);
				
				miss+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 0 evicted, adding there!";
			}
		
		}
		//there wasn't a hit or an evicted miss, so add a new set
		
		//create String array for Direct Mapped Chache containg setIndex and tag
		String[] newSet= {set, tag};
		
		//add to chache
		DMQueue.add(newSet);
		
		miss+=1;
		return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 0 miss, adding there!";
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
