package Telnet;

public class RelayCommunicator {
    private TelnetServer telnet;

    private RelayTournament relayTournament;

    private RelayGames relayGames;

    public RelayCommunicator(TelnetServer telnet) {
        this.telnet = telnet;
        relayTournament = new RelayTournament(telnet);
        relayGames = new RelayGames(telnet);
    }

    public String[] listTournaments() {
        return relayTournament.listTournaments();
    }

    public void listGames(String[] tournamentIds) {
        relayGames.listGames(tournamentIds);
    }
}
