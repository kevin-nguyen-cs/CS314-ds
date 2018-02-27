//Kevin Nguyen
//kdn433
//Section: 52870, CS314


// libtest.java      GSN    03 Oct 08; 21 Feb 12; 26 Dec 13
// 

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

public class libtest {

    // ****** your code starts here ******

//sumlist will add up a linked list of nodes using the iterator
public static Integer sumlist(LinkedList<Integer> lst) {
	Integer result = 0;
	Iterator<Integer> iterate = lst.iterator();
	while (iterate.hasNext()) {
		result = result + iterate.next();
	}
	return result;
}

//sum arrlist to add up values of an array list
public static Integer sumarrlist(ArrayList<Integer> lst) {
	Integer result = 0, item = 0;
	Iterator<Integer> iterate = lst.iterator();
	while (iterate.hasNext()) {
		item = iterate.next();
		result = result + item;
	}
	return result;
}

//Subset function that will produce a new list of elements that satifies predicate p
public static LinkedList<Object> subset(Predicate p, LinkedList<Object> lst) {
	Object item = null;
	LinkedList<Object> result = new LinkedList<Object>();
	Iterator<Object> iterate = lst.iterator();
	while(iterate.hasNext()) {
		item = iterate.next();
		if (p.pred(item)) {
			result.add(item);
		}
	}
	return result;
}

//Dsub set that will destructively modify the original lst, no copies are made
public static LinkedList<Object> dsubset (Predicate p, LinkedList<Object> lst) {
	Object item = null;
	Iterator<Object> iterate = lst.iterator();
	while (iterate.hasNext()) {
		item = iterate.next();
		if (!p.pred(item)) {
			iterate.remove();
		}
	}
	return lst;
}

//Some function that will produce a new list on the first element that satisfies predicate p
public static Object some(Predicate p, LinkedList<Object> lst) {
	Object item = null;
	Iterator<Object> iterate = lst.iterator();
	while(iterate.hasNext()) {
		item = iterate.next();
		if (p.pred(item)) {
			break;
		}
		else {
			item = null;
		}
	}
	return item;
}

//Mapcar function that will produce a new list after applying Functor, f
public static LinkedList<Object> mapcar(Functor f, LinkedList<Object> lst) {
	Object item = null;
	Iterator<Object> iterate = lst.iterator();	
	LinkedList<Object> result = new LinkedList<Object>();
	while (iterate.hasNext()) {
		item = iterate.next();
		result.add(f.fn(item));
	}
	return result;
}

//Merge function that will merge two lists together via mergesort technique
public static LinkedList<Object> merge(LinkedList<Object> lsta, LinkedList<Object> lstb) {
	Object itemA = null, itemB = null;
	Boolean canIterateA = true, canIterateB = true;
	LinkedList<Object> result = new LinkedList<Object>();
	Iterator<Object> iterateA = lsta.iterator(); Iterator<Object> iterateB = lstb.iterator();
	while (iterateA.hasNext() || iterateB.hasNext()) {
		if ((canIterateA) && iterateA.hasNext() != false) {
			itemA = iterateA.next(); 
		}
		if ((canIterateB) && iterateB.hasNext() != false) {
			itemB = iterateB.next();
		}
		
		if ((!iterateA.hasNext()) && itemB != null) {
			if (itemA != null && ((Comparable) itemB).compareTo(itemA) > 0) {
				result.add(itemA); itemA = null; 
			}
			result.add(itemB);
			canIterateA = false; canIterateB = true;
		}
		else if ((!iterateB.hasNext()) && itemA != null) {
			if (itemB != null && ((Comparable) itemA).compareTo(itemB) > 0) {
				result.add(itemB); itemB = null;
			}
			result.add(itemA); 
			canIterateB = false; canIterateA = true;
		}
		else {
			if (((Comparable) itemA).compareTo(itemB) <= 0) {
				result.add(itemA); canIterateB = false; canIterateA = true;
			}
			else {
				result.add(itemB); canIterateA = false; canIterateB = true;
			}
		}
	}
	return result;
}

//Recursive function sort method to quickly sort sublists
public static LinkedList<Object> sort(LinkedList<Object> lstSub) {
	int counter = 0;
	Object pivot = null, item = null;
	LinkedList<Object> lstA = new LinkedList<Object>(), lstB = new LinkedList<Object>();
	if (lstSub.size() <= 2) {
		if (lstSub.size() == 2) {
			if (((int) lstSub.get(0)) > ((int) lstSub.get(1))) {
				lstSub.addFirst(lstSub.get(1));
				lstSub.removeLast();
			}
		}		
		return lstSub;
	}
	else {
		pivot = lstSub.get(((lstSub.size()-1)/2));
		Iterator<Object> iterate = lstSub.iterator();
		while (iterate.hasNext()) {
			item = iterate.next(); 
			if (item != pivot) {
				if ((int) item < (int) pivot) {
					lstA.add(item);
				}
				else {
					lstB.add(item);
				}
			}
			else if (item == pivot) {
				++counter;
			}
			if (!iterate.hasNext() || counter > 1) {
				lstA.add((Object) pivot); 
				counter = 0;
			}			
		}
		lstA = sort(lstA);
		lstB = sort(lstB);
	}
	 return merge(lstA, lstB);
}

//Intersection will produce a list of the same combination between two sets
public static LinkedList<Object> intersection(LinkedList<Object> lsta, LinkedList<Object> lstb) {
	Object item = null, item2 = null, resultItem = null;
	LinkedList<Object> tempLst = new LinkedList<Object>(), tempLst2 = new LinkedList<Object>(), result = new LinkedList<Object>();
	tempLst = sort(merge(lsta, lstb)); tempLst2 = tempLst;
	Iterator<Object> iterate = tempLst.iterator(), iterate2 = tempLst2.iterator();
	item2 = iterate2.next();
	while (iterate2.hasNext()) {
		item = iterate.next(); item2 = iterate2.next();
		if (item.equals(item2) && (!item.equals(resultItem))) {			
			result.add(item); 
			resultItem = item;
		}
	}	
	return result;
}

//Reverse outputs a new list that is the reverse of the original list
public static LinkedList<Object> reverse(LinkedList<Object> lst) {
	Object item = null;
	LinkedList<Object> reversedResult = new LinkedList<Object>();
	Iterator<Object> iterate = lst.iterator();
	while (iterate.hasNext()) {
		item = iterate.next();
		reversedResult.addFirst(item);
	}
	return reversedResult;
}

