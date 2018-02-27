//Kevin Nguyen
//UT ID: kdn433
//CS314 Assignment 4, Section: 52870

/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          02 Oct 09; 12 Feb 10; 04 Oct 12
 */

import java.util.Stack;

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }
// safe car, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }
// safe cdr, returns null if lst is null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }
    public static Object second (Cons x) { return first(rest(x)); }
    public static Object third (Cons x) { return first(rest(rest(x))); }
    public static void setfirst (Cons x, Object i) { x.car = i; }
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }
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

    // iterative destructive merge using compareTo
public static Cons dmerj (Cons x, Cons y) {
  if ( x == null ) return y;
   else if ( y == null ) return x;
   else { Cons front = x;
          if ( ((Comparable) first(x)).compareTo(first(y)) < 0)
             x = rest(x);
            else { front = y;
                   y = rest(y); };
          Cons end = front;
          while ( x != null )
            { if ( y == null ||
                   ((Comparable) first(x)).compareTo(first(y)) < 0)
                 { setrest(end, x);
                   x = rest(x); }
               else { setrest(end, y);
                      y = rest(y); };
              end = rest(end); }
          setrest(end, y);
          return front; } }

public static Cons midpoint (Cons lst) {
  Cons current = lst;
  Cons prev = current;
  while ( lst != null && rest(lst) != null) {
    lst = rest(rest(lst));
    prev = current;
    current = rest(current); };
  return prev; }

    // Destructive merge sort of a linked list, Ascending order.
    // Assumes that each list element implements the Comparable interface.
    // This function will rearrange the order (but not location)
    // of list elements.  Therefore, you must save the result of
    // this function as the pointer to the new head of the list, e.g.
    //    mylist = llmergesort(mylist);
public static Cons llmergesort (Cons lst) {
  if ( lst == null || rest(lst) == null)
     return lst;
   else { Cons mid = midpoint(lst);
          Cons half = rest(mid);
          setrest(mid, null);
          return dmerj( llmergesort(lst),
                        llmergesort(half)); } }


    // ****** your code starts here ******
    // add other functions as you wish.

//Set union function that will recursively merge two sorted lists without duplicates
public static Cons union(Cons x, Cons y) {
	Cons xCopy = llmergesort(x), yCopy = llmergesort(y);
	return mergeUnion(xCopy,yCopy);
}

//merge union function that will assist in the recursion to get a final sorted merged list
public static Cons mergeUnion(Cons xCopy, Cons yCopy) {
	if (xCopy == null) {
		return yCopy;
	}
	else if (yCopy == null) {
		return xCopy;
	}
	else {
		if (((Comparable) first(xCopy)).compareTo(first(yCopy)) == 0) {
			return cons(first(xCopy), mergeUnion(rest(xCopy), rest(yCopy)));
		}
		else {
			if (((Comparable) first(xCopy)).compareTo(first(yCopy)) < 0) {
				return cons(first(xCopy), mergeUnion(rest(xCopy),yCopy));
			}
			else {
				return cons(first(yCopy), mergeUnion(xCopy,rest(yCopy)));
			}
		}
	}
}

//set difference function that will recursively find the difference of two sorted lists together
public static Cons setDifference(Cons x, Cons y) {
	Cons result = list(), xCopy = llmergesort(x), yCopy = llmergesort(y);
	return mergeDiff(xCopy, yCopy, result);
}

//merge difference auxiliary function to assist in recursion to get final sorted merged list symmetric difference
public static Cons mergeDiff(Cons xCopy, Cons yCopy, Cons result) {
	if (xCopy == null) {
		return result;
	}
	else if (((Comparable) first(xCopy)).compareTo((String) first(yCopy)) < 0) {
		result = (cons(first(xCopy), mergeDiff(rest(xCopy), yCopy, result)));
	}
	else if (((Comparable) first(xCopy)).compareTo((String) first(yCopy)) == 0) {
	    result = mergeDiff(rest(xCopy), rest(yCopy), result);
	}
	else {
		result = mergeDiff(rest(xCopy), yCopy, result);
	}
	return result;
}

//bank function that will return an updated list of accounts
public static Cons bank(Cons accounts, Cons updates) {
	Cons tempUpdate = null, result = null;
	int balance = 0;
	Cons updateCopy = deepCopy(updates, list()), accountCopy = deepCopy(accounts, list());
	tempUpdate = llmergesort(union(accountCopy, updateCopy));
	return bankProcess(accountCopy, tempUpdate, result, balance);	
}

