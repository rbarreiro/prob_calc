
public class Attribution {
	private String id;
	private RandomVariable rv;
	
	
	public Attribution(String name, RandomVariable randomVariable){
		id = name;
		rv = randomVariable;
	}


	public String getId() {
		return id;
	}


	public RandomVariable getRv() {
		return rv;
	}


}
