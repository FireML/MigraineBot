package uk.firedev.migrainebot.config;

import de.bsommerfeld.jshepherd.annotation.Comment;
import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.annotation.Section;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;

public class Configuration extends ConfigurablePojo<Configuration> {

    public enum Environment { TESTING, PROD }

    @Key("bot-token")
    @Comment("The discord bot token.")
    public String botToken;

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
