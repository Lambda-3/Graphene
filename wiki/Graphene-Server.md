## Usage of the Graphene-Server
The Graphene-Server captures the same functionalities as the Graphene-Core API.
The wrapped Core API functions can be executed by HTTP GET requests.

### Resolving co-references
The coreference service accepts the following endpoints with parameters:

#### Endpoint: /coreference/text
Returns the JSON-serialized version of the `CoreferenceContent` class.

* **text**: The text that should be resolved (mandatory). 
* **uri**: ???. 
* **links**: ???. 

Example:

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text."}' -H "Accept: application/json" "http://localhost:8080/coreference/text"
```

### Simplification
The simplification service accepts the following endpoints with parameters:

#### Endpoint: /simplification/text
Returns the JSON-serialized version of the `SimplificationContent` class.

* **text**: The text that should be resolved (mandatory). 
* **doCoreference**: Specifies, whether coreference should be executed before the simplification (true/false). 

Example:

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true"}' -H "Accept: application/json" "http://localhost:8080/simplification/text"
```

### Open Information Extraction
The open information extraction service accepts the following endpoints with parameters:

#### Endpoint: /graphExtraction/text
Returns the JSON-serialized versions of the `ExtractionContent` or `ExSimplificationContent` class depending on the `doSimplification` parameter.

* **text**: The text that should be resolved (mandatory). 
* **doCoreference**: Specifies, whether coreference should be executed before the simplification (true/false).
* **doSimplification**: Specifies, whether simplification should be executed before the open information extraction (true/false). 

Example:

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true", "doSimplification": "true"}' -H "Accept: application/json" "http://localhost:8080/graphExtraction/text"
```

#### Endpoint: /graphExtraction/rdf (with Simplification only)
Returns the JSON-serialized versions of the `RDFOutput` or the RDF.NL representation as plain text depending on the value of the `Accept` header in the POST request:
* `Accept: application/json`: returns SON-serialized versions of `RDFOutput`
* `Accept: text/plain`: RDF.NL representation as plain text

* **text**: The text that should be resolved (mandatory). 
* **doCoreference**: Specifies, whether coreference should be executed before the simplification (true/false).

Examples:

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true"}' -H "Accept: application/json" "http://localhost:8080/graphExtraction/rdf"
```

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true"}' -H "Accept: text/plain" "http://localhost:8080/graphExtraction/rdf"
```