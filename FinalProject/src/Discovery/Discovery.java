package Discovery;
import progServer.mining.CPAInterface;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe che modella l'eccezione da lanciare nel caso in cui si inserisca un valore di MinSup non valido
 * @author Paolo Pastore
 *
 */
class NoConsistentMinSupException extends Exception{
	NoConsistentMinSupException(){};
	NoConsistentMinSupException(String msg){super(msg);}
}

/**
 * Classe che modella l'eccezione da lanciare nel caso in cui si inserisca un valore di Epsilon non valido
 * @author Paolo
 *
 */
class NoConsistentEpsException extends Exception{
	NoConsistentEpsException(){};
	NoConsistentEpsException(String msg){super(msg);}
}

/**
 * Classe che estende la classe JApplet che crea un applet client e che si collega al server per inviargli informazioni e tramite la 
 * remote method invocation ottiene informazioni in risposta alle sue richieste.
 * Nella classe viene implementata un'interfaccia grafica e in base ai bottoni e alle selezioni effettuate si otterranno informazioni diverse in risposta
 * @author Paolo Pastore
 *
 */

public class Discovery extends JFrame

{
	/**
	 * oggetto per la gestione dello stream di output per comunicare con il server
	 */
	private PrintWriter out;
	/**
	 * Oggetto per la gestione dello stream di input per comunicare con il server
	 */
	private BufferedReader in;
	/**
	 * Oggetto di tipo JRadioButton per selezionare la modalità “processo di scoperta” da tabella di Database
	 */
	private static JRadioButton db = new JRadioButton("Avviare il processo di ricerca"); 
	/**
	 * Oggetto di tipo JRadioButton per selezionare la modalità "lettura file di backup" da file sul server
	 */
	private static JRadioButton file = new JRadioButton("Avviare la lettura del file di backup");
	/**
	 * Oggetto di tipo JTextField per l'inserimento del nome della tabella da analizzare o del file di backup da caricare
	 */
	private static JTextField nameDataTxt = new JTextField(10); 
	/**
	 * Oggetto di tipo JButton che avvia il thread che invia le informazioni al server e inizia il calcolo dei cp o carica un file esistente
	 */
	private static JButton runConstructioBt = new JButton("Run");
	/**
	 * Oggetto di tipo JTextField per l'inserimento del minSup
	 */
	private static JTextField minSupTxt = new JTextField(3); 
	/**
	 * Oggetto di tipo JTextField per l'inserimento della epsilon
	 */
	private static JTextField epsTxt = new JTextField(3);
	/**
	 * Oggetto di tipo JTextField per la visualizzazione dei risultati
	 */
	private static JTextArea patternsAreaTxt = new JTextArea(10, 80);
	/**
	 * Oggetto di tipo JTextField per la visualizzazione di un messaggio che permette di stabilire se il processo selezionato è andato a buon fine o meno
	 */
	private static JTextArea msgAreaTxt = new JTextArea (1, 15); 
	
	
	/**
	 * Classe che estende la classe Thread che quando viene istanziato avvia un nuovo thread che si occupa di inviare tutte le informazioni al server
	 * e di visualizzare i risultati sull'interfaccia grafica
	 * @author Paolo Pastore 652412
	 *
	 */
	class SeparateSubTask extends Thread{
		SeparateSubTask(){start();}
		public void run(){
			InetAddress addr;
			Socket socket = null;;
			try {
				addr = InetAddress.getByName("127.0.0.1");
				socket = new Socket(addr, 8080);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
				System.out.print("ConnessioneStabilita");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try{
				if(db.isSelected())
				{
					String name = nameDataTxt.getText();
					String minSup = minSupTxt.getText();
					String eps = epsTxt.getText();
					float x = Float.parseFloat(minSup);
					float y = Float.parseFloat(eps);
					//Creazione di una finestra di dialogo nel caso di dati non validi
					if(x < 0.000000 || x > 1.000000)
						throw new NoConsistentMinSupException();
					if(y < 0.000000 || x > 1.000000)
						throw new NoConsistentEpsException();
					//Invio al server di informazioni relative al nome della tabella da analizzare, il minSup ecc..
					out.println('d');
					out.println(name);
					out.println(minSup);
					out.println(eps);
					String tmp = in.readLine();
					//Gestione delle SQLException
					if(tmp != null && tmp.equals("ErrorDB"))
					{
						JOptionPane op = new JOptionPane("Errore!\nTabella non esistente", JOptionPane.WARNING_MESSAGE);
						JDialog dialog = op.createDialog("Errore");
						dialog.setVisible(true);
						msgAreaTxt.setText("NO");
						this.interrupt();
					}
					//Invocazione del metodo in remoto convertToString
					else
					{
						String res = "";
						Registry r = LocateRegistry.getRegistry("127.0.0.1", 2005);
						Object t = (r.lookup("cpaInterface"));
						Method m = t.getClass().getMethods()[3];
						System.out.println(m.getName() + "\n");
						res = (String)m.invoke(t, null);
						patternsAreaTxt.setText(res);
					}
				}
				
				else
				{
					String fileName = nameDataTxt.getText();
					out.println('i');
					out.println(fileName);
					String tmp = in.readLine();
					if(tmp != null && tmp.equals("ErrorConn"))
					{
						JOptionPane op = new JOptionPane("Errore nel caricamento del file", JOptionPane.WARNING_MESSAGE);
						JDialog dialog = op.createDialog("Errore");
						dialog.setVisible(true);
						msgAreaTxt.setText("NO");
						this.interrupt();
					}
					else
					{
						String res = "";
						Registry r = LocateRegistry.getRegistry("127.0.0.1", 2005);
						Object t = r.lookup("cpaInterface");
						Method m = t.getClass().getMethods()[3];
						m.invoke(t, null);
						patternsAreaTxt.setText(res);
					}
				}
				socket.close();
				msgAreaTxt.setText("YES");
			}catch(IOException e)
			{
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			} catch(NumberFormatException e)
			{
				JOptionPane op = new JOptionPane("Si prega di inserire due valori numerici nei campi 'minSup' e 'Epsilon'", JOptionPane.WARNING_MESSAGE);
				JDialog dialog = op.createDialog("Error");
				dialog.setVisible(true);
				msgAreaTxt.setText("NO");
				this.interrupt();
			} catch (NoConsistentEpsException e) {
				JOptionPane op = new JOptionPane("Valore di Epsilon non valido", JOptionPane.WARNING_MESSAGE);
				JDialog dialog = op.createDialog("Errore");
				dialog.setVisible(true);
				msgAreaTxt.setText("NO");
				this.interrupt();
			} catch (NoConsistentMinSupException e) {
				JOptionPane op = new JOptionPane("Errore", JOptionPane.WARNING_MESSAGE);
				JDialog dialog = op.createDialog("Valore di MinSup non valido");
				dialog.setVisible(true);;
				msgAreaTxt.setText("NO");
				this.interrupt();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				JOptionPane op = new JOptionPane("Errore", JOptionPane.WARNING_MESSAGE);
				JDialog dialog = op.createDialog("Errore nell'invocazione di metodi da remoto");
				dialog.setVisible(true);
				msgAreaTxt.setText("NO");
			}
		}
	}
	
