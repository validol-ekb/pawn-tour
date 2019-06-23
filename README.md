# Pawn's tour problem

## Problem description

A pawn can move on 10x10 chequerboard horizontally, vertically and diagonally by these rules:
* 3 tiles moving North (N), West (W), South (S) and East (E)
* 2 tiles moving NE, SE, SW and NW
* Moves are only allowed if the ending tile exists on the board
* Starting from initial position, the pawn can visit each cell only once

## Solution notes

* The main aim which I chased is performance, that's why I didn't use 
any additional libraries. 
* It's using [Warnsdorffs rule](https://en.wikipedia.org/wiki/Knight%27s_tour#Warnsdorff's_rule) as
a main algorithm. I added some optimization for situations when tile has several potential next tiles with same amount of moves.
In this situation the best way is to choose tile which is closer to the chessboard's edges. Here is a [code implementation](https://github.com/validol-ekb/pawn-tour/blob/master/src/main/scala/ekb/validol/pawn/tour/calculator/WarnsdorffsCalculator.scala#L40).
* In 95% it works in 100 moves which is equal to cells amount on the board. Solutions for rest situations vary in the interval from 140 to 492.

## Project structure

All project is stored in `ekb.validol.pawn.tour` package. 
The way this application is divided into layers is in compliance with SOLID principles.

* `calculator` package. This part is responsible for path finding. Here you can find the main interface `Calculator`
and the main (and only) implementation `WarnsdorffsCalculator`.
* `config` package. This part is just static config which was implemented as `Config` object. I did it for simplicity, of 
course it isn't production ready solution and for production it's better to use some config solutions like `typesafe config`.
* `input` package. This package is responsible for input data. Here you can find an interface for input date `InputInterface` and 
it's implementation `ConsoleInput`. This implementation works via console prompt and get all information from `Stdin`.
* `output` package. This package is responsible for result visualization. It consists from interface `OutputInterface` and 
it's implementation `ConsoleOutput`. This implementation prints response into `Stdout`.
* `model` package. This package contains all application data abstractions. The most interesting class here is `Chessboard`.
This class is chessboard abstraction which contains calculation state and provides search/validation functionality for calculations.
* `Pathfinder` - is a main controller class which glues all application parts together and provides calculation workflow to the user.

## How to run

You can run it via sbt. Just type `sbt clean run` in the project root and you will see a output
```text
Pawn pathfinder application started
Specify pawn start coordinates separated by a comma (e.g. 6,4) please
```
Then you need to specify start tile's coordinates and press Enter button and in a short moment you will see 
calculation result which looks like this
```text
Pawn pathfinder application started
Specify pawn start coordinates separated by a comma (e.g. 6,4) please 2,3

21 40 11 20 39 10 59 38 09 58 
01 74 23 46 73 24 47 56 25 48 
12 19 62 53 88 61 52 89 60 37 
22 41 00 77 50 55 82 49 08 57 
02 75 97 45 72 96 87 71 26 86 
13 18 63 54 91 78 51 90 83 36 
65 42 15 76 98 32 81 99 07 30 
03 68 92 44 69 95 84 70 27 85 
14 17 64 33 16 79 34 31 80 35 
66 43 04 67 93 05 28 94 06 29 

Iterations count: 100
Calculation time: 29 ms

Would you like to continue calculations?
```
Then you can continue calculations by typing `y` or `yes` or you can quit with `n` or `no` answers.