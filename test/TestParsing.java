import static org.junit.Assert.*;

import org.junit.Test;

import ac.il.afeka.fsm.DFSM;
import ac.il.afeka.fsm.NDFSM;

public class TestParsing {

	@Test
	public void test() throws Exception {
		
		String encoding = "0 1/a b/0,a,0;0,b,1;1,a,0;1,b,1/0/1";
		
		DFSM aDFSM = new DFSM(encoding);
		
		assertEquals(encoding, aDFSM.encode());
	}
	
	@Test
	public void testNoAcceptingStates() throws Exception {

		String encoding = "0/a b/0,a,0;0,b,0/0/";
		
		DFSM aDFSM = new DFSM(encoding);
		
		assertEquals(encoding, aDFSM.encode());

	}
	
	@Test
	public void testParsingEpsilonTransition() throws Exception {

		String encoding = "0 1/a b/0,,0;0,b,1;1,a,0;1,b,1/0/1";
		
		NDFSM anNDFSM = new NDFSM(encoding);
		
		assertEquals(anNDFSM.encode(), encoding);

	}

	@Test
	public void testTinyMachine() throws Exception {
		
		String encoding = "0/a/0,,0/0/"; 
		NDFSM anNDFSM = new NDFSM(encoding);
		
		assertEquals(anNDFSM.encode(), encoding);
	}
	
	@Test
	
	public void testWithSpaces() throws Exception {
		
		String withSpaces = "0 1 2 3 4 5 6 7/a b/3,b,7; 0,a,1; 7,a,1; 3,a,6; 7,b,0; 4,b,5; 2,b,5; 1,b,3; 0,b,0; 1,a,2; 2,a,4; 4,a,4; 5,b,7; 5,a,6; 6,a,2; 6,b,3/0/4 5 6 7";

		String noSpaces = "0 1 2 3 4 5 6 7/a b/0,a,1;0,b,0;1,a,2;1,b,3;2,a,4;2,b,5;3,a,6;3,b,7;4,a,4;4,b,5;5,a,6;5,b,7;6,a,2;6,b,3;7,a,1;7,b,0/0/4 5 6 7";
		
		NDFSM anNDFSM = new NDFSM(withSpaces);
		
		assertEquals(anNDFSM.encode(), noSpaces);
	}
}
