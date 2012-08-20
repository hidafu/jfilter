package org.ldap.filter.lib.operators;

import java.util.Collection;

import org.ldap.filter.Filter;

public abstract class FilterImpl implements Filter {

	public <E, T extends Collection<E>> T filter(T collection) {
		try {
			@SuppressWarnings("unchecked")
			T res = (T) collection.getClass().newInstance();
			for (E item : collection) {
				if (match(item))
					res.add(item);
			}
			return res;
		} catch (Exception e) {
			return null;
		}
	}

}