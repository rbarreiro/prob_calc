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

	private FiniteRV applyBinOp(BinOp op,FiniteRV b){
		Map<Integer,Double> res;
		Double p;
		Integer val;
		Double pval;
		Map<Integer, Double> prbB;
		

		prbB = b.prb;

		res = new HashMap<Integer,Double>();
		for ( Integer i : prb.keySet()) {
			for ( Integer j : prbB.keySet()) {
				val = op.calc(i,j);
				pval = prb.get(i) * prbB.get(j);
				if(res.containsKey(val)){
					p = res.get(val)+ pval;
				}else{
					p = pval;
				}
				
				res.put(i+j,p);
			}
		}

		
		return new FiniteRV(res);		
	}
	
	@Override
	RandomVariable plus(RandomVariable b) {
		return this.applyBinOp(new BinOp("+"), (FiniteRV)b);
	}

	@Override
	RandomVariable times(RandomVariable b) {
		return this.applyBinOp(new BinOp("*"), (FiniteRV)b);
	}

	@Override
	RandomVariable neg() {
		Map<Integer,Double> res;		

		res = new HashMap<Integer,Double>();
		for ( Integer i : prb.keySet()) {
			res.put(-i,prb.get(i));
		}
	
		return new FiniteRV(res);
	}
	
	@Override
	public String toString() {
		return prb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof FiniteRV){
			return prb.equals(((FiniteRV)o).prb);
		}else{
			return false;
		}
	}


	@Override
	Double prob_comp(Condition c, RandomVariable b) {
		Double res;
		Map<Integer, Double> prbB;
		
		if(b instanceof FiniteRV){
			prbB = ((FiniteRV) b).prb;
		}else{
			return 0.0;
		}
		res = 0.0;
		for ( Integer i : prb.keySet()) {
			for ( Integer j : prbB.keySet()) {
				if(c.test(i,j)){
					res = res + prb.get(i) * prbB.get(j);
				}
			}
		}

		return res;
	}

}
