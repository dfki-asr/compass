Custom COMPASS Archetype
========================

This package builds a maven archetype which can be used to create a custom,
COMPASS-based application.


Setting up the the archtype
---------------------------

You should first install the archetype to your local maven repository.
This should be a simple matter of running:

    mvn install

Quite possibly, the archetype has been installed to your local repository already, if
you've previously built and installed the COMPASS parent project.

Afterwards, you can instantiate the archetype by running:

    mvn archetype:generate
    -DarchetypeGroupId=de.dfki.asr.compass
    -DarchetypeArtifactId=compass-custom-archetype

If you use an IDE to develop, you can use its inbuilt archetype instantiation mechanism.
You will probably need to specify the above group and/or artifact ID.


Anatomy of a COMPASS-based project
----------------------------------

To keep things modular and nicely separated, your COMPASS-based application should
follow the pattern of the archetype (that is very similar to COMPASS' own structure),
which consists of these main modules:

* __Data Model:__
  These are your Java (JPA, to be precise) classes that hold the data in your application.
  Most likely, you will have additional `SceneNodeComponent`s in there, which extend
  COMPASS' Scene model.
* __Persistence Unit:__
  Your custom application defines its own JPA persistence unit, which ties together
  the base COMPASS data model and your customised data model from the previous item.
  The base COMPASS business logic, which is added to your application in the deployment
  module will then pick up and work on the combined data model.
* __Business Logic Implementation:__
  If you choose to add complex operations on your data model, this is the place to put it.
  Classes in this Package should probably be EJBs, to benefit from Container Managed
  Transactions. At the very least, you will need to register any custom `SceneNodeComponent`
  that you created in your Data Model with COMPASS' base business logic.
  An example how to do this is contained in the Archetype.
* __Web Applicaton Overlay:__
  Custom user-visible interfaces to your Data Model and Business Logic go here.
  The Overlay module is configured such that it will take COMPASS' base web interface,
  and add your contents, or even replace parts of the base web interface with your
  variant.
* __Deployment:__
  This module takes all of the above, and adds COMPASS' base business logic. All of this
  tied together will give you an enterprise application archive (EAR), which you can
  deploy to a container configured for COMPASS (see the documentation there).
  With that, you should have a customised web application that extends COMPASS with
  your features!


Implementing your functionality
-------------------------------

This section will briefly go over the minimum requirements to add a custom `SceneNodeComponent`. 
We anticipate that this will be the most frequent operation users of the archetype will do.
Of course, a custom COMPASS-based application can do much more than just add another kind of
data storage to the scene tree. For more advanced features, please have a look at the
implementation in "core" COMPASS. As the archetype is very closely modelled after it, similar
principles and mechanisms will apply. After all, it is "just" a usual JavaEE project.

### Data Model

Modify and rename the `SampleSceneNodeComponent` class to suit your needs. Observe
especially the contents of the `@CompassComponent` annotation, as that will be used by
the base COMPASS web layer to provide an interface to your component.

Entity classes should not be very "smart", in the sense that they support complex semantic
operations. These operations should be implemented in the Business Logic module. The main
concern of classes in the Data Model module is database storage and serialisation. Therefore
you should take care to get all of the annotations right (`javax.xml.bind.annotation.*`
and `com.fasterxml.jackson.annotation.*`).

If your IDE warns you that "The project does not contain a persistence unit.", please ignore
the  warning. The persistence unit only gets added to the deployment later on.

### Persistence Unit

You don't really need to modify this, unless you rename your Data Model artifact. The final
name of the Data Model module JAR file is referenced in the `persistence.xml` file.
If you edit your `persistence.xml` file, remember to leave the reference to the
`compass-model` JAR intact.

### Business Logic

Here, you need to add any `SceneNodeComponent`s from your Data Model you want to use
to the `SampleComponentAnnouncer.announceComponents()` method. The Announcer will
be picked up by COMPASS' base business logic via CDI. The `ComponentRegistry` looks for
all implementations of the `ComponentAnnouncer` interface.

### Web Applicaton Overlay

The `samplecomponent.xhtml` contains a predefined Facelet UI composition which will be
displayed in COMPASS' web UI properties view. Modify this to suit your needs, i.e. to be able
to edit all of your entity class' data fields.

If you decide to rename the file, don't forget to update the `@CompassComponent` annotation
on your Entity class.

If nothing is displayed in the Properties View after adding your component to a scene node
this usually indicates a problem with the JSF in your component's .xhtml file. Your JavaEE
container's log should provide further information on what went wrong.

The `sampleplugin.xhtml` and `SamplePluginAnnouncer` demonstrate the JSF plugin functionality
of COMPASS. It is a more lightweight way of extending COMPASS' JSF views, instead of
replacing the original file in the Overlay module.
For each slot, you can add an arbitrary number of Facelets to be rendered in the slot.
The `body` slot shown in the example is especially interesting, as you can add custom
javascript resources there. A complete listing of slots and their positions can be found in
the `SamplePluginAnnouncer` class.

### Deployment

The Deployment module is configured mainly via its `pom.xml` file. There, you can add
further modules to the EAR (such as an external EJB or WAR module you want to integrate),
or configure the root folder under which the web application(s) will be visible within the
container.  Please refer to maven's EAR plugin documentation for further details.


Building & Installing
---------------------

Once your implementation is finished, go to the root of your archetype instance and run

    mvn package

If all goes well, you can now pick up a finished EAR file from your `deployment/target`
folder, which you can deploy to your conatiner.
