package progServer.main;
import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Classe che estende Thread che contiene il main della parte Server
 * Il server può accettare contemporaneamente più richieste di client diversi
 * @author Paolo Pastore 652412
 *
 */ 
public class MultiServer extends Thread{
	
	/**
	 * porta del localhost sul quale avverrà la comunicazione
	 */
	public static final int PORT = 8080;
	
	/**
	 *Costruttore di classe. Avvia il thread (start()) e stampa sulla console il messaggio “Server Avviato”.
	 */
	public MultiServer(){
		
		System.out.println("Server avviato! ");
		start();
	}
	
	/**
	 * Metodo che avvia una socket che si mette in attesa di nuove connessioni. Ad ogni
	 * nuova connessione crea un oggetto ServerOneClient e lo associa alla nuova connessione
	 * Il metodo run() verrà chiamato implicitamente dal metodo start()
	 */
	
	public void run()
	{
		ServerSocket s = null;
		try{
			s = new ServerSocket(PORT);
			while(true)
			{
				Socket socket = s.accept();
				new ServerOneClient(socket);
			}
		}catch(IOException e){
			System.err.println("IOExcepion!");
			e.printStackTrace();
		}
		try {
			s.close();
		} catch (IOException e) {
			System.err.println("Errore nella chiuura del socket");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Main del server che si occupa di attivare un registro sulla porta 2005 e di istanziare un oggeto di classe MultiServer 
	 * @param args
	 */
	public static void main(String[] args){
		try {
			LocateRegistry.createRegistry(2005);
			new MultiServer();

		} catch (RemoteException e) {
			System.out.print("Errore nella allocazione del registro\n");
			e.printStackTrace();
		}
	}
}
