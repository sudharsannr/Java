package primMST;
import java.util.LinkedList;

/**
 * Class SimpleWrapper is used as a wrapper class to return the least cost node in the form of an edge 
 * and node for the prim's algorithm.
 * @author Sudharsan
 *
 */
public class SimpleWrapper
{
	Edge edge;
	VertexNode vNode;
	SimpleWrapper(Edge edge, VertexNode vNode) {
		this.edge = edge;
		this.vNode = vNode;
	}
}

/**
 * Class Edge represents the node and its weight. This class is used as representation for adjacency list.
 * @author Sudharsan
 */
class Edge
{
	//Node of edge
	int dNode;
	//Weight of edge
	int weight;

	Edge(int dNode, int weight) 
	{ 
		this.dNode = dNode; 
		this.weight = weight;
	}
    
	int getNode() 
    { 
    	return dNode; 
    }
    
	int getWeight()
    {
    	return weight;
    }
 
}

 
/**
 * Class VertexNode represents the vertex and maintains the adjacency list of the vertex
 * @author Sudharsan
 *
 */
class VertexNode
{	
	//Vertex node
	int node;
	//Neighbor list
	LinkedList<Edge> successors = new LinkedList<Edge>();

	VertexNode(int node) 
	{ 
		this.node = node; 
	}
    
	int getNode() 
    { 
    	return node; 
    }
    
	LinkedList<Edge> getSuccessors() 
    { 
    	return successors; 
    }
	
	@Override
	public boolean equals(Object object)
	{
	    boolean isEqual= false;
	    if (object != null && object instanceof VertexNode)
	        isEqual = (this.node == ((VertexNode) object).getNode());
	    return isEqual;
	}
}

/**
 * Class EdgeFile is used as a formatted input to represent the edge when read from file. 
 * @author Sudharsan
 *
 */
class EdgeFile
{
	private
	//Edge source
	int from;
	//Edge destination
	int to;
	//Edge cost
	int weight;
	
	EdgeFile(int from, int to, int weight)
	{
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	public int getFrom()
	{
		return from;
	}
	
	public int getTo()
	{
		return to;
	}
	
	public int getWeight()
	{
		return weight;
	}
	
	@Override
	public boolean equals(Object object)
	{
	    boolean isEqual= false;
	    if (object != null && object instanceof EdgeFile)
	        isEqual = ((this.from == ((EdgeFile) object).getFrom() && this.to == ((EdgeFile) object).getTo()) || 
	        		(this.to == ((EdgeFile) object).getFrom() && this.from == ((EdgeFile) object).getTo())
	        		|| (this.from == this.to));
	    return isEqual;
	}
	
}

