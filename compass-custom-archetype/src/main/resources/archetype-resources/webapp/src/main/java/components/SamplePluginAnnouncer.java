#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.components;

import de.dfki.asr.compass.web.plugins.PluginViewRegistry;
import de.dfki.asr.compass.web.plugins.api.PluginAnnouncer;

public class SamplePluginAnnouncer implements PluginAnnouncer {
	@Override
	public void announcePlugin(PluginViewRegistry registry) {
		registry.registerPluginView("body", "/plugins/sampleplugin.xhtml");
	}
}
