/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Rule;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.runner.ElectionPeriodRunner;
import ch.bfh.univoteverifier.runner.ElectionPreparationRunner;
import ch.bfh.univoteverifier.runner.ElectionSetupRunner;
import ch.bfh.univoteverifier.runner.MixerTallierRunner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.naming.InvalidNameException;

/**
 * This class is used to perform an universal verification.
 *
 * @author Scalzi Giuseppe
 */
public class UniversalVerification extends Verification {

	/**
	 * Construct a universal verification with an election id.
	 *
	 * @param eID the ID of the election.
	 * @param msgr the entity used to exchange messages with the GUI.
	 */
	public UniversalVerification(Messenger msgr, String eID) {
		super(msgr, eID);
	}

	@Override
	public void createRunners() {
		try {
			SystemSetupRunner ssr = new SystemSetupRunner(ebproxy, msgr);
			ElectionSetupRunner esr = new ElectionSetupRunner(ebproxy, msgr);
			ElectionPreparationRunner epr = new ElectionPreparationRunner(ebproxy, msgr);
			ElectionPeriodRunner eperiodr = new ElectionPeriodRunner(ebproxy, msgr);
			MixerTallierRunner mtr = new MixerTallierRunner(ebproxy, msgr);

			runners.add(ssr);
			runners.add(esr);
			runners.add(epr);
			runners.add(eperiodr);
			runners.add(mtr);
		} catch (InvalidNameException | CertificateException | ElectionBoardServiceFault ex) {
			msgr.sendElectionSpecError(geteID(), ex);
		}
	}

	/**
	 * Compute the result of an election
	 *
	 * @return a map of choices id with the relative vote counts.
	 */
	public Map<Choice, Integer> getElectionResults() {
		Map<Choice, Integer> electionResult = new LinkedHashMap<>();

		try {
			DecryptedVotes dv = ebproxy.getDecryptedVotes();
			List<Choice> choices = ebproxy.getElectionData().getChoice();
			List<Rule> rules = ebproxy.getElectionData().getRule();

			//decode each vote
			for (BigInteger vote : dv.getVote()) {
				//for each choice get the b_i value and compute v_i
				for (int i = 0; i < choices.size(); i++) {
					Choice c = choices.get(i);
					Integer choiceID = c.getChoiceId();

					for (int j = 0; j < rules.size(); j++) {
						//take the forall rule and compute b_i
						if (rules.get(j) instanceof ForallRule) {
							ForallRule fra = (ForallRule) rules.get(j);
							List<Integer> idForChoice = fra.getChoiceId();

							//check that the choice is in the list
							boolean isInList = idForChoice.contains(choiceID);

							//if the choice is in the list of for all rules
							if (isInList) {
								//compute the maximal bit per choice: log_2(upperBound) + 1
								int bitPerChoice = (int) Math.floor(Math.log(fra.getUpperBound()) / Math.log(2)) + 1;

								//perform an AND in order to get only the value we need (vote count for this choice id)
								BigInteger maxValuePerChoice = (new BigInteger("2")).pow(bitPerChoice).subtract(BigInteger.ONE);
								BigInteger choiceVoteCount = vote.and(maxValuePerChoice);
								//now we do not need the value anymore, so shift right
								vote = vote.shiftRight(bitPerChoice);

								//add the vote count to each choice
								Integer voteCount;
								if (electionResult.get(c) != null) {
									//if the count for this id is not null, sum the previous value too
									voteCount = electionResult.get(c) + choiceVoteCount.intValue();
								} else {
									voteCount = choiceVoteCount.intValue();
								}

								electionResult.put(c, voteCount);
							}
						}
					}

				}
			}

		} catch (ElectionBoardServiceFault ex) {
			msgr.sendElectionSpecError(geteID(), ex);
		}

		return electionResult;
	}

	/**
	 * This inner class represent the result of an election. A result is
	 * represented by a Choice and the count of the votes.
	 */
	public class ElectionResult {

		private final Choice c;
		private int voteCount;

		public ElectionResult(Choice c, int voteCount) {
			this.c = c;
			this.voteCount = voteCount;
		}

		public Choice getChoice() {
			return c;
		}

		public int getVoteCount() {
			return voteCount;
		}

		public void setVoteCount(int voteCount) {
			this.voteCount = voteCount;
		}
	}
}
