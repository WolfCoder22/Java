import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JFileChooser;


public class Sudi {
	
	static //declare instance Variables
	double U= .0000000000000000000000000000000000001;
	static Map<String, Map<String, Double>> emisions;
	static Map<String, Map<String, Double>> transitions;
	
	//constructor for maps
	Sudi(){
		emisions= new TreeMap<String, Map<String, Double>>();
		transitions= new TreeMap<String, Map<String, Double>>();
	}
	/**
	 * Creates Data for the Emmsion and Transition Maps(instance variables) 
	 * 
	 * @param sentances		file where senatance is located
	 * @param tags			file where tags for that sentance is located
	 * @throws IOException 
	 */
	public static void createMapData(String sentances, String tags ) throws IOException{
		
		//create ArrayLists for all words and their associated Tags(parrel arraylists)
		ArrayList<String> wordList= new ArrayList<String>();
		ArrayList<String> tagList= new ArrayList<String>();
		
		//create map for word count and tag count
		Map<String, Double> wordCount= new TreeMap<String, Double>();
		Map<String, Double> tagCount= new TreeMap<String, Double>();
		
		//intiialize string
		String line;
				
		//read Sentance and tag files
		@SuppressWarnings("resource")
		BufferedReader input1= new BufferedReader(new FileReader(sentances));
		@SuppressWarnings("resource")
		BufferedReader input2= new BufferedReader(new FileReader(tags));
		
		//read every line in sentance file
		while((line=input1.readLine())!=null){
			
			//split line bwetween every space
			String[] words= line.split(" ");
					
			//put each word in the line 
			for(String word:words){
				
				//add it to the arrayList
				wordList.add(word);
				
				//check to see if word is in wordcount map
				if(wordCount.containsKey(word)){
					
					//update it's count by one
					wordCount.put(word, wordCount.get(word)+1.0 );
				}
				
				//if it's not in the wordCount map
				else{
					
					//add word to the map with count of 1
					wordCount.put(word, 1.0);
					
				}
				}
		}
			
		//read every line in tag file
		while((line=input2.readLine())!=null){
				
			//split line bwetween every space
			String[] tags1= line.split(" ");
						
			//put each tag in the line 
			for(String tag:tags1){
					
				//add it to the Tag arrayList
				tagList.add(tag);
					
				}
		}
		
		//creater counter variable for wordList
		int count=0;
		
		//go through arrayListWords
		for(String word: wordList){
			
			//find word type associatted with word
			String wordType= tagList.get(count);
			
			//check to see if wordType is not wordCount map
			if(!tagCount.containsKey(wordType)){
				
				//insert newwordtype(tag) with count as 1
				tagCount.put(wordType, 1.0);
			}
			
			//wordType already in tagCount
			else{
				
				//update it's count
				tagCount.put(wordType, tagCount.get(wordType)+1.0);
			}
		
			
			//if word not already in emisions map
			if(!emisions.containsKey(word)){
				
				//create it's nested map
				Map<String, Double> nestedMap= new TreeMap<String, Double>();
				
				//update nestedMap with value as 1.0
				nestedMap.put(wordType, 1.0);
				
				//update emmisions map
				emisions.put(word, nestedMap);
				
			}
			
			//word is already in emissions map
			else{
				
				//find word type associatted with word
				String wordType1= tagList.get(count);
				
				//if wordType is not in countMap
				
				
				//check to see if wordType already in nestedMap
				if(emisions.get(word).containsKey(wordType1)){
					
					//update the word's nested map with it's value the amount of times it occurs
					emisions.get(word).put(wordType1, emisions.get(word).get(wordType1)+1.0);
				}
				
				//wordType not already in Nested Map
				else{
					
					//add it's key to map with value 1.0
					emisions.get(word).put(wordType1, 1.0);
				}
			
			}
			
			//update count by 1
			count+=1;
		}
		
		//for everyword in the emisions map
		for(String word: emisions.keySet()){
			
			//find the number of times word occurs
			Double count1= wordCount.get(word);
			
			//get it's nestedMap
			Map<String, Double> nestedMap=emisions.get(word);
			
			//for word type in nestedMap
			for(String type:nestedMap.keySet()){
				
				
				//find number of times word Type occurs for that word(stored as value of nested Map)
				Double typeNumber= nestedMap.get(type);

				//calculate the probability it will be that type for that word
				Double prob= typeNumber/count1;
				
				//update nested map with calculated probability
				nestedMap.put(type, prob);
				
			}
			
			//update emmsions map with it's correct nested Map
			emisions.put(word, nestedMap);
			
			//System.out.println(w+"|"+followingTag+"|"+prob+"        "+transitions.get(tag1).get(followingTag)+"/"+tagCount.get(tag1));
			
		}
		
		//create count variable to keep track of count
		int count3= 0;
		
		//loop through every type of tag
		for(String tag: tagList){
			
			//if the tag doesn't have a nested map for the HMM model create one
			if(!transitions.containsKey(tag)){
				
				//create a nestedMap for the transitions of the HMM
				Map<String, Double> nestedMap= new TreeMap<String, Double>();
				
				//if the tag isn't the last word in the file
				if(count3!=tagList.size()-1){
					
					//find the tag following the current tag in the sentance
					String followingTag= tagList.get(count3+1);
					
					//update nestedMap with folowwing tag and tagCount
					nestedMap.put(followingTag, 1.0);
					
					//add nested map to Transitions map as a value
					transitions.put(tag, nestedMap);
				
					}
			}
			
			//the tag type already has a nested map
			else{
				
				//if the tag isn't the last word in the file
				if(count3!=tagList.size()-1){
					
					//find the tag following the current tag in the sentance
					String followingTag= tagList.get(count3+1);
					
					//if following tag is already in Nested Map
					if(transitions.get(tag).containsKey(followingTag)){
						
						//update the nested map's value by one
						transitions.get(tag).put(followingTag, transitions.get(tag).get(followingTag)+1.0);
					}
					
					//the following tag isn't in the nested map
					else{
						
						//update the nested map's with key as following tag and value as 1
						transitions.get(tag).put(followingTag, 1.0);
						
					}
					
							
				}
				}
				
			
			//update count by 1
			count3=count3+1;
			}
	
	//loop over every tag
	for(String tag1: tagCount.keySet()){
		
		//loop over all of its following tags
		for(String followingTag: transitions.get(tag1).keySet()){
			
			//find it's probability
			double prob= (transitions.get(tag1).get(followingTag))/tagCount.get(tag1);
			
			//update the transitions map with the correct probability
			transitions.get(tag1).put(followingTag, prob);
			
		}
	}
	
	//loop over evey tag type
	for(String tag1: transitions.keySet()){
		
		//loop over every tag again
		for(String tag2: transitions.keySet()){
			
			//if there is a transition that doesn't exisit
			if(!transitions.get(tag1).containsKey(tag2)){
				
				//make it with an extremely low probability
				transitions.get(tag1).put(tag2, U);
				
			}
	
		}
	}
	
	//loop over all words
	for(String word: wordList){
		
		//loop over all tag types
		for(String tag: transitions.keySet()){
			
			//if an emmsion doesn't exisit
			if(!emisions.get(word).containsKey(tag)){
				
				//make it with a low probability
				emisions.get(word).put(tag, U);
			}
		}
	}
	
}
	
