# CS 3310, Design and Analysis of Algorithms
At Cal Poly Pomona, Fall 2025 – **Assignment 2**
## Description 
This program primarly creates sets of anagrams based on a given text file. 

Another functionality of this program is that it compares the efficiency of two different hashing methods. **In the first method** I create a hash out of the alphabetical sorting of the string. **In the second method** I create a hash by computing the product of the string when every character is assigned a prime number (See the LETTER_PRIMES map to see the order in which the characters are assigned primes).

## Compilation instructions 
from the root dir run: 
```
javac *.java 
``` 
Then to see results fast just run: 
```
java Main.java 
```
If you want to run your own word file just include it as an argument:
```
java Main.java ./path/mywords.txt
```


## Classes 
### 1. Anagram Utility 
This class is used run all the different hash methods of anagrams at once. It builds sets upon construction.
### 2. Anagram Class
This is the abstract class that holds all the functionality that can be used across the classes with different hash methods 
### 3. Anagrams Prime Hash 
This class uses the method of finding the product of the string using primes. This uses the AnagramsClass.
### 4. Anagrams Sorted String Hash
This class demonstrates the method of finding the sorted string in order to make the hash and to build the anagram sets. 


