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

import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.Rule;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This runner computes the results of an election.
 *
 * @author Scalzi Giuseppe
 */
public class ResultsRunner extends Runner {

    private Map<Choice, Integer> electionResult;
    private final ElectionBoardProxy ebp;

    public ResultsRunner(ElectionBoardProxy ebp, Messenger msgr) {
        super(RunnerName.RESULT, msgr);
        this.ebp = ebp;
    }

    @Override
    public List<VerificationResult> run() throws InterruptedException {
        throw new UnsupportedOperationException("ResultsRunner doesn't support run(). Use runResults() instead.");
    }

    /**
     * Compute the result of an election
     *
     * @return a map of choices with the relative vote counts.
     */
    public Map<Choice, Integer> runResults() {
        electionResult = new LinkedHashMap<>();

        try {
            DecryptedVotes dv = ebp.getDecryptedVotes();
            List<Choice> choices = ebp.getElectionData().getChoice();
            List<Rule> rules = ebp.getElectionData().getRule();

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
            msgr.sendElectionSpecError(ex);
        }

        msgr.sendElectionResults(ebp.getElectionID(), electionResult);
        return electionResult;
    }
}
