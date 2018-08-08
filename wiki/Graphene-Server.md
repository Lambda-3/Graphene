## Usage of the Graphene-Server
The Graphene-Server captures the same functionalities as the Graphene-Core API.
The wrapped Core API functions can be executed by HTTP GET requests.

### Resolving co-references
Coreference Resolution will resolve coreferences in the given input text.
The Coreference Resolution service accepts the following endpoints with parameters:

#### Endpoint: /coreference/text
Returns the JSON-serialized versions of the `CoreferenceContent` class or a textual representation depending on the value of the `Accept` header in the POST request:
* `Accept: text/plain`: returns a textual representation
* `Accept: application/json`: returns JSON-serialized versions of `CoreferenceContent`

* **text**: The input text (mandatory). 

Example:

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text."}' -H "Accept: text/plain" "http://localhost:8080/coreference/text"
```

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text."}' -H "Accept: application/json" "http://localhost:8080/coreference/text"
```


### Discourse Simplification
Discourse Simplification will recursively simplify the given input text and identify rhetorical relations, but will NOT extract subject-predicate-argument relations.
The Discourse Simplification service accepts the following endpoints with parameters:

#### Endpoint: /discourseSimplification/text
Returns the JSON-serialized versions of the `SimplificationContent` class or a textual representation depending on the value of the `Accept` header in the POST request:
* `Accept: text/plain`: returns a textual representation specified by the `format` parameter
* `Accept: application/json`: returns JSON-serialized versions of `SimplificationContent`

* **text**: The input text (mandatory). 
* **doCoreference**: Specifies whether coreference should be executed before Discourse-Simplification (true/false).
* **isolateSentences**: Specifies whether the sentences from the input text should be processed individually (This will not extract relationships that occur between neighboured sentences). Set **true**, if you run Graphene over a collection of independent sentences and **false** for a full coherent text (true/false).
* **format**: Specifies which textual representation should be returned:
    * `DEFAULT`: The default RDFNL format
    * `DEFAULT_RESOLVED`: The resolved default RDFNL format
    * `FLAT`: The flat RDFNL format
    * `FLAT_RESSOLVED`: The flat resolved RDFNL format

Examples:

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true", "isolateSentences": "false", "format": "DEFAULT"}' -H "Accept: text/plain" "http://localhost:8080/discourseSimplification/text"
```

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true", "isolateSentences": "false"}' -H "Accept: application/json" "http://localhost:8080/discourseSimplification/text"
```

### Open Relation Extraction
Open Relation Extraction will recursively simplify the given input text and identify rhetorical relations, as well as subject-predicate-argument relations.
The Open Relation Extraction service accepts the following endpoints with parameters:

#### Endpoint: /relationExtraction/text
Returns the JSON-serialized versions of the `ExContent` class or a textual representation depending on the value of the `Accept` header in the POST request:
* `Accept: text/plain`: returns a textual representation specified by the `format` parameter
* `Accept: application/json`: returns JSON-serialized versions of `ExContent`

* **text**: The input text (mandatory). 
* **doCoreference**: Specifies whether coreference should be executed before Relation-Extraction (true/false).
* **isolateSentences**: Specifies whether the sentences from the input text should be processed individually (This will not extract relationships that occur between neighboured sentences). Set **true**, if you run Graphene over a collection of independent sentences and **false** for a full coherent text (true/false).
* **format**: Specifies which textual representation should be returned:
    * `DEFAULT`: The default RDFNL format
    * `DEFAULT_RESOLVED`: The resolved default RDFNL format
    * `FLAT`: The flat RDFNL format
    * `FLAT_RESSOLVED`: The flat resolved RDFNL format
    * `RDF`: The RDF N-Triples format

Examples:

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true", "isolateSentences": "false", "format": "DEFAULT"}' -H "Accept: text/plain" "http://localhost:8080/relationExtraction/text"
```

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true", "isolateSentences": "false"}' -H "Accept: application/json" "http://localhost:8080/relationExtraction/text"
```
