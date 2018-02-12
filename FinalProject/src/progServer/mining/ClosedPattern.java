package progServer.mining;
import java.util.*;
import java.io.*;
/**
 * Classe che rappresenta un pattern frequente closed
 * @author Paolo Pastore 652412
 *
 */
public class ClosedPattern extends FrequentPattern implements Serializable {
	
	/**
	 *Costruttore che avvalora i dati membro di uno specifico closed 
	 * @param fp lista di Items
	 * @param support supporto del pattern di origine
	 */
	public ClosedPattern(LinkedList<Item> fp, float support)
	{
		super();
		this.setSupport(support);
		for(Item a: fp)
		{
			this.addItem(a);
		}
	}
	
	
}
