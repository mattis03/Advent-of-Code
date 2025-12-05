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

/**
 * Calculates the maximum achievable joltage of `bank` using `battery_count` number of batteries.
 */
unsigned long long calc_max_joltage(std::string_view bank, int battery_count)
{
    /*
        All batteries start out aligned to the right side of the bank. Example (batteries={a,b,c}):

        Index:  0123456789
        Bank:   9468392152
                       ^^^
                       abc

        The indices of {a,b,c} will be moved in left-to-right order to find the leftmost largest digit.
        {a} will start by finding the first largest digit in the index range 0 to 7: 

        Index:  0123456789
        Bank:   9468392152
                ^       ^^
                a       bc

        {b} will now look for the leftmost largest digit in the range 1 to 8:

        Index:  0123456789
        Bank:   9468392152
                ^    ^   ^
                a    b   c
        
        {c} will now look for the leftmost largest digit in the range 6 to 9:

        Index:  0123456789
        Bank:   9468392152
                ^    ^  ^
                a    b  c

        Joltage = 100a + 10b + c = 995
    */

    int start_search = 0;
    int end_search   = bank.length() - battery_count;

    unsigned long long bank_joltage = 0;
    for (int i = 0; i < battery_count; i++)
    {
        int index = largest_digit_index(bank, start_search, end_search);

        // Add the value of the character to the sum
        char c = bank.at(index);
        bank_joltage = bank_joltage * 10 + (c - '0');

        // The next battery's can look at any digit between its own start position and the current battery's position+1
        start_search = index + 1;
        end_search++;
    }

    return bank_joltage;
}

int main(void)
{
    InputParser ip("Day03\\input.txt");

    unsigned long long answer_part1 = 0;
    unsigned long long answer_part2 = 0;

    for (std::string bank : ip.get_rows())
    {
        /* --- PART 1 --- */
        answer_part1 += calc_max_joltage(bank, 2);

        /* --- PART 2 --- */
        answer_part2 += calc_max_joltage(bank, 12);
    }

    std::cout << "Part 1: " << answer_part1 << "\n";
    std::cout << "Part 2: " << answer_part2;

    return 0;
}