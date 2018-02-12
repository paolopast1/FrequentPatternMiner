
package progServer.mining;
import progServer.data.*;
import java.io.*;

/**
 * che estende la classe Item e rappresenta una coppia <Attributo continuo- intervallo di valori assunti>
 * @author Paolo Pastore 652412 
 *
 */
public class ContinuousItem extends Item implements Serializable {
	/**
	 * Estremo inferiore dell'intervallo
	 */
	private float inf;
	/**
	 * Estremo superiore dell'intervallo
	 */
	private float sup;
	
	/**
	 * Costruttore che richiama il costruttore della superclasse e avvalora inf e sup
	 * @param attribute Oggetto di classe AttributoContinuo 
	 * @param value oggeto di classe Interval che viene usato per avvalorare i membri della classe
	 */
	public ContinuousItem(AttributoContinuo attribute, Interval value)
	{
		super(attribute, value);
		inf = value.getInf();
		sup = value.getSup();
	}
	
	
	/**
	 * Metodo che verifica che il valore-intervallo in input è incluso nell'intervallo di valori associato all'Item. Ad esempio,
	 * dato una transazioni in cui [Temperature=15] e un Item con <Temperature =[10;30.3]>, verifica se 15 è incluso in [10;30.3].
	 * @param value valore del quale si deve verificare l'inclusione
	 */
	boolean checkItemCondition(Object value)
	{
		if(value instanceof Interval)
		{
			return(((Interval)value).getInf() >= inf && ((Interval)value).getSup() <= sup);
		}
		else
			return((float)value >= inf && (float)value <= sup);
	}
	
	/**
	 * Overriding del metodo toString della classe Object
	 */
	public String toString()
	{
		String s = "(" + this.getAttribute().getName() + ",[" + inf + ";"+sup+"])";
		return s;
	}
	
	
}
