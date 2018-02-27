//Kevin Nguyen
//kdn433
//Section: 52870, CS314


/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 27 Mar 09; 18 Mar 11; 09 Oct 13
 *          30 Dec 13
 */

import java.util.StringTokenizer;

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
    // test whether argument is a Cons
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
    // access functions for expression representation
    public static Object op  (Cons x) { return first(x); }
    public static Object lhs (Cons x) { return first(rest(x)); }
    public static Object rhs (Cons x) { return first(rest(rest(x))); }
    public static boolean numberp (Object x)
       { return ( (x != null) &&
                  (x instanceof Integer || x instanceof Double) ); }
    public static boolean integerp (Object x)
       { return ( (x != null) && (x instanceof Integer ) ); }
    public static boolean floatp (Object x)
       { return ( (x != null) && (x instanceof Double ) ); }
    public static boolean stringp (Object x)
       { return ( (x != null) && (x instanceof String ) ); }

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

// member returns null if requested item not found
public static Cons member (Object item, Cons lst) {
  if ( lst == null )
     return null;
   else if ( item.equals(first(lst)) )
           return lst;
         else return member(item, rest(lst)); }

public static Cons union (Cons x, Cons y) {
  if ( x == null ) return y;
  if ( member(first(x), y) != null )
       return union(rest(x), y);
  else return cons(first(x), union(rest(x), y)); }

    // combine two lists: (append '(a b) '(c d e))  =  (a b c d e)
public static Cons append (Cons x, Cons y) {
  if (x == null)
     return y;
   else return cons(first(x),
                    append(rest(x), y)); }

    // look up key in an association list
    // (assoc 'two '((one 1) (two 2) (three 3)))  =  (two 2)
public static Cons assoc(Object key, Cons lst) {
  if ( lst == null )
     return null;
  else if ( key.equals(first((Cons) first(lst))) )
      return ((Cons) first(lst));
          else return assoc(key, rest(lst)); }

    public static int square(int x) { return x*x; }
    public static int pow (int x, int n) {        // x to the power n
        if ( n <= 0 ) return 1;
        if ( (n & 1) == 0 )
            return square( pow(x, n / 2) );
        else return x * pow(x, n - 1); }

public static Object reader(String str) {
    return readerb(new StringTokenizer(str, " \t\n\r\f()'", true)); }

public static Object readerb( StringTokenizer st ) {
    if ( st.hasMoreTokens() ) {
        String nexttok = st.nextToken();
        if ( nexttok.charAt(0) == ' ' ||
             nexttok.charAt(0) == '\t' ||
             nexttok.charAt(0) == '\n' ||
             nexttok.charAt(0) == '\r' ||
             nexttok.charAt(0) == '\f' )
            return readerb(st);
        if ( nexttok.charAt(0) == '(' )
            return readerlist(st);
        if ( nexttok.charAt(0) == '\'' )
            return list("QUOTE", readerb(st));
        return readtoken(nexttok); }
    return null; }

    public static Object readtoken( String tok ) {
        if ( (tok.charAt(0) >= '0' && tok.charAt(0) <= '9') ||
             ((tok.length() > 1) &&
              (tok.charAt(0) == '+' || tok.charAt(0) == '-' ||
               tok.charAt(0) == '.') &&
              (tok.charAt(1) >= '0' && tok.charAt(1) <= '9') ) ||
             ((tok.length() > 2) &&
              (tok.charAt(0) == '+' || tok.charAt(0) == '-') &&
              (tok.charAt(1) == '.') &&
              (tok.charAt(2) >= '0' && tok.charAt(2) <= '9') )  ) {
            boolean dot = false;
            for ( int i = 0; i < tok.length(); i++ )
                if ( tok.charAt(i) == '.' ) dot = true;
            if ( dot )
                return Double.parseDouble(tok);
            else return Integer.parseInt(tok); }
        return tok; }

