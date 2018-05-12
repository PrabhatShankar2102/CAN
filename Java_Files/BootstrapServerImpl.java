import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.stream.events.NotationDeclaration;

public class BootstrapServerImpl extends UnicastRemoteObject  implements IBootstrapServer, Serializable {

	List<NodeModel> nodeList ;
	public static final int maxNodes = 10;
	Random random ;
	protected BootstrapServerImpl() throws RemoteException {
		super();
		nodeList = new ArrayList<NodeModel>();
		random = new Random();
		// TODO Auto-generated constructor stub
	}

	
	public List<NodeModel> getNodeList() {
		return nodeList;
	}


	public void setNodeList(List<NodeModel> nodeList) {
		this.nodeList = nodeList;
	}


	public Random getRandom() {
		return random;
	}


	public void setRandom(Random random) {
		this.random = random;
	}


	public static int getMaxnodes() {
		return maxNodes;
	}


	@Override
	public void testMsg() throws RemoteException {
		System.out.println("YOOOOO HOOO");
	}

	@Override
	public void addNodetoList(String clientIp , int portNumber, String clientIdentifier) throws RemoteException {
		//Create a Peer Node object 
		NodeModel peer = new NodeModel();
		peer.setIpAddress(clientIp);
		peer.setPortNumber(portNumber);
		peer.setNodeIdentifier(clientIdentifier);
		nodeList.add(peer);
		System.out.println("Current Size of NodeList is "+nodeList.size());
	}

	
	@Override
	public NodeModel fetchAPeer() throws RemoteException {
		
		 int index = 0;
		System.out.println(index);
		 if(nodeList.size()==0) {
			return null;
		} else if(nodeList.size()>10){
			System.out.println("Max nodes to be added reached");
			index= 9;
			return nodeList.get(9);
			
		}
		//Random index to select a random node
		 index =	random.nextInt(nodeList.size());
		return nodeList.get(index);
	}

	@Override
	public void removeNodefromCAN(String peerIdentifier) throws RemoteException {
		for(int i =0;i<nodeList.size();i++) {
			if(nodeList.get(i).nodeIdentifier==peerIdentifier) {
				nodeList.remove(i);
			}
		}
		
	}


	@Override
	public NodeModel fetchThisAPeer(String peerName) throws RemoteException {
		System.out.println("Fetching node "+peerName);
		for(int i =0;i<nodeList.size();i++) {
			if(nodeList.get(i).nodeIdentifier.equals(peerName)) {
			System.out.println("Found node "+nodeList.get(i));
			 return nodeList.get(i);	
			}
		}
		System.out.println("I should not be here, Null will be returned");
		return null;
		}


	@Override
	public List<NodeModel> fetchAll() throws RemoteException {
		return nodeList;
	}
		
	}

