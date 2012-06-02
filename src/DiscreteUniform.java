import java.util.HashMap;


public class DiscreteUniform extends FiniteRV {

	public DiscreteUniform(int lowerBound, int upperBound){
		super(new HashMap<Integer, Double>());
		Double p = 1/(1.0 + upperBound - lowerBound);
		for (int i = lowerBound; i < upperBound + 1; i = i + 1){
			prb.put(i,p);
		}
		
	}
}
