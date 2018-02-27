//Kevin Nguyen
//UT ID: kdn433
//CS314 Assignment 3, Section: 52870

/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 16 Feb 09
 *          01 Feb 12; 08 Feb 12; 22 Sep 13; 26 Dec 13
 */

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

public class Cons
{
    // instance variables
    private Object car;   // traditional name for first
    private Cons cdr;     // "could-er", traditional name for rest
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }

    // Cons is the data type.
    // cons() is the method that makes a new Cons and puts two pointers in it.
    // cons("a", null) = (a)
    // cons puts a new thing on the front of an existing list.
    // cons("a", list("b","c")) = (a b c)
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }

    // consp is true if x is a Cons, false if null or non-Cons Object
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }

    // first returns the first thing in a list.
    // first(list("a", "b", "c")) = "a"
    // safe, first(null) = null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }

    // rest of a list after the first thing.
    // rest(list("a", "b", "c")) = (b c)
    // safe, rest(null) = null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }

    // second thing in a list
    // second(list("+", "b", "c")) = "b"
    public static Object second (Cons x) { return first(rest(x)); }

    // third thing in a list
    // third(list("+", "b", "c")) = "c"
    public static Object third (Cons x) { return first(rest(rest(x))); }

    // destructively change the first() of a cons to be the specified object
    // setfirst(list("a", "b", "c"), 3) = (3 b c)
    public static void setfirst (Cons x, Object i) { x.car = i; }

    // destructively change the rest() of a cons to be the specified Cons
    // setrest(list("a", "b", "c"), null) = (a)     
    // setrest(list("a", "b", "c"), list("d","e")) = (a d e)
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }

    // make a list of the specified items
    // list("a", "b", "c") = (a b c)
    // list() = null
   public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }

    // convert a list to a string in parenthesized form for printing
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


// sum function to add up elements of a list of numbers
public static int sum(Cons lst) {
	int result = 0; Cons listCopy = lst; //shallow copy
    while(listCopy != null) {
    	if (consp(first(listCopy))) {
    		result = result + sum((Cons) first(listCopy));
    	}
    	else {
    		result = result + ((int) first(listCopy));
    	}
    	listCopy = rest(listCopy);
    }
    return result;
}

// Mean function to find average, mean = (sum x[i]) / n  
public static double mean(Cons lst) {
	double result = 0, counter = 0; Cons lstCopy = lst; //shallow copy
	while(lstCopy != null) {
		result = result + (Integer) first(lstCopy);
		lstCopy = rest(lstCopy); 
		counter++;
	}
	return result/counter;
}

// Means Square function to find the average squared
// square of the mean = mean(lst)^2  
// mean square = (sum x[i]^2) / n  
public static double meansq(Cons lst) {
	double result = 0, counter = 0; Cons lstCopy = lst; //shallow copy
	while(lstCopy != null) {
		result = result + ((Integer) first(lstCopy) * (Integer) first(lstCopy));
		lstCopy = rest(lstCopy); 
		counter++;
	}
	return result/counter;
}

//Variance function to compute the statistical variance
public static double variance(Cons lst) {
	double result = (meansq(lst) - (mean(lst)*mean(lst)));
	return result;
}

//Standard Deviation function to compute the standard deviation of a list
public static double stddev(Cons lst) {
	return Math.sqrt(variance(lst));
}

//Sine function to calculate the Sine given a value x
public static double sine(double x) {
	double result = x, counter = 1;
	result = sineb(result, x, counter);
	return result;
}

//Sine auxiliary function to assist in the recursion
public static double sineb(double result, double x, double counter){	
	double numTemp = x, numSignAlternator = -1, numeratorDenominator = 2 * counter + 1;
	if (counter == 21) {
		return result;
	}
	else {
		for (int iterations = 1; iterations <= counter+1; iterations++) { //sign changer
			numSignAlternator = numSignAlternator * -1;
		}
		for (int iterations = 2; iterations <= numeratorDenominator; iterations++) { //numerator binomial
			numTemp = numTemp * x;
		}
		for (double deiterations = numeratorDenominator; deiterations >= 1; deiterations--) { //denominator binomial
			numTemp = numTemp / deiterations;
		}
		result = result + (numSignAlternator * numTemp); counter++;
		return sineb(result,x,counter);
	}
}

