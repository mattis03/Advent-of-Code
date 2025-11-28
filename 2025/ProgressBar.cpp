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

        std::cout << "\r[";
        for (int i = 0; i < length; i++)
        {
            std::cout << (((100 * (i+1)) / length > percentage) ? " " : "=");
        }
        std::cout << "] " << percentage << "%";
    }
};