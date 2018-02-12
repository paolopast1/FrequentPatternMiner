
package progServer.mining;
import progServer.data.*;
import progServer.main.EmptySetException;
import progServer.utility.EmptyQueueException;
import progServer.utility.Queue;
import java.util.*;
/**
 * Classe che implementa l'algoritmo per la ricerca dei pattern frequenti
 * @author Paolo Pastore 652412
 */
public class FrequentPatternMiner {
	
	/**
	 * Metodo che restituisce la lista dei pattern frequenti della tabella data che hanno un supporto maggiore del minSup passato come parametro
	 * @param data oggetto di classe Data che rappresenta la tabella da cui calcolare i FrequentPattern
	 * @param minSup valore di tipo float che rappresenta il supporto minimo che un pattern deve avere per essere considerato frequente
	 * @return una lista di FrequentPattern
	 * @throws EmptySetException
	 */
	public static LinkedList<FrequentPattern> frequentPatternDiscovery(Data data,float minSup) throws EmptySetException{
		if(data.getNumberOfExemples() == 0)
			throw new EmptySetException();
		Queue<FrequentPattern> fpQueue=new Queue<FrequentPattern>();		
		LinkedList<FrequentPattern> outputFP=new LinkedList<FrequentPattern>();
		//Ciclo che permette di inserire tutte le coppie (attributo valore) purchè abbiano frequenza > minSup
		//Si inseriscono tutti i pattern di lunghezza k = 1
		//Purchè questi siano "frequenti"
		//Si vedono tutte le possibili combinazioni attributo-valore 
		//Se ne calcola la frequenza con il metodo computeSupport
		//Se la frequenza è abbastanza grande si inserisce nella lista
		//Si inserisce anche nella coda perchè ci serirà per calcolare i pattern di lunghezza > 1
		for(int i=0;i<data.getNumberOfAttributes();i++)
		{
			Attributo currentAttribute=data.getAttributo(i); // genera item discreto k=1 con valori attributo discreto-valore discreto possibile di attributo
			if(currentAttribute instanceof AttributoDiscreto)
			{
				for(int j=0;j<((AttributoDiscreto)currentAttribute).getNumeroValoriDistintiAttributi();j++)
				{
					DiscreteItem item=new DiscreteItem((AttributoDiscreto)currentAttribute, ((AttributoDiscreto)currentAttribute).getValue(j));
					FrequentPattern fp=new FrequentPattern(); //genera pattern con questo item e calcola il supporto
					fp.addItem(item);
					fp.setSupport(FrequentPatternMiner.computeSupport(data, fp));
					if(fp.getSupport() > minSup)
					{
						outputFP.add(fp);
						fpQueue.enqueue(fp);
					}
				}
			}
			else if(currentAttribute instanceof AttributoContinuo)
			{
				float tempMin = ((AttributoContinuo)currentAttribute).getMin();
				for(Iterator<Float> it = ((AttributoContinuo) currentAttribute).iterator(); it.hasNext(); )
				{
					float tempMax = it.next();
					ContinuousItem item= new ContinuousItem((AttributoContinuo)currentAttribute,new Interval(tempMin, tempMax));
					FrequentPattern fp=new FrequentPattern();
					fp.addItem(item);
					fp.setSupport(FrequentPatternMiner.computeSupport(data, fp));
					if(fp.getSupport() > minSup)
					{
						outputFP.add(fp);
						fpQueue.enqueue(fp);
					}
					tempMin = tempMax;
				}
			}
		}
		outputFP=expandFrequentPatterns(data,minSup,fpQueue,outputFP);
		return outputFP;
	}
	
