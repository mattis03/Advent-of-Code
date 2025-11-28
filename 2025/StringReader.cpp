#include <string>
#include <vector>
#include <iostream>

class StringReader
{
public:
    /**
     * Splits a string into a vector of strings. The string will be split
     * around any character(s) contatined within the `delimiters`.
     */
    static std::vector<std::string> split(const std::string& string, const std::string& delimiters)
    {
        std::string remaining = string + delimiters.at(0); // Add a delimiter at the end to make string separation easier
        std::vector<std::string> split_string;

        while (true)
        {
            // Remove the delimiter characters from the front of the string
            size_t delim_count = remaining.find_first_not_of(delimiters);
            if (delim_count == -1) break;
            remaining.erase(0, delim_count);

            // Find the index of the next delimiter character and add a string to the split string
            size_t delim_index = remaining.find_first_of(delimiters);
            split_string.push_back(remaining.substr(0, delim_index));
            // Remove the word from the remaining string
            remaining.erase(0, delim_index);
        }

        return split_string;
    }

    /**
     * Splits a string in two parts at a specific position.
     */
    static std::pair<std::string, std::string> split_at(const std::string& string, int index)
    {
        std::pair<std::string, std::string> pair;
        pair.first  = string.substr(0, index);
        pair.second = string.substr(index, string.length() - index);
        return pair;
    }

    /**
     * Parses all numbers in a string and stores them in a vector.
     */
    template<typename T>
    constexpr static std::vector<T> extract_numbers(const std::string& string)
    {
        std::vector<T> numbers;

        int i = 0;

        while (i < string.length())
        {
            // Find the start of the next number
            while (i < string.length() && !std::isdigit(string.at(i))) i++;

            // Return if the end of the string has been reached
            if (i == string.length()) return numbers;

            // Check signedness
            int is_negative = (i > 0 && string.at(i - 1) == '-');

            // Parse the number
            T value = 0;
            while (i < string.length() && std::isdigit(string.at(i)))
            {
                value = value * 10 + (string.at(i) - '0');
                i++;
            }

            numbers.push_back(is_negative ? -value : value);
        }

        return numbers;
    }
};