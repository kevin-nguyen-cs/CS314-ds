//Kevin Nguyen
//UT ID: kdn433
//Section: 52870
//Assignment 2

/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 02 Sep 09; 27 Jan 10
 *          05 Feb 10; 16 Jul 10; 02 Sep 10; 13 Jul 11
 */

public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }

    // make a new Cons and put the arguments into it
    // add one new thing to the front of an existing list
    // cons("a", list("b", "c"))  =  (a b c)
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }

    // test whether argument is a Cons
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }

    // first thing in a list:    first(list("a", "b", "c")) = "a"
    // safe, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }

    // rest of a list after the first thing:
    //    rest(list("a", "b", "c")) = (b c)
    // safe, returns null if lst is null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }

    // second thing in a list:    second(list("a", "b", "c")) = "b"
    public static Object second (Cons x) { return first(rest(x)); }

    // third thing in a list:    third(list("a", "b", "c")) = "c"
    public static Object third (Cons x) { return first(rest(rest(x))); }

    // destructively replace the first
    public static void setfirst (Cons x, Object i) { x.car = i; }

    // destructively replace the rest
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }

    // make a list of things:   list("a", "b", "c") = (a b c)
    public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }

    // convert a list to a string for printing
    public String toString() {
       return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
       return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
       return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                  + ((rest(lst) == null) ? ")" 
                     : " " + toStringb(rest(lst)) ) ); }

    public static int square(int x) { return x*x; }

    // ****** your code starts here ******

    // Sum of squares of integers from 1..n
	public static int sumsq(int n) {
		if (n == 1) {
			return 1;
		}
		else {
			return ((n*n) + sumsq(n-1));
		}
	}
	
    // Addition using Peano arithmetic
	public static int peanoplus(int x, int y) {
		if (y == 0) {
			return x;
		}
		else {
			++x; --y;
			return peanoplus(x,y);
		}
	}
	
    // Multiplication using Peano arithmetic
	public static int peanotimes(int x, int y) {
		int result = 0;
		
		if (y == 1) {
			return x;
		}
		else if (y == 0) {
			return 0;
		}
		else {
			if (y > 0) {
				while (y > 0) {
					result = result + x;
					--y;
				}
			}
			else if (y < 0) {
				while (y < 0) {
					result = result - x;
					++y;
				}
			}
			return result;
		}
	}
	
    // n choose k: distinct subsets of k items chosen from n items
	public static int choose(int n, int k) {
		int result = 0;
		result = chooseAux(result, n, k);
		return result;
	}
	
	public static int chooseAux(int result, int n, int k) {
		if (n == k) {
			return 1;
		}
		else if (n < k || n < 0 || k < 0) {
			return 0;
		}
		else {
			return choose(n-1,k-1) + choose(n-1,k);
		}
	}

    // Add up a list of Integer
    // iterative version, using while
	public static int sumlist (Cons lst) {
	  int sum = 0;
	  while (lst != null) {
	     sum += ((Integer) first(lst));
	     lst = rest(lst); 
	  }
	  return sum;   
	}
	
	// second iterative version, using for
	public static int sumlistb (Cons arg) {
	  int sum = 0;
	  for (Cons lst = arg ; lst != null; lst = rest(lst)) {
		  sum += ((Integer) first(lst));
	  }
	  return sum; 
	}
	
	// recursive version
	public static int sumlistr (Cons lst) {
		if (lst == null) {
			return 0;
		}
		else {
			return ((Integer) first(lst)) + sumlistr(rest(lst));
		}			
	}
	    // tail recursive version
	public static int sumlisttr (Cons lst) {
		int result = tailRec(first(lst),rest(lst));
		return result;
	}
	
	//AUX FUNCTION
	public static int tailRec(Object x, Cons y) {
		if (y == null) {
			return ((Integer) x);
		}
		else {
			return ((Integer) x) + tailRec(first(y),rest(y));
		}
	}
	
	// Sum of squared differences of elements of two lists
	// iterative version
	public static int sumsqdiff(Cons lst, Cons lstb) {
		int sumTotalDifferences = 0;		
		while (lst != null && lstb != null) {				
			int resultA = peanotimes(((Integer) first(lst)), ((Integer) first(lst)));
			int resultB = peanotimes(((Integer) first(lstb)), ((Integer) first(lstb)));
			sumTotalDifferences = sumTotalDifferences + (resultA - resultB);
			lst = rest(lst); lstb = rest(lstb);
		}
		return sumTotalDifferences;
	}
	
	// recursive version
	public static int sumsqdiffr(Cons lst, Cons lstb) {
		if (lst == null && lstb == null) {
			return 0;
		}
		else {
			int resultA = peanotimes(((Integer) first(lst)), ((Integer) first(lst)));
			int resultB = peanotimes(((Integer) first(lstb)), ((Integer) first(lstb)));
			return (resultA - resultB) + sumsqdiffr(rest(lst), rest(lstb));
		}
	}
	
	// tail recursive version
	//tail Rec
	public static int sumsqdifftr(Cons lst, Cons lstb) {
		int result = 0;
		result = getSumSqDiff(result, lst, lstb);
		return result;
	}
	
	//AUX 
	public static int getSumSqDiff(int result, Cons lst, Cons lstb) {
		if (lst == null && lstb == null) {
			return result;
		}
		else {
			int resultA = peanotimes(((Integer) first(lst)), ((Integer) first(lst)));
			int resultB = peanotimes(((Integer) first(lstb)), ((Integer) first(lstb)));
			result = result + (resultA - resultB);			
			result = getSumSqDiff(result, rest(lst), rest(lstb));
		}
		return result;
	}
	
	
	// Find the maximum value in a list of Integer
	// iterative version
	public static int maxlist (Cons lst) {
		int value = ((Integer) first(lst)), newValue = 0; lst = rest(lst);
		while (lst != null) {
			newValue = ((int) ((Integer) first(lst)));
			if (value - newValue < 0) {
				value = newValue;
			}
			lst = rest(lst);
		}
		return value;
	}

	
	// recursive version
	public static int maxlistr (Cons lst) {
		int value = ((Integer) first(lst)); lst = rest(lst);
		if (lst == null) {
			return 0;
		}
		else {
			if (value - maxlistr(lst) > 0) {
				return value;
			}
			else {
				return maxlistr(lst);
			}
		}
	}
	
	// tail recursive version
	public static int maxlisttr (Cons lst) {
		int value = ((Integer) first(lst)); 
		value = getHighValue (value, rest(lst));
		return value;
	}
	
	//AUX
	public static int getHighValue(int value, Cons lst) {			
		if (lst == null) {
			return value;
		}
		else {
			int nextValue = ((Integer) first(lst)); lst = rest(lst);
			
			if (nextValue > value) {
				value = nextValue;
			}
			return getHighValue(value, lst);
		}
	}

    // Make a list of Binomial coefficients
    // binomial(2) = (1 2 1)
    public static Cons binomial(int n) {
    	int value = 1, counter = 0;
    	String resultStr = "";
    	resultStr = createPTriangle(resultStr, counter, n,value);
    	return list(resultStr);
    }

    //aux to help binomial
	public static String createPTriangle(String resultStr, int counter, int n, int value) {
	    String tempValString = "";
	    if (counter > n) {
	    	return resultStr;
	    }
	    else {
	    	if (counter > 0) {
	    		value = value * ((n+1) - counter) / counter;
	    	}
	    	tempValString = value + "";
	    	resultStr = resultStr + tempValString;
	    	if (counter != n) {
	    		resultStr = resultStr + " ";
	    	}
	    	return createPTriangle(resultStr, counter+1, n, value);
	    }  
	}

    // ****** your code ends here ******


    public static void main( String[] args )
      { 
        System.out.println("sumsq(5) = " + sumsq(5));

        System.out.println("peanoplus(3, 5) = " + peanoplus(3, 5));
        System.out.println("peanotimes(3, 5) = " + peanotimes(3, 5));
        System.out.println("peanotimes(30, 30) = " + peanotimes(30, 30));

        System.out.println("choose 5 3 = " + choose(5, 3));
        System.out.println("choose 100 3 = " + choose(100, 3));
        System.out.println("choose 20 10 = " + choose(20, 10));
        System.out.println("choose 100 5 = " + choose(100, 5));
        for (int i = 0; i <= 4; i++)
          System.out.println("choose 4 " + i + " = " + choose(4, i));

        Cons mylist = list(Integer.valueOf(3), Integer.valueOf(4),
                           Integer.valueOf(8), Integer.valueOf(2));
        Cons mylistb = list(Integer.valueOf(2), Integer.valueOf(1),
                           Integer.valueOf(6), Integer.valueOf(5));

        System.out.println("mylist = " + mylist);

        System.out.println("sumlist = " + sumlist(mylist));
        System.out.println("sumlistb = " + sumlistb(mylist));
        System.out.println("sumlistr = " + sumlistr(mylist));
        System.out.println("sumlisttr = " + sumlisttr(mylist));

        System.out.println("mylistb = " + mylistb);

        System.out.println("sumsqdiff = " + sumsqdiff(mylist, mylistb));
        System.out.println("sumsqdiffr = " + sumsqdiffr(mylist, mylistb));

        System.out.println("sumsqdifftr = " + sumsqdifftr(mylist, mylistb));

        System.out.println("maxlist " + mylist + " = " + maxlist(mylist));
        System.out.println("maxlistr = " + maxlistr(mylist));
        System.out.println("maxlisttr = " + maxlisttr(mylist));

        System.out.println("binomial(4) = " + binomial(4));
        System.out.println("binomial(20) = " + binomial(20));
      }

}
