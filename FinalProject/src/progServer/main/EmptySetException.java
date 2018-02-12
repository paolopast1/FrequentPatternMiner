package progServer.main;

/**
 * Classe che modella l'eccezione da lanciare nel caso in cui la tabella dovesse essere vuota
 * @author Paolo Pastore 652412
 */
public class EmptySetException extends Exception{
	/**
	 * Costruttore senza parametri
	 */
	public EmptySetException(){}
	/**
	 * Costruttore in overloading che richiama il costruttore della superclasse
	 * @param msg oggetto di tipo String
	 */
	public EmptySetException(String msg){super(msg);}
}
