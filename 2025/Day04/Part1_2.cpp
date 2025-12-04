#include "../InputParser.cpp"
#include "../StringReader.cpp"

/**
 * Returns true if the roll at (x, y) has less than 4 adjacent rolls.
 * This method assumes that (x, y) is a roll.
 */
bool is_roll_accessible(InputParser& input, int x, int y)
{
    // Find the 3x3 square that surrounds (x, y)
    CharSquare adjacent = input.get_adjacent(x, y);

    int neighbors = -1; // The center roll should always be excluded
    for (char c : adjacent.array)
        if (c == '@')
            neighbors++;

    return neighbors < 4;
}

/**
 * Removes all accessible rolls and returns the number of removed rolls.
 */
int remove_rolls(InputParser& input)
{
    int removed_rolls = 0;

    for (int y = 0; y < input.height(); y++)
    {
        for (int x = 0; x < input.width(); x++)
        {
            if (input.get_char(x, y) == '@' && is_roll_accessible(input, x, y))
            {
                input.set_char('.', x, y);
                removed_rolls++;
            }
        }
    }

    return removed_rolls;
}

int main(void)
{
    InputParser ip("Day04\\input.txt");

    int answer_part1 = 0;
    int answer_part2 = 0;

    /* --- PART 1 --- */

    for (int y = 0; y < ip.height(); y++)
        for (int x = 0; x < ip.width(); x++)
            if (ip.get_char(x, y) == '@' && is_roll_accessible(ip, x, y))
                answer_part1++;



    /* --- PART 2 --- */

    int removed;
    do
    {
        removed = remove_rolls(ip);
        answer_part2 += removed;        
    }
    while(removed > 0);



    std::cout << "Part 1: " << answer_part1 << "\n";
    std::cout << "Part 2: " << answer_part2;

    return 0;
}