package ch.bfh.univoteverifier.runner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.GUIMessenger;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner{
	
	private static final Logger logger = Logger.getLogger(SystemSetupRunner.class.getName());
	private final ParametersImplementer prmVrf;
	private final SectionNameEnum sne = SectionNameEnum.SYSTEM_SETUP;
	private GUIMessenger gm;
	
	public SystemSetupRunner(ElectionBoardProxy ebp, String name) {
		super(ebp, name);
		
		//create the verification classes we want
		prmVrf = new ParametersImplementer(ebp);
		gm = new GUIMessenger();
	}
	
	@Override
	public List<VerificationResult> run() {
		//perform the checks we want - pay attention to exceptions!
		VerificationResult v1 = prmVrf.vrfPrimeP();
		VerificationResult v2 = prmVrf.vrfPrimeQ();
		VerificationResult v3 = prmVrf.vrfGenerator();
		VerificationResult v4 = prmVrf.vrfSafePrime();
		VerificationResult v5 = prmVrf.vrfParamLen();

		//cache the results	
		partialResults.add(v1);
		partialResults.add(v2);
		partialResults.add(v3);
		partialResults.add(v4);
		partialResults.add(v5);
		
		//set the section name and notify the observer
		//maybe pay attention: if something goes wrong before we don't have the section
		//name
		for(VerificationResult vr : partialResults){
			vr.setSectionName(sne);
			gm.sendVrfMsg(vr);
			//notify observer
		}
		
		
		return Collections.unmodifiableList(partialResults);
	}
}
