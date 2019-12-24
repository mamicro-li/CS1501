**bashtest.py**

Corpus list contains the unicode of symbol to generate.
Results will be saved in the `output_dir`
 ```
 python3 bashtest.py -corpus corpus_dir -out output_dir
 ```



**blsp_adder.py**

Argument [1] is the symbol file path
Symbol file format: symbols are in the first column of each line
```
a
b
c
...
```
 ```
 python3 blsp_adder.py symbolfile.txt
 ```



**char2index_with_blsp_es_ES.txt**

Contains supported characters for packing.



**check_font_exist.py**

check if the there is unsupported unicode in the corpus list.
 ```
 python3 check_font_exist.py -corpus corpus_dir  -font font_dir
 ```


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


**cpGen.py**


Use `python 2`


Used for fonts seperated by unicodes
```
all_font
﹂00A1
	﹂arial.ttf
	  calibri.ttf
﹂00A2
	﹂cambriab.ttf
...
```
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



**pack.py**

Pack data from `output_dir` to `pack_dir`
```
python3 pack.py -o output_dir -p pack_dir
```



**post_processing.py**

Filter out images that have giberish or character
not supported by its font.

Argument [1] is the output directory to process, 
it can also be the upper directory of outputs.
```
python3 post_processing.py output_dir
```


**unpack.py**

Unpack the packed data and save the image.
```
python3 unpack pack_dir
```


**VM.txt**

Contains VM information.


**word_select.py**

Filter word with length of 1, 2 and 3 from current directory.
```
python3 word_select.py -o out_dir -num max_word_of_each_length
```


**Depth_detection**

Demo code in `demo/demo_DCNF_FCSP_depths_prediction.m`



**Segmentation**

Demo code in `pre-trained/demos/demo_im2*`



**crop_form.py**

Crop IAM form data based on `lines.txt`.
```
python3 crop_form.py
```


**genismtest.py**

Get corpus from Wikicorpus.

Manually change the `path_to_wiki_dump`
```
python3 genismtest.py
```



**iam_division_form.py**

Divide IAM data into train, validataion and test sets.

```
python3 iam_division_form.py
```


**resize_color.py & resize_train_val.py**

Resize IAM images to specific height. Use `python3`.


**skeletonize.py**

Extract skeleton from groundtruth IAM image. Use `python3`.


**wikicorpus.py**

Modified wikicorpus source code. (Remove regular expression filter and tokenizing process)


