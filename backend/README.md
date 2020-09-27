Chopper Backend:
===

Chopper Backend module. It is built using [deno.land][1]. 
(A secure runtime for JavaScript and TypeScript.)

#### Available Commands/Tasks:

We use [Denon][4] as our task runner for Deno.
Please run the command [`denon`][4] to view the list of available commands/tasks.   

**Installing Denon:**

To install simply run:
`deno install -qAf --unstable https://deno.land/x/denon@2.4.0/denon.ts`

Afterwards, make sure you add denon to your path:
`export PATH="/home/u-ways/snap/deno/12/.deno/bin:$PATH"`

#### Folder structure and component architecture

The folder structure and component architecture was inspired from Gonzalo P. 
blog post ["Structure of a NodeJS API Project"][3] that follows similar patterns 
and frameworks to our Deno project.

#### Style guide:

We follow Deno's official style guide. It includes guidelines for things like 
filenames, new feature testing, TODO comments, exports/imports, JSDoc and more.
You can find the complete style guide document by [clicking here][2].

___

[1]:https://deno.land/
[2]:https://deno.land/manual/contributing/style_guide
[3]:https://medium.com/codebase/structure-of-a-nodejs-api-project-cdecb46ef3f8
[4]:https://github.com/denosaurs/denon