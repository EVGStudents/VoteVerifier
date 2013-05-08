/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represent a MixerTallierRunner.
 *
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner {

	private final ParametersImplementer paramImpl;
	private final CertificatesImplementer certImpl;

	/**
	 * Construct an SystemSetupRunner with a given Messenger.
	 *
	 */
	public SystemSetupRunner(ElectionBoardProxy ebp, Messenger msgr) {
		super(RunnerName.SYSTEM_SETUP, msgr);

		//create the implementer we want
		paramImpl = new ParametersImplementer(ebp, runnerName);
		certImpl = new CertificatesImplementer(ebp, runnerName);
	}

	@Override
	public List<VerificationResult> run() {

		try {
			//ToDo add comments
			VerificationResult v1 = paramImpl.vrfPrime(Config.p, VerificationType.SETUP_SCHNORR_P);
			msgr.sendVrfMsg(v1);
			Thread.sleep(1000);

			VerificationResult v2 = paramImpl.vrfPrime(Config.q, VerificationType.SETUP_SCHNORR_Q);
			msgr.sendVrfMsg(v2);
			Thread.sleep(1000);

			VerificationResult v3 = paramImpl.vrfGenerator(Config.p, Config.q, Config.g, VerificationType.SETUP_SCHNORR_G);
			msgr.sendVrfMsg(v3);
			Thread.sleep(1000);

			VerificationResult v4 = paramImpl.vrfSafePrime(Config.p, Config.q, VerificationType.SETUP_SCHNORR_P_SAFE_PRIME);
			msgr.sendVrfMsg(v4);
			Thread.sleep(1000);

			VerificationResult v5 = paramImpl.vrfSchnorrParamLen(Config.p, Config.q, Config.g);
			msgr.sendVrfMsg(v5);
			Thread.sleep(1000);

			VerificationResult v6 = certImpl.vrfCACertificate();
			msgr.sendVrfMsg(v6);
			Thread.sleep(1000);
			Thread.sleep(1000);

			VerificationResult v7 = certImpl.vrfEMCertificate();
			msgr.sendVrfMsg(v7);

			//cache the results
			partialResults.add(v1);
			partialResults.add(v2);
			partialResults.add(v3);
			partialResults.add(v4);
			partialResults.add(v5);
			partialResults.add(v6);
			partialResults.add(v7);
		} catch (InterruptedException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException ex) {
			Logger.getLogger(SystemSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
