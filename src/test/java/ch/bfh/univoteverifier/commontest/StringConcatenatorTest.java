/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.commontest;

import ch.bfh.univoteverifier.common.StringConcatenator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the string concatenator used to concatenate the strings.
 *
 * @author snake
 */
public class StringConcatenatorTest {

	StringConcatenator sc;

	public StringConcatenatorTest() {
		this.sc = new StringConcatenator();
	}

	/**
	 * Test if the pull works
	 */
	@Test
	public void pullConcatenation() {
		sc.pushObjectDelimiter("string", StringConcatenator.INNER_DELIMITER);
		sc.pushObject("string");

		assertEquals("string|string", sc.pullAll());

		//now we must have an empty string
		assertEquals("", sc.pullAll());
	}

	/**
	 * Test a simple concatenation
	 */
	@Test
	public void simpleConcatenation() {
		String one = "first";
		String two = "second";

		sc.pushLeftDelim();
		sc.pushObject(one);
		sc.pushInnerDelim();
		sc.pushObject(two);
		sc.pushRightDelim();

		assertEquals("(first|second)", sc.pullAll());
	}

	/**
	 * Test a simple concatenation with the method pushObjectDelimiter that
	 * pushes the delimiter too.
	 */
	@Test
	public void simpleConcatenationObjectDelimiter() {
		String one = "first";
		String two = "second";

		sc.pushLeftDelim();
		sc.pushObjectDelimiter(one, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(two, StringConcatenator.RIGHT_DELIMITER);

		assertEquals("(first|second)", sc.pullAll());
	}

	/**
	 * Test a concatenation with an array inside
	 */
	@Test
	public void simpleConcatenationTwo() {
		String first = "first";
		String array_first = "arr1";
		String array_second = "arr2";
		String array_third = "arr3";
		String second = "second";

		sc.pushLeftDelim();
		sc.pushObject(first);
		sc.pushInnerDelim();
		sc.pushLeftDelim();
		sc.pushObject(array_first);
		sc.pushInnerDelim();
		sc.pushObject(array_second);
		sc.pushInnerDelim();
		sc.pushObject(array_third);
		sc.pushRightDelim();
		sc.pushInnerDelim();
		sc.pushObject(second);
		sc.pushRightDelim();

		assertEquals("(first|(arr1|arr2|arr3)|second)", sc.pullAll());
	}

	/**
	 * Test a concatenation with some BigInteger objects
	 */
	@Test
	public void pushListObject() {
		List<BigInteger> bi = new ArrayList<>();
		bi.add(new BigInteger("134536454634534"));
		bi.add(new BigInteger("454634534"));
		bi.add(new BigInteger("134536534"));
		bi.add(new BigInteger("634534"));
		bi.add(new BigInteger("579485769845"));
		bi.add(new BigInteger("678549"));
		bi.add(new BigInteger("13565565756134536454634534"));
		bi.add(new BigInteger("9879879134536454634534"));

		sc.pushList(bi, true);

		String res = sc.pullAll();

		assertEquals("(134536454634534|454634534|134536534|634534|579485769845|678549|13565565756134536454634534|9879879134536454634534)", res);
	}

	/**
	 * Push a list with one element
	 */
	@Test
	public void pushListSimple() {
		List<BigInteger> bi = new ArrayList<>();
		bi.add(new BigInteger("1"));

		sc.pushList(bi, true);

		assertEquals("1", sc.pullAll());
	}

	/**
	 * Push a list with two elements
	 */
	@Test
	public void pushListObjectTwo() {
		List<BigInteger> bi = new ArrayList<>();
		bi.add(new BigInteger("1"));
		bi.add(new BigInteger("1"));

		sc.pushList(bi, true);

		assertEquals("(1|1)", sc.pullAll());
	}

	/**
	 * Push an empty list
	 */
	@Test
	public void pushListObjectEmpty() {
		List<BigInteger> bi = new ArrayList<>();

		sc.pushList(bi, true);

		assertEquals("", sc.pullAll());
	}

	/**
	 * Push two object without separators
	 */
	@Test
	public void pushListWithoutSeparators() {
		List<BigInteger> bi = new ArrayList<>();
		bi.add(new BigInteger("8458347593847978797879871"));
		bi.add(new BigInteger("445363641"));

		sc.pushList(bi, false);

		assertEquals("8458347593847978797879871445363641", sc.pullAll());
	}
}
