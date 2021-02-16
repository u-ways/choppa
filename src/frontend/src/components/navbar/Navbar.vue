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
            <NavbarLink :url="{ name: 'dashboard' }">Dashboard</NavbarLink>
            <NavbarLink :url="{ name: 'my-tribes' }">My Tribes</NavbarLink>
          </div>
        </div>
      </div>
    </nav>
</template>

<script>
import NavbarLink from "@/components/navbar/NavbarLink";
import ChoppaLogo from "@/components/atoms/ChoppaLogo";
import IconButton from "@/components/atoms/buttons/IconButton";

export default {
  name: "Navbar",
  components: {
    IconButton,
    ChoppaLogo,
    NavbarLink,
  },
  data() {
    return {
      expandedMenu: false,
    };
  },
};
</script>
