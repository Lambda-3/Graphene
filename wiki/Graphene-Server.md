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

### Open Relation Extraction
The Open Relation Extraction service accepts the following endpoints with parameters:

#### Endpoint: /relationExtraction/text
Returns the JSON-serialized versions of the `ExContent` class or a textual representation depending on the value of the `Accept` header in the POST request:
* `Accept: application/json`: returns JSON-serialized versions of `ExContent`
* `Accept: text/plain`: returns a textual representation specified by the `format` parameter

* **text**: The input text (mandatory). 
* **doCoreference**: Specifies, whether coreference should be executed before the simplification (true/false).
* **isolateSentences**: Specifies whether the sentences from the input text should be processed individually (This will not extract relationships that occur between neighboured sentences). Set **true**, if you run Graphene over a collection of independent sentences and **false** for a full coherent text (true/false).
* **format**: Specifies, which textual representation should be returned:
    * `DEFAULT`: The default RDFNL format
    * `FLAT`: The flat RDFNL format
    * `EXTENDED`: The extended RDFNL format
    * `RDF`: The RDF N-Triples format

Examples:

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true", "isolateSentences": "false"}' -H "Accept: application/json" "http://localhost:8080/relationExtraction/text"
```

```sh
curl -X POST -H "Content-Type: application/json"  -d '{"text": "The text.", "doCoreference": "true", "isolateSentences": "false", "format": "DEFAULT"}' -H "Accept: text/plain" "http://localhost:8080/relationExtraction/text"
```
