Contributions of each member: 

Iman:

I wrote the base player that played the game at minimal functionality. To do this, it simply played the first valid move it found on the board. I then wrote the minimax algorithm. Initially, I wrote an algorithm that did not exactly follow the minimax algorithm described - in week two after looking through the code more, we realized the deviation from the given minimax algorithm, and I rewrote it to match the given method. Both times this was written recursively, so to beat ConstantTimePlayer and BetterPlayer all we had to do was increase the the depth of the tree of the corrected minimax algorithm. I then tried to implement principal variation search but had difficulty getting that to work. The attempted code can be found in PVSPlayer.java.

Rupesh:

I helped to create the heuristic that this program uses. I initially created a heuristic in which a particular move on a board is given a score and that score is taken as the comparable value. Later (in assignment 2) I learned that it would be better to take the heuristic as a property of the board itself. In this way, we do not have to pass a certain move to the board to get it’s heuristic. We can know how preferable a board is by simply calling this function on the board itself. I also created the GoodPlayer object which uses the heuristic to beat SimplePlayer. I then implemented alphabeta pruning to improve our algorithm's time efficiency.

———————————

Improvements we made to our AI to make it competition-worthy:

Okay so the first time around, we accidentally programmed our AI with a misunderstanding of the minimax algorithm. Using the heuristic mentioned above, we implemented minimax but did so in such a way that each move is scored instead of scoring each possible board. It did not use the minimum/maximum scores in the best way (i.e. our score was not being optimized).

This next time around, we sat down and thought of how to fix this and decided to use a more appropriate heuristic. Since our algorithm was recursive, we were able to rapidly improve the speed of our AI by simply increasing the depth of our tree and it beat better player on the first run with a depth of 5 with a minimal time increase. 

We tried three more approaches to improve our time efficiency. The first two were implementing principal variation search and multithreading the generation of our tree. Though we weren't able to get these running, we've included the code in our submission. We also imiplemented alphabeta pruning, which works and is included in our submission as well.
