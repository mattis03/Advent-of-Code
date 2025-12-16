#include "../InputParser.cpp"
#include "../StringReader.cpp"
#include "../ProgressBar.cpp"
#include "../StopWatch.cpp"

#include <bitset>
#include <array>

typedef std::array<std::bitset<64>, 3> variant_type;

class Shape
{
private:
    std::vector<variant_type> m_variants;
    int m_occupied_area = 0;

    /**
     * Creates a transposed version of `shape`.
     */
    variant_type transpose(variant_type& shape)
    {
        variant_type new_shape;

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                new_shape.at(x).set(y, (shape.at(y).test(x)));
            }
        }

        return new_shape;
    }

    /**
     * Creates a variant of `shape` flipped horizontally.
     */
    variant_type flip(variant_type& shape)
    {
        variant_type new_shape;

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                new_shape.at(y).set(2-x, (shape.at(y).test(x)));
            }
        }

        return new_shape;
    }

    /**
     * Returns true if the shapes `a` and `b` are identical.
     */
    bool equals(variant_type& a, variant_type& b)
    {
        return (a.at(0) == b.at(0)) && (a.at(1) == b.at(1)) && (a.at(2) == b.at(2));
    }

public:
    Shape(std::vector<std::string> shape_rows)
    {
        variant_type base_variant;

        // Parse from string input
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                if (shape_rows.at(y).at(x) == '#')
                {
                    base_variant.at(y).set(x);
                    m_occupied_area++;
                }
            }
        }

        // Generate all variants
        std::vector<variant_type> temp;
        for (int i = 0; i < 4; i++)
        {
            base_variant = transpose(base_variant);
            temp.push_back(base_variant);
            base_variant = flip(base_variant);
            temp.push_back(base_variant);
        }

        // Remove copies of the same variant
        for (variant_type t : temp)
        {
            bool already_exists = false;
            for (variant_type in_vector : m_variants)
            {
                if (equals(t, in_vector))
                {
                    already_exists = true;
                    break;
                }
            }

            if (!already_exists)
            {
                m_variants.push_back(t);
            }
        }
    }

    const variant_type& get_variant(int variant) const
    {
        return m_variants.at(variant);
    }

    int num_variants(void) const
    {
        return m_variants.size();
    }

    constexpr int occupied_area(void) const
    {
        return m_occupied_area;
    }

    void print(int variant)
    {
        variant_type shape = m_variants.at(variant);
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                std::cout << (shape.at(y).test(x) ? "#" : ".") << (x == 2 ? "\n" : "");
            }
        }
    }
};

class Region
{
private:
    std::array<std::bitset<64>, 64> m_grid;
    int m_width;
    int m_height;
    std::vector<Shape>* m_shapes;
    std::vector<int>    m_shape_stash;
    int m_total_placed;
    int m_total_shape_count;

    bool collides(const variant_type& variant, int x, int y) const
    {
        std::bitset<64> result;
        result  = (variant.at(0) << x) & m_grid.at(y + 0);
        result |= (variant.at(1) << x) & m_grid.at(y + 1);
        result |= (variant.at(2) << x) & m_grid.at(y + 2);
        return result != 0;
    }

    void place(const variant_type& variant, int x, int y)
    {
        m_grid.at(y + 0) |= variant.at(0) << x;
        m_grid.at(y + 1) |= variant.at(1) << x;
        m_grid.at(y + 2) |= variant.at(2) << x;
    }

    void remove(const variant_type& variant, int x, int y)
    {
        m_grid.at(y + 0) &= ~(variant.at(0) << x);
        m_grid.at(y + 1) &= ~(variant.at(1) << x);
        m_grid.at(y + 2) &= ~(variant.at(2) << x);
    }

