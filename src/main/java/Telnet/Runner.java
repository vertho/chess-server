package Telnet;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static Telnet.Commands.*;

public class Runner {
    public static final String FREECHESS_ORG = "freechess.org";
    public static final int PORT = 5000;
    private TelnetServer telnet;

    public Runner() {
        telnet = new TelnetServer(FREECHESS_ORG, PORT);
    }

    public void connect() {
        try {
            System.out.println("Got Connection...");
            telnet.sendCommand("set seek 0");
            telnet.sendCommand("set silence on");
            telnet.sendCommand("set shout off");
            telnet.sendCommand("set style 9");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        telnet.disconnect();
    }

    public void listGames() {
        telnet.sendCommand(RELAY_LIST_TOURNAMENTS);
        String tournamentList = telnet.readUntil(PROMPT + " ");
        persistTournamentList(tournamentList);
        System.out.println(tournamentList);
    }

    private void persistTournamentList(String tournamentList) {
        List<String> tournaments = Arrays.asList(StringUtils.split(tournamentList, ":"));
        System.out.println("LIST\n\n");
        for (String s : tournaments) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.connect();
        runner.listGames();
        runner.disconnect();
    }
}
