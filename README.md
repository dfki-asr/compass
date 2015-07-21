COMPASS
=======

COMPASS ("Collaborative Modular Prototyping And Simulation Server") is a framework for developing
web applications with which elements of 3D scenes can be arranged and end enriched with metadata.

It is collaborative in the sense that multiple users are able to see and interact with the same scene at the same time. 
It is modular because it's easy to assemble customized applications with the building blocks of the framework, and that it is easy to extend the metadata model to accommodate a given usage scenario.
Its main usage scenario is prototypical layouting (one might also say simulation) of a scene. Furthermore, we hope that it is easy to build protoypical web applications with COMPASS.


Buzzwords
---------

COMPASS is a JavaEE application, based on EJB 3.1 and CDI, and uses JPA as a storage backend.
Its main data model is an Entity-Component Architecture (ECA), where its Entities are nodes in a Scene Tree, and metadata concerning these Entities are the Components.
COMPASS provides a REST API to access the scene tree, based on JAX-RS.
There is also a JSF-based web application which provides an XML3D-based view on the Scene Tree.


Features
--------

* Scene Management in a two-layer structure ("Projects" and "Scenarios").
* Generic data model not tailored to a specific renderer.
* ...


Repository Structure
--------------------

    compass-maven-resources  - Resources for the build. Checkstyle and PMD configuration.
    compass-model            - The base Data Model classes for COMPASS, mainly JPA entities.
    compass-persistence-unit - Declares a persistence unit out of compass-model
    compass-business-impl    - COMPASS business logic.
    compass-database-initialization - A package to fill an empty database with some defaults.
    compass-rest             - The JAX-RS classes implementing COMPASS' REST services.
    compass-webapp           - The JSF web application, also bundles compass-rest.
    compass-deployment       - Produces an EAR deployment from the above.
    compass-custom-archetype - An example of how to create a compass-based application.


Building
--------

You sould just be able to say

    mvn install

from this directory (with `README` and `pom.xml`). If all goes well you can then either

* start developing your own COMPASS-Based application by instantiating the archetype, see [the archetype README](compass-custom-archetype/README.md).
* start using "stock" COMPASS, see the next section.


Running
-------

Currently, COMPASS is supported on the WildFly 8 JavaEE container. You will need to configure WildFly appropriately to run the web app, however. Details can be found in the [container setup guide](CONTAINER.md).
Once the container is configured, you can deploy the "stock" COMPASS deployment EAR from the `compass-deployment/target/` directory, and start using compass from your containers `compass/` context.


REST Api documentation
----------------------

After deployment, the REST Api documentation is available at `<compass-path>/restapi-doc.html` in the browser.


Contributing
------------

Contributions are very welcome. We're using the well-known [git branching model](http://nvie.com/posts/a-successful-git-branching-model/), supported by [git flow](https://github.com/nvie/gitflow). We recommend reading the [git flow cheatsheet](http://danielkummer.github.io/git-flow-cheatsheet/).
Please use the github workflow (i.e., pull requests) to get your features and/or fixes to our attention.


License
-------

Our code (and documentation) is licensed under the Apache License version 2.0. You should have received an approriate LICENSE file with this distribution. At any rate, you can get the official license text from http://www.apache.org/licenses/LICENSE-2.0.txt
Please also have a look at the NOTICE file, which contains further information and licenses applicable to third party contents included in this distribution.

