# A simple sidebar plugin for Minecraft Spigot
To raise an issue, click the issues tab. To see the wiki, click the wiki tab.

## Latest release build &nbsp; &nbsp; &nbsp; ![1](https://img.shields.io/github/release/flintintoe/SimpleSidebar.svg?style=flat-square&label=Release) ![2](https://img.shields.io/github/release-date/flintintoe/SimpleSidebar.svg?style=flat-square&label=Last%20release) ![3](https://img.shields.io/github/license/flintintoe/SimpleSidebar.svg?style=flat-square&label=License)

Check releases for release notes

### Todo:
- Test special cases of plugin settings (in config.yml)
- Allow use of names and aliases to set sidebar using commands
- Allow use of \ to cancel _ or %
- Allow dupicate entries in sidebar
- Possibly add placeholders.yml

## Latest commit build &nbsp; &nbsp; &nbsp; ![1](https://img.shields.io/badge/Commit-v0.7.1s-orange.svg?style=flat-square&label=Build) ![2](https://img.shields.io/github/last-commit/flintintoe/SimpleSidebar/master.svg?style=flat-square&label=Last%20commit) ![3](https://img.shields.io/circleci/project/github/flintintoe/SimpleSidebar/master.svg?style=flat-square&label=CircleCI) ![4](https://img.shields.io/codacy/grade/ad2a5c3320dd43cbad38ba13a85f8a66/master.svg?style=flat-square&label=Codacy%20grade)

### Changes
- Added feature to allow use of \ to cancel %
- Added feature to allow duplicate entries in a sidebar
- Added feature to set sidebar using name or alias through commands
- Untested fixes to commands
- Untested fixes to startup (loading sidebars)
- Adjustments to Placeholder class

### Todo
- Test special cases of plugin settings (in config.yml)
- ~~Allow use of names and aliases to set sidebar using commands.~~ 0.7.1s
- ~~Allow use of \ to cancel . or %~~ 0.7.1s
- ~~Allow duplicate entries in sidebar~~ 0.7.1s
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
