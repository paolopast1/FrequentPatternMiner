package progServer.mining;
import java.rmi.*;

/**
 * Interfaccia remota che estende l' interfaccia Remote
 * @author Paolo Pastore 652412
 */
public interface CPAInterface extends Remote{
	/**
	 * metodo che ha lo stesso comportamento del toString
	 * @return oggetto di tipo String
	 * @throws RemoteException
	 */
	public String convertToString() throws RemoteException;
}
