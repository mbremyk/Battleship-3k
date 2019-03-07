package model;

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
        this.ratio = calculateRatio();
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public int getWonGames()
    {
        return wonGames;
    }
    
    public int getLostGames()
    {
        return lostGames;
    }
    
    public double getRatio()
    {
        return ratio;
    }
    
    private double calculateRatio()
    {
    	if(lostGames + wonGames == 0)
	    {
	    	return 0;
	    }
	    
	    return (double)wonGames / (double)(wonGames + lostGames);
    }
}
