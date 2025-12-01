#include "../InputParser.cpp"
#include "../StringReader.cpp"

int main(void)
{
    InputParser ip("Day01\\input.txt");
    
    int dial = 50;

    int password_part1 = 0;
    int password_part2 = 0;

    for (std::string r : ip.get_rows())
    {
        int distance = StringReader::extract_numbers<int>(r).at(0);

        for (int i = 0; i < distance; i++)
        {
            dial += (r.at(0) == 'R') ? 1 : -1;

            if (dial == -1)
                dial = 99;
            if (dial == 100)
                dial = 0;

            if (dial == 0)
                password_part2++;
        }

        if (dial == 0)
            password_part1++;
    }

    std::cout << "Part 1: "<< password_part1 << "\n";
    std::cout << "Part 2: "<< password_part2;

    return 0;
}