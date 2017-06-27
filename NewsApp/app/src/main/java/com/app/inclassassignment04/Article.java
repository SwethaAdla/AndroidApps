package com.app.inclassassignment04;

import org.json.JSONException;
import org.json.JSONObject;



public class Article {

    String author,title,description,url,urlToImage,publishedAt;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString() {
        return "Article{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }

    public static Article createArticle(JSONObject jsonObject) throws JSONException {
        Article article = new Article();
        article.setAuthor(jsonObject.getString("author"));
        article.setDescription(jsonObject.getString("description"));
        article.setPublishedAt(jsonObject.getString("publishedAt"));
        article.setTitle(jsonObject.getString("title"));
        article.setUrl(jsonObject.getString("url"));
        article.setUrlToImage(jsonObject.getString("urlToImage"));

        return article;
    }
}