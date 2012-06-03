
public abstract class RandomVariable {
	
	abstract Double probability(Integer point);
	
	abstract Double prob_comp(Condition c, RandomVariable b);
		
	abstract RandomVariable plus(RandomVariable b);
	
	abstract RandomVariable times(RandomVariable b);
	
	abstract RandomVariable neg();
	
}