	/**
	 * Metodo privato ausiliario che permette di espandere i pattern di lunghezza 1 e di ottenere quindi una lista di pattern con lunghezza >= 1
	 * @param data oggetto di tipo Data che rappresenta la tabella da cui calcolare i frequentPattern
	 * @param minSup valore di tipo float che rappresenta il supporto minimo che un pattern deve avere per essere considerato Frequente
	 * @param fpQueue struttura dati ausiliaria utilizzata per espandere i frequentPattern
	 * @param outputFP lista di FrequentPattern da espandere
	 * @return una lista che contiene tutti i frequentPattern della tabella data, compresi quelli di lunghezza 1
	 */
	private static   LinkedList<FrequentPattern> expandFrequentPatterns(Data data, float minSup, Queue<FrequentPattern> fpQueue,LinkedList<FrequentPattern> outputFP){
		
		while (!fpQueue.isEmpty())
		{
			FrequentPattern fp=(FrequentPattern)fpQueue.first(); //fp da espandere
			try{
				fpQueue.dequeue();
			}catch(EmptyQueueException e)
			{
				e.printStackTrace();
				break;
			}
			
			for(int i=0;i<data.getNumberOfAttributes();i++)
			{
				boolean found=false;
				for(int j=0;j<fp.getPatternLength();j++) //the new item should involve an attribute different form attributes already involved into the items of fp
					if(fp.getItem(j).getAttribute().equals(data.getAttributo(i)))
					{
						found=true;
						break;
					}
				if(!found) //data.getAttribute(i) is not involve into an item of fp
				{
					if(data.getAttributo(i) instanceof AttributoDiscreto)
					{
						for(int j=0;j < ((AttributoDiscreto)data.getAttributo(i)).getNumeroValoriDistintiAttributi();j++)
						{
							DiscreteItem item=new DiscreteItem(
									(AttributoDiscreto)data.getAttributo(i),
									((AttributoDiscreto)(data.getAttributo(i))).getValue(j)
									);
							FrequentPattern newFP=FrequentPatternMiner.refineFrequentPattern(fp,item); //generate refinement
							newFP.setSupport(FrequentPatternMiner.computeSupport(data,newFP));
							//Lo inserisce nella coda solo se il nuovo pattern ha una frequenza abbastanza grande
							if(newFP.getSupport() > minSup)
							{
								outputFP.add(newFP);
								fpQueue.enqueue(newFP);
							}	
						}
					}
					else
					{
						float tempMin = ((AttributoContinuo)data.getAttributo(i)).getMin();
						for(Iterator<Float> it = ((AttributoContinuo)data.getAttributo(i)).iterator(); it.hasNext(); )
						{
							float tempMax = it.next();
							ContinuousItem item = new ContinuousItem((AttributoContinuo)data.getAttributo(i), new Interval(tempMin, tempMax));
							FrequentPattern newFP = FrequentPatternMiner.refineFrequentPattern(fp, item);
							newFP.setSupport(FrequentPatternMiner.computeSupport(data,  newFP));
							if(newFP.getSupport() > minSup)
							{
								outputFP.add(newFP);
								fpQueue.enqueue(newFP);
							}
							tempMin = tempMax;
						}
					}
				}
			}
		}
		return outputFP;
	}
	
	/**
	 * Metodo che calcola il supporto di un pattern frequente
	 * @param data riferimento ad un oggetto di classe Data che rappresenta la tabella che si sta analizzando
	 * @param FP riferimento ad un oggetto di classe FrequentPattern del quale si vuole calcolare il supporto
	 * @return valore di tipo float che rappresenta il supporto del pattern frequente all'interno della tabella
	 */
	static float computeSupport(Data data,FrequentPattern FP)
	{
		int conteggio = 0;
		int num = (data.getNumberOfExemples());
		for(int i = 0; i < data.getNumberOfExemples(); i++)
		{
			boolean found = true;
			for(int k = 0; found == true && k < FP.getPatternLength(); k++)
			{
				if(FP.getItem(k) instanceof DiscreteItem)
				{
					if(!(FP.getItem(k).checkItemCondition(data.getAttributeValue(i, FP.getItem(k).getAttribute().getIndex()))))
					{
						found = false;
					}
				}
				else
				{
					if(!(FP.getItem(k).checkItemCondition(data.getAttributeValue(i, FP.getItem(k).getAttribute().getIndex()))))
					{
						found = false;
					}
				}	
			}
			if(found == true)
				conteggio++;
		}
		return((float)conteggio/num);
	}
	
	/**
	 * Metodo che inserisce un Item ad un FrequentPattern
	 * @param FP riferimento ad un oggetto di classe FrequentPattern al quale si deve aggiungere l'item
	 * @param item riferimento ad un oggetto di classe Item che rappresenta l'item da aggiungere
	 * @return riferimento ad un oggetto di classe FrequentPattern che rappresenta il pattern frequente esteso
	 */
	static FrequentPattern refineFrequentPattern(FrequentPattern FP, Item item)
	{
		FrequentPattern temp = new FrequentPattern();
		for(int i = 0; i < FP.getPatternLength(); i++)
		{
			temp.addItem(FP.getItem(i));
		}
		temp.addItem(item);
		return temp;
	}

}


	
