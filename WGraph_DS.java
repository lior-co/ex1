package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class WGraph_DS implements weighted_graph {
	
	int mc;
	int sume; // counter of Edges
	private HashMap<Integer,node_info> gn=new HashMap<Integer,node_info>(); //graph node
	private static int count=0; //count of key

	public WGraph_DS() {
		this.gn=new HashMap<Integer,node_info>();
		this.sume=0;
		this.mc=0;
		count=0;
	}
	
	public boolean nodeEquals(node_info n1, node_info n2) {
		if(n1.getKey()==n2.getKey() && n1.getInfo().equals(n2.getInfo()) && n1.getTag()==n2.getTag()) return true;
		return false;		
	}
	
	public boolean equals(Object g) {
		WGraph_DS wg =(WGraph_DS) g;
		if(this.edgeSize()!=wg.edgeSize() && this.nodeSize()!=wg.nodeSize()) return false;		

		Iterator<node_info> iter=wg.getV().iterator(); //using Iterator
			while(iter.hasNext()) {
				node_info N =iter.next();
				node_info N2=gn.get(N.getKey());
				if(!nodeEquals(N,N2)) return false;
				
				Iterator<node_info> iterNi=wg.getV(N.getKey()).iterator();
				while(iterNi.hasNext()) {
					node_info Ni=iterNi.next();
					node_info Ni2=gn.get(Ni.getKey());
					if(getEdge(N.getKey(),Ni.getKey())!=wg.getEdge(N2.getKey(),Ni2.getKey())) return false;
				}
			}		
		return true;	
	}
	
	public static class NodeInfo implements node_info,Comparable<NodeInfo>{
		
		int key;
		double tag;
		String info;
		private HashMap<Integer,node_info> nib=new HashMap<Integer,node_info>(); //neighbors for node
		private HashMap<Integer,Double> ge=new HashMap<Integer,Double>(); //edges for node
		node_info daddy; //use for shortestPath
		
		public NodeInfo() {
			this.key=count++;
			this.tag=0;
			this.info="";
			this.nib=new HashMap<Integer,node_info>();
			this.ge=new HashMap<Integer,Double>();
			this.daddy=null;
		}
		public NodeInfo(node_info n) {
			this.key=n.getKey();
			this.tag=n.getTag();
			this.info=n.getInfo();	
			this.nib=new HashMap<Integer,node_info>();
			this.ge=new HashMap<Integer,Double>();
			this.daddy=null;
		}
		public NodeInfo(int key) {
			this.key=key;
			count=key;
			count++;
			this.tag=0;
			this.info="";
			this.nib=new HashMap<Integer,node_info>();
			this.ge=new HashMap<Integer,Double>();
			this.daddy=null;
		}
		
		public node_info getDaddy() { //get daddy node
			return this.daddy;
		}
		
		public void setDaddy(node_info daddy) { //remember the node for shortestPath
			this.daddy= daddy;
		}
		
		public boolean hasDaddy() { //if has shortestPath node
			if(this.daddy!=null) return true;
			return false;
		}
		
		public Stack<node_info> sugarDaddy(){ //stack for shortestPath - list of daddy
			Stack<node_info> st= new Stack<node_info>(); //make new stack st
			st.add(this);
			if(hasDaddy()) {
				NodeInfo n=(NodeInfo)getDaddy();
				while(n.hasDaddy()) { //while we have daddy
					st.add(n); //add to stack st
					n=(NodeInfo)n.getDaddy(); //n is the next daddy
				}
			}		
			return st;			
		}
		
		@Override
		public int getKey() {
			return this.key;
		}

		@Override
		public String getInfo() {
			return this.info;
		}

		@Override
		public void setInfo(String s) {
			this.info=s;
		}

		@Override
		public double getTag() {
			return this.tag;
		}

		@Override
		public void setTag(double t) {
			this.tag=t;
		}

		public int compareTo(NodeInfo o) { //use for PriorityQueue
			if(tag<o.tag) return 1; //return biggest
			else if(tag==o.tag) return 0;
			return -1; //change
		}
		
	}
	
	public void clearall() {
		for (node_info n:getV()) {
			n.setTag(Double.POSITIVE_INFINITY); //set tag as infinity;
			n.setInfo("no"); //set info as no
		}
	}
	
	@Override
	public node_info getNode(int key) {
		if(gn.containsKey(key)) return gn.get(key);	//checking if they are node with this key
		return null;
	}

	@Override
	public boolean hasEdge(int node1, int node2) {
		NodeInfo e1=(NodeInfo)getNode(node1);
		NodeInfo e2=(NodeInfo)getNode(node2);
		if(node1!=node2 && e1!=null && e2!=null && e1.ge.containsKey(node2) && e2.ge.containsKey(node1)) return true;	
		return false;
	}

	@Override
	public double getEdge(int node1, int node2) {
		NodeInfo e1=(NodeInfo)getNode(node1);
		if(hasEdge(node1,node2)) return e1.ge.get(node2);
		return -1;
	}

	@Override
	public void addNode(int key) {
		
		if(!gn.containsKey(key))
		{
			gn.put(key, new NodeInfo(key)); //put the node n in the graph
			this.mc++;
		}		
	}

	@Override
	public void connect(int node1, int node2, double w) {
		NodeInfo n1=(NodeInfo)getNode(node1);
		NodeInfo n2=(NodeInfo)getNode(node2);

		if(node1!=node2 && n1!=null && n2!=null && !hasEdge(node1,node2) && w>=0) {		
			n1.nib.put(node2,n2); //n2 neighbor of n1
			n2.nib.put(node1,n1); //n1 neighbor of n2
			
			n1.ge.put(node2,w); //put the value of w in the edge between 1,2
			n2.ge.put(node1,w); //put the value of w in the edge 2,1
			sume++;
			mc++;
		}
		else if(node1!=node2 && n1!=null && n2!=null && hasEdge(node1,node2) && w>=0) {
			n1.ge.put(node2,w); //put the new value of w in the edge between 1,2
			n2.ge.put(node1,w); //put the new value of w in the edge 2,1
			mc++;
		}
	}

	@Override
	public Collection<node_info> getV() {
		return gn.values();
	}

	@Override
	public Collection<node_info> getV(int node_id) {
		return ((NodeInfo)this.gn.get(node_id)).nib.values();
	}

	@Override
	public node_info removeNode(int key) {		
		node_info n1=getNode(key); //get the node n1 by using key		
		if(n1==null)return null;
		Iterator<node_info> iter=getV(key).iterator();
		while(iter.hasNext()) {
			NodeInfo n2=(NodeInfo)iter.next();
			n2.nib.remove(n1.getKey());
			sume--;
		}
		getV(n1.getKey()).clear();
		this.gn.remove(key);
		this.mc++;	
		return n1;
	}

	@Override
	public void removeEdge(int node1, int node2) {
		NodeInfo e1=(NodeInfo)getNode(node1);
		NodeInfo e2=(NodeInfo)getNode(node2);	
		if(hasEdge(node1,node2)) {
			e1.ge.remove(node2); //remove e2 from edge list of e1
			e2.ge.remove(node1); //remove e1 from edge list of e2
			e1.nib.remove(node2); //remove e2 from node list of e1
			e2.nib.remove(node1);//remove e1 from node list of e2
			sume--;
		}
	}

	@Override
	public int nodeSize() {
		return gn.size();
	}

	@Override
	public int edgeSize() {
		return this.sume;
	}

	@Override
	public int getMC() {
		return this.mc;
	}
	public void addNode(node_info n) {
		if(n!=null)gn.put(n.getKey(), n); //Delete
	}

	public void copy(weighted_graph g) {	
		Iterator<node_info> iter=g.getV().iterator(); //using Iterator
		
		while(iter.hasNext()) {		
			node_info Ntemp=iter.next(); //Make a new node Ntemp
			NodeInfo newN=new NodeInfo(Ntemp); //Make a new node newN
			int keyN=newN.getKey();
			if(!gn.containsKey(keyN)) { //if the node not in the new graph
				addNode(newN); //put newN in the new graph
				mc++;
			}			
			Iterator<node_info> iterNi=g.getV(keyN).iterator();	
			
			while(iterNi.hasNext()){
				node_info Nt=iterNi.next(); //Make a new node Nt
				NodeInfo newNi=new NodeInfo(Nt); //Make a new node newNi
				int keyNi=newNi.getKey();
				if(!gn.containsKey(keyNi)) { //if the node not in the new graph		
					addNode(newNi); //put newN in the new graph
				}
				if(!hasEdge(keyN,keyNi)) {
					connect(keyNi,keyNi,getEdge(keyNi,keyN));

				}
				this.mc=g.getMC();
				this.sume=g.edgeSize();
			}
		}		
	}

}