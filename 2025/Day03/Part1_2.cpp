#include "../InputParser.cpp"
#include "../StringReader.cpp"

#include <array>

/**
 * Finds the index of the largest digit in the string `s` between the indices `start_index` and `stop_index` (inclusive).
 * If there are multiple instances of the largest digit, the index of the left-most largest digit will be returned.
 */
int largest_digit_index(std::string_view s, int start_index, int stop_index)
{
    // Create a new string_view contatining the substring of interest
    std::string_view substr(s);
    substr.remove_suffix(s.length() - stop_index - 1);
    substr.remove_prefix(start_index);

    // Find the index of the left-most max-value in substr
    int max_index = 0;
    for (int i = 1; i < substr.length(); i++)
        if (substr.at(i) > substr.at(max_index))
            max_index = i;

    return max_index + start_index;
}

int main(void)
{
    InputParser ip("Day03\\input.txt");

    unsigned long long answer_part1 = 0;
    unsigned long long answer_part2 = 0;

    for (std::string bank : ip.get_rows())
    {
        /* --- PART 1 --- */

        // Find the index of the largest digit, left_index cannot be at the end of the string
        int left_index  = largest_digit_index(bank,              0, bank.length() - 2);
        int right_index = largest_digit_index(bank, left_index + 1, bank.length() - 1);

        // Combine the digits into a single number
        answer_part1 += (bank.at(left_index) - '0') * 10 + (bank.at(right_index) - '0');



        /* --- PART 2 --- */

        // Create an array containing the indices of the chosen batteries
        // This array has the following proprty: battery_indices(i) < battery_indices(i + 1)
        std::array<int, 12> battery_indices;

        // Make battery_indices point to the twelve last batteries in the bank
        for (int i = 0; i < battery_indices.size(); i++)
            battery_indices.at(i) = bank.length() - (12 - i);

        for (int i = 0; i < battery_indices.size(); i++)
        {
            // Begin search from the position to the right of the previous battery
            int start_index = (i == 0) ? 0 : battery_indices.at(i - 1) + 1;

            // Search up to the current battery index
            int stop_index = battery_indices.at(i);

            // Set the current battery index to the index of the left-most largest digit
            battery_indices.at(i) = largest_digit_index(bank, start_index, stop_index);
        }

        // Combine the digits into a single number
        unsigned long long bank_joltage = 0;
        for (int i : battery_indices)
            bank_joltage = bank_joltage * 10 + (bank.at(i) - '0');

        answer_part2 += bank_joltage;
    }

    std::cout << "Part 1: " << answer_part1 << "\n";
    std::cout << "Part 2: " << answer_part2;

    return 0;
}