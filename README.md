# A simple sidebar plugin for Minecraft Spigot
To raise an issue, click the issues tab. To see the wiki, click the wiki tab.

## Latest release build &nbsp; &nbsp; &nbsp; ![GitHub release](https://img.shields.io/github/release/flintintoe/SimpleSidebar.svg?style=flat-square&label=Release) ![GitHub Release Date](https://img.shields.io/github/release-date/flintintoe/SimpleSidebar.svg?style=flat-square&label=Last%20release) ![license](https://img.shields.io/github/license/flintintoe/SimpleSidebar.svg?style=flat-square&label=License)

### Release notes:

#### Rewritten
- Rewritten Placeholder and Sidebar java files
- Rewritten code for grabbing data from sidebar.yml. Looks much different now

#### Added
- Added more events and Async update support using events
- Added more options in config.yml
- Added working support for statistics, regions, date and time (with timezone) placeholders (Did I mention location as well?)
- Added warning when a config file is outdated (No automatic updates yet -.-)

#### Fixed
- Fixed commands not working properly
- Fixed CustomSidebarUpdater not working as intended

#### Changed
- Changed tag structure (now uses . instead of \_) to work well with statistic values
- Changed messaging to better reflect on what information should be presented to the player/ console

### Todo:
- Test special cases of plugin settings (in config.yml)
- Allow use of names and aliases to set sidebar using commands
- Allow use of \ to cancel _ or %
- Allow dupicate entries in sidebar
- Possibly add placeholders.yml

## Latest commit build &nbsp; &nbsp; &nbsp; ![Github commit](https://img.shields.io/badge/Commit-v0.7.0__RC1-orange.svg?style=flat-square&label=Build) ![GitHub last commit (branch)](https://img.shields.io/github/last-commit/flintintoe/SimpleSidebar/master.svg?style=flat-square&label=Last%20commit) ![CircleCI branch](https://img.shields.io/circleci/project/github/flintintoe/SimpleSidebar/master.svg?style=flat-square&label=CircleCI) ![Codacy branch grade](https://img.shields.io/codacy/grade/ad2a5c3320dd43cbad38ba13a85f8a66/master.svg?style=flat-square&label=Codacy%20grade)

### Changes
- Fixed commands not working properly
- Changed tag structure (now uses . instead of \_) to work well with statistic values

### Todo
- Test special cases of plugin settings (in config.yml)
- ~~Allow use of names and aliases to set sidebar using commands.~~ 0.7.1
- ~~Allow use of \ to cancel . or %~~ 0.7.1
- ~~Allow duplicate entries in sidebar~~ 0.7.1
- Possibly add placeholders.yml and Placeholder API support
***
## News

### 2018/MAY/2
First release candidate of v0.7.0 released!
### 2018/APR/15
First release! v0.6.0_BETA
***
## Releases order
Commit → Pre-release → Release candidate → Official release

## Semantic versioning
vMAJOR.MINOR.PATCH + suffix
#### For releases
| Suffix        | Meaning           | Shown in releases |
|:--------------|:------------------|:------------------|
| \_pre(X)      | pre-release       | No                |
| \_RC(X)       | release candidate | Yes               |
|               | release           | Yes               |
#### For commits
| Suffix | Meaning        | Changes are final |
|:-------|:---------------|:------------------|
| x      | experimental   | No                |
| s      | remote save    | No                |
| b      | build success  | Yes/ No           |
| t      | feature tested | Yes               |

\**Changes final does not mean that the feature will stay final in future builds.*
***
## Planned for future releases
- Placeholder API support
- Static/ custom variables support
- messages.yml

## External links
Spigot resource page: https://www.spigotmc.org/resources/simplesidebar.56344/

Github.io page: https://flintintoe.github.io/SimpleSidebar/
