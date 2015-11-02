import Telnet.Tournament;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;

/**
 * Created by Thomas on 23.10.2015.
 */
public class RegexTest {

    private String listGamesOutput =
            "There are 20 games in the Chinese Chess League Division A 2015 - Round 18\n"
                    + "\n"
                    + "208 GMZhao            GMYangyiYu        *       A04\n"
                    + "129 GMWen             GMHaoWang         0-1     A11\n"
                    + "4   GMLysyj           GMZhouWeiqi       1/2-1/2 A06\n"
                    + "42  GMGao             GMBu              1/2-1/2 B35\n"
                    + "59  GMLu              GMPengxiang       1/2-1/2 C84\n"
                    + "94  IMZhonghanMa      GMNi              1/2-1/2 D52\n"
                    + "166 GMQingnanLiu      GMXiu             1/2-1/2 C52\n"
                    + "91  GMBai             IMChenWang        1/2-1/2 E11\n"
                    + "38  KeMu              GMZhou            1/2-1/2 D43\n"
                    + "232 WGMTan            WGMGirya          1/2-1/2 D17\n"
                    + "178 YuxiangFang       IMLou             1/2-1/2 D78\n"
                    + "177 GMWenjunJu        WIMZhou           1/2-1/2 E04\n"
                    + "218 MinghuiXu         IMWan             1/2-1/2 D83\n"
                    + "128 WGMHuang          WIMGu             1/2-1/2 A10\n"
                    + "116 GMZhaoXue         WIMXu             1/2-1/2 D20\n"
                    + "148 WIMDoudouWang     WGMDing           1/2-1/2 D55\n"
                    + "ics%\n"
                    + "90  WGMWang           WFMYiyiXiao       *       C42\n"
                    + "81  ZilunLan          ZijiZhang         1/2-1/2 D94\n"
                    + "51  WGMXiaowenZhang   YeYuan            1-0     B43\n"
                    + "226 WIMZhai           JinerZhu          1/2-1/2 E73";

    private String listTournamentsOutput = "12  Thoresen Chess Engines Competition - Season 8 Stag ...  Round Started";

    public static final String TOURNAMENT_LIST_REGEX = "(\\d+)\\s+(.+)(Round.+)";

    @Test
    public void tournamentNameShouldBeSplitIntoThreeGroups() {

        Pattern pattern = Pattern.compile(TOURNAMENT_LIST_REGEX);
        Matcher matcher = pattern.matcher(listTournamentsOutput);
        matcher.find();
        assertEquals(matcher.group(1), "12");
        assertEquals(matcher.group(2).trim(), "Thoresen Chess Engines Competition - Season 8 Stag ...");
        assertEquals(matcher.group(3).trim(), "Round Started");
    }

    @Test
    public void gameNameShouldBeSplitIntoSixGroups() {
        String regex = createGameListRegex();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("129 GMWen             GMHaoWang         0-1     A11");
        matcher.find();
        assertEquals(matcher.group(1), "129");
        assertEquals(matcher.group(2), "GMWen");
        assertEquals(matcher.group(3), "GMHaoWang");
        assertEquals(matcher.group(4), "0-1");
        assertEquals(matcher.group(5), "A11");
    }

    @Test
    public void gameScoreIsInterpretedCorrectly() {
        String regex = "(\\d-\\d|\\d/.+|\\*)";
        String whiteWin = "1-0";
        String remis = "1/2-1/2";
        String blackWin = "0-1";
        String inProgress = "*";
        List<String> scores = new ArrayList<String>();
        scores.add(whiteWin);
        scores.add(remis);
        scores.add(blackWin);
        scores.add(inProgress);
        for (String s : scores) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(s);
            matcher.find();
            System.out.println(matcher.group(1));
        }
    }

    @Test
    public void convertToJSON() {
        Tournament tournament = createTournamentFromInput(listTournamentsOutput);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(tournament);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Pattern pattern = Pattern.compile(TOURNAMENT_LIST_REGEX);
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
