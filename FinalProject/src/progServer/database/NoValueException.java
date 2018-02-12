package progServer.database;

/**
 * Classe che modella un'eccezione da lanciare quando si trova un valore nullo all'interno della tabella che si vuole caricare	
 * @author Paolo
 *
 */
public class NoValueException extends Exception{
	/**
	 * Costruttore senza parametri che inizializza un oggetto di tipo NoValueException
	 */
	public NoValueException(){}
	/**
	 * Costruttore in overloading che richiama il costruttore della superclasse
	 * @param msg oggetto di tipo String 
	 */
	public NoValueException(String msg){super(msg);}
}
