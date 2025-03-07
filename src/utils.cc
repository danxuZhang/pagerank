#include "utils.h"

#include <string>

namespace pagerank {

FileLineIterator::FileLineIterator(const std::string& filename, char comment)
    : has_next_line_(false), comment_char_(comment) {
  file_.open(std::string(filename));
  if (!file_.is_open()) {
    throw std::runtime_error("Could not open file: " + filename);
  }
  ReadNextLine();
}

FileLineIterator::~FileLineIterator() { file_.close(); }

auto FileLineIterator::HasNext() const -> bool { return has_next_line_; }

auto FileLineIterator::Next() -> void { ReadNextLine(); }

auto FileLineIterator::Get() const -> const std::string& {
  return current_line_;
}

auto FileLineIterator::Reset() -> void {
  file_.clear();
  file_.seekg(0);
  ReadNextLine();
}

auto FileLineIterator::ReadNextLine() -> void {
  has_next_line_ = false;
  while (std::getline(file_, current_line_)) {
    // Skip comment lines
    if (!current_line_.empty() && current_line_[0] == comment_char_) {
      continue;
    }
    has_next_line_ = true;
    break;
  }
}

}  // namespace pagerank