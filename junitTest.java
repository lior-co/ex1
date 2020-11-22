package ex1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;

import ex1.WGraph_DS.NodeInfo;

public class junitTest {
	
	@Test
	public void isConnected() {
		weighted_graph g0=G0();
		WGraph_Algo WG=new WGraph_Algo();
		WG.init(g0);
		boolean flag=WG.isConnected();
		assertTrue(flag);
	}
	
	
	@Test
	public void shortestPathDist() {
		weighted_graph g0=G0();
		WGraph_Algo WG=new WGraph_Algo();
		WG.init(g0);
		double dist=WG.shortestPathDist(0,4);
		boolean flag=false;
		if(dist==6.4) flag=true;
		assertTrue(flag);
	}
	
	@Test
	public void shortestPath() {
		weighted_graph g0=G0();
		WGraph_Algo WG=new WGraph_Algo();
		WG.init(g0);
        List<node_info> L0=WG.shortestPath(0,4);
        int[] checkKey = {0,2,4};
        int i = 0;
        for(node_info n: L0) {
        	assertEquals(n.getKey(), checkKey[i]);
        	i++;
        }
	}
	
	@Test
    public void copy_equals() {
		weighted_graph g0=G0();
		WGraph_Algo WG0=new WGraph_Algo();
		WG0.init(g0);
		
		weighted_graph g0_copy=WG0.copy();
		WGraph_Algo WG0_copy=new WGraph_Algo();
		WG0_copy.init(g0_copy);
		
		boolean flag=WG0.equals(WG0); //equals test
		assertTrue(flag);
    	assertEquals(g0.edgeSize(),g0_copy.edgeSize()); //edgeSize copy test
    	assertEquals(g0.nodeSize(),g0_copy.nodeSize()); //edgeSize copy test
    }

	@Test
    public void save_load() {
		weighted_graph g0=G0();
		WGraph_Algo WG0=new WGraph_Algo();
		WG0.init(g0);
		
        weighted_graph g1=new WGraph_DS();
		WGraph_Algo WG1=new WGraph_Algo();
        WG1.init(g1);
        
        boolean flagSave=WG0.save("test.txt"); 		
        boolean flagLode=WG1.load("test.txt");
		assertTrue(flagSave);
		assertTrue(flagLode);
		
    	assertEquals(g0.edgeSize(),g1.edgeSize()); //edgeSize copy test
    	assertEquals(g0.nodeSize(),g1.nodeSize()); //edgeSize copy test
    }
	
	@Test
	public void removeEdge() {
		weighted_graph g0=G0();
		WGraph_Algo WG0=new WGraph_Algo();
		WG0.init(g0);
		g0.removeEdge(5,3);
		assertTrue(!g0.hasEdge(5,3));
	}
	
	@Test
	public void MillionNodes() {
		weighted_graph g0=randGraph(1000000);
		WGraph_Algo WG0=new WGraph_Algo();
		WG0.init(g0);
		 boolean flag=WG0.isConnected();
		 assertTrue(flag);
	}
	
	
	public static WGraph_DS randGraph(int nodes) {
		WGraph_DS WG=new WGraph_DS();
		int n=0;
		WG.addNode(n++);
		while(n==nodes) {
			WG.addNode(n++);
			double w=(int)(Math.random()*10+1);
			WG.connect(n-1,n,w);
		}
		return WG;
	}
	
	public static WGraph_DS G0() {
		WGraph_DS G=new WGraph_DS(); //new WGraph
		
		NodeInfo n0=new NodeInfo(); //new node n0
		NodeInfo n1=new NodeInfo(); //new node n1
		NodeInfo n2=new NodeInfo(); //new node n2
		NodeInfo n3=new NodeInfo(); //new node n3
		NodeInfo n4=new NodeInfo(); //new node n4
		NodeInfo n5=new NodeInfo(); //new node n5
		
		G.addNode(n0); //add node n0 to G
		G.addNode(n1); //add node n1 to G
		G.addNode(n2); //add node n3 to G
		G.addNode(n3); //add node n3 to G
		G.addNode(n4); //add node n4 to G
		G.addNode(n5); //add node n5 to G
		
		G.connect(n0.getKey(),n1.getKey(),2.2); //connect n0 to n1 with edge of 2.2
		G.connect(n0.getKey(),n2.getKey(),5.3); //connect n0 to n2 with edge of 5.3
		G.connect(n1.getKey(),n3.getKey(),7); //connect n1 to n3 with edge of 7
		G.connect(n1.getKey(),n4.getKey(),10); //connect n1 to n4 with edge of 10
		G.connect(n2.getKey(),n4.getKey(),1.1); //connect n2 to n4 with edge of 1.1
		G.connect(n3.getKey(),n5.getKey(),1.2); //connect n3 to n5 with edge of 1.2
		G.connect(n4.getKey(),n5.getKey(),6.5); //connect n4 to n5 with edge of 6.5
		
		return G; //nodeSize=6,edgeSize=7
	}


}
