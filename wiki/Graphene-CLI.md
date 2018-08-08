## Usage of the Graphene-CLI
The Graphene-CLI captures the same functionalities as the Graphene-Core API.

### Running the CLI
The CLI can be executed through Maven by running:

    cd graphene-cli
    mvn exec:java -Dexec.args="...command-line-arguments..." -Dconfig.file="...path-to-config-file..."
    
### Arguments
* **--help**: Prints help.
* **--version**: Prints the version of Graphene.
* **--operation**: The operation that sould be performed by Graphene:
    * `COREF`: Perform Coreference-Resolution.
    * `SIM`: Perform Discourse-Simplification.
    * `RE`: Perform Relation-Extraction.
* **--input**: Choose the input source:
    * `TEXT`: Use textual string.
    * `FILE`: Load text from filepath.
* **--output**: Choose whether to create files or print result to commandline.
    * `CMDLINE`: Print to commandline.
    * `FILE`: Store in filepath.
* **--corefformat**: Specifies which textual representation for Coreference-Resolution should be returned:
    * `DEFAULT`: The default textual string.
    * `SERIALIZED`: The serialized Java class
* **--simformat**: Specifies which textual representation for Discourse-Simplification should be returned:
    * `DEFAULT`: The default RDFNL format
    * `DEFAULT_RESOLVED`: The resolved default RDFNL format
    * `FLAT`: The flat RDFNL format
    * `FLAT_RESSOLVED`: The flat resolved RDFNL format
    * `SERIALIZED`: The serialized Java class
* **--reformat**: Specifies which textual representation for Relation-Extraction should be returned:
    * `DEFAULT`: The default RDFNL format
    * `DEFAULT_RESOLVED`: The resolved default RDFNL format
    * `FLAT`: The flat RDFNL format
    * `FLAT_RESOLVED`: The flat resolved RDFNL format
    * `RDF`: The RDF N-Triples format
    * `SERIALIZED`: The serialized Java class
* **--doCoreference**: Specifies whether coreference should be executed before Discourse-Simplification or Relation-Extraction (true/false).
* **--isolateSentences**: Specifies whether the sentences from the input text should be processed individually (This will not extract relationships that occur between neighboured sentences). Set **true**, if you run Graphene over a collection of independent sentences and **false** for a full coherent text (true/false).

The following argument specifies the data source specified by **--input**

Examples:
```sh
mvn exec:java -Dexec.args="--operation RE --output CMDLINE --reformat DEFAULT --input TEXT 'The text.'" -Dconfig.file="../conf/graphene.conf"
```

```sh
mvn exec:java -Dexec.args="--operation RE --output CMDLINE --reformat DEFAULT --input FILE input.txt" -Dconfig.file="../conf/graphene.conf"
```

[Back to the home page](../README.md)
