package me.justapie.cakedj.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public final class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer audioPlayer;
    private final QueueData queueData;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.queueData = new QueueData();
    }

    public QueueData getQueueData() {
        return this.queueData;
    }

    public void enqueue(AudioTrack track) {
        if (!this.audioPlayer.startTrack(track, true))
            this.queueData.getQueue().add(track);
    }

    public void endTrack() {
        this.queueData.setInLoop(false);
        AudioTrack track = this.queueData.getQueue().poll();
        AudioTrack playing = this.audioPlayer.getPlayingTrack();
        if (playing != null) this.queueData.getPreviousQueue().add(playing.makeClone());
        audioPlayer.setPaused(false);
        audioPlayer.startTrack(track, false);
    }

    public void clear() {
        this.queueData.getQueue().clear();
        this.queueData.getPreviousQueue().clear();
    }

    public void previous() {
        AudioTrack playing = this.audioPlayer.getPlayingTrack();
        if (playing != null) this.queueData.getQueue().addFirst(playing.makeClone());
        this.audioPlayer.startTrack(this.queueData.getPreviousQueue().poll(), false);
    }

    public void restart() {
        this.audioPlayer.startTrack(this.audioPlayer.getPlayingTrack().makeClone(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (this.queueData.isInLoop()) this.queueData.getQueue().add(track.makeClone());
            else this.queueData.getPreviousQueue().add(track.makeClone());
            audioPlayer.startTrack(this.queueData.getQueue().poll(), false);
        }
    }
}
