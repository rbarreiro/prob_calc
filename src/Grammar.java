
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jparsec.OperatorTable;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Terminals;
import org.codehaus.jparsec.Token;
import org.codehaus.jparsec.functors.Binary;
import org.codehaus.jparsec.functors.Tuple3;
import org.codehaus.jparsec.functors.Tuple4;
import org.codehaus.jparsec.functors.Unary;



public class Grammar {
	
	  public static Map<String, RandomVariable> STATE = new HashMap<String, RandomVariable>();

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
		  
		  private static final Terminals OPERATORS = 
				  Terminals.operators("P","U","=","let","<=",">=",">","<","{","}",",","+", "-", "*", "/", "(", ")");


		  static final Parser<RandomVariable> UNIFORM = 
			Parsers.tuple(Terminals.IntegerLiteral.PARSER,OPERATORS.token(","), Terminals.IntegerLiteral.PARSER)
				.between(OPERATORS.token("U").next(OPERATORS.token("{")), OPERATORS.token("}"))
			.map(				
			new org.codehaus.jparsec.functors.Map<Tuple3<String,Token,String>, RandomVariable>() {
				
		      public RandomVariable map(Tuple3<String,Token,String> s) {
		    	
		        return new DiscreteUniform(Integer.parseInt(s.a),Integer.parseInt(s.c));
		      }
		      
		    });
		  
		  static final Parser<RandomVariable> RVIDENT = 
		    Terminals.Identifier.PARSER
			.map(				
			new org.codehaus.jparsec.functors.Map<String, RandomVariable>() {
				
		      public RandomVariable map(String s) {
		    	
		        return STATE.get(s);
		      }
		      
		    });
		  
		  static final Parser<RandomVariable>  RNDVAR= Parsers.or(NUMBER,UNIFORM,RVIDENT); 
		  
		  
		  
		  
		  static final Parser<Void> IGNORED =
		      Scanners.WHITESPACES.skipMany();
		      
		  static final Parser<?> TOKENIZER =
		      Parsers.or(
			    		  Terminals.IntegerLiteral.TOKENIZER, 
			    		  OPERATORS.tokenizer(),
			    		  Terminals.Identifier.TOKENIZER
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
		  
		  public static final Parser<RandomVariable> RVExprParser = calculator(RNDVAR);
		  
		  
		  static final Parser<Attribution> ATTR = 
			Parsers.tuple(OPERATORS.token("let"),Terminals.Identifier.PARSER, 
					OPERATORS.token("="), RVExprParser)
			.map(				
			new org.codehaus.jparsec.functors.Map<Tuple4<Token,String,Token,RandomVariable>, Attribution>() {
				
		      public Attribution map(Tuple4<Token,String,Token,RandomVariable> s) {
		    	
		        return new Attribution(s.b, s.d);
		      }
		      
		    });
		  
		  static final Parser<Token> PredOP = Parsers.or(OPERATORS.token("<"),
				  OPERATORS.token(">"),OPERATORS.token("<="),OPERATORS.token(">="),OPERATORS.token("="));
		  
		  static final Parser<Double> Pred =
				  Parsers.tuple(RVExprParser, PredOP, RVExprParser)
			.map(				
			new org.codehaus.jparsec.functors.Map<Tuple3<RandomVariable,Token,RandomVariable>, Double>() {
				
		      public Double map(Tuple3<RandomVariable,Token,RandomVariable> s) {
		    	  return s.a.prob_comp(new Condition(s.b.toString()), s.c);
		      }

		    });	
		  
		  static final Parser<Double> Prob = 
		    Pred.between(OPERATORS.token("P").next(OPERATORS.token("(")), OPERATORS.token(")"));  

		  
		  public static final Parser<?> ExprParser = Parsers.or(Prob,ATTR,RVExprParser).from(TOKENIZER, IGNORED);
	
	
}
