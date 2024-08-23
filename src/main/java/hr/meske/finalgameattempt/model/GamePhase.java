package hr.meske.finalgameattempt.model;

import jakarta.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum GamePhase implements java.io.Serializable{
    ADD_SOLDIER, MOVE_SOLDIER, ATTACK, END
}
