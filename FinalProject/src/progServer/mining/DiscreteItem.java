
package progServer.mining;
import progServer.data.AttributoDiscreto;
import java.io.*;

/**
 * Classe che estende la classe Item e rappresenta una coppia <Attributo discreto- valore discreto> (Outlook=”Sunny”)
 * @author Paolo Pastore 652412
 */
public class DiscreteItem extends Item implements Serializable{
	/**
	 * Costruttore che invoca il costruttore della superclasse
	 * @param a oggetto di classe AttributoDiscreto
	 * @param v oggetto di tipo String
	 */
	public DiscreteItem(AttributoDiscreto a, String v)
	{
		super(a, v);
	}
	
	/**verifica che l'item corrente ha valore uguale a Value per la coppia <Attributo discreto- Value discreto >
	 * @param v valore del quale si deve verificare l'uguaglianza con il valore dell'item corrente
	 * @return true se v è uguale al valore dell'attributo membro value della superclasse, false altrimenti
	 */
	public boolean checkItemCondition(Object v)
	{
		return (((String)this.getValue()).equals((String)v));
	}
}
