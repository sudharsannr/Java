package primMST;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class mst computes the minimum spanning tree for the given graph using Prim's Algorithm. There are two modes user input and random mode.
 * User Input Mode 
 * Simple Mode: Executable -s <file-name>
 * F-heap Mode: Executable -f <file-name>
 * Random Mode: Executable -r <no of vertices> <density(in order of 10, 20, .. 100%)> 
 * @author Sudharsan
 */
public class mst {

	/**
	 * main
	 * -s <file-name> for Simple mode
	 * -f <file-name> for F-heap mode
	 * -r <no of vertices> <density(in order of 10, 20, .. 100)> for Random mode
	 * @param args to accept the command line arguments to run in either simple, f-Heap or random mode
	 */
	
	public static ArrayList<Boolean> dfsVisited = new ArrayList<Boolean>();
	
	public static void main(String[] args) 
	{
		//Classify into modes - Simple, F-heap and Random
		String mode = args[0];
		//User input simple mode
		if("-s".equals(mode))
		{
			System.out.println("Simple Mode");
			String fileName = args[1];
			if(fileName.equals(null))
			{
				System.out.println("Use -s <file-name> to execute");
			}
			else
				primUserInput(fileName, "-s");
		}
		//User input f-heap mode
		else if("-f".equals(mode))
		{
			System.out.println("F-heap Mode");
			String fileName = args[1];
			if(fileName.equals(null))
			{
				System.out.println("Use -f <file-name> to execute");
			}
			else
				primUserInput(fileName, "-f");
		}
		//Random mode
		else if("-r".equals(mode))
		{
			//Calculate the no of edges using density
			int nVer = Integer.parseInt(args[1]);
			double density = Double.parseDouble(args[2])/100;
			int nEdg = (int) (density * nVer * (nVer - 1)/2);
			System.out.println("Random Mode " + nVer + " " + args[2]);
			System.out.println("Generating graph");
			primRandom(nVer, nEdg);
		}
		else
		{
			System.out.println("Use -r <n> <d> (or) -s <file-name> (or) -f <file-name> to execute");
		}
	}

	/**
	 * primFMST executes the prim's algorithm using the Fibonacci Heap data structure 
	 * @param adjList containing the adjacencylist representation of the graph
	 * @param nVer is the number of vertices of the graph
	 * @return minimum spanning tree
	 */
	private static ArrayList<EdgeFile> primFMST(ArrayList<VertexNode> adjList, int nVer) {
		/*
		 * 1. Add the current vertex and mark it as read.
		 * 2. Get successors of the vertex and add it to the fHeap.
		 * 3. RemoveMin() and obtain the minimum distance edge.
		 * 4. Add the result only if the vertex is not visited and set it as the next vertex to populate the successors.
		 * 5. Repeat until all the vertices are visited.
		 */
		int count = 0, key = 0;
		int minVertex = 0, minKey = 0, srcVertex = 0;
		int tEdgeNode, tEdgeWeight;
		ArrayList<EdgeFile> mst = new ArrayList<EdgeFile>();
		FibonacciHeap fHeap = new FibonacciHeap();
		FibonacciHeapNode fHeapNode = new FibonacciHeapNode(adjList.get(0).getNode(), key, srcVertex);
		FibonacciHeapNode fMinNode = new FibonacciHeapNode(minVertex, minKey, srcVertex);
		Boolean fVisited[] = new Boolean[adjList.size()];
		for(int i = 0; i < nVer; i++)
			fVisited[i] = false;
		VertexNode vn = adjList.get(0);
		LinkedList<Edge> successors = new LinkedList<Edge>();
		fVisited[0] = true;
		while(count < nVer-1)
		{
			successors = vn.getSuccessors();
			srcVertex = vn.getNode();
			//Populate the successors to the f-Heap
			for(Edge edge : successors)
			{
				tEdgeNode = edge.getNode();
				tEdgeWeight = edge.getWeight();
				if(!fVisited[tEdgeNode])
				{
					fHeapNode = new FibonacciHeapNode(tEdgeNode, tEdgeWeight, srcVertex);
					fHeap.insert(fHeapNode);
				}
			}
			fMinNode = fHeap.min();
			//Add the least cost edge only if it is not yet added
			if(!fVisited[fMinNode.data])
			{
				mst.add(new EdgeFile(fMinNode.source, fMinNode.data, fMinNode.key));
				fVisited[fMinNode.data] = true;
				count++;
				//Set the next vertex to proceed
				vn = adjList.get(fMinNode.data);
			}
			//Removemin to remove the least cost edge
			fHeap.removeMin();
		}
		return mst;
	}

