# Graphene

## Requirements

* Java 8
* Maven 3.3.9
* Ant
* Docker version 1.13+
* docker-compose version 1.10+

## Compiling/Packaging
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

### Graphene
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
Clone the repository

	git clone --depth 1 https://github.com/Lambda-3/CoreferenceResolutionPython.git /tmp/coreference
	cd /tmp/coreference

Then, adjust the CoreNLP access URL to reflect the later docker-compose network in the `Resolver.py` file:

	# OLD:
	_nlp = StanfordCoreNLP('http://localhost:9000')
	# NEW
	_nlp = StanfordCoreNLP('http://corenlp:9000')

Then build the image with

	docker build -t "coreference:v1" -f Dockerfile .

### Docker-Compose

Then, you can build and start the composed images:
	
	docker-compose up