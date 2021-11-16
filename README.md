# manufacturingproduction
Introduction:
A manufacturing facility has products it wants to produce. Manufacturing team provides the products, production times, and changeovers. This script will conduct neighbor swap to improve the total production time, and use the Held Karp algorithm to solve the traveling salesmen problem to find solutions for the optimal total production time.

Inputs: 
JSON file containing
  Products to produce with key of "products"
  Production times with key of "production time"
  Changeovers with key of "changeover"
  Initial production order with key of "order"
  
Outputs:
  Neibor swap solution with Total Production Time
  Optimal solution(s) with Total Production Time

