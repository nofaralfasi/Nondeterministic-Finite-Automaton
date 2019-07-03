package ac.il.afeka.fsm;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class TransitionMapping {

	public abstract Set<Transition> transitions();
	
	public abstract String prettyName();
	
	public void prettyPrint(PrintStream out) {

		out.print("{");
		
		Iterator<Transition> p = transitions().iterator();
		
		if (p.hasNext()) {
			p.next().prettyPrint(out);
		}
		
		while(p.hasNext()) {
			out.print(", ");
			p.next().prettyPrint(out);
		}
		
		out.print("}");

	}

	public String encode() {
		
		String encoding = "";
		
		List<Transition> transitionsList = new ArrayList<Transition>(transitions());
		Collections.sort(transitionsList);
		
		Iterator<Transition> p = transitionsList.iterator();
		
		if (p.hasNext()) {
			encoding = encoding + p.next().encode();
		}
		
		while(p.hasNext()) {
			encoding = encoding + ";" + p.next().encode();
		}
 		
		return encoding;
	}

	abstract public Set<State> at(State state, Character symbol);

	/** Checks that the transition mapping contains valid states and symbols. 
	 *
	 * @param states the states of the state machine that holds this mapping
	 * @param alphabet the alphabet of the state machine that holds this mapping
	 * @throws Exception if it finds a transition with a state that does not belong to the machine or a symbol that does not belong to the machine's alphabet.
	 */
	public void verify(Set<State> states, Alphabet alphabet) throws Exception {
		
		for(Transition t : transitions()) {
			if (!states.contains(t.fromState())) {
					throw new Exception("Transition mapping contains a state (id " + t.fromState() + ") that is not a part of the state machine.");
			}
				
			if (t.symbol() != Alphabet.EPSILON && !alphabet.contains(t.symbol())) {
					throw new Exception("Transition contains symbol " + t.symbol() +" that is not a part of the machine's alphabet");
			}
			
			if (!states.contains(t.toState())) {
					throw new Exception("Transition mapping contains a state (id " + t.toState() + ") that is not a part of the state machine.");
			}
		}
	}
}
