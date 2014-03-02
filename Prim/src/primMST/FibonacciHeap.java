package primMST;
import java.util.ArrayList;
import java.util.List;

/**
 * class FibonacciHeap contains the methods and members for a fibonacci heap data structure
 * @author Sudharsan
 *
 */
public class FibonacciHeap
{
	//Number of nodes in fibonacci heap
	private int nNodes;
	//Fibonacci heap node containing the minimum value
	private FibonacciHeapNode minNode;
	//Degree Constant limit
    private static final Double degreeConstant = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);

    public FibonacciHeap()
    {
    }

    /**
     * pairwiseMerge method performs pairwise merge on the f-Heap which is called only for the removeMin() operation 
     */
    protected void pairwiseMerge()
    {
        int arraySize = ((int) Math.floor(Math.log(nNodes) * degreeConstant)) + 1;
        List<FibonacciHeapNode> degreeTbl = new ArrayList<FibonacciHeapNode>(arraySize);
        //Initialization
        for (int i = 0; i < arraySize; i++) 
            degreeTbl.add(null);

        int numRoots = 0;
        //Traverse through the heap to obtain the count
        FibonacciHeapNode minNd = minNode;
        if (minNd != null) 
        {
            numRoots++;
            minNd = minNd.right;
            while (minNd != minNode) 
            {
                numRoots++;
                minNd = minNd.right;
            }
        }

        //Loop until the number of roots is empty
        while (numRoots > 0) 
        {
            //Traverse the node to its right
            int d = minNd.degree;
            FibonacciHeapNode next = minNd.right;

            for (;;) 
            {
                FibonacciHeapNode y = degreeTbl.get(d);
                //If no duplicate degree is present skip
                if (y == null) 
                {
                    break;
                }
                //If duplicate degree is present, set minimum value node as the parent
                if (minNd.key > y.key) 
                {
                    FibonacciHeapNode temp = y;
                    y = minNd;
                    minNd = temp;
                }
                setParent(y, minNd);
                degreeTbl.set(d, null);
                d++;
            }
            degreeTbl.set(d, minNd);
            minNd = next;
            numRoots--;
        }

        minNode = null;
        FibonacciHeapNode tNode;
        for (int i = 0; i < arraySize; i++) 
        {
            tNode = degreeTbl.get(i);
            if (tNode == null) 
            {
                continue;
            }
            //Remove the node from the list and add it to the root and update minimum value 
            if (minNode != null) {
                tNode.left.right = tNode.right;
                tNode.right.left = tNode.left;
                tNode.left = minNode;
                tNode.right = minNode.right;
                minNode.right = tNode;
                tNode.right.left = tNode;
                if (tNode.key < minNode.key) 
                {
                    minNode = tNode;
                }
            } 
            else 
            {
                minNode = tNode;
            }
        }
    }

    /**
     * method removeMin removes the minimum value from the f-heap
     * @return node with the smallest key
     */
    public FibonacciHeapNode removeMin()
    {
        FibonacciHeapNode minNd = minNode;

        if (minNd != null) 
        {
            int childCount = minNd.degree;
            FibonacciHeapNode nextNode;
            FibonacciHeapNode minChild = minNd.child;
            while (childCount > 0) 
            {
                nextNode = minChild.right;

                //remove minChild from child list
                minChild.left.right = minChild.right;
                minChild.right.left = minChild.left;
                
                //add minChild to root list of heap and set it's parent to null
                minChild.left = minNode;
                minChild.right = minNode.right;
                minNode.right = minChild;
                minChild.right.left = minChild;
                minChild.parent = null;
                minChild = nextNode;
                childCount--;
            }

            //remove minNode from root list of heap
            minNd.left.right = minNd.right;
            minNd.right.left = minNd.left;
            if (minNd == minNd.right) 
            {
                minNode = null;
            } 
            else 
            {
                minNode = minNd.right;
                pairwiseMerge();
            }
            nNodes--;
        }
        return minNd;
    }

    /**
     * insert method adds a new element to the heap
     * @param node to be inserted to the heap
     * @param key is the value to be inserted(cost)
     */
    public void insert(FibonacciHeapNode node)
    {
        //if the f-Heap is empty add it to the list
        if (minNode != null) 
        {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            node.right.left = node;
            if (node.key < minNode.key) 
            {
                minNode = node;
            }
        } 
        else 
        {
            minNode = node;
        }
        nNodes++;
    }

    /**
     * method min returns the minimum value of the f-Heap
     * @return heap node with the smallest key
     */
    public FibonacciHeapNode min()
    {
        return minNode;
    }

    /**
     * size method returns the number of elements in the f-Heap
     * @return number of elements in the heap
     */
    public int size()
    {
        return nNodes;
    }
    
    /**
     * setParent method performs the join operation where one node is made as the child of another node
     * @param childNd is the to-be child node
     * @param parentNd is the node to be made as the parent
     */
    protected void setParent(FibonacciHeapNode childNd, FibonacciHeapNode parentNd)
    {
        //remove the node from the heap
        childNd.left.right = childNd.right;
        childNd.right.left = childNd.left;

        //make it as parent
        childNd.parent = parentNd;

        if (parentNd.child == null) 
        {
            parentNd.child = childNd;
            childNd.right = childNd;
            childNd.left = childNd;
        } 
        else 
        {
            childNd.left = parentNd.child;
            childNd.right = parentNd.child.right;
            parentNd.child.right = childNd;
            childNd.right.left = childNd;
        }
        parentNd.degree++;
        //initialize node for cut
        childNd.mark = false;
    }
}


/**
 * class FibonacciHeapNode represents the node for the f-Heap
 * @author Sudharsan
 *
 */
class FibonacciHeapNode
{

	int source, data, key, degree;
    FibonacciHeapNode child, parent, left, right;
    boolean mark;

    public FibonacciHeapNode(int data, int key, int source)
    {
    	this.source = source;
    	this.data = data;
    	this.key = key;
        right = this;
        left = this;
    }

    public final int getKey()
    {
        return key;
    }

    public final int getData()
    {
        return data;
    }
}