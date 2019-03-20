/**
 * Models a the users we store information about in the database
 * #TODO remove password
 *
 * @Author Thorkildsen Torje
 * @Author Brevik Magnus
 */
package model;

public class BattleshipUser {
    private int userId;
    private String username;
    private String password;
    private String email;
    private int wonGames;
    private int lostGames;
    private double ratio;

    
    /**
     * Constructor for the BattleshipUser class
     *
     * @param username username
     * @param password password
     * @param email email
     * @param wonGames won games
     * @param lostGames lost games
     */
    public BattleshipUser(int userId, String username, String password, String email, int wonGames, int lostGames) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.wonGames = wonGames;
        this.lostGames = lostGames;
        this.ratio = calculateRatio();
    }
    /**
     * Constructor without won or lost games
     *
     * @param username username
     * @param password password
     * @param email email
     */
    public BattleshipUser(int userId,String username, String password, String email) {
        this(userId, username, password, email, 0, 0);
    }

    public int getUserId() {
        return userId;
    }
    
    /**
     * Returns username
     *
     * @return String username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Returns password
     *
     * @return String password
     */
    public String getPassword() { return password; }
    
    /**
     * Returns email address
     *
     * @return String email
     */
    public String getEmail()
    {
        return email;
    }
    
    /**
     * Returns the number of games the player has won
     *
     * @return int wonGames
     */
    public int getWonGames() {
        return wonGames;
    }
    
    /**
     * Returns the number of games the player has lost
     *
     * @return int lostGames
     */
    public int getLostGames() {
        return lostGames;
    }
    
    /**
     * Returns the ratio of games won over total games
     *
     * @return double ratio
     */
    public double getRatio() {
        return ratio;
    }
    
    /**
     * Calculates the ratio of games won and total games
     *
     * @return double ratio
     */
    private double calculateRatio() {
        if (lostGames + wonGames == 0) {
            return 0;
        }

        return (double) wonGames / (double) (wonGames + lostGames);
    }
}
