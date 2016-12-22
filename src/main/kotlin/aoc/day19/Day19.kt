package aoc.day19

/**
* Project: AdventOfCode2016
* Created by KoxAlen on 19/12/2016.
*/

fun f(n: Int): Int {
    var pow3 = 1
    while ((3 * pow3)<= n) pow3 *= 3
    if (n == pow3) return n
    return n - pow3 + (n - 2 * pow3).coerceAtLeast(0)
}

fun main(args: Array<String>) {
    val input = 3004953 //Puzzle input

    // Josephus problem for k = 2
    println("[Part 1] Remaining elf: ${2 * (input - Integer.highestOneBit(input)) + 1}")
//    println("[Part 2] Remaining elf: ${solve(input)}")
    println("[Part 2] Remaining elf: ${f(input)}")
}

/*
Deriving f()
 */
fun solve(n: Int): Int {
    var prev = 2
    for (i in 4..n) {
        prev += 1
        prev %= i-1
        if (prev >= i/2) prev += 1
    }
    return prev
}
/*
1 : 1 pow3 = 1; n == pow3  -> n = 1 (Case 1)
2 : 1 pow3 = 1; n <= 2*pow3-> n(2)-pow3 = 1 (Case 2)
3 : 3 pow3 = 3; n == pow3  -> n = 3 (Case 1)
4 : 1 pow3 = 3; n < 2*pow3 -> n(4)-pow3 = 1 (Case 2)
5 : 2 pow3 = 3; n < 2*pow3 -> n(5)-pow3 = 2 (Case 2)
6 : 3 pow3 = 3; n <= 2*pow3-> n(6)-pow3 = 3 (Case 2)
7 : 5 pow3 = 3; n > 2*pow3 -> n(7)-pow3+(n-2*pow3) = 5 (Case 3)
8 : 7 pow3 = 3; n > 2*pow3 -> n(7)-pow3+(n-2*pow3) = 7 (Case 3)
9 : 9   case 1 base
10 : 1  case 2
11 : 2  case 2
12 : 3  case 2
13 : 4  case 2
14 : 5  case 2
15 : 6  case 2
16 : 7  case 2
17 : 8  case 2
18 : 9  case 2 <= 2*pow3
19 : 11 case 3
20 : 13 case 3
21 : 15 case 3
22 : 17 case 3
23 : 19 case 3
24 : 21 case 3
25 : 23 case 3
26 : 25 case 3
27 : 27 case 1 base
28 : 1
29 : 2
30 : 3
31 : 4
32 : 5
33 : 6
34 : 7
35 : 8
36 : 9
37 : 10
38 : 11
39 : 12
40 : 13
41 : 14
42 : 15
43 : 16
44 : 17
45 : 18
46 : 19
47 : 20
48 : 21
49 : 22
50 : 23
51 : 24
52 : 25
53 : 26
54 : 27
55 : 29
56 : 31
57 : 33
58 : 35
59 : 37
60 : 39
61 : 41
62 : 43
63 : 45
64 : 47
65 : 49
66 : 51
67 : 53
68 : 55
69 : 57
70 : 59
71 : 61
72 : 63
73 : 65
74 : 67
75 : 69
76 : 71
77 : 73
78 : 75
79 : 77
80 : 79
81 : 81
82 : 1
83 : 2
84 : 3
85 : 4
86 : 5
87 : 6
88 : 7
89 : 8
90 : 9
91 : 10
92 : 11
93 : 12
94 : 13
95 : 14
96 : 15
97 : 16
98 : 17
99 : 18
100 : 19
 */
fun dump(n: Int = 100) {
    for (i in 1..n)
        println("$i : ${solve(i)+1}")
}