//Kevin Nguyen
//kdn433
//Section: 52870, CS314

import java.util.HashMap;

public class Memoizer {
	private Functor funct;
	HashMap<Object, Object> d = new HashMap<>();
	
	public Memoizer(Functor f) {
		funct = f;
	}
	
	public Object call(Object x) {
		Object result = funct.fn(x);
		d.put(x, result);
		return result;
	}
}