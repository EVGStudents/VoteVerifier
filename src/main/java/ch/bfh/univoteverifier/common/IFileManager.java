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

import java.io.File;

/**
 * This interface defines a FileManager which is implemented by an inner class
 * in the GUI and holds a reference to a file expected to be QRCode and which is
 * sent to the verification thread when individual verification is selected.
 *
 * @author prinstin
 */
public interface IFileManager {

    public File getFile();

    public void setFile(File file);
}