//Nthcdr function to output a new list after applying rest to cons list
public static Cons nthcdr(int n, Cons lst) {
	if (n <= 0) {
		return lst;
	}
	else if (sizeList(lst,0) == 0  || n > sizeList(lst, 0)) {
		return null;
	}
	else {
		return nthcdr(n-1,rest(lst));
	}
}

//size auxiliary function to help get the size of the list
public static double sizeList(Cons copyList, double counter) {
	if (copyList == null) {
		return counter;
	}
	else if (consp(first(copyList))) {
		counter = sizeList((Cons) first(copyList), counter);
	}
	else {
		if (first(copyList) != null) {
			counter++;
		}
	}
	return sizeList(rest(copyList), counter);
}

//Elt function to find the first item in position of a cons list
public static Object elt(Cons lst, int n) {
	return first(nthcdr(n,lst));
}

//Interpolate function to find the interpolation of a list and a value
public static double interpolate(Cons lst, double x) {
	int integerX = (int) x;
	double delta = x - integerX;
	return ((Integer) first(nthcdr(integerX, lst))) + delta * (((Integer) first(nthcdr(integerX+1, lst)) - (Integer) first(nthcdr(integerX, lst))));
}

//Sumtr recursive function to add all values in a list
public static int sumtr(Cons lst) {
	Cons copyList = lst; int total = 0;
	if (copyList == null) {
		return 0;
	}
	else {
		if (consp(first(copyList))) {
			total = ((int) sumtr((Cons) first(copyList)) + ((int) sumtr(rest(copyList))));
		}
		else {
			total = ((Integer)first(copyList) + sumtr(rest(copyList)));
		}
		return total;
	}
}

//Sub sequence function to obtain a subset of a cons list
public static Cons subseq (Cons lst, int start, int end) {
	Cons listCopy = lst, newList = list();
	listCopy = nthcdr(start, listCopy);
	int stop = end - start;
	newList = subseqAux(listCopy, newList, stop, 0);
	return newList;
}

//aux to help with subseq
public static Cons subseqAux(Cons listCopy, Cons newList, int stop, int counter) {
	if (counter >= stop) {
		return newList;
	}
	else {
		return cons(first(listCopy), subseqAux(rest(listCopy), newList, stop, counter+1));
	}
}

//Pos filter recursive function that will return a new list containing only non-negative values
public static Cons posfilter (Cons lst) {
	Cons listCopy = lst, newList = list();
	return posfilterAux(listCopy, newList);
}

//posfilter auxiliary function to assist in the recursion
public static Cons posfilterAux(Cons listCopy, Cons result) {
	if (listCopy == null) {
		return result;
	}
	else {
		if ((int) first(listCopy) >= 0) {
			return cons(first(listCopy), posfilterAux(rest(listCopy), result));
		}
		else {
			return posfilterAux(rest(listCopy), result);
		}
	}
}

//Subset recursive function that will return a new list that satisfies a predicate p
public static Cons subset(Predicate p, Cons lst) {
	Cons listCopy = lst; Cons result = list();
	return ((Cons) first(list(subSetAux(listCopy, result, p))));
}

//subset auxiliary function to assist with the recursion
public static Cons subSetAux(Cons listCopy, Cons result, Predicate p) {
	if (listCopy == null) {
		return result;
	}
	else {
		if (p.pred(first(listCopy))) {
			return cons(first(listCopy), subSetAux(rest(listCopy), result, p));
		}
		else {
			return subSetAux(rest(listCopy), result, p);
		}
	}
}

//Map car function that recursively applies functor f to each element in cons list
public static Cons mapcar (Functor f, Cons lst) {
	Cons listCopy = lst, result = list(); 
	return ((Cons) first(list(mapcarAux(listCopy, result, f))));
}

//map car auxiliary functiont to assist with the recursion
public static Cons mapcarAux(Cons listCopy, Cons result, Functor f) {
	if (listCopy == null) {
		return result;
	}
	else {
		if (!first(listCopy).equals(null)) {
			return cons(f.fn(first(listCopy)), mapcarAux(rest(listCopy), result, f));
		}
		else {
			return mapcarAux(rest(listCopy), result, f);
		}
	}
}

//Some function that will recursively output the first element that satisfies the predicate p
public static Object some(Predicate p, Cons lst) {
	Object result = null;
	return someAux(lst, result, p);
}

