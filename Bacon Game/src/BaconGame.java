import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.management.Query;
import javax.swing.JFileChooser;

/**
 * A class that plays the Bacon Game
 * @author alexwolf
 *
 */
public class BaconGame {
	
	//intitialize instance varibles
	static AdjacencyMapGraph<String, String> baconGraph;
	
	//constructor
	BaconGame(){
		baconGraph= new AdjacencyMapGraph<String,String>();
		
	}
	
	/**
	 * MEthod that creates an undirected graph of actors as vertices and edges asthe movie they're in together
	 * 
	 * @param Actors	file containing actorID and actors
	 * @param movies	file containing movie ID and movie
	 * @param ActorMovie	file containing movieID and Actor ID's
	 * @throws IOException
	 */
	public void createBaconGraph(String Actors, String movies, String ActorMovie) throws IOException{
		
		//create maps for all data
		Map<String, String> actorID= new TreeMap<String, String>();
		Map<String, String> MovieID= new TreeMap<String, String>();
		Map<String, Set<String>> ActorMovieID= new TreeMap<String, Set<String>>();//MOvies as key, Set is the value of
																					//actors in it
		//intiialize string
		String line;
		
		//read Actors
		@SuppressWarnings("resource")
		BufferedReader input1= new BufferedReader(new FileReader(Actors));
		
		//read every line in file
		while((line=input1.readLine())!=null){
			
			//split line
			String[] actorIDname= line.split("\\|");
			
			//update the actorID map
			actorID.put(actorIDname[0], actorIDname[1]);
			
		}
		
		//read Movies
		@SuppressWarnings("resource")
		BufferedReader input2= new BufferedReader(new FileReader(movies));
				
		//read every line in file
		while((line=input2.readLine())!=null){
					
			//split line
			String[] Movies= line.split("\\|");
			
			//update the actorID map
			MovieID.put(Movies[0], Movies[1]);
			
		}
		
		//read Actor Movie
		@SuppressWarnings("resource")
		BufferedReader input3= new BufferedReader(new FileReader(ActorMovie));
				
		//read every line in file
		while((line=input3.readLine())!=null){
					
			//split line
			String[] MovieActor= line.split("\\|");
			
			//find movie name
			String movieName= MovieID.get(MovieActor[0]);
			
			//if movie name is already in Map add actor to the set
			if(ActorMovieID.containsKey(movieName)){
				
				ActorMovieID.get(movieName).add(MovieActor[1]);
			}
			
			//movie isn't in map
			else{
				
				//create set of actors that are in a specific movie
				Set<String> setofActors= new TreeSet<String>();
				
				//add actor to set
				setofActors.add(MovieActor[1]);
				
				//add movie to ActorMOvie map with the set as a value
				ActorMovieID.put(movieName, setofActors);
				
				}
				
			}
		
		
		//loop through every movie
		for(String movie: ActorMovieID.keySet()){
			
			//for every actor that is in that movie
			for(String actor1:ActorMovieID.get(movie)){
				
				//make actor be actor's name not ID
				actor1=actorID.get(actor1);
				
				//look at other actors in the same movie
				for(String actor2:ActorMovieID.get(movie)){
					
					//make actor be actor's name not ID
					actor2=actorID.get(actor2);
					
					//if the actors are not the same person
					if(actor1!=actor2){
						
						//create vertex for actor1 and actor2
						baconGraph.insertVertex(actor1);
						baconGraph.insertVertex(actor2);
						
						//update the graph
						baconGraph.insertUndirected(actor1, actor2, movie);
						
					}
					
			}
		}
		
		}	
	}
	
