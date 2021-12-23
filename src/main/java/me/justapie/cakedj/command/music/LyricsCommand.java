package me.justapie.cakedj.command.music;

import com.github.connyscode.ctils.jTrack.Song;
import com.github.connyscode.ctils.jTrack.backend.SongNotFoundException;
import com.github.connyscode.ctils.jTrack.backend.types.SongSearchResult;
import com.github.connyscode.ctils.jTrack.jTrackClient;
import com.google.common.collect.Lists;
import me.justapie.cakedj.CakeDJ;
import me.justapie.cakedj.Context;
import me.justapie.cakedj.structure.Command;
import me.justapie.cakedj.utils.DiscordMarkdown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class LyricsCommand extends Command {
    private static final jTrackClient CLIENT = new jTrackClient(CakeDJ.getConfig().geniusKey());
    private static final Pattern REGEX = Pattern.compile("\\[(.*?)\\]", Pattern.CASE_INSENSITIVE);

    public LyricsCommand() {
        this.data = new CommandData("lyrics", "Get lyrics for a song")
                .addOption(OptionType.STRING, "name", "name of the song", true);
    }

    @Override
    public void execute(Context context) {
        String query = context.getOptions().get(0).getAsString();
        List<SongSearchResult> resultList = CLIENT.performSongSearch(query);
        if (resultList.isEmpty()) {
            context.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("No songs found")
                            .setDescription("No songs were found with the query " + DiscordMarkdown.singleQuote(query))
                            .build()
            ).queue();
            return;
        }

        SongSearchResult res = resultList.get(0);
        Song song;
        try {
            song = CLIENT.getSong(res);
        } catch (SongNotFoundException e) {
            context.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("No songs found")
                            .setDescription("No songs were found with the query " + DiscordMarkdown.singleQuote(query))
                            .build()
            ).queue();
            return;
        }
        String rawText = song.songLyrics().trim()
                .replaceAll(REGEX.pattern(), " ");
        IntStream rawLyrics = rawText.substring(0, rawText.length() - 506).chars();
        List<Character> characterList = rawLyrics.mapToObj((c) -> (char) c).collect(Collectors.toList());

        for (int i = characterList.size() - 1; characterList.get(i) != ' '; i--)
            characterList.remove(i);
        for (int i = characterList.size() - 1; characterList.get(i) == ' '; i--)
            characterList.remove(i);
        StringBuilder lyrics = new StringBuilder();
        for (Character ch : characterList)
            lyrics.append(ch);
        String finalLyrics = lyrics.toString();

        List<MessageEmbed> embeds = new ArrayList<>();
        List<List<String>> splits = Lists.partition(Arrays.asList(finalLyrics.split("\\n\\n")), 5);

        embeds.add(
                new EmbedBuilder()
                        .setTitle(song.songFullName())
                        .setDescription(String.join(" ", splits.get(0)))
                        .build()
        );

        for (int i = 1; i < splits.size(); i++) {
            embeds.add(
                    new EmbedBuilder()
                            .setDescription(String.join(" ", splits.get(i)))
                            .build()
            );
        }
        context.sendMessageEmbeds(embeds).queue();
    }
}
