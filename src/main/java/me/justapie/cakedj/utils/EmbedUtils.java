package me.justapie.cakedj.utils;

import me.justapie.cakedj.database.collections.ConfigCollection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import java.awt.*;
import java.util.Arrays;

public class EmbedUtils {
    public static void sendEmbed(SlashCommandEvent event, Color color, String desc) {
        MessageEmbed embed = new EmbedBuilder().setColor(color).setDescription(desc).build();
        ReplyAction action = event.deferReply();
        if (event.getMember().getUser().getId().equals(ConfigCollection.getConfig().ownerID()))
            action.setEphemeral(true);
        action.queue((resp) -> resp.sendMessageEmbeds(embed).queue());
    }

    public static void sendEmbed(SlashCommandEvent event, Color color, String desc, boolean ephemeral) {
        MessageEmbed embed = new EmbedBuilder().setColor(color).setDescription(desc).build();
        event.deferReply().setEphemeral(ephemeral).queue((resp) -> resp.sendMessageEmbeds(embed).queue());
    }

    public static void sendEmbed(SlashCommandEvent event, MessageEmbed... embed) {
        ReplyAction action = event.deferReply();
        if (event.getMember().getUser().getId().equals(ConfigCollection.getConfig().ownerID()))
            action.setEphemeral(true);
        action.queue((resp) -> resp.sendMessageEmbeds(Arrays.asList(embed)).queue());
    }

    public static void sendEmbed(SlashCommandEvent event, boolean ephemeral, MessageEmbed... embed) {
        event.deferReply().setEphemeral(ephemeral).queue((resp) -> resp.sendMessageEmbeds(Arrays.asList(embed)).queue());
    }

    public static MessageEmbed createEmbed(Color color, String desc) {
        return new EmbedBuilder().setColor(color).setDescription(desc).build();
    }
}
