#include <chrono>

class StopWatch
{
private:
    inline static std::chrono::high_resolution_clock::time_point start_time, stop_time;
public:
    static void reset(void) { start_time = std::chrono::high_resolution_clock::now(); }

    static void stop(void) { stop_time = std::chrono::high_resolution_clock::now(); }

    static long elapsed_ms(void) { return static_cast<long>(std::chrono::duration_cast<std::chrono::milliseconds>(stop_time - start_time).count()); }

    static long elapsed_us(void) { return static_cast<long>(std::chrono::duration_cast<std::chrono::microseconds>(stop_time - start_time).count()); }
};
