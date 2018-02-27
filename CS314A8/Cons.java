//Kevin Nguyen
//kdn433
//Section: 52870, CS314

/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 30 Oct 08; 07 Apr 09
 *          10 Apr 09; 02 Aug 10; 06 Aug 10
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

    public boolean equals(Object other) { return equal(this,other); }

    // tree equality
public static boolean equal(Object tree, Object other) {
    if ( tree == other ) return true;
    if ( consp(tree) )
        return ( consp(other) &&
                 equal(first((Cons) tree), first((Cons) other)) &&
                 equal(rest((Cons) tree), rest((Cons) other)) );
    return eql(tree, other); }

    // simple equality test
public static boolean eql(Object tree, Object other) {
    return ( (tree == other) ||
             ( (tree != null) && (other != null) &&
               tree.equals(other) ) ); }

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

    // iterative version of length
public static int length (Cons lst) {
  int n = 0;
  while ( lst != null ) {
    n++;
    lst = rest(lst); }
  return n; }

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

public static boolean subsetp (Cons x, Cons y) {
    return ( (x == null) ? true
             : ( ( member(first(x), y) != null ) &&
                 subsetp(rest(x), y) ) ); }

public static boolean setEqual (Cons x, Cons y) {
    return ( subsetp(x, y) && subsetp(y, x) ); }

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
    public static int pow (int x, int n) {
        if ( n <= 0 ) return 1;
        if ( (n & 1) == 0 )
            return square( pow(x, n / 2) );
        else return x * pow(x, n - 1); }

public static Object copy_tree(Object tree) {
    if ( consp(tree) )
        return cons(copy_tree(first((Cons) tree)),
                    (Cons) copy_tree(rest((Cons) tree)));
    return tree; }

public static Object subst(Object gnew, String old, Object tree) {
    if ( consp(tree) )
        return cons(subst(gnew, old, first((Cons) tree)),
                    (Cons) subst(gnew, old, rest((Cons) tree)));
    return (old.equals(tree)) ? gnew : tree; }

public static Object sublis(Cons alist, Object tree) {
    if ( consp(tree) )
        return cons(sublis(alist, first((Cons) tree)),
                    (Cons) sublis(alist, rest((Cons) tree)));
    if ( tree == null ) return null;
    Cons pair = assoc(tree, alist);
    return ( pair == null ) ? tree : second(pair); }

public static Cons dummysub = list(list("t", "t"));

public static Cons match(Object pattern, Object input) {
    return matchb(pattern, input, dummysub); }

public static Cons matchb(Object pattern, Object input, Cons bindings) {
    if ( bindings == null ) return null;
    if ( consp(pattern) )
        if ( consp(input) )
            return matchb( rest( (Cons) pattern),
                           rest( (Cons) input),
                           matchb( first( (Cons) pattern),
                                   first( (Cons) input),
                                   bindings) );
        else return null;
    if ( varp(pattern) ) {
        Cons binding = assoc(pattern, bindings);
        if ( binding != null )
            if ( equal(input, second(binding)) )
                return bindings;
            else return null;
        else return cons(list(pattern, input), bindings); }
    if ( eql(pattern, input) )
        return bindings;
    return null; }

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

public static Object transform(Cons patpair, Cons input) {
    Cons bindings = match(first(patpair), input);
    if ( bindings == null ) return null;
    return sublis(bindings, second(patpair)); }

    // Transform a list of arguments.  If no change, returns original.
public static Cons transformlst(Cons allpats, Cons input) {
    if ( input == null ) return null;
    Cons restt = transformlst(allpats, rest(input));
    Object thist = transformr(allpats, first(input));
    if ( thist == first(input) && restt == rest(input) )
        return input;
    return cons(thist, restt); }

    // Transform a single item.  If no change, returns original.
public static Object transformr(Cons allpats, Object input) {
    //    System.out.println("transformr:  " + input.toString());
    if ( consp(input) ) {
        Cons listt = transformlst(allpats, (Cons) input);
        //       System.out.println("   lst =  " + listt.toString());
        return transformrb(allpats, allpats,
                           transformlst(allpats, listt)); }
    Object res = transformrb(allpats, allpats, input);
    //    System.out.println("   result =  " + res.toString());
    return res; }

    // Transform a single item.  If no change, returns original.