	/**
	 * PrimRandom function takes the no of vertices and edges as the parameter. This function randomly generates the graph and
	 * checks whether the graph is connected or not using depth-first search. The random edges(i,j) are added with the condition such
	 * that edge(i,j) is not in the graph.
	 * @param nVer is the number of vertices in the graph
	 * @param nEdg is the number of edges in the graph
	 */
	private static void primRandom(int nVer, int nEdg) {
		//TODO : Remove comparisons
		Random generator = new Random();
		int i, j, cost, edgeCount = 0;
		ArrayList<VertexNode> adjList = new ArrayList<VertexNode>();
		for(i = 0; i < nVer; i++)
			adjList.add(new VertexNode(i));
		//Random graph generation begins
		StringBuffer sb = new StringBuffer();
		Boolean iFlag = false, jFlag = false;
		while(edgeCount < nEdg)
		{
			i = generator.nextInt(nVer);
			j = generator.nextInt(nVer);
			iFlag = false;
			jFlag = false;
			cost = generator.nextInt(1000) + 1;
			/*Check for the neighboring vertices if (i,j) form the same edge already added onto the graph only if i and j
			are not the same*/
			if(i != j)
			{
				for(Edge et : adjList.get(i).getSuccessors())
				{
					if(j == et.getNode())
					{
						iFlag = true;
						break;
					}
				}
				if(!iFlag)
				{
					for(Edge et : adjList.get(j).getSuccessors())
					{
						if(i == et.getNode())
						{
							jFlag = true;
							break;
						}
					}
				}
			}
			//Evaluating condition to add the edge to the graph
			if(!(i == j || iFlag || jFlag))
			{
				sb.append("\n" + i + " " + j + " " + cost);
			    adjList.get(i).getSuccessors().add(new Edge(j, cost));
			    adjList.get(j).getSuccessors().add(new Edge(i, cost));
			    edgeCount++;
			}
		}
		dfsVisited.clear();
		for(int i1 = 0; i1 < adjList.size(); i1++)
			dfsVisited.add(false);
		VertexNode vertex = adjList.get(0);
		
		//Call depth-first search for the first vertex of the graph
		dfs(vertex, adjList);
		
		//If the graph is connected, call the prim's algorithm
		if(Collections.frequency(dfsVisited, true) == dfsVisited.size())
		{
			System.out.println("Graph generated. Running prim's algorithm.");
			long start = 0, stop = 0, stime = 0, ftime = 0;
			start = System.currentTimeMillis();
			//Assignment for debug
			//ArrayList<EdgeFile> mst = primMST(adjList, nVer);
			primMST(adjList, nVer);
			stop = System.currentTimeMillis();
			stime = stop - start;
			start = System.currentTimeMillis();
			//Assignment for debug
			//ArrayList<EdgeFile> fmst = primFMST(adjList, nVer);
			primFMST(adjList, nVer);
			stop = System.currentTimeMillis();
			ftime = stop - start;
			System.out.println("Simple" + "\t" + stime + "\nF-Heap" + "\t" + ftime);
		}
		//If the graph is not connected, call the random mode again to generate a connected graph
		else
		{
			primRandom(nVer, nEdg);
		}
	}

	/**
	 * printAdjList method to print adjacency list
	 * Method for debug purposes
	 * @param adjList is the adjacency list to be printed
	 */
	/*private static void printAdjList(ArrayList<VertexNode> adjList) {
		for(int i = 0; i < adjList.size(); i++)
		{
			System.out.print(adjList.get(i).getNode());
			for(int j = 0; j < adjList.get(i).getSuccessors().size(); j++)
			{
				System.out.print("->" + adjList.get(i).getSuccessors().get(j).getNode() + "(" + adjList.get(i).getSuccessors().get(j).getWeight() + ")");
			}
			System.out.println();
		}
	}*/


	/**
	 * dfs method uses Depth-first search to check whether the graph is connected or not. The parameter VertexNode is the current 
	 * vertex of the graph adjList which is the adjacency list.
	 * @param vn is the vertex which has to be called recursively for its successors
	 * @param adjList is the adjacency list representation of the graph
	 */
	private static void dfs(VertexNode vn, ArrayList<VertexNode> adjList) {
		//Setting true to the visited node
		dfsVisited.set(vn.getNode(), true);
		LinkedList<Edge> neighbours = vn.getSuccessors();
		//Depth-first search routine to neighbors of the current visited vertex
		for(Edge nb : neighbours)
		{
			VertexNode vn2 = adjList.get(adjList.indexOf(new VertexNode(nb.getNode())));
			if(!dfsVisited.get(vn2.getNode()))
				dfs(vn2, adjList);
		}
	}

