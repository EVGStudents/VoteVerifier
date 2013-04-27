/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.common;

/**
 * This enumeration specify the name of the sections 
 * for the verification results
 * @author snake
 */
public enum SectionNameEnum {
	UNSET("Unset"),
	SYSTEM_SETUP("System Setup"),
	ELECTION_SETUP("Election Setup"),
	ELECTION_PREPARATION("Election Preparation"),
	ELECTION_PERIOD("Election Period"),
	MIXING_TALLING("Mixing and Talling"),
	CA("Certificate Authority"),
	EM("Election Manager"),
	EA("Election Administrator"),
	MIXER("Mixer"),
	TALLIER("Tallier");

	private final String name;
	
	private SectionNameEnum(String name){
		this.name = name;	
	}

	@Override
	public String toString(){
		return this.name;
	}
}