public static Object transformrb(Cons pats, Cons allpats, Object input) {
    if ( pats == null ) return input;
    if ( input == null ) return null;
    Cons bindings = match(first((Cons)first(pats)), input);
    if ( bindings == null ) return transformrb(rest(pats), allpats, input);
    return sublis(bindings, second((Cons)first(pats))); }

    // Transform a single item repeatedly, until fixpoint (no change).
public static Object transformfp(Cons allpats, Object input) {
    //    System.out.println("transformfp: " + input.toString());
    Object trans = transformr(allpats, input);
    if ( trans == input ) return input;
    //    System.out.println("    result = " + trans.toString());
    return transformfp(allpats, trans); }          // potential loop!

public static boolean varp(Object x) {
    return ( stringp(x) &&
             ( ((String) x).charAt(0) == '?' ) ); }

    // Note: this list will handle most, but not all, cases.
    // The binary operators - and / have special cases.
public static Cons opposites = 
    list( list( "+", "-"), list( "-", "+"), list( "*", "/"),
          list( "/", "*"), list( "sqrt", "expt"), list( "expt", "sqrt"),
          list( "log", "exp"), list( "exp", "log") );

public static String opposite(String op) {
    Cons pair = assoc(op, opposites);
    if ( pair != null ) return (String) second(pair);
    return "error"; }

public static void javaprint(Object item, int tabs) {
    if ( item == null ) System.out.print("null");
    else if ( consp(item) ) javaprintlist((Cons) item, tabs);
    else if ( stringp(item) )
        if ( item.equals("zlparen") ) System.out.print("(");
        else if ( item.equals("zrparen") ) System.out.print(")");
        else if ( item.equals("zspace") ) System.out.print(" ");
        else if ( item.equals("znothing") ) ;
        else if ( item.equals("ztab") ) System.out.print("\t");
        else if ( item.equals("zreturn") ) System.out.println();
        else System.out.print((String)item);
    else System.out.print(item.toString()); }

public static void javaprintlist(Cons lst, int tabs) {
    if ( lst != null ) {
        if ( stringp(first(lst)) )
            if ( ((String)first(lst)).equals("ztab" ) ) tabs++;
            else if ( ((String)first(lst)).equals("zuntab" ) ) tabs--;
            else if ( ((String)first(lst)).equals("zreturn" ) ) {
                System.out.println();
                for (int i = 0; i < tabs; i++) System.out.print("\t"); }
            else javaprint(first(lst), tabs);
        else javaprint(first(lst), tabs);
        javaprintlist(rest(lst), tabs); } }
             
    // ****** your code starts here ******

    // your program from the previous assignments

//solve function that will attempt to solve for a string variable, v
public static Cons solve(Cons e, String v) {
	if ((e == null) || ((!v.equals(rhs(e))) && (!consp(rhs(e))))) {
		return null;
	}
	else if (v.equals(lhs(e))) {
		return e;
	}
	else if (v.equals(rhs(e)))
		return cons(first(e), list(rhs(e), lhs(e)));
	else {
		if (consp(rhs(e))) {
			Object operator = op((Cons)rhs(e)), newOp = second(assoc(operator, opposites));
			if (operator.equals("*") || operator.equals("+")) {
				if (solve(list("=", list(newOp, lhs(e), lhs((Cons) rhs(e))), rhs((Cons) rhs(e))), v) != null) {
					return solve( list("=", list(newOp, lhs(e), lhs((Cons) rhs(e))), rhs((Cons) rhs(e))), v);
				}
				else {
					return solve(list("=", list(newOp, lhs(e), rhs((Cons) rhs(e))), lhs((Cons)rhs(e))), v);
				}
			}
			else if (operator.equals ("/") || operator.equals("-")) {
				if (rhs((Cons) rhs(e)) == null) {
					return solve(list("=", list("-", lhs(e)), lhs((Cons) rhs(e))), v);
				}
				else {
					if (solve(list("=", list(operator, lhs((Cons)rhs(e)), lhs(e)),rhs((Cons) rhs(e))), v) != null) {
						return solve(list("=", list(operator, lhs((Cons)rhs(e)), lhs(e)),rhs((Cons) rhs(e))), v);
					}
					else {
						return solve(list("=", list(newOp, lhs(e), rhs((Cons) rhs(e))), lhs((Cons) rhs(e))), v);
					}
				}
			}
			else if(operator.equals("sqrt")) {
				return solve(list("=", list(newOp, lhs(e), 2), lhs((Cons)rhs(e))), v);
			}
			else if (operator.equals("expt") || operator.equals("exp")|| operator.equals("log")) {
	      		return solve(list("=", list(newOp, lhs(e)), lhs((Cons)rhs(e))), v);
			}
		}
	}
	return null;
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

//eval function that will evaluate and output an integer based on the bindings
public static Double eval(Object tree, Cons bindings) {
	if (numberp(tree)) {
		return ((Number) tree).doubleValue();	
	}
	else if (stringp(tree)) {
		return (Double) second(assoc(tree, bindings));
	}
	else {
		if (consp(tree)) {
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
				return Math.pow(eval(lhs((Cons) tree), bindings), eval(rhs((Cons) tree), bindings));
			}
			else if (op((Cons) tree).equals("exp")) {
				return Math.exp(eval(lhs((Cons) tree), bindings));
			}
			else if (op((Cons) tree).equals("sqrt")) {
				return Math.sqrt(eval(lhs((Cons) tree), bindings));
			}
			else if (op((Cons) tree).equals("log")) {
				return Math.log(eval(lhs((Cons) tree), bindings));
			}
		}
		return 0.0;
	}
}
    // new for this assignment

