import sys
from z3 import *

input = ''
with open('input.txt', 'r') as f:
  input = f.readlines()

def calc_minimum_presses_lights(buttons: list[str], lights: str) -> int:
  queue   = [(0, 0)] # (state, presses): 0 button presses, starts with all lights off
  visited = { 0 }    # Visited states

  while len(queue) > 0:
    state, presses = queue.pop(0)

    for b in buttons:
      next_state, next_presses = state^b, presses+1

      if (next_state == lights):
        return next_presses

      if next_state not in visited:
        queue.append((next_state, next_presses))
        visited.add(next_state)
      
  exit(1)

def calc_minimum_presses_joltage(buttons: list[str], joltages: list[int]) -> int:
  goal = Int('goal')
  goal_rhs = 0
  constraint_lhs = [0 for x in joltages]
  solver = Optimize()
  for i, button in enumerate(buttons):
    b = Int(f'b{i}')
    solver.add(b >= 0)
    goal_rhs += b
    for x in button:
      constraint_lhs[x] += b
  constraints = [lhs == rhs for lhs, rhs in zip(constraint_lhs, joltages)]
  solver.add(goal == goal_rhs)
  for c in constraints:
    solver.add(c)
  solver.minimize(goal)
  solver.check()
  model = solver.model()
  return solver.model()[goal].as_long()



def to_button_mask(button: str):
  numbers = [int(n) for n in button[1:len(button)-1].split(',')]
  m = 0
  for i in numbers:
    m |= 1 << i
  return m

def to_light_mask(lights: str):
  m = 0
  for i in range(len(lights)):
    if lights[i] == '#':
      m |= 1 << i
  return m


def ints(text):
  return [int(x) for x in text[1:len(text)-1].split(',')]

answer_part1, answer_part2 = 0, 0
for l in input:
  seg      = l.split(' ')
  lights   = to_light_mask(seg[0][1:len(seg[0])-1])
  buttons  = [to_button_mask(b) for b in seg if b[0] == '(']
  joltages = [int(j) for j in seg[len(seg)-1][seg[len(seg)-1].find('{')+1:seg[len(seg)-1].find('}')].split(',')]

  answer_part1 += calc_minimum_presses_lights(buttons, lights)
  answer_part2 += calc_minimum_presses_joltage([ints(x) for x in seg[1:len(seg)-1]], joltages)

print(f'Part 1: {answer_part1}')
print(f'Part 2: {answer_part2}')