	public static String[] viterbi(String sentances) throws IOException{
		
		//create master arrayList for sentences
		ArrayList<String> words= new ArrayList<String>();
		
		//intitialize string
		String line;
				
		//read sentance file
		@SuppressWarnings("resource")
		BufferedReader input= new BufferedReader(new FileReader(sentances));
				
			//read every line in file
			while((line=input.readLine())!=null){
					
					//split line
					String[] line1= line.split(" ");
					
					//loop over string
					for(int i=0; i<line1.length-1; i++){
						
						//add word to master arraylist
						words.add(line1[i]);
					}
					
				}
		
		//create an array for the tags corresponding with the sentance
		String[] tagAnswers= new String[words.size()];
		
		//create an array to keep track of possible tags for each word
        @SuppressWarnings("unchecked")
		Map<String, String>[] trackerArray = new TreeMap[words.size()];
		
		//create an arraylist for prev states
		ArrayList<String> prevStates= new ArrayList<String>();
		
		//add start to previous state
		prevStates.add("start");
		
		//create new nested map for transitions map for start 
		Map<String, Double> nestedMap= new TreeMap<String, Double>();
		
		//loop through every tag type
		for(String tag: transitions.keySet()){
			
			//update nested map with low prob for each tag
			nestedMap.put(tag, U);
			
			
		}
		
		//update transitions map for start 
		transitions.put("start", nestedMap);
		
		//create map for prev scores
		Map<String, Double> prevScores= new TreeMap<String, Double>();
		
		//update score of start to 0
		prevScores.put("start", 0.0);
		
		//loop through every word in sentance
		for(int i=0; i< words.size(); i++ ){
			
			//create map to store possible state for a given determined previous state
			Map<String, String> tracker= new TreeMap<String, String>();
			
			//update trackerArray with tracker Map
			trackerArray[i]= tracker;
			
			//create an arraylist for next states
			ArrayList<String> nextStates= new ArrayList<String>();
			
			//creat map for next scores
			Map<String, Double> nextScores= new TreeMap<String, Double>();
			
			//loop through every tag type
			for(String tag: transitions.keySet()){
				
				//make sure tag ins't start
				if(tag!="start"){
					
					//add tag to next states
					nextStates.add(tag);
				}
			}
			
			//for each state in prev states
			for(String state: prevStates){
				
				//for each transition from state to next state
				for(String nextState: transitions.get(state).keySet()){
					
					
					//System.out.println(prevScores.get(state));
					//System.out.println(Math.log(transitions.get(state).get(nextState)));
					//System.out.println(Math.log(emisions.get(words.get(i)).get(nextState)));
					//System.out.println("------------");
					
					
					//find the next score
					Double nextScore= prevScores.get(state)+ Math.log(transitions.get(state).get(nextState))+Math.log(emisions.get(words.get(i)).get(nextState));					
					
					//if next state isn't in nextScores or nextScore> nextScores[nextState]
					if(!nextScores.containsKey(nextState)|| nextScore>nextScores.get(nextState)){
						
						//reset NextScores
						nextScores.put(nextState, nextScore);
						
						//remeber got nextState for obersrvtion i from state
						trackerArray[i].put(nextState, state);
						
					}
				}
				
			}
			
			//update previous scores and states
			prevStates=nextStates;
			prevScores= nextScores;
			
		}
	
		//create variable to find best score
		Double bestScore= -9999999999999999999999999999999999999999.9;
		
		//create variable for lastword Stae
		String lastWordState=null;
		
		//loop through all previous scores
		for(String state: prevScores.keySet()){
			
			//if score better than best score
			if(prevScores.get(state)>bestScore){
				
				//reset best Score and lastWord State
				bestScore= prevScores.get(state);
				lastWordState= state;
				
			}
		
		}
		
		//update last Word answer with  LastWordTag
		tagAnswers[words.size()-1]=lastWordState;
		
		//loop through every word in sentance
		for(int i=words.size()-2; i>=0; i-- ){
			
			
			//find tag before the current tag
			String prevTag= trackerArray[i].get(tagAnswers[i+1]);
			
			//add previos tag to answer
			tagAnswers[i]=prevTag;
			
		}	
		
		//return tag answers
		return tagAnswers;
	}
	
	
	/**
	 * Puts up a fileChooser and gets path name for file to be opened.
	 * Returns an empty string if the user clicks "cancel".
	 * @return path name of the file chosen	
	 */
	public static String getFilePath() {
	  JFileChooser fc = new JFileChooser("."); // start at current directory
	  
	  int returnVal = fc.showOpenDialog(null);
	  if(returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    String pathName = file.getAbsolutePath();
	    return pathName;
	  }
	  else
	    return "";
	}
					
	
	public static void main(String[] args) throws IOException {
		
		Sudi main= new Sudi();
		
		//fill in tansitions and emmsions map with data
		createMapData("/Users/alexwolf/Documents/workspace/cs10/PS5/./src/brown-train-sentences.txt", "/Users/alexwolf/Documents/workspace/cs10/PS5/./src/brown-train-tags.txt");
		
		String[] answer= viterbi("/Users/alexwolf/Documents/workspace/cs10/PS5/./src/simple-test-sentences.txt");
		
		
		
		for(int i=0; i<answer.length-1; i++){
			 
			System.out.println(answer[i]);
		}
	}
	}
