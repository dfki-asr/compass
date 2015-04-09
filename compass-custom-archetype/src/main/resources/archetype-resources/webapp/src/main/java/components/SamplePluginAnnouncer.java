#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.components;

import de.dfki.asr.compass.web.plugins.PluginViewRegistry;
import de.dfki.asr.compass.web.plugins.api.PluginAnnouncer;

/**
 * Announces the views that plug into slots in the core JSF pages.
 * This class, which is automatically found by and subsequently called from the
 * PluginViewRegistry should only take care of adding views to slots. Of course,
 * you can register multiple views per slot, if you so desire.
 * You can also have other classes implementing the PluginAnnouncer interface,
 * if you want to keep things nicely separated.
 * Also have a look at the SampleComponentAnnouncer class in the business-impl
 * module, which works similarly. Unfortunately these need to be separate classes,
 * due to classloading in an EAR environment.
 */
public class SamplePluginAnnouncer implements PluginAnnouncer {
	@Override
	public void announcePlugin(PluginViewRegistry registry) {
		/*
		 * Currenly available plugin slots are:
		 * - "body" (inside the <body> tag)
		 * - "menuBar" (at the right end of the top bar)
		 * - "prefabButtons" (at the top of the prefab list)
		 * - "renderGeometryEditor" (in the RenderGeometry component editor panel)
		 * If you need slots in other positions, send a patch or pull request!
		 */
		registry.registerPluginView("body", "/plugins/sampleplugin.xhtml");
	}
}
