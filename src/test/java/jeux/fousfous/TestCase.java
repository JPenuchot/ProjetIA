package fousfous;

import org.junit.Test;
import org.junit.Assert;

public class TestCase{
	@Test
	public void testStateToString(){
		System.out.println("Testing state to string interoperability...");

		Case testCase = new Case(0, 0);

		Assert.assertTrue(testCase.getStateAsString().equals("-"));

		Assert.assertTrue(Case.stateToString(State.empty).equals("-"));
		Assert.assertTrue(Case.stateToString(State.black).equals("b"));
		Assert.assertTrue(Case.stateToString(State.white).equals("r"));

		Assert.assertTrue(Case.stringToState("-") == State.empty);
		Assert.assertTrue(Case.stringToState("b") == State.black);
		Assert.assertTrue(Case.stringToState("r") == State.white);
	}
}