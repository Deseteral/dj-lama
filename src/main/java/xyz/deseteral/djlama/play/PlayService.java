package xyz.deseteral.djlama.play;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.springframework.stereotype.Component;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

@Component
public class PlayService {
    private AudioPlayer audioPlayer;
    private AudioPlayerManager playerManager;

    PlayService() throws Exception {
        final String DISCORD_TOKEN = System.getenv("DISCORD_TOKEN");
        final String DISCORD_CHANNEL_NAME = System.getenv("DISCORD_CHANNEL_NAME");

        final JDA api = new JDABuilder(AccountType.BOT)
            .setToken(DISCORD_TOKEN)
            .buildBlocking();

        // guild is the server
        final Guild guild = api.getGuilds().get(0);

        // find voice channel and connect
        final VoiceChannel voiceChannel = guild.getVoiceChannelsByName(DISCORD_CHANNEL_NAME, true).get(0);

        // do the audio stuff
        final AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(voiceChannel);


        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);

        // get the thing that actually plays the audio
        audioPlayer = playerManager.createPlayer();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
    }

    void play(String id) {
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
            public void loadFailed(FriendlyException exception) { }
        });
    }

    class AudioPlayerSendHandler implements AudioSendHandler {
        private final AudioPlayer audioPlayer;
        private AudioFrame lastFrame;

        AudioPlayerSendHandler(AudioPlayer audioPlayer) {
            this.audioPlayer = audioPlayer;
        }

        @Override
        public boolean canProvide() {
            lastFrame = audioPlayer.provide();
            return lastFrame != null;
        }

        @Override
        public byte[] provide20MsAudio() {
            return lastFrame.data;
        }

        @Override
        public boolean isOpus() {
            return true;
        }
    }
}
