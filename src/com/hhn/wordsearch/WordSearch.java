//Author : Henry Noonan
//Work was performed alone
//This project takes in a text filename and, so long as that file is formatted correctly as follows:
//#ofdimensions
//Integer indicating whether or not words in the puzzle can wrap around the sides (0 no, 1 yes)
//word search puzzle in those dimensions
//list of words to find, each on their own line
//
//Creates and subsequently solves, via exhaustive search, a word search puzzle as a 2D array, and reports the coordinates at which the words were found back to the user


package com.hhn.wordsearch;

import java.util.ArrayList;

public class WordSearch {
	
	public static void main (String[] args)
	{
		WordSearch testSearch = new WordSearch();
		testSearch.makepuzzle(args[0]);
	}
	//Creates a new puzzle object and passes it the filename input by the user, then calls this new instance of puzzle to solve the puzzle it created
	//Input : Filename
	//Output : none
	public void makepuzzle(String inputtext)
	{
		Puzzle p = new Puzzle(inputtext);
		p.solvePuzzle();
	}

}
