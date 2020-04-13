# Qev

**THE DEFAULT BRANCH IS CURRENTLY THE DEV BRANCH (FRAMEWORKV2 BRANCH). THIS IS BECAUSE EVERYTHING IS CURRENTLY BEING REWRITTEN.**

## Info

Qev is a bot I made to replace the 5+ bots that most servers seem to have on them. It is meant to be the only bot that you need for your server. I am still adding commands to the bot, and if you have any ideas, (no matter how niche) feel free to suggest them! Please see [my contact info](#contact)
## Commands

If something is in angled brackets (<>), then it is optional. If it is in curly brackets ({}), then it is required.

* Moderation
  * kick {user}
    - Admin only
    - Kicks a user. You can supply the user in the command. If you don't, then it will prompt you for the user. (Does not require @mention. It will search for a user if you only provided a word.)
  * ban {user}
    - Admin only
    - Bans a user.
    - You can supply the user in the command. If you don't, then it will prompt you for the user. (Does not require @mention. It will search for a user if you only provided a word.)
  * unban {user}
    - Admin only
    - Unbans a user.
    - You can supply the user in the command. If you don't, then it will prompt you for the user. (Does not require @mention. It will search for a user if you only provided a word.)
* Search
  * youtube {query}
    - Searche [youtube](https://youtube.com) for a specific string.
  * imgur {query}
    - Searches [imgur](https://imgur.com) for a string.
  * anime {query}
    - Searches [MyAnimeList](https://myanimelist.net) for a string.
  * hentai
    - Retrieves hentai image from danbooru.
    - NSFW channels only.
* Statistics
  * leaderboard
    - Gets the top 10 people with the highest level on the user.
    - Will be reskinned soon.
  * rank <user>
    - Gets the rank of the given user.
    - If no user is specified, it defaults to the user who used it.
* Utility
  * giverole {user} {role}
    - Admin only
    - Gives a role to a user.
    - Does not require you to mention role or user.
    - If no role & user is provided in the command, it will prompt you for a user & role.
  * ping
    - Pings the bot & checks responsivity to discord.
  * prefix
    - Admin only
    - Changes the prefix of the bot for the server on which it's used.
  * clear {number}
    - Admin only
    - Clears the specified amount of messages from the chat.
    - It will take a while to clear the chat for really large numbers.
  * poll "{question}" "{answer1}" "{answer2}" . . . "{answer10}"
    - Generates a poll.
    - The first item is the question.
    - The next items are the answers. Up to 10.
    - All of the questions and answers must be seperated by quotes. ("")
    - eg. !poll "This is the question" "answer 1" "answer 2" "answer 3"
  * avatar <user>
    - Gets the avatar of the specified user.
    - Defaults to the user who used the command.
  * userinfo <user>
    - Gets the user info of the specified user.
    - Defaults to the user who used the command.
  * info
    - Gets bot info.
    - Contains the invite link for the bot.
  * help <command>
    - If used without a name of a command, it will bring you to this page.
    - If used with a command name, it will give info for that command.
* Fun
  * 8ball {question}
    - Ask the magic 8 ball a question! It will give you an answer.

## Contact
Contact @solonovamax#6983 on discord or [@solonovamax](https://twitter.com/solonovamax) on twitter. Or you can join the [support discord server](https://discord.gg/YFSQ4cF).

## Credit
Credit to pikisuperstar for the background used for the !level command.

Credit to AvaIre for making a good, open source, bot so that I can ~~steal~~ take inspiration from their code. I'll leave their licence at the top of any file I use from their bot. 

Credit to Minecraft Forge and JDA for being a good inspiration for how to write good code.


## Self Hosting
1. Clone the github. This can be done by running `git clone https://github.com/solonovamax/Qev.git`
2. Pull the dependencies and build the project. This can be done by running `gradlew dependencies` and then `gradlew shadowJar`.
3. Next, you need to take the jar (generated in `./libs/`)
7. After adding all the required things, run the bot again and it will start. You can then invite it like any normal bot.
