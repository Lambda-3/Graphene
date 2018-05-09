## Usage of the Graphene-CLI
The Graphene-CLI captures the same functionalities as the Graphene-Core API.

### Running the CLI
The CLI can be executed through Maven by running:

    cd graphene-cli
    mvn exec:java -Dexec.args="...command-line-arguments..." -Dconfig.file="...path-to-config-file..."
    
### Arguments
* **--help**: Prints help.
* **--version**: Prints the version of Graphene.
* **--input**: Choose the input source:
    * `TEXT`
    * `FILE`
    * `WIKI`
* **--output**: Choose whether to create files or print result on commandline.
    * `FILE`
    * `CMDLINE`
* **--doCoreference**: Specifies whether coreference should be executed before the simplification (true/false).
* **--relationExtraction**: Specifies whether relation extraction should be executed.
* **--isolateSentences**: Specifies whether the sentences from the input text should be processed individually (This will not extract relationships that occur between neighboured sentences). Set **true**, if you run Graphene over a collection of independent sentences and **false** for a full coherent text (true/false).
* **--format**: Specifies which textual representation should be returned:
    * `DEFAULT`: The default RDFNL format
    * `DEFAULT_RESOLVED`: The resolved default RDFNL format
    * `FLAT`: The flat RDFNL format
    * `FLAT_RESSOLVED`: The flat resolved RDFNL format
    * `RDF`: The RDF N-Triples format
    * `SERIALIZED`: The serialized Java class

The following argument specifies the data source specified by **--input**

Examples:
```sh
mvn exec:java -Dexec.args="--relationExtraction --output CMDLINE --format DEFAULT --input TEXT 'The text.'" -Dconfig.file="../conf/graphene.conf"
```

```sh
mvn exec:java -Dexec.args="--relationExtraction --output CMDLINE --format DEFAULT --input FILE input.txt" -Dconfig.file="../conf/graphene.conf"
```

[Back to the home page](../README.md)
