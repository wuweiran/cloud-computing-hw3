package edu.nyu.cs9223.hw3;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.util.CollectionUtils;
import edu.nyu.cs9223.hw3.chatbot.ChatBot;
import edu.nyu.cs9223.hw3.chatbot.ChatBotFactory;
import edu.nyu.cs9223.hw3.model.SearchRequest;
import edu.nyu.cs9223.hw3.model.SearchResponse;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.nyu.cs9223.hw3.search.ElasticSearchService;

import java.util.List;

/**
 * @author wuweiran
 */
public class SearchRequestHandler implements RequestHandler<SearchRequest, SearchResponse> {
    @Override
    public SearchResponse handleRequest(SearchRequest searchRequest, Context context) {
        final LambdaLogger logger = context.getLogger();
        ChatBot chatBot = ChatBotFactory.getInstance();
        logger.log("Request received. Sentence: " + searchRequest.getQ());
        List<String> replies = chatBot.postText("test1", searchRequest.getQ());
        SearchResponse result = new SearchResponse();
        if (CollectionUtils.isNullOrEmpty(replies)) {
            result.setSuccess(false);
            result.setErrorMessage("Bot error?");
        } else {
            String reply = replies.get(0);
            if (reply.toLowerCase().contains("sorry") || reply.toLowerCase().contains("repeat")) {
                result.setSuccess(false);
                result.setErrorMessage("No keywords");
            } else {
                logger.log("Tags to search: " + reply);
                String[] tags = reply.split(",");
                String searchString = reply.replaceAll(",", " ");
                List<String> searchResult = ElasticSearchService.search(searchString);
                result.setSuccess(true);
                result.setMatched(searchResult);
            }
        }
        return result;
    }
}
