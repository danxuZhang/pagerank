# NTUHPC Parallel Computing Challenge - Page Rank

## Page Rank

[Stanford Slides for PageRank](https://web.stanford.edu/class/cs246/slides/09-pagerank.pdf)

$$PR(p_i) = \frac{1-d}{N} + d \sum_{p_j \in M(p_i)} \frac{PR(p_j)}{L(p_j)}$$

### Implementation Ideas
- Map Reduce
- Data Parallel
- Matrix Multiplication
- ...

## Implementation

Either CPU or GPU, focus on multi-threaded shared-memory implementation.


To add a new implementation:

- Add a new folder in `pagerank` folder
- Add new files to Makefile
- Compile

### Suggested Library/Framework

- C++ Standard Parallelism Execution Policy
- OpenMP
- OpenACC
- CUDA
- ...

## Benchmarking 

### Dataset
SNAP dataset, each line in the txt file reprsents an edge.

``` bash
./utils/download_dataset.sh
```

### Resrouce
EITHER 
- 1x CPU Node @ Aspire2A (128x AMD 7713 CPU)

OR
- 1x GPU on 1x GPU Node @ Aspire2A (1x NVIDIA A100 GPU)

### Verify Correctness 

Use Python3 networkx library to verify correctness.

``` bash
python3 -m venv .venv
source .venv/bin/activate
pip install python/requirements.txt

time python3 python/pagerank.py build/web-BerkStan.txt
```
