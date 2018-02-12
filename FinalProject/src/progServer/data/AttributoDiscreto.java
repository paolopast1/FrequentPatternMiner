package progServer.data;
import java.util.*;
import java.io.*;


/**
 * Classe che estende la classe astratta Attributo e rappresenta un attributo i cui valori sono valori discreti
 * @author Paolo Pastore
 * @matricola 652412
 *
 */
public class AttributoDiscreto extends Attributo implements Serializable {
	/**
	 * Lista dei possibili valori che l'attributo discreto può assumere 
	 */
	private ArrayList<String> values;
	
	/**
	 * Costruttore che richiama il costruttore della superclasse e inizializza gli attributi membro
	 * @param n nome da assegnare all'attributo
	 * @param i indice identificativo da assegnare all'attributo
	 * @param val valori discreti da inserire nella lista di possibili valori che l'attributo può assumere
	 */
	public AttributoDiscreto(String n, int i, ArrayList<Object> val)
	{
		super(n, i);
		values = new ArrayList<String>();
		for(Iterator<Object> it = val.iterator(); it.hasNext(); )
		{
			values.add((String)it.next());
		}
	}
	
	/**
	 * Metodo che restituisce il numero di valori che l'attributo può assumere
	 * @return valore intero che rappresenta il numero di possibili valori che l'attributo discreto può assumere
	 */
	public int getNumeroValoriDistintiAttributi()
	{
		return values.size();
	}
	
	/**
	 * Metodo che restituisce il valore i-esimo che l'attributo può assumere
	 * @param i indice del possibile valore che l'attributo può assumere
	 * @return un oggetto di tipo String che rappresenta l'i-esimo valore che l'attributo può assumere
	 */
	public String getValue(int i)
	{
		return values.get(i);
	}
}