//solve equations that will solve a series of equations to get to a desired variable
public static Double solveqns(Cons eqns, Cons vals, String v) {
	boolean solved = false;
	Cons copyValues = vals, copyEquations = eqns;
	if (assoc(v, copyValues) != null) {
		return (Double) second(assoc(v, copyValues));
	}
	else if (assoc(v, copyValues) == null && copyEquations == null) {
		return null;
	}
	else {
		String P = findUnknown((Cons) first(copyEquations), copyValues, null);		
		if (!consp(rhs((Cons) first(copyEquations))) && !consp(lhs((Cons) first(copyEquations))) && numberp(rhs((Cons) first(copyEquations)))) {
			copyValues = append( list(list(lhs((Cons) op(copyEquations)), rhs((Cons) op(copyEquations)))), copyValues );
			solved = true;
		}
		else if (stringp(rhs((Cons) first(copyEquations))) && stringp(lhs((Cons) first(copyEquations)))) {
			copyValues = append( list(list(P, second(assoc(rhs((Cons) first(copyEquations)), copyValues)))), copyValues);
			solved = true;
		}
		
		if (!solved) {
			double result = solveHelper(copyEquations, P, copyValues);
			copyValues = append(list(list(P, result)), copyValues);
		}
		return solveqns(rest(copyEquations), copyValues, v);
	}
}

//solve helper method that will determine if equation is solvable and solves for the value
public static Double solveHelper(Cons equations, String var, Cons values) {
	Cons copyValues = values, copyValuesTemp = null, copyEquations = equations, varlist = list(var), completeEquation = null;
	Boolean solvable = true;
	while (copyValues != null) {
		varlist = cons(op((Cons) first(copyValues) ), varlist);
		copyValues = rest(copyValues);
	}
	copyValues = values;
	copyValuesTemp = append(vars(lhs((Cons) first(copyEquations))),vars(rhs((Cons) first(copyEquations))));
	while (copyValuesTemp != null && solvable) {
		if (member(first(copyValuesTemp), varlist) == null) {
			solvable = false;
		}
		copyValuesTemp = rest(copyValuesTemp);
	}
	completeEquation = ((Cons) rhs(solve((Cons) first(copyEquations), var)));
	return eval(completeEquation, copyValues);
}

