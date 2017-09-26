package xyz.deseteral.djlama.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.managers.AudioManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.deseteral.djlama.play.PlayService;

import javax.security.auth.login.LoginException;

@Component
public class DiscordBot {
    private String token;
    private String channelName;

    private JDA api;

    private AudioPlayerManager playerManager;
    private AudioPlayer audioPlayer;

    public DiscordBot(
        @Value("${DISCORD_TOKEN}") String token,
        @Value("${DISCORD_CHANNEL_NAME}") String channelName
    ) {
        this.token = token;
        this.channelName = channelName;
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

        TrackScheduler trackScheduler = new TrackScheduler();
        audioPlayer.addListener(trackScheduler);
    }

    public void play(String id) {
        if (api == null) {
            try {
                connect();
            } catch (Exception e) {
                // TODO: Handle exception
                e.printStackTrace();
            }
        }

        playerManager.loadItem(id, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                audioPlayer.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) { }

            @Override
            public void noMatches() { }

            @Override
            public void loadFailed(FriendlyException throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
