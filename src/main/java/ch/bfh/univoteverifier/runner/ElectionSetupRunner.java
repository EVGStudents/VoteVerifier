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
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;

/**
 * This class represent an ElectionSetupRunner.
 *
 * @author snake
 */
public class ElectionSetupRunner extends Runner {

	private final RSAImplementer rsaImpl;
	private final CertificatesImplementer certImpl;
	private final ParametersImplementer prmImpl;
	private final ElectionBoardProxy ebp;

	/**
	 * Construct an ElectionSetupRunner with a given ElectionBoardProxy.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 */
	public ElectionSetupRunner(ElectionBoardProxy ebp, Messenger msgr) throws CertificateException, ElectionBoardServiceFault {
		super(RunnerName.ELECTION_SETUP, msgr);
		this.ebp = ebp;
		rsaImpl = new RSAImplementer(ebp, runnerName);
		certImpl = new CertificatesImplementer(ebp, runnerName);
		prmImpl = new ParametersImplementer(ebp, runnerName);

	}

	@Override
	public List<VerificationResult> run() {

		try {
			//EA certificate verification
			VerificationResult v1 = certImpl.vrfEACertificate();
			msgr.sendVrfMsg(v1);
			partialResults.add(v1);

			//RSA signature of (id|EA|timestamp) - ToDo decomment when we find the signature
//			VerificationResult v2 = rsaImpl.vrfEACertIDSign();
//			msgr.sendVrfMsg(v2);
//			partialResults.add(v2);

			//RSA signature of (id|descr|l|T|M|timestamp)
			VerificationResult v3 = rsaImpl.vrfBasicParamSign();
			msgr.sendVrfMsg(v3);
			partialResults.add(v3);

			//talliers certificates
			List<VerificationResult> tCertsRes = certImpl.vrfTalliersCertificates();

			for (VerificationResult v : tCertsRes) {
				msgr.sendVrfMsg(v);
				partialResults.add(v);
			}

			//mixers certificates
			List<VerificationResult> mCertsRes = certImpl.vrfMixersCertificates();

			for (VerificationResult v : mCertsRes) {
				msgr.sendVrfMsg(v);
				partialResults.add(v);
			}

			//RSA Signature of (id|Z_T|Z_M|timestamp) - ToDo decomment when we find the signature
//			VerificationResult v4 = rsaImpl.vrfTMCertsSign();
//			msgr.sendVrfMsg(v4);
//			partialResults.add(v4);

			//ElGamal parameters
			BigInteger elGamalP = ebp.getEncryptionParameters().getPrime();
			BigInteger elGamalQ = ebp.getEncryptionParameters().getGroupOrder();
			BigInteger elGamalG = ebp.getEncryptionParameters().getGenerator();

			//prime P
			VerificationResult v5 = prmImpl.vrfPrime(elGamalP, VerificationType.EL_SETUP_ELGAMAL_P);
			msgr.sendVrfMsg(v5);
			partialResults.add(v5);

			//prime Q
			VerificationResult v6 = prmImpl.vrfPrime(elGamalQ, VerificationType.EL_SETUP_ELGAMAL_Q);
			msgr.sendVrfMsg(v6);
			partialResults.add(v6);

			//generator G
			VerificationResult v7 = prmImpl.vrfGenerator(elGamalP, elGamalQ, elGamalG, VerificationType.EL_SETUP_ELGAMAL_G);
			msgr.sendVrfMsg(v7);
			partialResults.add(v7);


			//is P a safe prime
			VerificationResult v8 = prmImpl.vrfSafePrime(elGamalP, elGamalQ, VerificationType.EL_SETUP_ELGAMAL_SAFE_PRIME);
			msgr.sendVrfMsg(v8);
			partialResults.add(v8);

			//is P length enough
			VerificationResult v9 = prmImpl.vrfElGamalParamLen(elGamalP, ebp.getElectionDefinition().getKeyLength());
			msgr.sendVrfMsg(v9);
			partialResults.add(v9);

			//RSA Signature of (id|P|Q|G|timestamp)
			VerificationResult v10 = rsaImpl.vrfElGamalParamSign();
			msgr.sendVrfMsg(v10);
			partialResults.add(v10);

			//Encryption Key Share Proof
			//ToDo

			//Encryption Key Share Signature
			//ToDo

			//Encryption Key
			//ToDo

			//Encryption Key Signature
			//ToDo

			//Election Generator Share Proof
			//ToDo

			//Election Generator Share Proof Signature
			//ToDo

			//Election Generator
			//ToDo

			//Election Generator Signature
			//ToDo







		} catch (ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | InvalidNameException | UnsupportedEncodingException ex) {
			Logger.getLogger(ElectionSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
