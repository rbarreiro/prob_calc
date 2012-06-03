
public class Condition {
	
	private String comparator;
	
	public Condition(String s){
		comparator = s;
	}
	
	public boolean test(Integer i, Integer j){
		
		
		if (comparator.equals("<")) return i<j;
		else if (comparator.equals("<=")) return i<=j;
		else if (comparator.equals(">")) return i>j;
		else if (comparator.equals(">=")) return i>=j;
		else if (comparator.equals("=")) return i==j;
		else return false;
		
	}
	
}
