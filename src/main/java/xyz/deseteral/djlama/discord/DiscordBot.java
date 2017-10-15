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

import javax.security.auth.login.LoginException;
import java.time.Instant;

@Component
public class DiscordBot {
    private String token;
    private String channelName;

    private JDA api;

    private Queue queue;

    private AudioPlayerManager playerManager;
    private AudioPlayer audioPlayer;

    private TrackScheduler trackScheduler;

    @Autowired
    public DiscordBot(
        @Value("${DISCORD_TOKEN}") String token,
        @Value("${DISCORD_CHANNEL_NAME}") String channelName,
        Queue queue
    ) {
        this.token = token;
        this.channelName = channelName;
        this.queue = queue;
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

        trackScheduler = new TrackScheduler(queue, audioPlayer, playerManager);
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

    public Queue getQueue() {
        return queue;
    }
}