    // ****** your code ends here ******

    public static void main(String args[]) {
        LinkedList<Integer> lst = new LinkedList<Integer>();
        lst.add(new Integer(3));
        lst.add(new Integer(17));
        lst.add(new Integer(2));
        lst.add(new Integer(5));
        System.out.println("lst = " + lst);
        System.out.println("sum = " + sumlist(lst));
        
        ArrayList<Integer> lstb = new ArrayList<Integer>();
        lstb.add(new Integer(3));
        lstb.add(new Integer(17));
        lstb.add(new Integer(2));
        lstb.add(new Integer(5));
        System.out.println("lstb = " + lstb);
        System.out.println("sum = " + sumarrlist(lstb));

        final Predicate myp = new Predicate()
            { public boolean pred (Object x)
                { return ( (Integer) x > 3); }};

        LinkedList<Object> lstc = new LinkedList<Object>();
        lstc.add(new Integer(3));
        lstc.add(new Integer(17));
        lstc.add(new Integer(2));
        lstc.add(new Integer(5));
        System.out.println("lstc = " + lstc);
        System.out.println("subset = " + subset(myp, lstc));

        System.out.println("lstc     = " + lstc);
        System.out.println("dsubset  = " + dsubset(myp, lstc));
        System.out.println("now lstc = " + lstc);

        LinkedList<Object> lstd = new LinkedList<Object>();
        lstd.add(new Integer(3));
        lstd.add(new Integer(17));
        lstd.add(new Integer(2));
        lstd.add(new Integer(5));
        System.out.println("lstd = " + lstd);
        System.out.println("some = " + some(myp, lstd));

        final Functor myf = new Functor()
            { public Integer fn (Object x)
                { return new Integer( (Integer) x + 2); }};

        System.out.println("mapcar = " + mapcar(myf, lstd));

        LinkedList<Object> lste = new LinkedList<Object>();
        lste.add(new Integer(1));
        lste.add(new Integer(3));
        lste.add(new Integer(5));
        lste.add(new Integer(6));
        lste.add(new Integer(9));
        lste.add(new Integer(11));
        lste.add(new Integer(23));
        System.out.println("lste = " + lste);
        LinkedList<Object> lstf = new LinkedList<Object>();
        lstf.add(new Integer(2));
        lstf.add(new Integer(3));
        lstf.add(new Integer(6));
        lstf.add(new Integer(7));
        System.out.println("lstf = " + lstf);
        System.out.println("merge = " + merge(lste, lstf));

        lste = new LinkedList<Object>();
        lste.add(new Integer(1));
        lste.add(new Integer(3));
        lste.add(new Integer(5));
        lste.add(new Integer(7));
        System.out.println("lste = " + lste);
        lstf = new LinkedList<Object>();
        lstf.add(new Integer(2));
        lstf.add(new Integer(3));
        lstf.add(new Integer(6));
        lstf.add(new Integer(6));
        lstf.add(new Integer(7));
        lstf.add(new Integer(10));
        lstf.add(new Integer(12));
        lstf.add(new Integer(17));
        System.out.println("lstf = " + lstf);
        System.out.println("merge = " + merge(lste, lstf));

        LinkedList<Object> lstg = new LinkedList<Object>();
        lstg.add(new Integer(39));
        lstg.add(new Integer(84));
        lstg.add(new Integer(5));
        lstg.add(new Integer(59));
        lstg.add(new Integer(86));
        lstg.add(new Integer(17));
        System.out.println("lstg = " + lstg);

        System.out.println("intersection(lstd, lstg) = "
                         + intersection(lstd, lstg));

        System.out.println("reverse lste = " + reverse(lste));

        System.out.println("sort(lstg) = " + sort(lstg));
   }
}