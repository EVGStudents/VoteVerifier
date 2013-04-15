package ch.bfh.univoteverifier.runner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner{
	
	private static final Logger logger = Logger.getLogger(SystemSetupRunner.class.getName());
	
	/**
	 *
	 * @return
	 */
	public VerificationResult vrfEMCert(){
		return CryptoFunc.getVrfRes(VerificationEnum.SETUP_EM_CERT, true);
	}

	/**
	 * 
	 * @return 
	 */
	public VerificationResult vrfSignParamP(){
		return CryptoFunc.getVrfRes(VerificationEnum.SETUP_P_IS_PRIME, prmVrf.vrfPrimeNumber(Config.p));
	}

	/**
	 * 
	 * @return 
	 */
	public VerificationResult vrfSignParamQ(){
		return CryptoFunc.getVrfRes(VerificationEnum.SETUP_Q_IS_PRIME, prmVrf.vrfPrimeNumber(Config.q));
	}

	public VerificationResult vrfSignParamG(){
		return CryptoFunc.getVrfRes(VerificationEnum.SETUP_G_IS_GENERATOR, prmVrf.vrfGenerator(Config.g, Config.p, Config.q));
	}

	/**
	 * 
	 * @return 
	 */
	public VerificationResult vrfSignParamLen(){
		return CryptoFunc.getVrfRes(VerificationEnum.SETUP_PARAM_LEN, prmVrf.vrfParamLen(Config.pLength, Config.qLength, Config.gLength));
	}

	/**
	 * 
	 * @return 
	 */
	public VerificationResult vrfSignParamSafePrime(){
		return CryptoFunc.getVrfRes(VerificationEnum.SETUP_P_IS_SAFE_PRIME, prmVrf.vrfSafePrime(Config.p, Config.q));
	}
	
	@Override
	public void run() {
		
		//notify the observer
		vrfSignParamP();
		vrfSignParamQ();
		vrfSignParamG();
		vrfSignParamLen();
		vrfSignParamSafePrime();
		vrfEMCert();
	}
}
