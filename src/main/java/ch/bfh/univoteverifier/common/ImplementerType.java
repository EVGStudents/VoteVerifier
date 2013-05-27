/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.common;

/**
 * This enum represent the type of implementer. Used to have a view by
 * "implementer type".
 *
 * @author snake
 */
public enum ImplementerType {

    RSA,
    CERTIFICATE,
    NIZKP,
    PARAMETER,
    SCHNORR;
}
