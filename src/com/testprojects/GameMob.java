package com.testprojects;

import java.util.Random;

/**
 * Created by rei on 20.07.2017.
 * Описание класса моба
 */

class GameMob {

    String[] bossNames = {"Ensis",
            "Огр",
            "Циклоп",
            "KUMA"
    };

    private double chanseToBoss = 0.07;
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
                this.mobName = bossNames[random.nextInt(3)];
                this.maxHp = random.nextInt(250) + Main.arr.get(currentId).getAttack() * 8;
                this.attack = random.nextInt(150) + Main.arr.get(currentId).getMaxHP() / 3;
                this.eva = (float)(random.nextInt(25) + 25) * 0.01;
                this.critChanse = (float)(random.nextInt(20) + 60) * 0.01;
            } else {
                this.mobName = mobNames[random.nextInt(12)];
                this.maxHp = random.nextInt(40) + Main.arr.get(currentId).getAttack() * 6;
                this.attack = random.nextInt(30) + Main.arr.get(currentId).getMaxHP() / 7;
                this.eva = (float)(random.nextInt(20) + 15) * 0.01;
                this.critChanse = (float)(random.nextInt(10) + 20) * 0.01;
            }
        } else {
            this.mobName = mobNames[random.nextInt(12)];
            this.maxHp = random.nextInt(20) + Main.arr.get(currentId).getAttack() * 2;
            this.attack = random.nextInt(1) + Main.arr.get(currentId).getMaxHP() / 7;
            this.eva = (float)(random.nextInt(10) + 10) * 0.01;
            this.critChanse = (float)(random.nextInt(10) + 20) * 0.01;
        }
    }

    int getMaxHp() {
        return maxHp;
    }

    int getAttack() {
        return attack;
    }

    double getEva() {
        return eva;
    }

    double getCritChanse() {
        return critChanse;
    }

    String getMobName() {
        return mobName;
    }
}
