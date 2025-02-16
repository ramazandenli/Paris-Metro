

import java.util.Iterator;


public class DirectedGraph<T> implements GraphInterface<T> {

	private DictionaryInterface<T, VertexInterface<T>> vertices;
	private int edgeCount;

	public boolean addVertex(T vertexLabel) {

		VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));

		return addOutcome == null;
	} // end addVertex﻿

	public boolean addEdge(T begin, T end, double edgeWeight,T label) {
		boolean result = false;
		VertexInterface<T> beginVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		if ((beginVertex != null) && (endVertex != null))

			result = beginVertex.connect(endVertex, edgeWeight,label);

		if (result)
			edgeCount++;

		return result;

	} // end addEdge﻿

	public boolean hasEdge(T begin, T end) {

		boolean found = false;
		VertexInterface<T> beginVertex = vertices.getValue(begin);

		VertexInterface<T> endVertex = vertices.getValue(end);

		if ((beginVertex != null) && (endVertex != null)) {

			Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();

			while (!found && neighbors.hasNext()) {

				VertexInterface<T> nextNeighbor = neighbors.next();

				if (endVertex.equals(nextNeighbor))
					found = true;
			} // end while
		}

		// end if
		return found;
	} // end hasEdge

	public boolean addEdge(T begin, T end,T label) {

		return addEdge(begin, end, 0,label);

	} // end addEdge﻿

	public boolean isEmpty() {
		return vertices.isEmpty();
	} // end isEmpty

	public void clear() {
		vertices.clear();
		edgeCount = 0;
	} // end clear

	public int getNumberOfVertices() {
		return vertices.getSize();
	} // end getNumberOfVertices

	public int getNumberOfEdges()

	{

		return edgeCount;
	}

	public int getShortestPath(T begin, T end, StackInterface<T> path) {

		resetVertices();
		boolean done = false;
		QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<VertexInterface<T>>();
		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		originVertex.visit();

		vertexQueue.enqueue(originVertex);
		while (!done && !vertexQueue.isEmpty()) {

			VertexInterface<T> frontVertex = vertexQueue.dequeue();
			Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();

			while (!done && neighbors.hasNext()) {
				VertexInterface<T> nextNeighbor = neighbors.next();
				if (!nextNeighbor.isVisited()) {
					nextNeighbor.visit();
					nextNeighbor.setCost(1 + frontVertex.getCost());
					nextNeighbor.setPredecessor(frontVertex);
					vertexQueue.enqueue(nextNeighbor);
				}
				
				if (nextNeighbor.equals(endVertex)) {

					done = true;
				}

			}
		}

		int pathLength = (int) endVertex.getCost();
		path.push(endVertex.getLabel());
		VertexInterface<T> vertex = endVertex;

		while (vertex.hasPredecessor()) {
			vertex = vertex.getPredecessor();
			path.push(vertex.getLabel());
		}

	
		return pathLength;

	}
	
	
		

	public double getCheapestPath(T begin, T end, StackInterface<T> path) {

		resetVertices();
		boolean done = false;

		PriorityQueueInterface<EntryPQ> pq = new HeapPriorityQueue<EntryPQ>();
		


		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		pq.add(new EntryPQ(originVertex, 0, null));
		
		while(!done && !pq.isEmpty()) {
			
			EntryPQ frontEntry=pq.remove();
			VertexInterface<T> frontVertex=frontEntry.getVertex();
			
			if(!frontVertex.isVisited()) {
				
				frontVertex.visit();
				frontVertex.setCost(frontEntry.getCost());
				frontVertex.setPredecessor(frontEntry.getPredecessor());
				
				if(frontVertex.equals(endVertex)) {
					
					done=true;
				}
				else {
					
					Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
					Iterator<Double> weights = frontVertex.getWeightIterator();
					
					
					while(neighbors.hasNext()) {
						
						VertexInterface<T> nextNeighbor=neighbors.next();
						double weight=weights.next();
						
						double cost=frontEntry.getCost()+weight;
						
						if(!nextNeighbor.isVisited()) {
							
							pq.add(new EntryPQ(nextNeighbor,cost,frontVertex));
						}
						
					}
				}
			}
			
		}
		
		double pathCost=endVertex.getCost();
		
		path.push(endVertex.getLabel());
		
		VertexInterface<T> vertex=endVertex;
		
		while(vertex.hasPredecessor()) {
			
			vertex=vertex.getPredecessor();
			path.push(vertex.getLabel());
		}
		
		return pathCost;
		
		

	}

	public DirectedGraph() {
		vertices = new Dictionary<>();
		edgeCount = 0;
	} // end default constructor
	
	public boolean hasVertex(T vertexLabel) {
		
		return vertices.contains(vertexLabel);
	}
	
	protected void resetVertices()
	{
	  Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
	  while (vertexIterator.hasNext())
	  {
	    VertexInterface<T> nextVertex = vertexIterator.next();
	    nextVertex.unvisit();
	    nextVertex.setCost(0);
	    nextVertex.setPredecessor(null);
	  } // end while
	} // end resetVertices
	
	
	public DictionaryInterface<T, VertexInterface<T>> getVertices(){
		
		return vertices;
	}

	public class EntryPQ implements Comparable<EntryPQ >{

		private VertexInterface<T> vertex;
		private double cost;
		private VertexInterface<T> predecessor;

		public EntryPQ(VertexInterface<T> vertex,double cost,VertexInterface<T> predecessor){
			
			this.vertex=vertex;
			this.cost=cost;
			this.predecessor=predecessor;
			
		}


		public VertexInterface<T> getVertex() {
			return vertex;
		}

		public void setVertex(VertexInterface<T> vertex) {
			this.vertex = vertex;
		}

		public double getCost() {
			return cost;
		}

		public void setCost(double cost) {
			this.cost = cost;
		}

		public VertexInterface<T> getPredecessor() {
			return predecessor;
		}

		public void setPredecessor(VertexInterface<T> predecessor) {
			this.predecessor = predecessor;
		}

		public int compareTo(EntryPQ entry) {

			if (this.cost > entry.cost) {

				return 1;
			} else {

				return -1;
			}

		}

	}

}
