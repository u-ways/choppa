<template>
    <nav class="p-3 flex justify-between">
      <div class="flex flex-row gap-4">
        <NavbarLink :url="{ name: 'home' }" :is-on-home-page="isOnHomePage" css="font-bold sm:text-lg">
          Choppa.app
        </NavbarLink>
        <NavbarLink :url="{ name: 'dashboard' }" :is-on-home-page="isOnHomePage" v-if="isAuthenticated">
          Dashboard
        </NavbarLink>
      </div>
      <div class="flex-grow flex flex-row gap-3 justify-end items-center">
        <template v-if="isAuthenticated">
          <button class="flex flex-row gap-2 items-center outline-none ring-0 focus:outline-none"
                  @click="expandedMenu = !expandedMenu">
            <span class="hidden sm:block font-bold text-sm"
                  :class="[ isOnHomePage ? 'text-gray-200' : 'text-gray-800 dark:text-gray-200' ]">
              {{authenticatedAccount.shortName }}
            </span>
            <Avatar class="w-8 h-8"
                    :class="[ expandedMenu === true ? 'border-2 border-choppa-two' : '' ]"
                    :seed="authenticatedAccount.avatarSeed"
                    :imageUrlOverride="authenticatedAccount.profilePicture"
            />
          </button>
        </template>
        <template v-else>
          <NavbarLink :url="{ name: 'login' }" :is-on-home-page="isOnHomePage">Login</NavbarLink>
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
      <div v-if="isAuthenticated" v-show="expandedMenu">
        <NavbarAccountMenu @hide="expandedMenu = false" :is-on-home-page="isOnHomePage" />
      </div>
    </nav>
</template>

<script>
/* eslint-disable */
import NavbarLink from "@/components/navbar/NavbarLink";
import ChoppaLogo from "@/components/atoms/ChoppaLogo";
import IconButton from "@/components/atoms/buttons/IconButton";
import { mapActions, mapGetters } from "vuex";
import router from "@/config/router";
import Avatar from "@/components/member/Avatar";
import NavbarAccountMenu from "@/components/navbar/NavbarAccountMenu";

export default {
  name: "Navbar",
  components: {
    NavbarAccountMenu,
    Avatar,
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
  }
};
</script>
