# League Buddy Application

### By: Noah Scheffer, Teake Jonkman, Bram Jansen and Sam Echten

The main functionality of the League Buddy app is to help League of Legends players when playing the game. The app helps players track there enemies spells and connects the player with there team using discord. 

In order to use all the features of the app you'll need a League of Legends and Discord account. These can be created on the following sites:

League of Legends: https://signup.leagueoflegends.com/en-gb/signup/index#/

Discord: https://discord.com/register

Once you have created a discord account you'll have to join the League Buddy discord server, this is needed so the bot can send you an invite link.

Discord server: https://discord.gg/CJzu22Ke

## Setting up the application

Before you can fully use the application you'll need to setup and run the API. In order to run the API you'll need to add the .env file to the directory provided in the assignment folder. Once the .env file is added you can run the following command:

```
npm i
```

 In order to run this command you'll need to install node.js. This can be done via the following link:

Node.js: https://nodejs.org/en/download/

To run the API run the following command:

```
node app.js
```

Next you'll have to make the API accessible to the application, this can be done using ngrok which can be downloaded from the following link:

ngrok: https://ngrok.com/

To use ngrok, open the ngrok application and type in the following command

```
ngrok http https://localhost:443
```

this will show you an ngrok url, copy this url but leave the 'https://' part out. Now in the DiscordApiHelper file in the application directory replace the BASE_URL variable with the ngrok url. The application can now communicate with the API.

Once the API is running you can run the application using Android studio. Here you can create an account, when creating your account you'll need to fill in your League of Legends name and discord id that you previously created. Once you have created an account you'll be send to the home screen. On the home screen you'll see some news related to gaming. If you go over to the middle tab on the bottom of the screen you'll see the match info page. Here you will see the players of the game you are currently in (if you are in a match), you'll also receive an invite link from our discord bot. Now you can talk to your teammates. Have fun!
