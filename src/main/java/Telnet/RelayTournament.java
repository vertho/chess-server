package Telnet;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Telnet.Commands.PROMPT;
import static Telnet.Commands.RELAY_LIST_TOURNAMENTS;

public class RelayTournament {
    private TelnetServer telnet;


    public RelayTournament(TelnetServer telnet) {
        this.telnet = telnet;
    }

    public String[] listTournaments() {
        telnet.setSilentMode(true);
        telnet.sendCommand(RELAY_LIST_TOURNAMENTS);
        String tournamentList = getListOfTournamentsFromServer();
        if(StringUtils.contains(tournamentList, "There are no tournaments in progress")){
            System.out.println("There are no tournaments in progress");
            return null;
        }
        else {
            List<Tournament> persistedTournamentList = persistTournamentList(tournamentList);
            return extractTournamentIds(persistedTournamentList);
        }
    }

    private String[] extractTournamentIds(List<Tournament> persistedTournamentList) {
        String[] tournamentIds = new String[persistedTournamentList.size()];
        for (int i = 0; i < persistedTournamentList.size(); i++) {
            Tournament tournament = persistedTournamentList.get(i);
            if(tournament != null) {
                tournamentIds[i] =tournament.getId();
            }
        }
        return tournamentIds;
    }

    private String getListOfTournamentsFromServer() {
        String result = telnet.readUntil(PROMPT + " ");
        result = StringUtils.remove(result, "\n\r");
        result = StringUtils.remove(result, "The following tournaments are currently in progress");
        result = StringUtils.remove(result, "fics" + PROMPT);
        return result;
    }

    private List<Tournament> persistTournamentList(String input) {
        List<String> tournaments = RelayUtils.formatServerOutput(input);
        List<Tournament> tournamentList = new ArrayList<Tournament>();
        for (String s : tournaments) {
            tournamentList.add(createTournamentFromInput(s));
        }
        //TODO: Persister til firebase
        return tournamentList;
    }

    private Tournament createTournamentFromInput(String input) {
        Pattern pattern = Pattern.compile(RelayUtils.getTournamentListRegex());
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String id = matcher.group(1).trim();
            String name = matcher.group(2).trim();
            String status = matcher.group(3).trim();
            return new Tournament(id, name, status);
        }
        return null;
    }
}
