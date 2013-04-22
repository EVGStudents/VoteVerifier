package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.GUIMessenger;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner{
	
	private static final Logger logger = Logger.getLogger(SystemSetupRunner.class.getName());
	private final ParametersImplementer prmVrf;
	private final GUIMessenger gm;
	
	public SystemSetupRunner(ElectionBoardProxy ebp, String name, GUIMessenger gm) {
		super(ebp, name);
		
		//create the verification classes we want
		prmVrf = new ParametersImplementer(ebp);
		this.gm = gm;
	}
	
	@Override
	public List<VerificationResult> run() {
		//perform the checks we want - pay attention to exceptions!
		VerificationResult v1 = prmVrf.vrfPrimeP();
		gm.sendVrfMsg(v1);
		
		VerificationResult v2 = prmVrf.vrfPrimeQ();
		gm.sendVrfMsg(v2);

		VerificationResult v3 = prmVrf.vrfGenerator();
		gm.sendVrfMsg(v3);

		VerificationResult v4 = prmVrf.vrfSafePrime();
		gm.sendVrfMsg(v4);

		VerificationResult v5 = prmVrf.vrfParamLen();
		gm.sendVrfMsg(v5);

		//cache the results	
//		partialResults.add(v1);
//		partialResults.add(v2);
//		partialResults.add(v3);
//		partialResults.add(v4);
//		partialResults.add(v5);
		
		//set the section name and notify the observer
		//maybe pay attention: if something goes wrong before we don't have the section
		//name
//		for(VerificationResult vr : partialResults){
//			vr.setSectionName(SectionNameEnum.SYSTEM_SETUP);
//			
//			//notify observer
//			gm.sendVrfMsg(vr);
//		}
//		
		return Collections.unmodifiableList(partialResults);
	}
}
