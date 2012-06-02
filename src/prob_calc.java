import java.io.*;

public class prob_calc {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

	    // Defines the standard input stream
	    BufferedReader stdin = new BufferedReader
	      (new InputStreamReader(System.in));
	    String message; // Creates a varible called message for input
	    
	    System.out.print ("Type q to quit.\n");

	    while (true){
		    System.out.print (">");
		    System.out.flush(); // empties buffer, before you input text
		    message = stdin.readLine();
		    
		    if (message.replace(" ","").equals("q")){
		    	break;
		    }

		    System.out.println(Grammar.ExprParser.parse(message));
	    }	    
	    
	}

}
