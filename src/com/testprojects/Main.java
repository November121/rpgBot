package com.testprojects;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args){
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken("MzM2Nzg5NTQ0MjYxMjU1MTY4.DFpqzQ.RGbU69ENtrWAodmEclcXWocYWRc")
                    .addEventListener(new RpgBot())
                    .buildBlocking();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    static HashMap<String, UserInterface> arr = new HashMap<>();
}
