package uk.firedev.migrainebot;

import uk.firedev.migrainebot.discord.MigraineBot;

import java.util.logging.Logger;

public class Main {

    public static final boolean TESTING = false;

    public static final long SERVER_ID = TESTING ? 1477810954686300303L : 1493002700067962912L;
    public static final long AUTOMOD_RULE_ID = TESTING ? 1518797860353605642L : 1496606212492497097L;
    public static final long LOG_CHANNEL_ID = TESTING ? 1496613182557520084L : 1493912068095479808L;

    public static final String INDIGENA_WEBHOOK = TESTING
        ? "https://discord.com/api/webhooks/1518823906889175041/50yatvQ2VRXVCNhJfut0KGwVLrUDuV4c_XEvnEq-k2Dtre1VeOsgIIgU05KntBaQu8JI"
        : "https://discord.com/api/webhooks/1518825903235530784/SnKLmQRAGSon0NQHoVwoeckdNT0BXnY9Jvruc03jHNRLqHNfmND3vs8UoQ2oy-3wrmwt";

    public static final String FEUD_WEBHOOK = TESTING
        ? "https://discord.com/api/webhooks/1518815789648580608/IO1-VpF494snOW07CI4i-K3V9CIicLsJmfdpKL-p2vWv7gBdcAO8jvIGb2GMZRZkYRcr"
        : "";

    static void main(String[] args) {
        MigraineBot.get().load();
        Main.getLogger().info("Loaded!");
    }

    public static Logger getLogger() {
        return Logger.getLogger("MigraineBot");
    }

}