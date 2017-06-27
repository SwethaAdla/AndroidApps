package com.example.sweth.inclass06;

import java.util.ArrayList;

public class Game {
    String id, gameTitle, releaseDate, platform , baseImgUrl, overview, genre, publisher, trailerLink, platformId, clearLogo;
    ArrayList<Game> similarGames= new ArrayList<Game>();

    public String getClearLogo() {
        return clearLogo;
    }

    public void setClearLogo(String clearLogo) {
        this.clearLogo = clearLogo;
    }

    public ArrayList<Game> getSimilarGames() {
        return similarGames;
    }

    public void setSimilarGames(ArrayList<Game> similarGames) {
        this.similarGames = similarGames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlatform() {
        return platform;
    }

    public String getBaseImgUrl() {
        return baseImgUrl;
    }

    public void setBaseImgUrl(String baseImgUrl) {
        this.baseImgUrl = baseImgUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }


    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", gameTitle='" + gameTitle + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", platform='" + platform + '\'' +
                ", baseImgUrl='" + baseImgUrl + '\'' +
                ", overview='" + overview + '\'' +
                ", genre='" + genre + '\'' +
                ", publisher='" + publisher + '\'' +
                ", trailerLink='" + trailerLink + '\'' +
                ", platformId='" + platformId + '\'' +
                ", clearLogo='" + clearLogo + '\'' +
                ", similarGames=" + similarGames +
                '}';
    }
}
