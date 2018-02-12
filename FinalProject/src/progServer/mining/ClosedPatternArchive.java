package progServer.mining;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Classe che fa da contenitore per i pattern frequenti closed con i relativi pattern ridondanti.
 * @author Paolo Pastore 652412
 */
public class ClosedPatternArchive extends UnicastRemoteObject implements Serializable, CPAInterface {
	/**
	 * Attributo membro per contenere coppie di pattern frequenti chiusi con i loro pattern frequenti ridondanti
	 */
	public HashMap<FrequentPattern, TreeSet<FrequentPattern>> archive;
	
	/**
	 * Istanzia un oggetto di tipo HashMap<FrequentPattern, TreeSet<FrequentPattern>>
	 * @throws RemoteException
	 */
	public ClosedPatternArchive() throws RemoteException
	{
		archive = new HashMap<FrequentPattern, TreeSet<FrequentPattern>> ();
	}
	
	/**
	 * Inserisce un nuovo FrequentPattern come pattern chiuso nell'archivio
	 * @param fp pattern-chiave da aggiungere
	 */
	public void put(FrequentPattern fp)
	{
		if(!archive.containsKey(fp))
		{
			archive.put(fp, new TreeSet<FrequentPattern>());
		}
	}
	
	/**
	 * aggiunge il pattern frequente fp come pattern chiuso se non è già contenuto. Se invece è contenuto, inserisce l’argomento pattern all’insieme dei ridondanti di fp.
	 * @param fp oggetto di tipo FrequentPattern
	 * @param pattern oggetto di tipo FrequentPattern
	 */
	public void put(FrequentPattern fp, FrequentPattern pattern)
	{
		if(archive.containsKey(fp) == true)
		{
			archive.get(fp).add(pattern);
		}
		else
		{
			archive.put(fp, new TreeSet<FrequentPattern>());
			archive.get(fp).add(pattern);
		}
	}
	
	/**
	 * Metodo che restituisce l'insieme dei ridondanti del FrequentPattern passato come parametro
	 * @param fp oggetto di tipo FrequentPattern del quale si voogliono sapere i ridondanti
	 * @return Oggetto di tipo TreeSet<FrequentPattern> che rappresenta l'insieme dei pattern ridondanti di fp
	 */
	public TreeSet<FrequentPattern> getRedundants(FrequentPattern fp)
	{
		TreeSet<FrequentPattern> res;
		if(archive.get(fp).isEmpty())
		{
			res = new TreeSet<FrequentPattern>();
			res.add(fp);
			return res;
		}
		else
			return archive.get(fp);
	}
	
	/**
	 * Overriding del metodo toString della classe Object
	 */
	public String toString()
	{
		Set<FrequentPattern> temp = archive.keySet();
		int max = 0;
		for(FrequentPattern a: temp)
		{
			if(a.getPatternLength() > max)
				max = a.getPatternLength();
		}
		FrequentPattern temp1, temp2;
		String res = "";
		while(max > 0)
		{
			for(Iterator<FrequentPattern> j = archive.keySet().iterator(); j.hasNext(); )
			{
				temp1 = j.next();
				if(temp1.getPatternLength() == max)
				{
					
					res += temp1;
					res += "\n";
					for(Iterator<FrequentPattern> i = archive.get(temp1).iterator(); i.hasNext(); )
					{
						temp2 = i.next();
						res = res + "    =>" + temp2 + "\n";
					}
				}
			}
			max--;
		}
		return res;
	}
	
	
	private static int getMaxLength(LinkedList<FrequentPattern> outFP)
	{
		int max = 0;
		for(FrequentPattern a: outFP)
		{
			if(a.getPatternLength() > max)
				max = a.getPatternLength();
		}
		return max;
	}
	/**
	 * Metodo statico per la serializzazione dell'archivio
	 * @param x oggetto di classe closedPatternArchive che deve essere serializzato
	 * @param path oggetto di tipo String che rappresenta il nome del file su cui serializzare
	 * @throws IOException
	 */
	public static void serializeArchive(ClosedPatternArchive x, String path) throws IOException
	{
		FileOutputStream outFile = new FileOutputStream(path);
		ObjectOutputStream outStream = new ObjectOutputStream(outFile);
		outStream.writeObject(x);
		outStream.close();	
	}
	
	/**
	 * Metodo statico per la deserializzazione dell'archivio
	 * @param path oggetto di classe String che rappresenta il nome del file da cui deserializzare l'oggetto
	 * @return oggetto di classe ClosedPatternArchive
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static ClosedPatternArchive deserializeArchive(String path) throws IOException, ClassNotFoundException
	{
		FileInputStream inFile = new FileInputStream(path);
		ObjectInputStream inStream = new ObjectInputStream(inFile);
		ClosedPatternArchive res = (ClosedPatternArchive)inStream.readObject();
		inStream.close();
		return res;
	}
	
	/**
	 * Metodo per serializzare la lista di FrequentPattern
	 * @param x lista di FrequentPattern da serializzare
	 * @param path oggetto di tipo String che rappresenta il nome del file su cui serializzare 
	 * @throws IOException
	 */
	public static void serializeFrequent(LinkedList<FrequentPattern> x, String path) throws IOException
	{
		FileOutputStream outFile = new FileOutputStream(path);
		ObjectOutputStream outStream = new ObjectOutputStream(outFile);
		outStream.writeObject(x);
		outStream.close();	
	}
	
	/**
	 * Metodo per deserializzare la lista di FrequentPattern
	 * @param path oggetto di tipo String che rappresenta il nome del file da cui deserializzare
	 * @return lista di FrequentPattern
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static LinkedList<FrequentPattern> deserializeFrequent(String path) throws IOException, ClassNotFoundException
	{
		FileInputStream inFile = new FileInputStream(path);
		ObjectInputStream inStream = new ObjectInputStream(inFile);
		LinkedList<FrequentPattern> res = (LinkedList<FrequentPattern>)inStream.readObject();
		inStream.close();
		return res;
	}
	
	/**
	 * Metodo simile al metodo toString. Questo metodo è stato creato perchè la sua implementazione è richiesta dall'interfaccia remota CPAInterface
	 */
	public String convertToString()
	{
		return this.toString();
	}
}