    bool solve_dfs(int place_x, int place_y, int shape_id, int variant_id)
    {
        /**
         *  Complete DFS call:
         * 
         *  1. Place shape at (place_x, place_y), guaranteed to be valid. Also remove the shape from the stash. Return true if the stash is empty.
         * 
         *  2. Go through all combinations of adjacent positions (or all valid (x, y)?), shapes, and variants and add all valid ones to a list.
         * 
         *  3. Iterate through the list and call DFS on them. If any call returns true, this call should immediately return true.
         * 
         *  4. If no call returned true, the loop will exit (all paths from here have been explored). Now, we need to backtrack.
         *     Undo the place() by calling remove(), and putting the shape back into the stash.
         * 
         *  5. Since no path from here found a solution, this call will finally return false;
         */

        // Place shape
        const variant_type& variant = m_shapes->at(shape_id).get_variant(variant_id);
        place(variant, place_x, place_y);
        m_shape_stash.at(shape_id)--;
        m_total_placed++;

        // Check if the stash has been completely emptied
        if (m_total_placed == m_total_shape_count)
            return true;

        // Find all legal paths
        for (int y = -3; y <= 3; y++)
        for (int x = -3; x <= 3; x++)
        {
            int xx = place_x + x;
            int yy = place_y + y;

            // Bounds check
            if (xx < 0 || xx+3 > m_width || yy < 0 || yy+3 > m_height)
                continue;

            // Remove 5 center positions which will always overlap with the previously placed shape
            if ( ((x >= -1 && x <= 1) && y == 0) || ((y >= -1 && y <= 1) && x == 0) )
                continue;

            // Iterate through shapes and variants
            for (int i = 0; i < m_shapes->size(); i++)
            {
                // Skip if there are no shape[i] left
                if (m_shape_stash.at(i) == 0)
                    continue;

                const Shape& shape = m_shapes->at(i);

                // Try all shape variants
                for (int j = 0; j < shape.num_variants(); j++)
                {
                    const variant_type& variant = shape.get_variant(j);
                    // Collision check
                    if (collides(variant, xx, yy))
                        continue;
                    // New state is legal
                    bool result = solve_dfs(xx, yy, i, j);
                    if (result) return true;
                }
            }
        }

        // Erase the placed shape
        remove(variant, place_x, place_y);
        m_shape_stash.at(shape_id)++;
        m_total_placed--;

        return false;
    }

public:
    Region(int width, int height, std::vector<Shape>& shapes, std::vector<int>& shape_count)
    {
        m_width  = width;
        m_height = height;
        m_shapes = &shapes;
        m_shape_stash = shape_count;
        m_total_shape_count = 0;

        for (int n : shape_count)
        {
            m_total_shape_count += n;
        }
    }

    bool solve()
    {
        m_total_placed = 0;

        // Test all shapes and all rotations for the start position
        for (int s = 0; s < m_shapes->size(); s++)
        {
            const Shape& shape = m_shapes->at(s);
            for (int v = 0; v < shape.num_variants(); v++)
            {
                bool result = solve_dfs(0, 0, s, v);
                if (result)
                    return true;
            }
        }

        return false;
    }

    void print(void)
    {
        for (int y = 0; y < m_height; y++)
        {
            for (int x = 0; x < m_width; x++)
            {
                std::cout << (m_grid.at(y).test(x) ? "#" : ".") << (x == m_width-1 ? "\n" : "");
            }
            std::cout << "\n";
        }
    }
};

int main(void)
{
    InputParser ip("Day12\\input.txt");

    std::vector<Shape>  shapes;
    std::vector<Region> regions;

    // Parse shapes
    int row = 1;
    while (ip.get_row(row).find("x") == -1) // Break once the first 'region' line is found
    {
        // Create shape
        Shape shape = Shape( { ip.get_row(row + 0), ip.get_row(row + 1), ip.get_row(row + 2) } );
        shapes.push_back(shape);
        // Skip to the next shape
        row += 5;
    }

    // Parse regions
    for (; row < ip.height(); row++)
    {
        auto numbers = StringReader::extract_numbers<int>(ip.get_row(row));

        // The first two numbers are the size of the region
        int region_width  = numbers.at(0);
        int region_height = numbers.at(1);
        int region_area   = region_height * region_width;

        // The rest of the numbers are the number of presents of each type
        std::vector<int> shape_count;
        for (int i = 0; i < shapes.size(); i++)
        {
            shape_count.push_back(numbers.at(2 + i));
        }

        // Sum up the occupied area of all the presents
        int shape_area = 0;
        for (int i = 0; i < shapes.size(); i++)
        {
            shape_area += shapes.at(i).occupied_area() * shape_count.at(i);
        }
        // Ignore impossible-to-solve regions
        if (shape_area > region_area)
        {
            continue;
        }

        // Create region
        Region region = Region(region_width, region_height, shapes, shape_count);
        regions.push_back(region);
    }



    /* --- PART 1 --- */

    int answer_part1 = 0;
    for (Region& region : regions)
    {
        if (region.solve())
        {
            answer_part1++;
        }
    }



    std::cout << "Part 1: " << answer_part1;

    return 0;
}