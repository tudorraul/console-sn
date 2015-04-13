#Social network demo in the console

## How to use
1/ clone project
2/ install scala and sbt
3/ run `sbt run` to start the app
4/ type in commands, get creative
5/ type `exit` to exit the app

## Why I chose Akka?
- it ensures messages to actors are processed in order and so consistency can be inferred from this

## Improvements?
- make user messages lazy load depending on how many the client wants
- make posting messages to followers async by diferring user followers management to a separate child actor