import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NodeZone implements Serializable, Remote {

	//Four points of the Quadrilateral (Square or Rectangle)
	public float x1;
	public float x2;
	public float y1;
	public float y2;
	
	public NodeZone() {
		x1=x2=y1=y2= -1;
		
	}

	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}
	public float getY1() {
		return y1;
	}

	public void setY1(float y1) {
		this.y1 = y1;
	}

	public float getY2() {
		return y2;
	}

	public void setY2(float y2) {
		this.y2 = y2;
	}
	
}
