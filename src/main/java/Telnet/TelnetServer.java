package Telnet;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.InputStream;
import java.io.PrintStream;

public class TelnetServer {
    public static final String USERNAME = "";

    public static final String PASSWORD = "";

    private TelnetClient telnet = new TelnetClient();

    private InputStream in;

    private PrintStream out;

    //skal fjernes når vi ikke trenger output til konsoll lengre. Er kun med under utvikling
    private boolean silentMode = false;

    public TelnetServer(String server, int port) {
        connect(server, port);
        login();
    }

    private void login() {
        try {
            readUntil("login: ");
            write(USERNAME);
            readUntil("password: ");
            write(PASSWORD);
            readUntil(Commands.PROMPT + " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(String server, int port) {
        try {
            telnet.connect(server, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                if (!silentMode) {
                    System.out.print(ch);
                }
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil(Commands.PROMPT + " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSilentMode(boolean mode) {
        this.silentMode = mode;
    }
}
