#include "../InputParser.cpp"
#include "../StringReader.cpp"

typedef unsigned long long u64;

int main(void)
{
    InputParser ip("Day07\\input.txt");

    u64 answer_part1 = 0;
    u64 answer_part2 = 0;

    // Create a 2D grid of integer values to keep track of the number of beams that has passed through each (x, y) position.
    std::vector<std::vector<u64>> beam_count(ip.height(), std::vector<u64> (ip.width(), 0));

    // Find the position of S
    int s_y = 0;
    int s_x = ip.get_row(s_y).find('S');

    // Mark S as the first beam
    beam_count.at(s_y).at(s_x) = 1;

    for (int y = s_y + 1; y < ip.height(); y++)
    {
        for (int x = 0; x < ip.width(); x++)
        {
            // Find out how many beams passed through the cell above this cell (x, y)
            u64 beams_above = beam_count.at(y - 1).at(x);

            // No action needs to be taken for this cell (x, y) if there are no beams above it
            if (beams_above == 0)
                continue;

            if (ip.get_char(x, y) != '^')
            {
                // The beams will continue moving down in a straight line
                beam_count.at(y).at(x) += beams_above;
            }
            else
            {
                // Make the beams go both ways around the splitter
                beam_count.at(y).at(x + 1) += beams_above;
                beam_count.at(y).at(x - 1) += beams_above;

                /* --- PART 1 --- */
                answer_part1++; // Count the number of splitters that split at least one beam
            }
        }
    }

    /* --- PART 2 --- */
    for (u64 beams : beam_count.at(ip.height() - 1))
    {
        answer_part2 += beams;
    }

    std::cout << answer_part1 << "\n"; 
    std::cout << answer_part2;

    return 0;
}