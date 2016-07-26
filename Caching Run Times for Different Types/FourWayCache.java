package chacheshw8;

import java.util.HashMap;

public class FourWayCache {
	String tagMask="11111111111111111111111100000000";
	String setMask="00000000000000000000000011110000";
	
	int hit=0;
	int miss=0;
	
	HashMap<String, String[][]> cache= new HashMap<String, String[][]>();
	
	FourWayCache(){
		
	}
	
	//read chache with given address
	//return the specific string output to output file
	public String read(String address){
		
		//get set number from address
		String set= andl(address, setMask);
		set= set.substring(24, 28);
		
		//get tag from address
		String tag= andl(address, tagMask);
		tag= tag.substring(0, 24);
		
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
			
			//tag was found in line 2
			else if(lines[2][1].equals(tag)){
				hit+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; found it in line 2;  hit!";
			}
			
			//tag was found in line 3
			else if(lines[3][1].equals(tag)){
				hit+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; found it in line 3;  hit!";
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
			
			//if the third line has not been written (valid bit 0)
			else if(lines[2][0].equals("0")){
				
				//create new line
				String[] line={"1", tag, "2"};
				
				//update cache
				lines[2]= line;
				cache.put(set, lines);
				
				miss+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 2 empty, adding there!";
			}
			
			//if the fourth line has not been written (valid bit 0)
			else if(lines[3][0].equals("0")){
				
				//create new line
				String[] line={"1", tag, "3"};
				
				//update cache
				lines[3]= line;
				cache.put(set, lines);
				
				miss+=1;
				return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 3 empty, adding there!";
			}
			
			//both lines have been written. Figure out which to write to
			else{
				String[] newline= {"1", tag, "3"};
				
				//get lines
				String[] line0= lines[0];
				String[] line1= lines[1];
				String[] line2= lines[2];
				String[] line3= lines[3];
				
				//get the string int of which was added first
				int line0val= Integer.parseInt(lines[0][2])-1;
				int line1val= Integer.parseInt(lines[1][2])-1;
				int line2val= Integer.parseInt(lines[2][2])-1;
				int line3val= Integer.parseInt(lines[3][2])-1;
				
				//line 0 was first
				if(line0val==-1){
					
					//update current values in set
					line1[2]= String.valueOf(line1val);
					line2[2]= String.valueOf(line2val);
					line3[2]= String.valueOf(line3val);
					
					
					String[][] newset= {newline, line1, line2, line3};
					cache.put(set, newset);//update cache
					
					miss+=1;
					return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 0 evicted, adding there!";
				}
				
				//line 0 was first
				if(line1val==-1){
					
					//update current values in set
					line2[2]= String.valueOf(line2val);
					line0[2]= String.valueOf(line0val);
					line3[2]= String.valueOf(line3val);
					
					String[][] newset= {line0, newline, line2, line3};
					cache.put(set, newset);//update cache
					
					miss+=1;
					return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 1 evicted, adding there!";
				}
				
				//line 2 was first
				if(line2val==-1){
					
					//update current values in set
					line1[2]= String.valueOf(line1val);
					line0[2]= String.valueOf(line0val);
					line3[2]= String.valueOf(line3val);
					
					String[][] newset= {line0, line1, newline, line3};
					cache.put(set, newset);//update cache
					
					miss+=1;
					return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 2 evicted, adding there!";
				}
				
				//line 3 was first
				if(line3val==-1){
					
					//update current values in set
					line1[2]= String.valueOf(line1val);
					line2[2]= String.valueOf(line2val);
					line0[2]= String.valueOf(line0val);
					
					String[][] newset= {line0, line1, line2, newline};
					cache.put(set, newset);//update cache
					
					miss+=1;
					return "addr 0x"+Address+"; tag "+hexTag+"; looking in set "+Set+"; miss!  line 3 evicted, adding there!";
				}
			}
			return "";
		}	
		
		
		//there was no data at that set so write the the first line
		else{
			String[] line0= {"1", tag, "0"}; //1 for valid. 3rd index for who came first
			String[] line1= {"0", "null", "null"};// line 1 that hasn't be written yet
			String[] line2= {"0", "null", "null"}; //1 for valid. 3rd index for who came first
			String[] line3= {"0", "null", "null"};// line 1 that hasn't be written yet
			String[][] oneSet= {line0, line1, line2, line3};//nested array for set
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
