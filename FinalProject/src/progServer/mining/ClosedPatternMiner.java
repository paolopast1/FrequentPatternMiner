package progServer.mining;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Classe per la scoperta dei pattern chiusi e dei relativi ridondanti
 * @author Paolo Pastore 652412
 *
 */
public class ClosedPatternMiner implements Irrilevancy {

	/**
	 *Metodo che identifica il pattern con più items e restituisce la lunghezza
	 * @param outFP lista di FrequentPattern
	 * @return valore di tipo int che rappresenta la lunghezza massima dei pattern frequenti
	 */
	private static int getMaxLength(LinkedList<FrequentPattern> outFP)
	{
		int max = -1;
		for(FrequentPattern a: outFP)
		{
			if(a.getPatternLength() > max)
				max = a.getPatternLength();
		}
		return max;
	}
	
	/**
	 * Metodo che verifica se i pattern rendono vera la condizione di chiusura rispetto agli items contenuti.
	 * @param current oggetto di classe FrequentPattern che rappresenta il pattern frequente con il quale "Examined" si sta confrontando
	 * @param examined oggetto di classe FrequentPattern che rappresenta il pattern frequente che si sta analizzando
	 * @return true se examined è coperto da current, false altrimenti
	 */
	private static boolean isCoveredByItems(FrequentPattern current,FrequentPattern examined)
	{
		if(examined.getPatternLength() >= current.getPatternLength())
			return false;
		else
		{
			boolean sottoinsieme = true;
			Iterator<Item> i;
			for(i = examined.iterator(); i.hasNext(); )
			{
				Item temp = i.next();
				boolean trovato = false;
				for(Iterator<Item> j = current.iterator(); j.hasNext() && trovato == false; )
				{
					Item temp1 = j.next();
					if(temp instanceof DiscreteItem && temp1 instanceof DiscreteItem)
					{
						if(temp1.checkItemCondition(temp.getValue()))
							trovato = true;
					}
					else if(temp instanceof ContinuousItem && temp1 instanceof ContinuousItem)
					{
						if(temp1.checkItemCondition(temp.getValue()))
							trovato = true;
					}
				}
				if(trovato == false)
				{
					sottoinsieme = false;
					break;
				}
			}
			return sottoinsieme;
		}
		
	}
	
	/**
	 * Metodo che verifica se i pattern rendono vera la condizione di chiusura rispetto al supporto.
	 * @param current oggetto di classe FrequentPattern
	 * @param examined oggetto di classe FrequentPattern
	 * @param epsilon valore di tipo float
	 * @return true se la differenza fra i supporti dei due pattern è minore di epsilon (in modulo), false altrimenti
	 */
	private static boolean isCoveredBySupport(FrequentPattern current,FrequentPattern examined, float epsilon)
	{
		if(Math.abs(current.getSupport() - examined.getSupport()) < epsilon)
			return true;
		else
			return false;
	}
	
	

	/**
	 * Metodo statico che si occupa della scoperta dei pattern chiusi
	 * @param outFP lista dei pattern frequenti
	 * @param epsilon valore di tipo float che serve per il calcolo dei pattern chiusi
	 * @return oggetto di tipo ClosedPatternArchive che rappresenta i pattern chiusi con i relativi ridondanti, calcolati a partire dalla lista di frequent pattern fornita come argomento
	 * @throws RemoteException
	 */
//	1. Ciclo che inserisce tutti gli elementi di outFP in una nuova lista e cancella tutti gli elementi che sono coperti dall'elemento appena inserito
//	2. inserisci i pattern chiusi nella tabella hash come chiavi
//	3. per ogni elemento chiave nella tabella hash cerca i ridondanti
	public static ClosedPatternArchive closedPatternDiscovery(LinkedList<FrequentPattern> outFP, float epsilon) throws RemoteException
	{
		ClosedPatternArchive ris = new ClosedPatternArchive();
		LinkedList<FrequentPattern> patternChiusi = new LinkedList<FrequentPattern>();
		ClosedPatternMiner temp = new ClosedPatternMiner();
		
		//Eliminazione di pattern di lunghezza 1
		temp.pruneSingle(outFP);
		//Eliminazione delle varie permutazioni dei pattern
		temp.prunePermutations(outFP);
		
		//punto 1
		for(FrequentPattern a: outFP)
		{
			patternChiusi.add(a);
			for(FrequentPattern b: outFP)
			{
				if(a != b)
				{
					if(isCoveredBySupport(a,b,epsilon) && isCoveredByItems(a,b))
					{
						patternChiusi.add(a);
						patternChiusi.remove(b);
					}
				}
			}
				
		}
		
		System.out.println("\n\n\n\n");
		//punto 2
		for(FrequentPattern a: patternChiusi)
		{
			ris.put(a);
		}
		
		//punto 3
		for(FrequentPattern a: ris.archive.keySet())
		{
			for(FrequentPattern b: outFP)
			{
				if(a != b && isCoveredBySupport(a,b,epsilon) && isCoveredByItems(a,b))
				{
					ris.put(a,b);
				}
			}
		}
		return ris;
	}

