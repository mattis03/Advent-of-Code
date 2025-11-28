#include "../InputParser.cpp"
#include "../StringReader.cpp"

#include <algorithm>

using namespace std;

int main()
{
    InputParser ip("Day1\\input");
    vector<int> r_list, l_list;

    for (string row : ip.get_rows())
    {
        vector<int> lr = StringReader::extract_numbers<int>(row);
        l_list.push_back(lr[0]);
        r_list.push_back(lr[1]);
    }

    sort(l_list.begin(), l_list.end());
    sort(r_list.begin(), r_list.end());

    int dist_sum = 0;
    for (int i = 0; i < l_list.size(); i++)
        dist_sum += abs(l_list.at(i) - r_list.at(i));

    cout << dist_sum;

    return 0;
}
