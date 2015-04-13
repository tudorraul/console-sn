#Social network demo in the console

## Why I chose Akka?
- it ensures messages to actors are processed in order and so consistency can be inferred from this

## Improvements?
- make user messages lazy load depending on how many the client wants
- make posting messages to followers async by diferring user followers management to a separate child actor