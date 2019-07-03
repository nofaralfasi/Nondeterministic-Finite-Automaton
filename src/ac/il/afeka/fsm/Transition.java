package ac.il.afeka.fsm;
import java.io.PrintStream;

public class Transition implements Comparable<Transition> {

	public Transition(State fromState, Character symbol, State toState) {
		this.fromState = fromState;
		this.symbol = symbol;
		this.toState = toState;
	}
	
	public State fromState() { return fromState; }
	
	public Character symbol() { return symbol; }
	
	public State toState() { return toState; }
	
	private State fromState;
	private Character symbol;
	private State toState;
	
	public void prettyPrint(PrintStream out) {
		out.print("(");
		fromState.prettyPrint(out);
		out.print(", ");
		if (symbol == Alphabet.EPSILON)
			out.print("\u03B5");
		else
			out.print(symbol);
		out.print(", ");
		toState.prettyPrint(out);
		out.print(")");
	}

	public String encode() {
		return fromState.encode() + "," + (symbol == Alphabet.EPSILON ? "" : symbol) + "," + toState.encode();
	}

	@Override
	public int compareTo(Transition other) {
		
		int result = fromState.compareTo(other.fromState); 
		if (result != 0)
			return result;

		result = symbol.compareTo(other.symbol);		
		if (result != 0)
			return result;
		
		return toState.compareTo(other.toState);
	}
}
