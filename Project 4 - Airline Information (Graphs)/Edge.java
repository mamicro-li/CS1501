// Sharon Gao
// CS 1501, Assignment 4
import java.util.*; 
	
public class Edge implements Comparable<Edge>{ 
	private double distance;
    private double price;
    private double weight;
    private final int v; // vertex
    private final int w; // weight
    

    public Edge(int x, int y, double d, double p) {
        distance = d;
        price = p;
        weight = d;
		v = x;
        w = y;
    }
    
    public Edge(int x, int y) {
        distance = 0;
        price = 0;
        weight = 0;
		v = x;
        w = y;
    }
    
    public double distance() {
        return distance;
    }

    public double price() {
        return price;
    }
    
    public double weight() {
        return weight;
    }

	public int either() {
        return v;
    }

   	public int other(int vertex) {
        if (vertex == v) 
			return w;
        else if (vertex == w) 
			return v;
        else 
			throw new IllegalArgumentException("Illegal endpoint");
    }
    
    public void changeWeightToDistance(){
    	weight = distance;
    }
    
    public void changeWeightToPrice(){
    	weight = price;
    }


    public int compareTo(Edge that) {
        if (this.weight() < that.weight()) 
			return -1;
        else if (this.weight() > that.weight()) 
			return +1;
        else         	                        
			return  0;
    }
    
    public boolean equals(Object obj){
    	Edge that = (Edge)obj;
    	if (this.either() == that.either() && this.other(this.either()) == that.other(that.either())) 		
			return true;
    	else if (this.either() ==  that.other(that.either()) && this.other(this.either()) == that.either()) 	
			return true;
    	else 
			return false;
    }
	
	//Arraylist of Vertices used to convert between vertex Name and Number.
	//Flag used to print out either weight, or both distance and weight
    public String toString(ArrayList<String> vertices, int flag) {
    	StringBuilder s = new StringBuilder();
    	s.append(vertices.get(v) + " , " + vertices.get(w) + " : ");
    	if (flag == 1) {
    		s.append(weight);
    		return s.toString();
    	}
    	else {
    		s.append(distance + " / " + price);
    		return s.toString();
    	}
    }

}
