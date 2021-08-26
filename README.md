
# cakedj

[![Build](https://github.com/justapieop/cakedj/actions/workflows/gradle.yml/badge.svg)](https://github.com/justapieop/cakedj/actions/workflows/gradle.yml)

A Discord music bot. Crafted with JDA and LavaPlayer

## Building the bot

**Note:** 
- Java 11 or newer is required

- If you are using Heroku or similar services, please skip this process

- If you know how to eliminate errors, you are fine to skip this note, if you don't, please double check the `build` status under the project's name. As long as the status is `Passing`, you are fine to continue

The building process is simple. Clone the repository then type the command in the terminal that corresponds to your operating system

For Windows:

```bash
cd /path/to/the/repository
gradlew build
```

For Linux/MacOS:

```bash
cd /path/to/the/repository
chmod +x gradlew
./gradlew build
```

## Configuring the bot

Create a MongoDB database named `CakeDJ` and a collection named `config`

Then create **one and just one** document with the format below

```
_id: (no need to touch this because MongoDB will auto generate one)
token: (a string contains your bot's token)
ownerID: (a string contains your Discord ID)
dblToken: (a string contains your top.gg bot token) (OPTIONAL)
ipv6Block: (an array contains ipv6 blocks) (OPTIONAL)
```


## Deploying the bot

### For Heroku users

- Fork the repository

- Create an application

- Before deploying anything, set the `GRADLE_TASK` variable to `buildWithConfig` and `DATABASE` to a MongoDB Connection String

- Link the forked repository in the `Build` tab

- Turn on automatic deployment and click on `Deploy the project` button

- If the deployment fails, manually deploy again. If it continues to fail, contact me through my Discord: `JustAPie#9511`

- Go to `Resources` tab and turn on the `worker` process. Do not turn the `web` process on because Heroku will disable it after the bot runs for a while

**Note:**
- I do not recommend you use Heroku or any services like that because they do not support IP rotation to avoid being rate-limited by Youtube and other audio sources

- This applies to all app-deploying services which have a build system like Heroku

### For Linux/Windows/MacOS users

After building the bot, the bot jar file will be in `build/libs` with the name of `CakeDJ.jar`. You can move it anywhere you want. In the same directory where `CakeDJ.jar` file resides, make a file named `.env` with a content by the format below:

```
DATABASE=Your MongoDB Connection String
```

Your MongoDB Connection String must point to the database that has the bot's configuration

# Contributing

You are free to contribute to this repository, be sure to eliminate bugs and errors before making a pull request
