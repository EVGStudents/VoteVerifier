package ch.bfh.univoteverifier.runner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Logger;
import javax.security.cert.Certificate;
import sun.security.x509.X509CertImpl;

/**
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner{
	
	private static final Logger logger = Logger.getLogger(SystemSetupRunner.class.getName());
	private RSAPublicKey CaPubKey;	
	
	
	/**
	 * 
	 * @return 
	 */
	public VerificationResult vrfCACert() throws ElectionBoardServiceFault, CertificateException{
		InputStream is = new ByteArrayInputStream(ebproxy.getElectionSystemInfo().getCertificateAuthority().getValue());
		X509Certificate c = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(is);

		//remove the parameters of vrfCert - ToDo	
		return CryptoFunc.getVrfRes(VerificationEnum.SETUP_CA_CERT, prmVrf.vrfCert(new X509CertImpl(), CaPubKey));
	}
	
	/**
	 *
	 * @return
	 */
	public VerificationResult vrfEMCert(){
		return CryptoFunc.getVrfRes(VerificationEnum.SETUP_EM_CERT, true);
	}

	public VerificationResult vrfSignParamSign(){
			return CryptoFunc.getVrfRes(VerificationEnum.SETUP_SIGN_PARAM_SIGN, true);
	
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
