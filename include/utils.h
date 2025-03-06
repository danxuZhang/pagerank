#pragma once

#include <fstream>
#include <string>

namespace pagerank {

inline auto ParseEdge(std::string_view line) -> std::pair<int, int> {
  const auto pos = line.find_first_of(' ');
  const auto src = std::stoi(std::string(line.substr(0, pos)));
  const auto dst = std::stoi(std::string(line.substr(pos + 1)));
  return {src, dst};
}

class FileLineIterator {
 public:
  explicit FileLineIterator(const std::string& filename, char comment = '#');
  FileLineIterator(const FileLineIterator&) = delete;
  FileLineIterator& operator=(const FileLineIterator&) = delete;
  ~FileLineIterator();

  auto HasNext() const -> bool;
  auto Next() -> void;
  auto Get() const -> const std::string&;

 private:
  std::ifstream file_;
  std::string current_line_;
  bool has_next_line_;
  char comment_char_;

  auto ReadNextLine() -> void;
};  // class FileLineIterator

}  // namespace pagerank