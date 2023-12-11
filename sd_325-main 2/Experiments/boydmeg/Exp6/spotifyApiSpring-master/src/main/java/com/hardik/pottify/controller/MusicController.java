package com.hardik.pottify.controller;

import javax.servlet.http.HttpSession;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hardik.pottify.constant.ApiPath;
import com.hardik.pottify.constant.Template;
import com.hardik.pottify.exception.NoAccountDataException;
import com.hardik.pottify.exception.NoAlbumSavedException;
import com.hardik.pottify.exception.NoTrackSavedException;
import com.hardik.pottify.service.FeaturedPlaylistService;
import com.hardik.pottify.service.NewReleasedService;
import com.hardik.pottify.service.RecentPlayesTrackService;
import com.hardik.pottify.service.SavedAlbumService;
import com.hardik.pottify.service.SavedTrackService;
import com.hardik.pottify.service.TopArtistService;
import com.hardik.pottify.service.TopTrackService;
import com.hardik.pottify.utility.TermPeriodUtility;

import lombok.AllArgsConstructor;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class MusicController {

	private final TopTrackService topTrackService;
	private final TopArtistService topArtistService;
	private final SavedTrackService savedTrackService;
	private final SavedAlbumService savedAlbumService;
	private final NewReleasedService newReleasedService;
	private final RecentPlayesTrackService recentPlayedTrackService;
	private final FeaturedPlaylistService featuredPlaylistService;


	@GetMapping(value = ApiPath.TOP_TRACKS, produces = MediaType.TEXT_HTML_VALUE)
	public String topTracksaHandler(@RequestParam("term") final Integer term, final HttpSession session,
			final Model model) {
		try {
			model.addAttribute("tracks",
					topTrackService.getTopTracks((String) session.getAttribute("accessToken"), term));
			model.addAttribute("term", TermPeriodUtility.getTerm(term));
		} catch (NoAccountDataException exception) {
			return Template.NO_DATA;
		}
		return Template.TOP_TRACKS;
	}

	@GetMapping(value = ApiPath.TOP_ARTISTS, produces = MediaType.TEXT_HTML_VALUE)
	public String topArtistsHandler(@RequestParam("term") final Integer term, final HttpSession session,
			final Model model) {
		try {
			model.addAttribute("artists",
					topArtistService.getTopArtists((String) session.getAttribute("accessToken"), term));
			model.addAttribute("term", TermPeriodUtility.getTerm(term));
		} catch (NoAccountDataException exception) {
			return Template.NO_DATA;
		}
		return Template.TOP_ARTISTS;
	}

	@GetMapping(value = ApiPath.SAVED_TRACKS, produces = MediaType.TEXT_HTML_VALUE)
	public String savedTracksHandler(final HttpSession session, final Model model) {
		try {
			model.addAttribute("tracks", savedTrackService.getTracks((String) session.getAttribute("accessToken")));
		} catch (NoTrackSavedException exception) {
			return Template.NO_TRACK_SAVED;
		}
		return Template.SAVED_TRACKS;
	}

	@GetMapping(value = ApiPath.SAVED_ALBUMS, produces = MediaType.TEXT_HTML_VALUE)
	public String savedAlbumsHandler(final HttpSession session, final Model model) {
		try {
			model.addAttribute("albums", savedAlbumService.getAlbums((String) session.getAttribute("accessToken")));
		} catch (NoAlbumSavedException exception) {
			return Template.NO_ALBUM_SAVED;
		}
		return Template.SAVED_ALBUMS;
	}

	@GetMapping(value = ApiPath.NEW_RELEASE, produces = MediaType.TEXT_HTML_VALUE)
	public String newReleasesHandler(final HttpSession session, final Model model) {
		model.addAttribute("releases", newReleasedService.getReleases((String) session.getAttribute("accessToken")));
		return Template.NEW_RELEASES;
	}

	@GetMapping(value = ApiPath.RECENT_TRACKS, produces = MediaType.TEXT_HTML_VALUE)
	public String recentTracksHandler(final HttpSession session, final Model model) {
		model.addAttribute("tracks", recentPlayedTrackService.getHistory((String) session.getAttribute("accessToken")));
		return Template.RECENT_TRACKS;
	}

	@GetMapping(value = ApiPath.FEATURED_PLAYLIST, produces = MediaType.TEXT_HTML_VALUE)
	public String featuredPlaylistsHandler(final HttpSession session, final Model model) {
		model.addAttribute("playlists",
				featuredPlaylistService.getPlaylists((String) session.getAttribute("accessToken")));
		return Template.FEATURED_PLAYLISTS;
	}

	@GetMapping(value = "testSong")
	public String getSong() {

		//our apps specific info
		//valid for around an hour, and then request will need to be remade
		String client_id = "c1674e1ab75943bd8b230ee88a57f47f";
		String client_secret = "ef45d175b89e4cbbbe22879df19fce63";

		//setting parameters
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("grant_type", "client_credentials");


		String form = parameters.keySet().stream()
				.map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
				.collect(Collectors.joining("&"));

		String header = client_id + ":"  + client_secret;


		try {

			//init our http client and request
			//endcode client credientials
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest req = HttpRequest.newBuilder()
					//.uri(new URI("https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg"))
					.uri(new URI("https://accounts.spotify.com/api/token"))
					.headers("Content-Type", "application/x-www-form-urlencoded","Authorization","Basic " +  Base64.getEncoder().encodeToString(header.getBytes()))
					.POST(HttpRequest.BodyPublishers.ofString(form)).build();
			HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
			System.out.println(response);
			//return response.toString();
			//HttpResponse<?> response = client.send(request, BodyHandlers.ofString());
			System.out.println(response.statusCode() + response.body());

			//response returned as json object
			JSONObject obj = new JSONObject(response.body());

			//grab value for access_token field, which we may then use to make requests
			//yay!!!
			var token = obj.getString("access_token");


			//example to find artist Taylor Swift albums, id : 06HL4z0CvFAxyc27GXpf02
			//https://api.spotify.com/v1/artists/06HL4z0CvFAxyc27GXpf02/albums

			//make new http request
			HttpRequest albums = HttpRequest.newBuilder()
					.uri(new URI("https://api.spotify.com/v1/artists/06HL4z0CvFAxyc27GXpf02/albums"))
					.header("Authorization", "Bearer " + token)
					.GET().build();

			//get response
			HttpResponse<String> album_resp = client.send(albums, HttpResponse.BodyHandlers.ofString());

			//expect: a list of all taylor swift albums
			System.out.println(album_resp.body());


			//lets try requesting a specific song
			//song id 1p80LdxRV74UKvL8gnD7ky or blank space
			HttpRequest song = HttpRequest.newBuilder()
					.uri(new URI("https://api.spotify.com/v1/tracks/1p80LdxRV74UKvL8gnD7ky"))
					.header("Authorization", "Bearer " + token)
					.GET().build();

			//get response
			HttpResponse<String> song_resp = client.send(song, HttpResponse.BodyHandlers.ofString());

			//expect: a list of all taylor swift albums
			System.out.println(song_resp.body());


			//lets try grabbing and printing the artist for this song
			JSONObject arr = new JSONObject(song_resp.body());


			//create a new json array full of artist info
			JSONArray artist = new JSONArray(arr.getString("artists"));

			//json object external urls contains the name of the artist, so create the object
			JSONObject extern_url = new JSONObject(String.valueOf(artist.getJSONObject(0)));

			//print artist name
			//(JSONObject.getString(string value) lets you get the value from the key provied
			//here the key is "name" and we're extracting the value, "Taylor Swift"
			System.out.println(extern_url.getString("name"));

			return Template.FEATURED_PLAYLISTS;
			//return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("error");
			//return "error";
			return Template.TOP_ARTISTS;
			//return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
