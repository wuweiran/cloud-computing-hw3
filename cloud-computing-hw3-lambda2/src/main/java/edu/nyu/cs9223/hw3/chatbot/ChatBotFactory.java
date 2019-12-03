package edu.nyu.cs9223.hw3.chatbot;

import com.amazonaws.services.lexruntime.AmazonLexRuntimeClient;
import com.amazonaws.services.lexruntime.AmazonLexRuntimeClientBuilder;

/**
 * @author wuweiran
 */
public class ChatBotFactory {
    private volatile static ChatBot chatBot;

    private ChatBotFactory() {
    }

    public static ChatBot getInstance() {
        if (chatBot == null) {
            synchronized (ChatBot.class) {
                if (chatBot == null) {
                    chatBot = buildChatBot();
                }
            }
        }
        return chatBot;
    }

    private static ChatBot buildChatBot() {

        final AmazonLexRuntimeClient lexRuntimeClient = (AmazonLexRuntimeClient) AmazonLexRuntimeClientBuilder
                .defaultClient();
        return new ChatBot(lexRuntimeClient, "PhotoAlbum", "Pre");
    }
}
