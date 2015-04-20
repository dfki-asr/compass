Changelog
=========

Version 2.0.0
-------------

### Features

* Semantic versioning: As of release 2.0.0 we comply with the Semantic Versioning 2.0.0 specification. We therefore gained the PATCH identifier. A new major release is required since we broke API by removing ATLAS support from core.
* JSF-Plugins: Adding plugin components into an existing frontend page is now possible without much effort. The archetype provides means for adding Beans, JSF pages and custom scripts, as well as registering them.
* Removed ATLAS from core: Prefab import as core feature is gone and can now be added as a plugin, avoiding confusion for those that wish to use COMPASS without an ATLAS deployment.
* Click extension support: It's possible to create your own click handlers by AOP-ing the new XML3DProducer._createMeshClickHandler function
* Archetype REST service: Archetype projects now provide an example how to add extensions to the REST implementation. The archetype documentation provides guidance for those using this feature.
* Archetype documentation: The documentation for generating and implementing archetype projects was extended to clarify necessary steps when adding new entity classes, especially concerning the necessity of iplementing forceEagerFetch and clearIDsafterDeepCopy.
* Vector conversion from DOM-String: now possible via fromDOMString function, moved from the rather well-hidden XML3DUtils helper class.
* There is now a link back to the project selection screen from the editor view. Just click the "COMPASS" text in the menu bar.

### Bugfixes

* Changing mesh sources of render components only affects model tags which are direct children of the corresponding scene node, keeping geometry of other components intact
* Improved swagger annotations and added fitting PMD rules
* Fixed example URL for the Swagger-generated REST API documentation.
* Fixed some views not properly updating when a scene node was deselected.
* Fixed a typo in the short License header.
