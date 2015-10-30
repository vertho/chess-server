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

    private String TournamentListregex = "(\\d+)\\s+(.+)(Round.+)";

    public RelayCommunicator(TelnetServer telnet) {
        this.telnet = telnet;
    }

    public void listTournaments() {
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
        //TODO: Thomas. Persister til firebase
    }

    private Tournament createTournamentFromInput(String input) {
        Pattern pattern = Pattern.compile(TournamentListregex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String id = matcher.group(1).trim();
            String name = matcher.group(2).trim();
            String status = matcher.group(3).trim();
            return new Tournament(id, name, status);
        }
        return null;
    }

    private List<String> formatTournamentList(String input) {
        List<String> resultList = new ArrayList<String>();
        for (String s : StringUtils.split(input, ":")) {
            if (!s.trim().isEmpty()) {
                resultList.add(s);
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
