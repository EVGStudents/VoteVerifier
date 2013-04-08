/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.Config;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author snake
 */
public class SystemSetupVerifier {

	private PrimitivesVerifier prmVrf = new PrimitivesVerifier();

	public List vrfSignParam(){
		List<VerificationResult> res = new ArrayList<>();

		res.add(new VerificationResult(Verification.PRIMITIVE_P_IS_PRIME, prmVrf.vrfPrimeNumber(Config.p)));

		res.add(new VerificationResult(Verification.PRIMITIVE_Q_IS_PRIME, prmVrf.vrfPrimeNumber(Config.q)));
		
		return res;
	}
}
