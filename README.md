# Pythia

Pythia is a plug'n'play engine for ontology-driven, deep natural language understanding and generation over structured (RDF) data. 


## Usage

### Setup

    $ java -jar pythia-0.1-standalone.jar setup [files]

Where `files` are file paths to the ontology and lexicon file(s). 

### Run application 

Once you have run `setup', Pythia is ready to rock and roll. 
Start it in question answering mode:

    $ java -jar pythia-0.1-standalone.jar qa 


## Configuration

All necessary settings and parameters are specified in `src/settings.clj`.


## Website 

For more information please visit [ontosem.net/pythia/](http://ontosem.net/pythia/).
