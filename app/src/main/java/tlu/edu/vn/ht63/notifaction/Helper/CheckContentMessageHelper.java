package tlu.edu.vn.ht63.notifaction.Helper;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckContentMessageHelper {
    public static String checkMessageBanking(String title,String content) {
        String titleRegex = ".*Biến động số dư.*";
        String contentRegex = "([+-]?\\d{1,3}(?:,\\d{3})*(?:\\.\\d+)?\\s?VND)";

        Pattern titlePattern = Pattern.compile(titleRegex);
        Matcher titleMatcher = titlePattern.matcher(title);
        Pattern contentPattern = Pattern.compile(contentRegex);
        Matcher contentMatcher = contentPattern.matcher(content);

        String transaction = null;
        if(titleMatcher.matches()){
            if (contentMatcher.find()) {
                transaction = contentMatcher.group(1);
            }
            return transaction;
        }
        return transaction;
    }
}
