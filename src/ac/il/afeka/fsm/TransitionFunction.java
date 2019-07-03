package ac.il.afeka.fsm;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TransitionFunction extends TransitionMapping {

	private Map<State, Map<Character, State> > delta;
	
	public TransitionFunction(Set<Transition> transitions) {
		
		Map<State, Map<Character, State> > delta = new HashMap<State, Map<Character, State> >();
		
		for(Transition t : transitions) {
			if (!delta.containsKey(t.fromState()))
					delta.put(t.fromState(), new HashMap<Character, State>());
			delta.get(t.fromState()).put(t.symbol(), t.toState());
		}
		
		this.delta = delta;
	}
	
	public State applyTo(State fromState, Character symbol) {
		return delta.get(fromState).get(symbol);
	}
	
	public boolean maps(State fromState, Character symbol) {
		return delta.containsKey(fromState) && delta.get(fromState).containsKey(symbol);
	}

	public Set<Transition> transitions() {
		
		Set<Transition> transitions = new HashSet<Transition>();
		
		for(Map.Entry<State, Map<Character, State> > p : delta.entrySet()) {
			for(Map.Entry<Character, State> q : p.getValue().entrySet()) {
				transitions.add(new Transition(p.getKey(), q.getKey(), q.getValue()));
			}
		}
		
		return transitions;
	}

	@Override
	public String prettyName() {
		return "\u03B4";
	}

	@Override
	public Set<State> at(State state, Character symbol) {
		Set<State> result = new HashSet<State>();
		if (symbol == Alphabet.EPSILON)
			return result;
		result.add(applyTo(state, symbol));
		return result;
	}
	

	/** Checks that the transition function is total.
	 * 
	 *  @param states	all the states of the DFSM
	 *  @param alphabet	the alphabet of the DFSM
	 * @throws Exception if there is a state that does not have a transition on all the symbols in the machine's alphabet.
	 */
	public void veryifyTotal(Set<State> states, Alphabet alphabet) throws Exception {
		
		for(Character symbol : alphabet) {
			for(State state: states) {
				if (!maps(state, symbol))
					throw new Exception("The transition function is missing a transition from state " + state + " on symbol " + symbol);
			}
		}
	}

	/** Checks that the transition function has no epsilon transitions.
	 * 
	 * @throws Exception if there is an epsilon transition in the function 
	 */
	public void verifyNoEpsilonTransitions() throws Exception {
		
		for(Map<Character, State> m : delta.values()) {
			if (m.keySet().contains(Alphabet.EPSILON))
				throw new Exception("The transition function has an epsilon transition");
		}
	}
}
