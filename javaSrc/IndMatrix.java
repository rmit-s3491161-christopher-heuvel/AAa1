import java.io.*;
import java.util.*;
import java.util.Map.Entry;



/**
 * Incidence matrix implementation for the FriendshipGraph interface.
 * 
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 */
public class IndMatrix <T extends Object> implements FriendshipGraph<T>
{
	private HashMap<T, Integer> Vertices;
	private int numVertex, numEdges, rowSize, columnSize;
	private boolean indMat[][], increaser[][], tempMat[][];
	
	/**
	 * Contructs empty graph.
	 */
    public IndMatrix() {
		rowSize = 1; //row size of matrix aka verticex on matrix
		columnSize = 1; //column size of matrix aka edges on matrix
		numVertex = 0; //number of vertices
		numEdges = 0; //number of edges
		
		indMat = new boolean[rowSize][columnSize];
		tempMat = new boolean[rowSize][columnSize];
		Vertices = new HashMap();
    } // end of IndMatrix()
      
    public void addVertex(T vertLabel) {
		if (!Vertices.containsKey(vertLabel)) {
			Vertices.put(vertLabel, columnSize);
			numVertex++;
		}
		
		//increase matrix
		if (numVertex == rowSize){
			rowSize++;
			columnSize++;	
			increaser = new boolean[rowSize][columnSize];
			indMat = increaser;
			
			for (int i = 0; i < rowSize - 1; i++) {
				for (int j = 0; j < columnSize - 1; j++) {
					indMat[i][j] = tempMat[i][j];
				}
			}
			tempMat = indMat;
		}
    } // end of addVertex()
	    
    public void addEdge(T srcLabel, T tarLabel) {
		
		Integer v1 = Vertices.get(srcLabel);
        Integer v2 = Vertices.get(tarLabel);
		//Integer edgeLabel = Vertices.merge(srcLabel,tarLabel);
		
		if ((!Vertices.containsKey(srcLabel)) || (!Vertices.containsKey(tarLabel))) {
			throw new IllegalArgumentException("One or more of the vertices do not exist.");
		}
		
		
		indMat[v1][v2] = true;
		indMat[v2][v1] = true;
		
		tempMat = indMat;
    } // end of addEdge()
	
    public ArrayList<T> neighbours(T vertLabel) {
		Integer v1 = Vertices.get(vertLabel);
        ArrayList<T> neighbours = new ArrayList<T>();
		
        if (!Vertices.containsKey(vertLabel)) {
			throw new IllegalArgumentException("The vertex does not exist.");
		}
		
		for (int i = 0; i < columnSize; i++) {
            if (indMat[v1][i])
                neighbours.add(vertexLabel(i));
        }

        return neighbours;
    } // end of neighbours()
    
    public void removeVertex(T vertLabel) {
		
		Integer v1 = Vertices.get(vertLabel);
		
		if (!Vertices.containsKey(vertLabel)){
			throw new IllegalArgumentException("Vertex " + vertLabel + " does not exist.");
		}
		
		Vertices.remove(vertLabel);
		numVertex--;
		
		for (int i = 0; i < rowSize; i++){
			indMat[i][v1] = false;
			indMat[v1][i] = false;
		}
		tempMat = indMat;
    } // end of removeVertex()
    
    public void removeEdge(T srcLabel, T tarLabel) {
		Integer v1 = Vertices.get(srcLabel);
        Integer v2 = Vertices.get(tarLabel);
		
		if ((!Vertices.containsKey(srcLabel)) || (!Vertices.containsKey(tarLabel))) {
		throw new IllegalArgumentException("One or more of the vertices do not exist.");
		}
		
		indMat[v1][v2] = false;
		indMat[v2][v1] = false;
		tempMat = indMat;
    } // end of removeEdges()	
    
    public void printVertices(PrintWriter os) {
		Iterator localIterator = Vertices.keySet().iterator();

		while (localIterator.hasNext())
		{
		  os.print(localIterator.next() + " ");
		}
		os.println();
    } // end of printVertices()
    
    public void printEdges(PrintWriter os) {
		
		 for (int i = 0; i < rowSize; i++) {

            for (int j = 0; j < columnSize; j++) {

                if (indMat[i][j]) {
                    os.println(vertexLabel(i) + " " + vertexLabel(j));
                }
            }
        }
    } // end of printEdges()
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
		Integer v1 = Vertices.get(vertLabel1);
        Integer v2 = Vertices.get(vertLabel2);
		
		if ((!Vertices.containsKey(vertLabel1)) || (!Vertices.containsKey(vertLabel2))) {
		throw new IllegalArgumentException("One or more of the vertices do not exist.");
		}
		
		boolean[] checked = new boolean[rowSize];
		int[] distance = new int[rowSize];

		for (int i = 0; i < rowSize; i++) {
            distance[i] = disconnectedDist;
        }
		
		distance[v1] = 0;
        checked[v1] = true; 	
		
		Queue<Integer> queue = new LinkedList<Integer>();
        queue.offer(v1);

        while (!queue.isEmpty()) {

            int i = queue.poll();

            if (i == v2) {
                return distance[i];
            }

            for (int j = 0; j < rowSize; j++) {
                // check if connected to parent and not checked
                if (indMat[i][j] && !checked[j]) {
                    distance[j] = distance[i] + 1;
                    checked[j] = true;
                    queue.offer(j);
                }
            }
        }
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    
	private T vertexLabel(int label) {

        for (Entry<T, Integer> entry : Vertices.entrySet()) {

            if (entry.getValue().equals(label)) {
                return entry.getKey();
            }

        }

        return null;
    }
} // end of class IndMatrix
