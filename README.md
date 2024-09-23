An implementation of the board game Pente as an Android app with a fully working computer player that plays optimally. The rules of the game are as follows:

<body>
<h1>Pente</h1>

Pente is a two-player board game, designed by Gary Gabriel. 


<h2>The Objective</h2>

The objective of this game is to win by either:
<ul>
  <li> Placing at least five of one's stones in an uninterrupted line (row/column/diagonal) or
  </li><li> Capturing five pairs of opponent's stones
</li></ul>
while also scoring as many points as possible.

<h2>The Players</h2>

Two players play this game - 
one player will be the human user of your program,
and the other player will be your program/computer.

The two players will play a "tournament", consisting of one or more rounds.
Each round will consist of the two players playing till one of them wins the round.

<h2>The Setup</h2>

<h3>Pieces</h3>

The game uses white and black stones.

<h3>Setup</h3>

The game uses a 19 X 19 board.
The columns are numbered A through S from left to right.
The rows are numbered 1 through 19 from bottom to top.
The intersections are referred to by their column and row numbers,
e.g., the bottom left intersection is A1 and the center of the board is J10.

<h2>A Round</h2>

<h3>First Player</h3>

On the first round, a coin is tossed and the human player is asked to call the toss.
If the human player correctly calls the toss, the human player plays first.
Otherwise, the computer player plays first.
<p>
  On subsequent rounds, the player with the most points plays first.
  If both the players have the same number of points, a coin is tossed to determine the first player.
</p><p>
The first player plays white stones. The other player plays black stones.
  
</p><h3>A Turn</h3>


On the first turn, the first player must place a white stone at the center (J10).
The other player can place a black stone anywhere on the board.
<p>
  On the second turn, the first player must place another white stone at least 3 intersections away from the first white stone.
</p><p>
  Thereafter, the two players alternate turns, placing one stone per turn on the board.
</p><p>
  A player can capture a pair of the opponent's stones if the opponent's stones are next to each other in a row, column or diagonal and the player places their stones on both sides of the row, column or diagonal. 
  
  <h3>End of a round</h3>

  A round ends when a player:
<ul>
  <li> Places at least five of one's stones in an uninterrupted line (row/column/diagonal) or    
  </li><li> Captures five pairs of opponent's stones
</li></ul>

<h3>Score</h3>

When a round ends, each player is awarded points as follows:
<ul>
  <li> 5 points to the player who has placed at least five of their stones in an uniterrupted row/column/diagonal
  </li><li> 1 point for each pair of opponent's stones captured
  </li><li> 1 point for each set of four of the player's stones placed in an uninterrupted row/column/diagonal
</li></ul>
A player would want to maximize their score and may want to delay winning a round when possible.

<h2>A Tournament</h2>
 After each round, the human player is asked whether
she/he wants to play another round.
  <ul>
   <li>   If yes, another round is played as described above
     and the process is repeated.
   </li><li> If no, the winner of the tournament is announced and the game quits.
   The winner of the tournament is the player who has earned the most number of points over all the rounds.
   If both the players have the same number of points, the tournament is a draw.
  </li></ul>

<h2>Demonstration</h2>
For a full demonstration of the android application, refer to this video: https://www.youtube.com/watch?v=6Z5stH2wfS4
