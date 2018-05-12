import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class PeerNodeModel implements Serializable,  Remote {
 
    public String ipAddress;
	//public String key;
    public String nodeIdentifietr ;
	public Map <String, String> map;
	public List<Neighbors> neighbourNodes ;
	public NodeZone nodeZone;
	private float maxX ;
	private float maxY ;
	public int portNumber ;
	public PeerNodeModel() {

		ipAddress = null ;
		nodeIdentifietr= null;
		map = new HashMap<String, String>();
		neighbourNodes = new ArrayList<>();
		nodeZone = new NodeZone();
		
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getNodeIdentifietr() {
		return nodeIdentifietr;
	}
	public void setNodeIdentifietr(String nodeIdentifietr) {
		this.nodeIdentifietr = nodeIdentifietr;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public List<Neighbors> getNeighbourNodes() {
		return neighbourNodes;
	}
	public void setNeighbourNodes(List<Neighbors> neighbourNodes) {
		this.neighbourNodes = neighbourNodes;
	}
	public NodeZone getNodeZone() {
		return nodeZone;
	}
	public void setNodeZone(NodeZone nodeZone) {
		this.nodeZone = nodeZone;
	}
	public float getMaxX() {
		return maxX;
	}
	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}
	public float getMaxY() {
		return maxY;
	}
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	
}