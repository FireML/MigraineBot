package uk.firedev.migrainebot.config;

import de.bsommerfeld.jshepherd.annotation.Comment;
import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.annotation.Section;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;

public class Configuration extends ConfigurablePojo<Configuration> {

    @Key("bot-token")
    @Comment("The discord bot token.")
    public String botToken;

    @Key("server-id")
    @Comment("The ID of the server to operate in.")
    public long serverId;

    @Key("automod-rule-id")
    @Comment("The ID of the automod rule to toggle. (WoozStaff ping toggle)")
    public long autoModRuleId;

    @Key("log-channel-id")
    @Comment("The ID of the channel to log to.")
    public long logChannelId;

    @Key("settings-role-id")
    @Comment("The role that can use the settings command.")
    public long settingsRoleId;

    @Comment("Webhook settings")
    @Section("webhooks")
    public WebhookSettings webhooks = new WebhookSettings();

    public static class WebhookSettings {

        @Key("indigena")
        @Comment("Indigena Webhook")
        public String indigena;

        @Key("build-comp")
        @Comment("Build Comp Webhook")
        public String buildComp;

    }

}
