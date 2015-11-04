package persistence.dao.jpa;


import Telnet.Tournament;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class FirebaseDao {
    private static String firebaseURL = "https://luminous-torch-447.firebaseio.com";
    //private static String firebaseURL = "https://luminous-torch-447.firebaseio.com/android/saving-data/fireblog";
    private static String email = "thomasboverby@gmail.com";
    private static String password = "rfrDJW44GnM9w2m";

    public void foo() throws InterruptedException {
        Firebase firebase = new Firebase(firebaseURL);
        firebase.authAnonymously(getHandler(firebase));
        Thread.sleep(2000);

        //firebase.unauth();


    }

    public static void main(String[]args) throws InterruptedException {
        FirebaseDao dao = new FirebaseDao();
        dao.foo();
    }

    public Firebase.AuthResultHandler getHandler(final Firebase firebase) {

        return new Firebase.AuthResultHandler() {
            public void onAuthenticated(AuthData authData) {
                System.out.println("Authenticated");
                Firebase tournamentRef = firebase.child("Tournaments").child("testTournament");
                Tournament tournament = new Tournament("1", "TestTournament", "Started");
                tournamentRef.setValue(tournament);
            }

            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.println("Not Authenticated");
            }
        };
    }

    /**
     *
     // Authenticate users with a custom Firebase token
     ref.authWithCustomToken("<token>", authResultHandler);

     // Alternatively, authenticate users anonymously
     ref.authAnonymously(authResultHandler);

     // Or with an email/password combination
     ref.authWithPassword("jenny@example.com", "correcthorsebatterystaple", authResultHandler);

     // Or via popular OAuth providers ("facebook", "github", "google", or "twitter")
     ref.authWithOAuthToken("<provider>", "<oauth-token>", authResultHandler);
     */
}
