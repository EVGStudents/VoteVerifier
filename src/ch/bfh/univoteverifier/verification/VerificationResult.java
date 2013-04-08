/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

/**
 *
 * @author prinstin
 */
public class VerificationResult {
    
    private Verification v;
    private Boolean result;



    public VerificationResult(Verification v, Boolean result) {
        this.v=v;
        this.result=result;
    }
    
        public Verification getV() {
        return v;
    }

    public Boolean getResult() {
        return result;
    }
    
}
