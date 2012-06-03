import java.io.*;

public class prob_calc {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Object inst;
		Attribution attr;

	    // Defines the standard input stream
	    BufferedReader stdin = new BufferedReader
	      (new InputStreamReader(System.in));
	    String message; // Creates a variable called message for input
	    
	    System.out.print ("Type q to quit.\n");

	    while (true){
		    System.out.print (">");
		    System.out.flush(); // empties buffer, before you input text
		    message = stdin.readLine();
		    
		    if (message.replace(" ","").equals("q")){
		    	break;
		    }
		    
		    
		    try {
			    inst = Grammar.ExprParser.parse(message);

			    if( inst instanceof Attribution){
			    	attr = (Attribution) inst;
			    	Grammar.STATE.put(attr.getId(), attr.getRv());
			    }else{
			    	System.out.println(inst);
			    }
		    } catch (Exception e) {
		    	System.out.println(e);
		    }
		     
		    
	    }	    
	    
	}

}
