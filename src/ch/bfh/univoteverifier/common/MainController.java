package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.verification.IndividualVerification;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.gui.StatusSubject;

public class MainController {

    UniversalVerification uv;
    IndividualVerification iv;

    public MainController() {
        uv = new UniversalVerification();
        iv = new IndividualVerification();
    }

    public StatusSubject getUniversalStatusSubject() {
        return uv.getStatusSubject();
    }

    public StatusSubject getIndividualStatusSubject() {
        return iv.getStatusSubject();
    }

    public void testObserverPattern() {
        uv.testObserverPattern();
    }
}
