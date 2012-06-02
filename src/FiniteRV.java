import java.util.HashMap;
import java.util.Map;


public class FiniteRV extends RandomVariable {
	
	protected Map<Integer,Double> prb;
	
	public FiniteRV(Map<Integer,Double> probability){
		prb = probability; 
	}

	@Override
	Double probability(Integer point) {
		return prb.get(point);
	}

	@Override
	RandomVariable plus(RandomVariable b) {
		Map<Integer,Double> res;
		Double p;
		Integer val;		
		Map<Integer, Double> prbB;
		
		if(b instanceof FiniteRV){
			prbB = ((FiniteRV) b).prb;
		}else{
			return null;
		}
		res = new HashMap<Integer,Double>();
		for ( Integer i : prb.keySet()) {
			for ( Integer j : prbB.keySet()) {
				val = i+j;
				if(res.containsKey(val)){
					p = res.get(val)+ i*j;
				}else{
					p = i*j*1.0;
				}
				
				res.put(i+j,p);
			}
		}

		
		return new FiniteRV(res);
	}

	@Override
	RandomVariable times(RandomVariable b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	RandomVariable neg() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return prb.toString();
	}

}
