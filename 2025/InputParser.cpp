#include <vector>
#include <string>
#include <iostream>
#include <fstream>

/**
 * This struct represents a 3x3 grid of chars. The elements can be accessed individually,
 * or by indexing into the `array` member in which characters are stored left to right, top to bottom.
 * ( array[0]==top_left ; array[8]==bot_right )
 */
typedef union
{
    char array[9];

    struct
    {
        char top_left, top_center, top_right,
             mid_left, mid_center, mid_right,
             bot_left, bot_center, bot_right;
    };
} CharSquare;

class InputParser
{
private:
    const std::string DIRECTORY_BASE = "D:\\Dokument\\VSCodium\\AdventOfCode\\2025\\";

    std::vector<std::string> rows;
    size_t                   max_width;
public:
    InputParser(const std::string& file_name)
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
            max_width = std::max(max_width, s.length());
        }

        file.close();
    }

    /**
     * Returns the length of the longest row of the input. Mostly useful for rectangularly shaped input.
     */
    int width(void) const { return max_width; }

    /**
     * Returns the number of rows in the input.
     */
    int height(void) const { return rows.size(); }

    /**
     * Treats the input as a grid of characters and returns the character at position (x, y).
     */
    char get_char(int x, int y) const { return rows.at(y).at(x); }

    /**
     * Treats the input as a grid of characters and sets the character at position (x, y) to `c`.
     */
    void set_char(char c, int x, int y) { rows.at(y).at(x) = c; }

    /**
     * Returns a 3x3 square of characters centered around the point (x, y).
     * Adjacent elements to (x, y) that are out of range will have their character value set to 0.
     */
    CharSquare get_adjacent(int x, int y) const
    {
        CharSquare square = {0};

        for (int i = 0; i < 9; i++)
        {
            try
            {
                square.array[i] = get_char(x + i%3 - 1, y + i/3 - 1);
            }
            catch(const std::out_of_range& e) { /* The character will remain 0 if (x, y) is out of range */ }
        }

        return square;
    }

    /**
     * Returns a reference to the string at row `y`.
     */
    std::string& get_row(int y) { return rows.at(y); }

    /**
     * Returns a string vector containing all rows.
     */
    const std::vector<std::string>& get_rows(void) const { return rows; }
};
