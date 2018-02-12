package progServer.data;
import java.util.*;
import java.io.*; 

/**
 * modella l'iterazione (Iterator) sulla collezione di intervalli di valori che
 * l'attributo continuo può assumere. La classe implementa i metodi della
 * interfaccia generica Iterator<T> tipizzata con Float
 * @author Paolo Pastore
 * @matricola 652412
 *
 */
public class ContinuousAttributeIterator implements Iterator<Float>, Serializable{
	/**
	 * Valore minimo dell'attributo
	 */
	private float min;
	/**
	 * valore massimo dell'attributo
	 */
	private float max;
	/**
	 * Indice che tiene traccia dell'elemento che si sta analizzando
	 */
	private int j = 0;
	/**
	 * numero di sottointervalli da scandire
	 */
	private int numValues;
	
	/**
	 * Costruttore che avvalora gli attributi membro
	 * @param min valore di tipo float che rappresenta l'estremo inferiore da assegnare all'intervallo
	 * @param max valore di tipo float che rappresenta l'estremo superiore da assegnare all'intervallo
	 * @param numValues valore di tipo int che rappresenta il numero di sottointervalli in cui dividere l'intervallo dato
	 */
	public ContinuousAttributeIterator(float min, float max, int numValues)
	{
		this.min = min;
		this.max = max;
		this.numValues = numValues;
	}
	
	/**
	 * Implementazione del metodo hasNext() dell'interfaccia generica Iterator<T>
	 * Restituisce false se sono stati scanditi tutti i sottointervalli, true altrimenti
	 */
	public boolean hasNext()
	{
		return(j < numValues);
	}
	
	/**
	 * Implementazione del metodo next() dell'interfaccia generica Iterator<T>
	 * Retituisce l'estremo superiore del sottointervallo successivo a quello che si sta analizzando
	 */
	public Float next()
	{
		j++;
		return new Float(min + j*(max - min)/numValues);
	}
	
	/**
	 * Implementazione del metodo remove() dell'interfaccia generica Iterator<T>
	 * Non fa nulla
	 */
	public void remove(){}
}
