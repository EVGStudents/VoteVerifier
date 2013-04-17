/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

	public UniversalVerification(String eID) {
		super(eID);
	}

	@Override
	protected void createRunnerBySpec() {
		SystemSetupRunner ssr = new SystemSetupRunner(ebproxy);
		ElectionSetupRunner esr = new ElectionSetupRunner(ebproxy);
		ElectionPreparationRunner epr = new ElectionPreparationRunner(ebproxy);
		ElectionPeriodRunner eperiodr = new ElectionPeriodRunner(ebproxy);
		MixerTallierRunner mtr = new MixerTallierRunner(ebproxy);

		runners.add(ssr);
		runners.add(esr);
		runners.add(epr);
		runners.add(eperiodr);
		runners.add(mtr);
	}

	@Override
	protected void createRunnerByEntities() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	
}
