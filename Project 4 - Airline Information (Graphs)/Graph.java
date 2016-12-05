// Sharon Gao
// CS 1501, Assignment 4
import java.util.*;

public class Graph {
    private final int numVertices; // Number of Vertices
    private int numEdges;
    private LinkedList<Edge>[] adj;
    private ArrayList<String> vertices;
    
    /**
     * Initializes an empty edge-weighted graph with <tt>V</tt> vertices and 0 edges.
     * param V the number of vertices
     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
     */
    public Graph(int numVertices) {
        if (numVertices < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.numVertices = numVertices;
        this.numEdges = 0;
        adj = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adj[i] = new LinkedList<Edge>();
        }
    }

    /**
     * Returns the number of vertices in the edge-weighted graph.
     * @return the number of vertices in the edge-weighted graph
     */
    public int numVertices() {
        return numVertices;
    }

    /**
     * Returns the number of edges in the edge-weighted graph.
     * @return the number of edges in the edge-weighted graph
     */
    public int numEdges() {
        return numEdges;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= numVertices)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (numVertices - 1));
    }

    /**
     * Adds the undirected edge <tt>e</tt> to the edge-weighted graph.
     * @param e the edge
     * @throws java.lang.IndexOutOfBoundsException unless both endpoints are between 0 and V-1
     */
    public boolean addEdge(Edge e) {
    	boolean result = false;
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        result = adj[v].add(e);
        result = adj[w].add(e);
        if(result)
        	numEdges++;
        return result;
    }
    /**
     * Removes the undirected edge <tt>e</tt> from the edge-weighted graph.
     * @param e the edge
     * @throws java.lang.IndexOutOfBoundsException unless both endpoints are between 0 and V-1
     */
    public boolean removeEdge(Edge e) {
    	boolean result = false;
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
		result = adj[v].remove(e);
        result = adj[w].remove(e);
        if(result)
        	numEdges--;
        return result;
    }

    /**
     * Returns the edges incident on vertex <tt>v</tt>.
     * @return the edges incident on vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns all edges in the edge-weighted graph.
     * To iterate over the edges in the edge-weighted graph, use foreach notation:
     * <tt>for (Edge e : G.edges())</tt>.
     * @return all edges in the edge-weighted graph as an Iterable.
     */
    public Iterable<Edge> edges() {
        LinkedList<Edge> list = new LinkedList<Edge>();
        for (int i = 0; i < numVertices; i++) {
            int selfLoops = 0;
            for (Edge e : adj(i)) {
                if (e.other(i) > i) {
                    list.add(e);
                }
                // only add one copy of each self loop (self loops will be consecutive)
                else if (e.other(i) == i) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }
    
    public void setVertices(ArrayList<String> vert){
    	vertices = vert;
    }

    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *   followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ALL FLIGHT PATHS\n");
		s.append("-----------------\n");
		s.append("List of all flight paths in format \"Source, Destination : Distance, Cost.\"\n");
        for (int i = 0; i < numVertices; i++) {
            s.append((i + 1) + ": ");
            for (Edge e : adj[i]) {
                s.append(e.toString(vertices, 0) + "  ");
            }
            s.append("\n");
        }
        return s.toString();
    }
    /**
	* Change edge weight to distance.
	*/
    private void changeEdgeWeightToDistance(){
    	for(int i = 0; i < numVertices; i++){
    		for (Edge e : adj(i)) {
				e.changeWeightToDistance();
			}
    	}
    }
    /**
    * Change edge weight to price.
    */
    private void changeEdgeWeightToPrice(){
        for(int i = 0; i < numVertices; i++){
    		for (Edge e : adj(i)) {
				e.changeWeightToPrice();
			}
    	}
    }
    
    
	/**
	* Breadth First Search method that finds shortest path from source vertex.
	* Used to determine path with least hops.
	*/
    public String bf_search(int src, int dest){
        validateVertex(src);
        validateVertex(dest);
    	if(src == dest){
    		throw new IllegalArgumentException("ERROR: Source and Destination cannot be equal.");
    	}
   	 	boolean[] mark = new boolean[numVertices];
        int[] parent = new int[numVertices];
   	 	boolean found = false;
   	 	int cf = 0;		// number of connecting flights/hops
    	Stack<Integer> paths = new Stack<Integer>();
        Queue<Integer> queue = new Queue<Integer>();
        mark[src] = true;
        queue.enqueue(src);

        while (!queue.isEmpty() && !found) {
            int v = queue.dequeue();
            for (Edge e : adj[v]) {
            	int w = e.other(v);
            	
            	if (w == dest) {
            		parent[w] = v;
            		paths.push(w);
            		v = parent[w];
    
            		while (v != src) {
            			paths.push(v);
            			cf++;
            			v = parent[v];
            		}
            			
    				paths.push(v);
    				cf++;
            		found = true;
            		break;
            	}
            	
                else if (!mark[w]){
                	parent[w] = v;
                    mark[w] = true;
                    queue.enqueue(w);
                }
            }
            
            if(queue.isEmpty() && !found){
            	return "No path exists between Source and Destination.\n";
            }
        }
        
        StringBuilder output = new StringBuilder("LEAST CONNECTING FLIGHTS from " + vertices.get(src)  + " to " + vertices.get(dest) + "\n");
		output.append("---------------------------------------------------------\n");
		output.append("Least number of connecting flights path from " + vertices.get(src)  + " to " + vertices.get(dest) + " is " + cf + "\n");
        while(!paths.isEmpty()){
        	output.append(vertices.get(paths.pop())+ " ");
        }
        output.append("\n");
        return output.toString();
    }
	
	// Initialize prim MST.
    public String initprim(){
    	 boolean[] mark = new boolean[numVertices];
    	 StringBuilder output = new StringBuilder("MINIMUM SPANNING TREE\n");
		 output.append("---------------------\n");
    	 this.prim(0, mark, output);
    	 
    	for(int i=0; i < numVertices; i++){
        	if(!mark[i] && !adj[i].isEmpty()){
        			output.append("Unconnected Graph: MST of Sub-Tree:\n");
        			this.prim(i, mark, output);
        	}
       	 }
    	 return output.toString();
    }
    
	// Compute a minimum spanning tree.
    public void prim(int s, boolean[] mark, StringBuilder out) {	
    	double weight = 0;       							
        Stack<Edge> paths = new Stack<Edge>();				
        MinPQ<Edge> minPQ = new MinPQ<Edge>();								
        this.changeEdgeWeightToDistance();			
        scan(s, mark, minPQ);
        while (!minPQ.isEmpty() && paths.size() < (numVertices - 1)) {                	
            Edge e = minPQ.delMin();                      	
            int v = e.either(), w = e.other(v);        	
            if (mark[v] && mark[w]) 
				continue;      		
            paths.push(e);                            	
            weight += e.weight();	
            if (!mark[v]) 
				scan(v, mark, minPQ);             
            if (!mark[w]) 
				scan(w, mark, minPQ);              
        }
        
        out.append("The edges in the MST based on distance follow:\n");
        while(!paths.isEmpty()){
        	out.append(paths.pop().toString(vertices, 1)+ "\n");
        }
        out.append("\n");
        return;
    }

    private void scan(int v, boolean[] mark, MinPQ<Edge> minPQ) {
        mark[v] = true;
        for (Edge e : adj(v))
            if (!mark[e.other(v)]) minPQ.insert(e);
    }
    
    public String dij(int src, int dest, char type) {
    	String pType;
    	if (type == 'd'){
    		this.changeEdgeWeightToDistance();		
    		pType = new String("DISTANCE");
    	}
    	else {
    	    this.changeEdgeWeightToPrice();			
    	    pType = new String("PRICE");
    	}
		
     	IndexMinPQ<Double> minPQ = new IndexMinPQ<Double>(numVertices);
     	Stack<Integer> paths = new Stack<Integer>();
		double[] dist = new double[numVertices];          				// distance of shortest path
     	Edge[] edge = new Edge[numVertices];    						// last edge of shortest path
     	double[] weightArr = new double[numVertices];
     	int[] parent = new int[numVertices];
     	boolean found = false;
        for (int v = 0; v < numVertices; v++){
            dist[v] = Double.POSITIVE_INFINITY;
            weightArr[v] = 0;
            parent[v] = 0;
        }
        dist[src] = 0;
        parent[src] = src;
        
        minPQ.insert(src, dist[src]);
        while (!minPQ.isEmpty() && !found) {
            int v = minPQ.delMin();
            for (Edge e : adj(v)) {
                rEdge(v, e, minPQ, dist, edge, weightArr, parent);
            }
            if(v == dest) 
				found = true;
            if(minPQ.isEmpty() && !found) 
				return "No path exists between Source and Destination.\n";
        }
        
        paths.push(dest);
        int v = parent[dest];
        while(v != src){
         	paths.push(v);
         	v = parent[v];
         }
        paths.push(src);
        
        StringBuilder output = new StringBuilder("SHORTEST " + pType + " PATH " + vertices.get(src)  + " to " + vertices.get(dest) + "\n");
		output.append("---------------------------------------------------------\n");
		if (pType.equals("DISTANCE"))
			output.append("Shortest distance path from " + vertices.get(src)  + " to " + vertices.get(dest) + " is " + dist[dest] + "\n");
		else if (pType.equals("PRICE"))
			output.append("Lowest cost path from " + vertices.get(src)  + " to " + vertices.get(dest) + " is " + dist[dest] + "\n");
        while(!paths.isEmpty()){
        	v = paths.pop();
        	if(weightArr[v] != 0) output.append(weightArr[v] + " ");
        	output.append(vertices.get(v) + " ");
        }
        output.append("\n");
        return output.toString();
        
    }
	
    public void initMax(double max){
    	boolean[] mark;
    	double[] weightArr;
    	double weight = 0;
    	ArrayList<Integer> path;
    	
    	System.out.println("ALL PATHS OF COST " + max + " OR LESS");
		System.out.println("Note that paths are duplicated, once from each end city's point of view");
		System.out.println("------------------------------------------------------------------------");
    	this.changeEdgeWeightToPrice();
    	
    	for(int i = 0; i < numVertices; i++){
    		mark = new boolean[numVertices];
    		weightArr = new double[numVertices];
    		weight = 0;
    		path = new ArrayList<Integer>();
    		
    	    for (int j = 0; j < numVertices; j++){
    			mark[j] = false;
    			weightArr[j] = 0.0;
    		}	
    	
    		path.add(i);
    		mark[i] = true;
    		max(i, max, weight, weightArr, path, mark);
    	
    	}
    }
	
    public void max(int v, double max, double weight, double[] weight_array, ArrayList<Integer> paths, boolean[] mark){    
    	Queue<Integer> q = new Queue<Integer>();
    	double[] weightArr = Arrays.copyOf(weight_array, weight_array.length);

    	for(Edge e : adj(v)){
    		if(!mark[e.other(v)] && (weight + e.weight() <= max) ){
    			q.enqueue(e.other(v));
    			weightArr[e.other(v)] = e.weight();
    		}
    	}
    	
    	while(!q.isEmpty()){
    		int w = q.dequeue();
    		double tempweight = weight + weightArr[w];
    		
    		mark[w] = true;
    		paths.add(w);

			StringBuilder max_path = new StringBuilder("Cost: " + tempweight + ", Path: ");
        	for(int i = 0; i < paths.size(); i++){
        		int x = paths.get(i);
        		if (weightArr[x] != 0) 
					max_path.append(weightArr[x] + " ");
        		max_path.append(vertices.get(x) + " ");
       		}
       			
        	max_path.append("\n");
        	System.out.println(max_path.toString());

    		if(tempweight != max){
    			max(w, max, tempweight, weightArr, paths, mark);
    		}
    			
    		Integer removeVertex = new Integer(w);
    		paths.remove(removeVertex);
    		mark[w] = false;		
    	}	
    }

    private void rEdge(int v, Edge edge, IndexMinPQ<Double> minPQ, double[] dist, Edge[] edges, double[] weightArr, int[] parent) {
        int w = edge.other(v);
        if (dist[w] > dist[v] + edge.weight()) {
            dist[w] = dist[v] + edge.weight();
            edges[w] = edge;
            weightArr[w] = edge.weight();
            parent[w] = v;
            if (minPQ.contains(w)) 
				minPQ.decreaseKey(w, dist[w]);
            else 
				minPQ.insert(w, dist[w]);
        }
    }


}
