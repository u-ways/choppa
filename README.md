# Choppa

Choppa is an Agile team rotation tool based on [The Spotify Model][1].  
It provides an easy-to-use web-based interface to rotate and track your team members.

---

## What is The Spotify Model?

The Spotify model is a people-driven, autonomous approach for scaling agile that emphasises the importance of culture and network. It has helped Spotify, and other organisations increase innovation and productivity by focusing on autonomy, communication, accountability, and quality.

The Spotify model was first introduced to the world in 2012, which introduced the radically simple way Spotify approached agility. Since then, the Spotify model generated a lot of buzz and became popular in the agile transformation space. Part of its appeal is that it focuses on organising around work rather than following a specific set of practices. In traditional scaling frameworks, specific practices (e.g. daily standups) are how the framework is executed, whereas the Spotify model focuses on how businesses can structure an organisation to enable agility.

If you're interested to learn more, please read [Scaling Agile @ Spotify whitepaper][2].

## Why should you do team rotations?

It's not uncommon, for example, for a successful software project to last many years. After all, successful software is never complete. There's always a new feature to add or an existing component to freshen up. 

But should the original team last just as long?  
Team rotation opportunities can be found in successful, happy, and innovative teams.

### Benefits of Team Rotations

There are lots of reasons to change up your team:

- **New ideas**: New team members bring a beginner's mindset to the problem space and commonly inject positive new ideas and observations.
- **Knowledge sharing**: Assuming the person rotating off the project isn't leaving your organisation, rotating team members can help you extend the knowledge base of people who can effectively work on this successful product.
- **Individual energy and team morale**: Variety is the spice of life. The existing feature challenges of the product are exciting and fresh for the new developer. This energy is contagious, and it can provide a boost to the entire team.

Many existing tech-based companies recommend team rotation:

- [Thoughtbot - "Our Experience With Team Rotations"][3]
- [Atomic Object - "Team Rotations – Yes, You Should"][4]

## Why should you use Choppa for team member rotation?

We mixed the structural concept of The Spotify Model and the iterative nature of Agile to introduce a chapter-based member rotation to build on the network-driven philosophy of The Spotify Model. This opens the possibility of maximising knowledge sharing and creating a more robust workplace culture within an organisation.

Instead of using the traditional Excel spreadsheet, pen and paper, or just relaying on memory to rotate your team members, you can use Choppa to do the hard crunching for you. Choppa provides you with team formation tracking and different rotation strategies that can utilise previous member formations to place members where they could have the most significant impact on the team.

We do this by considering the time spent in each squad, the different chapters a member got exposed to, and the various tribe members you have interacted with based on the formation history collected. This eliminates bias when rotating team members based on single user preferences, and saves you time from input and tracking chores automated in this application.

---

## Complete Self-hosting Instructions:

### Prerequisites:
- Ubuntu 20.04.2 LTS.
- Basic knowledge of using the terminal and bash.
- Superuser (sudo) privileges

### Instructions

#### 1. Install required packages, so you can setup the application:

```sh
sudo apt install git curl default-jre postgresql
```

#### 2. In a directory of your choice,  clone the choppa project.

```sh
git clone https://github.com/U-ways/choppa.git
```

#### 3. Go to project directory and run the gradle wrapper to setup the project environment:

```sh
cd choppa/
./gradlew
```

#### 4. Run the project acceptance tests to make sure the services work as expected.

```sh
./gradlew acceptance
```

#### 5. Create PostgreSQL choppa user and a database:

```sh
sudo -u postgres createuser -P -s choppa # => Enter password to the newly created user.
sudo -u postgres createdb choppa
```

#### 6. Setup the environment variables needed:

Since our application relies on OAuth 2.0, you will need to register your hosted version with the providers we support. For each provider, there is a link with the official documentation to generate your own OAuth 2.0 client id and secret. Below, is the command needed to create a file called .choppa.env in your home directory with all the environment variables needed. We have filled the non-sensitive values with the default values you might use, the sensitive values you have to change yourself are clearly UPPERCASED the value CHANGE_ME. When the CHANGE_ME values are replaced, just copy and paste this single command to the terminal.

```sh
echo """# Choppa Environment Variables:
# Profile
export PROFILE=prod

# Server Port
export PORT=8080

# Database
export DB_PORT=5432
export DB_HOST=localhost
export DB_NAME=choppa
export DB_USERNAME=choppa
export DB_PASSWORD=CHANGE_ME

# Database Migration Target
export FLY_TARGET=8.0.0

# Github OAuth 2.0
# See: https://docs.github.com/en/developers/apps/authorizing-oauth-apps
export GITHUB_CLIENT_ID=CHANGE_ME
export GITHUB_CLIENT_SECRET=CHANGE_ME

# Facebook OAuth 2.0
# See: https://developers.facebook.com/docs/facebook-login/
export FACEBOOK_CLIENT_ID=CHANGE_ME
export FACEBOOK_CLIENT_SECRET=CHANGE_ME

# Google OAuth 2.0
# See: https://developers.google.com/identity/protocols/oauth2
export GOOGLE_CLIENT_ID=CHANGE_ME
export GOOGLE_CLIENT_SECRET=CHANGE_ME

# Okta OAuth 2.0
# See: https://developer.okta.com/docs/guides/implement-oauth-for-okta
export OKTA_ISSUER=CHANGE_ME
export OKTA_CLIENT_ID=CHANGE_ME
export OKTA_CLIENT_SECRET=CHANGE_ME
""" > ~/.choppa.env
```

#### 7. Source the environment variables to the current active terminal session
```sh
source ./.choppa.env
```

#### 8. Install Node.Js v12 if it is not installed.
```sh
curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -
sudo apt-get install -y nodejs
```

#### 9. Build the fontend files.
```sh
npm ci --prefix ./src/frontend
npm run build --prefix ./src/frontend
```

#### 10. Create an executable jar for the project.
```sh
./gradlew bootJar
```

#### 11. Run the project.
```sh
java -jar build/libs/choppa-0.0.1-SNAPSHOT.jar
```

That’s it! Your application should be hosted at http://localhost:8080/ (if you did not change the default values). If one of these steps is not working as expected, please get in touch with one of the maintainers and we’re more than happy to help.

___

[1]: https://www.atlassian.com/agile/agile-at-scale/spotify
[2]: https://blog.crisp.se/wp-content/uploads/2012/11/SpotifyScaling.pdf
[3]: https://thoughtbot.com/blog/team-rotations
[4]: https://spin.atomicobject.com/2017/08/08/team-rotation-best-practices/