	/**
	 * primUserInput method receives the input from the file and adds it into the adjacency list. Calls the 
	 * method that runs prim's algorithm.
	 * @param fileName represents the filename from the command-line argument
	 * @param cmdParam represents whether the command-line is either for simple or f-Heap mode
	 */
	private static void primUserInput(String fileName, String cmdParam) {
		BufferedReader br = null;
		try 
		{
			br = new BufferedReader(new FileReader(fileName));
			String sCurrentLine;
			sCurrentLine = br.readLine();
			String[] param = sCurrentLine.split("\\s+");
			int nVer = Integer.parseInt(param[0]);
			int nEdg = Integer.parseInt(param[1]);
			ArrayList<EdgeFile> edgeList = new ArrayList<EdgeFile>();
			ArrayList<VertexNode> adjList = new ArrayList<VertexNode>();
			String tempStr[];
			//Reading from file
			for(int i = 0; i < nEdg; i++)
			{
				tempStr = br.readLine().split("\\s+");
				edgeList.add(new EdgeFile(Integer.parseInt(tempStr[0]), Integer.parseInt(tempStr[1]), Integer.parseInt(tempStr[2])));
			}
			
			//Converting the edges to adjacency list
			adjList = convertToAdjacencyList(edgeList, nVer);
			
			ArrayList<EdgeFile> mst = new ArrayList<EdgeFile>();
			//Call the prim's algorithm using the adjacency list
			if("-s".equalsIgnoreCase(cmdParam))
			{
				mst = primMST(adjList, nVer);
			}
			if("-f".equalsIgnoreCase(cmdParam))
			{
				mst = primFMST(adjList, nVer);
			}
			
			//Print the result
			System.out.println("Minimum spanning tree: ");
			StringBuffer sb = new StringBuffer();
			int weight = 0;
			for(EdgeFile test : mst)
			{
				sb.append("\n" + test.getFrom() + " " + test.getTo());
				weight += test.getWeight();
			}
			sb.insert(0, weight);			
			System.out.println(sb.toString());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				if (br != null)br.close();
			}
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
	}

	/**
	 * convertToAdjacency method list gets the edgeList and converts it to the adjacency list.
	 * @param edgeList contains the list representation of the edges in the graph
	 * @param nVer is the number of vertices in the graph
	 * @return ArrayList<VertexNode> as the adjacency list
	 */
	private static ArrayList<VertexNode> convertToAdjacencyList(ArrayList<EdgeFile> edgeList, int nVer) {
		ArrayList<VertexNode> adjList = new ArrayList<VertexNode>();
		Edge newEdge, newEdge2;
		for(int i = 0; i < nVer; i++)
		{
			adjList.add(new VertexNode(i));
		}
		for(EdgeFile edge : edgeList)
		{
			newEdge = new Edge(edge.getTo(), edge.getWeight());
		    adjList.get(edge.getFrom()).getSuccessors().add(newEdge);
		    newEdge2 = new Edge(edge.getFrom(), edge.getWeight());
		    adjList.get(edge.getTo()).getSuccessors().add(newEdge2);
		}
		return adjList;
	}

	/**
	 * primMST method employs the prim's minimum spanning tree algorithm
	 * @param VertexNode is the list of vertices in the graph
	 * @param nVer is the number of vertices in the graph
	 * @return the minimum spanning tree representation of the graph
	 */
	private static ArrayList<EdgeFile> primMST(ArrayList<VertexNode> VertexNode, int nVer) {
		int count = 1;
		ArrayList<EdgeFile> mst = new ArrayList<EdgeFile>();
		VertexNode temp = VertexNode.get(0);
		ArrayList<VertexNode> tNode = new ArrayList<VertexNode>();
		boolean visited[] = new boolean[VertexNode.size()];
		VertexNode fromNode = temp;
		int index = 0;
		//Initialize the visited flag to false
		for(int i = 0; i < nVer; i++)
			visited[i] = false;
		//Loop for prim's routine until all the vertices are visited
		while(count < nVer)
		{
			index = VertexNode.indexOf(temp);
			//Add the current vertex and mark it as visited
			tNode.add(VertexNode.get(index));
			visited[index] = true;
			
			//Get the least cost node of the current vertex which is not visited
			SimpleWrapper sw = getLeastCostSuccessor(tNode, visited);
			Edge tEdge = sw.edge;
			fromNode = sw.vNode;

			//Make the least cost vertex as the current vertex to check its neighbors for the next loop
			temp = new VertexNode(tEdge.getNode());
			//Add the least cost node to the minimum spanning tree
			mst.add(new EdgeFile(fromNode.getNode(), tEdge.getNode(), tEdge.getWeight()));
			count++;
		}
		return mst;
	}
	
	/**
	 * getLeastCostSuccessor Method returns the least cost node of the current vertex which is not visited.
	 * @param tNode is the vertex list
	 * @param visited contains data whether the vertex is visited or not
	 * @return the least cost edge of the graph
	 */
	private static SimpleWrapper getLeastCostSuccessor(ArrayList<VertexNode> tNode, boolean[] visited)
	{
		int minCost = Integer.MAX_VALUE;
		Edge minNode = null;
		VertexNode fromNode = null;
		//Calculate the least cost of the neighbors of the current node list
		for(VertexNode vNode : tNode)
		{
			//Iterate through the neighbors of the node
			for(Edge Node : vNode.getSuccessors())
			{
				//Find and assign the least cost node
				if(minCost > Node.getWeight() && !visited[Node.getNode()])
				{
					minCost = Node.getWeight();
					minNode = Node;
					fromNode = vNode;
				}
			}
		}
		//Wrapper object for the minimum cost node and return
		SimpleWrapper sw = new SimpleWrapper(minNode, fromNode);
		return sw;
	}

}
