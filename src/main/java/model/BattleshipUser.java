/**
 * Models a the users we store information about in the database
 *
 * @Author Thorkildsen Torje
 */
package model;

public class BattleshipUser {
    private String username;
    private byte[] password;
    private String email;
    private int wonGames;
    private int lostGames;
    private double ratio;

    public BattleshipUser(String username, byte[] password, String email, int wonGames, int lostGames) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.wonGames = wonGames;
        this.lostGames = lostGames;
        this.ratio = calculateRatio();
    }

    public BattleshipUser(String username, byte[] password, String email) {
        this(username, password, email, 0, 0);
    }

    public String getUsername() {
        return username;
    }
    
    public byte[] getPassword() { return password; }
    
    public String getEmail()
    {
        return email;
    }

    public int getWonGames() {
        return wonGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public double getRatio() {
        return ratio;
    }

    private double calculateRatio() {
        if (lostGames + wonGames == 0) {
            return 0;
        }

        return (double) wonGames / (double) (wonGames + lostGames);
    }
}
