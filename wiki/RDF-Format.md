# RDF Format

In order to increase further processability of the extracted relations, Graphene can materialize its relations into a graph serialized under the N-Triples specification of the RDF standard.

```
# The café arrived in Paris in the 17th century , when the beverage was first brought from Turkey , and by the 18th century Parisian cafés were centres of the city 's political and cultural life .
 
_:d677f6b6798041228711fa74b98bbf1a <http://lambda3.org/graphene/sentence#original-text> "The café arrived in Paris in the 17th century , when the beverage was first brought from Turkey , and by the 18th century Parisian cafés were centres of the city 's political and cultural life ."^^<http://www.w3.org/2001/XMLSchema#string> .
 
_:d677f6b6798041228711fa74b98bbf1a <http://lambda3.org/graphene/sentence#has-extraction> _:3084aa537b134d179cc7c09a2f87f27c .
_:3084aa537b134d179cc7c09a2f87f27c <http://lambda3.org/graphene/extraction#subject> <http://lambda3.org/graphene/text#The café> .
_:3084aa537b134d179cc7c09a2f87f27c <http://lambda3.org/graphene/extraction#predicate> <http://lambda3.org/graphene/text#arrived> .
_:3084aa537b134d179cc7c09a2f87f27c <http://lambda3.org/graphene/extraction#object> <http://lambda3.org/graphene/text#in Paris in the 17th century> .
_:3084aa537b134d179cc7c09a2f87f27c <http://lambda3.org/graphene/extraction#context-layer> "0"^^<http://www.w3.org/2001/XMLSchema#integer> .
<http://lambda3.org/graphene/text#The café> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "The café"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#arrived> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "arrived"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#in Paris in the 17th century> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "in Paris in the 17th century"^^<http://www.w3.org/2001/XMLSchema#string> .
_:3084aa537b134d179cc7c09a2f87f27c <http://lambda3.org/graphene/extraction#ELEM-BACKGROUND> _:e5f346b7470e46f287577451c898378d .
 
_:d677f6b6798041228711fa74b98bbf1a <http://lambda3.org/graphene/sentence#has-extraction> _:e5f346b7470e46f287577451c898378d .
_:e5f346b7470e46f287577451c898378d <http://lambda3.org/graphene/extraction#subject> <http://lambda3.org/graphene/text#The beverage> .
_:e5f346b7470e46f287577451c898378d <http://lambda3.org/graphene/extraction#predicate> <http://lambda3.org/graphene/text#was first brought> .
_:e5f346b7470e46f287577451c898378d <http://lambda3.org/graphene/extraction#object> <http://lambda3.org/graphene/text#> .
_:e5f346b7470e46f287577451c898378d <http://lambda3.org/graphene/extraction#context-layer> "1"^^<http://www.w3.org/2001/XMLSchema#integer> .
<http://lambda3.org/graphene/text#The beverage> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "The beverage"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#was first brought> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "was first brought"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> ""^^<http://www.w3.org/2001/XMLSchema#string> .
_:e5f346b7470e46f287577451c898378d <http://lambda3.org/graphene/extraction#VCON-SPATIAL> <http://lambda3.org/graphene/text#from Turkey .> .
<http://lambda3.org/graphene/text#from Turkey .> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "from Turkey ."^^<http://www.w3.org/2001/XMLSchema#string> .
 
_:d677f6b6798041228711fa74b98bbf1a <http://lambda3.org/graphene/sentence#has-extraction> _:6f51a07ceb914c1096b64592a5078bed .
_:6f51a07ceb914c1096b64592a5078bed <http://lambda3.org/graphene/extraction#subject> <http://lambda3.org/graphene/text#Parisian cafés> .
_:6f51a07ceb914c1096b64592a5078bed <http://lambda3.org/graphene/extraction#predicate> <http://lambda3.org/graphene/text#were> .
_:6f51a07ceb914c1096b64592a5078bed <http://lambda3.org/graphene/extraction#object> <http://lambda3.org/graphene/text#centres of the city 's political and cultural life> .
_:6f51a07ceb914c1096b64592a5078bed <http://lambda3.org/graphene/extraction#context-layer> "0"^^<http://www.w3.org/2001/XMLSchema#integer> .
<http://lambda3.org/graphene/text#Parisian cafés> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "Parisian cafés"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#were> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "were"^^<http://www.w3.org/2001/XMLSchema#string> .
<http://lambda3.org/graphene/text#centres of the city 's political and cultural life> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "centres of the city 's political and cultural life"^^<http://www.w3.org/2001/XMLSchema#string> .
_:6f51a07ceb914c1096b64592a5078bed <http://lambda3.org/graphene/extraction#VCON-TEMPORAL> <http://lambda3.org/graphene/text#by the 18th century .> .
<http://lambda3.org/graphene/text#by the 18th century .> <http://www.w3.org/1999/02/22-rdf-syntax-ns#value> "by the 18th century ."^^<http://www.w3.org/2001/XMLSchema#string> .
```

In this format, each sentence is modelled as a a blank node, containing the original sentence and a list of core-extractions (`has-extraction`) . Each extraction is represented as a blank node with `subject`, `predicate` and `object` RDF-text-resources that represent the relational tuple. Contextual information is attached by RDF-predicates that encode both the `context_class` and the classified `rhetorical_relation` (see [RDF.NL-Format](wiki/RDF.NL-Format.md)) and link to either text-resources or to other extractions.

Each extraction has an attribute `context-layer` which specifies the layer of contextual assignment (see [RDF.NL-Format](wiki/RDF.NL-Format.md))

Finally, all RDF-text-resources are mapped to real string attributes.