public static Cons readerlist( StringTokenizer st ) {
    if ( st.hasMoreTokens() ) {
        String nexttok = st.nextToken();
        if ( nexttok.charAt(0) == ' ' ||
             nexttok.charAt(0) == '\t' ||
             nexttok.charAt(0) == '\n' ||
             nexttok.charAt(0) == '\r' ||
             nexttok.charAt(0) == '\f' )
            return readerlist(st);
        if ( nexttok.charAt(0) == ')' )
            return null;
        if ( nexttok.charAt(0) == '(' ) {
            Cons temp = readerlist(st);
            return cons(temp, readerlist(st)); }
        if ( nexttok.charAt(0) == '\'' ) {
            Cons temp = list("QUOTE", readerb(st));
            return cons(temp, readerlist(st)); }
        return cons( readtoken(nexttok),
                     readerlist(st) ); }
    return null; }

    // read a list of strings, producing a list of results.
public static Cons readlist( Cons lst ) {
    if ( lst == null )
        return null;
    return cons( reader( (String) first(lst) ),
                 readlist( rest(lst) ) ); }

    // You can use these association lists if you wish.
    public static Cons engwords = list(list("+", "sum"),
                                       list("-", "difference"),
                                       list("*", "product"),
                                       list("/", "quotient"),
                                       list("expt", "power"));

    public static Cons opprec = list(list("=", 1),
                                     list("+", 5),
                                     list("-", 5),
                                     list("*", 6),
                                     list("/", 6) );

    // ****** your code starts here ******

//maxbt function will output the highest value node within the tree
public static Integer maxbt(Object tree) {
	int currentValue = 0, nextValue = 0;
	if (numberp(tree) && !consp(tree)) {
		return (int) tree;
	}
	else if (tree == null) {
		return 0;
	}
	else {
		currentValue = maxbt(first((Cons) tree));
		nextValue = maxbt(rest((Cons) tree));
		return Math.max(currentValue, nextValue);
	}
}

//vars function will output a new list of only variables
public static Cons vars(Object expr) {
	if (stringp(expr)) {
		return cons((String) expr, null);
	}
	else if ((expr == null) || numberp(expr)) {
		return null;
	}
	else if (consp(expr)) {
		return union(vars(lhs((Cons)expr)), vars(rhs((Cons)expr)));
	}
	else {
		return cons(vars(lhs((Cons) expr)), vars(rhs((Cons) expr)));
	}
}

//occurs function checks if the value occurs within the tree somewhere
public static boolean occurs(Object value, Object tree) {
	boolean result = false;
	if (tree != null && !consp(tree)) { 
		if (tree.equals(value)) {
			return true;
		}
	}
	else if (consp(tree)) {
		result = occurs(value, rest((Cons) tree));
	 	if (result == false) {
		 	result = occurs(value, first((Cons) tree));
	 	}
	}
	else {
		return false;
	}
	return result;
}

//eval function that will output an integer without bindings
public static Integer eval(Object tree) {
	if (numberp(tree)) {
		return (int) tree;
	}
	else {
		if (consp(tree))
		{
			if (op((Cons) tree).equals("+")) {
				return eval(lhs((Cons) tree)) + eval(rhs((Cons) tree));
			}
			else if (op((Cons) tree).equals("-") && rhs((Cons) tree) == null) {
				return eval(lhs((Cons)tree)) * -1;	
			}
			else if (op((Cons) tree).equals("-")) {
				return eval(lhs((Cons) tree)) - eval(rhs((Cons) tree));
			}
			else if (op((Cons) tree).equals("*")) {
				return eval(lhs((Cons) tree)) * eval(rhs((Cons) tree));
			}
			else if (op((Cons) tree).equals("/")) {
				return eval(lhs((Cons) tree)) / eval(rhs((Cons) tree));
			}
			else if (op((Cons) tree).equals("expt")) {
				return pow(eval(lhs((Cons) tree)), eval(rhs((Cons) tree)));
			}
		}
		return 0;
	}
}

