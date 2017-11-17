package xyz.deseteral.djlama.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.managers.AudioManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.deseteral.djlama.queue.Queue;
import xyz.deseteral.djlama.song.Song;
import xyz.deseteral.djlama.song.SongService;

import javax.security.auth.login.LoginException;
import java.time.Instant;

@Component
public class DiscordBot {
    private String token;
    private String channelName;

    private JDA api;

    private Queue queue;
    private SongService songService;

    private AudioPlayerManager playerManager;
    private AudioPlayer audioPlayer;

    private TrackScheduler trackScheduler;

    private static final int FADE_INTERVAL = 5;
    private static final int FADE_THRESHOLD = 10;

    @Autowired
    public DiscordBot(
        @Value("${DISCORD_TOKEN}") String token,
        @Value("${DISCORD_CHANNEL_NAME}") String channelName,
        Queue queue,
        SongService songService
    ) {
        this.token = token;
        this.channelName = channelName;
        this.queue = queue;
        this.songService = songService;
    }

    private void connect() throws LoginException, InterruptedException, RateLimitedException {
        api = new JDABuilder(AccountType.BOT)
            .setToken(token)
            .buildBlocking();

        final Guild guild = api.getGuilds().get(0);
        final VoiceChannel voiceChannel = guild
            .getVoiceChannelsByName(channelName, true)
            .get(0);

        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);

        final AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(voiceChannel);

        audioPlayer = playerManager.createPlayer();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));

        trackScheduler = new TrackScheduler(queue, songService, audioPlayer, playerManager);
        audioPlayer.addListener(trackScheduler);
    }

    public void play() {
        if (api == null) {
            try {
                connect();
            } catch (Exception e) {
                // TODO: Handle exception
                e.printStackTrace();
            }
        }

        if (audioPlayer.getPlayingTrack() == null) {
            trackScheduler.playNext();
        }
    }

    public Song getCurrentlyPlaying() {
        if (audioPlayer != null && audioPlayer.getPlayingTrack() != null && trackScheduler != null) {
            return trackScheduler.getCurrentlyPlaying();
        }

        return null;
    }

    public Instant getStartTime() {
        if (trackScheduler != null) {
            return trackScheduler.getStartTime();
        }

        return null;
    }

    public void skip() {
        if (trackScheduler != null) {
            audioPlayer.stopTrack();
            trackScheduler.playNext();
        }
    }

    public void fadeOut() {
        fadeTo(FADE_THRESHOLD, -1);
    }

    public void fadeIn() {
        fadeTo(100, +1);
    }

    private void fadeTo(int targetVolume, int deltaVolume) {
        if (audioPlayer == null) {
            return;
        }

        new Thread(() -> {
            while (audioPlayer.getVolume() != targetVolume) {
                audioPlayer.setVolume(audioPlayer.getVolume() + deltaVolume);
                try {
                    Thread.sleep(FADE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Queue getQueue() {
        return queue;
    }
}
