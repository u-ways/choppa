<template>
    <nav class="py-3 container mx-auto">
      <div class="px-8 flex justify-between sm:justify-start">
        <NavbarLink url="/" css="sm:font-bold sm:text-lg">Choppa.app</NavbarLink>
        <IconButton class="sm:hidden" screenReaderText="Open Menu" icon="bars" @click="expandedMenu = true"/>
        <div class="absolute top-0 left-0 w-full sm:relative sm:block" :class="expandedMenu ? '' : 'hidden'">
          <div class="mx-2 mt-2 p-5 pt-5 rounded-lg shadow-lg ring-1 ring-black ring-opacity-5 bg-white dark:bg-gray-700
          divide-gray-50 sm:flex sm:flex-row sm:gap-4 sm:shadow-none sm:ring-0 sm:bg-transparent sm:dark:bg-transparent
          sm:m-0 sm:p-0 sm:ml-4">
            <div class="flex justify-between mb-2 sm:hidden">
              <ChoppaLogo css="text-3xl text-gray-700 dark:text-gray-300"></ChoppaLogo>
              <button>
                <span class="sr-only">Close menu</span>
                <IconButton screenReaderText="Close Menu" icon="times" @click="expandedMenu = false"/>
              </button>
            </div>
            <NavbarLink url="/external/github">Github</NavbarLink>
            <NavbarLink url="/external/production">Production</NavbarLink>
            <NavbarLink url="/external/staging">Staging</NavbarLink>
          </div>
        </div>
      </div>
    </nav>
</template>

<script>
import NavbarLink from "@/components/navbar/NavbarLink";
import ChoppaLogo from "@/components/atoms/ChoppaLogo";
import IconButton from "@/components/atoms/IconButton";

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
