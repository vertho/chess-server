package Telnet;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static Telnet.Commands.PROMPT;
import static Telnet.Commands.RELAY_LIST_TOURNAMENTS;

public class RelayCommunicator {
    private TelnetServer telnet;

    private String listOfTournamentsFromServer;

    public RelayCommunicator(TelnetServer telnet) {
        this.telnet = telnet;
    }

    public void listGames() {
        telnet.setSilentMode(true);
        telnet.sendCommand(RELAY_LIST_TOURNAMENTS);
        System.out.println("\n List from response");
        String tournamentList = getListOfTournamentsFromServer();
        persistTournamentList(tournamentList);
    }

    private void persistTournamentList(String input) {
        List<String> tournaments = createTournamentList(input);
    }

    private List<String> createTournamentList(String input) {
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
        result = StringUtils.remove(result, "fics"+PROMPT);
        return result;
    }
}
