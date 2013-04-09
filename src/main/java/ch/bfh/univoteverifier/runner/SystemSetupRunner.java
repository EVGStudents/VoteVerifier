package ch.bfh.univoteverifier.runner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.utils.CryptoUtils;
import java.util.List;

/**
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner{

	public SystemSetupRunner(String eID) {
		super(eID);
	}

	/**
	 * Verify the parameters of the Schnorr's signature
	 * @return List the list of verification performed with the relatives results
	 */
	private void vrfSignParam(){

		result.add(CryptoUtils.getVrfRes(Verification.SETUP_P_IS_PRIME, prmVrf.vrfPrimeNumber(Config.p)));

		result.add(CryptoUtils.getVrfRes(Verification.SETUP_Q_IS_PRIME, prmVrf.vrfPrimeNumber(Config.q)));

		result.add(CryptoUtils.getVrfRes(Verification.SETUP_G_IS_GENERATOR, prmVrf.vrfGenerator(Config.g, Config.p, Config.q)));
	
		result.add(CryptoUtils.getVrfRes(Verification.SETUP_P_IS_SAFE_PRIME, prmVrf.vrfSafePrime(Config.p, Config.q)));
		
		result.add(CryptoUtils.getVrfRes(Verification.SETUP_PARAM_LEN, prmVrf.vrfParamLen(Config.p, Config.q, Config.g, 1024, 256, 1024)));
		
	}

	private void vrfEMCert(){
		result.add(CryptoUtils.getVrfRes(Verification.SETUP_EM_CERT, true));
	}

	@Override
	public List<VerificationResult> run() {
		vrfSignParam();
		vrfEMCert();
		return result;
	}
}
