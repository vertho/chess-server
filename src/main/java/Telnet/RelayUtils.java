package Telnet;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RelayUtils {
    private static final String tournamentListRegex = "(\\d+)\\s+(.+)(Round.+)";

    public static String getTournamentListRegex() {
        return tournamentListRegex;
    }

    public static String createGameListRegex() {
        StringBuilder sb = new StringBuilder();
        String space = "\\s+";

        sb.append("(\\d+)" + space);
        sb.append("(\\w+)" + space);
        sb.append("(\\w+)" + space);
        sb.append("(\\d-\\d|\\d/.+|\\*)" + space);
        sb.append("(\\w\\d+)");
        return sb.toString();
    }

    public static List<String> formatServerOutput(String input) {
        List<String> resultList = new ArrayList<String>();
        for (String s : StringUtils.split(input, ":")) {
            if (!s.trim().isEmpty()) {
                resultList.add(s);
            }
        }
        return resultList;
    }
}