//Some Auxiliary function to assist in scanning through every element in the list recursively
public static Object someAux(Cons listCopy, Object result, Predicate p) {
	if (listCopy == null) {
		return result;
	}
	else if (consp(first(listCopy))) {
		result = someAux((Cons) first(listCopy), result, p);
	}
	else {
		if (p.pred(first(listCopy))) {
			return first(listCopy);
		}
	}
	return someAux(rest(listCopy), result, p);
}

//Every function that will recursively output TRUE or FALSE depending if all elements satisfy the predicate p 
public static boolean every(Predicate p, Cons lst) {
	boolean result = true;
	return everyAux(lst, result, p);
}

//Every Auxiliary function to assist in scanning through all elements and checking if predicate p is not satisfied
public static Boolean everyAux(Cons listCopy, Boolean result, Predicate p) {
	if (result == false || listCopy == null) {
		return result;
	}
	else if (consp(first(listCopy))) {
		result = everyAux((Cons) first(listCopy), result, p);
	}
	else {
		if (!p.pred(first(listCopy))) {
			result = false;
		}
	}
	return everyAux(rest(listCopy), result, p);
}

//Make a list of Binomial coefficients
//binomial(2) = (1 2 1)
public static Cons binomial(int n) {
	Cons newList = list();
	int value = 1, counter = 0;
	newList = createPTriangle(newList, counter, n, value);
	return newList;
}

//aux to help binomial recursively
public static Cons createPTriangle(Cons result, int counter, int n, int value) {
 if (counter > n) {
 	return result;
 }
 else {
 	if (counter > 0) {
 		value = value * ((n+1) - counter) / counter;
 	}
 	return cons(value, createPTriangle(result, counter+1, n, value));    	
 }  
}
	
    // ****** your code ends here ******

    public static void main( String[] args )
      { 
    	
        Cons mylist =
            list(95, 72, 86, 70, 97, 72, 52, 88, 77, 94, 91, 79,
                 61, 77, 99, 70, 91 );
        System.out.println("mylist = " + mylist.toString());
        System.out.println("sum = " + sum(mylist));
        System.out.println("mean = " + mean(mylist));
        System.out.println("meansq = " + meansq(mylist));
        System.out.println("variance = " + variance(mylist));
        System.out.println("stddev = " + stddev(mylist));
        System.out.println("sine(0.5) = " + sine(0.5));  // 0.47942553860420301
        System.out.print("nthcdr 5 = ");
        System.out.println(nthcdr(5, mylist));
        System.out.print("nthcdr 18 = ");
        System.out.println(nthcdr(18, mylist));
        System.out.println("elt 5 = " + elt(mylist,5));

        Cons mylistb = list(0, 30, 56, 78, 96);
        System.out.println("mylistb = " + mylistb.toString());
        System.out.println("interpolate(3.4) = " + interpolate(mylistb, 3.4));
        Cons binom = binomial(12);
        System.out.println("binom = " + binom.toString());
        System.out.println("interpolate(3.4) = " + interpolate(binom, 3.4));

        Cons mylistc = list(1, list(2, 3), list(list(list(list(4)),
                                                     list(5)),
                                                6));
        System.out.println("mylistc = " + mylistc.toString());
        System.out.println("sumtr = " + sumtr(mylistc));
        Cons mylistcc = list(1, list(7, list(list(2), 3)),
                             list(list(list(list(list(list(list(4)))), 9))),
                             list(list(list(list(5), 4), 3)),
                             list(6));
        System.out.println("mylistcc = " + mylistcc.toString());
        System.out.println("sumtr = " + sumtr(mylistcc));

        Cons mylistd = list(0, 1, 2, 3, 4, 5, 6 );
        System.out.println("mylistd = " + mylistd.toString());
        System.out.println("subseq(2 5) = " + subseq(mylistd, 2, 5));

        Cons myliste = list(3, 17, -2, 0, -3, 4, -5, 12 );
        System.out.println("myliste = " + myliste.toString());
        System.out.println("posfilter = " + posfilter(myliste));

        final Predicate myp = new Predicate()
            { public boolean pred (Object x)
                { return ( (Integer) x > 3); }};

        System.out.println("subset = " + subset(myp, myliste).toString());

        final Functor myf = new Functor()
            { public Integer fn (Object x)
                { return  (Integer) x + 2; }};

        System.out.println("mapcar = " + mapcar(myf, mylistd).toString());

        System.out.println("some = " + some(myp, myliste).toString());

        System.out.println("every = " + every(myp, myliste));
      }
}