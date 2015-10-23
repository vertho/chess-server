import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;

/**
 * Created by Thomas on 23.10.2015.
 */
public class RegexTest {

    @Test
    public void foo(){
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        String line = "12  Thoresen Chess Engines Competition - Season 8 Stag ...  Round Started";
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        assertEquals(matcher.group(0), "12");

    }
}
