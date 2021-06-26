# SCOP_extractor

## Usage:

1. Download latest [SCOP database file](https://scop.mrc-lmb.cam.ac.uk/download)
2. Pick the functionality of the tool:
    * Generate protein list files from the latest SCOP database file to download protein chains:  
    `java SCOPExtractor [SCOP_database_file] protein [number_of_proteins_to_extract_from_each_class]`
    
    * Generate random protein list files with protein chain of interest to download from PDB:   
    `java SCOPExtractor [SCOP_database_file] random [protein_chain_of_interest]`
    
    * Generate datasets for NN model:   
    `java SCOPExtractor [SCOP_database_file] model [fraction_of_SCOP_devoted_for_training]`
    
    * Generate labelled datasets:  
    `java SCOPExtractor [SCOP_database_file] labelled`
    
    * Generate protein list files (for separate class files):  
    `java SCOPExtractor [SCOP_database_file] -`
    
