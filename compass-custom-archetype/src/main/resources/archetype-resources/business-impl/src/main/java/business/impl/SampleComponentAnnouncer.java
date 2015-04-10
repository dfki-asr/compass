#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.business.impl;

import de.dfki.asr.compass.business.api.ComponentAnnouncer;
import de.dfki.asr.compass.business.api.ComponentRegistry;
import javax.ejb.Stateless;
import ${package}.component.SampleSceneNodeComponent;

/**
 * Announces the SceneNodeComponents specific to this COMPASS app.
 * If you implement a custom SceneNodeComponent, you need to tell COMPASS core
 * business logic about it. This class, which is plicked up, instantiated and
 * called from COMPASS' ComponentRegistry takes care of this.
 *
 * Classes implementing the ComponentAnnouncer interface need to be available
 * to CDI at the EAR level. This effectively means you cannot put them anywhere
 * else but an EJB module. You can, of course, have multiple implementations
 * (each registering a different set of SceneNodeComponents), or even several EJB
 * modules in your custom COMPASS app.
 */
@Stateless
public class SampleComponentAnnouncer implements ComponentAnnouncer {

	@Override
	public void announceComponents(ComponentRegistry registry) {
		registry.registerComponent(SampleSceneNodeComponent.class);
	}
	
}
