![Graphene](wiki/images/graphene_logo.png)

# Graphene: Knowledge Graph / Open Relation Extraction

## Motivation

_Graphene_ is an information extraction pipeline which extracts _Knowledge Graphs_ from texts (n-ary relations and rhetorical structures extracted from complex factoid discourse). Given a sentence or a text, Graphene outputs a semantic representation of the text which is a labeled directed graph (a knowledge graph). This knowledge graph can be later used for addressing different AI tasks, such as building Question Answering systems, extracting structured data from text, supporting semantic inference, among other tasks. Differently from existing open relation extraction tools, which focus on the main relation expressed in a sentence, Graphene aims at maximizing the extraction of contextual relations. For example: 

`In mid-1981 , Obama traveled to Indonesia to visit his mother and half-sister Maya , and visited the families of college friends in Pakistan and India for three weeks .`

![Graphene-Extraction](wiki/images/Graphene-Extraction.jpg)

In order to capture all the contextual information, Graphene performs the following steps:
* Resolves co-references.
* Identifies rhetorical relations between sentences and clauses.
* Transforms complex sentences (for example, containing subordinations, coordinations, appositive phrases, etc), into simple independent sentences (one clause per sentence).
* Extract binary relations from each sentence.
* Merge all the extracted relations into a relation graph (knowledge graph).

Graphene’s extracted graphs are represented using the RDF.NL format, an extension of the RDF format which facilitates the representation of complex contextual relations in a way that balances machine representation with human legibility. A short description of the RDF.NL format can be found [here](wiki/RDF.NL-Format.md).
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

## Requirements

* Java 8
* Maven 3.3.9
* Ant
* Docker version 1.13+
* docker-compose version 1.10+

## Setup
Compiling and packaging requires two additional packages:

### Sentence Simplification
	cd /tmp
	wget https://github.com/Lambda-3/SentenceSimplification/archive/v5.0.0.tar.gz -O SentenceSimplification.tar.gz
	tar xfa SentenceSimplification.tar.gz
	cd SentenceSimplification
	mvn -DskipTests install

### Discourse Simplification
	cd /tmp
	wget https://github.com/Lambda-3/DiscourseSimplification/archive/v5.0.0.tar.gz -O DiscourseSimplification.tar.gz
	tar xfa DiscourseSimplification.tar.gz
	cd DiscourseSimplification
	mvn -DskipTests install

### Setup of Graphene-Core
Graphene-Core is build with

	mvn clean package -DskipTests

If you want the server part, you have to specify that profile:

    mvn -P server clean package -DskipTests

If you want the command line part, you have to specify that profile:

    mvn -P cli clean package -DskipTests
    
To build both interfaces, you can specify both profiles:

    mvn -P cli -P server clean package -DskipTests

## Running as a service

Prior to running `Graphene`, two additional dependencies must be met:
	
* [CoreNLP](https://github.com/stanfordnlp/CoreNLP.git)
* [CoreferenceResolution](https://github.com/Lambda-3/CoreferenceResolutionPython.git)
	
### Installing CoreNLP
First, clone the repository and copy the `Dockerfile-corenlp` to the CoreNLP directory

	git clone --depth 1 https://github.com/stanfordnlp/CoreNLP.git /tmp/CoreNLP
	cp Dockerfile-corenlp /tmp/CoreNLP
	cd /tmp/CoreNLP

Build the jar file

	ant
	jar -cf ../stanford-corenlp.jar edu

Download the required models:
	
	wget http://nlp.stanford.edu/software/stanford-corenlp-models-current.jar
    wget http://nlp.stanford.edu/software/stanford-english-corenlp-models-current.jar
    wget http://nlp.stanford.edu/software/stanford-english-kbp-corenlp-models-current.jar

Then, build the image 

	docker build -t "corenlp:latest" -f Dockerfile-corenlp .

### Installing coreference
Install `PyCobalt`as a docker image

	wget https://github.com/Lambda-3/PyCobalt/archive/v1.1.0-beta.3.tar.gz -O /tmp/PyCobalt.tar.gz
	cd /tmp
	tar xfva PyCobalt.tar.gz
	cd PyCobalt-1.1.0-beta.3
	docker build -t "lambda3/pycobalt:v1.1.0-beta.3" .
	

### Docker-Compose

Then, you can build and start the composed images:
	
	docker-compose up

## Usage

### Graphene-Core
Graphene comes with a java API which is described [here](wiki/Graphene-Core.md). 

### Graphene-Sever
For simplified access, we wrapped the Graphene-Core library inside a web-service.
The usage of the Graphene-Server is described [here](wiki/Graphene-Server.md).

## Graphene-CLI
Another way of accessing our service is provided by a command-line interface, which is described [here](wiki/Graphene-CLI.md).
