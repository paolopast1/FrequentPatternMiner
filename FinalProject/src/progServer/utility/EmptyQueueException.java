
package progServer.utility;

public class EmptyQueueException extends RuntimeException {
	EmptyQueueException(){}
	EmptyQueueException(String msg){super(msg);}
}
