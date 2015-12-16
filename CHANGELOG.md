Changelog
=========

Version 2.2.0
-------------

### Features

* Examine Controller: More extensive CAD metaphor support (multiple mouse buttons, modifier keys) and zoom via field of view.

### Bugfixes

* Container Documentation: Fixed a typo

Version 2.1.1
-------------

### Features

* Quickstart Documentation: The CONTAINER.md file now contains instructions on setting up WildFly for a COMPASS-only setup.

### Bugfixes

* Firefox fixes: The JSF UI now runs on Mozilla Firefox 42 (the current version at the time of release). 
* Object duplication on Prefab creation: The freshly created prefab is not erroneously added to the scene any more.
* Version clarification: The CONTAINER.md file now states that WildFly 8.2.0 is the supported platform.
* Changelog: The changelog now also contains the changes for the 2.1.0 release. Very meta.

Version 2.1.0
-------------

### Features

* Improved Project and Scenario name checks via REST: Checks for uniqueness of Scenario and Project names are now reasonably enforced via the REST interface, similar to what the JSF UI does. Checks don't fall through to the database layer, leading to cleander error reporting.
* Change inspect controller to CAD-like navigation: The inspect controller is now mainly controlled via the middle mouse button. Holding the button (or scroll wheel) moves the camera around the current center point. Scrolling in and out chnages the distance to the center point.
* Improvements to the continuous integration process: Travis now checks, whether the "stock" COMPASS EAR file can be successfully deployed to a WildFly 8.2.0 instance.
* Added unit tests for model classes.

Version 2.0.1
-------------

### Bugfixes

* Fixed deserialization of SceneNode and Image references from JSON. Makes the REST interface usable again.

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