	/**
	 * Classe che implementa l'interfaccia ActionListener e che permette di avviare un nuovo thread 
	 * 	 * @author Paolo
	 *
	 */
	class rba implements ActionListener {
		/**
		 * Avvia un nuovo thread quando avviene l'evento e
		 */
		public void actionPerformed(ActionEvent e)
		{
			new SeparateSubTask();
		}
	} 
	
	class wl extends WindowAdapter{
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
	}

	
	/**
	 * Metodo principale della classe che crea l'interfaccia grafica e associa il listener al bottone "run"
	 */
	public Discovery()	
	{
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		JPanel cpDiscoveryMining=new JPanel();
		Border borderTree = BorderFactory.createTitledBorder("Selecting Data Source");
		cpDiscoveryMining.setBorder(borderTree);
		cpDiscoveryMining.setLayout(new BoxLayout (cpDiscoveryMining, BoxLayout.Y_AXIS));
		ButtonGroup group=new ButtonGroup();
		db=new JRadioButton("Discovery patterns from db");
		file =new JRadioButton("Reading patterns from file");
		group.add(db);
		group.add(file);
		db.setSelected(true);
		cpDiscoveryMining.add(db);
		cpDiscoveryMining.add(file);
		cp.add(cpDiscoveryMining);
		JPanel insertData = new JPanel();
		Border borderData = BorderFactory.createTitledBorder("Insert input data");
		insertData.setBorder(borderData);
		insertData.add(new Label("Table name"));
		insertData.add(nameDataTxt);
		insertData.add(new Label("MinSup"));
		insertData.add(minSupTxt);
		insertData.add(new Label("Epsilon"));
		insertData.add(epsTxt);
		cp.add(insertData);
		runConstructioBt.addActionListener(new rba());
		cp.add(runConstructioBt);
		JPanel resultMessage = new JPanel();
		resultMessage.setLayout(new GridLayout(1, 2));
		Border borderResult = BorderFactory.createTitledBorder("Results");
		resultMessage.setBorder(borderResult);
		JScrollPane sp = new JScrollPane(patternsAreaTxt);
		resultMessage.add(sp);
		cp.add(resultMessage);
		JPanel isTerminated = new JPanel();
		isTerminated.setLayout(new GridLayout(1, 2));
		Border borderTerminated = BorderFactory.createTitledBorder("Is terminated successfully?");
		isTerminated.setBorder(borderTerminated);
		isTerminated.add(msgAreaTxt);
		cp.add(isTerminated);
		this.setVisible(true);
		this.setSize(1000, 400);
		this.addWindowListener(new wl());
	}
}