package ch.bfh.univoteverifier.runner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner{
	
	private static final Logger logger = Logger.getLogger(SystemSetupRunner.class.getName());

	private ParametersVerification prmVrf;
	
	public SystemSetupRunner(ElectionBoardProxy ebp) {
		super(ebp);

		
		//create the verification classes we want
		prmVrf = new ParametersVerification(ebp);
	}
	
	@Override
	public List<VerificationResult> run() {
		//perform the checks we want
		partialResults.add(prmVrf.vrfPrimeP());
		partialResults.add(prmVrf.vrfPrimeQ());
		partialResults.add(prmVrf.vrfGenerator());
		partialResults.add(prmVrf.vrfSafePrime());
		partialResults.add(prmVrf.vrfParamLen());
		
		return Collections.unmodifiableList(partialResults);
		
	}
}
