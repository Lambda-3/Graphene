# RDF.NL Format

The RDF.NL format was designed to act as an abstract RDF-representation, which is able to cover complex contextual relations, keeping the simplicity to be parsed and further processed by programs. The abstract representation also allows for customized RDF-generation where content from RDF.NL can be adapted or discarded depending on the user's needs.

The RDF.NL elements are:
* **DIS_TYPE:** Specifies whether the core-sentence plays a major role within discourse context (DISCOURSE_CORE) or a subordinate role (DISCOURSE_CONTEXT). It has the format:

    `CoreID DISCOURSE_TYPE DISCOURSE_CORE/DISCOURSE_CONTEXT`
* **CORE_EXTR:** An extraction of a core-sentence which has the format:

    `CoreID subject predicate object/NULL`
* **CONTEXT_EXTR:** An extraction of a context-sentence which has the format:

    `ContextID subject predicate object/NULL`
* **RHET_REL:** A rhetorical link between two core-sentences or between a core-sentence and a context-sentence. It has the format:

    `CoreID relation CoreID/ContextID`

For clarification, here is the corresponding RDF.NL representation for the visualization of the example in the [home page](../wiki):

`In mid-1981 , Obama traveled to Indonesia to visit his mother and half-sister Maya , and visited the families of college friends in Pakistan and India for three weeks .`

[[images/Graphene-Extraction.jpg]]

```
# original sentence: 'In mid-1981 , Obama traveled to Indonesia to visit his mother and half-sister Maya , and visited the families of college friends in Pakistan and India for three weeks .'

## core sentence: 'Obama traveled .'
DIS_TYPE:		CORE-87a64b24-7887-4141-a1f2-a9d13c2ab36d		DISCOURSE_TYPE		DISCOURSE_CORE
CORE_EXT:		CORE-87a64b24-7887-4141-a1f2-a9d13c2ab36d		Obama		traveled		null
RHET_REL:		CORE-87a64b24-7887-4141-a1f2-a9d13c2ab36d		JOINT_LIST		CORE-8a3d79ae-129a-483b-948f-5a99058b182c
### context sentence: 'This was in mid-1981 .'
CONT_EXT:		CONTEXT-597339dd-0084-4410-9ae4-0425784a47fc		This		was		in mid-1981
RHET_REL:		CORE-87a64b24-7887-4141-a1f2-a9d13c2ab36d		UNKNOWN_SENT_SIM		CONTEXT-597339dd-0084-4410-9ae4-0425784a47fc
### context sentence: 'This was to Indonesia .'
CONT_EXT:		CONTEXT-00641e4e-49eb-4ca7-a1a5-304cb3702269		This		was		to Indonesia
RHET_REL:		CORE-87a64b24-7887-4141-a1f2-a9d13c2ab36d		LOCATION		CONTEXT-00641e4e-49eb-4ca7-a1a5-304cb3702269
### context sentence: 'This was to visit his mother and half-sister Maya .'
CONT_EXT:		CONTEXT-36125a8f-6f11-4ad4-9786-75252c018ab1		This		was		to visit his mother and half-sister
CONT_EXT:		CONTEXT-36125a8f-6f11-4ad4-9786-75252c018ab1		This		to visit		his mother and half-sister
CONT_EXT:		CONTEXT-36125a8f-6f11-4ad4-9786-75252c018ab1		Maya		[is] sister [of]		[UNKNOWN]
RHET_REL:		CORE-87a64b24-7887-4141-a1f2-a9d13c2ab36d		ENABLEMENT		CONTEXT-36125a8f-6f11-4ad4-9786-75252c018ab1

## core sentence: 'Obama visited the families of college friends in Pakistan and India for three weeks .'
DIS_TYPE:		CORE-8a3d79ae-129a-483b-948f-5a99058b182c		DISCOURSE_TYPE		DISCOURSE_CORE
CORE_EXT:		CORE-8a3d79ae-129a-483b-948f-5a99058b182c		Obama		visited		the families of college friends in Pakistan and India
RHET_REL:		CORE-8a3d79ae-129a-483b-948f-5a99058b182c		JOINT_LIST		CORE-87a64b24-7887-4141-a1f2-a9d13c2ab36d
### context sentence: 'This was in mid-1981 .'
CONT_EXT:		CONTEXT-16b15a92-0f0d-41da-84c7-143bb1a9cc48		This		was		in mid-1981
RHET_REL:		CORE-8a3d79ae-129a-483b-948f-5a99058b182c		TIME		CONTEXT-16b15a92-0f0d-41da-84c7-143bb1a9cc48
```

[Back to the home page](../README.md)