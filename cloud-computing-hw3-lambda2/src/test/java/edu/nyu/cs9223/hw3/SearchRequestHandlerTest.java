package edu.nyu.cs9223.hw3;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.nyu.cs9223.hw3.chatbot.ChatBot;
import edu.nyu.cs9223.hw3.chatbot.ChatBotFactory;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SearchRequestHandlerTest {

    @org.junit.Test
    public void handleRequest() {
//        ChatBot chatBot = ChatBotFactory.getInstance();
//        List<String> replies = chatBot.postText("test1", "SEARCH");
//        for (String reply: replies) {
//            System.out.println(reply);
//        }
    }

    @org.junit.Test
    public void json() {
        JsonObject jsonObject = JsonParser.parseString("{\n  \"_index\" : \"photos\",\n  \"_type\" : \"_doc\",\n  \"_id\" : \"1\",\n  \"_score\" : 0.2876821,\n  \"_source\" : {\n    \"objectKey\" : \"IMG_8686.JPG\",\n    \"bucket\" : \"hw3-photos-storage\",\n    \"createdTimestamp\" : 1575568483040,\n    \"labels\" : [\n      \"Text\",\n      \"Human\",\n      \"Person\",\n      \"Document\",\n      \"Id Cards\"\n    ]\n  }\n}")
                .getAsJsonObject().get("_source").getAsJsonObject();
        System.out.println(jsonObject);
    }
}