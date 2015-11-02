package Telnet;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Telnet.Commands.PROMPT;
import static Telnet.Commands.RELAY_LIST_GAMES_TOURN_NR;
import static Telnet.Commands.RELAY_LIST_TOURNAMENTS;

public class RelayCommunicator {
    private TelnetServer telnet;

    private String TournamentListRegex = "(\\d+)\\s+(.+)(Round.+)";

    public RelayCommunicator(TelnetServer telnet) {
        this.telnet = telnet;
    }

    public String[] listTournaments() {
        telnet.setSilentMode(true);
        telnet.sendCommand(RELAY_LIST_TOURNAMENTS);
        String tournamentList = getListOfTournamentsFromServer();
        List<Tournament> persistedTournamentList = persistTournamentList(tournamentList);
        return extractTournamentIds(persistedTournamentList);
    }

    private String[] extractTournamentIds(List<Tournament> persistedTournamentList) {
        String[] tournamentIds = new String[persistedTournamentList.size()];
        for(int i = 0; i < persistedTournamentList.size(); i++){
            tournamentIds[i] = persistedTournamentList.get(i).getId();
        }
        return tournamentIds;
    }

    public String getListOfTournamentsFromServer() {
        String result = telnet.readUntil(PROMPT + " ");
        result = StringUtils.remove(result, "\n\r");
        result = StringUtils.remove(result, "The following tournaments are currently in progress");
        result = StringUtils.remove(result, "fics" + PROMPT);
        return result;
    }

    public void listGames(String[] tournamentIds){
        telnet.setSilentMode(true);
        for(String tournamentId : tournamentIds){
            telnet.sendCommand(String.format(RELAY_LIST_GAMES_TOURN_NR, tournamentId));
            String gamesList = getListOfGamesForTournament();
            persistGamesList(gamesList);
        }
    }

    private List<Tournament> persistTournamentList(String input) {
        List<String> tournaments = formatServerOutput(input);
        List<Tournament> tournamentList = new ArrayList<Tournament>();
        for (String s : tournaments) {
            tournamentList.add(createTournamentFromInput(s));
        }
        //TODO: Persister til firebase
        return tournamentList;
    }

    private void persistGamesList(String input) {
        List<String> games = formatServerOutput(input);
        List<Game> gameList= new ArrayList();
        for (String s : games) {
            gameList.add(createGameFromInput(s));
        }
        //TODO: persister til firebase
    }

    private String getListOfGamesForTournament() {
        String result = telnet.readUntil(PROMPT + " ");
        result = StringUtils.remove(result, "\n\r");
        result = StringUtils.remove(result, "There are 12 games in the Deizisauer Autumn Open 2015 - Round 6");
        result = StringUtils.remove(result, "fics" + PROMPT);
        return result;
    }

    private String createGameListRegex() {
        StringBuilder sb = new StringBuilder();
        String space="\\s+";

        sb.append("(\\d+)"+space);
        sb.append("(\\w+)"+space);
        sb.append("(\\w+)"+space);
        sb.append("(\\d-\\d|\\d/.+|\\*)" + space);
        sb.append("(\\w\\d+)");
        return sb.toString();
    }

    private Tournament createTournamentFromInput(String input) {
        Pattern pattern = Pattern.compile(TournamentListRegex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String id = matcher.group(1).trim();
            String name = matcher.group(2).trim();
            String status = matcher.group(3).trim();
            return new Tournament(id, name, status);
        }
        return null;
    }

    private Game createGameFromInput(String input) {
        String gameListRegex = createGameListRegex();
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

    private List<String> formatServerOutput(String input) {
        List<String> resultList = new ArrayList<String>();
        for (String s : StringUtils.split(input, ":")) {
            if (!s.trim().isEmpty()) {
                resultList.add(s);
            }
        }
        return resultList;
    }


}
