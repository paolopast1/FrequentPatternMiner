
package progServer.mining;
import java.io.*;

/**
 * Classe che modella un intervallo numerico ed implementa l'interfaccia Serializable
 * @author Paolo Pastore 652412
 *
 */
public class Interval implements Serializable {
	/**
	 * valore di tipo float che rappresenta l'estremo inferiore dell'intervallo
	 */
	private float inf;
	/**
	 * Valore di tipo float che rappresenta l'estremo superiore dell'intervallo
	 */
	private float sup;
	
	/**
	 * Costruttore che avvalora gli attributi membro della classe
	 * @param x valore di tipo float che sarà usato per avvalorare l'estremo inferiore
	 * @param y valore di tipo float che sarà usato per avvalorare l'estremo supriore
	 */
	Interval(float x, float y)
	{
		inf = x;
		sup = y;
	}
	
	/**
	 * Metodo che avvalora l'estremo inferiore dell'intervallo con il valore passato come parametro
	 * @param x valore di tipo float
	 */
	void setInf(float x)
	{
		inf = x;
	}
	
	/**
	 * Metodo che avvalora l'estremo superiore dell'intervallo con il valore passato come parametro
	 * @param y valore di tipo float
	 */
	void setSup(float y)
	{
		sup = y;
	}
	
	/**
	 * Metodo che restituisce l'estremo inferiore dell'intervallo
	 * @return valore di tipo float
	 */
	float getInf()
	{
		return inf;
	}
	
	/**
	 * Metodo che restituisce l'estremo superiore dell'intervallo
	 * @return valore di tipo float
	 */
	float getSup()
	{
		return sup;
	}
	
	/**
	 * Metodo che verifica che il valore passato come parametro sia incluso nell'intervallo
	 * @param value valore di tipo float
	 * @return true se value è compreso nell'intervallo, false altrimenti
	 */
	boolean checkValueInclusion(float value)
	{
		return (value >= inf && value < sup);
	}
	
	/**
	 * Overriding del metodo toString della classe Object
	 * @return riferimento ad un oggetto di classe String 
	 */
	public String toString()
	{
		String s = "[" + inf + ", " + sup +"]";
		return s;
	}
}
