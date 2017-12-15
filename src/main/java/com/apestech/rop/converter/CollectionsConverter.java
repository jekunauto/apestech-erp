package com.apestech.rop.converter;

import com.apestech.oap.request.OapConverter;

import java.util.Collection;


/**
 * Created with IntelliJ IDEA. Person: stamen Date: 13-10-25 Time: 上午11:54 To
 * change this template use File | Settings | File Templates.
 * 
 * @param <T>
 */
public class CollectionsConverter<T> implements OapConverter<String, Collection<T>> {

	public String unconvert(Collection<T> target) {
		return null;
	}

	public Class<String> getSourceClass() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	public Class<Collection<T>> getTargetClass() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	public Collection<T> convert(String s) {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}
}
