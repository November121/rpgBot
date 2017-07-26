package com.testprojects;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Consumer;

/**
 * Created by rei on 18.07.2017.
 */

public class RpgBot extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        long responseNumber = event.getResponseNumber();

        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel messageChannel = event.getChannel();

        String msg = message.getContent();
        StringTokenizer st = new StringTokenizer(msg);
        String firstToken = st.nextToken().toLowerCase();

        boolean bot = author.isBot();
        if (bot) return;

        if (event.isFromType(ChannelType.TEXT)) {
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) {
                name = author.getName();
            } else {
                name = member.getEffectiveName();
            }
            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            PrivateChannel privateChannel = event.getPrivateChannel();
            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        } else if (event.isFromType(ChannelType.GROUP)) {
            Group group = event.getGroup();
            String groupName = group.getName() != null ? group.getName() : "";
            System.out.printf("[GRP: %s]<%s>: %s\n", groupName, author.getName(), msg);
        }

        StringBuilder stringBuilder = new StringBuilder();
        GameEvents gameEvents = new GameEvents();
        String currentId = author.getId();

        switch (firstToken) {
            case "!начать": {
                if (!Main.arr.containsKey(currentId)) {
                    gameEvents.initPlayer(currentId, messageChannel, author);
                }
                break;
            }
            case "!умереть": {
                if (Main.arr.containsKey(currentId)) {
                    Main.arr.remove(currentId);
                    gameEvents.msgOnDeath(messageChannel, currentId);
                }
                break;
            }
            case "!статы": {
                if (Main.arr.containsKey(currentId)){
                    gameEvents.printStatS(messageChannel, currentId);
                }
                break;
            }
            case "!событие":{
                if (Main.arr.containsKey(currentId)){
                    gameEvents.initEvent(messageChannel, currentId);
                    if (gameEvents.checkOnDeath(currentId)){
                        gameEvents.msgOnDeath(messageChannel, currentId);
                    }
                }
                break;
            }
            case "!данж": {
                if (Main.arr.containsKey(currentId)) {
                    GameMob gameMob = new GameMob();
                    gameMob.initmob(currentId);
                    stringBuilder.append(createHelloMob(gameMob, currentId));
                    stringBuilder.append(fightVSMob(gameMob, currentId));
                    messageChannel.sendMessage(stringBuilder.toString()).queue();
                    if (gameEvents.checkOnDeath(currentId)) {
                        gameEvents.msgOnDeath(messageChannel, currentId);
                    }
                }
                break;
            }
            case "!команды":{
                sendPrivateMessage(author, "!начать\n" + "!статы\n" + "!событие\n" + "!данж\n" + "!пвп @Username\n"+
                        "Username - никнейм пользователя дискорд, которому бросается вызов.\n" +
                        "Существуют некоторая вероятность случайной смерти в событих, встретить босса в данже, и даже уйти живым из проигранного боя.\n" +
                        "В пвп режиме шанс критического удара и уворота увеличены, будьте аккуратны. \nУдачи!");
                break;
            }
            case "!пвп":{
                if (Main.arr.containsKey(currentId)) {
                    stringBuilder.append(msg);
                    String enemyName = stringBuilder.substring(6, stringBuilder.length());
                    System.out.println(enemyName);
                    stringBuilder.delete(0, stringBuilder.length());
                    String enemyId = foundEnemy(enemyName);
                    System.out.println(enemyId);

                    if (enemyId != null && !enemyId.equals(currentId)) {
                        stringBuilder.append("**<@").append(Main.arr.get(currentId).getId())
                                .append("> уровень ").append(Main.arr.get(currentId).getLvl()).append("** ")
                                .append(" вызывает на бой ")
                                .append("**<@").append(Main.arr.get(enemyId).getId())
                                .append("> уровень ").append(Main.arr.get(enemyId).getLvl()).append("**\n")
                                .append("***БОЙ***\n");
                        stringBuilder.append(initPVP(currentId, enemyId));
                        messageChannel.sendMessage(stringBuilder.toString()).queue();
                        stringBuilder.delete(0, stringBuilder.length());

                        if (gameEvents.checkOnDeath(currentId)) {
                            gameEvents.msgOnDeath(messageChannel, currentId);
                        }
                        if (gameEvents.checkOnDeath(enemyId)) {
                            gameEvents.msgOnDeath(messageChannel, enemyId);
                        }
                    } else messageChannel.sendMessage("<@" + Main.arr.get(currentId).getId() + "> невозможно провести бой").queue();
                }
            }
            default: {
                break;
            }
        }
    }



        /*else if (msg.equals("!событие") && Main.arr.containsKey(currentId)) {
            stringBuilder.append("<@")
                    .append(currentId)
                    .append(">\n")
                    .append(gameEvents.getGameEvent(currentId))
                    .append(printStats(currentId));
            messageChannel.sendMessage(stringBuilder.toString()).queue();
            stringBuilder.delete(0, stringBuilder.length() - 1);

            if (gameEvents.checkOnDeath(currentId)) {
                stringBuilder.append("<@")
                        .append(currentId)
                        .append("> умер");
                messageChannel.sendMessage(stringBuilder.toString()).queue();
                stringBuilder.delete(0, stringBuilder.length() - 1);
            }

        } else if (msg.equals("!данж") && Main.arr.containsKey(currentId)) {
            GameMob gameMob = new GameMob();
            gameMob.initmob(currentId);

            stringBuilder.append(createHelloMob(gameMob, currentId));
            stringBuilder.append(fightVSMob(gameMob, currentId));

            messageChannel.sendMessage(stringBuilder.toString()).queue();
            stringBuilder.delete(0, stringBuilder.length() - 1);

            if (gameEvents.checkOnDeath(currentId)) {
                stringBuilder.append("<@")
                        .append(currentId)
                        .append("> умер");
                messageChannel.sendMessage(stringBuilder.toString()).queue();
                stringBuilder.delete(0, stringBuilder.length() - 1);
            }
        } else if (msg.equals("!команды")) {
            sendPrivateMessage(author, "!начать\n" + "!статы\n" + "!событие\n" + "!данж\n" + "!пвп @Username\n"+
                    "Username - никнейм пользователя дискорд, которому бросается вызов.\n" +
                    "Существуют некоторая вероятность случайной смерти в событих, встретить босса в данже, и даже уйти живым из проигранного боя.\n" +
                    "В пвп режиме шанс критического удара и уворота увеличены, будьте аккуратны. \nУдачи!");
        } else if (msg.startsWith("!пвп")) {
            stringBuilder.append(msg);
            String enemyName = stringBuilder.substring(6, stringBuilder.length());
            stringBuilder.delete(0, stringBuilder.length());
            String enemyId = foundEnemy(enemyName);
            if (enemyId != null && !enemyId.equals(currentId)) {
                 stringBuilder.append("**<@").append(Main.arr.get(currentId).getId())
                        .append("> уровень ").append(Main.arr.get(currentId).getLvl()).append("** ")
                        .append(" вызывает на бой ")
                        .append("**<@").append(Main.arr.get(enemyId).getId())
                        .append("> уровень ").append(Main.arr.get(enemyId).getLvl()).append("**\n")
                        .append("***БОЙ***\n");
                stringBuilder.append(initPVP(currentId, enemyId));
                messageChannel.sendMessage(stringBuilder.toString()).queue();
                stringBuilder.delete(0, stringBuilder.length());

                if (gameEvents.checkOnDeath(currentId)) {
                    stringBuilder.append("<@")
                            .append(currentId)
                            .append("> умер");
                    messageChannel.sendMessage(stringBuilder.toString()).queue();
                    stringBuilder.delete(0, stringBuilder.length());
                }
                if (gameEvents.checkOnDeath(enemyId)) {
                    stringBuilder.append("<@")
                            .append(enemyId)
                            .append("> умер");
                    messageChannel.sendMessage(stringBuilder.toString()).queue();
                    stringBuilder.delete(0, stringBuilder.length());
                }
            } else System.out.println("<@" + Main.arr.get(currentId).getId() + "> невозможно провести бой");
        }
    }
    */

    /**PVP
     * @param currentId
     * @param enemyId
     * @return string with fight log
     */
    private String initPVP(String currentId, String enemyId) {
        StringBuilder stringBuilder = new StringBuilder();

        int maxHP1 = Main.arr.get(currentId).getMaxHP();
        int currentHP1 = Main.arr.get(currentId).getCurrentHP();
        int attack1 = Main.arr.get(currentId).getAttack();
        int def1 = Main.arr.get(currentId).getDef();
        double eva1 = Main.arr.get(currentId).getEva() * 2;
        int acc1 = Main.arr.get(currentId).getAcc();
        double critChanse1 = Main.arr.get(currentId).getCritChanse() * 2;

        int maxHP2 = Main.arr.get(enemyId).getMaxHP();
        int currentHP2 = Main.arr.get(enemyId).getCurrentHP();
        int attack2 = Main.arr.get(enemyId).getAttack();
        int def2 = Main.arr.get(enemyId).getDef();
        double eva2 = Main.arr.get(enemyId).getEva() * 2;
        int acc2 = Main.arr.get(enemyId).getAcc();
        double critChanse2 = Main.arr.get(enemyId).getCritChanse() * 2;

        while (currentHP2 > 0) {
            if (currentHP1 < 0) break;

            if (Math.random() >= (1 - (eva2))) { //если не попал
                stringBuilder.append(Main.arr.get(currentId).getName())
                        .append("**[").append(currentHP1).append("/").append(maxHP1).append("]**").append("HP")
                        .append("***промахивается***.\n");
            } else { //если попал
                if (Math.random() >= (1 - critChanse1)) { //если крит
                    currentHP2 -= (attack1 + acc1 / 100 - def2 / 4) * 2;
                    stringBuilder.append(Main.arr.get(currentId).getName())
                            .append("**[").append(currentHP1).append("/").append(maxHP1).append("]**").append("HP")
                            .append(" наносит ***критический удар***.")
                            .append("**").append(" -").append((attack1 + acc1 / 100 - def2 / 4) * 2).append("**HP ")
                            .append(Main.arr.get(enemyId).getName()).append("**[").append(currentHP2)
                            .append("/").append(maxHP2).append("]**HP\n");
                } else {    //если не крит
                    currentHP2 -= attack1 + acc1 / 100 - def2 / 4;
                    stringBuilder.append(Main.arr.get(currentId).getName())
                            .append("**[").append(currentHP1).append("/").append(maxHP1).append("]**").append("HP")
                            .append(" наносит удар. ")
                            .append("**").append(" -").append((attack1 + acc1 / 100 - def2 / 4)).append("**HP ")
                            .append(Main.arr.get(enemyId).getName()).append("**[").append(currentHP2)
                            .append("/").append(maxHP2).append("]**HP\n");
                }
            }
            if (currentHP2 < 0) break;

            if (Math.random() >= (1 - eva1)) { //если уворот
                stringBuilder.append(Main.arr.get(enemyId).getName()).append("**[")
                        .append(currentHP2).append("/").append(maxHP2).append("]HP**").append(" ***промахивается***.\n");
            } else { //если попал
                if (Math.random() >= (1 - critChanse2)) {
                    currentHP1 -= (attack2 + acc2 / 100 - def1 / 4) * 2;
                    stringBuilder.append(Main.arr.get(enemyId).getName()).append("**[")
                            .append(currentHP2).append("/").append(maxHP2).append("]HP**")
                            .append(" наносит ***критический удар***. ")
                            .append("**-").append(((attack2 + acc2 / 100 - def1 / 4) * 2)).append("**HP ")
                            .append(Main.arr.get(currentId).getName())
                            .append("**[").append(currentHP1).append("/").append(maxHP1).append("]**").append("HP\n");
                } else {
                    currentHP1 -= attack2 + acc2 / 100 - def1 / 4;
                    stringBuilder.append(Main.arr.get(enemyId).getName()).append("**[")
                            .append(currentHP2).append("/").append(maxHP2).append("]HP**").append(" наносит удар. ")
                            .append(" **-").append((attack2 + acc2 / 100 - def1 / 4)).append("**HP ")
                            .append(Main.arr.get(currentId).getName())
                            .append("**[").append(currentHP1).append("/").append(maxHP1).append("]**").append("HP\n");
                }
            }
        }

        if (currentHP1 <= 0) {
            Main.arr.get(currentId).lvlup();
            Main.arr.get(currentId).setCurrentHP(-Main.arr.get(currentId).getCurrentHP());
            stringBuilder.append("**").append(Main.arr.get(enemyId).getName()).append(" победил**\n");
        } else {
            Main.arr.get(currentId).lvlup();
            Main.arr.get(enemyId).setCurrentHP(-Main.arr.get(enemyId).getCurrentHP());
            stringBuilder.append("**").append(Main.arr.get(currentId).getName()).append(" победил**\n");
        }

        if (currentHP1 <= 0) {
            if (Math.random() >= (1 - 0.22)) {
                Main.arr.get(currentId).setCurrentHP(100);
                stringBuilder.append("Позорное бегство, на самом деле не такое уж и позороное, если вы все еще живы\n");
            }
        }

        return stringBuilder.toString();
    }

    /** check enemy in hashmap
     * @param enemyName
     * @return
     */
    private String foundEnemy(String enemyName) {
        for (Map.Entry<String, UserInterface> entry : Main.arr.entrySet()) {
            if (entry.getValue().getName().equals(enemyName)) {
                return entry.getValue().getId();
            }
        }
        return null;
    }

    /**Sending private message
     * @param user
     * @param content
     */
    public void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) -> sendAndLog(channel, content));
    }

    public void sendAndLog(MessageChannel channel, String message) {
        Consumer<Message> callback = (response) -> System.out.printf("Sent Message %s\n", response);
        channel.sendMessage(message).queue(callback); // ^ calls that
    }

    /**
     * Сосбщение о мобе
     * @param gameMob   экземпляр класса моба
     * @param currentId id автора
     * @return сообщение о встрече и статы моба
     */
    private String createHelloMob(GameMob gameMob, String currentId) {
        StringBuilder stringBuilder = new StringBuilder();
        if (checkOnBoss(gameMob)) { //если босс
            stringBuilder.append("<@")
                    .append(currentId).append(">\n")
                    .append("В подземелье вас заметил ").append("** БОСС УРОВНЯ ")
                    .append(gameMob.getMobName()).append("**\n")
                    .append(gameMob.getMaxHp()).append(" HP\n")
                    .append(gameMob.getAttack()).append(" Урон\n")
                    .append(gameMob.getEva()).append(" Уклонение\n")
                    .append(gameMob.getCritChanse()).append(" Шанс критического удара\n");
        } else {    //если обычный моб
            stringBuilder.append("<@")
                    .append(currentId).append(">\n")
                    .append("В подземелье вас заметил ").append("**")
                    .append(gameMob.getMobName()).append("**\n")
                    .append(gameMob.getMaxHp()).append(" HP\n")
                    .append(gameMob.getAttack()).append(" Урон\n")
                    .append(gameMob.getEva()).append(" Уклонение\n")
                    .append(gameMob.getCritChanse()).append(" Шанс критического удара\n");
        }
        return stringBuilder.toString();
    }

    /**
     * чек моба на босса
     *
     * @param gameMob
     * @return true если босс
     */
    private boolean checkOnBoss(GameMob gameMob) {
        for (int i = 0; i < 4; i++) {
            if (gameMob.getMobName().equals(gameMob.bossNames[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * лог боя
     *
     * @param gameMob
     * @param currentId
     * @return лог боя
     */
    private String fightVSMob(GameMob gameMob, String currentId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**БОЙ**\n");

        int maxHP = Main.arr.get(currentId).getMaxHP();
        int currentHP = Main.arr.get(currentId).getCurrentHP();
        int attack = Main.arr.get(currentId).getAttack();
        int def = Main.arr.get(currentId).getDef();
        double eva = Main.arr.get(currentId).getEva();
        int acc = Main.arr.get(currentId).getAcc();
        double critChanse = Main.arr.get(currentId).getCritChanse();

        String mobName = gameMob.getMobName();
        int mobHp = gameMob.getMaxHp();
        int mobAttack = gameMob.getAttack();
        double mobEva = gameMob.getEva();
        double mobCritChanse = gameMob.getCritChanse();

        while (mobHp > 0) {
            if (currentHP < 0) break;

            if (Math.random() >= (1 - mobEva)) { //если не попал
                stringBuilder.append(Main.arr.get(currentId).getName())
                        .append("**[").append(currentHP).append("/").append(maxHP).append("]**").append("HP")
                        .append("***промахивается***.\n");
            } else { //если попал
                if (Math.random() >= (1 - critChanse)) { //если крит
                    mobHp -= (attack + acc / 100) * 2;
                    stringBuilder.append(Main.arr.get(currentId).getName())
                            .append("**[").append(currentHP).append("/").append(maxHP).append("]**").append("HP")
                            .append(" наносит ***критический удар***. ")
                            .append("**").append(" -").append(((attack + acc / 100) * 2)).append("**HP ")
                            .append(mobName).append("**[").append(mobHp).append("]**HP\n");
                } else {    //если не крит
                    mobHp -= attack + acc / 100;
                    stringBuilder.append(Main.arr.get(currentId).getName())
                            .append("**[").append(currentHP).append("/").append(maxHP).append("]**").append("HP")
                            .append(" наносит удар. ")
                            .append("**").append(" -").append((attack + acc / 100)).append("**HP ")
                            .append(mobName).append("**[").append(mobHp).append("]**HP\n");
                }
            }
            if (mobHp < 0) break;

            if (Math.random() >= (1 - eva)) { //если уворот
                stringBuilder.append(mobName).append("**[").append(mobHp).append("]HP**").append(" ***промахивается***.\n")
                        .append(Main.arr.get(currentId).getName());
            } else { //если попал
                if (Math.random() >= (1 - mobCritChanse)) {
                    currentHP -= (mobAttack - def / 5) * 2;
                    stringBuilder.append(mobName).append("**[").append(mobHp).append("]HP**").append(" наносит ***критический удар***. ")
                            .append(Main.arr.get(currentId).getName())
                            .append("**[").append(currentHP).append("/").append(maxHP).append("]**").append("HP")
                            .append(" **-").append(((mobAttack - def / 5) * 2)).append("**HP\n");
                } else {
                    currentHP -= mobAttack - def / 5;
                    stringBuilder.append(mobName).append("**[").append(mobHp).append("]HP**").append(" наносит удар. ")
                            .append(Main.arr.get(currentId).getName())
                            .append("**[").append(currentHP).append("/").append(maxHP).append("]**").append("HP")
                            .append(" **-").append((mobAttack - def / 5)).append("**HP\n");
                }
            }
        }

        if (currentHP <= 0) {
            Main.arr.get(currentId).lvlup();
            Main.arr.get(currentId).setCurrentHP(-Main.arr.get(currentId).getCurrentHP());
            stringBuilder.append("**").append(mobName).append(" победил**\n");
        } else {
            Main.arr.get(currentId).lvlup();
            if (gameMob.getMobName().equals(gameMob.bossNames[0])) {
                for (int i = 0; i < 9; i++) {
                    Main.arr.get(currentId).lvlup();
                }
            }
            stringBuilder.append("**").append(Main.arr.get(currentId).getName()).append(" победил**\n");
        }

        if (currentHP <= 0) {
            if (Math.random() >= (1 - 0.22)) {
                Main.arr.get(currentId).setCurrentHP(100);
                stringBuilder.append("Позорное бегство, на самом деле не такое уж и позороное, если вы все еще живы\n");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Выводит статы
     *
     * @param currentId
     * @return статы
     */
    private String printStats(String currentId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n**Характеристики:**\n")
                .append(Main.arr.get(currentId).name).append(" уровень ").append(Main.arr.get(currentId).lvl).append("\n")
                .append(Main.arr.get(currentId).currentHP).append("\\").append(Main.arr.get(currentId).maxHP).append(" HP\n")
                .append(Main.arr.get(currentId).attack).append(" Урон\n")
                .append(Main.arr.get(currentId).def).append(" Броня\n")
                .append(Main.arr.get(currentId).eva).append(" Уклонение\n")
                .append(Main.arr.get(currentId).acc).append(" Точность\n");
        return stringBuilder.toString();
    }
}