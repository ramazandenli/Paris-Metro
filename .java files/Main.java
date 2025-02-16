import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Main {
	
	public static String getLeastCostlyLine(String begin,String end,DirectedGraph<String> metro) {
		
		VertexInterface<String> beginVertex=metro.getVertices().getValue(begin);
		VertexInterface<String> endVertex=metro.getVertices().getValue(end);
		
		java.util.Iterator<VertexInterface<String>> neighbors =beginVertex.getNeighborIterator();
		java.util.Iterator<Double> weights =beginVertex.getWeightIterator();
		java.util.Iterator<String> labels =beginVertex.getEdgeLabelIterator();
		
		
		double minimum=Double.MAX_VALUE;
		
		String label=null;;
		
		while(neighbors.hasNext() && weights.hasNext() && labels.hasNext()) {
			
			VertexInterface<String> nextNeighbor=neighbors.next();
			double nextWeight=weights.next();
			String nextLabel=labels.next();
			
			
			if(nextNeighbor.getLabel().equals(end)) {
				
				if(nextWeight<minimum) {
					
					minimum=nextWeight;
					label=nextLabel;
				}
			}
		}
		
		return label;
		
		
	}
	
	public static void printSuggestion(StackInterface<String> path,DirectedGraph<String> metro) {
		
		DictionaryInterface<ArrayList<String>,String> lines=new Dictionary<>();
		

		String vertexName=path.pop();
		
		while(!path.isEmpty()) {
			
			String secondVertexName=path.peek();
			
			String way=getLeastCostlyLine(vertexName,secondVertexName,metro);
			
			ArrayList<String> list=new ArrayList<String>();
			
			list.add(vertexName);
			list.add(secondVertexName);
			
			lines.add(list,way);
			
			vertexName=secondVertexName;
			path.pop();
			
			
			
		}
		
		java.util.Iterator<String> wayIterator=lines.getValueIterator();
		
		java.util.Iterator<ArrayList<String>> listIterator=lines.getKeyIterator();
		
		String firstLine =  wayIterator.next();
		ArrayList<String> list=listIterator.next();
		String firstStop=list.get(0);
		String lastStop;
		
		int stopCount=1;
		
		
		while(wayIterator.hasNext()) {
			
			String nextLine=wayIterator.next();
			
			if(firstLine.equals(nextLine)) {
				
				stopCount++;
				list=listIterator.next();
				
				
				
				
			}
			else {
				
				lastStop=list.get(1);
				
				if(firstLine.equals("walk")) {
					
					System.out.println("Walk :");
				}
				else {
					
					System.out.println("Line "+firstLine+":");
					
				}
				
				System.out.println(firstStop+" - "+lastStop+" ("+stopCount+" stations)");
				
				firstLine=nextLine;
				stopCount=1;
				list=listIterator.next();
				firstStop=list.get(0);
				
				
				
			}
		}
	
		lastStop=list.get(1);
		
		if(firstLine.equals("walk")) {
			
			System.out.println("Walk :");
		}
		else {
			
			System.out.println("Line "+firstLine+":");
			
		}
		System.out.println(firstStop+" - "+lastStop+" ("+stopCount+" stations)");
	}
	
	public static void main(String[] args) throws IOException {
		
		DirectedGraph<String> metro=new DirectedGraph<String>(); 
		
		File f=new File("Paris_RER_Metro_v2.csv");
		
		FileReader fr=new FileReader(f);
		
		BufferedReader br=new BufferedReader(fr);
		
		
		String line=br.readLine();
		line=br.readLine();
		
		String[] splitedLine=line.split(",");
		
		String stopName=splitedLine[1];
		int arrivalTime=Integer.parseInt(splitedLine[2]);
		String directionId=splitedLine[4];
		String routeShortName=splitedLine[5];
		int stopSequence=Integer.parseInt(splitedLine[3]);    
		
		metro.addVertex(stopName);
		
		
		while(line!=null) {
			
			
			line=br.readLine();
			
			if(line==null) {
				
				break;
			}
			splitedLine=line.split(",");
			
			if(!metro.hasVertex(splitedLine[1])) {
				
				metro.addVertex(splitedLine[1]);
			}


			
			if(splitedLine[4].equals(directionId) && splitedLine[5].equals(routeShortName) && (Integer.parseInt(splitedLine[3])-stopSequence==1)) {
				
				metro.addEdge(stopName, splitedLine[1],Math.abs(Integer.parseInt(splitedLine[2])-arrivalTime),routeShortName);

			}

			stopName=splitedLine[1];
			arrivalTime=Integer.parseInt(splitedLine[2]);
			directionId=splitedLine[4];
			routeShortName=splitedLine[5];
			stopSequence=Integer.parseInt(splitedLine[3]);  

		}
		
		br.close();
		
		
		File f2=new File("walk_edges.txt");
		
		FileReader fr2=new FileReader(f2);
		
		BufferedReader br2=new BufferedReader(fr2);
		
		
		line=br2.readLine();
		
		while(line!=null) {
			
			splitedLine=line.split(",");
			
			metro.addEdge(splitedLine[0],splitedLine[1],300,"walk");
			
			line=br2.readLine();
		}
		
		br2.close();
		
		
	    File f3=new File("Test100.csv");
		
		FileReader fr3=new FileReader(f3);
		
		BufferedReader br3=new BufferedReader(fr3);
		
		
		line=br3.readLine();
		
		StackInterface<String> testPath=new LinkedStack<String>();
		
		long time=System.nanoTime();
		
		
		while(line!=null) {
			
			testPath=new LinkedStack<String>();
			
			splitedLine=line.split(",");
			
			
		    if(splitedLine[2].equals("0")) {
		    	
		    	metro.getShortestPath(splitedLine[0], splitedLine[1], testPath);
		    }
		    else if(splitedLine[2].equals("1")) {
		    	
		    	metro.getCheapestPath(splitedLine[0], splitedLine[1], testPath);
		    }
			
			line=br3.readLine();
		}
		
		br3.close();
		
		System.out.println("The average query time is "+((System.nanoTime()-time)/100)+"ns.");
		
	
		Scanner sc=new Scanner(System.in);
		
		System.out.println("Do you want to make point to point query?(Y/N)");
		
		String answer=sc.nextLine();
		
		if(answer.equalsIgnoreCase("Y")) {
			
			System.out.print("Origin station: ");
			
			String originStation=sc.nextLine().trim();
			
			System.out.print("Destination station: ");
			
			String destinationStation=sc.nextLine().trim();
			
			System.out.print("Preferetion: ");
		    
			System.out.println();
			System.out.println("Suggestion");
			System.out.println();
			
			String preferetion=sc.nextLine().trim();
			
			if(!metro.hasVertex(originStation) || !metro.hasVertex(destinationStation)) {
				
				System.out.println("please enter stops which exists");
			}
			
			else {
				
				StackInterface<String> path=new LinkedStack<String>();
				
				double benchmark=0;
				
				String unit=null;
				
				if(preferetion.trim().equalsIgnoreCase("Minimum Time")) {
					
					
					benchmark=metro.getCheapestPath(originStation,destinationStation, path);
					benchmark=benchmark/60;
					unit="min";

				
					
					
				}
				
				
				else if(preferetion.trim().equalsIgnoreCase("Fewer Stops")) {
					
					benchmark=metro.getShortestPath(originStation,destinationStation, path);
					unit="stops";
					
				}
				
				printSuggestion(path, metro);
				System.out.println("\n");
				System.out.println(benchmark+" "+unit);
				
				
			}
		}
		
		
		

	}

}
