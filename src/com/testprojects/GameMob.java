package com.testprojects;

import java.util.Random;

/**
 * Created by rei on 20.07.2017.
 * Описание класса моба
 */

public class GameMob {

    String[] bossNames = {"Ensis",
            "Огр",
            "Циклоп",
            "KUMA"
    };

    private double chanseToBoss = 0.11;
    private String mobName;
    private int maxHp;
    private int attack;
    private double eva;
    private double critChanse;

    private String[] mobNames = {"Вампир",
            "Оборотень",
            "Дрампир",
            "Темный маг",
            "Орк",
            "Стая волков",
            "Зараженный",
            "Скелет",
            "Зомби",
            "Гноллы",
            "Оживший доспех",
            "loli sqwosha"
    };

    void initmob(String currentId) {
        Random random = new Random(System.currentTimeMillis());
        if (Main.arr.get(currentId).lvl >= 10) {
            if (Math.random() >= (1 - chanseToBoss)) {
                this.mobName = bossNames[random.nextInt(5)];
                this.maxHp = random.nextInt(50) + Main.arr.get(currentId).getAttack() * 5;
                this.attack = random.nextInt(50) + Main.arr.get(currentId).getMaxHP() / 5;
                this.eva = (random.nextInt(15) + 25) * 0.01;
                this.critChanse = (random.nextInt(15) + 20) * 0.01;
            } else {
                this.mobName = mobNames[random.nextInt(12)];
                this.maxHp = random.nextInt(40) + Main.arr.get(currentId).getAttack() * 5 / 2;
                this.attack = random.nextInt(40) + Main.arr.get(currentId).getMaxHP() / 7;
                this.eva = (random.nextInt(20) + 15) * 0.01;
                this.critChanse = (random.nextInt(10) + 20) * 0.01;
            }
        } else {
            this.mobName = mobNames[random.nextInt(12)];
            this.maxHp = random.nextInt(20) + Main.arr.get(currentId).getAttack() * 2;
            this.attack = random.nextInt(1) + Main.arr.get(currentId).getMaxHP() / 7;
            this.eva = (random.nextInt(10) + 10) * 0.01;
            this.critChanse = (random.nextInt(10) + 20) * 0.01;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public double getEva() {
        return eva;
    }

    public double getCritChanse() {
        return critChanse;
    }

    public String getMobName() {
        return mobName;
    }
}
