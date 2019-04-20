README for battleship-3k  
  
This is a project for TDAT1006 System Development with Database Project  
  
/\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\\  
  
        MAKE SURE YOUR JAVA VERSION IS 1.8  
          
\\\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*/  
  
Installation guide:  
-Download "DownloadThis.zip" from the top directory  
-Extract the "run" directory to somewhere on your computer  
-Open the "run" directory  
-Double click the "Battleship 3k.bat" file. The program now generates a file called dbConfig.properties  
-Close the program  
-Go to run/src/main/java  
-Open dbConfig.properties  
-Add configurations for your database. For using our database send a message to magbre@stud.ntnu.no  
--If you want to use your own database please follow the SQL guide below  
-Go back to the "run" directory  
-Double click the "Battleship 3k.bat" file again.   
-Enjoy your game :)  

SQL guide (do this only once):  
-Open your preferred way to handle database interactions  
-Make sure you are using a MySQL server with MySQL 8 or newer  
-Make sure you have no tables in your database called "BattleshipUser", "battleship_game", "battleship_board", "battleship_action", or "battleship_feedback" in your database  
-Copy the entire contents of "full_sql_wo_foreign_keys.sql" and run it against your database  
-You should now be good to go  
