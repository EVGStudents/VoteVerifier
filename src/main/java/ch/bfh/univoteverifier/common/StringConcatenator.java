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

import java.util.List;

/**
 * This class is used to concatenate a string in different ways.
 *
 * @author snake
 */
public class StringConcatenator {

	private final StringBuilder strB;
	/**
	 * The left delimiter "("
	 */
	public static final String LEFT_DELIMITER = "(";
	/**
	 * The right delimiter ")"
	 */
	public static final String RIGHT_DELIMITER = ")";
	/**
	 * The inner delimiter "|"
	 */
	public static final String INNER_DELIMITER = "|";

	/**
	 * Construct a new string concatenator
	 */
	public StringConcatenator() {
		strB = new StringBuilder();
	}

	/**
	 * Push an object to be concatenated to this StringConstructor. Its
	 * String value will be added to the concatenator.
	 *
	 * @param o the object to be pushed.
	 */
	public void pushObject(Object o) {
		strB.append(o.toString());
	}

	/**
	 * Push two an object and a delimiter to this StringConstructor.
	 *
	 * @param o the object to be pushed.
	 * @param delimiter the delimiter to be pushed.
	 */
	public void pushObjectDelimiter(Object o, String delimiter) {
		if (delimiter.equals(StringConcatenator.LEFT_DELIMITER)) {
			strB.append(StringConcatenator.LEFT_DELIMITER);
		}

		strB.append(o.toString());

		if (delimiter.equals(StringConcatenator.INNER_DELIMITER) || delimiter.equals(StringConcatenator.RIGHT_DELIMITER)) {
			strB.append(delimiter);
		}
	}

	/**
	 * Push the left delimiter "(" in the current buffer.
	 */
	public void pushLeftDelim() {
		strB.append(StringConcatenator.LEFT_DELIMITER);
	}

	/**
	 * Push the right delimiter ")" in the current buffer.
	 */
	public void pushRightDelim() {
		strB.append(StringConcatenator.RIGHT_DELIMITER);
	}

	/**
	 * Push the inner delimiter "|" in the current buffer.
	 */
	public void pushInnerDelim() {
		strB.append(StringConcatenator.INNER_DELIMITER);
	}

	/**
	 * Push a the elements of a list in the same way as the push() method
	 * do.
	 *
	 * @param l the list containing the elements
	 * @param setDelimiter if true the string will be concatenated using the
	 * delimiters otherwise no
	 */
	public void pushList(List l, boolean setDelimiter) {

		//if empty return
		if (l.isEmpty()) {
			return;
		}

		//if we have only 1 element, push it in the normal way then return
		if (l.size() == 1) {
			strB.append(l.get(0).toString());
			return;
		}

		if (setDelimiter) {
			strB.append(LEFT_DELIMITER);
		}

		int iteration = 0;

		for (Object o : l) {
			iteration++;
			strB.append(o.toString());


			if (setDelimiter && iteration != l.size()) {
				strB.append(INNER_DELIMITER);
			}
		}

		if (setDelimiter) {
			strB.append(RIGHT_DELIMITER);
		}

	}

	/**
	 * Return the string that has been previously pushed. The stack
	 * containing the old strings will be erased.
	 *
	 * @return the concatenated string
	 */
	public String pullAll() {
		String res = strB.toString();
		strB.delete(0, strB.length());
		return res;
	}
}
