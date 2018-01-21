![Graphene](wiki/images/graphene_logo.png)

# Graphene: Knowledge Graph / Open Relation Extraction

## Motivation

_Graphene_ is an information extraction pipeline which extracts _Knowledge Graphs_ from texts (n-ary relations and rhetorical structures extracted from complex factoid discourse). Given a sentence or a text, Graphene outputs a semantic representation of the text which is a labeled directed graph (a knowledge graph). This knowledge graph can be later used for addressing different AI tasks, such as building Question Answering systems, extracting structured data from text, supporting semantic inference, among other tasks. Differently from existing open relation extraction tools, which focus on the main relation expressed in a sentence, Graphene aims at maximizing the extraction of contextual relations. For example: 

`Trump withdrew his sponsorship after the second Tour
de Trump in 1990 because his business ventures were
experiencing financial woes.`

![Graphene-Extraction](wiki/images/Graphene-Extraction.jpg)

In order to capture all the contextual information, Graphene performs the following steps:
* Resolves co-references.
* Transforms complex sentences (for example, containing subordinations, coordinations, appositive phrases, etc), into simple independent sentences (one clause per sentence).
* Identifies rhetorical relations between those sentences
* Extract binary relations (`subject`,  `predicate` and  `object`) from each sentence.
* Merge all the extracted relations into a relation graph (knowledge graph).

Graphene’s extracted graphs are represented by our RDFNL format, an simple format that facilitates the representation of complex contextual relations in a way that balances machine representation with human legibility. A description of the RDFNL format can be found [here](wiki/RDFNL-Format.md).
In order to increase further processability of the extracted relations, Graphene can materialize its relations into a proper RDF graph serialized under the N-Triples specification of the RDF standard. A description of the RDF format can be found [here](wiki/RDF-Format.md).
Alternatively, developers can use the direct output class of the API, which is serializable and deserializable as a JSON object.

## Example Extractions

### Sentence Extraction

`Although the Treasury will announce details of the November refunding on Monday, the funding will be delayed if Congress and President Bush fail to increase the Treasury's borrowing capacity.`

The serialized class: [JSON](wiki/files/example.json)   
The RDFNL format:

```
# Although the Treasury will announce details of the November refunding on Monday , the funding will be delayed if Congress and President Bush fail to increase the Treasury 's borrowing capacity .

bacf06771e0f4fc5a8e68c30fc77c9c4    0    the Treasury    will announce    details of the November refunding
    S:TEMPORAL    on Monday .
    L:CONTRAST    948eeebd73564adab7dee5c6f177b3b9

948eeebd73564adab7dee5c6f177b3b9    0    the funding    will be delayed        
    L:CONDITION 006a71e51295440fab7a8e8c697d2ba6
    L:CONDITION e4d86228cff443b7a8e9f6d8a5c5987b
    L:CONTRAST    bacf06771e0f4fc5a8e68c30fc77c9c4

006a71e51295440fab7a8e8c697d2ba6    1    Congress    fail    to increase the Treasury 's borrowing capacity
    L:LIST    e4d86228cff443b7a8e9f6d8a5c5987b

e4d86228cff443b7a8e9f6d8a5c5987b    1    president Bush    fail    to increase the Treasury 's borrowing capacity
    L:LIST    006a71e51295440fab7a8e8c697d2ba6
```

The RDF N-Triples format: [NT](wiki/files/example.nt)

### Full text extraction of the [Barack Obama Wikipedia Page](https://en.wikipedia.org/wiki/Barack_Obama) (2017-11-06):

The serialized class: [JSON](wiki/files/Barack_Obama_2017_11_06.json)   
The RDFNL format: [RDFNL](wiki/files/Barack_Obama_2017_11_06.rdfnl)   
The RDF N-Triples format: [RDF](wiki/files/Barack_Obama_2017_11_06.nt)   

## Contributors (alphabetical order)
- Andre Freitas
- Bernhard Bermeitinger
- Christina Niklaus
- Leonardo Souza
- Matthias Cetto
- Siegfried Handschuh

## Requirements

* Java 8 (OpenJDK or Oracle)
* Maven 3.3.9
* Docker version 17.03+
* docker-compose version 1.12+

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
	wget https://github.com/Lambda-3/DiscourseSimplification/archive/v8.0.0.tar.gz -O DiscourseSimplification.tar.gz
	tar xfa DiscourseSimplification.tar.gz
	cd DiscourseSimplification
	mvn -DskipTests install

### More dependencies (requires [docker](https://www.docker.com/))
Prior to running `Graphene`, two additional dependencies must be met:

* [CoreNLP](https://github.com/Lambda-3/CoreNLP.git)
* [PyCobalt](https://github.com/Lambda-3/PyCobalt.git)

Both are provided with the docker images:
* [CoreNLP](https://hub.docker.com/r/lambdacube/corenlp/)
* [PyCobalt](https://hub.docker.com/r/lambdacube/pycobalt/)


### Setup of Graphene
Graphene-Core is build with

	mvn clean package -DskipTests

If you want the server part, you have to specify that profile:

    mvn -P server clean package -DskipTests

If you want the command line part, you have to specify that profile:

    mvn -P cli clean package -DskipTests
   
To build both interfaces, you can specify both profiles:

    mvn -P cli -P server clean package -DskipTests

### Docker-Compose

Create a new config file and adjust your settings:

	touch conf/graphene.conf

Then, you can build and start the composed images:
	
	docker-compose up

## Usage

### Graphene-Core
Graphene comes with a Java API which is described [here](wiki/Graphene-Core.md).

In order to use the Graphene API within your own Java application, you can import it as a Maven dependency.
For this task, install Graphene-Core into your local repository:

    mvn clean install -DskipTests

and add the following lines to your project's `pom.xml` file:

```
<dependency>
    <groupId>org.lambda3.graphene</groupId>
    <artifactId>graphene-core</artifactId>
    <version>3.0.0-SNAPSHOT</version>
</dependency>
```

You must have a PyCobalt instance running, it is provided in the `docker-compose-core.yml`. Start it with `docker-compose -f docker-compose-core.yml`. You must then change the config file:
```
graphene {
	coreference.url = "http://localhost:5128/resolve"
}
```

### Graphene-Sever
For simplified access, we wrapped the Graphene-Core library inside a REST-like web-service.
```bash
docker-compose up
```
The usage of the Graphene-Server is described [here](wiki/Graphene-Server.md).


## Graphene-CLI
Another way of accessing our service is provided by a command-line interface, which is described [here](wiki/Graphene-CLI.md).
Like the Graphene-Core setup, you must have a PyCobalt instance running before.
