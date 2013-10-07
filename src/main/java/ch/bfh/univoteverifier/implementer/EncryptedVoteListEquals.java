/*
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 * Bern University of Applied Sciences, Engineering and Information Technology,
 * Research Institute for Security in the Information Society, E-Voting Group,
 * Biel, Switzerland.
 *
 * Project Univote.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univote.common.EncryptedVote;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Severin Hauser &lt;severin.hauser@bfh.ch&gt;
 */
public class EncryptedVoteListEquals {

	static boolean equals(List<EncryptedVote> votes1, List<EncryptedVote> votes2) {
		Iterator<EncryptedVote> i1 = votes1.iterator();
		Iterator<EncryptedVote> i2 = votes2.iterator();
		if (votes1.size() != votes2.size()) {
			return false;
		}
		while (i1.hasNext()) {
			EncryptedVote encV1 = i1.next();
			EncryptedVote encV2 = i2.next();
			if (!encV1.getFirstValue().equals(encV2.getFirstValue())) {
				return false;
			}
			if (!encV1.getSecondValue().equals(encV2.getSecondValue())) {
				return false;
			}
		}
		return true;
	}
}
