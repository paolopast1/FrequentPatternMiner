package progServer.data;
import java.util.*;
import java.io.*;
import java.sql.SQLException;

import progServer.database.*;
import progServer.database.Table_Data.TupleData;

/**
 * Classe che rappresenta la tabella con tutti i dati da analizzare
 * @author Paolo Pastore 652412
 */
public class Data implements Serializable {
	/**
	 * una matrice nXm di tipo Object che contiene
	 * l'insieme di transazioni ed è organizzato come numero di transazioni X
	 * numero di attributi
	 */
	private Object data[][];
	
	/**
	 * Numero di transizioni della tabella
	 */
	private int numberOfExemples;
	/**
	 * Lista degli attrinuti(continui o discreti). Rappresentano le colonne della nostra tabella
	 */
	private List<Attributo> attributeSet=new LinkedList<Attributo>();
	
	/**
	 * Costruttore di Data. Questo si collega al DB, carica la matrice con i dati presenti nel DB, avvalora la lista di tutti gli attributi
	 * e infine calcola quante sono le tuple distinte
	 * @param table nome della tabella del DB da caricare nella nostra matrice
	 */
	public Data(String table) throws SQLException, NoValueException, DatabaseConnectionException
	{
		//tentativo di connessione al DB
		DBaccess.initConnection();
		Table_Data dat = new Table_Data();
		Table_Schema sch = new Table_Schema(table);
		//Ciclo che permette di avvalorare la lista degli attributi della tabella
		for(int i = 0; i < sch.getNumberOfAttributes(); i++)
		{
			String nomeColonna = sch.getColumn(i).getColumnName();
			if(!sch.getColumn(i).isNumber())
			{
				ArrayList<Object> valoriDistinti = new ArrayList<Object>();
				valoriDistinti = (ArrayList<Object>)dat.getColumnValues(table, sch.getColumn(i));
				attributeSet.add(i, new AttributoDiscreto(nomeColonna , i, valoriDistinti));
			}
			else
			{
				Float max = (Float)dat.getAggregateColumnValue(table, sch.getColumn(i), QUERY_TYPE.MAX);
				Float min = (Float)dat.getAggregateColumnValue(table, sch.getColumn(i), QUERY_TYPE.MIN);
				attributeSet.add(i, new AttributoContinuo(nomeColonna, i, min.floatValue(), max.floatValue()));
			}
		}
		
		//avvaloramentoo di numberOfExamples
		numberOfExemples = dat.getTransazioni(table).size();
		//Creazione della matrice 
		data = new Object[numberOfExemples][attributeSet.size()];
		List<TupleData> trans = dat.getTransazioni(table);
		//Due cicli annidati che permettono di avvalorare ogni elemento della nostra matrice
		for(int i = 0; i < numberOfExemples; i++)
		{
			for(int j = 0; j < trans.get(i).tuple.size(); j++)
			{
				data[i][j] = trans.get(i).tuple.get(j);
			}
		}
		//Chiusura connessione al termine della costruzione della tabella
		DBaccess.closeConnection();
	}
	
	/**
	 * Metodo che restituisce il numero di transizioni nella tabella
	 * @return valore di tipo int che rappresenta il numero di transizioni della tabella
	 */
	public int getNumberOfExemples()
	{
		return numberOfExemples;
	}
	
	/**
	 * Metodo che restituisce il numero di attributi nella tabella (numero di colonne)
	 * @return valore di tipo int che rappresenta il numero di attributi di cui si compone la tabella
	 */
	public int getNumberOfAttributes()
	{
		return attributeSet.size();
	}
	
	/**
	 * Metodo che restituisce un elemento della tabella delle transizioni
	 * @param row indice di riga 
	 * @param col indice di colonna
	 * @return elemento della tabella in posizione [row][col]
	 */
	public Object getAttributeValue(int row, int col)
	{
		return data[row][col];
	}
	
	/**
	 * Overriding del metodo toString ereditato dalla classe Object
	 * Restituisce una conversione in Stringa della tabella 
	 */
	public String toString()
	{
		String res = new String();
		for(int i = 0; i < numberOfExemples; i++)
		{
			res = res + (i+1) + ". ";
			for(int j = 0; j < getNumberOfAttributes(); j++)
			{
				if(data[i][j] instanceof Float)
					res = res + (Float)data[i][j] + ", ";
				else
					res += (String)data[i][j] + ", ";
			}
			res = res + "\n";
		}
		return res;
	}
	
	/**
	 * Metodo che restituisce un oggetto di tipo Attributo e che rappresenta l'Attributo in posizione index
	 * @param index indice dell'Attributo che si vuole che il metodo restituisca
	 * @return oggetto di tipo Attributo
	 */
	public Attributo getAttributo(int index)
	{
		return attributeSet.get(index);
	}
	
}



