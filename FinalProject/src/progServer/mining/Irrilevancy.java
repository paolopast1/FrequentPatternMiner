package progServer.mining;
import java.util.*;

/**
 * Interfaccia che stabiliisce la segnatura dei metodi prunePermutations e pruneSingle
 * @author Paolo Pastore 652412
 */
public interface Irrilevancy
{	
	void prunePermutations (LinkedList<FrequentPattern> outFP);
	void pruneSingle (LinkedList<FrequentPattern> outFP);
}