	/**
	 * Method that returns the quickest path from a goal to a root by doing a BFS on baconGraph
	 * 
	 * @param root	root of the BFS search
	 * @param goal	goal of the BFS search
	 * @return		An ArrayList<String> of quickest path from goal to root
	 * @throws Exception
	 */
	public ArrayList<String> BFSpath(String root, String goal) throws Exception{
		
		//create an arraylist to represent the path
		ArrayList<String> path= new ArrayList<String>();
		
		//intialize BFS tree
		AdjacencyMapGraph<String,String> BFStree= new AdjacencyMapGraph<String,String>();
		
		//intilize queue
		SLLQueue<String> q  = new SLLQueue<String>();
		
		//insert root into q and directed Graph BFS
		q.enqueue(root);
		BFStree.insertVertex(root);

		
		//until the queue is empty
		while(!q.isEmpty()){
			
			//dequeue the q
			String Actor=q.dequeue();
			
			//loop through Actor's's neigbor's
			for(String neighbor: baconGraph.outNeighbors(Actor)){
				
				//if the vertex isn't already in the BFS tree
				if(!BFStree.hasVertex(neighbor)){
					
					//insert neighbor into BFS tree
					BFStree.insertVertex(neighbor);
					
					//find connecting movie between two actors
					String movie= baconGraph.getLabel(Actor, neighbor);
					
					//add neighbor to BFS tree with Actor as parent
					BFStree.insertDirected(neighbor, Actor, movie);
					
					//add neighbor to the Queue
					q.enqueue(neighbor);
				}
			}
		}
		
		//intilaize current to keep track of vertex
		String current= goal;
		
		
		//run until we reach the root
		while(current!=root){
			
			//add current to path list
			path.add(current);
			
			//initiliaze parent string
			String parent= null;
			
			//System.out.println(BFStree.outNeighbors(current));
			
			//find parents of current(would only be one)
			Iterable<String> parents=BFStree.outNeighbors(current);
			
			//cast parents(interable strig) to parent(string)
			for(String parent1: parents){
				parent=parent1;
			}
			
			//update current
			current=parent;
		}
		
		//add root to end of path
		path.add(current);
		
		//return path
		return path;
		
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
	
	public static void main(String[] args) throws Exception{
		
		//initialize bacon game
		BaconGame main= new BaconGame();
		
		//prompt the user for a actor to play the Bacon Game with
		System.out.println("To quit the program, type return in answer to a question");
		System.out.println("Need to enter at least one actor before Quitting");
		
		//make conditional for quiting
		Boolean haveWeQuit= false;
		
		//run while loop so game is played infinitely until program is quit
		while(!haveWeQuit){
			
			//ask user for goal actor
			System.out.println("Enter name of actor: ");
		
			//get user input to set goal actor
			@SuppressWarnings("resource")
			Scanner user_input = new Scanner( System.in );
			String goal= user_input.nextLine();
			
			//allow program to close if user input is the string return
			if(goal=="return"){
				
				//change value of have we quit
				haveWeQuit=true;
				
				//close console
				user_input.close();
				
				//stop program
				break;
			}
			
			
			//create the bacon graph of all actors
			main.createBaconGraph("/Users/alexwolf/Documents/workspace/cs10/PS4/./src/actors.txt", "/Users/alexwolf/Documents/workspace/cs10/PS4/./src/movies.txt", "/Users/alexwolf/Documents/workspace/cs10/PS4/./src/movie-actors.txt");
			
			//get path from goal to Kevin Bacon
			ArrayList<String> path= main.BFSpath("Kevin Bacon", goal);
			
			//find the Bacon Number
			String baconNumber= Integer.toString(path.size()-1);
			
			//print the actor's Bacon Number
			System.out.println(goal+"'s Bacon Number is "+baconNumber);
			
			//for each actor in the path
			for(String actor: path){
				
				//get actor index
				int actorIndex= path.indexOf(actor);
				
				//get index of next person in the path
				int nextActorIndex=  path.indexOf(actor)+1;
				
				//checks to see if actor isn't kevin bacon
					if(!(actorIndex==path.size()-1)){
						
						//get next actor in path
						String nextActor= path.get(nextActorIndex);
						
						//get shared movie between actor and next actor
						String movie= baconGraph.getLabel(actor, nextActor);
						
						//print actor connection to console
						System.out.println(actor+" appeared in "+movie+" with "+nextActor);
						
					}	
			}
				
	}
}
}