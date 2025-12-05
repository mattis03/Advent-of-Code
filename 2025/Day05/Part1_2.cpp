#include "../InputParser.cpp"
#include "../StringReader.cpp"

#include <algorithm>

typedef unsigned long long  u64;
typedef std::pair<u64, u64> range_type;

int main(void)
{
    InputParser ip("Day05\\input.txt");

    // Store all number ranges in this vector
    std::vector<range_type> ranges;

    int i;
    // Iterate through rows until the blank row is found
    for (i = 0; !ip.get_row(i).empty(); i++)
    {
        // Read numbers in this row. (first number=lower bound, second number=upper bound)
        std::vector<u64> range = StringReader::extract_numbers<u64>(ip.get_row(i), true);
        ranges.push_back({range.at(0), range.at(1)});
    }

    i++;    // Skip the blank row

    // Store all available ingredient IDs in this vector
    std::vector<u64> available;
    // Continue iterating through the rest of the input and add the numbers to the available IDs
    for (; i < ip.height(); i++)
    {
        available.push_back(StringReader::extract_numbers<u64>(ip.get_row(i)).at(0));
    }



    /* --- PART 1 --- */

    u64 answer_part1 = 0;
    // Iterate through all IDs. Increment anwser_part1 if it belongs to any range.
    for (u64 id : available)
    {
        for (range_type& range : ranges)
        {
            if (id >= range.first && id <= range.second)
            {
                answer_part1++;
                break;
            }
        }
    }



    /* --- PART 2 --- */

    // Sort the ranges. Lowest lower bound first.
    std::sort(ranges.begin(), ranges.end(), [](auto &a, auto &b)
    {
        return a.first < b.first;
    });

    u64 answer_part2 = 0;

    range_type expand_range = ranges.at(0);
    bool last_range_added = false;

    for (int i = 1; i < ranges.size(); i++)
    {
        u64 this_range_start   = ranges.at(i).first;
        u64 this_range_end     = ranges.at(i).second;
        u64 expand_range_start = expand_range.first;
        u64 expand_range_end   = expand_range.second;

        if (this_range_end <= expand_range_end)         // The entirety of this_range is covered by expand_range
        {
            // this_range does not have any new unique IDs and can be therefore be ignored
        }
        else if (this_range_start <= expand_range_end)  // this_range partially overlaps with expand_range
        {
            // Let expanded_range "consume" this_range by extending the end of expanded_range
            expand_range.second = this_range_end;
        }
        else                                            // this_range and expand_range do not overlap (there's a gap between them)
        {
            // Add the length of the current expand_range to the answer and continue from the next range
            answer_part2 += expand_range.second - expand_range.first + 1;
            expand_range  = ranges.at(i);

            last_range_added = (i == ranges.size()-1);
        }
    }

    // Add the last expand_range if it wasn't added by the loop
    if (!last_range_added)
        answer_part2 += expand_range.second - expand_range.first + 1;



    std::cout << "Part 1: " << answer_part1 << "\n";
    std::cout << "Part 2: " << answer_part2;

    return 0;
}