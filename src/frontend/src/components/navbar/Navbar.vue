<template>
    <nav class="py-3">
      <div class="px-5 flex justify-between sm:justify-start">
        <NavbarLink :url="{ name: 'home' }" css="sm:font-bold sm:text-lg">Choppa.app</NavbarLink>
        <IconButton class="sm:hidden hover:bg-gray-200 dark:hover:bg-gray-700"
                    screenReaderText="Open Menu" icon="bars" @click="expandedMenu = true"
        />
        <div :class="expandedMenu ? '' : 'hidden'" class="absolute top-0 left-0 w-full sm:relative sm:block z-10
        sm:z-auto">
          <div class="mx-2 mt-2 p-5 pt-5 rounded-lg sm:rounded-none shadow-lg ring-1 ring-black ring-opacity-5 bg-white
          dark:bg-gray-700 divide-gray-50 sm:flex sm:flex-row sm:gap-4 sm:shadow-none sm:ring-0 sm:bg-transparent
          sm:dark:bg-transparent sm:m-0 sm:p-0 sm:ml-4">
            <div class="flex justify-between mb-2 sm:hidden">
              <ChoppaLogo css="text-2xl text-gray-700 dark:text-gray-300"></ChoppaLogo>
              <IconButton class="sm:hidden hover:bg-gray-200 dark:hover:bg-gray-800"
                          screenReaderText="Close Menu" icon="times" @click="expandedMenu = false"
              />
            </div>
            <div class="flex-grow flex flex-row gap-1">
              <NavbarLink :url="{ name: 'dashboard' }" v-if="isAuthenticated">Dashboard</NavbarLink>
            </div>
            <div class="flex-grow flex flex-row gap-3 sm:justify-end">
              <template v-if="isAuthenticated">
                <div class="font-lg leading-10">
                  Hello, <span class="font-bold">{{authenticatedAccount.name}}</span>
                </div>
              </template>
              <template v-else>
                <NavbarLink :url="{ name: 'login' }">Login</NavbarLink>
                <router-link :to="{ name: 'login' }"
                             class="px-2 font-bold rounded-md leading-10 text-sm duration-200 outline-none ring-0
                           focus:ring-4 transform-gpu transition-transform transition-colors hover:-translate-y-0.5
                           focus:-translate-y-0.5 duration-100 motion-reduce:transition-none"
                             :class="isOnHomePage === true
                           ? [ 'bg-white', 'text-gray-600', 'ring-choppa-light-extra', 'hover:bg-gray-200' ]
                           : [ 'bg-choppa-two', 'text-gray-100', 'ring-indigo-500', 'hover:bg-indigo-500']"
                >
                  Try Now
                </router-link>
              </template>
            </div>
          </div>
        </div>
      </div>
    </nav>
</template>

<script>
import NavbarLink from "@/components/navbar/NavbarLink";
import ChoppaLogo from "@/components/atoms/ChoppaLogo";
import IconButton from "@/components/atoms/buttons/IconButton";
import { mapGetters } from "vuex";

export default {
  name: "Navbar",
  components: {
    IconButton,
    ChoppaLogo,
    NavbarLink,
  },
  props: {
    isOnHomePage: {
      type: Boolean,
      required: false,
      default: false,
    },
  },
  computed: {
    ...mapGetters(["isAuthenticated", "authenticatedAccount"]),
  },
  data() {
    return {
      expandedMenu: false,
    };
  },
};
</script>
