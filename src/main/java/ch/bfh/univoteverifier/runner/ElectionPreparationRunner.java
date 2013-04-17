/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class ElectionPreparationRunner extends Runner {


	private static final Logger logger = Logger.getLogger(SystemSetupRunner.class.getName());

	public ElectionPreparationRunner(ElectionBoardProxy ebp) {
		super(ebp);
	}


	@Override
	public List<VerificationResult> run() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	
}
