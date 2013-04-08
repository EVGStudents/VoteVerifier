/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

/**
 *
 * @author prinstin
 * 
* Enumerates the types of messages that the GUI receives
*/
public enum StatusMessage {

VRF_STATUS(0),
VRF_RESULT(10)

;

//The code of the message
private int code;

/**
* Construct a Message
* @param c the code for the message
*/
private StatusMessage(int c){
code = c;
}

/**
* Get the code of the message
* @return an integer with the code of this message
*/
public int getInt(){
return code;
}




}