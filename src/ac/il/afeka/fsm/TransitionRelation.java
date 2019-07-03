package ac.il.afeka.fsm;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TransitionRelation extends TransitionMapping {

	private Map<State, Set<Transition> > transitions;

	public TransitionRelation(Set<Transition> transitions) {
		
		this.transitions = new HashMap<State, Set<Transition> >();
		
		for(Transition t : transitions) {
			if (!this.transitions.containsKey(t.fromState()))
				this.transitions.put(t.fromState(), new HashSet<Transition>());
			
			this.transitions.get(t.fromState()).add(t);
		}
	}

	public Set<Transition> transitions() { 
		Set<Transition> result = new HashSet<Transition>();
		for(Set<Transition> s : transitions.values()) 
			result.addAll(s);
		return result;
	}

	@Override
	public String prettyName() {
		return "\u0394";
	}

	@Override
	public Set<State> at(State state, Character symbol) {
		Set<State> result = new HashSet<State>();
		if (!transitions.containsKey(state))
			return result;
		for(Transition t : transitions.get(state)) 
			if (t.symbol().equals(symbol))
				result.add(t.toState());
		
		return result;
	}
}
