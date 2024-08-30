module hr.meske.finalgameattempt {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.xml.bind;
    requires java.rmi;

    // Open all necessary packages to JAXB
    opens hr.meske.finalgameattempt.State to jakarta.xml.bind;
    opens hr.meske.finalgameattempt.model to jakarta.xml.bind, org.glassfish.jaxb.runtime;
    opens hr.meske.finalgameattempt to javafx.fxml;
    opens hr.meske.finalgameattempt.networking to jakarta.xml.bind;
    opens hr.meske.finalgameattempt.utils to jakarta.xml.bind;

exports hr.meske.finalgameattempt.model;
    exports hr.meske.finalgameattempt;
    exports hr.meske.finalgameattempt.chat;

}
