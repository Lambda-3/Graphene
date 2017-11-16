# RDF Format

In order to increase further processability of the extracted relations, Graphene can materialize its relations into a graph serialized under the N-Triples specification of the RDF standard.

```
# Although the Treasury will announce details of the November refunding on Monday , the funding will be delayed if Congress and President Bush fail to increase the Treasury 's borrowing capacity .

_:1e39d1cfb167421bacc237d55b05832e <http://lambda3.org/graphene/sentence#original-text> "Although the Treasury will announce details of the November refunding on Monday , the funding will be delayed if Congress and President Bush fail to increase the Treasury 's borrowing capacity ."^^<http://www.w3.org/2001/XMLSchema#string> .

_:1e39d1cfb167421bacc237d55b05832e <http://lambda3.org/graphene/sentence#has-extraction> _:bacf06771e0f4fc5a8e68c30fc77c9c4 .
_:bacf06771e0f4fc5a8e68c30fc77c9c4 <http://lambda3.org/graphene/extraction#extraction-type> "VERB_BASED"^^<http://www.w3.org/2001/XMLSchema#string> .
_:bacf06771e0f4fc5a8e68c30fc77c9c4 <http://lambda3.org/graphene/extraction#context-layer> "0"^^<http://www.w3.org/2001/XMLSchema#integer> .
_:bacf06771e0f4fc5a8e68c30fc77c9c4 <http://lambda3.org/graphene/extraction#subject> <http://lambda3.org/graphene/text#the+Treasury> .
_:bacf06771e0f4fc5a8e68c30fc77c9c4 <http://lambda3.org/graphene/extraction#predicate> <http://lambda3.org/graphene/text#will+announce> .
_:bacf06771e0f4fc5a8e68c30fc77c9c4 <http://lambda3.org/graphene/extraction#object> <http://lambda3.org/graphene/text#details+of+the+November+refunding> .

_:bacf06771e0f4fc5a8e68c30fc77c9c4 <http://lambda3.org/graphene/extraction#S-TEMPORAL> <http://lambda3.org/graphene/text#on+Monday+.> .
_:bacf06771e0f4fc5a8e68c30fc77c9c4 <http://lambda3.org/graphene/extraction#L-CONTRAST> _:948eeebd73564adab7dee5c6f177b3b9 .

<http://lambda3.org/graphene/text#the+Treasury> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "the Treasury"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#will+announce> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "will announce"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#details+of+the+November+refunding> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "details of the November refunding"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#on+Monday+.> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "on Monday ."^^<http://www.w3.org/2001/XMLSchema#string> .

_:1e39d1cfb167421bacc237d55b05832e <http://lambda3.org/graphene/sentence#has-extraction> _:948eeebd73564adab7dee5c6f177b3b9 .
_:948eeebd73564adab7dee5c6f177b3b9 <http://lambda3.org/graphene/extraction#extraction-type> "VERB_BASED"^^<http://www.w3.org/2001/XMLSchema#string> .
_:948eeebd73564adab7dee5c6f177b3b9 <http://lambda3.org/graphene/extraction#context-layer> "0"^^<http://www.w3.org/2001/XMLSchema#integer> .
_:948eeebd73564adab7dee5c6f177b3b9 <http://lambda3.org/graphene/extraction#subject> <http://lambda3.org/graphene/text#the+funding> .
_:948eeebd73564adab7dee5c6f177b3b9 <http://lambda3.org/graphene/extraction#predicate> <http://lambda3.org/graphene/text#will+be+delayed> .
_:948eeebd73564adab7dee5c6f177b3b9 <http://lambda3.org/graphene/extraction#object> <http://lambda3.org/graphene/text#> .

_:948eeebd73564adab7dee5c6f177b3b9 <http://lambda3.org/graphene/extraction#L-CONDITION> _:006a71e51295440fab7a8e8c697d2ba6 .
_:948eeebd73564adab7dee5c6f177b3b9 <http://lambda3.org/graphene/extraction#L-CONDITION> _:e4d86228cff443b7a8e9f6d8a5c5987b .
_:948eeebd73564adab7dee5c6f177b3b9 <http://lambda3.org/graphene/extraction#L-CONTRAST> _:bacf06771e0f4fc5a8e68c30fc77c9c4 .

<http://lambda3.org/graphene/text#the+funding> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "the funding"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#will+be+delayed> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "will be delayed"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> ""^^<http://www.w3.org/2001/XMLSchema#string> .

_:1e39d1cfb167421bacc237d55b05832e <http://lambda3.org/graphene/sentence#has-extraction> _:006a71e51295440fab7a8e8c697d2ba6 .
_:006a71e51295440fab7a8e8c697d2ba6 <http://lambda3.org/graphene/extraction#extraction-type> "VERB_BASED"^^<http://www.w3.org/2001/XMLSchema#string> .
_:006a71e51295440fab7a8e8c697d2ba6 <http://lambda3.org/graphene/extraction#context-layer> "1"^^<http://www.w3.org/2001/XMLSchema#integer> .
_:006a71e51295440fab7a8e8c697d2ba6 <http://lambda3.org/graphene/extraction#subject> <http://lambda3.org/graphene/text#Congress> .
_:006a71e51295440fab7a8e8c697d2ba6 <http://lambda3.org/graphene/extraction#predicate> <http://lambda3.org/graphene/text#fail> .
_:006a71e51295440fab7a8e8c697d2ba6 <http://lambda3.org/graphene/extraction#object> <http://lambda3.org/graphene/text#to+increase+the+Treasury+%27s+borrowing+capacity> .

_:006a71e51295440fab7a8e8c697d2ba6 <http://lambda3.org/graphene/extraction#L-LIST> _:e4d86228cff443b7a8e9f6d8a5c5987b .

<http://lambda3.org/graphene/text#Congress> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "Congress"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#fail> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "fail"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#to+increase+the+Treasury+%27s+borrowing+capacity> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "to increase the Treasury 's borrowing capacity"^^<http://www.w3.org/2001/XMLSchema#string> .

_:1e39d1cfb167421bacc237d55b05832e <http://lambda3.org/graphene/sentence#has-extraction> _:e4d86228cff443b7a8e9f6d8a5c5987b .
_:e4d86228cff443b7a8e9f6d8a5c5987b <http://lambda3.org/graphene/extraction#extraction-type> "VERB_BASED"^^<http://www.w3.org/2001/XMLSchema#string> .
_:e4d86228cff443b7a8e9f6d8a5c5987b <http://lambda3.org/graphene/extraction#context-layer> "1"^^<http://www.w3.org/2001/XMLSchema#integer> .
_:e4d86228cff443b7a8e9f6d8a5c5987b <http://lambda3.org/graphene/extraction#subject> <http://lambda3.org/graphene/text#president+Bush> .
_:e4d86228cff443b7a8e9f6d8a5c5987b <http://lambda3.org/graphene/extraction#predicate> <http://lambda3.org/graphene/text#fail> .
_:e4d86228cff443b7a8e9f6d8a5c5987b <http://lambda3.org/graphene/extraction#object> <http://lambda3.org/graphene/text#to+increase+the+Treasury+%27s+borrowing+capacity> .

_:e4d86228cff443b7a8e9f6d8a5c5987b <http://lambda3.org/graphene/extraction#L-LIST> _:006a71e51295440fab7a8e8c697d2ba6 .

<http://lambda3.org/graphene/text#president+Bush> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "president Bush"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#fail> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "fail"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#to+increase+the+Treasury+%27s+borrowing+capacity> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "to increase the Treasury 's borrowing capacity"^^<http://www.w3.org/2001/XMLSchema#string> .
```

In this format, each sentence is modeled as a a blank node, containing the original sentence and a list of core-extractions (`has-extraction`) . Each extraction is represented as a blank node with `subject`, `predicate` and `object` RDF-text-resources that represent the relational tuple. Contextual arguments are attached as RDF-predicates that encode both the `context_class` and the classified semantic relation (see [RDFNL-Format](wiki/RDFNL-Format.md)) and link to either text-resources or to other extractions.
Each extraction has an attribute `context-layer` which specifies the layer of contextual assignment (see [RDFNL-Format](wiki/RDFNL-Format.md))
Finally, all RDF-text-resources are mapped to real string values.