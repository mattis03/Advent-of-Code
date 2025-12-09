#include "../InputParser.cpp"
#include "../StringReader.cpp"

#include <unordered_map>
#include <algorithm>

typedef long long i64;

class JunctionBox
{
public:
    i64 x, y, z;

    JunctionBox(i64 x_, i64 y_, i64 z_) : x{x_}, y{y_}, z{z_} {}

    i64 square_distance(const JunctionBox& b) const
    {
        i64 dx = x - b.x;
        i64 dy = y - b.y;
        i64 dz = z - b.z;
        return std::abs(dx*dx + dy*dy + dz*dz);
    }
};

typedef struct
{
    int box1;
    int box2;
    i64 length;
} Cable;

class DisjointSetUnion
{
private:
    std::vector<int> parent;
    int group_count;
public:
	DisjointSetUnion(int size)
    {
        // Add *size* number of groups (all of size 1)
        group_count = size;
        for (int i = 0; i < group_count; i++)
        {
            parent.push_back(i);
        }
	}

    /**
     * Finds the parent node of node `a`.
     */
    int find(int a)
    {
        if (parent.at(a) != a)
            parent.at(a) = find(parent.at(a));  // Find this node's new parent
        return parent.at(a);
    }

    /**
     * Adds an edge between the nodes `a` and `b` and merges the two groups together.
     */
	void add_edge(int a, int b)
	{
        // Fins the parents of a and b
        a = find(a);
        b = find(b);

        // Check if they are already part of the same group
        if (a == b) return;

        // Merge the two groups
        parent.at(b) = a;
        group_count--;
	}

    /**
     * Returns the number of individual groups in the set. 
     */
    int number_of_groups(void) const
    {
        return group_count;
    }

    /**
     * Returns a vector contatining the sizes of each group in this set.
     */
    std::vector<int> group_sizes(void)
    {
        std::unordered_map<int, std::vector<int>> groups;

        // Go through all nodes and append them to their corresponding group (key: parent)
        for (int i = 0; i < parent.size(); i++)
        {
            groups[find(i)].push_back(i);
        }

        // Store the sizes of the groups in a vector
        std::vector<int> sizes(group_count);
        for (std::pair<int, std::vector<int>> key_val : groups)
        {
            sizes.push_back(key_val.second.size());
        }

        return sizes;
    }
};

int main(void)
{
    InputParser ip("Day08\\input.txt");

    // Read and store all the junction boxes
    std::vector<JunctionBox> boxes;
    for (std::string_view row : ip.get_rows())
    {
        std::vector<i64> coords = StringReader::extract_numbers<i64>(row);
        JunctionBox box = JunctionBox(coords.at(0), coords.at(1), coords.at(2));
        boxes.push_back(box);
    }

    // Find the distance between all boxes
    std::vector<Cable> cables;
    for (int i = 0; i < boxes.size() - 1; i++)
    {
        for (int j = i + 1; j < boxes.size(); j++) // Avoid duplicates (i<->j ; j<->i)
        {
            JunctionBox& b1 = boxes.at(i);
            JunctionBox& b2 = boxes.at(j);

            Cable distance = { 
                .box1   = i,
                .box2   = j,
                .length = b1.square_distance(b2)
                };

            cables.push_back(distance);
        }
    }



    /* --- PART 1 --- */

    // Sort the list of cables by shortest distance
    std::sort(cables.begin(), cables.end(), [](Cable& a, Cable& b)
    {
        return a.length < b.length;
    });

    // Create a DisjointSetJunion for the list of boxes and connect the 1000 boxes that are closest together
    DisjointSetUnion dsu(boxes.size());
    for (int i = 0; i < 1000; i++)
    {
        Cable& edge = cables.at(i);
		dsu.add_edge(edge.box1, edge.box2);
    }

    // Retreive the sizes of all circuits and sort them by greatest number of nodes
    std::vector<int> sizes = dsu.group_sizes();
    std::sort(sizes.begin(), sizes.end(), std::greater<>());

    // Multiply the 3 largest sizes together
    i64 answer_part1 = sizes.at(0) * sizes.at(1) * sizes.at(2);




    /* --- PART 2 --- */

    // Keep connecting boxes until there's only one circuit left
	Cable last_edge;
    for (int i = 1000; dsu.number_of_groups() > 1; i++)
    {
        last_edge = cables.at(i);
		dsu.add_edge(last_edge.box1, last_edge.box2);
    }

    JunctionBox& b1 = boxes.at(last_edge.box1);
    JunctionBox& b2 = boxes.at(last_edge.box2);
	i64 answer_part2 = b1.x * b2.x;



    std::cout << "Part 1: " << answer_part1 << "\n";
    std::cout << "Part 2: " << answer_part2;
    return 0;
}