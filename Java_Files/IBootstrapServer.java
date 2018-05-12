import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.BoundedRangeModel;

public interface IBootstrapServer extends Remote {
	public void testMsg() throws RemoteException;
	public void addNodetoList(String clientIp , int portNumber, String peerIdentifier) throws RemoteException;
	public NodeModel fetchAPeer() throws RemoteException;
	public NodeModel fetchThisAPeer(String peerName) throws RemoteException;
	public void removeNodefromCAN(String peerIdentifier) throws RemoteException;
	public List<NodeModel> fetchAll() throws RemoteException;
	
}
