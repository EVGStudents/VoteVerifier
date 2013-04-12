package ch.bfh.univoteverifier.runner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.utils.CryptoUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner{
	
	private static final Logger logger = Logger.getLogger(SystemSetupRunner.class.getName());
	
	private void vrfEMCert(){
		result.add(CryptoUtils.getVrfRes(Verification.SETUP_EM_CERT, true));
	}

	/**
	 * 
	 * @return 
	 */
	private VerificationResult vrfSignParamP(){
		return CryptoUtils.getVrfRes(Verification.SETUP_P_IS_PRIME, prmVrf.vrfPrimeNumber(Config.p));
	}

	/**
	 * 
	 * @return 
	 */
	private VerificationResult vrfSignParamQ(){
		return CryptoUtils.getVrfRes(Verification.SETUP_Q_IS_PRIME, prmVrf.vrfPrimeNumber(Config.q));
	}

	private VerificationResult vrfSignParamG(){
		return CryptoUtils.getVrfRes(Verification.SETUP_G_IS_GENERATOR, prmVrf.vrfGenerator(Config.g, Config.p, Config.q));
	}

	/**
	 * 
	 * @return 
	 */
	private VerificationResult vrfSignParamLen(){
		return CryptoUtils.getVrfRes(Verification.SETUP_P_IS_SAFE_PRIME, prmVrf.vrfSafePrime(Config.p, Config.q));
	}

	/**
	 * 
	 * @return 
	 */
	private VerificationResult vrfSignParamSafePrime(){
		return CryptoUtils.getVrfRes(Verification.SETUP_PARAM_LEN, prmVrf.vrfParamLen(Config.p.bitLength(), Config.q.bitLength(), Config.g.bitLength()));
	}
	
	@Override
	public List<VerificationResult> run() {
		if(super.eID == null) {
			logger.log(Level.SEVERE, "The election ID is empty{0}", eID);
		}
		
		//notify the observer
		vrfSignParamP();
		vrfSignParamQ();
		vrfSignParamG();
		vrfSignParamLen();
		vrfSignParamSafePrime();
		vrfEMCert();
		return result;
	}
}
