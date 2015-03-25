/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.components;

import de.dfki.asr.compass.model.components.annotations.CompassComponent;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class JSFFunctions {

	private JSFFunctions(){
		//forbidden default ctor
	}

	public static String truncateString(String input, int maxLength) {
		String output = input;
		if (input.length() > maxLength) {
			output = input.substring(0, maxLength - 3).concat("...");
		}
		return output;
	}

	public static Object findComponent(Collection list, String classToFind) {
		if (list == null) {
			return null;
		}
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object next = it.next();
			String actualClass = next.getClass().getSimpleName();
			if (classToFind.equals(actualClass)) {
				return next;
			}
		}
		return null;
	}

	public static String getComponentUISrc(Object component) {
		Class clazz;
		// The argument may already be the Class, or it may be an instance of the component
		if (component instanceof java.lang.Class) {
			clazz = (Class) component;
		} else {
			clazz = component.getClass();
		}
		Annotation anno = clazz.getAnnotation(CompassComponent.class);
		if (!(anno instanceof CompassComponent)) {
			return "/WEB-INF/components/unknown.xhtml";
		}
		return ((CompassComponent) anno).ui();
	}

	public static String prependAndJoin(String prefix, List<String> elements) {
		StringBuilder b = new StringBuilder();
		for (String string : elements) {
			b.append(prefix);
			b.append(string);
			b.append(' ');
		}
		return b.toString();
	}
}
