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

}
