# cakedj

[![Build](https://github.com/justapieop/cakedj/actions/workflows/gradle.yml/badge.svg)](https://github.com/justapieop/cakedj/actions/workflows/gradle.yml)

A Discord music bot. Crafted with JDA and LavaPlayer

## Building the bot

**Note:** 
- Java 11 or newer is required

- If you know how to eliminate errors, you are fine to skip this note, if you don't, please double check the `build` status under the project's name. As long as the status is `Passing`, you are fine to continue

The building process is simple. Clone the repository and type the command below:

For Windows:

```bash
cd /path/to/the/repository
gradlew build
```

For Linux/Mac OS:

```bash
cd /path/to/the/repository
chmod +x gradlew
./gradlew build
```

## Deploying the bot

### For Heroku users

- Fork the repository

- Create an application

- Before deploying anything, set the `GRADLE_TASK` variable to `buildWithConfig` and `DATABASE` to a MongoDB Connection String

- Link the forked repository in the `Build` tab

- Turn on automatic deployment and click on `Deploy the project` button

- If the deployment fails, manually deploy again. If it continues to fail, contact me through my Discord: `JustAPie#9511`

**Note:**
- I do not recommend you use Heroku or any services like that because they do not support IP rotation to avoid being rate-limited by Youtube and other audio sources

- This applies to all app-deploying services which have a build system like Heroku

### For Linux/Windows/Mac OS users

After building the bot, run the jar in `build/libs` with the name of `CakeDJ.jar`. Do not run the `cakedj-1.0.0.jar` because that's a raw jar that doesn't contain any dependencies

# Contributing

You are free to contribute to this repository, be sure to eliminate bugs and errors before making a PR
