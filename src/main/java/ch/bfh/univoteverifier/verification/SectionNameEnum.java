/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

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
