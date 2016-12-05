// Sharon Gao
// CS 1501, Assignment 4
import java.util.*; 
import java.io.*;

public class Airline {
	private static Graph graph;
	private static ArrayList<String> vertices;
	private static Scanner input;
	private static FileReader readFile;
	private static BufferedReader fileInput;
	private static PrintWriter fileOutput;
	private static File routes;
	private static String file;
	private static int numVertices;

	public static void main(String[] args) {
		input = new Scanner(System.in);
		try {
			do {
				System.out.print("INPUT FILE: ");
				file = input.nextLine();
				routes = new File(file);
			} while (!routes.exists());
	 		readFile = new FileReader(file);
    			fileInput = new BufferedReader(readFile);
    			numVertices = Integer.parseInt(fileInput.readLine());
    			vertices = new ArrayList<String>(numVertices);
    			graph = new Graph(numVertices);
    			for(int i = 0; i < numVertices; i++) {
				vertices.add(i, fileInput.readLine());
			}
			graph.setVertices(vertices);
			String edges;
    			while((edges = fileInput.readLine()) != null){
    			 	String[] splitStrings = edges.split(" ");
    			 	int v = Integer.parseInt(splitStrings[0]);
    			 	int w = Integer.parseInt(splitStrings[1]);
   		 		double d = Double.parseDouble(splitStrings[2]);
    			 	double p = Double.parseDouble(splitStrings[3]);
    			 	Edge E = new Edge((v-1),(w-1),d,p);
  	  			graph.addEdge(E);
    			}
    			fileInput.close();
			System.out.println("--------------------------\n");
    		} catch (Exception E) {}	
		
		int choice = 0;
		do {
			System.out.println("Menu Options:");
			System.out.println("1.) List all Routes, Distances, and Prices");
			System.out.println("2.) List Minimum Spanning Trees");
			System.out.println("3.) Shortest Distance Path between Source and Destination");
			System.out.println("4.) Lowest Price Path between Source and Destination");
			System.out.println("5.) Least Connecting Flights between Source and Destinaton");
			System.out.println("6.) Add a Route");
			System.out.println("7.) Remove a Route");
			System.out.println("8.) All Routes under a certain Price");
			System.out.println("9.) Quit");
			System.out.print("Enter Selection: ");
			try {
				choice = Integer.parseInt(input.nextLine());
			} catch (NumberFormatException e) {
				choice = 0;
			}
			System.out.println();
			
			if (choice == 1)	// List all routes
				System.out.print(graph.toString());
			else if (choice == 2)	// List Minimum Spanning Trees
				System.out.print(graph.initprim());
			else if (choice >= 3 && choice <= 7 ) { 	// Shortest Path Searches
				String source,destination;
				int src;
				int dest;
				boolean result;
				Edge E;
				do {
					System.out.print("Enter Source: ");
					source = capitalize(input.nextLine());
					System.out.print("Enter Destination: ");
					destination = capitalize(input.nextLine().toLowerCase());
					src = vertices.indexOf(source);
					dest = vertices.indexOf(destination);
					System.out.println();
					if (src < 0 || dest< 0)
						System.out.println("Source or destination does not exist in a route. Please try again with another input.");
				} while (src < 0 || dest < 0);
				
				if (choice == 3) 	// Shortest Distance		
					System.out.print(graph.dij(src, dest, 'd'));
				else if (choice == 4)	// Lowest Price
					System.out.print(graph.dij(src, dest, 'p'));
				else if (choice == 5)	// Least Connecting Flights
					System.out.print(graph.bf_search(src, dest));
				
				else if (choice == 6) { 	// Add Route
					double distance;
					double price;
					System.out.println("Enter Distance: ");
					distance = Double.parseDouble(input.nextLine());
					System.out.println("Enter Price: ");
					price = Double.parseDouble(input.nextLine());
					System.out.println();
					E = new Edge(src, dest, distance, price);
					result = graph.addEdge(E);
					if (result)	
						System.out.println(source +" to " + destination + " was added to the graph");
					else		
						System.out.println("ERROR: Edge was not added in graph.");
				}
				
				else if (choice == 7) { 	// Remove Route
					E = new Edge(src, dest);
					result = graph.removeEdge(E);
					if (result)	
						System.out.println(source +" to " + destination + " was removed from the graph");
					else		
						System.out.println("ERROR: Edge was not found in graph.");
				}
			}
			else if (choice == 8) {		// All Routes under certain Price
					double price;
					System.out.println("Enter Max Price: ");
					price = Double.parseDouble(input.nextLine());
					System.out.println();
					graph.initMax(price);
			}
			
			else if (choice == 9) {		// Quit
				try{
					fileOutput= new PrintWriter(file, "UTF-8");
					fileOutput.println(numVertices + "");
				
					for(int i = 0; i < numVertices; i++){
						fileOutput.println(vertices.get(i));
					}
				
					List<Edge> Edges = new ArrayList(numVertices);
					for(int i = 0; i < numVertices; i++){
						for(Edge e : graph.adj(i)){
							if(!Edges.contains(e)){
								Edges.add(e);
								int v = e.either() + 1;
								int w =	e.other((v - 1)) + 1;
								StringBuilder s = new StringBuilder(v + " " + w  + " " + e.distance()  + " " + e.price());
								fileOutput.println(s.toString());
							}
						}
					}
					fileOutput.close();
					System.out.println("Exiting Program.");
				} catch (Exception E) {}
			}
			else {
				System.out.println("Selection invalid. Please select a valid option from the menu.");
			}
			System.out.println();	
		} while (choice != 9);
    	
	}
	public static String capitalize(String s) {
   		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}
