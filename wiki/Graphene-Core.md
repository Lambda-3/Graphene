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

### Simplification

```java
SimplificationContent sc = graphene.doSimplification("The text.", true);
```

### Open Information Extraction (without Simplification)

```java
ExtractionContent ec = graphene.doGraphExtraction("The text.", true);
```

### Open Information Extraction (with Simplification)

```java
ExSimplificationContent esc = graphene.doSimplificationAndGraphExtraction("The text.", true);

// output as RDF.NL
String rdfnl = graphene.getRDFOutputStr(esc);
```

[Back to the home page](../README.md)