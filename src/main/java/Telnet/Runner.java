package Telnet;

public class Runner {
    public static final String FREECHESS_ORG = "freechess.org";

    public static final int PORT = 5000;

    private TelnetServer telnet;

    private RelayCommunicator relayCommunicator;

    public Runner() {
        telnet = new TelnetServer(FREECHESS_ORG, PORT);
        relayCommunicator = new RelayCommunicator(telnet);
    }

    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.connect();
        String[] tournamentIds = runner.listTournaments();
        runner.listGames(tournamentIds);
        runner.disconnect();
    }

    public void connect() {
        try {
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

    public String[] listTournaments() {
        return relayCommunicator.listTournaments();
    }

    public void listGames(String[] tournamentIds){
        relayCommunicator.listGames(tournamentIds);
    }
}
