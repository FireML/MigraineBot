package uk.firedev.migrainebot;

import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.firedev.migrainebot.config.Configuration;

public class Checks {

    public static boolean canUseAdminCommands(@Nullable Member member) {
        if (member == null) {
            return false;
        }
        return member.getIdLong() == 767886112177389599L ||
            member.getRoles().stream().anyMatch(role -> role.getIdLong() == Configuration.get().getSettingsRoleId());
    }

}
