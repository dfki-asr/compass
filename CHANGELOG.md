Changelog Version 2.0
======================

Features
---------

* JSF-Plugins: Adding plugin components using a frontend page of their own is now possible without much effort. The archetype provides means for adding Beans, JSF pages and custom scripts, as well as registering them.
* Removed ATLAS from core: Prefab import as core feature is gone and can now be added as a plugin, making it easier and more transparent to use Compass without a default asset server.
* Click extension support: It's possible to create your own click handlers by aop-ing the new createMeshClichHandler function
* Archetype REST service: Archetype projects can now use their own REST implementation. The archetype documentation provides guidance for those using this awesome feature.
* Archetype documentation: The documentation for generating and implementing archetype projects was extended to clarify necessary steps when adding new entity classes, especially concerning the necessity of iplementing forceEagerFetch and clearIDsafterDeepCopy
* Vector conversion from DOM-String: now possible via fromDOMString function

Bugfixes
--------

* Changing mesh sources of render components only affects model tags which are direct children of the corresponding scene node, keeping geometry of other components intact
* Improved swagger annotations and added fitting PMD rules

