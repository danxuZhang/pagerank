#pragma once

#include <fstream>

namespace pagerank {

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
};

}  // namespace pagerank