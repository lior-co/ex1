package ex1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import ex1.WGraph_DS.NodeInfo;

public class WGraph_Algo implements weighted_graph_algorithms {
	
	private weighted_graph g=new WGraph_DS();
	
	int sumV; //node counter
	
	public void BFS(node_info n) { //Dijkstra's Algorithm
		sumV=0;
		((WGraph_DS) this.g).clearall();
		PriorityQueue<node_info> q= new PriorityQueue<>(); //make new priority queue q
		n.setTag(0); ////set n tag as 0
		q.add(n); // add n to the queue
		while(!q.isEmpty()) { //while q is not empty
			node_info n1=q.poll(); //poll from q
			if(!n1.getInfo().equals("yes")) { //if this node need to get visit
				if(n1.getInfo().equals("no")) sumV++; //and 1 to node counter
				n1.setInfo("yes"); //set the info as non check yet
				Iterator<node_info> iterNi=g.getV(n1.getKey()).iterator(); //use iterator on the neighbors
				double tag1=n1.getTag(); //tag of this node
				while (iterNi.hasNext()) { //while we have a neighbor
					node_info n2=iterNi.next(); //n2 - node of the next neighbor
					double tag2=n2.getTag(); //tag of n2
					double tag12=tag1+g.getEdge(n1.getKey(),n2.getKey());//value of tag1+edge(n1 to n2) 
					if(tag2>tag12) {
						n2.setTag(tag12); //set tag12 as the new n2 tag 
						((NodeInfo) n2).setDaddy(n1); //remember n1 as dad of n2
						if(n2.getInfo().equals("no")) sumV++; //and 1 to node counter
						n2.setInfo("temp"); //check this node again
						q.add(n2); //add to q
					}
				}
				
			}
		}
	}

	@Override
	public void init(weighted_graph g) {
		this.g=g;		
	}

	@Override
	public weighted_graph getGraph() {
		return this.g;
	}

	@Override
	public weighted_graph copy() {
		WGraph_DS gnew=new WGraph_DS();
		gnew.copy(g);
		return gnew;
	}

	@Override
	public boolean isConnected() {
		if(this.g==null) return true;
		if(this.g.nodeSize()>1 && this.g.edgeSize()==0) return false; //more then 1 node and no edges
		if(this.g.nodeSize()==1 && this.g.edgeSize()==0) return true; //only 1 node end no edges
		Iterator<node_info> iter=this.g.getV().iterator(); //using Iterator
		if(iter.hasNext()) {
			node_info root=iter.next(); //the first node
			BFS(root);
		}

		if (sumV==this.g.nodeSize()) return true; //if sumV of BFS equals the v in the graph	
		return false;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if(g==null || g.getNode(dest)==null || g.getNode(src)==null) return -1;
		if(src==dest) return g.getNode(src).getTag();
		if(g.hasEdge(src, dest)) return g.getEdge(src, dest);
		BFS(this.g.getNode(src)); //use BFS algorithm
		return g.getNode(dest).getTag(); //return distant from src to dest
	}

	@Override
	public List<node_info> shortestPath(int src, int dest) {
		if(g==null || g.getNode(dest)==null || g.getNode(src)==null) return null;
		
		List<node_info> list=new ArrayList<>(); //make a new list
		list.add(g.getNode(src));

		if(src==dest) return list;
		BFS(g.getNode(src));
		Stack<node_info> st=((NodeInfo) g.getNode(dest)).sugarDaddy(); //make new stack st
		while(!st.isEmpty()) {
			list.add(st.pop());	
		}
		return list;
	}

	@Override
	public boolean save(String file) {
		int num=1;//number of 
		Iterator<node_info> iter=g.getV().iterator(); //using Iterator	
			FileWriter writer;
			try {
				writer = new FileWriter(file); //write to file
				writer.write("Lior's Graph !\n");
				writer.write("Node_= [:Key: :Info: :Tag:]\n");
				writer.write("Node_"+" neighbors= [:Key: :Info: :Tag:]->Edge size\n\n");
				
				while(iter.hasNext()) {
					NodeInfo n=(NodeInfo)iter.next();
					//writer Node_num_= [:Key: :Info: :Tag:]
					writer.write("Node"+ num++ +"= [:"+n.getKey()+": :"+n.getInfo()+": :"+n.getTag()+":]"); // Override ...
					Iterator<node_info> iterNi=g.getV(n.getKey()).iterator(); //using Iterator
					writer.write("\n");
					while(iterNi.hasNext()) {
						NodeInfo ni=(NodeInfo)iterNi.next();
						double eVal=g.getEdge(n.getKey(),ni.getKey());
						//write the neighbor  [:Key: :Info: :Tag:]->Edge size
						writer.write("		[:"+ni.getKey()+": :"+ni.getInfo()+": :"+ni.getTag()+":]->"+eVal+"\n");
					}
					writer.write("\n");
				}
				writer.close();
			} 
			catch (IOException e) {
				System.err.println("	We have some problem here :(	");
				e.printStackTrace();
				return false;
			}

		return true;
	}

	@Override
	public boolean load(String file) {
		String str;
		File f1 = new File(file);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f1));
			str = br.readLine(); //down a row
			str = br.readLine();
			str = br.readLine();
			str = br.readLine();
			while((str = br.readLine()) != null) {
				String[] sp=str.split(":"); //split string str by ":"
				
				int key=Integer.parseInt(sp[1]); //key
				String info=sp[3]; //info
				double tag=Double.parseDouble(sp[5]); //tag
				
				g.addNode(key); //add new node by this key
				g.getNode(key).setInfo(info); //set this info to the node
				g.getNode(key).setTag(tag); //set this info to the node
				
				System.out.println(g.getNode(key).getInfo());
								
				while((str = br.readLine()) != null && str.contains(">")) {
					String[] sp2=str.split(":"); //split string str by ":"
					
					String[] sp3=str.split(">"); //split the edge size by ">"
					double w=Double.parseDouble(sp3[1]); //edge size
										
					int key2=Integer.parseInt(sp2[1]); //key of this neighbor
					String info2=sp2[3]; //info  of this neighbor
					double tag2=Double.parseDouble(sp2[5]); //tag of this neighbor
					
					g.addNode(key2); //add new node by this key
					g.getNode(key2).setInfo(info2); //set this info to the node
					g.getNode(key2).setTag(tag2); //set this info to the node
					g.connect(key,key2,w); //connect between node to the neighbor 
				}
			}
				
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
