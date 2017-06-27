package com.example.sweth.inclass06;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class GamesUtil {
    static  class GamesListPullParser{

        static ArrayList<Game> parseGame(InputStream in) throws XmlPullParserException, IOException {

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            ArrayList<Game> gameArrayList = new ArrayList<Game>();
            Game game  =null;

            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("Game")){
                            game = new Game();
                        }else if(parser.getName().equals("id") && game != null){
                            game.setId(parser.nextText().trim());
                        }else if(parser.getName().equals("GameTitle")&& game != null){
                            game.setGameTitle(parser.nextText().trim());
                        }else if(parser.getName().equals("ReleaseDate")&& game != null){
                            game.setReleaseDate(parser.nextText().trim());
                        }else if(parser.getName().equals("Platform")&& game != null) {
                            game.setPlatform(parser.nextText().trim());
                        }else if(parser.getName().equals("clearlogo")&& game != null) {
                            game.setClearLogo(parser.nextText().trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("Game")){
                            gameArrayList.add(game);
                            game =null;
                        }
                        break;
                }
                event = parser.next();
            }
            return gameArrayList;
        }
    }

    static class GamePullParser{
        static Game parseGame(InputStream in) throws XmlPullParserException, IOException {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            Game game  =null;

            ArrayList<Game> similarGames = new ArrayList<Game>();
            Log.d("demo","parsing the game");
            int event = parser.getEventType();
            String imgUrl = null;
            String imgFanart = null;
            int imgFlag = 0;
            int idFlag=0;
            while(event != XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("Data")){
                            game = new Game();
                        }else if(parser.getName().equals("baseImgUrl") && game != null){
                            imgUrl = parser.nextText();
                        }else if(parser.getName().equals("GameTitle")&& game != null){
                            game.setGameTitle(parser.nextText().trim());
                        }else if(parser.getName().equals("Platform")&& game != null){
                            game.setPlatform(parser.nextText().trim());
                        }else if(parser.getName().equals("ReleaseDate")&& game != null){
                            game.setReleaseDate(parser.nextText().trim());
                        }else if(parser.getName().equals("Overview")&& game != null){
                            game.setOverview(parser.nextText().trim());
                        }else if(parser.getName().equals("genre")&& game != null) {
                            game.setGenre(parser.nextText().trim());
                        }else if(parser.getName().equals("Youtube")&& game != null) {
                            game.setTrailerLink(parser.nextText().trim());
                        }else if(parser.getName().equals("Publisher")&& game != null) {
                            game.setPublisher(parser.nextText().trim()+"Sports");
                        } else if(parser.getName().equals("original")&& game != null) {
                            if(imgFlag==0){
                                imgFanart = parser.nextText();
                                imgFlag++;
                            }
                        }else if(parser.getName().equals("boxart")&& game != null) {
                            if(imgFlag==0){
                                imgFanart = parser.nextText();
                                imgFlag++;
                            }
                        }else if(parser.getName().equals("id")&& game != null) {

                            if(idFlag==0) {
                                game.setId(parser.nextText());
                                idFlag++;
                            }else {

                                Game simGame = new Game();
                                simGame.setId(parser.nextText());
                                similarGames.add(simGame);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        /*if(parser.getName().equals("Data")){
                            similarGames.add(simGame);
                            simGame =null;
                        }*/

                        break;
                }
                event = parser.next();
            }
            game.setBaseImgUrl(imgUrl+imgFanart);
            game.setSimilarGames(similarGames);
            Log.d("demo","the image url is "+imgUrl+imgFanart);
            Log.d("demo","the game details are "+game.toString());
            for(Game G:similarGames)
                Log.d("demo","the similar ids are "+G.getId());
            return game;
        }
    }
}
