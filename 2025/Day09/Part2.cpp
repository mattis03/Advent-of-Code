#include "../InputParser.cpp"
#include "../StringReader.cpp"
#include "../ProgressBar.cpp"

#include <bitset>

typedef struct
{
    int x, y;
} Point;

Point add(Point& p, Point& q) { return {p.x+q.x, p.y+q.y}; }

typedef struct
{
    Point start, end;
} Line;

std::bitset<100000> grid[100000];
void g_reset(int x, int y) { grid[y].reset(x); }
void g_set(int x, int y)   { grid[y].set(x); }
bool g_get(int x, int y)   { return grid[y].test(x); }

std::vector<Point> points_on_circumference(Point c1, Point c2)
{
    std::vector<Point> points;
    Point top_left =     {std::min(c1.x, c2.x), std::min(c1.y, c2.y)};
    Point bottom_right = {std::max(c1.x, c2.x), std::max(c1.y, c2.y)};

    for (int i = top_left.x; i <= bottom_right.x; i++) points.push_back({i,top_left.y});
    for (int i = top_left.x; i <= bottom_right.x; i++) points.push_back({i,bottom_right.y});

    for (int i = top_left.y; i <= bottom_right.y; i++) points.push_back({top_left.x,i});
    for (int i = top_left.y; i <= bottom_right.y; i++) points.push_back({bottom_right.x,i});

    return points;
}

int main(void)
{
    InputParser ip("Day09\\input.txt");

    std::vector<Point> points;
    std::vector<Line> lines;

    for (auto& row : ip.get_rows())
    {
        auto xy = StringReader::extract_numbers<int>(row);
        int x = xy.at(0);
        int y = xy.at(1);
        points.push_back({x, y});
    }

    for (int i = 1; i < points.size(); i++)
    {
        Line l;
        l.start = points.at(i - 1);
        l.end = points.at(i);
        lines.push_back(l);
    }
    Line l;
    l.start = points.at(points.size() - 1);
    l.end = points.at(0);
    lines.push_back(l);

    std::cout << "Resetting all bits in the grid...\n";
    for (int y = 0; y < 100000; y++)
    {
        std::bitset<100000> bs(0);
        grid[y] = bs;
        ProgressBar::display((100 * (y+1) / 100000));
    }

    std::cout << "\n\nDrawing polygon circumference...\n";
    int i = 0;
    for (Line& l : lines)
    {
        // Find line direction
        Point step;
        if (l.start.x == l.end.x)
            step = l.end.y > l.start.y ? Point{0, 1} : Point{0, -1};
        else
            step = l.end.x > l.start.x ? Point{1, 0} : Point{-1, 0};

        Point& p = l.start;
        do
        {
            g_set(p.x, p.y);
            p = add(p, step);
            g_set(p.x, p.y);
        } while (p.x != l.end.x || p.y != l.end.y);

        ProgressBar::display((100 * (++i)) / lines.size());
    }

    std::cout << "\n\nFilling the polygon with BFS...\n";
    std::vector<Point> queue;
/*     queue.push_back(Point{10, 3});
    g_set(10, 3); */
    queue.push_back(Point{97997, 50800});
    g_set(97997, 50800);
    while (queue.size() > 0)
    {
        Point p = queue.at(queue.size() - 1);
        queue.erase(queue.end());

        if (p.x < 0 || p.x >= 100000 || p.y < 0 || p.y >= 100000)
            continue;
            
        Point o_n = Point{0, -1};
        Point o_s = Point{0, 1};
        Point o_w = Point{-1, 0};
        Point o_e = Point{1, 0};

        Point p1 = add(p, o_n);
        Point p2 = add(p, o_s);
        Point p3 = add(p, o_w);
        Point p4 = add(p, o_e);

        if (!g_get(p1.x, p1.y)) { queue.push_back(p1); g_set(p1.x, p1.y); }
        if (!g_get(p2.x, p2.y)) { queue.push_back(p2); g_set(p2.x, p2.y); }
        if (!g_get(p3.x, p3.y)) { queue.push_back(p3); g_set(p3.x, p3.y); }
        if (!g_get(p4.x, p4.y)) { queue.push_back(p4); g_set(p4.x, p4.y); }
    }

    std::cout << "\nCalculating largest area...";
    unsigned long long max_area = 0;
    for (int i = 0; i < points.size() - 1; i++)
    {
        for (int j = i + 1; j < points.size(); j++)
        {
            bool valid = true;
            for (Point p : points_on_circumference(points.at(i), points.at(j)))
            {
                if (!g_get(p.x, p.y))
                {
                    valid = false;
                    break;
                }
            }
            if (!valid) continue;

            unsigned long long dx = abs(points.at(i).x - points.at(j).x) + 1;
            unsigned long long dy = abs(points.at(i).y - points.at(j).y) + 1;

            max_area = std::max(max_area, dx * dy);
        }
    }

    std::cout << max_area;

    return 0;
}