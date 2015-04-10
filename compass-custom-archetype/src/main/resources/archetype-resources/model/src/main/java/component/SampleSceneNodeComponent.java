#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.component;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.dfki.asr.compass.${artifactId}.SceneNodeComponent;
import de.dfki.asr.compass.${artifactId}.components.annotations.CompassComponent;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Entity (database storage) class for the sample component.
 * Each SceneNodeComponent you develop needs to have its data stored using JPA,
 * which is why it (at the very minimum) needs the @Entity, as well as the
 * @JsonSubTypes annotation. The JAXB annotations are used mainly for loading
 * sample data at startup, see COMPASS' core "database-initialization" module.
 * Sometimes, you need more jackson annotations (@Json...) to customize serialization.
 * If you need classes to support this, e.g. @JsonSerialize(using=Custom.class),
 * these are better suited to live in the REST module. See the COMPASS core
 * REST module on how to perform this (look for "Mixin").
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@CompassComponent(ui = "/components/samplecomponent.xhtml")
@JsonSubTypes.Type(value = SampleSceneNodeComponent.class)
public class SampleSceneNodeComponent extends SceneNodeComponent {

	@XmlElement
	private String sampleData;

	public String getSampleData() {
		return sampleData;
	}

	public void setSampleData(String sampleData) {
		this.sampleData = sampleData;
	}

	/*
	 * When your SceneNodeComponent becomes more complex (e.g. by referencing
	 * other entity classes), you need to take care of some peculiarities.
	 * To support copying of SceneNode hierarchies, COMPASS performs a
	 * serialization/deserialization cycle on the whole hierarchy. To save
	 * that copy as new entities, the @Id columns need to be reset to 0.
	 * Therefore, you should implement clearIdsAfterDeepCopy(), and especially
	 * propagate that call to any child entities.
	 */
	@Override
	public void clearIdsAfterDeepCopy() {
		super.clearIdsAfterDeepCopy();
	}

	/*
	 * On some occasions, COMPASS needs to load a complete hierarchy from the
	 * database. If your SceneNodeComponent has a lazy-loaded Collecion field,
	 * You should implement forceEagerFetch(), in which you should iterate
	 * over you Collection field(s) and also call forceEagerFetch on child
	 * entities.
	 */
	@Override
	public void forceEagerFetch() {
		super.forceEagerFetch();
	}

}
