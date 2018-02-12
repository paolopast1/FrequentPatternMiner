package progServer.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Classe che modella una tabella di un database relazionale. Al suo interno vi è la classe membro Column
 */

public class Table_Schema {
	/**
	 * Classe che modella una colonna di un Database
	 */
	public class Column{
		/**
		 * nome della colonna
		 */
		private String name;
		/**
		 * tipo dei valori che può contenere (number o !number)
		 */
		private String type;
		/**
		 * Costruttore che avvalora gli attributi membro
		 * @param name nome da assegnare alla colonna
		 * @param type tipo da assegnare alla colonna
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}
		/**
		 * Metodo che restituisce il nome della colonna
		 * @return un oggetto di tipo String che rappresenta il nome della colonna
		 */
		public String getColumnName(){
			return name;
		}
		/**
		 * Metodo che permette di scoprire se i valori di una tabella sono numerici o no
		 * @return true se i valori della colonna sono numerici, false altrimenti
		 */
		public boolean isNumber(){
			return type.equals("number");
		}
		
		/**
		 * overriding del metodo toString della classe Object 
		 * @return un oggetto di tipo String
		 */
		public String toString(){
			return name+":"+type;
		}
	}
	/**
	 *  Lista delle colonne della tabella
	 */
	List<Column> tableSchema=new ArrayList<Column>();
	
	
	/**
	 * Crea lo schema della tabella tableName.
	 * @param tableName nome della tabella di cui si vuole ottenere lo schema
	 * @throws SQLException
	 */
	public Table_Schema(String tableName) throws SQLException{
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		
	
		 Connection con=DBaccess.getConnection();
		 DatabaseMetaData meta = con.getMetaData();
	     ResultSet res = meta.getColumns(null, null, tableName, null);
		   
	     while (res.next()) 
	     {
	         
	         if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	        		 tableSchema.add(new Column(
	        				 res.getString("COLUMN_NAME"),
	        				 mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
	     }
	      res.close();
	    }
	  
	
	/**
	 * Metodo che restituisce il numero di attributi dello schema
	 * @return valore di tipo int che rappresenta il numero di attrobuti di cui si compone lo schema della tabella
	 */
	public int getNumberOfAttributes()
	{
		return tableSchema.size();
	}
		
	/**
	 * Metodo che restituisce la colonna con indice identificativo l'indice che viene passato come parametro
	 * @param index valore di tipo int 
	 * @return oggetto di tipo Column
	 */
	public Column getColumn(int index)
	{
		return tableSchema.get(index);
	}

		
}

		