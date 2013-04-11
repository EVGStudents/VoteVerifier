/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.utils.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.PrimitivesVerifier;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author snake
 */
public abstract class Runner {
	protected String eID;
	protected PrimitivesVerifier prmVrf;
	protected List<VerificationResult> result;
	protected ElectionBoardProxy ebp;

	public Runner(String eID) {
		this.ebp = new ElectionBoardProxy(eID);
		this.result = new ArrayList<VerificationResult>();
		this.prmVrf = new PrimitivesVerifier();
	}

	public abstract List<VerificationResult> run();
}
