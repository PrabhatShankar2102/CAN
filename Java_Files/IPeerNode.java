import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPeerNode extends Remote {

	public void joinCANthroughBootstrap (NodeModel model) throws RemoteException, NotBoundException;
	public void joinCAN (int xCoodinate, int yCoordinate, String clientIp, String peerName, int peerPort) throws RemoteException, NotBoundException;
	public void viewPeer (String peerName) throws RemoteException;
	public NodeZone getZone() throws RemoteException;
	public void setZone(NodeZone zone) throws RemoteException;
	public void insertFile (int xCoodinate, int yCoordinate,String keyword, String ipAddress) throws RemoteException, NotBoundException;
	public void searchFile (String keyword) throws RemoteException, NotBoundException;
	public PeerNodeModel getModel() throws RemoteException;
	public void deleteNodeEntry(String peerName) throws RemoteException;
	public Neighbors findNeighbor(int x, int y) throws RemoteException, NotBoundException;
	/*
	 *
	public void viewAll() throws RemoteException;
	public void searchFile (String keyword);
	
	
	public void leaveCAN(String peerName);*/
}
