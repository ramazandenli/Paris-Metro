public final class HeapPriorityQueue<T extends Comparable<? super T>>
                   implements PriorityQueueInterface<T>
{
	private MinHeapInterface<T> pq;	
	
	public HeapPriorityQueue()
	{
		pq=new MinHeap<T>();
		
	} // end default constructor
	
	public void add(T newEntry)
	{ 
		pq.add(newEntry);
	} // end add

	public T remove()
	{
		return pq.removeMin();
	} // end remove
		
	public T peek()
	{
		return pq.getMin();
	} // end peek

	public boolean isEmpty()
	{
	   return  pq.isEmpty();
	} // end isEmpty
	
	public int getSize()
	{
		return pq.getSize();
	} // end getSize

	public void clear()
	{
		pq.clear();
	} // end clear
} // end HeapPriorityQueue
