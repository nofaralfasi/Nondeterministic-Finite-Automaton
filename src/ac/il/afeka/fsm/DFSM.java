package ac.il.afeka.fsm;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DFSM extends NDFSM {
	
	/**
	 * Builds a DFSM from a string representation (encoding) 
	 *  
	 * @param encoding	the string representation of a DFSM
	 * @throws Exception if the encoding is incorrect or if it does not represent a deterministic machine
	 */
	public DFSM(String encoding) throws Exception {
		super(encoding);
		transitionFunction().veryifyTotal(states, alphabet);
		transitionFunction().verifyNoEpsilonTransitions();
	}
	
	/**
	 * Build a DFSM from its components
	 * 
	 * @param states			the set of states for this machine
	 * @param alphabet			this machine's alphabet
	 * @param transitions		the transition mapping of this machine
	 * @param initialState		the initial state (must be a member of states)
	 * @param acceptingStates	the set of accepting states (must be a subset of states)
	 * @throws Exception if the components do not represent a deterministic machine
	 */
	public DFSM(Set<State> states, Alphabet alphabet, Set<Transition> transitions, State initialState,
			Set<State> acceptingStates) throws Exception {
		
		super(states, alphabet, transitions, initialState, acceptingStates);
		
		transitionFunction().veryifyTotal(states, alphabet);
		transitionFunction().verifyNoEpsilonTransitions();
	}

	protected DFSM() { super(); }
	
	protected NDFSM create() { return new DFSM(); }
	
	protected TransitionMapping createMapping(Set<Transition> transitions) {
		return new TransitionFunction(transitions);
	}

	protected TransitionFunction transitionFunction() { return (TransitionFunction)transitions; }

	/** Returns a minimal version of this state machine
	 * 
	 * @return a DFSM that recognizes the same language as this machine, but has a minimal number of states.
	 */

	public DFSM minimize() {
		return ((DFSM)removeUnreachableStates()).minimizeWithNoUnreachableStates();
	}
	

	private DFSM minimizeWithNoUnreachableStates() {
	
		Map<State, State> equivalent = equivalentStates();
		
		Set<Transition> minimalTransitions = new HashSet<Transition>();
		
		for(Transition t : transitions.transitions()) {
			
				minimalTransitions.add(new Transition(equivalent.get(t.fromState()), t.symbol(), equivalent.get(t.toState())));
		}
		
		Set<State> minimalAccepting = new HashSet<State>();
		for(State s : acceptingStates) {
			minimalAccepting.add(equivalent.get(s));
		}

		DFSM aDFSM = new DFSM();
		
		aDFSM.states = new HashSet<State>(equivalent.values());
		aDFSM.alphabet = alphabet;
		aDFSM.transitions = new TransitionFunction(minimalTransitions);
		aDFSM.initialState = equivalent.get(initialState);
		aDFSM.acceptingStates = minimalAccepting;
		
		return aDFSM;
	}
	
	// returns a map that maps each state to a representative of their equivalence class.
	
	private Map<State, State> equivalentStates() {

		 Map<State, State> prevEcc = new HashMap<State, State>();

		 Map<State, State> ecc = new HashMap<State, State>();
		 
		 /* We will represent each equivalence classes with a representative member 
and use a dictionary to map each state to this representative.

First we create two equivalence classes, put all the accepting states in the first
and all the non accepting states in the second. */

		 if (!acceptingStates.isEmpty()) {
			 State rep = acceptingStates.iterator().next();
			 for(State state : acceptingStates)
					 ecc.put(state, rep);
		 }
		 
		 Set<State> nonAcceptingStates = new HashSet<State>(states);
		 nonAcceptingStates.removeAll(acceptingStates);
		 
		 if (!nonAcceptingStates.isEmpty()) {
			 State rep = nonAcceptingStates.iterator().next();
			 for(State state : nonAcceptingStates)
				 ecc.put(state, rep);
		 }
		 
/*		The invariant for the following loop is:

		1. for any s -> r association in ecc, s is equivalent to r in prevEcc,
		2. for any input symbol c, the destination of the transition from s on c is equivalent (in prevEcc) to the destiation of the transition from r to c,
		3. for any two values r1, r2 in ecc, they are not equivalent to each other in prevEcc,
		4. all the equivalence classes in prevEcc have a representative in ecc.

*/

		 while(!prevEcc.equals(ecc)) {
		
			 prevEcc = ecc;
			 
			 ecc = new HashMap<State, State>();
			 
/*		To establish the invariant we will set ecc with the associations of the form

		r -> r where r is a representative from prevEcc. 

		This will initially satisfy the invariant because our action establishes 
		condition (4) and conditions (1) and (2) and (3) are correct by induction 
		from the validity of prevEcc."
*/
			 for(State state : prevEcc.values()) {
				 ecc.put(state,  state);
			 }
			 
			 for(State state : states) {
				 
/*		For each state s, we look in ecc for a rep r that is equivalent to s in prevEcc 
		(that is, s's rep in prevEcc is r and for every input they transition to the same 
		equivalence class in prevEcc) and add s to ecc with the same equivalence rep. 
		If no state is equivalent to s, we add s to ecc as its own rep. */
			 
				 Iterator<State> p = ecc.keySet().iterator();
				 State rep = null;
				 boolean equivalent = false;
				 while(p.hasNext() && !equivalent) {
					 rep = p.next();
					 equivalent = equivalentIn(prevEcc, state, rep);
				 }
				 if (equivalent) 
					 ecc.put(state, rep);
				 else
					 ecc.put(state,  state);
			 }
		 }
		 
		 return ecc;
	}
	
	private boolean equivalentIn(Map<State, State> equivRel, State s, State t) {
		if (equivRel.get(s) != equivRel.get(t) )
			return false;
		
		boolean equiv = true;
		Iterator<Character> p = alphabet.iterator();
		Character symbol = null;
		while (p.hasNext() && equiv) {
			symbol = p.next();
			equiv = equivRel.get(transitionFunction().applyTo(s, symbol)) == equivRel.get(transitionFunction().applyTo(t,  symbol));
		}
		
		return equiv;
	}
	
	/** Returns true if and only if input belongs to this machine's language. 
	 * 
	 * @param input a string whose characters are members of this machine's alphabet
	 * @return a boolean that indicates if the input is a member of this machine's language or not
	 */
	public boolean compute(String input) {
		State state = initialState;
		
		for(int i = 0; i < input.length();i ++) 
			state = transitionFunction().applyTo(state, input.charAt(i));
		
		return acceptingStates.contains(state);
	}
	
	public DFSM complement() throws Exception {
		
		Set<State> nonAcceptingStates = new HashSet<State>(states);
		
		nonAcceptingStates.removeAll(acceptingStates);
		
		return new DFSM(states, alphabet, transitions.transitions(), initialState, nonAcceptingStates);
	}
}
