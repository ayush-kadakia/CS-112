package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	private static Node reverse(Node node) {
		
		Node ptr = node;

		Node ans = null;
		
		while (ptr != null)
		{
			if (ptr.term.coeff == 0)
			{
				ptr = ptr.next;
				continue;
			}
			else 
			{
				ans = new Node(ptr.term.coeff, ptr.term.degree, ans);
				ptr = ptr.next;
			}
		}	

		if (ans == null)
			return null;
		else
			return ans;
	}
	
	public static Node add(Node poly1, Node poly2) {
		Node ptr = poly1;
		Node ptr2 = poly2;
		Node temp = null;
		float coe;

		while(ptr != null || ptr2 != null){	
			if(ptr == null &&  ptr2 != null) {
				temp = new Node(ptr2.term.coeff, ptr2.term.degree, temp);
				ptr2 = ptr2.next;
			}	
			else if(ptr2 == null &&  ptr != null) {
				temp = new Node(ptr.term.coeff, ptr.term.degree, temp);
				ptr = ptr.next;
			}	
			else if(ptr.term.degree > ptr2.term.degree) {
				temp = new Node(ptr2.term.coeff, ptr2.term.degree, temp);
				ptr2 = ptr2.next;
			}		
			else if(ptr.term.degree < ptr2.term.degree) {
				temp = new Node(ptr.term.coeff, ptr.term.degree, temp);
				ptr = ptr.next;
			}		
			else if(ptr.term.degree == ptr2.term.degree) {
				coe = ptr.term.coeff + ptr2.term.coeff;
				temp = new Node(coe, ptr.term.degree, temp);
				ptr = ptr.next;
				ptr2 = ptr2.next;
			}			
		}
		
		Node ans = null;
		for(Node ptr3 = temp; ptr3 != null; ptr3 = ptr3.next) {
			if(ptr3.term.coeff != 0)
				ans = new Node(ptr3.term.coeff, ptr3.term.degree, ans);
		}
		
			return ans;
		
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {

        Node ptr1 = poly1;
        Node ptr2 = poly2;
        
        Node temp = null;
        Node ans = null;
        
        if(poly1 == null || poly2 == null) {
        	
        	return null;
        }

        while(ptr1 != null){
            while(ptr2 != null){
                temp = new Node (ptr1.term.coeff * ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, temp);
                ptr2 = ptr2.next;
            }
            ans = add(ans, reverse(temp));
            temp = null;
            ptr2 = poly2;
            ptr1 = ptr1.next;
        }
		
        return ans;
	}
		
	
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		float ans = 0;
		
		for(Node ptr = poly; ptr!= null; ptr= ptr.next) 
			ans += ptr.term.coeff * (Math.pow(x, ptr.term.degree));
		
		return ans;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}