[[images/graphene_logo.png]]

# Graphene: Knowledge Graph / Open Relation Extraction

## Motivation

_Graphene_ is an information extraction pipeline which extracts _Knowledge Graphs_ from texts (n-ary relations and rhetorical structures extracted from complex factoid discourse). Given a sentence or a text, Graphene outputs a semantic representation of the text which is a labeled directed graph (a knowledge graph). This knowledge graph can be later used for addressing different AI tasks, such as building Question Answering systems, extracting structured data from text, supporting semantic inference, among other tasks. Differently from existing open relation extraction tools, which focus on the main relation expressed in a sentence, Graphene aims at maximizing the extraction of contextual relations. For example: 

`In mid-1981 , Obama traveled to Indonesia to visit his mother and half-sister Maya , and visited the families of college friends in Pakistan and India for three weeks .`

[[images/Graphene-Extraction.jpg]]

In order to capture all the contextual information, Graphene performs the following steps:
* Resolves co-references.
* Identifies rhetorical relations between sentences and clauses.
* Transforms complex sentences (for example, containing subordinations, coordinations, appositive phrases, etc), into simple independent sentences (one clause per sentence).
* Extract binary relations from each sentence.
* Merge all the extracted relations into a relation graph (knowledge graph).

Graphene’s extracted graphs are represented using the RDF.NL format, an extension of the RDF format which facilitates the representation of complex contextual relations in a way that balances machine representation with human legibility. A short description of the RDF.NL format can be found [here](wiki/RDF.NL-Format).
Alternative to the RDF.NL representation, developers can use the direct output class of the API, which is serializable and deserializable as a JSON object.

## Example Extractions

### Sentence Extraction

`The café arrived in Paris in the 17th century , when the beverage was first brought from Turkey , and by the 18th century Parisian cafés were centres of the city 's political and cultural life .`

The serialized class: [JSON](wiki/files/example2.json)  
The corresponding RDF.NL format:

```
# original sentence: 'The café arrived in Paris in the 17th century , when the beverage was first brought from Turkey , and by the 18th century Parisian cafés were centres of the city 's political and cultural life .'

## core sentence: 'the café arrived in Paris in the 17th century .'
DIS_TYPE:		CORE-8874b563-12ce-4e67-9f59-3b4d7f0882c1		DISCOURSE_TYPE		DISCOURSE_CORE
CORE_EXT:		CORE-8874b563-12ce-4e67-9f59-3b4d7f0882c1		the café		arrived		in Paris
RHET_REL:		CORE-8874b563-12ce-4e67-9f59-3b4d7f0882c1		JOINT_LIST		CORE-c0123203-3227-4b8f-9744-9fc1b64f4607
RHET_REL:		CORE-8874b563-12ce-4e67-9f59-3b4d7f0882c1		BACKGROUND		CORE-852b01fd-6a17-42c7-bd1a-30385b8c2acb

## core sentence: 'Parisian cafés were centres of the city 's political and cultural life .'
DIS_TYPE:		CORE-c0123203-3227-4b8f-9744-9fc1b64f4607		DISCOURSE_TYPE		DISCOURSE_CORE
CORE_EXT:		CORE-c0123203-3227-4b8f-9744-9fc1b64f4607		Parisian cafés		were		centres of the city 's political and cultural life
RHET_REL:		CORE-c0123203-3227-4b8f-9744-9fc1b64f4607		JOINT_LIST		CORE-8874b563-12ce-4e67-9f59-3b4d7f0882c1
### context sentence: 'This was by the 18th century .'
CONT_EXT:		CONTEXT-343e1c2a-f6be-45ca-a9f2-55b0d9eb2d8a		This		was		by the 18th century
RHET_REL:		CORE-c0123203-3227-4b8f-9744-9fc1b64f4607		UNKNOWN_SENT_SIM		CONTEXT-343e1c2a-f6be-45ca-a9f2-55b0d9eb2d8a

## core sentence: 'the beverage was first brought .'
DIS_TYPE:		CORE-852b01fd-6a17-42c7-bd1a-30385b8c2acb		DISCOURSE_TYPE		DISCOURSE_CONTEXT
CORE_EXT:		CORE-852b01fd-6a17-42c7-bd1a-30385b8c2acb		the beverage		was brought		null
### context sentence: 'This was from Turkey .'
CONT_EXT:		CONTEXT-2aec8b1c-9d48-478c-bfdc-ab5031378137		This		was		from Turkey
RHET_REL:		CORE-852b01fd-6a17-42c7-bd1a-30385b8c2acb		LOCATION		CONTEXT-2aec8b1c-9d48-478c-bfdc-ab5031378137
```

### Full text extraction of the [Barack Obama Wikipedia Page](https://en.wikipedia.org/wiki/Barack_Obama) (2017-02-23):

The serialized class: [JSON](wiki/files/Barack_Obama_2017-02-23.json)  
The corresponding RDF.NL format: [RDF.NL](wiki/files/Barack_Obama_2017-02-23.RDF.NL)

## Usage of the Grapene-Core library

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

## Graphene-Sever
For simplified access, we wrapped the Graphene-Core library inside a web-service.
The usage of the Graphene-Server is described [here](wiki/Graphene-Server).

## Graphene-CLI
Another way of accessing our service is provided by a command-line interface, which is described [here](wiki/Graphene-CLI).
