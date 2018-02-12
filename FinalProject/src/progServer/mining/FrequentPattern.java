
package progServer.mining;
import java.io.*;
import java.util.*;
/**
 * Classe che modella un pattern frequente e che implementa le interfacce Iterable<Item>, Comparable<FrequentPattern> e Serializable
 * @author Paolo Pastore 652412
 *
 */
public class FrequentPattern implements Iterable<Item>, Comparable<FrequentPattern>, Serializable
{
	/**
	 * Lista di Item
	 */
	private List<Item> fp = new LinkedList<Item>();
	/**
	 * supporto (Frequenza) del pattern frequente
	 */
	private float support;
	
	
	/**
	 * Costruttore. Non fa nulla.
	 */
	public FrequentPattern(){}
	/**
	 * Metodo che aggiunge un Item al frequentPattern. Aggiunge un item al vettore facendo una copia del "vecchio" vettore e aggiungendo in coda il nuovo item
	 * @param item
	 */
	public void addItem(Item item)
	{
		List<Item> fs = new LinkedList<Item>();
		for(Item a: fp)
		{
			fs.add(a);
		}
		fs.add(fs.size(),item);
		fp = fs;
	}
	
	/**
	 * Metodo che restituisce un riferimento all'item nella posizione passata come parametro
	 * @param index valore di tipo int che rappresenta una posizione all'interno del frequentPattern
	 * @return Riferimento all' oggetto di classe Item nella posizione index del FrequentPattern
	 */
	 //restituisce il riferimento all'item in posizione index
	public Item getItem(int index)
	{
		return fp.get(index);
	}
	
	/**
	 * Metodo che restituisce il supporto del pattern frequente
	 * @return valore di tipo float che rappresenta il supporto del pattern frequente
	 */
	public float getSupport()
	{
		return support;
	}
	
	/**
	 * Metodo che restituisce la lunghezza del pattern, ovvero il numero di item che sono contenuti nel pattern frequente
	 * @return valore di tipo int che rappresenta il numero di Item contenuti nel FrequentPattern
	 */
	public int getPatternLength()
	{
		return fp.size();
	}
	
	/**
	 * Metodo che permette di settare il supporto del frequentPattern corrente
	 * @param support valore di tipo float che rappresenta il valore al quale settare il supporto del pattern corrente
	 */
	public void setSupport(float support)
	{
		this.support = support;
	}
	
	/**
	 * overriding del metodo toString() di Object
	 * @return un oggetto di classe String
	 */
	public String toString()
	{
		String value="";
		for(int i = 0; i < fp.size() - 1; i++)
		{
			value+= fp.get(i) +" AND ";
		}
		if(fp.size()>0)
		{
			value += fp.get(fp.size()-1);
			value+="Freq: ["+support+"]";
		}
		return value;
	}
	
	/**
	 * Metodo che restituisce un Iteratore e che implementa il metodo iterator() dell'interfaccia Iterable<Item>
	 * @return Iterator<Item>
	 */
	public Iterator<Item> iterator()
	{
		return fp.iterator();
	}
	
	
	/**
	 * Metodo che restituisce un intero e che implementa il metodo compareTo() dell'interfaccia Comparable<FrequetPattern>
	 * @param x oggetto di classe FrequentPattern con il quale confrontare il pattern corrente
	 * @return -1 se la lunghezza del pattern corrente è minore o uguale della lunghezza di x, 1 altrimenti
	 */
	public int compareTo(FrequentPattern x)
	{
		if(this.getPatternLength() <= x.getPatternLength())
			return -1;
		else
			return 1;
	}
	
	/**
	 * Metodo che restituisce una lista di Item contenuti nel FrequentPattern
	 * @return
	 */
	public LinkedList<Item> geitemList()
	{
		return (LinkedList<Item>) fp;
	}
}
