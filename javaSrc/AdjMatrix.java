import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class AdjMatrix <T extends Object> implements FriendshipGraph<T>
{
    private int matrixSize;           // Current size of matrix
    private int matrixMltply;		  // How much should matrixSize be multiplied when full
    private int numVertices;		  // The number of vertices
    private int lastVertex;			  // The number of the last vertex
    private int freedInts[]; 		  // Freed ints
    private int availInts;			  // Available ints
    private boolean adjMatrix[][];    // The matrix
    private Map<T, Integer> vertices; // Map of Vertices -> Integers

    /**
	 * Constructs empty graph.
	 */
    public AdjMatrix() {

        matrixSize = 6;
        matrixMltply = 2;                                   // matrixSize * 2 when full
        numVertices = 0;			                        // Amount of vertices in graph
        lastVertex = 0;				                        // Last vertex

        freedInts	= new int[matrixSize];					// Freed ints
        availInts	= -1;

        adjMatrix   = new boolean[matrixSize][matrixSize];    // The matrix
        vertices    = new HashMap<T, Integer>();            // All our vertices pointing to an integer
    } // end of AdjMatrix()
    
    
    public void addVertex(T vertLabel) {
        // Prevent duplicates
        if (vertices.containsKey(vertLabel)) {
            return;
        }

        // Expand capacity if matrix size is insufficient
        if (numVertices == matrixSize) {
            expandMatrix();
        }

        int newID = findNewInt();

        if (newID >= lastVertex) {
            lastVertex = newID + 1;
        }

        vertices.put(vertLabel, newID);

    } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) {
        Integer srcID = vertices.get(srcLabel);
        Integer tarID = vertices.get(tarLabel);

        if (srcID == null || tarID == null) {
            throw new IllegalArgumentException("Error! one or more vertices do not exist: " + srcLabel + " " + tarLabel);
        }

        adjMatrix[srcID][tarID] = true;
        adjMatrix[tarID][srcID] = true;
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) {
        Integer tarID = vertices.get(vertLabel);

        if (tarID == null) {
            throw new IllegalArgumentException("Error! vertex does not exist: " + vertLabel);
        }

        ArrayList<T> neighbours = new ArrayList<T>();

        for (int row = 0; row < lastVertex; row++) {
            if (adjMatrix[tarID][row])
                neighbours.add(getVertexLabel(row));
        }

        return neighbours;

    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) {
        Integer tarID = vertices.get(vertLabel);

        if (tarID == null) {
            throw new IllegalArgumentException("Error! vertex does not exist: " + vertLabel);
        }

        vertices.remove(vertLabel);
        freedInts[++availInts] = tarID;
        numVertices--;

        // Loop through every row
        for (int row = 0; row < lastVertex; row++) {
            adjMatrix[row][tarID] = false;
            adjMatrix[tarID][row] = false;
        }
    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) {
        Integer srcID = vertices.get(srcLabel);
        Integer tarID = vertices.get(tarLabel);

        if (srcID == null || tarID == null) {
            throw new IllegalArgumentException("Error! one or more vertices do not exist: " + srcLabel + " " + tarLabel);
        }

        adjMatrix[srcID][tarID] = false;
        adjMatrix[tarID][srcID] = false;
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) {
        Iterator<T> iterator = vertices.keySet().iterator();

        // Print all vertices
        while (iterator.hasNext()) {
            os.print(" " + iterator.next());
        }

        // Print new line and flush
        os.println();
        os.flush();
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) {

        for (int i = 0; i < lastVertex; i++) {

            for (int j = 0; j < lastVertex; j++) {

                if (adjMatrix[i][j]) {
                    os.println(getVertexLabel(i) + " " + getVertexLabel(j));
                }
            }
        }

        os.flush();

    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
        Integer srcID = vertices.get(vertLabel1);
        Integer tarID = vertices.get(vertLabel2);

        if (srcID == null || tarID == null) {
            throw new IllegalArgumentException("Error! targets don't exist: " + vertLabel1 + " " + vertLabel2);
        }

        // checked array to prevent constant looping over same node
        boolean[] checked = new boolean[lastVertex];

        // distance array to calculate all distances
        int[] distance = new int[lastVertex];

        // initialise distances to -1
        for (int i = 0; i < lastVertex; i++) {
            distance[i] = disconnectedDist;
        }

        distance[srcID] = 0; 		// source to source = 0
        checked[srcID] = true; 	    // don't loop over itself

        // queue to manage the loop below
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.offer(srcID);

        // Loop over our queue until empty
        while (!queue.isEmpty()) {

            int i = queue.poll(); // grab next element


            if (i == tarID) {
                return distance[i];
            }

            // loop through each neighbour
            for (int j = 0; j < lastVertex; j++) {
                // check if connected to parent and not checked
                if (adjMatrix[i][j] && !checked[j]) {
                    distance[j] = distance[i] + 1;
                    checked[j] = true;
                    queue.offer(j);
                }
            }
        }

        return disconnectedDist;    	
    } // end of shortestPathDistance()

    private int findNewInt() {

        if (availInts >= 0) {
            return freedInts[availInts--];
        }
        else {
            return numVertices++;
        }

    }


    private T getVertexLabel(int tarID) {

        for (Entry<T, Integer> entry : vertices.entrySet()) {

            if (entry.getValue().equals(tarID)) {
                return entry.getKey();
            }

        }

        return null;
    }

    private void expandMatrix() {
        // create new variables that are double the size of the originals
        int newSize = matrixSize * matrixMltply;
        int[] largerFreedInts = new int[newSize];
        boolean[][] largerMatrix = new boolean[newSize][newSize];

        // copy to bigger freedints array
        for (int i = 0; i < lastVertex; i++)
            largerFreedInts[i] = freedInts[i];

        // copy to bigger adjMatrix array
        for (int i = 0; i < lastVertex; i++) {

            for (int j = 0; j < lastVertex; j++) {
                largerMatrix[i][j] = adjMatrix[i][j];

            }
        }

        matrixSize = newSize;
        adjMatrix = largerMatrix;
        freedInts = largerFreedInts;
    }
//v2
} // end of class AdjMatrix