package me.justapie.cakedj;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Listener.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        log.info("Logged in as " + event.getJDA().getSelfUser().getAsTag());
    }
}
