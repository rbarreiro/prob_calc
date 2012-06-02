
public abstract class RandomVariable {
	
	abstract Double probability(Integer point);
	
	abstract RandomVariable plus(RandomVariable b);
	
	abstract RandomVariable times(RandomVariable b);
	
	abstract RandomVariable neg();
	
}
