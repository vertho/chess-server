package Telnet;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Telnet.Commands.PROMPT;
import static Telnet.Commands.RELAY_LIST_GAMES_TOURN_NR;

public class RelayGames {
    private TelnetServer telnet;

    public RelayGames(TelnetServer telnet) {
        this.telnet = telnet;
    }

    public void listGames(String[] tournamentIds) {
        telnet.setSilentMode(true);
        for (String tournamentId : tournamentIds) {
            telnet.sendCommand(String.format(RELAY_LIST_GAMES_TOURN_NR, tournamentId));
            String gamesList = getListOfGamesForTournament();
            persistGamesList(gamesList);
        }
    }

    private void persistGamesList(String input) {
        List<String> games = RelayUtils.formatServerOutput(input);
        List<Game> gameList = new ArrayList();
        for (String s : games) {
            gameList.add(createGameFromInput(s));
            System.out.println(s);
        }
        //TODO: persister til firebase
    }

    private String getListOfGamesForTournament() {
        String result = telnet.readUntil(PROMPT + " ");
        result = StringUtils.remove(result, StringUtils.substringBetween(result,"There are","\n"));
        result = StringUtils.remove(result, "There are");
        result = StringUtils.remove(result, "\n\r");
        result = StringUtils.remove(result, "fics" + PROMPT);
        return result;
    }

    private Game createGameFromInput(String input) {
        String gameListRegex = RelayUtils.createGameListRegex();
        Pattern pattern = Pattern.compile(gameListRegex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1).trim());
            String playerOne = matcher.group(2).trim();
            String playerTwo = matcher.group(3).trim();
            String result = matcher.group(4).trim();
            String ecocode = matcher.group(5).trim();
            return new Game(id, playerOne, playerTwo, result, ecocode);
        }
        return null;
    }
}
