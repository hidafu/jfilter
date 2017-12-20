/**
 * Copyright (C) 2012 University Lille 1, Inria
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301, USA.
 *
 * Contact: romain.rouvoy@univ-lille1.fr
 */
package fr.inria.jfilter.operators;

import static fr.inria.jfilter.resolvers.ValueResolver.instance;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public abstract class ComparableFilter extends FilterImpl {
	protected final String value, operator;
	protected final String[] attribute;

	public ComparableFilter(String[] attribute, String value, String operator) {
		this.attribute = attribute;
		this.value = value;
		this.operator = operator;
	}

	public Collection<Object> resolveValue(Object input,
			Map<String, Object> context) {
		return instance.resolve(input, this.attribute, context);
	}

	protected <T extends Comparable<T>> boolean compare(T left, T right) {
		return convert(left.compareTo(right));
	}

	protected abstract boolean convert(int result);

	public boolean match(Object bean, Map<String, Object> context) {
		final boolean check = check(bean, context);
		if (!check && bean instanceof Collection<?>) {
			Collection<?> col = (Collection<?>) bean;
			for (Object elt : col)
				if (check(elt, context))
					return true;
		}
		return check;
	}

	protected boolean check(Object bean, Map<String, Object> context) {
		Collection<?> values = resolveValue(bean, context);
		if (values == null)
			return false;
		for (Object val : values)
			if (eval(val))
				return true;
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean eval(Object bean) {
		if (bean == null)
			return value == null;
		if (bean instanceof Byte)
			return compare((Byte) bean, new Byte(value));
		if (bean instanceof Integer)
			return compare((Integer) bean, new Integer(value));
		if (bean instanceof Short)
			return compare((Short) bean, new Short(value));
		if (bean instanceof Long)
			return compare((Long) bean, new Long(value));
		if (bean instanceof Float)
			return compare((Float) bean, new Float(value));
		if (bean instanceof Double)
			return compare((Double) bean, new Double(value));
		if (bean instanceof Boolean)
			return compare((Boolean) bean, new Boolean(value));
		if (bean instanceof Comparable)
			return convert(((Comparable) bean).compareTo(value));
		return bean.equals(value);
	}

	public String toString() {
		return "[" + Arrays.toString(attribute) + " " + operator + " " + value
				+ "]";
	}
}
