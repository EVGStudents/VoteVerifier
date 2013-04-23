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
import ch.bfh.univoteverifier.verification.SectionNameEnum;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.List;
//
///**
// *
// * @author snake
// */
public class MixerTallierRunner extends Runner {

	public MixerTallierRunner(ElectionBoardProxy ebp) {
		super(ebp, SectionNameEnum.MIXING_TALLING);
	}




	@Override
	public List<VerificationResult> run() {
		return null;
	}
	
}
