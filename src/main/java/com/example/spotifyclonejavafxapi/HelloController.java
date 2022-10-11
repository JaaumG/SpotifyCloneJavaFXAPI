package com.example.spotifyclonejavafxapi;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

public class HelloController implements Initializable {
    @FXML
    private Label musica, artista;

    @FXML
    private ImageView album;

    private static final String accessToken = "";
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();
    private static final GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingTrackRequest = spotifyApi
            .getUsersCurrentlyPlayingTrack()
//          .market(CountryCode.SE)
//          .additionalTypes("track,episode")
            .build();

    private void getMusic(){
        try {
            final CompletableFuture<CurrentlyPlaying> currentlyPlayingFuture = getUsersCurrentlyPlayingTrackRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final CurrentlyPlaying currentlyPlaying = currentlyPlayingFuture.join();
            Track track = spotifyApi.getTrack(currentlyPlaying.getItem().getId()).build().execute();
            musica.setText(track.getName());
            artista.setText("");
            for (ArtistSimplified artistas:track.getArtists()) {
                artista.setText(artista.getText() + artistas.getName()+", ");
            }
            album.setImage(new javafx.scene.image.Image(track.getAlbum().getImages()[0].getUrl()));
            System.out.println(TimeUnit.MILLISECONDS.toMinutes(currentlyPlaying.getProgress_ms())+":"+(TimeUnit.MILLISECONDS.toSeconds(currentlyPlaying.getProgress_ms()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentlyPlaying.getProgress_ms()))));
            System.out.println(TimeUnit.MILLISECONDS.toMinutes(track.getDurationMs())+":"+(TimeUnit.MILLISECONDS.toSeconds(track.getDurationMs()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.getDurationMs()))));
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getMusic();
    }
}
