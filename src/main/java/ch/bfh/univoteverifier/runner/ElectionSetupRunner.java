/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.SectionNameEnum;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.util.List;
//
///**
// *
// * @author snake
// */
public class ElectionSetupRunner extends Runner{

	public ElectionSetupRunner(ElectionBoardProxy ebp) {
		super(ebp, SectionNameEnum.ELECTION_SETUP);
	}


	@Override
	public List<VerificationEvent> run() {
		return null;
	}


	
}
