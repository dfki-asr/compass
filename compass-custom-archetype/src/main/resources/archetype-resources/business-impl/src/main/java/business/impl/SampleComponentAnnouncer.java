#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.business.impl;

import de.dfki.asr.compass.business.api.ComponentAnnouncer;
import de.dfki.asr.compass.business.api.ComponentRegistry;
import javax.ejb.Stateless;
import ${package}.component.SampleSceneNodeComponent;

@Stateless
public class SampleComponentAnnouncer implements ComponentAnnouncer {

	@Override
	public void announceComponents(ComponentRegistry registry) {
		registry.registerComponent(SampleSceneNodeComponent.class);
	}
	
}
