package edu.nyu.cs9223.hw3.chatbot;

import com.amazonaws.services.lexruntime.AmazonLexRuntimeClient;
import com.amazonaws.services.lexruntime.model.*;
import com.amazonaws.util.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wuweiran
 */
public class ChatBot {
    private final String botName;

    private final String botAlias;

    private final AmazonLexRuntimeClient lexRuntimeClient;

    public ChatBot(AmazonLexRuntimeClient lexRuntimeClient, String botName, String botAlias) {
        this.lexRuntimeClient = lexRuntimeClient;
        this.botName = botName;
        this.botAlias = botAlias;
    }

    /**
     * @param userId user ID
     * @param text   chat text to send to lex
     * @return message that the chat bot responds
     */
    public List<String> postText(String userId, String text) {

        PostTextRequest postTextRequest = new PostTextRequest();

        postTextRequest
                .withBotName(botName)
                .withBotAlias(botAlias)
                .withUserId(userId)
                .withInputText(text)
                .withRequestAttributes(null)
                .withSessionAttributes(null);
        PostTextResult result = null;
        try {
            result = lexRuntimeClient.postText(postTextRequest);
        } catch (NotFoundException nfe) {
            putSession(userId);
            result = lexRuntimeClient.postText(postTextRequest);
        }

        List<String> response = new LinkedList<>();
        if (result != null) {
            if (MessageFormatType.Composite.toString().equals(result.getMessageFormat())) {
                JsonArray messages = JsonParser.parseString(result.getMessage()).getAsJsonObject()
                        .getAsJsonArray("messages");

                for (JsonElement element : messages) {
                    JsonObject message = element.getAsJsonObject();
                    response.add(message.get("value").getAsString());
                }
            } else if (MessageFormatType.PlainText.toString().equals(result.getMessageFormat())) {
                response.add(result.getMessage());
            } else {
                response.add(result.getMessage());
            }
        }
        return response;
    }

    public boolean resetChat(String userId) {
        try {
            DeleteSessionRequest deleteSessionRequest = new DeleteSessionRequest();
            deleteSessionRequest
                    .withBotName(botName)
                    .withBotAlias(botAlias)
                    .withUserId(userId);
            lexRuntimeClient.deleteSession(deleteSessionRequest);
            return putSession(userId);
        } catch (NotFoundException nfe) {
            return putSession(userId);
        }
    }

    private boolean putSession(String userId) {
        PutSessionRequest putSessionRequest = new PutSessionRequest();
        putSessionRequest
                .withBotName(botName)
                .withBotAlias(botAlias)
                .withUserId(userId)
                .withAccept("text/plain; charset=utf-8");
        PutSessionResult result = lexRuntimeClient.putSession(putSessionRequest);
        return StringUtils.isNullOrEmpty(result.getSessionId()) == false;
    }
}
