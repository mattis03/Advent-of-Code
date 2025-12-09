#include <iostream>

class ProgressBar
{
private:
    const static int length = 40;
public:
    static void display(int percentage)
    {
        if (percentage < 0)   percentage = 0;
        if (percentage > 100) percentage = 100;

        std::string out = "\r[";
        for (int i = 0; i < length; i++)
        {
            out += (((100 * (i+1)) / length > percentage) ? " " : "=");
        }
        out += "] " + std::to_string(percentage) + "%";
        std::cout << out;
    }
};