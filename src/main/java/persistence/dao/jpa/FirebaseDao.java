package persistence.dao.jpa;


import com.firebase.client.Firebase;

public class FirebaseDao {
    private static String firebaseURL = "https://chess-matches.firebaseio.com";
    private static String email = "thomasboverby@gmail.com";
    private static String password = "";
    public void foo() {
        Firebase firebase = new Firebase(firebaseURL);
        firebase.authWithPassword(email, password, null );

    }
}