//helper method for bank that processes through the account and updates it accordingly
public static Cons bankProcess(Cons accounts, Cons tempUpdate, Cons results, int balance) {
	Boolean isFound = false;
	String name = ((Account) first(tempUpdate)).name(), tempName = null; balance = ((Account) first(tempUpdate)).amount(); 
	tempUpdate = rest(tempUpdate);
	while (tempUpdate != null) {
		tempName = ((Account) first(tempUpdate)).name();
		if (!isFound(accounts, name, isFound) && balance > 0) {
			System.out.println("NEW ACCOUNT: " + name + ", final amount: " + balance);
		}
		else if (!isFound(accounts, name, isFound) && balance <= 0) {
			isFound = true;
			System.out.println("NO ACCOUNT: " + name + ", final amount: " + balance);
		}
		else {
			if (balance < 0) { 
				balance = balance - 30;
				System.out.println("OVERDRAFT: " + name + ", final amount: " + balance);
			}
		}
		if (name.equals(tempName)) {
			balance = balance + ((Account) first(tempUpdate)).amount();
		}
		else {
			if (!name.equals(((Account) first(tempUpdate)).name()) && isFound == false) {
				Account updateComplete = new Account(name, balance);
				Cons updatedAccount = list(updateComplete);
				results = union(results, updatedAccount);
			}
			name = ((Account) first(tempUpdate)).name(); balance = ((Account) first(tempUpdate)).amount();			
		}
		isFound = false;
		tempUpdate = rest(tempUpdate);	
		if (tempUpdate == null) {
			if (balance < 0) {
				balance = balance - 30;
				System.out.println("OVERDRAFT: " + name + ", final amount: " + balance);
			}
			Account updateComplete = new Account(name, balance);
			Cons updatedAccountInfo = list(updateComplete);
			results = union(results, updatedAccountInfo);	
		}
	}
	return results;
}

//isFound will search through the list to see if the account exists or not in original
public static Boolean isFound(Cons accounts, String name, boolean isFound) {	
	while (accounts != null) {
		if (name == ((Account) first(accounts)).name()) {
			isFound = true;
		}
		accounts = rest(accounts);
	}
	return isFound;
}

//deep copy will make an exact duplicate of the original list to modify without destroying original
public static Cons deepCopy(Cons accounts, Cons result) {
	if (accounts == null) {
		return result;
	}
	else {
		return cons(first(accounts), deepCopy(rest(accounts), result));
	}
}

//Merge function for arrays with a new combined array is to be expected
public static String[] mergearr(String [] x, String [] y) {
	String[] result = new String[x.length + y.length]; int counter = 0, counterX = 0, counterY = 0;	
	while (counter <= result.length-1) {
		if (counterX <= x.length-1 && counterY <= y.length-1) {
			if (x[counterX].compareTo(y[counterY]) < 0){
				result[counter] = x[counterX];
				++counterX;
			}
			else {
				result[counter] = y[counterY];
				++counterY;
			}
		}
		else if (counterX >= x.length-1 && counterY <= y.length-1) {
			result[counter] = y[counterY];
			++counterY;
		}
		else if (counterX <= x.length-1 && counterY >= y.length-1) {
			result[counter] = x[counterX];
			++counterX;
		}
		++counter;
	}
	return result;
}

