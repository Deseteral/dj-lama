package xyz.deseteral.djlama.integration

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import xyz.deseteral.djlama.song.Song
import xyz.deseteral.djlama.web.Error

class SongIntegrationSpec extends IntegrationSpec {

    def "should post song"() {
        given:
        def song = [
                title: "song title",
                artist: "song artist",
                youtubeId: "youtube-id"
        ]

        def postRequest = RequestEntity.post(localURI('/songs'))
            .contentType(MediaType.APPLICATION_JSON)
            .body(song)

        when:
        def postResponse = restTemplate.exchange(postRequest, Void)

        then:
        postResponse.statusCode == HttpStatus.CREATED

        and: "get results"
        def getRequest = RequestEntity.get(localURI('/songs'))
            .accept(MediaType.APPLICATION_JSON);

        when:
        def getResponse = restTemplate.getForEntity(localURI('/songs'), Song[])

        then:
        Song[] songs = (Song[]) getResponse.body
        songs.length == 1
        with (songs[0]) {
            title == "song title"
            artist == "song artist"
            youtubeId == "youtube-id"
            playCount == 0
        }
    }

    def "should get song"() {
        setup:
        def song = [
            title: "song title",
            artist: "song artist",
            youtubeId: "youtube-id",
            playCount: 12
        ]

        def postRequest = RequestEntity.post(localURI('/songs'))
            .contentType(MediaType.APPLICATION_JSON)
            .body(song)

        def postResponse = restTemplate.exchange(postRequest, Void)

        def location = postResponse.headers['Location'][0]

        when:
        def getResponse = restTemplate.getForEntity(localURI(location), Song)

        then:
        Song responseSong = getResponse.body
        with (responseSong) {
            title == "song title"
            artist == "song artist"
            youtubeId == "youtube-id"
            playCount == 12
        }
    }

    def "should update song"() {
        setup:
        def song = [
            title: "song title",
            artist: "song artist",
            youtubeId: "youtube-id",
            playCount: 12
        ]

        def postRequest = RequestEntity.post(localURI('/songs'))
            .contentType(MediaType.APPLICATION_JSON)
            .body(song)

        def postResponse = restTemplate.exchange(postRequest, Void)

        def location = postResponse.headers['Location'][0]

        def updateSong = [
            title: "updated song title",
            artist: "new song artist",
            youtubeId: "fresh-youtube-id",
            playCount: 42
        ]

        def putRequest = RequestEntity.put(localURI(location))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updateSong)

        when:
        def putResponse = restTemplate.exchange(putRequest, Void)

        then:
        putResponse.statusCode == HttpStatus.ACCEPTED

        and:
        def getResponse = restTemplate.getForEntity(localURI(location), Song)

        Song responseSong = getResponse.body
        with (responseSong) {
            title == "updated song title"
            artist == "new song artist"
            youtubeId == "fresh-youtube-id"
            playCount == 42
        }
    }

    def "should return 404 on accessing not existing resource"() {
        when:
        def getResponse = restTemplate.getForEntity(localURI('/songs/fake-id'), Error)

        then:
        Error e = (Error) getResponse.body
        getResponse.statusCode == HttpStatus.NOT_FOUND

        with (e) {
            e.status == 404
            e.key == "RESOURCE_NOT_FOUND"
        }
    }

    def "should return 404 on updating not existing resource"() {
        given:
        def updateSong = [
            title: "updated song title",
            artist: "new song artist",
            youtubeId: "fresh-youtube-id",
            playCount: 42
        ]

        def putRequest = RequestEntity.put(localURI('/songs/fake-id'))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updateSong)

        when:
        def putResponse = restTemplate.exchange(putRequest, Error)

        then:
        Error e = (Error) putResponse.body
        putResponse.statusCode == HttpStatus.NOT_FOUND

        with (e) {
            e.status == 404
            e.key == "RESOURCE_NOT_FOUND"
        }
    }
}
