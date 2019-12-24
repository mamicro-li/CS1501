**bashtest.py**

Corpus list contains the unicode of symbol to generate.
Results will be saved in the `output_dir`
 ```
 python3 bashtest.py -corpus corpus_dir -out output_dir
 ```
<br>

**blsp_adder.py**

Argument [1] is the symbol file path
Symbol file format: symbols are in the first column of each line
a
b
c
...
 ```
 python3 blsp_adder.py symbolfile.txt
 ```
<br>

**char2index_with_blsp_es_ES.txt**

Contains supported characters for packing.
<br>

**check_font_exist.py**

check if the there is unsupported unicode in the corpus list.
 ```
 python3 check_font_exist.py -corpus corpus_dir  -font font_dir
 ```
<br>

**cp_gen_v2.py**

Use `python 2`

Used for fonts in a single directory
```
Font<br>
﹂arial.ttf
  pala.ttf
  ...
```
Argument [1] is the directory of font
 ```
 python cp_gen_v2.py font_dir
```
<br>


**cpGen.py**

Use python 2
Used for fonts seperated by unicodes
all_font<br>
﹂00A1<br>
	﹂arial.ttf<br>
	  calibri.ttf<br>
﹂00A2<br>
	﹂cambriab.ttf<br>
...
Argument [1] is the directory of font
```
python cpGen.py font_dir
```


**crawl_command.txt**

Contains command to use google crawler.


**filter_word.py**

Filter out unicodes that haven't generated
Need `new_list.txt` and `old_list.txt` in the same directory.
```
python3 filter_word.py
```


**find_font_combined.py**

Geneate a list:`character` `unicode_combined` `supported_fonts...`
Need to manually change `not_have_list` in the code.
```
python3 find_font_combined.py
```
<br>

**pack.py**

Pack data from `output_dir` to `pack_dir`
```
python3 pack.py -o output_dir -p pack_dir
```
<br>

**post_processing.py**

Filter out images that have giberish or character
not supported by its font.

Argument [1] is the output directory to process, 
it can also be the upper directory of outputs.
```
python3 post_processing.py output_dir
```
<br>
