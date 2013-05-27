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
import ch.bfh.univoteverifier.implementer.ProofImplementer;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;
import javax.naming.InvalidNameException;

/**
 * This class represent an ElectionSetupRunner.
 *
 * @author Scalzi Giuseppe
 */
public class ElectionSetupRunner extends Runner {

	private final RSAImplementer rsaImpl;
	private final CertificatesImplementer certImpl;
	private final ParametersImplementer prmImpl;
	private final ProofImplementer proofImpl;
	private final ElectionBoardProxy ebp;

	/**
	 * Construct an ElectionSetupRunner with a given ElectionBoardProxy and
	 * Messenger.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public ElectionSetupRunner(ElectionBoardProxy ebp, Messenger msgr) throws CertificateException, ElectionBoardServiceFault, InvalidNameException {
		super(RunnerName.ELECTION_SETUP, msgr);
		this.ebp = ebp;
		rsaImpl = new RSAImplementer(ebp, runnerName);
		certImpl = new CertificatesImplementer(ebp, runnerName);
		prmImpl = new ParametersImplementer(ebp, runnerName);
		proofImpl = new ProofImplementer(ebp, runnerName);
	}

	@Override
	public List<VerificationResult> run() throws InterruptedException {

		try {
			//EA certificate verification
			VerificationResult v1 = certImpl.vrfEACertificate();
			msgr.sendVrfMsg(v1);
			partialResults.add(v1);
			Thread.sleep(SLEEP_TIME);


			//RSA signature of EA Cert with ID - ToDo when we find the signature
			VerificationResult v2 = rsaImpl.vrfEACertIDSign();
			msgr.sendVrfMsg(v2);
			partialResults.add(v2);
			Thread.sleep(SLEEP_TIME);


			//RSA signature of (id|descr|l|T|M|timestamp)
			VerificationResult v3 = rsaImpl.vrfBasicParamSign();
			msgr.sendVrfMsg(v3);
			partialResults.add(v3);
			Thread.sleep(SLEEP_TIME);


			//talliers certificates
			for (String tName : ebp.getElectionDefinition().getTallierId()) {
				VerificationResult vr = certImpl.vrfTallierCertificate(tName);
				msgr.sendVrfMsg(vr);
				partialResults.add(vr);
				Thread.sleep(SLEEP_TIME);

			}

			//mixers certificates
			for (String mName : ebp.getElectionDefinition().getMixerId()) {
				VerificationResult vr = certImpl.vrfMixerCertificate(mName);
				msgr.sendVrfMsg(vr);
				partialResults.add(vr);
				Thread.sleep(SLEEP_TIME);

			}


			//RSA Signature of Mixers and Talliers Certs with ID - ToDo we find the signature
			VerificationResult v4 = rsaImpl.vrfTMCertsSign();
			msgr.sendVrfMsg(v4);
			partialResults.add(v4);
			Thread.sleep(SLEEP_TIME);


			//ElGamal parameters
			BigInteger elGamalP = ebp.getEncryptionParameters().getPrime();
			BigInteger elGamalQ = ebp.getEncryptionParameters().getGroupOrder();
			BigInteger elGamalG = ebp.getEncryptionParameters().getGenerator();

			//prime P
			VerificationResult v5 = prmImpl.vrfPrime(elGamalP, VerificationType.EL_SETUP_ELGAMAL_P);
			msgr.sendVrfMsg(v5);
			partialResults.add(v5);
			Thread.sleep(SLEEP_TIME);


			//prime Q
			VerificationResult v6 = prmImpl.vrfPrime(elGamalQ, VerificationType.EL_SETUP_ELGAMAL_Q);
			msgr.sendVrfMsg(v6);
			partialResults.add(v6);
			Thread.sleep(SLEEP_TIME);


			//generator G
			VerificationResult v7 = prmImpl.vrfGenerator(elGamalP, elGamalQ, elGamalG, VerificationType.EL_SETUP_ELGAMAL_G);
			msgr.sendVrfMsg(v7);
			partialResults.add(v7);
			Thread.sleep(SLEEP_TIME);


			//is P a safe prime
			VerificationResult v8 = prmImpl.vrfSafePrime(elGamalP, elGamalQ, VerificationType.EL_SETUP_ELGAMAL_SAFE_PRIME);
			msgr.sendVrfMsg(v8);
			partialResults.add(v8);
			Thread.sleep(SLEEP_TIME);


			//is P length enough
			VerificationResult v9 = prmImpl.vrfElGamalParamLen(elGamalP, ebp.getElectionDefinition().getKeyLength());
			msgr.sendVrfMsg(v9);
			partialResults.add(v9);
			Thread.sleep(SLEEP_TIME);


			//RSA Signature of ElGamal paramters
			VerificationResult v10 = rsaImpl.vrfElGamalParamSign();
			msgr.sendVrfMsg(v10);
			partialResults.add(v10);
			Thread.sleep(SLEEP_TIME);


			//Encryption Key Share Proof and Signature
			for (String tName : ebp.getElectionDefinition().getTallierId()) {
				VerificationResult vr = proofImpl.vrfDistributedKeyByProof(tName);
				msgr.sendVrfMsg(vr);
				partialResults.add(vr);
				Thread.sleep(SLEEP_TIME);


				VerificationResult vrSign = rsaImpl.vrfDistributedKeyBySign(tName);
				msgr.sendVrfMsg(vrSign);
				partialResults.add(vrSign);
				Thread.sleep(SLEEP_TIME);

			}

			//Encryption Key
			VerificationResult v11 = prmImpl.vrfDistributedKey();
			msgr.sendVrfMsg(v11);
			partialResults.add(v11);
			Thread.sleep(SLEEP_TIME);


			//Encryption Key Signature
			VerificationResult v12 = rsaImpl.vrfDistributedKeySign();
			msgr.sendVrfMsg(v12);
			partialResults.add(v12);
			Thread.sleep(SLEEP_TIME);


			//Election Generator Share Proof and Signature
			for (int i = 0; i <= ebp.getElectionDefinition().getMixerId().size() - 1; i++) {
				String actualName = ebp.getElectionDefinition().getMixerId().get(i);
				String previousName;

				if (i == 0) {
					previousName = "schnorr_generator";
				} else {
					previousName = ebp.getElectionDefinition().getMixerId().get(i - 1);
				}

				VerificationResult vr = proofImpl.vrfElectionGeneratorByProof(actualName, previousName);
				msgr.sendVrfMsg(vr);
				partialResults.add(vr);
				Thread.sleep(SLEEP_TIME);



				VerificationResult vrSign = rsaImpl.vrfElectionGeneratorBySign(actualName);
				msgr.sendVrfMsg(vrSign);
				partialResults.add(vrSign);
				Thread.sleep(SLEEP_TIME);

			}

			//Election Generator
			VerificationResult v13 = prmImpl.vrfElectionGenerator();
			msgr.sendVrfMsg(v13);
			partialResults.add(v13);
			Thread.sleep(SLEEP_TIME);


			//Election Generator Signature
			VerificationResult v14 = rsaImpl.vrfElectionGeneratorSign();
			msgr.sendVrfMsg(v14);
			partialResults.add(v14);
			Thread.sleep(SLEEP_TIME);


		} catch (ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | InvalidNameException | UnsupportedEncodingException ex) {
			msgr.sendElectionSpecError(ebp.getElectionID(), ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
