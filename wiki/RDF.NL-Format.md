# RDF.NL Format

The RDF.NL format was designed to act as an easy representation, which is able to cover complex contextual relations, keeping the simplicity to be parsed and further processed by programs. The RDF.NL format can be printed in the following ways:

## Default-Format

```
# The café arrived in Paris in the 17th century , when the beverage was first brought from Turkey , and by the 18th century Parisian cafés were centres of the city 's political and cultural life .
 
3469c5159933484a8bbdb5b1df36136f    0   The café    arrived in Paris in the 17th century
    ELEM-BACKGROUND fcbcb5c547f248cc88f8aa3033178d12
 
fcbcb5c547f248cc88f8aa3033178d12    1   The beverage    was first brought   
    VCON-SPATIAL    from Turkey .
 
394d55aacc064bcc82912689dc0500b9    0   Parisian cafés  were    centres of the city 's political and cultural life
    VCON-TEMPORAL   by the 18th century .
```

In the Default-Format, extractions are grouped by sentences in which they occur and represented by tab-separated values for the `identifier`, the `context_layer`, as well as `subject`, `predicate` and `object`.

The numerical `context_layer` value determines the layer of contextual assignment. Extractions with a `context-layer = 0` provide core-information of the input sentence while those with `context-layer > 0` provide contextual information about extractions with `context-layer - 1`.

Contextual dependencies are indicated by an extra indentation level to their parent extractions, followed by a `type_string`.

The `type_string` encodes both `context_class`:

* `VCON`: context mediated by verbs
* `NCON`: context mediated by nouns
* `ELEM`: relation to another extraction

and the classified `rhetorical_relation` (e.g. CAUSE, ENABLEMENT, LOCATION) separated by a `-` character.

After the `type_string`, there follow tab-separated values for:
* `object` (if `type_string = VCON`)
* `subject`, `predicate` and `object` (if `type_string = NCON`)
* the `identifier` of another referenced extraction (if `type_string = ELEM`)

## Flat-Format

```
# The café arrived in Paris in the 17th century , when the beverage was first brought from Turkey , and by the 18th century Parisian cafés were centres of the city 's political and cultural life .
 
3469c5159933484a8bbdb5b1df36136f    0   The café    arrived in Paris in the 17th century    ELEM-BACKGROUND(fcbcb5c547f248cc88f8aa3033178d12)
fcbcb5c547f248cc88f8aa3033178d12    1   The beverage    was first brought       VCON-SPATIAL(from Turkey .)
394d55aacc064bcc82912689dc0500b9    0   Parisian cafés  were    centres of the city 's political and cultural life  VCON-TEMPORAL(by the 18th century .)
```

In the Flat-Format, each extraction is shown in one line. Contextual dependencies are appended at the end of the `identifier`, the `context_layer`, `subject`, `predicate` and `object`, all separated by tabs. The contexts are represented by the `type_string` that is encoded just like in the Default-Format and their content in round brackets.

## Expanded-Format

```
# The café arrived in Paris in the 17th century , when the beverage was first brought from Turkey , and by the 18th century Parisian cafés were centres of the city 's political and cultural life .
 
0   The café    arrived in Paris in the 17th century
    ELEM-BACKGROUND The beverage    was first brought   
        VCON-SPATIAL    from Turkey .
 
1   The beverage    was first brought   
    VCON-SPATIAL    from Turkey .
 
0   Parisian cafés  were    centres of the city 's political and cultural life
    VCON-TEMPORAL   by the 18th century .
```

The Expanded-Format is like the Default-Format but without identifiers. Thereby, references to contextual extractions are resolved and represented as a nested hierarchy by their indentations.

[Back to the home page](../README.md)