//find unknown helper method that will get the unknown string-variable in the equation
public static String findUnknown(Cons eqns, Cons vals, String variableUnknown) {
	if (eqns == null) {
		return variableUnknown;
	}
	else {
		if (consp(lhs(eqns))) {
			variableUnknown = findUnknown((Cons) lhs(eqns), vals, variableUnknown);
		}
		else {
			if (stringp(lhs(eqns)) && assoc(lhs(eqns), vals) == null) {
				variableUnknown = (String) lhs(eqns);
			}
		}
		if (consp(rhs(eqns))) {
			variableUnknown = findUnknown((Cons) rhs(eqns), vals, variableUnknown); 
		}
		else {
			if (stringp(rhs(eqns)) && assoc(rhs(eqns), vals) == null) {
				variableUnknown = (String) rhs(eqns);
			}
		}	
		return variableUnknown;
	}
}

    // Question 2 of Assignment 8
    // Code substitutions to be done in 'binaryfn' below
    // ((?function ...) (?combine ...) (?baseanswer ...))
    //    (defun ?function (tree)
    //       (if (consp tree)
    //           (?combine (?function (first tree))
    //                     (?function (rest tree)))
    //           ?baseanswer))

//substitutions method that will determine a pattern to substitute current with new pattern
public static Cons substitutions = readlist(list(
		"( (?function addnums) (?combine +) (?baseanswer (if (numberp tree) tree 0)))",
	    "( (?function countstrings) (?combine +) (?baseanswer (if (stringp tree) 1 0)))",
	    "( (?function copytree) (?combine cons) (?baseanswer  (if (stringp tree) tree null) ))",
	    "( (?function mintree) (?combine min) (?baseanswer (if (numberp tree) tree 999999)))",
	    "( (?function conses) (?combine add1) (?baseanswer 0)))",
	    "( (?function addnums) (?combine +) (?baseanswer (if (numberp tree) tree 0)))"
	    ));

//opt pats function that will utilize all possible known patterns
public static Cons optpats = readlist(list(
	    "( (+ ?x 0)   ?x)",
	    "( (+ 0 ?x)   ?x)",
	    "( (expt ?x 1) ?x)",
	    // add more
	    "( (* ?x 1)   ?x)",
	    "( (- (- ?x ?y))   (- ?y ?x))",
	    "( (* ?x 0)  0)",
	    "( (- 2 1)   1)",
	    "( (- 3 1)   2)",
	    "( (- (- ?x))   ?x)",
	    "( (+ 0 ?x)   ?x)",    
	    "( (+ ?x 0)   ?x)",
	    "( (expt ?x 1) ?x)",     
	    "( (- ?x 0)   ?x)",  
	    "( (* 0 ?x)   0)",   
	    "( (/ ?x 1)   ?x)",
	    "( (* 1 ?x)   ?x)",     
	    "( (/ (* ?u ?x) (* ?u ?y))  (/ ?x ?y)"
	    ));

//derivative pattern that will get a pattern based on derivatives
public static Cons derivpats = readlist(list(
		"( (deriv ?x ?x)   1)",
	    "( (deriv (+ ?u ?v) ?x)   (+ (deriv ?u ?x) (deriv ?v ?x)))",      
	    "( (deriv (- ?u ?v) ?x)   (- (deriv ?u ?x) (deriv ?v ?x)))",
	    "( (deriv (- ?v) ?x)   (-(deriv ?v x?)))",
	    "( (deriv (* ?u ?v) ?x)   (+ (* ?u (deriv ?v ?x)) (* ?v (deriv ?u ?x))))",
	    "( (deriv (/ ?u ?v) ?x)   (/ (- (* ?u (deriv ?v ?x)) (* ?v (deriv ?u ?x))) (exp ?v 2)))",
	    "( (deriv (expt ?u ?c) ?x)   (* (* ?c (expt ?u (- ?c 1))) (deriv ?u ?x)))",
	    "( (deriv (sqrt ?u) ?x)   (/ (* .5 (deriv ?u ?x)) (sqrt ?u)))",
	    "( (deriv (log ?u) ?x)   (/ (deriv ?u ?x) ?u)",
	    "( (deriv (exp ?u) ?x)   (* (expt ?u) (deriv ?u ?x)))",
	    "( (deriv (sin ?u) ?x)   (* (cos ?u) (deriv ?u ?x)))",
	    "( (deriv (cos ?u) ?x)   (* (-(sin ?u)) (deriv ?u ?x)))",
	    "( (deriv (tan ?u) ?x)   (* (+ (* (tan ?u) 2) 1) (deriv ?u ?x)))",
	    "( (deriv ?y ?x)   0)"   // this must be last!
	    ));

