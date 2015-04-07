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
@CompassComponent(ui = "/WEB-INF/components/samplecomponent.xhtml")
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

}
