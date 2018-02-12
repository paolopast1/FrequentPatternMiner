
package progServer.mining;
import progServer.data.Attributo;
import java.io.*;

/**
 * Classe che modella un Item ovvero una coppia <Attributo - valore>. Questa classe implementa l'interfaccia Serializable
 * @author Paolo Pastore 652412
 */
public abstract class Item implements Serializable{
	/**
	 * riferimento ad un oggetto di classe Attributo che rappresenta l'attributo coinvolto nell'item
	 */
	protected Attributo attribute;
	/**
	 * riferimento ad un oggetto di classe Object che rappresenta il valore dell'attributo
	 */
	protected Object value;
	
	/**
	 * metodo che inizializza i valori dei membri attributi
	 * @param a riferimento ad un oggetto di classe Attributo
	 * @param v riferimento ad un oggetto di classe Object
	 */
	public Item(Attributo a, Object v)
	{
		attribute = a;
		value = v;
	}
	
	
	/**
	 * Metodo che restituisce un riferimento all'oggetto (attributo membro) di classe Attributo
	 * @return riferimento ad un oggetto di classe Attributo
	 */ 
	Attributo getAttribute()
	{
		return attribute;
	}
	
	/**
	 * Metodo che restituisce un riferimento all'oggetto(attributo membro) di classe Object
	 * @return riferimento ad un oggetto di classe Object
	 */
	Object getValue()
	{
		return value;
	}
	
	/**
	 * Metodo che verifica se esiste un item formato dalla coppia <Attributo (membro della classe) – Value (valore passato per argomento)
	 * @param value riferimento ad un oggetto di classe Object
	 * @return valore booleano
	 */
	abstract boolean checkItemCondition(Object value);
	
	/**
	 * Overriding del metodo toString della classe Object
	 * @return riferimento ad un oggetto di classe String
	 */
	public String toString()
	{
		String res = new String();
		res += "(";
		res += attribute;
		res += " = ";
		res += value;
		res += ")";
		return res;
	}
}
