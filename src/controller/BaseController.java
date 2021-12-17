package controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseController {
    public HashMap<String, Matcher> checkCommand(HashMap<String, String> patterns, String command) {
        for (String key: patterns.keySet()) {
            String regex = patterns.get(key);
            if (Pattern.matches(regex, command)) {
                HashMap<String, Matcher> result = new HashMap<>();
                result.put(key, getMatcher(regex, command));
                return result;

            }
        }
        return null;
    }

    private Matcher getMatcher(String regex, String command) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);

    }

}
