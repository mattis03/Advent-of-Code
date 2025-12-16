#include "../InputParser.cpp"
#include "../StringReader.cpp"

typedef unsigned long long u64;

int main(void)
{
    InputParser ip("Day09\\input.txt");

    std::vector<std::pair<u64, u64>> corners;
    for (auto& row : ip.get_rows())
    {
        auto numbers = StringReader::extract_numbers<u64>(row);       
        corners.push_back({numbers.at(0), numbers.at(1)});
    }

    u64 max_area = 0;
    for (int i = 0; i < corners.size() - 1; i++)
    {
        for (int j = i + 1; j < corners.size(); j++)
        {
            u64 dx = abs(corners.at(i).first - corners.at(j).first) + 1;
            u64 dy = abs(corners.at(i).second - corners.at(j).second) + 1;
            u64 area = dx * dy;

            max_area = std::max(max_area, area);
        }
    }

    std::cout << max_area << "\n";

    return 0;
}