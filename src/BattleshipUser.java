public class BattleshipUser {
    private String username;
    private String password;
    private String email;
    private int wonGames;
    private int lostGames;
    private double ratio;
    
    public BattleshipUser(String username, String password, String email, int wonGames, int lostGames){
        this.username = username;
        this.password = password;
        this.email = email;
        this.wonGames = wonGames;
        this.lostGames = lostGames;
        this.ratio = wonGames/lostGames;
    }
    
}
