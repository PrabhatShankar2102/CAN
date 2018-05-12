import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Neighbors implements Serializable, Remote {

	public String ipAddress;
	public String nodeIdentifier;
	public int portNumber;
	NodeZone nodeZone;
	List<Neighbors> neighbors = null;
	public Neighbors(){
		// TODO Auto-generated constructor stub
		ipAddress = null;
		nodeIdentifier = null;
		portNumber = 0;
		neighbors = new ArrayList<>();
		nodeZone = new NodeZone();
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getNodeIdentifier() {
		return nodeIdentifier;
	}
	public void setNodeIdentifier(String nodeIdentifier) {
		this.nodeIdentifier = nodeIdentifier;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	public List<Neighbors> getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(List<Neighbors> neighbors) {
		this.neighbors = neighbors;
	}
	public NodeZone getNodeZone() {
		return nodeZone;
	}
	public void setNodeZone(NodeZone nodeZone) {
		this.nodeZone = nodeZone;
	}
	
	
}
