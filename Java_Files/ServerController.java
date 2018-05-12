import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ServerController {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException, UnknownHostException, NotBoundException {
		
		try {
			System.out.println("IP of Bootsrap: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try{			
			Registry reg = LocateRegistry.createRegistry(2102);
			IBootstrapServer boot = new BootstrapServerImpl();
			reg.bind("Bootstrap", boot);
			System.out.println("Bootstrap Server running on port 2102");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	
	}
}
