package progServer.database;
import java.sql.*;

public class DBaccess {
	/**
	 * Nome del driver
	 */
	private static final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	/**
	 * Nome del DBMS utilizzato
	 */
	private static final String DBMS = "jdbc:mysql";
	/**
	 * Server al quale collegarsi
	 */
	private static final String SERVER = "localhost";
	/**
	 * Porta utilizzata
	 */
	private static final int PORT = 3306;
	/**
	 * Nome del Database al quale collegarsi
	 */
	private static final String DATABASE = "CFP_652412";
	/**
	 * Nome utente da usare nel tentativo di connettersi
	 */
	private static final String USER_ID = "Pastore_652412_17";
	/**
	 * Password per accedere al DB
	 */
	private static final String PASSWORD = "paolopastore";
	/**
	 * Oggetto di tipo Connection
	 */
	private static Connection conn;
	
	/**
	 * Metodo che provvede a
caricare il driver per stabilire la connessione con la base di dati e inizializza tale
connessione.  
	 */
	public static void initConnection() throws DatabaseConnectionException
	{
		//"?autoReconnect=true&useSSL=false" è stato inserito per risolvere alcune eccezioni su SSL
		String connectionString = DBMS+"://" + SERVER + ":" + PORT + "/" + DATABASE + "?autoReconnect=true&useSSL=false"; 
		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance() ; 
		} catch (Exception ex) {
			System.out.println("Impossibile trovare il Driver: " + DRIVER_CLASS_NAME);
		}
		try {
			conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD); 
		} catch (SQLException e) {
			System.out.println("Impossibile connettersi al DB");
			e.printStackTrace();
			}
	}
	
	/**
	 * Metodo che restituisce l'OID dell'oggetto di tipo connessione, attributo membro
	 * @return un oggetto di tipo Connection che rappresenta la connessione inizializzata dalla classe
	 */
	public static Connection getConnection() 
	{
		return conn;
	}
	
	/**
	 * Metodo che chiude la connessione al DB
	 */
	public static void closeConnection()
	{
		try{
			conn.close();
		}catch(SQLException e)
		{
			System.out.println("Impossibile chiudere il DB");
			e.printStackTrace();
		}
	}
}