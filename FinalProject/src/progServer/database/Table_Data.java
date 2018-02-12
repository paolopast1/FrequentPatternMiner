package progServer.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import progServer.database.Table_Schema.Column;

/**
 * classe che modella la gestione dei dati contenuti in una tabella. Al suo interno è definita la classe Tuple_Data come membro e utilizzata
 * per modellare una tupla della tabella.
 * @author Paolo Pastore 652412
 */
public class Table_Data {
	/**
	 * Nasted Class che modella una tupla della tabella
	 * @author Paolo Pastore 652412
	 *
	 */
	public class TupleData
	{
		/**
		 * Lista di Object che rappresenta una tupla della tabella
		 */
		public List<Object> tuple=new ArrayList<Object>();
		/**
		 * Overriding del metodo toString della classe Object
		 */
		public String toString()
		{
			String value="";
			Iterator<Object> it= tuple.iterator();
			while(it.hasNext())
				value+= (it.next().toString() +" ");
			return value;
		}
	}
	
	/**
	 * Metodo che restituisce una lista di tuple dove ogni elemento di tale lista corrisponde ad una tupla della tabella del DB
	 * E' necessario che sia già stata stabilita una connessione con il DB
	 * @param table nome della tabella del DB da caricare 
	 * @return Lista di tuple presenti nella tabella del DB
	 * @throws SQLException
	 */
	public List<TupleData> getTransazioni(String table) throws SQLException
	{
		//inizializzazione di una lista di tuple che verrà restituita come risultato
		ArrayList<TupleData> trans = new ArrayList<TupleData>();
		Connection conn = DBaccess.getConnection();
		Table_Schema schema = new Table_Schema(table);
		Statement st = conn.createStatement();
		//Esecuzione della query che restituisce tutte le tuple della tabella
		ResultSet res = st.executeQuery("SELECT* FROM " + table + ";");
		int j = 0;
		//ciclo che scandisce tutto il ResultSet per popolare la lista
		while(res.next())
		{
			boolean insert = true;
			TupleData corr = new TupleData();
			//Ciclo che per ogni tupla del ResultSet controlla se l'i-esimo elemento sia null, un'istanza di String o un'istanza di Float
			//Con questo ciclo si popola una tupla (se non ci sono elementi nulli) 
			for(int i = 1; i <= schema.getNumberOfAttributes(); i++)
			{
				try{
					if(res.getObject(i) == null || res.getObject(i).equals(""))
						throw new NoValueException();
					else if(res.getObject(i) instanceof String)
						corr.tuple.add(i-1, res.getString(i));
					else if(res.getObject(i) instanceof Float)
						corr.tuple.add(i-1, (Float)res.getFloat(i));
				}catch(NoValueException e)
				{
					insert = false;
					break;
				}
			}
			//Al termine del ciclo si inserisce la tupla all'interno della lista
			if(insert)
			{
				trans.add(j, corr);
				j++;
			}
		}
		return trans;
	}
	
	/**
	 * Metodo che restituisce una lista con i valori distinti che una colonna può assumere
	 * @param table nome della tabella
	 * @param column oggetto ti tipo column che modella la colonna della tabella della quale si vogliono sapere i valori distinti
	 * @return Lista di Object. Ogni elemento della lista rappresenta uno dei possibili valori distinti che si trovano nella colonna
	 * @throws SQLException
	 */
	public List<Object>getColumnValues(String table, Column column) throws SQLException
	{
		ArrayList<Object> trans = new ArrayList<Object>();
		Connection conn = DBaccess.getConnection();
		Statement st = conn.createStatement();
		//Esecuzione della query che restituisce i valori distinti della colonna data
		ResultSet res = st.executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table + ";");
		while(res.next())
		{
			trans.add(res.getObject(column.getColumnName()));
		}
		return trans;
	}
	
	/**
	 * Metodo che a seconda che gli si passi un argomento di tipo QUERY_TYPE con valore MIN o MAX restituisce un Object che rappresenta
	 * rispettivamente il valore minimo o il valore massimo della colonna passata come argomento nella tabella con nome uguale a quello passato come argomento
	 * @param table nome tabella
	 * @param column colonna da analizzare
	 * @param aggregate valore di tipo QUERY_TYPE che puo' assumere solo valore MIN o MAX
	 * @return valore minimo o valore massimo della colonna data nella tabella
	 * @throws SQLException
	 * @throws NoValueException
	 */
	public Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate)throws SQLException,NoValueException
	{
		Connection conn = DBaccess.getConnection();
		Statement st = conn.createStatement();
		ResultSet res;
		if(aggregate == QUERY_TYPE.MIN)
		{
			res = st.executeQuery("SELECT MIN(" + column.getColumnName() + ") FROM " + table + ";");
		}
		else
		{
			res = st.executeQuery("SELECT MAX(" + column.getColumnName() + ") FROM " + table + ";");
		}
		while(res.next())
			return res.getObject(1);
		return res.getObject(1);
	}
}
