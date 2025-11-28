#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <unistd.h>

class InputParser
{
private:
    const std::string DIRECTORY_BASE = "D:\\Dokument\\VSCodium\\AdventOfCode\\2025\\";

    std::vector<std::string> rows;
    size_t                   max_width;
public:
    InputParser(std::string file_name)
    {
        std::ifstream file(DIRECTORY_BASE + file_name);

        if (!file.is_open()) 
        {
            std::cout << "Failed to read input file";
            exit(1);
        }

        max_width = 0;
        std::string s;
        while (std::getline(file, s))
        {
            rows.push_back(s);

            if (s.length() > max_width)
                max_width = s.length();
        }

        file.close();
    }

    /**
     * Returns the length of the longest row of the input.
     * Mostly useful for rectangularly shaped input.
     */
    int width(void) { return static_cast<int>(max_width); }

    /**
     * Returns the number of rows in the input.
     */
    int height(void) { return static_cast<int>(rows.size()); }

    /**
     * Treats the input as a grid of characters and
     * returns the character at position (x, y).
     */
    char get_char(unsigned int x, unsigned int y) { return rows.at(y).at(x); }

    /**
     * Returns the string at row `y`.
     */
    std::string get_row(unsigned int y) { return rows.at(y); }

    /**
     * Returns a vector where each element
     * consists of one row of the input.
     */
    std::vector<std::string> get_rows(void) { return rows; }
};
