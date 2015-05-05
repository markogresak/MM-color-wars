# Color Wars

This project was created as a part of [Mathematical Modelling](https://ucilnica.fri.uni-lj.si/course/view.php?id=25&lang=en) course at [Faculty of Computer and Information Science, Ljubljana](http://www.fri.uni-lj.si/en/).

*Color Wars* is a simulation, which takes place on a square, randomly generated field, where each iteration a color changes to a random, not necessarily different, neighbor color. The goal was to prove that chances for a color to win is equal to initial share of that color on the field.

#### View of main window

![Main window](http://i.imgur.com/cfNjaFg.png)

#### Graphs

![Graphs](http://i.imgur.com/VUCLVHU.png)

## Running the project

*Note: This was not tested on Windows.*

1. Install **Java 8** and *latest version* of **[maven](https://maven.apache.org/)**. For viewing live graphs, have [Node.js + npm](https://nodejs.org/) installed.
2. Clone the repository or [download the zip file](https://github.com/markogresak/MM-color-wars/archive/master.zip) and `cd` to the root of this project.
3. To start everything (web server and main program + WebSockets server), run run script [`./start.sh`](./start.sh).
    - To start only main program (+ WebSockets server), run command `mvn compile test`.
    - To start web server, `cd` to `webserver/` and run `npm install && npm run-script start`.
4. To view live graphs open [http://localhost:3000](http://localhost:3000). In order to work, this requires both main and WebSockets server running.
