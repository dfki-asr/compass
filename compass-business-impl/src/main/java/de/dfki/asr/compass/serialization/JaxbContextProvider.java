/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.serialization;

import de.dfki.asr.compass.business.api.ComponentRegistry;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

@Provider
@Produces("application/xml")
public class JaxbContextProvider implements ContextResolver<JAXBContext> {

	@Inject
	private ComponentRegistry componentRegistry;

	@Override
	public JAXBContext getContext(final Class<?> type) {
		Set<Class<?>> annotated = new HashSet<>();
		annotated.addAll(componentRegistry.getAvailableSceneNodeComponents());
		annotated.add(type);
		try {
			Class[] classesToBeBound = annotated.toArray((Class[]) Array.newInstance(Class.class, 0));
			return JAXBContextFactory.createContext(classesToBeBound, null);
		} catch (JAXBException ex) {
			Logger.getLogger(JaxbContextProvider.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
}
