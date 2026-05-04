package uk.firedev.migrainebot.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.automod.AutoModRule;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Main;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DisablePingTask extends TimerTask {

    private final Timer timer = new Timer();
    private final Set<Integer> allowedHours = Set.of(
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16
    );
    private final Set<DayOfWeek> disallowedDays = Set.of(
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    );

    private Boolean isMentionable = null;

    private static final DisablePingTask INSTANCE = new DisablePingTask();

    private DisablePingTask() {}

    public static @NotNull DisablePingTask getInstance() {
        return INSTANCE;
    }

    public void start() {
        timer.scheduleAtFixedRate(this, 0L, TimeUnit.MINUTES.toMillis(1));
    }

    @Override
    public void run() {
        ZoneId zone = TimeZone.getTimeZone("America/New_York").toZoneId();
        LocalDateTime time = LocalDateTime.now(zone);
        DayOfWeek currentDay = time.getDayOfWeek();
        int currentHour = time.getHour();

        if (allowedHours.contains(currentHour) && !disallowedDays.contains(currentDay)) {
            enablePing();
        } else {
            disablePing();
        }
    }

    private void disablePing() {
        if (isMentionable == null || isMentionable) {
            getLogChannel().sendMessage("Staff can no longer be pinged.").queue();
            Main.getLogger().info("AutoMod Rule Enabled, Staff can no longer be pinged :D");
            getRule().getManager().setEnabled(true).queue();
            isMentionable = false;
        }
    }

    private void enablePing() {
        if (isMentionable == null || !isMentionable) {
            getLogChannel().sendMessage("Staff can now be pinged.").queue();
            Main.getLogger().info("AutoMod Rule Disabled, Staff can now be pinged :D");
            getRule().getManager().setEnabled(false).queue();
            isMentionable = true;
        }
    }

    private AutoModRule getRule() {
        Guild guild = MigraineBot.get().getBot().getGuildById(Main.SERVER_ID);
        if (guild == null) {
            throw new RuntimeException("Invalid Guild.");
        }
        AutoModRule rule = guild.retrieveAutoModRuleById(Main.AUTOMOD_RULE_ID).complete();
        if (rule == null) {
            throw new RuntimeException("Invalid AutoMod Rule.");
        }
        return rule;
    }

    private TextChannel getLogChannel() {
        GuildChannel channel = MigraineBot.get().getBot().getGuildChannelById(Main.LOG_CHANNEL_ID);
        if (!(channel instanceof TextChannel text)) {
            throw new RuntimeException("Log channel isn't a text channel");
        }
        return text;
    }

}
