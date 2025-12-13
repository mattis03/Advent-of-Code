#include "../InputParser.cpp"
#include "../StringReader.cpp"

#include <unordered_map>

std::unordered_map<std::string, unsigned long long> cache;
std::unordered_map<std::string, std::vector<std::string>> connections;

unsigned long long dfs(std::string from, std::string to)
{
    if (from == to) return 1;

    unsigned long long sum = 0;
    for (std::string connection : connections[from])
    {
        if (cache.find(connection) != cache.end())
        {
            sum += cache[connection];
        }
        else
        {
            sum += dfs(connection, to);
        }
    }

    cache.insert({from, sum});

    return sum;
}

unsigned long long find_paths(std::string from, std::string to)
{
    cache.clear();
    return dfs(from, to);
}

int main(void)
{
    InputParser ip("Day11\\input.txt");

    for (std::string_view row : ip.get_rows())
    {
        auto devices = StringReader::split(row, ": ");

        std::string key = devices.at(0);
        std::vector<std::string> values;

        for (int i = 1; i < devices.size(); i++)
        {
            values.push_back(devices.at(i));
        }

        connections.insert({key, values});
    }

    unsigned long long answer_part1 = find_paths("you", "out");
    unsigned long long answer_part2 = find_paths("svr", "fft") * find_paths("fft", "dac") * find_paths("dac", "out") +
                                      find_paths("svr", "dac") * find_paths("dac", "fft") * find_paths("fft", "out");

    std::cout << "Part 1: " << answer_part1 << "\n";
    std::cout << "Part 2: " << answer_part2;

    return 0;
}