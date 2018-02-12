package progServer.main;


import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.sql.*;
import java.util.*;

import progServer.data.Data;
import progServer.database.DatabaseConnectionException;
import progServer.database.NoValueException;
import progServer.mining.*;

/**
 * Classe che estende la classe Thread e si occupa di soddisfare le richieste di uno specifico client
 * @author Paolo
 *
 */
public class ServerOneClient extends Thread {
	/**
	 * Porta per il canale di connessione con il client
	 */
	private Socket socket;
	/**
	 * riferimento all' archivio di closed pattern correntemente scoperti 
	 */
	private ClosedPatternArchive cp; 
	/**
	 * Attributo per la gestione dello stream di input
	 */
	private BufferedReader in;
	/**
	 * Attributo per la gestione dello stream di output
	 */
	private PrintWriter out;

	
	/**
	 * Costruttore di classe. Collega la socket passata, crea gli stream di input e output sulla socket, quindi avvia il thread (start()).
	 * @param s oggetto di tipo Socket
	 * @throws IOException
	 */
	public ServerOneClient(Socket s) throws IOException{
		//Istanziazione dell'attributo socket
		socket = s;
		//Creazione dello stream di input
		in = new BufferedReader( new InputStreamReader(
				s.getInputStream()));
		//Creazione dello stream di output
		out = new PrintWriter(
			  new BufferedWriter(
		      new OutputStreamWriter(
		      socket.getOutputStream())), true);
		//Avvio del nuovo thread
		start();
	} 
	
	/**
	 * Metodo che viene avviato implicitamente al momento dell'esecuzione del metodo start()
	 * Si occupa di calcolare (dipendentemente dalla scelta effettuata dall'utente sul client) i pattern chiusi o di caricare un file.
	 * Una volta ottenute tutte le informazioni si effettua il bind fra l'oggetto che contiene queste informazioni e una stringa identificativa 
	 */
	public void run(){
		
		try{
			char choice = '0';
			choice = (char)in.read();
			switch(choice)
			{
			case 'd':
				String nomeTabella = "";
				float minSup = 0;
				float epsilon = 0;
				LinkedList<FrequentPattern> outFP = null;
				// Lettura del nome della tabella
				nomeTabella = in.readLine();
				nomeTabella = in.readLine();
				//Lettura del minSup
				minSup = (Float.parseFloat(in.readLine()));
				//Lettura della epsilon
				epsilon = Float.parseFloat(in.readLine());
				Data tabella;
				//Costruzione della tabella
				tabella = new Data(nomeTabella);
				//Calcolo dei FP
				outFP = FrequentPatternMiner.frequentPatternDiscovery(tabella, minSup);
				//Calcolo dei CP
				cp = ClosedPatternMiner.closedPatternDiscovery(outFP, epsilon);
				//Salvataggio dei CP su un file 
				String nomeFile = nomeTabella.toLowerCase() + ".dat";
				ClosedPatternArchive.serializeArchive(cp, nomeFile);
				String serverObjName = "//127.0.0.1:2005/cpaInterface";
				Naming.rebind(serverObjName, cp);
				//Si passa il controllo al Client
				out.println(" ");
				break;
		
			case 'i':
				String nomeFileBack = in.readLine();
				nomeFileBack = in.readLine();
				if(nomeFileBack.contains(".dat"))
					cp = (ClosedPatternArchive)ClosedPatternArchive.deserializeArchive(nomeFileBack);
				else
				{
					nomeFileBack += ".dat";
					cp = (ClosedPatternArchive)ClosedPatternArchive.deserializeArchive(nomeFileBack);	
					String serverObjName1 = "//127.0.0.1:2005/cpaInterface";
					Naming.rebind(serverObjName1, cp);
				}
			break;
			
			default:
				break;
			}
			socket.close();
			
		}
		//Le eccezioni inviano al Client messaggi diversi in modo che il Client possa gestirle fornendo dei messaggi di dialogo
		catch(SQLException | DatabaseConnectionException e)
		{
			out.println("ErrorDB");
			e.printStackTrace();
			return;
		}catch(NoValueException e)
		{
			out.println("ErrorData");
			e.printStackTrace();
			return;
		}catch(IOException e)
		{
			out.println("ErrorConn");
			e.printStackTrace();
			return;
		}catch(ClassNotFoundException e)
		{
			out.println("ErrorSerialize");
			e.printStackTrace();
			return;
		} catch (EmptySetException e) {
			out.println("ErrorData");
			e.printStackTrace();
		}
	}
	
}

