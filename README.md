RegExTester
===========
A tool for testing regular expressions.

Run
---
Just build and start the application with [sbt](https://github.com/harrah/xsbt/wiki)
```$ sbt
$ run
```
Now you have to choose if you want to start the gui view or run the textual application.

For running the lift application you have to start the server with:
```$ sbt
$ container:start
```

To execute all test which are provided with tool, run:
```$ sbt
$ test
```
After doing this, your server runs and you can find the application at: localhost:8080

Usage
---
Just type in your regular expression and a string which should be tested.
Every view(Gui, Tui, Web) provides an about or help screen with additional information.

In the Gui choose the About dialogue in the menu.
In the Tui enter :help or :h for the help screen.
In the web choose also the About button in the menu.
