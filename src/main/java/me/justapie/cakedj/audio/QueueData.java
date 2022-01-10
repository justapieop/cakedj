package me.justapie.cakedj.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Collections;
import java.util.LinkedList;

public class QueueData {
    private final LinkedList<AudioTrack> queue;
    private final LinkedList<AudioTrack> previousQueue;
    private boolean isInLoop = false;
    private boolean isEarrape = false;
    private boolean isNightcore = false;

    public QueueData() {
        this.queue = new LinkedList<>();
        this.previousQueue = new LinkedList<>();
    }

    public LinkedList<AudioTrack> getQueue() {
        return this.queue;
    }

    public LinkedList<AudioTrack> getPreviousQueue() {
        return this.previousQueue;
    }

    public void shuffle() {
        Collections.shuffle(this.queue);
    }

    public void swap(int a, int b) throws IndexOutOfBoundsException {
        Collections.swap(this.queue, a, b);
    }

    public boolean isEarrape() {
        return this.isEarrape;
    }

    public void setEarrape(boolean earrape) {
        this.isEarrape = earrape;
    }

    public boolean isInLoop() {
        return this.isInLoop;
    }

    public void setInLoop(boolean inLoop) {
        this.isInLoop = inLoop;
    }

    public boolean isNightcore() {
        return this.isNightcore;
    }

    public void setNightcore(boolean nightcore) {
        this.isNightcore = nightcore;
    }
}
