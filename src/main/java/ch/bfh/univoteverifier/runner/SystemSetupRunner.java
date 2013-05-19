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

/**
 * This class represent a the SystemSetupRunner.
 *
 * @author Scalzi Giuseppe
 */
public class SystemSetupRunner extends Runner {

	private final ParametersImplementer paramImpl;
	private final CertificatesImplementer certImpl;
	private final ElectionBoardProxy ebp;

	/**
	 * Construct an SystemSetupRunner with a given ElectionBoardProxy and
	 * Messenger.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public SystemSetupRunner(ElectionBoardProxy ebp, Messenger msgr) {
		super(RunnerName.SYSTEM_SETUP, msgr);
		this.ebp = ebp;

		paramImpl = new ParametersImplementer(ebp, runnerName);
		certImpl = new CertificatesImplementer(ebp, runnerName);
	}

	@Override
	public List<VerificationResult> run() {
		try {
			//is Schnorr p prime
			VerificationResult v1 = paramImpl.vrfPrime(Config.p, VerificationType.SETUP_SCHNORR_P);
			msgr.sendVrfMsg(v1);
			partialResults.add(v1);
			Thread.sleep(1000);

			//is Schnorr q prime
			VerificationResult v2 = paramImpl.vrfPrime(Config.q, VerificationType.SETUP_SCHNORR_Q);
			msgr.sendVrfMsg(v2);
			partialResults.add(v2);
			Thread.sleep(1000);

			//is Schnorr g a generator
			VerificationResult v3 = paramImpl.vrfGenerator(Config.p, Config.q, Config.g, VerificationType.SETUP_SCHNORR_G);
			msgr.sendVrfMsg(v3);
			partialResults.add(v3);
			Thread.sleep(1000);

			//is Schnorr p a safe prime
			VerificationResult v4 = paramImpl.vrfSafePrime(Config.p, Config.q, VerificationType.SETUP_SCHNORR_P_SAFE_PRIME);
			msgr.sendVrfMsg(v4);
			partialResults.add(v4);
			Thread.sleep(1000);

			//are the Schnorr paramters long enough
			VerificationResult v5 = paramImpl.vrfSchnorrParamLen(Config.p, Config.q, Config.g);
			msgr.sendVrfMsg(v5);
			partialResults.add(v5);
			Thread.sleep(1000);

			//verifiy CA certificate
			VerificationResult v6 = certImpl.vrfCACertificate();
			msgr.sendVrfMsg(v6);
			partialResults.add(v6);
			Thread.sleep(1000);

			//verifiy EM certificate
			VerificationResult v7 = certImpl.vrfEMCertificate();
			msgr.sendVrfMsg(v7);
			partialResults.add(v7);

		} catch (InterruptedException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException ex) {
			msgr.sendElectionSpecError(ebp.getElectionID(), ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
