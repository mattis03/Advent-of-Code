#include "../InputParser.cpp"
#include "../StringReader.cpp"

typedef unsigned long long u64;

int main(void)
{
    InputParser ip("Day02\\input.txt");

    u64 answer_part1 = 0;
    u64 answer_part2 = 0;

    std::vector<u64> numbers = StringReader::extract_numbers<u64>(ip.get_row(0), true);

    for (int i = 0; i < numbers.size(); i+=2)
    {
        u64 start = numbers.at(i);
        u64 end   = numbers.at(i + 1);

        for (u64 j = start; j <= end; j++)
        {
            // Convert number to string
            std::string number_string = std::to_string(j);

            // Extract the first half of the string
            std::string_view first_half(number_string);
            first_half.remove_suffix(number_string.length() / 2);
            // Check if the first half appears exactly two times in the string
            if (StringReader::count_occurences(number_string, first_half) == 2)
                answer_part1 += j;

            // Test using the first k characters as a repeating pattern
            for (int k = 1; k <= number_string.length() / 2; k++)
            {
                // The length of the number-string must be divisible by the pattern length
                if (number_string.length() % k != 0)
                    continue;

                // Retrieve the first k characters from number_string to use as a pattern
                std::string_view pattern(number_string);
                pattern.remove_suffix(number_string.length() - k);

                int expected_pattern_count = number_string.length() / pattern.length();
                int actual_pattern_count   = StringReader::count_occurences(number_string, pattern);

                if (actual_pattern_count == expected_pattern_count)
                {
                    answer_part2 += j;
                    // No need to check larger pattern sizes at this point 
                    break;
                }
            }
        }
    }

    std::cout << "Part 1: " << answer_part1 << "\n";
    std::cout << "Part 2: " << answer_part2;

    return 0;
}