//java structure pats will get a java structure pattern from the data
public static Cons javarestructpats = readlist(list(
	    "( (return (if ?test ?then)) (if ?test (return ?then)) )",
	    "( (return (if ?test ?then ?else)) (if ?test (return ?then) (return  ?else)) )",
	    "( (defun ?fun ?args ?code) (zdefun ?fun (arglist ?args) (progn (return ?code))) )",
	    "( (setq ?x (+ ?x 1)) (incf ?x) )"
	    ));

//javapats will read through the java pattern
public static Cons javapats = readlist( list(
        "( (if ?test ?then) (if zspace zlparen zspace ?test zspace zrparen ztab zreturn ?then))",
        "( (if ?test ?then ?test2) (if zspace zlparen ?test zrparen ztab zreturn ?then zuntab zreturn else zspace ?test2) )",
        "( (zdefun ?fun ?args ?code)   (public zspace static zspace Object zspace ?fun zspace ?args zreturn ?code zreturn) )",
        "( (min ?x ?y)   (Math.min zlparen ?x , zspace ?y zrparen))",
        "( (cons ?x ?y)   (znothing cons zlparen ?x , zspace ?y zrparen))",
        "( (add1 ?x ?y)   (znothing add1 zlparen ?x , zspace ?y zrparen))",
        "( (arglist (?x))   (zlparen Object zspace ?x zrparen))",
        "( (progn ?x)   ({ ztab zreturn ?x zuntab zreturn }) )",
        "( (setq ?x ?y)   ( ?x zspace = zspace ?y ; zreturn ) )",
        "( (first ?x)   (znothing first zlparen zlparen Cons zrparen ?x zrparen ) )",
        "( (rest ?x)   (rest zlparen zlparen Cons zrparen ?x zrparen)",
        "( (incf ?x)   (x + + ;) )",
        "( (expt ?r ?c)   (Math.pow zlparen ?r , zspace ?c zrparen) )",
        "( (or ?x ?y)   (?x zspace || zspace ?y) )",
        "( (and ?x ?y)   (?x zspace && zspace ?y) )",
        "( (<= ?x ?y)   (zlparen ?x zspace <= zspace ?y zrparen) )",
        "( (< ?x ?y)   (zlparen ?x zspace < zspace ?y zrparen) )",
        "( (>= ?x ?y)   (zlparen ?x zspace >= zspace ?y zrparen) )",
        "( (> ?x ?y)   (zlparen ?x zspace > zspace ?y zrparen) )",
        "( (not ?x)   (! ?x) )",
        "( (+ ?x ?c)   (?x zspace + zspace ?c) )",
        "( (- ?x ?c)   (?x zspace - zspace ?c) )",
        "( (* ?x ?c)   (?x zspace * zspace ?c) )",
        "( (/ ?x ?c)   (?x zspace / zspace ?c) )",
        "( (% ?x ?c)   (?x zspace % zspace ?c) )",
        "( (return ?x)   (return zspace ?x ;) )",
        "( (?fun ?x)   (znothing ?fun zlparen ?x zrparen) )" // must be last
	    ));

    // ****** your code ends here ******

    public static void main( String[] args ) {

        Cons eqnsbat =
            readlist(
              list("(= loss_voltage (* current internal_resistance))",
                   "(= loss_power (* internal_resistance (expt current 2)))",
                   "(= terminal_voltage (- voltage loss_voltage))",
                   "(= power (* terminal_voltage current))",
                   "(= work (* charge terminal_voltage))" ) );
        
        System.out.println("battery = " +
                           solveqns(eqnsbat, (Cons)
             reader("((current 0.3)(internal_resistance 4.0)(voltage 12.0))"),
                                    "terminal_voltage"));
        Cons eqnscirc =
            readlist(
              list("(= acceleration (/ (expt velocity 2) radius))",
                   "(= force        (* mass acceleration))",
                   "(= kinetic_energy   (* (/ mass 2) (expt velocity 2)))",
                   "(= moment_of_inertia (* mass (expt radius 2)))",
                   "(= omega (/ velocity radius))",
                   "(= angular_momentum (* omega moment_of_inertia))" ));
        
        System.out.println("circular = " +
                           solveqns(eqnscirc, (Cons)
                                    reader("((radius 4.0)(mass 2.0)(velocity 3.0))"),
                                    "angular_momentum"));
        Cons eqnslens =
            readlist(
              list("(= focal_length (/ radius 2))",
                   "(= (/ 1 focal_length) (+ (/ 1 image_distance) (/ 1 subject_distance)))",
                   "(= magnification (- (/ image_distance subject_distance)))",
                   "(= image_height (* magnification subject_height))" ));

        
        System.out.println("magnification = " +
                           solveqns(eqnslens, (Cons)
              reader("((subject_distance 6.0)(focal_length 9.0))"),
                                    "magnification"));
 
        Cons eqnslift =
            readlist(
              list("(= gravity     9.80665 )",
                   "(= weight      (* gravity mass))",
                   "(= force       weight)",
                   "(= work        (* force height))",
                   "(= speed       (/ height time))",
                   "(= power       (* force speed))",
                   "(= power       (/ work time)) ))" ));
        
        System.out.println("power = " +
                           solveqns(eqnslift, (Cons)
             reader("((weight 700.0)(height 8.0)(time 10.0)))"),
                                    "power"));
        Cons binaryfn = (Cons) reader(
          "(defun ?function (tree) (if (consp tree) (?combine (?function (first tree)) (?function (rest tree))) ?baseanswer))");

        for ( Cons ptr = substitutions; ptr != null; ptr = rest(ptr) ) {
            Object trans = sublis((Cons) first(ptr), binaryfn);
            System.out.println("sublis:  " + trans.toString()); }

        Cons opttests = readlist( list(
          "(+ 0 foo)",
          "(* fum 1)",
          "(- (- y))",
          "(- (- x y))",
          "(+ foo (* fum 0))"
          ));

        for ( Cons ptr = opttests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("opt:  " + ((Cons)first(ptr)).toString());
            Object trans = transformfp(optpats, first(ptr));
            System.out.println("      " + trans.toString()); }


        Cons derivtests = readlist( list(
          "(deriv x x)",
          "(deriv 3 x)",
          "(deriv z x)",
          "(deriv (+ x 3) x)",
          "(deriv (* x 3) x)",
          "(deriv (* 5 x) x)",
          "(deriv (sin x) x)",
          "(deriv (sin (* 2 x)) x)",
          "(deriv (+ (expt x 2) (+ (* 3 x) 6)) x)",
          "(deriv (sqrt (+ (expt x 2) 2)) x)",
          "(deriv (log (expt (+ 1 x) 3)) x)"

          ));

        for ( Cons ptr = derivtests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("deriv:  " + ((Cons)first(ptr)).toString());
            Object trans = transformfp(derivpats, first(ptr));
            System.out.println("  der:  " + trans.toString());
            Object transopt = transformfp(optpats, trans);
            System.out.println("  opt:  " + transopt.toString()); }

        Cons javatests = readlist( list(
          "(* fum 7)",
          "(setq x y)",
          "(setq x (+ x 1))",
          "(setq area (* pi (expt r 2)))",
          "(if (> x 7) (setq y x))",
          "(if (or (> x 7) (< y 3)) (setq y x))",
          "(if (and (> x 7) (not (< y 3))) (setq y x))",
          "(defun factorial (n) (if (<= n 1) 1 (* n (factorial (- n 1)))))"
          ));

        for ( Cons ptr = javatests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("java:  " + ((Cons)first(ptr)).toString());
            Cons restr = (Cons) transformfp(javarestructpats, first(ptr));
            System.out.println("       " + restr.toString());
            Cons trans = (Cons) transformfp(javapats, restr);
            System.out.println("       " + trans.toString());
            javaprintlist(trans, 0);
            System.out.println(); }


        for ( Cons ptr = substitutions; ptr != null; ptr = rest(ptr) ) {
            Object ltrans = sublis((Cons) first(ptr), binaryfn);
            System.out.println("sublis:  " + ltrans.toString());
            Cons restr = (Cons) transformfp(javarestructpats, ltrans);
            System.out.println("       " + restr.toString());
            Cons trans = (Cons) transformfp(javapats, restr);
            System.out.println("       " + trans.toString());
            javaprintlist(trans, 0);
            System.out.println(); }
      }
}