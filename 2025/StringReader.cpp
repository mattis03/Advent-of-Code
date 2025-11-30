#include <string>
#include <vector>

class StringReader
{
public:
    /**
     * Splits a string into a vector of strings.
     * The string will be split around any character(s) contained within the `delimiters`.
     */
    static std::vector<std::string> split(std::string_view string, std::string_view delimiters)
    {
        std::vector<std::string> split_string;
        
        while (true)
        {
            // Skip leading delimiters
            size_t start = string.find_first_not_of(delimiters);
            if (start == std::string_view::npos)
                break;

            string.remove_prefix(start);

            // Find the next delimiter
            size_t end = string.find_first_of(delimiters);

            split_string.emplace_back(string.substr(0, end));

            // Move input forward
            string.remove_prefix(end == std::string_view::npos ? string.length() : end);
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
    constexpr static std::vector<T> extract_numbers(std::string_view string)
    {
        std::vector<T> numbers;

        int i = 0;

        while (i < string.length())
        {
            // Find the start of the next number
            while (i < string.length() && !std::isdigit(string.at(i)))
                i++;

            // Return if the end of the string has been reached
            if (i == string.length())
                return numbers;

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