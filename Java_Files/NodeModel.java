import java.io.Serializable;

public class NodeModel implements Serializable {
	public String ipAddress;
	public String nodeIdentifier;
	public int portNumber;
	
	public NodeModel() {
		ipAddress = null;
		nodeIdentifier = null;
		portNumber = -1;
		// TODO Auto-generated constructor stub
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
}
