package me.justapie.cakedj.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter {
    private final LinkedList<AudioTrack> queue;
    private final LinkedList<AudioTrack> previous;
    private final AudioPlayer audioPlayer;

    public boolean isInLoop = false;
    public boolean isEarrape = false;
    public boolean isNightcore = false;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.queue = new LinkedList<>();
        this.previous = new LinkedList<>();
        this.audioPlayer = audioPlayer;
    }

    public void enqueue(AudioTrack track) {
        if (!this.audioPlayer.startTrack(track, true))
            this.queue.add(track);
    }

    public void endTrack() {
        this.onTrackEnd(this.audioPlayer, this.audioPlayer.getPlayingTrack(), AudioTrackEndReason.FINISHED);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (isInLoop) this.queue.add(track.makeClone());
            else this.previous.add(track.makeClone());
            audioPlayer.startTrack(this.queue.poll(), false);
        }
    }
}