//eval function that will evaluate and output an integer based on the bindings
public static Integer eval(Object tree, Cons bindings) {
	if (numberp(tree)) {
		return (int) tree;
	}
	else if (stringp(tree)) {
		return (int) second(assoc(tree, bindings));
	}
	else {
		if (consp(tree))
		{
			if (op((Cons) tree).equals("+")) {
				return eval(lhs((Cons) tree), bindings) + eval(rhs((Cons) tree), bindings);
			}
			else if (op((Cons) tree).equals("-") && rhs((Cons) tree) == null) {
				return eval(lhs((Cons)tree), bindings) * -1;	
			}
			else if (op((Cons) tree).equals("-")) {
				return eval(lhs((Cons) tree), bindings) - eval(rhs((Cons) tree), bindings);
			}
			else if (op((Cons) tree).equals("*")) {
				return eval(lhs((Cons) tree), bindings) * eval(rhs((Cons) tree), bindings);
			}
			else if (op((Cons) tree).equals("/")) {
				return eval(lhs((Cons) tree), bindings) / eval(rhs((Cons) tree), bindings);
			}
			else if (op((Cons) tree).equals("expt")) {
				return pow(eval(lhs((Cons) tree), bindings), eval(rhs((Cons) tree), bindings));
			}
		}
		return 0;
	}
}

//english function that will translate a math expression into english
public static Cons english(Object tree) {
	if (tree == null) {
		return list(tree);
	}
	else if (integerp(tree)) {
		return list((int) tree);
	}
	else if (stringp(tree)) {
		return list((String) tree);
	}
	else {
		if (consp(tree))
		{
			if (op((Cons) tree).equals("+") && rhs((Cons) tree) != null) {
				return append(list("The addition of"), append(english(lhs((Cons) tree)), append(list("and"), english(rhs((Cons) tree)))));
			}
			else if (op((Cons) tree).equals("-") && rhs((Cons) tree) != null) {
				return append(list("the difference of"), append(english(lhs((Cons) tree)), append(list("and"), english(rhs((Cons) tree)))));
			}
			else if (op((Cons) tree).equals("*") && rhs((Cons) tree) != null) {
				return append(list("the product of"), append(english(lhs((Cons) tree)), append(list("and"), english(rhs((Cons) tree)))));
			}
			else if (op((Cons) tree).equals("/") && rhs((Cons) tree) != null) {
				return append(list("the quotient of"), append(english(lhs((Cons) tree)), append(list("and"), english(rhs((Cons) tree)))));
			}
			else if (op((Cons) tree).equals("expt") && rhs((Cons) tree) != null) {
				return append(null, append(list("the power of"), append(english(lhs((Cons) tree)), append(list("and"), english(rhs((Cons) tree))))));
			}
			else if (rhs((Cons) tree) == null) {
				if (op((Cons) tree).equals("+")) {
					return append(list("the addition of"), english(lhs((Cons) tree)));
				}
				else if (op((Cons) tree).equals("-")) {
					return append(list("negative"), english(lhs((Cons) tree)));
				}
				else if (op((Cons) tree).equals("*")) {
					return append(list("the product of"), english(lhs((Cons) tree)));
				}
				else if (op((Cons) tree).equals("/")) {
					return append(list("the quotient of"), english(lhs((Cons) tree)));
				}
			}
		}
		return null;
	}
}

//tojava function to output a math expression based on a tree of operations and variables
public static String tojava(Object tree) {
   return (tojavab(tree, 0) + ";"); 
}

