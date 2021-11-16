# manufacturingproduction
Introduction:

A manufacturing facility has products it wants to produce. Manufacturing team provides the products, production times, changeovers, and initial order. This script will conduct neighbor swap to improve the total production time, and use the Held Karp algorithm to solve the traveling salesmen problem to find solutions with optimal total production time.

Inputs: 
JSON file containing
  * Products to produce with key of "products"
  * Production times with key of "production time"
  * Changeover matrix with key of "changeover"
  * Initial production order with key of "order"
  
Outputs:
* Neighbor swap solution with Total Production Time
* Optimal solution(s) with Total Production Time

Example: 

Input: 

{
	"order": ["B", "G", "E", "A", "C", "I", "J", "D", "H", "F"], 
	"products": ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"], 
	"production time": [7, 13, 2, 4, 21, 6, 8, 12, 17, 22], 
	"changeover": [[0, 9, 13, 6, 7, 8, 3, 17, 7, 3],
		[3, 0, 8, 7, 9, 4, 3, 16, 3, 8],
		[5, 0, 0, 8, 7, 9, 4, 3, 16, 5],
		[7, 2, 5, 0, 4, 2, 5, 7, 2, 5],
		[4, 3, 4, 7, 0, 4, 3, 4, 7, 3],
		[2, 14, 7, 7, 5, 0, 1, 4, 6, 4],
		[8, 7, 9, 4, 16, 3, 0, 4, 9, 8],
		[11, 5, 2, 3, 2, 6, 4, 0, 9, 7],
		[4, 2, 12, 9, 1, 5, 5, 6, 0, 9],
		[9, 3, 6, 7, 8, 3, 17, 7, 3, 0]]
}

Output:

NEIBOR SWAP: 

["B","G","C","E","A","J","I","H","D","F"]

PRODUCTION TIME: 152

OPTIMAL PRODUCTIONS: 

["D","I","E","H","C","B","A","J","F","G"]

PRODUCTION TIME: 131

["H","C","B","A","J","F","G","D","I","E"]

PRODUCTION TIME: 131
