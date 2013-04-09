package ch.bfh.univoteverifier.verification;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.utils.CryptoUtils;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author snake
 */
public class SystemSetupRunner {

	private final PrimitivesVerifier prmVrf = new PrimitivesVerifier();
	
	/**
	 * Verify the parameters of the Schnorr's signature
	 * @return List the list of verification performed with the relatives results
	 */
	public List vrfSignParam(){
		List<VerificationResult> res = new ArrayList<>();

		res.add(CryptoUtils.getVrfRes(Verification.SETUP_P_IS_PRIME, prmVrf.vrfPrimeNumber(Config.p)));

		res.add(CryptoUtils.getVrfRes(Verification.SETUP_Q_IS_PRIME, prmVrf.vrfPrimeNumber(Config.q)));

		res.add(CryptoUtils.getVrfRes(Verification.SETUP_G_IS_GENERATOR, prmVrf.vrfGenerator(Config.g, Config.p, Config.q)));
	
		res.add(CryptoUtils.getVrfRes(Verification.SETUP_P_IS_SAFE_PRIME, prmVrf.vrfSafePrime(Config.p, Config.q)));
		
		res.add(CryptoUtils.getVrfRes(Verification.SETUP_PARAM_LEN, prmVrf.vrfParamLen(Config.p, Config.q, Config.g, 1024, 256, 1024)));
		
		return res;
	}

	public List vrfEMCert(){
		
		List<VerificationResult> res = new ArrayList<>();

		return res;

	}
}
