///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.List;
//
//
///**
// *
// * @author snake
// */
public class ElectionPeriodRunner extends Runner {

	public ElectionPeriodRunner(ElectionBoardProxy ebp, String name) {
		super(ebp, name);
	}


	@Override
	public List<VerificationResult> run() {
		return null;
	}


	
}
