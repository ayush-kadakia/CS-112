package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
	public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
        /** COMPLETE THIS METHOD **/
        /** DO NOT create new vars and arrays - they are already created before being sent in
         ** to this method - you just need to fill them in.
         **/

        expr = expr.replaceAll("\\s+", "");

        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(expr);

        Variable varToAdd;
        Array arrToAdd;

        while (matcher.find()) {

            String toAdd = (expr.substring(matcher.start(), matcher.end()));
            varToAdd = new Variable(toAdd);
            arrToAdd = new Array(toAdd);

            if(matcher.end() >= expr.length()){
                if(!vars.contains(varToAdd))
                    vars.add(new Variable(toAdd));
                continue;
            }
            
            else if(!arrays.contains(arrToAdd) && expr.charAt(matcher.end()) == ('['))
                arrays.add(arrToAdd); 
            
            else if (expr.charAt(matcher.end()) != ('[') && !vars.contains(varToAdd))
                vars.add(varToAdd);
        }
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }

    
    /**
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    	expr = expr.replaceAll("\\s+","");
    	
    	Stack<Float> result = new Stack<>();
    	
    	Stack<String> terms = new Stack<>();
    	
    	StringTokenizer expression = new StringTokenizer(expr, delims, true);
    	
    	while(expression.hasMoreTokens()) {
    		String current = expression.nextToken();
    		
    		if(current.matches("[0-9]+"))
    			result.push(Float.parseFloat(current));
    		
    		else if(vars.contains(new Variable(current)))
    			result.push((float)vars.get(vars.indexOf(new Variable(current))).value);
    		
    		else if(arrays.contains(new Array(current)))
    			terms.push(current);
    		
    		else if(current.equals("("))
    			terms.push("(");
    		
    		else if(current.equals(")")){
    			
    			while(terms.peek() != "("){
    				float term2 = result.pop();
    				float term1 = result.pop();
    				
    				String operation = terms.pop();
    				
    				if(operation.equals("+")) {
    					result.push(term1 + term2);
						continue;
    				}
    				
    				else if(operation.equals("-")) {
    					result.push(term1 - term2);
						continue;
    				}
    				else if(operation.equals("*")) {
    					result.push(term1 * term2);
						continue;
    				}
    				else if(operation.equals("/")) {
    					result.push(term1 / term2);
						continue;
    				}
    				
    			}
    			terms.pop();
    		}
    		
    		else if(current.equals("["))
    			terms.push("[");
    		
    		else if(current.equals("]")) {
    			while(terms.peek() != "["){	
    				float term2 = result.pop();
    				float term1 = result.pop();
    				
    				String operation = terms.pop();
    				
    				if(operation.equals("+")) {
    					result.push(term1 + term2);
						continue;
    				}
    				
    				else if(operation.equals("-")) {
    					result.push(term1 - term2);
						continue;
    				}
    				else if(operation.equals("*")) {
    					result.push(term1 * term2);
						continue;
    				}
    				else if(operation.equals("/")) {
    					result.push(term1 / term2);
						continue;
    				}
    			}
    			
    			
    			terms.pop();
    			
    			String arrName = terms.pop();
    			float arrIndex = result.pop();
    			
    			result.push((float)arrays.get(arrays.indexOf(new Array(arrName))).values[(int)arrIndex]);
    		}
    		
    		else if(current.equals("+") || current.equals("-") || current.equals("*") || current.equals("/")){
    			while(terms.isEmpty() != true && pemdas(current, terms.peek())){
    				float term2 = result.pop();
    				float term1 = result.pop();
    				
    				String operation = terms.pop();
    				
    				if(operation.equals("+")) {
    					result.push(term1 + term2);
						continue;
    				}
    				
    				else if(operation.equals("-")) {
    					result.push(term1 - term2);
						continue;
    				}
    				else if(operation.equals("*")) {
    					result.push(term1 * term2);
						continue;
    				}
    				else if(operation.equals("/")) {
    					result.push(term1 / term2);
						continue;
    				}
    				
    			}
    			terms.push(current);
    		}
    	}
    	
    	while(terms.isEmpty() != true) {
			float term2 = result.pop();
			float term1 = result.pop();
			
			String operation = terms.pop();
			
			if(operation.equals("+")) {
				result.push(term1 + term2);
				continue;
			}
			
			else if(operation.equals("-")) {
				result.push(term1 - term2);
				continue;
			}
			else if(operation.equals("*")) {
				result.push(term1 * term2);
				continue;
			}
			else if(operation.equals("/")) {
				result.push(term1 / term2);
				continue;
			}
    	}
    	return result.pop();
    }
    
   
    private static boolean pemdas(String s, String s2)   
    {
    	if((s.equals("*") || s.equals("/")) && (s2.equals("+") || s2.equals("-"))) 
    		return false;
    	if(s2.equals("(") || s2.equals("[")) 
    		return false;

    	return true;
    }

}