//helper function to javab to translate a tree into math expressions
public static String tojavab(Object tree, int prec) {
	if (tree == null) {
		return "";
	}
	else if (stringp(tree)) {
		return (String) tree;
	}
	else if (consp(tree)) {
		if (op((Cons) tree).equals("=")) {
			return (String) lhs((Cons) tree) + op((Cons) tree) + tojavab(rhs((Cons) tree), 1);
		}
		else if (op((Cons) tree).equals("+") || op((Cons) tree).equals("-")) {
			if (prec > 5) {
				if (op((Cons) tree).equals("-") && rhs((Cons) tree) ==  null) {
					return "(" + op((Cons) tree) + tojavab(lhs((Cons) tree), 6) + tojavab(rhs((Cons) tree), 6) + ")";
				}
				else {
					return "(" + tojavab(lhs((Cons) tree), 6) + op((Cons) tree) + tojavab(rhs((Cons) tree), 6) + ")";
				}
			}
			else {
				return tojavab(lhs((Cons) tree), 6) + op((Cons) tree) + tojavab(rhs((Cons) tree), 6);
			}
		}
		else if (op((Cons) tree).equals("*") || op((Cons) tree).equals("/")) {
			return tojavab(lhs((Cons) tree), 6) + op((Cons) tree) + tojavab(rhs((Cons) tree), 6);
		}
		else if (op((Cons) tree).equals("sin")) {
			return "Math.sin(theta)";
		}
		else if (op((Cons) tree).equals("exp")) {
			return ("e^(" + tojavab(lhs((Cons) tree), 6) + ")");
		}
	}
	else {
		return null;
	}
	return null;
}

    // ****** your code ends here ******

    public static void main( String[] args ) {
        Cons bt1 = (Cons) reader("(((23 77) -3 88) ((((99)) (7))) 15 -1)");
        System.out.println("bt1 = " + bt1.toString());
        System.out.println("maxbt(bt1) = " + maxbt(bt1));

        Cons expr1 = list("=", "f", list("*", "m", "a"));
        System.out.println("expr1 = " + expr1.toString());
        System.out.println("vars(expr1) = " + vars(expr1).toString());

        Cons expr2 = list("=", "f", list("/", list("*", "m",
                                                   list("expt", "v",
                                                        new Integer(2))),
                                         "r"));
        System.out.println("expr2 = " + expr2.toString());
        System.out.println("vars(expr2) = " + vars(expr2).toString());
        
        System.out.println("occurs(m, expr2) = " + occurs("m", expr2));
        System.out.println("occurs(7, expr2) = " + occurs(new Integer(7), expr2));
        Cons expr9 = list( "=", "v",
                                list("*", "v0",
                                     list("exp", list("/", list("-", "t"),
                                                      list("*", "r", "c")))));
        System.out.println("expr9 = " + expr9.toString());
        System.out.println("vars(expr9) = " + vars(expr9).toString());
        System.out.println("occurs(c, expr9) = " + occurs("c", expr9));
        System.out.println("occurs(m, expr9) = " + occurs("m", expr9));

        Cons expr3 = list("+", new Integer(3), list("*", new Integer(5),
                                                         new Integer(7)));
        System.out.println("expr3 = " + expr3.toString());
        System.out.println("eval(expr3) = " + eval(expr3));

        Cons expr4 = list("+", list("-", new Integer(3)),
                               list("expt", list("-", new Integer(7),
                                                      list("/", new Integer(4),
                                                                new Integer(2))),
                                            new Integer(3)));
        System.out.println("expr4 = " + expr4.toString());
        System.out.println("eval(expr4) = " + eval(expr4));

        System.out.println("eval(b) = " + eval("b", list(list("b", 7))));

        Cons expr5 = list("+", new Integer(3), list("*", new Integer(5), "b"));
        System.out.println("expr5 = " + expr5.toString());
        System.out.println("eval(expr5) = " + eval(expr5, list(list("b", 7))));

        Cons expr6 = list("+", list("-", "c"),
                          list("expt", list("-", "b", list("/", "z", "w")),
                                            new Integer(3)));
        Cons alist = list(list("c", 3), list("b", 7), list("z", 4),
                          list("w", 2), list("fred", 5));
        System.out.println("expr6 = " + expr6.toString());
        System.out.println("alist = " + alist.toString());
        System.out.println("eval(expr6) = " + eval(expr6, alist));
        System.out.println("english(expr5) = " + english(expr5).toString());
        System.out.println("english(expr6) = " + english(expr6).toString());
        
        
        System.out.println("tojava(expr1) = " + tojava(expr1).toString());
        Cons expr7 = list("=", "x", list("*", list("+", "a", "b"), "c"));
        System.out.println("expr7 = " + expr7.toString());
        System.out.println("tojava(expr7) = " + tojava(expr7).toString());
        Cons expr8 = list("=", "x", list("*", "r", list("sin", "theta")));
        System.out.println("expr8 = " + expr8.toString());
        System.out.println("tojava(expr8) = " + tojava(expr8).toString());
        System.out.println("expr9 = " + expr9.toString());
        System.out.println("tojava(expr9) = " + tojava(expr9).toString());

        /*
        Cons set3 = list("d", "b", "c", "a");
        
        */
      }
}