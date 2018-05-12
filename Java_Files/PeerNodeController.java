import java.awt.List;
import java.beans.FeatureDescriptor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Scanner;

public class PeerNodeController {

	static LocateRegistry localRegistry;
	static Registry registry;
	static Random random = new Random();
	public static void messages(String s) {
		System.out.println(s);
	}
	public static int getRandomNumber(){
		return random.nextInt(10);
		
	}
	public static void main(String[] args) throws RemoteException, NotBoundException, UnknownHostException, AlreadyBoundException {
		LocateRegistry localRegistry;
		IBootstrapServer ibootstrapServer = null;
		//IPeerNode iPeerNode = null;
		
		//PeerNodeImpl stub = (PeerNodeImpl)UnicastRemoteObject.exportObject(model, 0);
		NodeModel targetNode = new NodeModel();
		Scanner scan = new Scanner(System.in);
		String task = null ;
		String serverIp = null;
		int serverPort= 2102;
		//String option
		
		String newNodeId ;
		int newNodePort ;
		String newNodeIpAddress;
		while(true) {
			System.out.println("Please select an option");
			System.out.println("Join");
			System.out.println("Insert");
			System.out.println("Search");
			System.out.println("View");
			System.out.println("Leave");
			String choice = scan.next();
			switch (choice) {
			case "Join":
			
				System.out.println("Select 1) for Insert via Bootstrap Server, Select 2) for inserting via any active node)");
				int newChoice = scan.nextInt();
				switch(newChoice){
				case 1:
						System.out.println("Enter Bootstrap_IP Bootstrap_Port PeerName PortNumber");
						serverIp = scan.next();
						serverPort = scan.nextInt();
						 newNodeId = scan.next();
						 newNodePort = scan.nextInt();
						 newNodeIpAddress = InetAddress.getLocalHost().getHostAddress();
						IPeerNode model = new PeerNodeImpl(newNodeIpAddress,newNodePort, newNodeId);
						Registry reg = LocateRegistry.createRegistry(newNodePort);
						reg.bind(newNodeId, model);
						
						Registry myReg = LocateRegistry.getRegistry(serverPort);
						ibootstrapServer = (IBootstrapServer) myReg.lookup("Bootstrap");
						targetNode = ibootstrapServer.fetchAPeer();
							if(targetNode != null) {
							System.out.println("New node will contact  "+targetNode.nodeIdentifier+" with Port "+targetNode.getPortNumber());
							int xCoodinate = getRandomNumber() ;
							int yCoordinate = getRandomNumber();
			
							Registry registery1 =LocateRegistry.getRegistry(targetNode.ipAddress, targetNode.getPortNumber());
							IPeerNode iPeerNode = (IPeerNode) registery1.lookup(targetNode.getNodeIdentifier());
							System.out.println("---"+iPeerNode.getModel().getNodeIdentifietr());
							iPeerNode.joinCAN(xCoodinate, yCoordinate, newNodeIpAddress,newNodeId, newNodePort );
							
							
							} else {
								model.joinCANthroughBootstrap(targetNode);
							}
						ibootstrapServer.addNodetoList(newNodeIpAddress, newNodePort, newNodeId);
						break;
				 
				case  2:
							System.out.println("Enter activeNodeIp ActivenodePort PeerName PortNumber");
							serverIp = scan.next();
							serverPort = scan.nextInt();
							newNodeId = scan.next();
						    newNodePort = scan.nextInt();
							newNodeIpAddress = InetAddress.getLocalHost().getHostAddress();
							IPeerNode model1 = new PeerNodeImpl(newNodeIpAddress,newNodePort, newNodeId);
							Registry reg1 = LocateRegistry.createRegistry(newNodePort);
							reg1.bind(newNodeId, model1);
							
							Registry myReg1 = LocateRegistry.getRegistry(serverPort);
							ibootstrapServer = (IBootstrapServer) myReg1.lookup("Bootstrap");
							targetNode = ibootstrapServer.fetchAPeer();
							if(targetNode != null) {
							System.out.println("New node will contact  "+targetNode.nodeIdentifier+" with Port "+targetNode.getPortNumber());
							int xCoodinate = getRandomNumber() ;
							int yCoordinate = getRandomNumber();
			
							Registry registery1 =LocateRegistry.getRegistry(targetNode.ipAddress, targetNode.getPortNumber());
							IPeerNode iPeerNode = (IPeerNode) registery1.lookup(targetNode.getNodeIdentifier());
							System.out.println("---"+iPeerNode.getModel().getNodeIdentifietr());
							iPeerNode.joinCAN(xCoodinate, yCoordinate, newNodeIpAddress,newNodeId, newNodePort );
							
							
							ibootstrapServer.addNodetoList(newNodeIpAddress, newNodePort, newNodeId);
							
							}
							break;
				}
				
				
				break;
				
			case "Insert":
				
				System.out.println("Please enter a fileName to inster");
				String keyword = scan.next();
				newNodeIpAddress = InetAddress.getLocalHost().getHostAddress();
				Registry myReg1 = LocateRegistry.getRegistry(serverPort);
				ibootstrapServer = (IBootstrapServer) myReg1.lookup("Bootstrap");
				targetNode = ibootstrapServer.fetchAPeer();
				
				int xCoodinate = getRandomNumber() ;
				int yCoordinate = getRandomNumber();

				Registry registery1 =LocateRegistry.getRegistry(targetNode.ipAddress, targetNode.getPortNumber());
				IPeerNode iPeerNode = (IPeerNode) registery1.lookup(targetNode.getNodeIdentifier());
				System.out.println("---"+iPeerNode.getModel().getNodeIdentifietr());
				iPeerNode.insertFile(xCoodinate,yCoordinate, keyword, newNodeIpAddress);
				
				break;

			case "Search":
				System.out.println("Please enter a fileName to search");
				String searchString = scan.next();
				Registry myReg2 = LocateRegistry.getRegistry(serverPort);
				ibootstrapServer = (IBootstrapServer) myReg2.lookup("Bootstrap");
				targetNode = ibootstrapServer.fetchAPeer();

				Registry registery2 =LocateRegistry.getRegistry(targetNode.ipAddress, targetNode.getPortNumber());
				IPeerNode iPeerNode2 = (IPeerNode) registery2.lookup(targetNode.getNodeIdentifier());
				System.out.println("---"+iPeerNode2.getModel().getNodeIdentifietr());
				iPeerNode2.searchFile(searchString);
				break;

			case "View":
				 System.out.println("Select 1) for viewing specific node, 2) for viewing all");
				 int viewChoice = scan.nextInt();
				 Registry myReg3 = LocateRegistry.getRegistry(serverPort);
					ibootstrapServer = (IBootstrapServer) myReg3.lookup("Bootstrap");
				 if(viewChoice==1) {
					 System.out.println("Node you want to view ");
					 String thisPeer = scan.next();
					 targetNode = ibootstrapServer.fetchThisAPeer(thisPeer);
					 
					 Registry registery3 =LocateRegistry.getRegistry(targetNode.ipAddress, targetNode.getPortNumber());
					 IPeerNode iPeerNode3 = (IPeerNode) registery3.lookup(targetNode.getNodeIdentifier());
					 iPeerNode3.viewPeer(thisPeer);
					 
				 }else if(viewChoice==2) {
					 java.util.List<NodeModel> list = ibootstrapServer.fetchAll();
					 for (NodeModel node : list) {
						 Registry registery3 =LocateRegistry.getRegistry(node.ipAddress, node.getPortNumber());
						 IPeerNode iPeerNode3 = (IPeerNode) registery3.lookup(node.getNodeIdentifier());
						 System.out.println("---"+iPeerNode3.getModel().getNodeIdentifietr());
						 iPeerNode3.viewPeer(node.getNodeIdentifier());
					}
				 }
				break;


			case "Leave":
				
				break;

			default:
				break;
			}
			
		}
	}
}

