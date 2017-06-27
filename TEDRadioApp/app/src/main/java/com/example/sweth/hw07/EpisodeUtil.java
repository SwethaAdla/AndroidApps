package com.example.sweth.hw07;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class EpisodeUtil {
    static  class EpisodeListPullParser{

        static ArrayList<Episode> parseEpisode(InputStream in) throws XmlPullParserException, IOException {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            XmlPullParser parser = factory.newPullParser();
            parser.setInput(in, "UTF-8");
            ArrayList<Episode> episodeArrayList = new ArrayList<Episode>();
            Episode episode  =null;

            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equalsIgnoreCase("item")){
                            episode = new Episode();
                        }else if(parser.getName().equalsIgnoreCase("title") && episode != null){
                            episode.setEpisodeTitle(parser.nextText().trim());
                        }else if(parser.getName().equalsIgnoreCase("description")&& episode != null){
                            episode.setDescription(parser.nextText().trim());
                        }else if(parser.getName().equalsIgnoreCase("pubdate")&& episode != null){
                            String s =parser.nextText().trim();
                            episode.setPublishedDate(getDateVal(s));
                            episode.setPostedOn(s.substring(0,16));
                        }else if(parser.getName().equalsIgnoreCase("duration")&& episode != null){
                           episode.setDuration(parser.nextText().trim());
                        }else if(parser.getName().equalsIgnoreCase("image")&& episode != null){
                            episode.setImageUrl(parser.getAttributeValue(0).trim());
                        }else if(parser.getName().equalsIgnoreCase("enclosure")&& episode != null) {
                            episode.setPlayLink(parser.getAttributeValue(0).trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            episodeArrayList.add(episode);
                            episode =null;
                        }
                        break;
                }
                event = parser.next();
            }
            return episodeArrayList;
        }
    }

    public static java.util.Date getDateVal(String s){
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        java.util.Date startDate = null;
        try {
            startDate = df.parse(s);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }
}
