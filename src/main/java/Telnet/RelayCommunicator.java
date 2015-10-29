package Telnet;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Telnet.Commands.PROMPT;
import static Telnet.Commands.RELAY_LIST_TOURNAMENTS;

public class RelayCommunicator {
    private TelnetServer telnet;

    private String listOfTournamentsFromServer;

    private String TournamentListregex = "(\\d+)\\s+(.+)(Round.+)";

    public RelayCommunicator(TelnetServer telnet) {
        this.telnet = telnet;
    }

    public void listGames() {
        telnet.setSilentMode(true);
        telnet.sendCommand(RELAY_LIST_TOURNAMENTS);
        String tournamentList = getListOfTournamentsFromServer();
        persistTournamentList(tournamentList);
    }

    private void persistTournamentList(String input) {
        List<String> tournaments = formatTournamentList(input);
        List<Tournament> tournamentList = new ArrayList<Tournament>();
        for (String s : tournaments) {
            tournamentList.add(createTournamentFromInput(s));
        }
        int i =1;
    }

    private Tournament createTournamentFromInput(String input) {
        Pattern pattern = Pattern.compile(TournamentListregex);
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            String id = matcher.group(1);
            String name = matcher.group(2);
            String status = matcher.group(3);
            return new Tournament(id, name, status);
        }
        return null;
    }

    private List<String> formatTournamentList(String input) {
        List<String> resultList = new ArrayList<String>();
        for (String s : StringUtils.split(input, ":")) {
            if (!s.trim().isEmpty()) {
                resultList.add(s);
                System.out.println(s);
            }
        }
        return resultList;
    }

    public String getListOfTournamentsFromServer() {
        String result = telnet.readUntil(PROMPT + " ");
        result = StringUtils.remove(result, "\n\r");
        result = StringUtils.remove(result, "The following tournaments are currently in progress");
        result = StringUtils.remove(result, "fics" + PROMPT);
        return result;
    }
}
