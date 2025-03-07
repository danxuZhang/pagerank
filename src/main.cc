#include <chrono>
#include <fstream>
#include <iostream>

#include "pagerank.h"

#ifndef DAMPING_FACTOR
#define DAMPING_FACTOR 0.85
#endif

#ifndef EPSILON
#define EPSILON 1e-12
#endif

int main(int argc, char *argv[]) {
  if (argc != 3) {
    std::cerr << "Usage: " << argv[0] << " <graph input> <rank output>"
              << std::endl;
    return 1;
  }

  const std::string filename = argv[1];
  const std::string output = argv[2];

  const auto start = std::chrono::high_resolution_clock::now();
  const auto pagerank = pagerank::PageRank(filename, DAMPING_FACTOR, EPSILON);
  const auto end = std::chrono::high_resolution_clock::now();
  const std::chrono::duration<double> elapsed = end - start;
  std::cout << "Elapsed time: " << elapsed.count() << " s" << std::endl;

  std::ofstream file(output);
  if (!file.is_open()) {
    std::cerr << "Could not open file: " << output << std::endl;
    return 1;
  }

  for (const auto &rank : pagerank) {
    file << rank << std::endl;
  }

  return 0;
}