# RDFNL Format

The RDFNL format was designed to act as an easy representation, which is able to cover complex contextual relations, keeping the simplicity to be parsed and further processed by programs. The RDFNL format can be printed in the following ways:

## Default-Format

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

In the Default-Format, extractions are grouped by sentences in which they occur and represented by tab-separated values for the `identifier`, the `context_layer`, as well as `subject`, `predicate` and `object`.

The numerical `context_layer` value determines the layer of contextual assignment. Extractions with a `context-layer = 0` provide core-information of the input sentence while those with `context-layer > 0` provide contextual information about extractions with `context-layer - 1`.

Contextual dependencies are indicated by an extra indentation level to their parent extractions, followed by a `type_string`.

The `type_string` encodes both `context_class`:

* `S`: Simple contextual argument (contains textual content) 
* `L`: Linked contextual argument (contains a link to another extraction)

and the classified semantic relation (e.g. CAUSE, PURPOSE, LOCATION), separated by a `:` character.

After the `type_string`, there follows:
* the textual content (for simple contexts) or
* the `identifier` of another referenced extraction (for linked contexts)

## Flat-Format

```
Although the Treasury will announce details of the November refunding on Monday , the funding will be delayed if Congress and President Bush fail to increase the Treasury 's borrowing capacity .    bacf06771e0f4fc5a8e68c30fc77c9c4    0    the Treasury    will announce    details of the November refunding    S:TEMPORAL(on Monday .) L:CONTRAST(948eeebd73564adab7dee5c6f177b3b9)
Although the Treasury will announce details of the November refunding on Monday , the funding will be delayed if Congress and President Bush fail to increase the Treasury 's borrowing capacity .    948eeebd73564adab7dee5c6f177b3b9    0    the funding    will be delayed        L:CONDITION(006a71e51295440fab7a8e8c697d2ba6)    L:CONDITION(e4d86228cff443b7a8e9f6d8a5c5987b)    L:CONTRAST(bacf06771e0f4fc5a8e68c30fc77c9c4)
Although the Treasury will announce details of the November refunding on Monday , the funding will be delayed if Congress and President Bush fail to increase the Treasury 's borrowing capacity .    006a71e51295440fab7a8e8c697d2ba6    1    Congress    fail    to increase the Treasury 's borrowing capacity    L:LIST(e4d86228cff443b7a8e9f6d8a5c5987b)
Although the Treasury will announce details of the November refunding on Monday , the funding will be delayed if Congress and President Bush fail to increase the Treasury 's borrowing capacity .    e4d86228cff443b7a8e9f6d8a5c5987b    1    president Bush    fail    to increase the Treasury 's borrowing capacity    L:LIST(006a71e51295440fab7a8e8c697d2ba6)
```

In the Flat-Format, each extraction is shown in one line. Contextual arguments are appended at the end of the `input sentence`, `identifier`, `context_layer`, `subject`, `predicate` and `object` values, all separated by tabs. The contextual arguments are represented by the `type_string` that is encoded just like in the Default-Format. Their content is enclosed in round brackets.

## Resolved-Formats

For both Default- and Flat- format, we provide a resolved format where `identifier`s of referenced extractions from linked contextual arguments are resolved by their tab-separated values for `subject`, `predicate` and `object`.

An example of the resolved Default-Format is shown below:

```
# Although the Treasury will announce details of the November refunding on Monday , the funding will be delayed if Congress and President Bush fail to increase the Treasury 's borrowing capacity .

bacf06771e0f4fc5a8e68c30fc77c9c4    0    the Treasury    will announce    details of the November refunding
    S:TEMPORAL    on Monday .
    L:CONTRAST    the funding    will be delayed        

948eeebd73564adab7dee5c6f177b3b9    0    the funding    will be delayed        
    L:CONDITION Congress    fail    to increase the Treasury 's borrowing capacity
    L:CONDITION president Bush    fail    to increase the Treasury 's borrowing capacity
    L:CONTRAST    the Treasury    will announce    details of the November refunding

006a71e51295440fab7a8e8c697d2ba6    1    Congress    fail    to increase the Treasury 's borrowing capacity
    L:LIST    president Bush    fail    to increase the Treasury 's borrowing capacity

e4d86228cff443b7a8e9f6d8a5c5987b    1    president Bush    fail    to increase the Treasury 's borrowing capacity
    L:LIST    Congress    fail    to increase the Treasury 's borrowing capacity
```


[Back to the home page](../README.md)