//Function mark up that checks if the structure of list of text is correct
public static boolean markup(Cons text) {
	boolean isTrue = true;
	int incorrectTextAtPos = 0, counter = 0;
	String stringText = "", endingString = "", correctText = "", incorrectText = "";
	Stack<String> stackText = new Stack<String>();
	  
	while (text != null) {
		stringText = ((String) first(text));
		if (stringText.startsWith("<") && stringText.length()-1 >= 1) {
			if(stringText.charAt(1) != '/') {
				stackText.push(stringText); 
			}
			else {
				incorrectTextAtPos = counter-1;
				if(stackText.isEmpty()) {
					isTrue = false;
					correctText = "<" + stringText.substring(2, stringText.length()); incorrectText = stringText;
				}
				else {
					endingString = ((String) stackText.pop());
					if (!(stringText.substring(2, stringText.length())).equals(endingString.substring(1, endingString.length()))) {
						isTrue = false;
						correctText = "</" + endingString.substring(1, endingString.length()); incorrectText = stringText;
					}
				}
			} 
	    }
	    ++counter;
	    text = rest(text);   
	    if (text == null && !stackText.isEmpty()) {
	    	isTrue = false;
	    	incorrectTextAtPos = counter-1;
	    	correctText = "</" + ((String) stackText.pop()).substring(1);
	    	incorrectText = ((String) stackText.peek());
	    }
	}
	
	if (!isTrue) {
		System.out.println("Incorrect text located at position: " + incorrectTextAtPos + ". Incorrect Text: " + incorrectText + ". Correct Text: " + correctText);
	}
	return isTrue;
}

    // ****** your code ends here ******

    public static void main( String[] args )
      { 
        Cons set1 = list("d", "b", "c", "a");
        Cons set2 = list("f", "d", "b", "g", "h");
        System.out.println("set1 = " + Cons.toString(set1));
        System.out.println("set2 = " + Cons.toString(set2));
        System.out.println("union = " + Cons.toString(union(set1, set2)));

        Cons set3 = list("d", "b", "c", "a");
        Cons set4 = list("f", "d", "b", "g", "h");
        System.out.println("set3 = " + Cons.toString(set3));
        System.out.println("set4 = " + Cons.toString(set4));
        System.out.println("difference = " +
                           Cons.toString(setDifference(set3, set4)));

        Cons accounts = list(
               new Account("Arbiter", new Integer(498)),
               new Account("Flintstone", new Integer(102)),
               new Account("Foonly", new Integer(123)),
               new Account("Kenobi", new Integer(373)),
               new Account("Rubble", new Integer(514)),
               new Account("Tirebiter", new Integer(752)),
               new Account("Vader", new Integer(1024)) );

        Cons updates = list(
               new Account("Foonly", new Integer(100)),
               new Account("Flintstone", new Integer(-10)),
               new Account("Arbiter", new Integer(-600)),
               new Account("Garble", new Integer(-100)),
               new Account("Rabble", new Integer(100)),
               new Account("Flintstone", new Integer(-20)),
               new Account("Foonly", new Integer(10)),
               new Account("Tirebiter", new Integer(-200)),
               new Account("Flintstone", new Integer(10)),
               new Account("Flintstone", new Integer(-120))  );
        System.out.println("accounts = " + accounts.toString());
        System.out.println("updates = " + updates.toString());
        Cons newaccounts = bank(accounts, updates);
        System.out.println("result = " + newaccounts.toString());

        String[] arra = {"a", "big", "dog", "hippo"};
        String[] arrb = {"canary", "cat", "fox", "turtle"};
        String[] resarr = mergearr(arra, arrb);
        for ( int i = 0; i < resarr.length; i++ )
            System.out.println(resarr[i]);

        Cons xmla = list( "<TT>", "foo", "</TT>");
        Cons xmlb = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "</TR>",
                          "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                          "baz", "</TD>", "</TR>", "</TABLE>" );
        Cons xmlc = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "</TR>",
                          "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                          "baz", "</TD>", "</WHAT>", "</TABLE>" );
        Cons xmld = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "", "</TR>",
                          "</TABLE>", "</NOW>" );
        Cons xmle = list( "<THIS>", "<CANT>", "<BE>", "foo", "<RIGHT>" );
        Cons xmlf = list( "<CATALOG>",
                          "<CD>",
                          "<TITLE>", "Empire", "Burlesque", "</TITLE>",
                          "<ARTIST>", "Bob", "Dylan", "</ARTIST>",
                          "<COUNTRY>", "USA", "</COUNTRY>",
                          "<COMPANY>", "Columbia", "</COMPANY>",
                          "<PRICE>", "10.90", "</PRICE>",
                          "<YEAR>", "1985", "</YEAR>",
                          "</CD>",
                          "<CD>",
                          "<TITLE>", "Hide", "your", "heart", "</TITLE>",
                          "<ARTIST>", "Bonnie", "Tyler", "</ARTIST>",
                          "<COUNTRY>", "UK", "</COUNTRY>",
                          "<COMPANY>", "CBS", "Records", "</COMPANY>",
                          "<PRICE>", "9.90", "</PRICE>",
                          "<YEAR>", "1988", "</YEAR>",
                          "</CD>", "</CATALOG>");
        System.out.println("xmla = " + xmla.toString());
        System.out.println("result = " + markup(xmla));
        System.out.println("xmlb = " + xmlb.toString());
        System.out.println("result = " + markup(xmlb));
        System.out.println("xmlc = " + xmlc.toString());
        System.out.println("result = " + markup(xmlc));
        System.out.println("xmld = " + xmld.toString());
        System.out.println("result = " + markup(xmld));
        System.out.println("xmle = " + xmle.toString());
        System.out.println("result = " + markup(xmle));
        System.out.println("xmlf = " + xmlf.toString());
        System.out.println("result = " + markup(xmlf));

      }
}