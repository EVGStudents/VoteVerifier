/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.runnertest;

import ch.bfh.univote.common.Choice;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.runner.ResultsRunner;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  * Test the runner of the results.
 *
 * @author Scalzi Giuseppe
 */
public class ResultsRunnerTest {

	ResultsRunner rr;

	public ResultsRunnerTest() throws FileNotFoundException {
		rr = new ResultsRunner(new ElectionBoardProxy("vsbfh-2013", true), new Messenger());
	}

	/**
	 * Test the result of an election.
	 */
	@Test
	public void testGetResults() throws ElectionBoardServiceFault {
		//build a list of precomputed vote count for each choice ID
		Map<Integer, Integer> precomputedVotesCount = new LinkedHashMap();

		//pre computed vote count per choice - these comes from the cvs file for the election of BFH
		int[] voteCount = {44, 105, 109, 88, 93, 87, 99, 12, 105, 9, 67, 80, 29, 91, 134, 84, 60, 137, 129, 122, 126, 135, 89, 130, 15, 87, 82, 31, 113, 113, 108, 115};

		for (int i = 0; i < 32; i++) {
			precomputedVotesCount.put(i, voteCount[i]);
		}

		Map<Choice, Integer> er = rr.runResults();

		for (Map.Entry<Choice, Integer> e : er.entrySet()) {
			Choice c = e.getKey();
			Integer count = e.getValue();

			//check if the voute count per choice is correct
			assertEquals(count, precomputedVotesCount.get(c.getChoiceId() - 1));
		}

	}
}
