# JHandball-Table

## What is it?

A score table for the european game handball

## Handball basic rules of a match (relevant to us)

- 2 teams, each with 7 players
- the team with most goals wins the match
- match = two periods of 30 minutes
- 10 minutes intermission between periods
- if draw, there can be a maximum of two overtimes 5 minutes each
- if still draw, penalty shootout occurs
- referees can call timeouts of any length, at any time, for any reason
- teams can call up to 3 team timeouts of 1 minute each
- players can be given 1 yellow card (maximum 3 per team) (the second time they are suspended)
- players can be suspended at any time for 2 minutes (the third time they are given a red card)
- players can be given 1 red card, which means disqualification

## The program idea

### Windows

- A main window for managing the match
- A secondary (public) window for displaying the information
- An initialization window
- An about window

### Functions

- Initialize game
- Begin match
- Referee timeout (just pause timer)
- Team timeout (for each team)
- Select player
  - Score
  - Give yellow card
  - Suspend
  - Give red card
- End match
- Reset match

### Data

- Match
  - Timer {object}
  - Date {string}
  - Has ended {bool}
  - List of suspended players {array}
- Team
  - Name {string}
  - Total score {int}
  - Number of timeout calls {int}
  - Number of yellow cards {int}
  - List of players {array}
- Player
  - Name {string}
  - Number {int}
  - Score {int}
  - Has yellow card {bool}
  - Is suspended {bool}
  - Number of suspensions {int}
  - Has red card - is disqualified {bool}
- Timer
  - Current time {int}
  - Is running {bool}
- Suspended player
  - Player {object}
  - Timer {object}

### Public window shows

- Current time
- 2x (team name, score and list of players)
- List of suspended players
- (A player is: name and number)
