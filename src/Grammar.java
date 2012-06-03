
import org.codehaus.jparsec.OperatorTable;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Terminals;
import org.codehaus.jparsec.Token;
import org.codehaus.jparsec.functors.Binary;
import org.codehaus.jparsec.functors.Tuple3;
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
		  
		  private static final Terminals OPERATORS = Terminals.operators("u","{","}",",","+", "-", "*", "/", "(", ")");

		  static final Parser<RandomVariable> UNIFORM = 
			Parsers.tuple(Terminals.IntegerLiteral.PARSER,OPERATORS.token(","), Terminals.IntegerLiteral.PARSER)
				.between(OPERATORS.token("u").next(OPERATORS.token("{")), OPERATORS.token("}"))
			.map(				
			new org.codehaus.jparsec.functors.Map<Tuple3<String,Token,String>, RandomVariable>() {
				
		      public RandomVariable map(Tuple3<String,Token,String> s) {
		    	
		        return new DiscreteUniform(Integer.parseInt(s.a),Integer.parseInt(s.c));
		      }
		      
		    });
		  

		  
		  static final Parser<RandomVariable>  RNDVAR= Parsers.or(NUMBER,UNIFORM); 
		  
		  
		  
		  
		  static final Parser<Void> IGNORED =
		      Scanners.WHITESPACES.skipMany();
		      
		  static final Parser<?> TOKENIZER =
		      Parsers.or(
			    		  /*Terminals.Identifier.TOKENIZER,*/
			    		  Terminals.IntegerLiteral.TOKENIZER, 
			    		  OPERATORS.tokenizer()
		    		  );
		
		  
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
		  
		  public static final Parser<RandomVariable> RVExprParser = calculator(RNDVAR).from(TOKENIZER, IGNORED);
		  
		  public static final Parser<RandomVariable> ExprParser = RVExprParser;
	
	
}
