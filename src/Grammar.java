
import org.codehaus.jparsec.OperatorTable;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Terminals;
import org.codehaus.jparsec.functors.Binary;
import org.codehaus.jparsec.functors.Unary;


public class Grammar {

	  enum BinaryOperator implements Binary<RandomVariable> {
		    PLUS {
		      public RandomVariable map(RandomVariable a, RandomVariable b) {
		        return a.plus(b);
		      }
		    },
		    MINUS {
		      public RandomVariable map(RandomVariable a, RandomVariable b) {
		        return a.plus(b.neg());
		      }
		    },
		    MUL {
		      public RandomVariable map(RandomVariable a, RandomVariable b) {
		        return a.times(b);
		      }
		    }
		  }
		  
		  enum UnaryOperator implements Unary<RandomVariable> {
		    NEG {
		      public RandomVariable map(RandomVariable a) {
		        return a.neg();
		      }
		    }
		  }
		  
		  static final Parser<RandomVariable> NUMBER = 
			Terminals.IntegerLiteral.PARSER.map(
			new org.codehaus.jparsec.functors.Map<String, RandomVariable>() {
				
		      public RandomVariable map(String s) {
		    	Integer val = Integer.parseInt(s);
		        return new DiscreteUniform(val,val);
		      }
		      
		    });

		  
		  
		  static final Parser<RandomVariable>  RNDVAR= NUMBER; 
		  
		  private static final Terminals OPERATORS = Terminals.operators("=","+", "-", "*", "/", "(", ")");
		  
		  static final Parser<Void> IGNORED =
		      Scanners.WHITESPACES.skipMany();
		      
		  static final Parser<?> TOKENIZER =
		      Parsers.or(Terminals.Identifier.TOKENIZER, Terminals.IntegerLiteral.TOKENIZER, OPERATORS.tokenizer());
		  
		  static Parser<?> term(String... names) {
		    return OPERATORS.token(names);
		  }
		  
		  
		  static <T> Parser<T> op(String name, T value) {
		    return term(name).retn(value);
		  }
		  
		  static Parser<RandomVariable> calculator(Parser<RandomVariable> atom) {
		    Parser.Reference<RandomVariable> ref = Parser.newReference();
		    Parser<RandomVariable> unit = ref.lazy().between(term("("), term(")")).or(atom);
		    Parser<RandomVariable> parser = new OperatorTable<RandomVariable>()
		        .infixl(op("+", BinaryOperator.PLUS), 10)
		        .infixl(op("-", BinaryOperator.MINUS), 10)
		        .prefix(op("-", UnaryOperator.NEG), 30)
		        .build(unit);
		    ref.set(parser);
		    return parser;
		  }
		  
		  public static final Parser<RandomVariable> ExprParser = calculator(RNDVAR).from(TOKENIZER, IGNORED);	
	
	
}
