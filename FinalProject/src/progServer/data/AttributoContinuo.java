package progServer.data;
import java.util.*;
import java.io.*;

/**
 * Classe che estende la classe astratta Attributo e rappresenta un attributo i cui valori sono contenuti in un determinato intervallo
 * @author Paolo Pastore
 * @matricola 652412
 */

public class AttributoContinuo extends Attributo implements Iterable<Float>, Serializable {
	/**
	 * Estremo inferiore dell'intervallo
	 */
	private float min;
	/**
	 * Estremo superiore dell'intervallo
	 */
	private float max;
	
	/**
	 * Costruttore che richiama il costruttore della superclasse e inizializza gli attributi membro
	 * @param s nome da assegnare all'attributo
	 * @param i indice identificativo da assegnre all'attributo
	 * @param x estremo inferiore da assegnare all'attributo 
	 * @param y estremo superiore da assegnare all'attributo
	 */
	public AttributoContinuo(String s, int i, float x, float y)
	{
		super(s, i);
		min = x;
		max = y;
	}
	
	/**
	 * Metodo che restituisce l'estremo inferiore dell'intervallo
	 * @return valore di tipo float che rappresenta l'estremo inferiore dell'intervallo
	 */
	public float getMin()
	{
		return min;
	}
	
	/**
	 * Metodo che restituisce l'estremo superiore dell'intervallo
	 * @return valore di tipo float che rappresenta l'estremo superiore dell'intervallo
	 */
	public float getMax()
	{
		return max;
	}
	
	/**
	 * Iteratore per un oggetto di classe AttributoContinuo che divide l'intervallo in 5 sottointervalli e scandisce uno per uno questi sottointervalli
	 */
	public Iterator<Float> iterator()
	{
		return (new ContinuousAttributeIterator(min, max, 5));
	}
}
