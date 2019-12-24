##bashtest.py
Corpus list contains the unicode of symbol to generate
Results will be saved in the output_dir
`python3 bashtest.py -corpus corpus_dir -out output_dir`


##blsp_adder.py
Argument [1] is the symbol file path
Symbol file format: symbols are in the first column of each line
a
b
c
...
`python3 blsp_adder.py symbolfile.txt`

##char2index_with_blsp_es_ES.txt
Contains supported characters for packing.

##check_font_exist.py
check if the there is unsupported unicode in the corpus list
`python3 check_font_exist.py -corpus corpus_dir  -font font_dir`

##cp_gen_v2.py
Use python 2
Used for fonts in a single directory
Font
﹂arial.ttf
  pala.ttf
  ...
Argument [1] is the directory of font
`python cpGen.py font_dir`