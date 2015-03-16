/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.util;

import de.dfki.asr.compass.business.exception.CompassRuntimeException;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Perform CDI injection on any class.
 * Usage: In constructor of object to perform injection on, say:
 * {@code new CdiInjector<ObjectInstance>().inject(this);}
 * Based on: https://github.com/openknowledge/openknowledge-cdi-extensions/blob/14e316913d12bb7c5f4003df1d1100a76f5dad19/openknowledge-cdi-inject/src/main/java/de/openknowledge/cdi/inject/CdiInjector.java
 * @param <T> Type of Object into which to perform injection.
 */
@SuppressWarnings("PMD.CommentSize")
public class CDIInjector<T> {

	public void inject(final T object) {
		BeanManager beanManager = getBeanManager();
		CreationalContext<T> creationalContext = beanManager.createCreationalContext(null);
		AnnotatedType<T> annotatedType = beanManager.createAnnotatedType((Class<T>) object.getClass());
		InjectionTarget<T> injectionTarget = beanManager.createInjectionTarget(annotatedType);
		injectionTarget.inject(object, creationalContext);
		injectionTarget.postConstruct(object);
	}

	public void dispose(final T object) {
		BeanManager beanManager = getBeanManager();
		AnnotatedType<T> annotatedType = beanManager.createAnnotatedType((Class<T>) object.getClass());
		InjectionTarget<T> injectionTarget = beanManager.createInjectionTarget(annotatedType);
		injectionTarget.preDestroy(object);
		injectionTarget.dispose(object);
	}

		protected BeanManager getBeanManager() throws RuntimeException {
		try {
			return (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
		} catch (NamingException ex) {
			throw new CompassRuntimeException("Cannot resolve BeanManager from JNDI. This should not happen.", ex);
		}
	}

}
