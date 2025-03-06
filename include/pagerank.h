#pragma once

#include <string_view>
#include <vector>

namespace pagerank {

/**
 * @brief Compute the PageRank of a graph
 * @param
 * @param damping_factor Damping factor
 * @param epsilon Convergence criterion
 * @return std::vector<double> PageRank values (sorted by node ID)
 */
auto PageRank(std::string_view path, double damping_factor, double epsilon)
    -> std::vector<double>;

}  // namespace pagerank