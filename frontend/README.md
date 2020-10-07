Chopper Frontend:
===

Chopper frontend module. It is built using VUE.js framework. (Progressive JavaScript Framework)

#### Commands:

- Project setup: `npm install`
- Compile/hot-reload for development: `npm run serve`
- Compile/minify for production: `npm run build`
- Run unit tests: `npm run test:unit`
- Lint and fixe files: `npm run lint`

See [Configuration Reference][6]

#### Component architecture

Our component architecture follows the atomic design pattern developed by
Brad Frost. The details of this design pattern can be [found here][7].

It is nicely described in VueFront's blog post:
[Finding the perfect component file structure for our VueJS App.][8]

#### Folder structure and component architecture

Our Vue.js structure and architecture is inspired from:

- [VUEX application structure][1]
- [VUEJS vue-hackernews-2.0 application structure][2]
- [@mchandleraz realworld-vue example][3]

The complete structure is documented in a blog post written by Sandoche Adittane:

- [Itnext.io - How to Structure a Vue.js Project][4]

#### Vue CLI

We use Vue CLI (@vue/cli) for managing our configurations, runtime dependency,
plugins and task management. You can find the [latest Vue CLI documentation here][5].

___

[1]:https://vuex.vuejs.org/en/structure.html
[2]:https://github.com/vuejs/vue-hackernews-2.0/tree/master/src
[3]:https://github.com/mchandleraz/realworld-vue/tree/master/src
[4]:https://itnext.io/how-to-structure-a-vue-js-project-29e4ddc1aeeb
[5]:https://cli.vuejs.org/guide/
[6]:https://cli.vuejs.org/config/
[7]:https://bradfrost.com/blog/post/atomic-web-design/
[8]:https://medium.com/@vuefront/finding-the-perfect-component-file-structure-for-out-vuejs-app-b808a69dacac