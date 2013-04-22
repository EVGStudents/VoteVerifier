///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
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
public class ElectionSetupRunner extends Runner{

	public ElectionSetupRunner(ElectionBoardProxy ebp) {
		super(ebp, SectionNameEnum.ELECTION_SETUP);
	}


	@Override
	public List<VerificationResult> run() {
		return null;
	}


	
}
