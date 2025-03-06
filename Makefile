CXX ?= g++
LD ?= $(CXX)

CXXFLAGS ?= -std=c++20 -Wall -Wextra -Wpedantic 
INCLUDES ?= -I./include/

BUILD_DIR ?= build

ifdef DEBUG
	CXXFLAGS += -g -O0 -DDEBUG
else
	CXXFLAGS += -O2 -DNDEBUG
endif

CORE_SRC = $(wildcard src/*.cc)


# Add dummy implementation 
DUMMY_SRC = $(wildcard pagerank/dummy/*.cc)

# Add sequential implementation 
SEQ_SRC = $(wildcard pagerank/seq/*.cc)

# Add parallel implementation here 
# e.g. an OpenMP implementation
# OMP_SRC = $(wildcard pagerank/omp/*.cc)

.PHONY: all clean directories dummy seq 

all: seq

directories:
	mkdir -p $(BUILD_DIR)

dummy: directories
	$(CXX) $(CXXFLAGS) $(INCLUDES) $(CORE_SRC) $(DUMMY_SRC) -o $(BUILD_DIR)/pagerank_dummy

seq: directories
	$(CXX) $(CXXFLAGS) $(INCLUDES) $(CORE_SRC) $(SEQ_SRC) -o $(BUILD_DIR)/pagerank_seq

# Add parallel implementation here
# e.g. an OpenMP implementation
# omp:
# 	$(CXX) $(CXXFLAGS) $(INCLUDES) $(CORE_SRC) $(OMP_SRC) -fopenmp -o $(BUILD_DIR)/pagerank_omp

clean:
	rm -rf $(BUILD_DIR)/*
