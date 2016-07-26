package chacheshw8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class TwoWaySetCache {
	String tagMask="11111111111111111111111000000000";
	String setMask="00000000000000000000000111110000";
	int numSets= 32;
	HashMap<String, String[][]> cache= new HashMap<String, String[][]>();
	
	int hit=0;
	int miss=0;
	
	TwoWaySetCache(){
		hit=0;
		miss=0;
		
	}
	
	//read chache with given address
	//return the specific string output to output file
	public String read(String address){
		
		//get set number from address
		String set= andl(address, setMask);
		set= set.substring(23, 28);
		
		//get tag from address
		String tag= andl(address, tagMask);
		tag= tag.substring(0, 23);
		
		//get set number in decimal
		int decimalValueSetNum = Integer.parseInt(set, 2);
		String Set= Integer.toString(decimalValueSetNum);
		
		//get tag in hexidecimal
		int decimal = Integer.parseInt(tag,2);
		String hexTag = Integer.toString(decimal,16);
		
		//get address in hexidecimal
		long dec = Long.parseLong(address,2);
		String Address = Long.toString(dec,16);
		
		//check to see if there's data at the set index
		if(cache.containsKey(set)){
			
			String[][] lines= cache.get(set);
			
			//tag was found in line zero
			if(lines[0][1].equals(tag)){
				hit+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; found it in line 0;  hit!";
			}
			
			//tag was found in line 1
			else if(lines[1][1].equals(tag)){
				hit+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; found it in line 1;  hit!";
			}
		
			//if the second line has not been written (valid bit 0)
			else if(lines[1][0].equals("0")){
				
				//create new line
				String[] line={"1", tag, "1"};
				
				//update cache
				lines[1]= line;
				cache.put(set, lines);
				
				miss+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 1 empty, adding there!";
			}
			
			//both lines have been written. Figure out which to write to
			else{
				
				String[] newline= {"1", tag, "1"};
				
				//get both lines
				String[] line0= lines[0];
				String[] line1= lines[1];
				
				//get the string int of which was added first
				int line0val= Integer.parseInt(lines[0][2])-1;
				int line1val= Integer.parseInt(lines[1][2])-1;
				
				//line 0 was written earlier
				if(line0val==-1){
					
					//update line 1 value
					line1[2]= String.valueOf(line1val);
					
					String[][] newset= {newline, line1};
					cache.put(set, newset);//update cache
					
					miss+=1;
					return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 0 evicted, adding there!";
				}
				
				//line 1 was written earlier
				else if(line1val==-1){
					
					//update line 0 value
					line0[2]= String.valueOf(line0val);
					
					String[][] newset= {line0, newline};
					cache.put(set, newset);//update cache
					
					miss+=1;
					return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 1 evicted, adding there!";
				}
				
				else{
					//System.out.print("Shouldn't be here");
					return "";
				}
			}
		}	
		
		
		//there was no data at that set so write the the first line
		else{
			String[] line0= {"1", tag, "0"}; //1 for valid. 3rd index for who came first
			String[] line1= {"0", "null", "null"};// line 1 that hasn't be written yet
			String[][] oneSet= {line0, line1};//nested array for set
			cache.put(set, oneSet); //add set to cache
			
			miss+=1;
			return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 0 empty, adding there!";
			
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
