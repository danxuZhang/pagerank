#include "pagerank.h"

#include <unordered_set>

#include "utils.h"

namespace pagerank {

auto PageRank(std::string_view path, double d [[maybe_unused]],
              double eps [[maybe_unused]]) -> std::vector<double> {
  FileLineIterator file((std::string(path)));
  std::unordered_set<int> nodes;

  while (file.HasNext()) {
    const auto &line = file.Get();
    const auto [src, dst] = ParseEdge(line);
    nodes.insert(src);
    nodes.insert(dst);
    file.Next();
  }

  const auto n = nodes.size();

  return std::vector<double>(n, 1.0 / n);
}

}  // namespace pagerank