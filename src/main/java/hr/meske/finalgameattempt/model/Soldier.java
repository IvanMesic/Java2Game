package hr.meske.finalgameattempt.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "Soldier")
@XmlAccessorType(XmlAccessType.FIELD)
public class Soldier implements java.io.Serializable{

    @XmlElement(name = "HP")
    public int hp = 100;

    @XmlElement(name = "Attack")
    public int attack = 30;

    @XmlElement(name = "Team")
    private Team team;

    public int getAttack() {
        return attack;
    }

    public void deduceHP(int damage) {
        hp -= damage;
    }

    public int getHp() {
        return hp;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team){
        this.team = team;
    }

    public Soldier(Team team) {
        this.team = team;
    }

    public Soldier() {}
}
