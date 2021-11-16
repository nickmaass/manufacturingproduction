package production_order;

//Imports for reading JSON file
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Production_Order {

	public static void main(String[] args) {
		
		JSONObject jo = getJSON("ProductionData.json");
		boolean goodJSON = checkJSON(jo);
		if (goodJSON){
			JSONObject jo1 = runSwaps(jo, false);
			System.out.println("NEIGHBOR SWAP: ");
			productionTime(jo1, true);
		}
		findOptimalProductionTime(jo);
	}
	public static JSONObject runSwaps(JSONObject jo, boolean display) {
		JSONArray order = (JSONArray) jo.get("order");
		int changeoverImprovements[] = new int[order.size()];
		int changeMax;
		int changeIndex = -1;
		boolean complete = false;
		while (!complete) {	
			changeMax = 0;
			for (int i = 0; i < order.size()-1;i++){
				changeoverImprovements[i] = compareChangeover(jo, i);
				if (changeoverImprovements[i] > changeMax){
					changeMax = changeoverImprovements[i];
					changeIndex = i;
				}
			}
			if (changeMax == 0) {
				complete = true;
			} else {
				order = swapOrder(order, changeIndex);
				if (display){
					productionTime(jo, display);
				}
			}
		}
		jo.put("order", order);
		return(jo);
	}
	public static JSONArray swapOrder(JSONArray order, int i){
		//System.out.println("SWAP " + order.get(i) + " AND " + order.get(i+1));
		String temp = (String) order.get(i);
		order.set(i, order.get(i+1));
		order.set(i+1, temp);
		return order;
	}
	public static int productionTime(JSONObject jo, boolean display) {
		int duration = 0;
		int thisProductIndex, nextProductIndex;
		int processTime;
		
		JSONArray order = (JSONArray) jo.get("order");
		JSONArray products = (JSONArray) jo.get("products");
		JSONArray productionTime = (JSONArray) jo.get("production time");
		JSONArray changeover = (JSONArray) jo.get("changeover");
		
		for(int i = 0; i < order.size();i++){
			thisProductIndex = products.indexOf(order.get(i));
			processTime = productionTime.get(thisProductIndex).hashCode();
			duration = duration + processTime;
			if (i < order.size()-1){
				nextProductIndex = products.indexOf(order.get(i+1));
				duration = duration + ((JSONArray) changeover.get(thisProductIndex)).get(nextProductIndex).hashCode();
			}
		}
		if (display){
			System.out.println(order);
			System.out.println("PRODUCTION TIME: " + duration);
		}
		return duration;
	}
	public static int productionTime(JSONObject jo, JSONArray order, boolean display){
		int duration = 0;
		int thisProductIndex, nextProductIndex;
		int processTime;
		
		JSONArray products = (JSONArray) jo.get("products");
		JSONArray productionTime = (JSONArray) jo.get("production time");
		JSONArray changeover = (JSONArray) jo.get("changeover");
		
		for(int i = 0; i < order.size();i++){
			thisProductIndex = products.indexOf(order.get(i));
			processTime = productionTime.get(thisProductIndex).hashCode();
			duration = duration + processTime;
			if (i < order.size()-1){
				nextProductIndex = products.indexOf(order.get(i+1));
				duration = duration + ((JSONArray) changeover.get(thisProductIndex)).get(nextProductIndex).hashCode();
			}
		}
		if (display){
			System.out.println(order);
			System.out.println("PRODUCTION TIME: " + duration);
		}
		return duration;
	}
	public static int compareChangeover(JSONObject jo, int i) {
		JSONArray order = (JSONArray) jo.get("order");
		JSONArray products = (JSONArray) jo.get("products");
		JSONArray changeover = (JSONArray) jo.get("changeover");
		int maxIndex = order.size()-1;
		int pI_P, pI_0, pI_1, pI_2; //short for product index. P is short for previous.
		int tP, t0, t1; //intermediate terms
		if (i==0){
			pI_0 = products.indexOf(order.get(i));
			pI_1 = products.indexOf(order.get(i+1));
			pI_2 = products.indexOf(order.get(i+2));
			t0 = ((JSONArray) changeover.get(pI_0)).get(pI_1).hashCode() -
					((JSONArray) changeover.get(pI_1)).get(pI_0).hashCode();
			t1 = ((JSONArray) changeover.get(pI_1)).get(pI_2).hashCode() -
					((JSONArray) changeover.get(pI_0)).get(pI_2).hashCode();
			return(t0 + t1);
		} else if (i == maxIndex - 1) {
			pI_P = products.indexOf(order.get(i-1));
			pI_0 = products.indexOf(order.get(i));
			pI_1 = products.indexOf(order.get(i+1));
			tP = ((JSONArray) changeover.get(pI_P)).get(pI_0).hashCode() -
					((JSONArray) changeover.get(pI_P)).get(pI_1).hashCode();
			t0 = ((JSONArray) changeover.get(pI_0)).get(pI_1).hashCode() -
					((JSONArray) changeover.get(pI_1)).get(pI_0).hashCode();
			return(tP + t0);
		} else {
			pI_P = products.indexOf(order.get(i-1));
			pI_0 = products.indexOf(order.get(i));
			pI_1 = products.indexOf(order.get(i+1));
			pI_2 = products.indexOf(order.get(i+2));
			tP = ((JSONArray) changeover.get(pI_P)).get(pI_0).hashCode() -
					((JSONArray) changeover.get(pI_P)).get(pI_1).hashCode();
			t0 = ((JSONArray) changeover.get(pI_0)).get(pI_1).hashCode() -
					((JSONArray) changeover.get(pI_1)).get(pI_0).hashCode();
			t1 = ((JSONArray) changeover.get(pI_1)).get(pI_2).hashCode() -
					((JSONArray) changeover.get(pI_0)).get(pI_2).hashCode();
			return(tP + t0 + t1);
		}
	}
	public static boolean checkJSON(JSONObject jo){
		String[] requiredKeys = new String[] {"products", "production time", "changeover", "order"};
		String key;
		JSONArray ja = (JSONArray) jo.get("products");
		JSONArray row;
		int requiredLength = ja.size();
		for (int i=0;i<requiredKeys.length;i++){
			key = requiredKeys[i];
			if (!jo.containsKey(key)){
				System.out.println("Missing key - " + key);
				return(false);
			}
			ja = (JSONArray) jo.get(key);
			if (ja.size() != requiredLength){
				System.out.println("Incorrect Size - " + key);
				return(false);
			}
			if (key == "changeover"){
				for (int j=0; j < ja.size();j++){
					row = (JSONArray) ja.get(j);
					if (row.size() != requiredLength){
						System.out.println("Incorrect Size (Rows) - " + key);
						return(false);
					}
					if (row.get(j).hashCode() != 0){
						System.out.println("Non-Zero diagonal - row " + j + " col " + j);
						return(false);
					}
					
				}
			}
		}
		return(true);
	}
	public static JSONObject getJSON(String jsonFile) {
		try {
			Object obj = new JSONParser().parse(new FileReader(jsonFile));
			JSONObject jo = (JSONObject) obj;
			return(jo);
		} catch(Exception FileNotFoundException) {
			System.out.println("File was not found.");
			return(null);
		}
	}
	
	//EXTRA - Find Optimal Production Time
	public static int findOptimalProductionTime(JSONObject jo){
		System.out.println("\nOPTIMAL PRODUCTIONS: ");
		JSONArray c = (JSONArray) jo.get("changeover");
		int[] circuit = HeldKarpCircuit(c, 1);
		int[] circuitWeights = getCircuitWeights(c, circuit);
		int[] sol = findEndMaxSolutions(circuit, circuitWeights);
		int optimalProductionTime = 0;
		for (int i = 0; i < sol.length; i++){
			circuit = HeldKarpCircuit(c, sol[i]);
			circuitWeights = getCircuitWeights(c, circuit);
			optimalProductionTime = productionTime(jo, convertCircuitToProductOrder(jo, circuit), true);
		}
		return optimalProductionTime;
	}
	public static JSONArray convertCircuitToProductOrder(JSONObject jo, int[] circuit){
		JSONArray products = (JSONArray) jo.get("products");
		JSONArray order =  (JSONArray) products.clone();
		for (int i = 0; i < circuit.length-1; i++){
			order.set(i, products.get(circuit[i]-1));
		}
		return order;
	}
	public static int[] findEndMaxSolutions(int[] circuit, int[] circuitWeights){
		int maxWeight = getMax(circuitWeights);
		int[] solutions = new int[getArrayCount(circuitWeights, maxWeight)];
		int cnt = 0;
		for (int i = 0; i < circuitWeights.length; i++){
			if (circuitWeights[i] == maxWeight){
				solutions[cnt] = circuit[i+1];
				cnt = cnt + 1;
			}
		}
		return solutions;
	}
	public static int[] getCircuitWeights(JSONArray c, int[] circuit){
		int[] circuitWeights = new int[circuit.length - 1];
		for (int i=0; i < circuit.length - 1; i ++){
			circuitWeights[i] = get_c(c, circuit[i], circuit[i+1]);
		}
		return circuitWeights;
	}
	public static int[] HeldKarpCircuit(JSONArray c, int startpt){
		int[] hkSet = new int[c.size()];
		for (int i=0; i < c.size();i++){
			hkSet[i] = i + 1;
		}
		startpt = startpt - 1;
		int[] circuit = getCircuit(c, hkSet[startpt], removeItemfromArray(hkSet, hkSet[startpt]), hkSet[startpt]);
		return circuit;
	}
	public static int HeldKarpMinLen(JSONArray c, int startpt){
		int[] hkSet = new int[c.size()];
		for (int i=0; i < c.size();i++){
			hkSet[i] = i + 1;
		}
		int minLen = g(c, hkSet[startpt], removeItemfromArray(hkSet, hkSet[startpt]), hkSet[startpt]);
		return minLen;
	}
	public static int[] getCircuit(JSONArray c, int x, int[] s, int x0){
		int[] circuit = new int[c.size()+1];
		circuit[0] = x0;
		for(int i = 1; i < c.size()+1; i++) {
			x = p(c, x, s, x0);
			if (s.length > 0){
				s = removeItemfromArray(s, x);
			}
			circuit[i] = x;
		}
		return circuit;
	}
	public static int g(JSONArray c, int x, int[] s, int x0){
		int gVal;
		if (s == null){
			gVal = get_c(c, x, x0);
		} else if (s.length == 1){
			gVal = get_c(c, x, s[0]) + g(c, s[0], null, x0);
		} else {
			int[] gVals = new int[s.length];
			for (int i = 0; i < s.length ; i++){
				gVals[i] = get_c(c, x, s[i]) + g(c, s[i], removeItemfromArray(s, s[i]), x0);
			}
			gVal = getMin(gVals);
		}
		return(gVal);
	}
	public static int p(JSONArray c, int x, int[] s, int x0){
		int pVal = 0;
		if (s == null){
			pVal = x0;
		} else if (s.length == 0){
			pVal = x0;
		} else if (s.length == 1){
			pVal = s[0];
		} else {
			int[] gVals = new int[s.length];
			for (int i = 0; i < s.length ; i++){
				gVals[i] = get_c(c, x, s[i]) + g(c, s[i], removeItemfromArray(s, s[i]), x0);
			}
			pVal = s[getMinIndex(gVals)];
		}
		return(pVal);
	}
	public static int[] removeItemfromArray(int[] s, int sn){
		int[] s1 = new int[s.length - 1];
		int cnt = 0;
		for (int i = 0; i < s.length ; i++){
			if (s[i] != sn){
				s1[cnt] = s[i];
				cnt = cnt + 1;
			}
		}
		return s1;
	}
	public static int getMin(int[] inputArray){ 
		int minValue = inputArray[0];
		for(int i=1;i<inputArray.length;i++){ 
	      if(inputArray[i] < minValue){ 
	        minValue = inputArray[i]; 
	      }
	    } 
	    return minValue; 
	} 
	public static int getMax(int[] inputArray){ 
		int maxValue = inputArray[0];
		for(int i=1;i<inputArray.length;i++){ 
	      if(inputArray[i] > maxValue){ 
	        maxValue = inputArray[i]; 
	      }
	    } 
	    return maxValue; 
	} 
	public static int getMinIndex(int[] inputArray){ 
		int minIndex = 0;
		int minValue = inputArray[0];
		for(int i=1;i<inputArray.length;i++){ 
	      if(inputArray[i] < minValue){ 
	        minValue = inputArray[i]; 
	        minIndex = i;
	      } 
	    } 
	    return minIndex; 
	} 
	public static int getArrayCount(int[] inputArray, int x){
		int count = 0;
		for (int i = 0; i < inputArray.length; i++){
			if (inputArray[i] == x){
				count = count + 1;
			}
		}
		return count;
	}
	public static int get_c(JSONArray c, int row, int col){
		return ((JSONArray) c.get(row-1)).get(col-1).hashCode();
	}
	public static void printArray(int[] inputArray){
		for(int i=0;i<inputArray.length;i++){ 
			if (i == inputArray.length - 1){
				System.out.println(inputArray[i]);
			} else {
				System.out.print(inputArray[i] + "-");
			}
	    } 
	}
}
