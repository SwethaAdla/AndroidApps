package com.app.inclassassignment04;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class ArticlesUtil {

    static public class articleJSONParser {
        static ArrayList<Article> parseArticles(String in) throws JSONException {
            ArrayList<Article> articlesList = new ArrayList<Article>();
            JSONObject root = new JSONObject(in);
            JSONArray articleArry = root.getJSONArray("articles");
            for (int i=0; i<articleArry.length(); i++){
                JSONObject articleJSONObj = articleArry.getJSONObject(i);
                Article article = Article.createArticle(articleJSONObj);
                articlesList.add(article);
            }
            return articlesList;
        }
    }
}