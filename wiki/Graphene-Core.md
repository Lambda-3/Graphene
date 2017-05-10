## Usage of Grapene-Core

The Graphene-Core library offers methods for the complete extraction, as well as for individual and composite steps of the information extraction process.  
If you want to use Graphene inside your application, you first have to create a new instance of the Graphene class:

```java
// load configuration
Config config = ConfigFactory.load();

// create new graphene-instance
Graphene graphene = new Graphene(config);
```

### Resolving co-references

```java
CoreferenceContent cc = graphene.doCoreference("The text.");

// get result as a string
String substituted = cc.getSubstitutedText();
```

### Open Information Extraction (with Simplification)

```java
ExContent ec = graphene.doRelationExtraction("The text.", true);

// ### OUTPUT AS RDFNL #####
// default
String rdfnl1 = graphene.getRepresentation(ec, RepStyle.DEFAULT)
// flat 
String rdfnl2 = graphene.getRepresentation(ec, RepStyle.FLAT)
// extended
String rdfnl3 = graphene.getRepresentation(ec, RepStyle.EXTENDED)

// ### OUTPUT AS PROPER RDF (N-Triples) ###
String rdf = graphene.getRepresentation(ec, RepStyle.N_TRIPLES)
```

[Back to the home page](../README.md)