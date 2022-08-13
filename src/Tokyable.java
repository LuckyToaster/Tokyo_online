import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Tokyable extends Remote {
	
	public int getLives() throws RemoteException;
	public int throwDice() throws RemoteException;
	public int getPrevious() throws RemoteException;
	public String getAsciiArt() throws RemoteException;

}
