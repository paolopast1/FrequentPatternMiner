package progServer.database;

/**
 * Classe che modella un' eccezione per qualche problema di connessione a DB
 * @author Paolo Pastoore 652412
 *
 */

public class DatabaseConnectionException extends Exception{
	/**
	 * Costruttore senza parametri
	 * Si limita a creare un oggetto di tipo DatabaseConnectionException
	 */
	DatabaseConnectionException(){};
	/**
	 * Costruttore in overloading che richiama il costruttore della superclasse con un parametro di tipo String
	 * @param msg oggetto di tipo String
	 */
	DatabaseConnectionException(String msg){super(msg);}
}
