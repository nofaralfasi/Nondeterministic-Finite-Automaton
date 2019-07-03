import static org.junit.Assert.*;

import org.junit.Test;

import ac.il.afeka.fsm.DFSM;
import ac.il.afeka.fsm.NDFSM;

public class TestCanonicForm {

	@Test
	public void testCanonicFormOfEmptyMachine() throws Exception {
		
		String original = "5/a b/5,b,5;5,a,5/5/";
		String canonic = "0/a b/0,a,0;0,b,0/0/";
		
		assertEquals(canonic, new DFSM(original).toCanonicForm().encode());
	}

	@Test
	public void testCanonicFormNDFMS() throws Exception {

		String original = "5 2 3/a b/5,a,5;5,a,2;5,,3/5/3";
		String canonic = "0 1 2/a b/0,,1;0,a,0;0,a,2/0/1";
		
		assertEquals(canonic, new NDFSM(original).toCanonicForm().encode());

	}
	
}
