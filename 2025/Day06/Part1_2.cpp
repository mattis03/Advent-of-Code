#include "../InputParser.cpp"
#include "../StringReader.cpp"

typedef unsigned long long u64;

int main(void)
{
    InputParser ip("Day06\\input.txt");



    /* --- PART 1 --- */

    u64 answer_part1 = 0;
    
    // Store the rows of numbers in a vector
    std::vector<std::vector<u64>> number_rows;
    for (int i = 0; i < ip.height() - 1; i++)
    {
        std::vector<u64> row = StringReader::extract_numbers<u64>(ip.get_row(i));
        number_rows.push_back(row);
    }
    // Store the operators
    std::vector<std::string> operators = StringReader::split(ip.get_row(ip.height() - 1), " ");

    for (int col = 0; col < operators.size(); col++)
    {
        if (operators.at(col) == "*")
        {
            u64 product = 1;
            for (int row = 0; row < number_rows.size(); row++)
            {
                product *= number_rows.at(row).at(col);
            }
            answer_part1 += product;
        }
        else
        {
            u64 sum = 0;
            for (int row = 0; row < number_rows.size(); row++)
            {
                sum += number_rows.at(row).at(col);
            }
            answer_part1 += sum;
        }
    }



    /* --- PART 2 --- */

    u64 answer_part2 = 0;

    int n_start = 0;
    int n_end   = 0;
    do
    {
        // Get the operator at position n_start
        char op = ip.get_char(n_start, ip.height()-1);

        // Move n_end to the next operator (or end of the string)
        do
        {
            n_end++;
        } while (n_end < ip.width() && ip.get_char(n_end, ip.height()-1) == ' ');
    
        // Put all numbers in the column into n_str
        std::string n_str;
        for (int col = n_start; col < n_end; col++)
        {
            // Add the characters in the number from top to bottom
            for (int row = 0; row < ip.height() - 1; row++)
            {
                n_str += ip.get_char(col, row);
            }
            n_str += ' ';   // Separate the numbers by a space
        }

        // Extract all numbers from n_str and add/multiply them together
        if (op == '*')
        {
            u64 product = 1;
            for (u64 n : StringReader::extract_numbers<u64>(n_str))
                product *= n;
            answer_part2 += product;
        }
        else
        {
            u64 sum = 0;
            for (u64 n : StringReader::extract_numbers<u64>(n_str))
                sum += n;
            answer_part2 += sum;
        }

        n_start = n_end;    // Move n_start to the next operator
    } while (n_end < ip.width());



    std::cout << "Part 1: " << answer_part1 << "\n";
    std::cout << "Part 2: " << answer_part2;

    return 0;
}