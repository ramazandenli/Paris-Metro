
import java.util.Iterator;
import java.util.NoSuchElementException;



public class Vertex<T> implements VertexInterface<T> {

	private T label;
	private ListInterface<Edge> edgeList; // Edges to neighbors
	private boolean visited;
	private VertexInterface<T> previousVertex;
	private double cost;

	public Vertex(T vertexLabel) {
		label = vertexLabel;
		edgeList = new LList<>();
		visited = false;
		previousVertex = null;
		cost = 0;
	} // end constructor
	
	
	
	public boolean hasPredecessor() {
		
		return previousVertex!=null;
	}
	

	
	public T getLabel() {
	
	    return label;
	}
	
	public Iterator<Double> getWeightIterator()
	{
		return new WeightIterator();
	} // end getNeighborIterator

	private class WeightIterator implements Iterator<Double>
	{
		int edgeIndex = 1;  
		private WeightIterator()
		{
			edgeIndex = 1; 
		} // end default constructor

		public boolean hasNext()
		{
			return edgeIndex <= edgeList.getLength();
		} // end hasNext

		public Double next()
		{
			Double edgeWeight=null;

			if (hasNext())
			{
				edgeWeight = edgeList.getEntry(edgeIndex).getWeight();
				edgeIndex++;
			}
			else
				throw new NoSuchElementException();

			return edgeWeight;
		} // end next

		public void remove()
		{
			throw new UnsupportedOperationException();
		} // end remove
	} // end NeighborIterator﻿

	public Iterator<T> getEdgeLabelIterator()
	{
		return new EdgeLabelIterator();
	} // end getNeighborIterator

	private class EdgeLabelIterator implements Iterator<T>
	{
		int edgeIndex = 1;  
		private EdgeLabelIterator()
		{
			edgeIndex = 1; 
		} // end default constructor

		public boolean hasNext()
		{
			return edgeIndex <= edgeList.getLength();
		} // end hasNext

		public T next()
		{
			T edgeLabel=null;

			if (hasNext())
			{
				edgeLabel = edgeList.getEntry(edgeIndex).getLabel();
				edgeIndex++;
			}
			else
				throw new NoSuchElementException();

			return edgeLabel;
		} // end next

		public void remove()
		{
			throw new UnsupportedOperationException();
		} // end remove
	} // end NeighborIterator﻿

	
	

	public boolean connect(VertexInterface<T> endVertex, double edgeWeight,T label) {

		boolean result = false;

		if (!this.equals(endVertex)) {

			
			edgeList.add(new Edge(endVertex, edgeWeight,label));
			result=true;
		}
		return result;
	}

	public boolean connect(VertexInterface<T> endVertex,T label) {
		return connect(endVertex, 0,label);
	}	
	

	public VertexInterface<T> getPredecessor() {
		return previousVertex;
	}

	public void setPredecessor(VertexInterface<T> predecessor) {
		this.previousVertex = predecessor;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public void visit() {
		this.visited = true;
	}

	public void unvisit() {
		this.visited = false;
	}

	public boolean isVisited() {
		return this.visited;
	}

	public VertexInterface<T> getUnvisitedNeighbor() {
		
		VertexInterface<T> result = null;

		Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
		while (neighbors.hasNext() && (result == null))
		{
			VertexInterface<T> nextNeighbor = neighbors.next();
			if (!nextNeighbor.isVisited())
				result = nextNeighbor;
		} // end while

			return result;
	}

	public boolean hasEdge(VertexInterface<T>neighbor) {
		boolean found = false;
		Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
		while (neighbors.hasNext())
		{
			VertexInterface<T> nextNeighbor = neighbors.next();
			if (nextNeighbor==neighbor)
			{
				found = true;
				break;
			}
		} // end while

		return found;
	}
	
	public boolean hasNeighbor() {
		
		return !edgeList.isEmpty();
	}

	public Iterator<VertexInterface<T>> getNeighborIterator()
	{
		return new NeighborIterator();
	} // end getNeighborIterator

	private class NeighborIterator implements Iterator<VertexInterface<T>>
	{
		int edgeIndex = 1;  
		private NeighborIterator()
		{
			edgeIndex = 1; 
		} // end default constructor

		public boolean hasNext()
		{
			return edgeIndex <= edgeList.getLength();
		} // end hasNext

		public VertexInterface<T> next()
		{
			VertexInterface<T> nextNeighbor = null;

			if (hasNext())
			{
				nextNeighbor = edgeList.getEntry(edgeIndex).getEndVertex();
				edgeIndex++;
			}
			else
				throw new NoSuchElementException();

			return nextNeighbor;
		} // end next

		public void remove()
		{
			throw new UnsupportedOperationException();
		} // end remove
	} // end NeighborIterator﻿

	protected class Edge {
		private VertexInterface<T> vertex; // Vertex at end of edge
		private double weight;
		private T label;

		protected Edge(VertexInterface<T> endVertex, double edgeWeight,T label) {
			vertex = endVertex;
			weight = edgeWeight;
			this.label=label;
		}

		public T getLabel() {
			return label;
		}

		public void setLabel(T label) {
			this.label = label;
		}

		// end constructor
		protected VertexInterface<T> getEndVertex() {
			return vertex;
		}

		// end getEndVertex
		protected double getWeight() {
			return weight;
		} // end getWeight
	} // end Edge
} // end Vertex
