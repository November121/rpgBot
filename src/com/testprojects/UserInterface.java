package com.testprojects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rei on 18.07.2017.
 */
public class UserInterface implements Serializable {

    // MEMBER NAME EFFECTIVENAME
    String id;
    String name;
    ArrayList<String> messages;

    String getId() {
        return this.id;
    }
    void setId(String id) {
        this.id = id;
    }
    String getName() {
        return this.name;
    }
    void setName(String name) {
        this.name = name;
    }

    //EXP LVL CLASS
    int exp = 10;
    int lvl = 1;
    private String type = "none";

    String[] types = {"маг",
            "паладин",
            "лучник",
            "разбойник",
            "999"
    };

    public void upExp(int exp) {
        this.exp += exp;
    }
    public int getExp() {
        return exp;
    }
    public int getLvl() {
        return lvl;
    }


    //MAXHP CURRENTHP ATTACK DEF EVA ACC CRIT
    int maxHP = 100;
    int currentHP = 100;
    int attack = 10;
    int def = 10;
    double eva = 0.10;
    int acc = 95;
    double critChanse = 0.11;
    private double chanseToSkill = 0.2;

    public int getMaxHP() {
        return this.maxHP;
    }
    public void upMaxHP(int upHP) {
        this.maxHP += upHP;
    }
    public int getCurrentHP() {
        return this.currentHP;
    }
    public void setCurrentHP(int upHp) {
        this.currentHP += upHp;
    }
    public int getAttack() {
        return attack;
    }
    public void upAttack(int upAttack) {
        this.attack += upAttack;
    }
    public int getDef() {
        return this.def;
    }
    public void upDef(int upDef) {
        this.def += upDef;
    }
    public double getEva(){
        return this.eva;
    }
    public void upEva(int upEvasion){
        this.eva += upEvasion;
    }
    public int getAcc(){
        return this.acc;
    }
    public void setAcc(int acc){
        this.acc += acc;
    }
    public double getCritChanse(){
        return this.critChanse;
    }
    public void setCritChanse(double critChanse){
        this.critChanse += critChanse;
    }

    public void lvlup() {
        switch (this.type) {
            case "999": {
                this.lvl++;
                this.maxHP += 10;
                this.currentHP = this.maxHP;
                this.attack += 4;
                this.def += 3;
                this.acc += 10;
                this.critChanse += 0.005;
                this.eva += 0.003;
                break;
            }
            case "маг": {
                this.lvl++;
                this.maxHP += 4;
                this.currentHP = this.maxHP;
                this.attack += 7;
                this.def += 2;
                this.acc += 20;
                this.critChanse += 0.007;
                this.eva += 0.002;
                break;
            }
            case "паладин": {
                this.lvl++;
                this.maxHP += 20;
                this.currentHP = this.maxHP;
                this.attack += 3;
                this.def += 8;
                this.acc += 10;
                this.critChanse += 0.005;
                this.eva += 0.002;
                break;
            }
            case "лучник": {
                this.lvl++;
                this.maxHP += 5;
                this.currentHP = this.maxHP;
                this.attack += 8;
                this.def += 2;
                this.acc += 25;
                this.critChanse += 0.008;
                this.eva += 0.005;
                break;
            }
            case "разбойник": {
                this.lvl++;
                this.maxHP += 8;
                this.currentHP = this.maxHP;
                this.attack += 5;
                this.def += 4;
                this.acc += 15;
                this.critChanse += 0.006;
                this.eva += 0.005;
                break;
            }
            default: {
                this.lvl++;
                this.maxHP += 10;
                this.currentHP = this.maxHP;
                this.attack += 4;
                this.def += 2;
                this.acc += 10;
                this.critChanse += 0.005;
                this.eva += 0.003;
                break;
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getChanseToSkill() {
        return chanseToSkill;
    }

    public void setChanseToSkill(double chanseToSkill) {

        this.chanseToSkill = chanseToSkill;
    }
}
