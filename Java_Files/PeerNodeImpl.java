import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class PeerNodeImpl extends UnicastRemoteObject implements IPeerNode, Serializable {

	PeerNodeModel model ;
	Random random;
	NodeZone nodeZone;
	Neighbors neighbors ;
	int newPort;
	String newIp ;
	public PeerNodeImpl(String ip, int port, String nodeId) throws RemoteException{
		model = new PeerNodeModel();
		model.setIpAddress(ip);
		model.setPortNumber(port);
		model.setNodeIdentifietr(nodeId);
		random = new Random();
		nodeZone = new NodeZone();
		neighbors = new Neighbors();
		
	}
	public PeerNodeImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
		random = new Random();
		model = new PeerNodeModel();
		nodeZone = new NodeZone();
		neighbors = new Neighbors();
	}
	
	
	
	public NodeZone getNodeZone() {
		return nodeZone;
	}
	public void setNodeZone(NodeZone nodeZone) {
		this.nodeZone = nodeZone;
	}
	public Neighbors getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(Neighbors neighbors) {
		this.neighbors = neighbors;
	}
	public int getNewPort() {
		return newPort;
	}
	public void setNewPort(int newPort) {
		this.newPort = newPort;
	}
	public String getNewIp() {
		return newIp;
	}
	public void setNewIp(String newIp) {
		this.newIp = newIp;
	}
	public PeerNodeModel getModel() {
		return model;
	}
	public void setModel(PeerNodeModel model) {
		this.model = model;
	}
	public Random getRandom() {
		return random;
	}
	public void setRandom(Random random) {
		this.random = random;
	}
	
	
	@Override
	public void joinCANthroughBootstrap(NodeModel model) throws RemoteException, NotBoundException {
		if(model==null) {
			bootstrapSpace();
			System.out.println("First Node in the N/W");
			 displayZonePoints() ;
			
		} else {
			System.out.println(model.ipAddress+" --  "+model.portNumber+" --- "+model.getNodeIdentifier() );
			
			
			
		}
	}
	@Override
	public void joinCAN(int x, int y, String clientIp, String peerName, int peerPort) throws RemoteException, NotBoundException {
		System.out.println("Incomign as Argument "+peerName+" Port is "+peerPort);
		System.out.println("This pointer  "+this.model.getNodeIdentifietr()+" Port is :"+this.model.getPortNumber());
		System.out.println(this.getZone().getX1());
		int xCoodinate = x;
		int yCoordinate = y;
		System.out.println(xCoodinate);
		System.out.println(yCoordinate);
		Registry registery = null;
		//NodeZone nodeZone;
		IPeerNode iPeerNode = null;
		boolean isSameZone = sameZone(xCoodinate,yCoordinate, this.model.getNodeZone());
		if(isSameZone) {
			registery = LocateRegistry.getRegistry(clientIp, peerPort);
			iPeerNode = (IPeerNode) registery.lookup(peerName);
			this.splitZone(xCoodinate,yCoordinate, iPeerNode);
			this.updateNeignborList(iPeerNode); // To-DO
			//this.displayZonePoints();
			System.out.println("-----New------");
			this.viewPeer(this.model.nodeIdentifietr); // To-Do
			
		} else {
			//check in the neighbor
			System.out.println("Checking Neighbor");
			Neighbors neighborNode = this.checkMyNeighbours(xCoodinate,yCoordinate);
			if(neighborNode!=null) {
				// we have found the node 
				registery = LocateRegistry.getRegistry(neighborNode.getIpAddress(), neighborNode.getPortNumber());
				iPeerNode = (IPeerNode) registery.lookup(neighborNode.getNodeIdentifier());
				//Call Join 
				iPeerNode.joinCAN(x, y, clientIp, peerName, peerPort);
			} else { 
				
				// Find the route
				 neighborNode = this.findNeighbor(xCoodinate, yCoordinate); // To Do
				
				 	registery = LocateRegistry.getRegistry(neighborNode.getIpAddress(), neighborNode.getPortNumber());
					iPeerNode = (IPeerNode) registery.lookup(neighborNode.getNodeIdentifier());
					//Call Join 
					iPeerNode.joinCAN(x, y ,clientIp, peerName, peerPort);
			}
		}
	}
	
	public Neighbors checkMyNeighbours(int x, int y){
		List<Neighbors> nodes = this.model.neighbourNodes;
		for(Neighbors node : nodes) {
		if((x>=node.getNodeZone().x1) && (x<=node.getNodeZone().getX2()) && (y>=node.getNodeZone().getY1()) && (y<=node.getNodeZone().getY2())) {
			System.out.println("Current Node is :"+node.nodeIdentifier);
			return node;
			}
		}
		return null;
	}
	@Override
	public Neighbors findNeighbor(int x, int y) throws RemoteException, NotBoundException {
		
		System.out.println("Started Routing..... ");
		Neighbors target = new Neighbors();
		NodeZone zone = this.nodeZone;
		//IPeerNode iPeerNode = null;
		float nodeMidX = (zone.getX1() + zone.getX2())/2;
		float nodeMidY = (zone.getY1()+zone.getY2())/2;
		IPeerNode iPeerNode = null;
		double initialDistace = Math.sqrt(Math.pow((x-nodeMidX),2) + Math.pow((y-nodeMidY),2));
		for (Neighbors neighbor : this.model.getNeighbourNodes()) {
			float neighborMidX = (neighbor.getNodeZone().getX1() + neighbor.getNodeZone().getX2())/2;
			float neighborMidY = (neighbor.getNodeZone().getY1()+neighbor.getNodeZone().getY2())/2;
			double minDistance = Math.sqrt(Math.pow((x-neighborMidX),2) + Math.pow((y-neighborMidY),2));
			if(minDistance < initialDistace) {
				initialDistace = minDistance;
				target.ipAddress = neighbor.ipAddress;
				target.neighbors = neighbor.neighbors;
				target.nodeIdentifier = neighbor.nodeIdentifier;
				target.nodeZone = neighbor.nodeZone;
				target.portNumber = neighbor.portNumber;
			}
		}
		if(!sameZone(x,y, target.getNodeZone())) {
			
		Registry registry = LocateRegistry.getRegistry(target.getIpAddress(), target.getPortNumber());
		iPeerNode = (IPeerNode) registry.lookup(target.getNodeIdentifier());
		iPeerNode.findNeighbor(x, y);
		}
		System.out.println("Node is:  "+ target.getNodeIdentifier());
		return target;
	}
	


	//Displays Node Info
	@Override
	public void viewPeer(String peerName) throws RemoteException {
		
			/*
			 * 	Display the information of a specified peer peer where peer is a node identifier, not an IP address. 
			 * The information includes the node identifier, the IP address, the coordinate, a list of neighbors, and the data items currently stored at the peer
			 */
			System.out.println("Node Identifier: "+this.model.getNodeIdentifietr());
			System.out.println("Node IPAddress "+this.model.getIpAddress());
			System.out.println("Node Coordinate :" +this.nodeZone);
			displayZonePoints();
			System.out.println("Neighbors List"+this.model.getNeighbourNodes() );
			displayNeignbors();
			displayFiles();
		}

	
	@Override
	public void insertFile(int xCo , int yCo, String keyword, String ipAddress) throws RemoteException, NotBoundException{
		
		int x = CharAtOdd(keyword);
		int y = CharAtEven(keyword);
		System.out.println("X ="+x);
		System.out.println("X ="+x);
		Registry registery = null;
		IPeerNode iPeerNode = null;
		//check if Same zone
		boolean isSameZone = sameZone(x,y, this.model.getNodeZone());
		if(isSameZone) {
			this.model.map.put(keyword, ipAddress);
		} else {
			//check in the neighbor
			System.out.println("Checking Neighbor");
			Neighbors targetNode = this.checkMyNeighbours(x,y);
			if(targetNode!=null) {
				// we have found the node 
				registery = LocateRegistry.getRegistry(targetNode.getIpAddress(), targetNode.getPortNumber());
				iPeerNode = (IPeerNode) registery.lookup(targetNode.getNodeIdentifier());
				//Call Join 
				iPeerNode.insertFile(xCo, yCo, keyword, ipAddress);
			} else { // Find the route
				 targetNode = this.findNeighbor(x, y);
				
				 	registery = LocateRegistry.getRegistry(targetNode.getIpAddress(), targetNode.getPortNumber());
					iPeerNode = (IPeerNode) registery.lookup(targetNode.getNodeIdentifier());
					//Call Join 
					iPeerNode.insertFile(xCo, yCo,keyword, ipAddress);
			}
		}
		System.out.println("File Succesfully Inserted on node :"+this.model.getNodeIdentifietr());
	}
	

	public int CharAtOdd(String keyword) {
		int size= keyword.length();
		int x=0;
		if(size%2==0) {
			for(int i=1;i<size;i=i+2) {
				x+=(int)keyword.charAt(i);
			}
		}else {
			for(int i=1;i<size-1;i=i+2) {
				x+=(int)keyword.charAt(i);
			}
		}
		return (x%10);
	}

	
	public int CharAtEven(String keyword) {
		int size= keyword.length();
		int y=0;
		if(size%2==0) {
			for(int i=0;i<size-1;i=i+2) {
				y+=(int)keyword.charAt(i);
			}
		}else {
			for(int i=0;i<size;i=i+2) {
				y+=(int)keyword.charAt(i);
			}
		}
		return y%10;
	}

	
	@Override
	public void searchFile(String keyword) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
		int x = CharAtOdd(keyword);
		int y = CharAtEven(keyword);
		Registry registery = null;
		IPeerNode iPeerNode = null;
		//check if Same zone
		boolean isSameZone = sameZone(x,y, this.model.getNodeZone());
		if(isSameZone) {
			if(this.model.map.containsKey(keyword)) {
			System.out.println("Peer : "+this.model.nodeIdentifietr+" Holds file : "+keyword);
			} else {
				System.out.println("Failure");
			}
				
		} else {
			//check in the neighbor
			System.out.println("Checking Neighbor");
			Neighbors neighborNode = this.checkMyNeighbours(x,y);
			if(neighborNode!=null) {
				// we have found the node 
				registery = LocateRegistry.getRegistry(neighborNode.getIpAddress(), neighborNode.getPortNumber());
				iPeerNode = (IPeerNode) registery.lookup(neighborNode.getNodeIdentifier());
				//Call Join 
				if(iPeerNode.getModel().map.containsKey(keyword)) {
					System.out.println("Peer "+iPeerNode.getModel().nodeIdentifietr+"Holds file "+keyword);
					} else {
						System.out.println("Failure");
					}
				
			} else { // Find the route
				 neighborNode = this.findNeighbor(x, y); // To Do
				
				 	registery = LocateRegistry.getRegistry(neighborNode.getIpAddress(), neighborNode.getPortNumber());
					iPeerNode = (IPeerNode) registery.lookup(neighborNode.getNodeIdentifier());
					//Call Join 
					if(iPeerNode.getModel().map.containsKey(keyword)) {
						System.out.println("Peer "+iPeerNode.getModel().nodeIdentifietr+"Holds file "+keyword);
						} else {
							System.out.println("Failure");
						}
			}
		}
		
		
	}
	

	

	public void displayDetails(PeerNodeImpl node) {
		
	}
	public void bootstrapSpace() {
		this.model.getNodeZone().x1 = this.model.getNodeZone().y1 =0; //p(x1,y1)
		this.model.getNodeZone().x2 = this.model.getNodeZone().y2 =10; //q(x2,y1)
		
	}
	public int getRandomNumber(){
		return random.nextInt(10);
		
	}
	//Check if point is in Calling peer's zone
	public boolean sameZone(int x, int y, NodeZone zone) {
		if((x>=zone.getX1()) && (x<=zone.getX2()) && (y>=zone.getY1()) && (y<=zone.getY2())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void splitZone(int x, int y, IPeerNode iPeerNode) throws RemoteException {
		NodeZone nodeZone = iPeerNode.getZone();
		//Check for Square, height = breadth
		if((Math.abs(this.model.getNodeZone().x1-this.model.getNodeZone().x2)) == (Math.abs(this.model.getNodeZone().y1-this.model.getNodeZone().y2))) {
			//This split will generate rectangles, Divide Vertically
			nodeZone.x1 = (this.model.getNodeZone().x1+this.model.getNodeZone().x2)/2;
			nodeZone.x2=   this.model.getNodeZone().x2;
			nodeZone.y1=this.model.getNodeZone().y1;
			nodeZone.y2=this.model.getNodeZone().y2;
			this.model.getNodeZone().setX2((this.model.getNodeZone().x1+this.model.getNodeZone().x2)/2);
				
				
			
		} else { 
			// Rectangle case, If the zone is a rectangle with height greater than width, split the zone horizontally
				if((this.model.getNodeZone().y2 - this.model.getNodeZone().y1)>(this.model.getNodeZone().x2-this.model.getNodeZone().x1)) {
					
					nodeZone.y1 = (this.model.getNodeZone().y1+this.model.getNodeZone().y2)/2;
					nodeZone.y2=   this.model.getNodeZone().x2;
					nodeZone.x1=this.model.getNodeZone().x1;
					nodeZone.x2=this.model.getNodeZone().x2;
					
					this.model.getNodeZone().setY2((this.model.getNodeZone().y1+this.model.getNodeZone().y2)/2);
				} else {// TODO Auto-generated method stub
					
					nodeZone.x1 = (this.model.getNodeZone().x1+this.model.getNodeZone().x2)/2;
					nodeZone.x2=   this.model.getNodeZone().x2;
					nodeZone.y1=this.model.getNodeZone().y1;
					nodeZone.y2=this.model.getNodeZone().y2;
					this.model.getNodeZone().setX2((this.model.getNodeZone().x1+this.model.getNodeZone().x2)/2);
				}
		
		}
		iPeerNode.setZone(nodeZone);
	}
	
	public void updateNeignborList(IPeerNode iPeerNode) throws RemoteException, NotBoundException{
		//Add new adjacent nodes to each other's neighbor list
		Neighbors newNode = new Neighbors();
		Neighbors oldNode = new Neighbors();
		
		newNode.ipAddress = iPeerNode.getModel().ipAddress;
		newNode.nodeIdentifier = iPeerNode.getModel().nodeIdentifietr;
		newNode.portNumber = iPeerNode.getModel().portNumber;
		newNode.nodeZone = iPeerNode.getZone();
		
		oldNode.ipAddress = this.model.ipAddress;
		oldNode.nodeIdentifier = this.model.nodeIdentifietr;
		oldNode.portNumber = this.model.portNumber;
		oldNode.nodeZone = this.model.nodeZone;
 
		
		// Now add other neighbors in new node
		//Create a list of current neighbor
		if(!(this.model.neighbourNodes.isEmpty())){
		List<Neighbors> currentNeighbors = new ArrayList<Neighbors>();
		for (Neighbors neighbor : this.model.neighbourNodes) {
			currentNeighbors.add(neighbor);
		}
		
		//Now empty the Neighbor list from current(old) Node 
		 this.model.neighbourNodes.clear();
		//Both new and old node has empty neighbor list, iterate over list of old neighbors and push in respective node's neighbors
		 for (Neighbors neighbor : currentNeighbors) {
			 //Check for Old
			
			if(isNeighbor(neighbor, oldNode)) {
				this.model.neighbourNodes.add(neighbor);
			} else {
				//Remove it's entry from neighbor's node
				Registry registry = LocateRegistry.getRegistry(neighbor.getIpAddress(), neighbor.getPortNumber());
				IPeerNode remoteObj  = (IPeerNode) registry.lookup(neighbor.getNodeIdentifier());
				remoteObj.deleteNodeEntry(this.model.nodeIdentifietr);
			}
			if(isNeighbor(neighbor, newNode)) {
				//Add as new node's neighbor
				this.model.neighbourNodes.add(neighbor);
				//Add new node to as neighbpor's neighbor
				Registry registry = LocateRegistry.getRegistry(neighbor.getIpAddress(), neighbor.getPortNumber());
				IPeerNode remoteObj  = (IPeerNode) registry.lookup(neighbor.getNodeIdentifier());
				remoteObj.getModel().neighbourNodes.add(newNode);
			}
			
		 }
		 
	}
		
		this.model.neighbourNodes.add(newNode);
		iPeerNode.getModel().neighbourNodes.add(oldNode);
		//Update HashMap
		updateHashtable(iPeerNode);
	}
		
		public void deleteNodeEntry(String peerName) {
			for(int i = 0; i< this.model.neighbourNodes.size(); i++) {
				
				if(peerName.equals(this.model.neighbourNodes.get(i).getNodeIdentifier())) {
					this.model.neighbourNodes.remove(i);
					return;
				}
			}
		}
		public boolean isNeighbor(Neighbors node1, Neighbors node2) {
			
			/*//Common X-Axix
			if((node1.getNodeZone().y1 == node2.getNodeZone().y1) || node1.getNodeZone().y1==node2.getNodeZone().y2) {
				if((node1.getNodeZone().x1 == node2.getNodeZone().x1) || (node1.getNodeZone().x2 == node2.getNodeZone().x2)) {
					return true;
				}
				
			}
			//Common y-axis
			if((node1.getNodeZone().x1 == node2.getNodeZone().x1) || node1.getNodeZone().x2==node2.getNodeZone().x2) {
				if((node1.getNodeZone().y1 == node2.getNodeZone().y1) || (node1.getNodeZone().y2 == node2.getNodeZone().y2)) {
					return true;
				}
				
				
				
			}*/
			
			/*if((node1.getNodeZone().y1 == node2.getNodeZone().y1) || node1.getNodeZone().y2==node2.getNodeZone().y2 || 
					(node1.getNodeZone().y1 == node2.getNodeZone().y2) || node1.getNodeZone().y2==node2.getNodeZone().y1 ) {
					
				if(node1.getNodeZone().x1< node2.getNodeZone().x1) {
					if(node1.getNodeZone().x2>node2.getNodeZone().x1) {
						return true ;
					}
				}
				else if(node1.getNodeZone().x1> node2.getNodeZone().x1) {
					if(node1.getNodeZone().x2<node2.getNodeZone().x1) {
						return true ;
					}
				} else {
					return true;
				}
				
			}
			
			
			if((node1.getNodeZone().x1 == node2.getNodeZone().x1) || node1.getNodeZone().x2==node2.getNodeZone().x2 || 
					(node1.getNodeZone().x1 == node2.getNodeZone().x2) || node1.getNodeZone().x2==node2.getNodeZone().x1 ) {
					
				if(node1.getNodeZone().y1< node2.getNodeZone().y1) {
					if(node1.getNodeZone().y2>node2.getNodeZone().y1) {
						return true ;
					}
				}
				else if(node1.getNodeZone().y1> node2.getNodeZone().y1) {
					if(node1.getNodeZone().y2<node2.getNodeZone().y1) {
						return true ;
					}
				} else {
					return true;
				}
				
			}*/
			
	if(lookXAxis(node1.getNodeZone().getX1(), node1.getNodeZone().getY2(), node1.getNodeZone().getX2(), node1.getNodeZone().getY2(),node2.getNodeZone().getX1(), node2.getNodeZone().getY1(), node2.getNodeZone().getX2(), node2.getNodeZone().getY1()) 
				|| lookXAxis(node1.getNodeZone().getX1(), node1.getNodeZone().getY1(), node1.getNodeZone().getX2(), node1.getNodeZone().getY1(),node2.getNodeZone().getX1(), node2.getNodeZone().getY2(), node2.getNodeZone().getX2(), node2.getNodeZone().getY2()) 
				||	lookYAxis(node1.getNodeZone().getX2(), node1.getNodeZone().getY1(), node1.getNodeZone().getX2(), node1.getNodeZone().getY2(),node2.getNodeZone().getX1(), node2.getNodeZone().getY1(), node2.getNodeZone().getX1(), node2.getNodeZone().getY2())	
				||	lookYAxis(node1.getNodeZone().getX1(), node1.getNodeZone().getY1(), node1.getNodeZone().getX1(), node1.getNodeZone().getY2(),node2.getNodeZone().getX2(), node2.getNodeZone().getY1(), node2.getNodeZone().getX2(), node2.getNodeZone().getY2())){
				return true;
			}
			return false;
			
		}

		public boolean lookYAxis(float node1x1, float node1y2, float node1x2, float node1y1, float node2x1, float node2y1, float node2x2, float node2y2) {
			if(node1x1 == node2x1){
				if((node2y1 >= node1y2) || (node2y2 <= node1y2)) {
					return false;
				}
				return true;
			}else {
			return false;
			}
		}
		
		public boolean lookXAxis(float node1x1, float node1y1, float node1x2, float node1y2, float node2x1, float node2y1, float node2x2, float node2y2) {
			if(node1y1 == node2y1){
				if((node1x1 >= node2x2) || (node1x2 <= node2x1)) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		}
		
		
	
	public void updateHashtable(IPeerNode iPeerNode) throws RemoteException {

		//Iterate over hashmap
		for(Map.Entry<String, String> mapEntry: this.model.map.entrySet()) {
			String key = mapEntry.getKey();
			String value = mapEntry.getValue();
			int x = CharAtOdd(key);
			int y = CharAtEven(key);
			boolean isSameZone = sameZone(x,y, iPeerNode.getZone());
			if(isSameZone) {
				iPeerNode.getModel().map.put(key, value);
				this.model.map.remove(key);			}
		}
	}

	public void displayZonePoints() {
		System.out.println("Zone Co-Ordinates are : ");
		System.out.println("p("+this.model.getNodeZone().x1+","+this.model.getNodeZone().y1+")");
		System.out.println("q("+this.model.getNodeZone().x2+","+this.model.getNodeZone().y1+")");
		System.out.println("r("+this.model.getNodeZone().x2+","+this.model.getNodeZone().y2+")");
		System.out.println("s("+this.model.getNodeZone().x1+","+this.model.getNodeZone().y2+")");
	}
	public void displayNeignbors() {
		System.out.println("Neighbors are: ");
		List<Neighbors> nodes = this.model.getNeighbourNodes();
		for(Neighbors node : nodes) {
			System.out.println(node.nodeIdentifier);
			}
	}
	
	private void displayFiles() {
		System.out.println("Files in node "+this.model.nodeIdentifietr);
		Map<String, String> map = this.model.map;
		for(String key: map.keySet()) {
			System.out.println(map.get(key));
		}
	}
	@Override
	public NodeZone getZone() throws RemoteException {
		// TODO Auto-generated method stub
		return this.nodeZone;
	}

	@Override
	public void setZone(NodeZone zone) throws RemoteException {
		this.nodeZone = zone;
		
	}
}

	

