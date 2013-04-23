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
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.runner.ElectionPeriodRunner;
import ch.bfh.univoteverifier.runner.ElectionPreparationRunner;
import ch.bfh.univoteverifier.runner.ElectionSetupRunner;
import ch.bfh.univoteverifier.runner.MixerTallierRunner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;

/**
 *
 * @author snake
 */
public class UniversalVerification extends Verification{


	/**
	 * Construct a universal verification with an election id
	 * @param eID the ID of the election
	 * @param gm the entity used to exchange messages with the GUI
	 */	
	public UniversalVerification(String eID) {
		super(eID);

		//initialize the runners based on the order 
		if(VerificationEnum.ORDER_BY_ENTITES == displayType){
			createRunnerByEntities();
		}
		else if(VerificationEnum.ORDER_BY_SPEC == displayType){
			createRunnerBySpec();
		}
	}

	private void createRunnerBySpec() {
		SystemSetupRunner ssr = new SystemSetupRunner(ebproxy,gm);
		ElectionSetupRunner esr = new ElectionSetupRunner(ebproxy);
		ElectionPreparationRunner epr = new ElectionPreparationRunner(ebproxy);
		ElectionPeriodRunner eperiodr = new ElectionPeriodRunner(ebproxy);
		MixerTallierRunner mtr = new MixerTallierRunner(ebproxy);

		runners.add(ssr);
//		runners.add(esr);
//		runners.add(epr);
//		runners.add(eperiodr);
//		runners.add(mtr);
	}

	private void createRunnerByEntities() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	
}
