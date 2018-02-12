package progServer.data; 
import java.io.*;

/**
 * 
 * @author Paolo Pastore 
 * @matricola 652412
 * Classe astratta che modella un attributo che può essere sia discreto che continuo
 *	
 */
public abstract class Attributo implements Serializable{
	/**
	 * Nome simbolico dell'attributo
	 */
	protected String name;
	/**
	 * Indice identificativo dell'attributo
	 */
	protected int index;
	
	
	/**
	 * Costruttore che avvalori gli attributi di un oggetto di tipo Attributo
	 * @param s nome da assegnare all'attributo
	 * @param i indice da assegnare all'attributo
	 */
	public Attributo(String s, int i)
	{
		name = s;
		index = i;
	}
	
	/**
	 * Restituisce il nome dell'attributo
	 * @return un oggetto di tipo String che rappresenta il nome dell'attributo
	 */
	public String getName()
	{
		return new String(name);
	}
	
	/**
	 * Restituisce l'indice identificativo dell'attributo
	 * @return un int indice identificativo dell'attributo
	 */
	public int getIndex()
	{
		return index;
	}
	
	
	/**
	 * Overriding del metodo toString della classe Object
	 * Simile al metodo getName()
	 * Restituisce un oggetto di tipo String con il nome dell'attributo
	 */
	public String toString()
	{
		return new String(name);
	}
}