	/**
	 * Metodo che elimina i Frequent Pattern già presenti nella lista outFP ma in ordine diverso in modo tale che alla fine non esistano pattern frequenti con gli stessi items disposti in maniera diversa
	 *@param outFP Lista dei pattern frequenti
	 */
	public void prunePermutations (LinkedList<FrequentPattern> outFP)
	{	
		for(Iterator<FrequentPattern> it = outFP.iterator(); it.hasNext(); )
		{
			FrequentPattern temp = it.next();
			if(isPresent(temp, outFP))
					it.remove();
		}
		
	}
	/**
	 * Metodo che elimina dalla lista i pattern frequenti tutti i pattern di lunghezza 1
	 * @param outFP lista dei pattern frequenti
	 */
	public void pruneSingle (LinkedList<FrequentPattern> outFP)
	{
		for(Iterator<FrequentPattern> it = outFP.iterator(); it.hasNext(); )
		{
			if(it.next().getPatternLength() == 1)
			{
				it.remove();
			}
		}
	}
	
	/**
	 * metodo privato ausiliario che controlla che un pattern non sia già presente nella lista
	 * @param fp oggetto di tipo FrequentPattern che rappresenta il pattern frequente che si sta analizzando
	 * @param outputFP lista dei patternFrequenti nella quale si va a ricercare fp
	 * @return true se fp è già presente nella lista, false altrimenti
	 */
	private static boolean isPresent(FrequentPattern fp, LinkedList<FrequentPattern> outputFP)
	{
		//se trova due valori uguali del pattern ma in ordine diverso allora uguali = true ed esce dal ciclo
		boolean uguali = false;
		FrequentPattern temp;
		for(Iterator<FrequentPattern> it = outputFP.iterator(); it.hasNext() && uguali == false; )
		{
			uguali = true;
			temp = it.next();
			//Se le lunghezze dei pattern sono diverse allora saranno ovviamente diversi anche i pattern in considerazione
			if(temp.getPatternLength() != fp.getPatternLength())
				uguali = false;
			//Caso in cui le lunghezze sono uguali
			else
			{
				for(int i = 0; i < temp.getPatternLength() && uguali == true; i++)
				{
					boolean trovato = false;
					for(int j = 0; j < fp.getPatternLength() && trovato == false; j++)
					{
						if(temp.getItem(i) instanceof DiscreteItem && fp.getItem(j) instanceof DiscreteItem)
						{
							if(temp.getItem(i).checkItemCondition(fp.getItem(j).getValue()))
								trovato = true;
						}
						else if(temp.getItem(i) instanceof ContinuousItem && fp.getItem(j) instanceof ContinuousItem)
						{
							if(temp.getItem(i).checkItemCondition(fp.getItem(j).getValue()))
								trovato = true;
						}
					}
					//Se non si trova uno stesso valore nel pattern significa che i pattern sono sicuramente diversi 
					if(trovato == false || temp == fp)
						uguali = false;
				}
			}
		}
		return uguali;
	}
}
