/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.databaseinitialization;

import de.dfki.asr.compass.serialization.JaxbContextProvider;
import de.dfki.asr.compass.model.AbstractCompassEntity;
import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.SceneNode;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.jboss.logging.Logger;

@Startup
@javax.ejb.Singleton
public class InitialDatabaseSetup {
	@PersistenceContext
	private EntityManager persistenceContext;

	@Inject
	private Logger log;

	@Inject
	private JaxbContextProvider contextProvider;

	private InputStream importFileStream;

	@PostConstruct
	public void onInitialize() {
		if (shouldLoadDatabase()) {
			log.info("Loading database from Archive");
			loadDatabaseFromArchive();
		}
	}

	private boolean shouldLoadDatabase() {
		return !persistenceContextContainsSceneNodes();
	}

	private boolean persistenceContextContainsSceneNodes() {
		return countSceneNodes() > 0;
	}

	private long countSceneNodes() {
		CriteriaQuery<Long> query = createCountQuery(SceneNode.class);
		return persistenceContext.createQuery(query).getSingleResult();
	}

	private CriteriaQuery<Long> createCountQuery(final Class<? extends AbstractCompassEntity> entityClass) {
		CriteriaBuilder builder = persistenceContext.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		query.select(builder.count(query.from(entityClass)));
		return query;
	}

	private void loadDatabaseFromArchive() {
		List<Project> projectsFromArchive = loadArchiveContents();
		log.infov("Loaded {0} projects from Archive.",projectsFromArchive.size());
		for (Project project: projectsFromArchive) {
			storeProjectToDatabase(project);
		}
	}

	private List<Project> loadArchiveContents() {
		if (importFileExists()) {
			return readImportFile();
		} else {
			log.info("No Archive file found.");
			return new ArrayList<>();
		}
	}

	private boolean importFileExists() {
		importFileStream = openResourceFile("import.xml");
		return importFileStream != null;
	}

	private List<Project> readImportFile() {
		return parseXMLFile(importFileStream);
	}

	private InputStream openResourceFile(final String filename) {
		return InitialDatabaseSetup.class.getResourceAsStream("/" + filename);
	}

	private List<Project> parseXMLFile(final InputStream xmlDataFile) {
		XMLRootProjectList list = unmarshalStreamIntoClass(xmlDataFile, XMLRootProjectList.class);
		if (list == null) {
			return new ArrayList<>();
		} else {
			return list.projects;
		}
	}

	private <T> T unmarshalStreamIntoClass(final InputStream xmlDataFile, final Class<? extends T> theClass) {
		try {
			Unmarshaller unmarshaller = createUnmarshallerForClass(theClass);
			return theClass.cast(unmarshaller.unmarshal(xmlDataFile));
		} catch (JAXBException | ClassCastException e) {
			log.errorv(e,"Cannot unmarshall Stream into {0}",theClass.getName());
			return null;
		}
	}

	private void storeProjectToDatabase(final Project project) {
		try {
			persistenceContext.persist(project);
			log.infov("Saved project {0}",project.getName());
		} catch (PersistenceException | IllegalArgumentException ex) {
			log.errorv(ex,"Not saving Project",project);
		}
	}

	private Unmarshaller createUnmarshallerForClass(final Class<?> theClass) throws JAXBException {
		JAXBContext context = contextProvider.getContext(theClass);
		return context.createUnmarshaller();
	}
}

