/**
 * A project on hashing use cases.
 * Hashes text file points.txt consisting of 1 million coordinates and finds the two closest points.
 * The output of the program is the distance between the two points.
 *
 * This program can be run by redirecting the input of the file to a txt file of decimal numbers.
 * The file MUST be formatted as pairs of decimal numbers separated by a space per line.
 * IF THIS FORMAT IS NOT FOLLOWED, THE PROGRAM WILL NOT WORK AS INTENDED OR AT ALL.
 *
 * @author Zachary Holt
 * @version 10/21/24
 */

import java.util.Scanner;

/**
 * Class Closest - All the methods and fields needed to run the program.
 */
public class Closest {
    /**
     * Class Node - the inner class that contains the container that will hold our data for processing.
     */
    public static class Node{
        public double[] data = new double[2];
        public Node next;

        /**
         * The constructor for the Node inner class.
         * @param p1 the coordinate point corresponding to x for the Node.
         * @param p2 the coordinate point corresponding to y for the Node.
         * @param next a pointer that points to another node, used for chaining in the hash table.
         */
        public Node(double p1, double p2, Node next)
        {
            this.data[0] = p1;
            this.data[1] = p2;
            this.next = next;
        }
    }

    //Fields for the Closest class.
    private static final int B = 100;
    private Node[][] grid;

    //Methods for the Closest class.

    /**
     * Constructor for the Closest class.
     * Initializes our grid and scans in the text file to fill the hash table.
     */
    public Closest(){
        //initialize grid
        grid = new Node[B][B];
        //read in points to hash table.
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            double pointX = Double.parseDouble(scanner.next());
            double pointY = Double.parseDouble(scanner.next());
            insert(pointX, pointY);
        }
        scanner.close();
        System.out.println("Loaded File");
    }

    /**
     * Inserts a point pair into the hash table via encapsulation within a Node class.
     * We hash x and y individually to give us the specific grid the pair will be sent to.
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public void insert(double x, double y){
        Node insertion = new Node(x, y, null);
        int hashLocX = hash(x);
        int hashLocY = hash(y);
        if(grid[hashLocX][hashLocY] == null){
            grid[hashLocX][hashLocY] = insertion;
        }
        else{
            Node chain = grid[hashLocX][hashLocY];
            while(chain.next != null){
                chain = chain.next;
            }
            chain.next = insertion;
        }
    }

    /**
     * The formula we use to hash the points to corresponding grids.
     * @param key the coordinate we are hashing to the table.
     * @return
     */
    private int hash(Double key){
        double h = 0;
        h = (key % B);
        return (int) h;
    }

    /**
     * Finds the two closest points in the entire grid and returns the value.
     * Checks a 3x3 area centered on the grid square of the current point.
     * @return a double containing the distance between the closest two points.
     */
    public double findClosest(){
        double currentClosest = 1000;
        double tempClosest;
        //traverse our grid starting at grid[0][0] to grid[b-1][b-1].
        for(int i = 0; i < B; i++){
            for(int j = 0; j < B; j++){
                if(grid[i][j] != null){
                    //The node we want to check the others to.
                    Node cur = grid[i][j];
                    do{
                        //check node against all in center.
                        tempClosest = checkNodes(cur, i, j);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //check node against all top left.
                        tempClosest = checkNodes(cur, i-1, j-1);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //check node against above.
                        tempClosest = checkNodes(cur, i, j-1);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //check node against top right.
                        tempClosest = checkNodes(cur, i+1, j-1);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //check node against left.
                        tempClosest = checkNodes(cur, i-1, j);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //check node against right.
                        tempClosest = checkNodes(cur, i+1, j);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //check node against bottom left.
                        tempClosest = checkNodes(cur, i-1, j+1);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //check node against below.
                        tempClosest = checkNodes(cur, i, j+1);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //check node against bottom right.
                        tempClosest = checkNodes(cur, i+1, j+1);
                        if(tempClosest < currentClosest){
                            currentClosest = tempClosest;
                        }
                        //iterate cur to end of current grid box.
                        cur = cur.next;
                    }while(cur.next != null);
                }
            }
        }
        return currentClosest;
    }

    /**
     * A helper method for the findClosest() method.
     * Checks all the nodes within a specific grid square.
     * @param cur the current node we are comparing everything to.
     * @param i the current index i corresponding to the x-axis of the grid.
     * @param j the current index j corresponding to the y-axis of the grid.
     * @return the closest distance between two points within the grid square.
     */
    private double checkNodes(Node cur, int i, int j){
        double currentClosest = 1000;
        if((i < B && i >= 0) && (j < B && j >= 0)){
            if(grid[i][j] != null){
                Node comparator = grid[i][j];
                do{
                    double currentX = cur.data[0];
                    double currentY = cur.data[1];
                    double compX = comparator.data[0];
                    double compY = comparator.data[1];
                    if(currentX != compX || currentY != compY){
                        double temp = calculateDistance(currentX, currentY, compX, compY);
                        if(temp < currentClosest){
                            currentClosest = temp;
                        }
                    }
                    comparator = comparator.next;
                }while(comparator.next != null);
            }
        }
        return currentClosest;
    }

    /**
     * Takes two pairs of coordinates and calculates the distance between them.
     * @param x1 the x value of the first pair.
     * @param y1 the y value of the first pair.
     * @param x2 the x value of the second pair.
     * @param y2 the y value of the second pair.
     * @return the distance between the two pairs of points.
     */
    private double calculateDistance(double x1, double y1, double x2, double y2){
        double xMinus = (x2 - x1);
        xMinus = Math.pow(xMinus, 2);
        double yMinus = (y2 - y1);
        yMinus = Math.pow(yMinus, 2);
        double sum = xMinus + yMinus;
        sum = Math.sqrt(sum);
        return sum;
    }

    /**
     * Main method of the Closest Class.
     * @param args redirect the program input to the text file you want to use.
     */
    public static void main(String[] args){
        Closest test = new Closest();
        System.out.println(test.findClosest());
    }
}
