# MigraineBot

The source code for the MigraineBot Discord Bot.

This bot was written in Java using the [JDA](https://github.com/discord-jda/JDA) library.

## Functionality
- Toggles an automod rule to disable pinging specific people at specific times.
- Allows commands to be created that give users information.
- Allows submission events to be easily created.

## Commands
| Command                    | Description                                                                   |
|----------------------------|-------------------------------------------------------------------------------|
| `/settings`                | Configure the bot.                                                            |
| `/addmessagecommand`       | Add a command, which sends the user the given message when used.              |
| `/removemessagecommand`    | Removes a message command.                                                    |
| `/addsubmissioncommand`    | Add a command, which accepts submissions and sends them to the given channel. |
| `/removesubmissioncommand` | Removes a submission command.                                                 |