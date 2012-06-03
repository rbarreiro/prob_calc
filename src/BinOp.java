
public class BinOp {

	private String op;
	
	public BinOp(String s){
		op = s;
	}
	
	public Integer calc(Integer i, Integer j){
		
		
		if (op.equals("+")) return i+j;
		else if (op.equals("*")) return i*j;
		else return null;
		
	}
	
}
