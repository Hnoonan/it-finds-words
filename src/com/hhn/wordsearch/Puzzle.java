package com.hhn.wordsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Puzzle {

	char[][] puzzlearray;
	Scanner scan;
	List<String> searchterms; // List of words to find
	int dimensions,
		wrap; //0 or 1 boolean to allow word wrapping in the puzzle
	// Constructor for puzzle, takes in a filename and then reads in that file to produce the word search puzzle it specifies and a list of words
	//  		to search for 
	//Input : filename
	//Output : none
	//Postcondition : puzzlearray, searchterms, and dimensions populated with the correct data from the input file
	public Puzzle(String puzzletext)
	{
		try {
			scan = new Scanner(new File(puzzletext));
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			e.printStackTrace();
		}
		List<String> lines = new ArrayList<String>(); //List of rows that will form the word search puzzle
		lines.add(scan.nextLine()); // Scan the first line to get the dimension
		dimensions = Integer.valueOf(lines.get(0));
		lines.remove(0); // Remove the number from the list, so that it doesn't get added to the search table
		lines.add(scan.nextLine()); // Scan the Second line for whether or not we allow word wrapping in this puzzle
		wrap = Integer.valueOf(lines.get(0));
		lines.remove(0); // Dont add the wrap to the search table
		if(wrap >= 1) //So nothing gets broken by bad input
		{
			wrap = 1;
		}
		int rows = 0;
		while (rows < dimensions) {
		  lines.add(scan.nextLine());
		  rows++;
		}
		System.out.println("dimensions = " + dimensions);
		searchterms = new ArrayList<String>();
		while (scan.hasNextLine())
		{
			searchterms.add(scan.nextLine());
		}
		
		puzzlearray = new char[dimensions][dimensions];
		System.out.println("The puzzle looks like this:");
		for( int i = 0; i < dimensions; i++)
		{
			for (int x = 0; x < dimensions; x++)
			{
				puzzlearray[i][x] = lines.get(i).charAt(x);
				System.out.print(lines.get(i).charAt(x));
			}
			System.out.println("");
		}
	}
	//Systematically searches every node in every direction on the word search in order to find the specified words
	//Input/Output : None
	//Precondition : Valid information is stored in the class variables
	//Postcondition : List of words and their coordinates printed into console
	public void solvePuzzle()
	{
		System.out.println("The search terms look like this:");
		List<String> foundwords = new ArrayList();
		for (int y = 0; y < searchterms.size(); y++)
		{
			System.out.println(searchterms.get(y));
		}
		System.out.println(dimensions);
		for( int i = 0; i < dimensions; i++)
		{
			for (int x = 0; x < dimensions; x++) // Iterate over the entire puzzle
			{
				//Is the space we're looking at the start of a word we're looking for
				for (int z = 0; z < searchterms.size(); z++)
				{
					if(puzzlearray[i][x] == searchterms.get(z).charAt(0)) //This could be the start of a word, get ready to search
					{
						String foundstring = new String();
						boolean done = false;
						int position = 0; // tracks the position in the word that we're looking at
						int looki = i; //Allows us to manipulate our position in the array withoug interfering with the outer loops
						int lookx = x;
						int searchdir = 1; //Designates the direction we're looking in, with 2 being down, 8 being up ect. (keyboard number pad directions) 
											//default it to 1
						int maxword = 0; //maximum size of a word in our array, the limit of string lengths to check in our search 
						List<String> possible = new ArrayList<String>(searchterms); // Keeps track of the strings we might still be looking at
						HashSet found = new HashSet();
						for(int u = 0; u <possible.size(); u++) //What words can we rule out at this point
						{
							if(possible.get(u).length() > maxword)
							{
								maxword = possible.get(u).length();
							}
							if(puzzlearray[looki][lookx] != possible.get(u).charAt(0))
							{
								possible.remove(u);
							}
						}
						while (done == false)
						{
							boolean foundit = false;
							foundstring = (foundstring + Character.toString(puzzlearray[looki][lookx])); //check the new character
							for(int q = 0; q <possible.size(); q++) // Have we found any words?
							{
								if (foundstring.equals(possible.get(q))) 
								{
									for(int t = 0; t < foundwords.size(); t++) //ungainly, but stops duplicates from appearing
									{
										if(foundstring.equals(foundwords.get(t)))
										{
											foundit = true;
										}
									}
									if (foundit == false) 
									{
									System.out.println("Found " + possible.get(q) + " at: " + i+ "," + x);
									}
									foundwords.add(possible.get(q));
									possible.remove(q); //We found it, no need to keep looking for it from here on in
								}
							}
							if (searchdir == 10 ) //If we've exhausted all directions, we're done with this node of the puzzle
							{
								done = true;
								break;
							}
							// Now to check the next character, based on position and searching direction
							position ++;
							if(searchdir == 1) // Down and left diagonally
							{
								looki = (looki+1);
								lookx = (lookx-1);
							}
							if(searchdir == 2) // Down
							{
								looki = (looki+1);
							}
							if(searchdir == 3) // Down and right diagonally
							{
								looki = (looki+1);
								lookx = (lookx+1);
							}
							if(searchdir == 4) // Left
							{
								lookx = (lookx-1);
							}
							if(searchdir == 5) //Not really a direction in our scheme, skip it
							{
								searchdir++;
							}
							if(searchdir == 6) // Right
							{
								lookx = (lookx+1);
							}
							if(searchdir == 7) // Up and Left
							{
								looki = (looki-1);
								lookx = (lookx-1);
							}
							if(searchdir == 8) // Up
							{
								looki = (looki-1);
							}
							if(searchdir == 9) // Up and Right
							{
								looki = (looki-1);
								lookx = (lookx+1);
							}
							if (wrap == 1)
							{
								//now to account for the wraparound manually since Java modulus returns the sign of the divisor :(
								if (looki == dimensions)
								{
									looki = 0;
								}
								if (looki == -1)
								{
									looki = (dimensions-1);
								}
								if (lookx == dimensions)
								{
									lookx = 0;
								}
								if (lookx == -1)
								{
									lookx = (dimensions -1);
								}
							}
							else //Wraparound is disabled
							{
								if(looki == dimensions || looki == -1 || lookx == dimensions || lookx == -1) //Overran the boundaries of the puzzle
																											// reset everything and move on
								{
									searchdir++;
									looki = i;
									lookx = x;
									foundstring = new String();
									position = 0;
								}
							}
							
							if(position > maxword) // We've exhausted one direction, reset everything and check the next
							{
								searchdir++;
								looki = i;
								lookx = x;
								foundstring = new String();
								position = 0;
							}
						}
					}
				}	
			}
		}		
	}
}
