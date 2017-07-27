package com.testprojects;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.Random;

/**
 * Created by rei on 18.07.2017.
 */

class GameEvents {
    private static String[] array = {"Вы заболели, -10HP -20 к точноcти",
            "Удача сопуствует Вам в пути. Шанс критического удар повышен",
            "На вас снизошло благословление богов, +4 к атаке, +100 к точности",
            "Кража! У вас украли наплечник, -2 к броне",
            "Вы помогли торговцу в пути, в награду вам дали новое оружие, +2 к атаке",
            "Засада! После сражение вы потеряли 15HP",
            "Вы отдохнули в городе, восстановлено HP",
            "Смерть приходит внезапно"};

    /**
     * Роллит событие
     *
     * @param id currentId
     * @return строку события
     */
    private String getGameEvent(String id) {
        Random rand = new Random(System.currentTimeMillis());
        int roll = rand.nextInt(7);
        if (Math.random() >= 1 - 0.02) {
            Main.arr.get(id).setCurrentHP(-Main.arr.get(id).getCurrentHP());
            return array[7];
        } else {
            switch (roll) {
                case 0: {
                    Main.arr.get(id).setCurrentHP(-10);
                    Main.arr.get(id).setAcc(-20);
                    if (Main.arr.get(id).getAcc() < 0)
                        Main.arr.get(id).setAcc(Main.arr.get(id).getAcc() * (-1));
                    break;
                }
                case 1: {
                    Main.arr.get(id).setCritChanse(0.01);
                    Main.arr.get(id).lvlup();
                    break;
                }
                case 2: {
                    Main.arr.get(id).upAttack(4);
                    Main.arr.get(id).setAcc(100);
                    Main.arr.get(id).lvlup();
                    break;
                }
                case 3: {
                    Main.arr.get(id).upDef(-2);
                    if (Main.arr.get(id).getDef() < 0) {
                        Main.arr.get(id).upDef(Main.arr.get(id).getDef() * (-1));
                    }
                    break;
                }
                case 4: {
                    Main.arr.get(id).upAttack(2);
                    break;
                }
                case 5: {
                    Main.arr.get(id).setCurrentHP(-15);
                    break;
                }
                case 6: {
                    Main.arr.get(id).setCurrentHP(Main.arr.get(id).getMaxHP() - Main.arr.get(id).getCurrentHP());
                    break;
                }
                default: {
                    break;
                }
            }
        }
        return array[roll];
    }

    /**
     * Проверяет хп, удаляет убитого персонажа
     *
     * @param id
     * @return
     */
    boolean checkOnDeath(String id) {
        long hp = Main.arr.get(id).currentHP;
        if (hp <= 0) {
            Main.arr.remove(id);
            return true;
        } else return false;
    }

    /**
     * Выводит сообщение о смерти
     *
     * @param messageChannel
     * @param currentId
     */
    void msgOnDeath(MessageChannel messageChannel, String currentId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<@")
                .append(currentId)
                .append("> умер");
        messageChannel.sendMessage(stringBuilder.toString()).queue();
    }

    /**
     * Заносит экземлпяр userinterface в мап, выводит сообщение
     *
     * @param currentId
     * @param messageChannel
     * @param author
     */
    void initPlayer(String currentId, MessageChannel messageChannel, User author, String secondToken) {
        StringBuilder stringBuilder = new StringBuilder();
        UserInterface user = new UserInterface();
        user.setId(currentId);
        user.setName(author.getName());

        switch (secondToken) {
            case "маг":{
                user.setType(user.types[0]);
                break;
            }
            case "паладин": {
                user.setType(user.types[1]);
                break;
            }
            case "лучник": {
                user.setType(user.types[2]);
                break;
            }
            case "разбойник": {
                user.setType(user.types[3]);
                break;
            }
            default: {
                user.setType(user.types[4]);
                break;
            }
        }

        Main.arr.put(currentId, user);

        stringBuilder.append("<@")
                .append(currentId)
                .append("> ***Вы начали приключение!***")
                .append(printStats(currentId));
        messageChannel.sendMessage(stringBuilder.toString()).queue();
    }

    /**
     * Выводит сообщение о статах
     *
     * @param messageChannel
     * @param currentId
     */
    void printStatS(MessageChannel messageChannel, String currentId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<@")
                .append(currentId)
                .append(">")
                .append(printStats(currentId));
        messageChannel.sendMessage(stringBuilder.toString()).queue();
    }

    /**
     * Формирует сообщение о статах
     *
     * @param currentId
     * @return
     */
    private String printStats(String currentId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n**Характеристики:**\n")
                .append(Main.arr.get(currentId).name).append(" ").append(Main.arr.get(currentId).getType())
                .append(" уровень ").append(Main.arr.get(currentId).lvl).append("\n")
                .append(Main.arr.get(currentId).currentHP).append("\\").append(Main.arr.get(currentId).maxHP).append(" HP\n")
                .append(Main.arr.get(currentId).attack).append(" Урон\n")
                .append(Main.arr.get(currentId).def).append(" Броня\n")
                .append(((float) Main.arr.get(currentId).eva)).append(" Уклонение\n")
                .append(Main.arr.get(currentId).acc).append(" Точность\n");
        return stringBuilder.toString();
    }

    /**
     * Формирует и выводит событие
     *
     * @param messageChannel
     * @param currentId
     */
    void initEvent(MessageChannel messageChannel, String currentId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<@")
                .append(currentId)
                .append(">\n")
                .append(getGameEvent(currentId))
                .append(printStats(currentId));
        messageChannel.sendMessage(stringBuilder.toString()).queue();
    }


}