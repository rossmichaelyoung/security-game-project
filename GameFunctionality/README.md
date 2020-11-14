# Cybersecurity Adventure Game  
  
### CyberAdventure.java  

 includes main and GUI declarations for panels, frames, etc.  
GameEffects.java  
  
 includes all GUI properties like panel sizing, button designs, fonts,   etc.  
  
### GameEffects.java  
  
 includes functions for the functionality of the game itself
 
### DictionaryAttackPasswordCracker.java  
  
 make sure to unzip the **dictionary.txt.zip** file before running this program and make sure to **not add dictionary.txt** to git 
 
### SQLInjection.java  

 you need to add the **postgresql-42.2.18.jar** file to the build path to run this program 
 
 Enter the following to perform an SQL injection for the usernames and passwords in our database 
 ``` 
 ' UNION SELECT username || ' ' || password FROM users-- 
 ```

### SecurityGame.java

To compile:
```
javac -cp ".:../Resources/postgresql-42.2.18.jar" SecurityGame.java DictionaryAttackPasswordCracker.java SQLInjection.java GUIproperties.java
```
To run:
```
java -cp ".:../Resources/postgresql-42.2.18.jar" SecurityGame
```