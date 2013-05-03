/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
