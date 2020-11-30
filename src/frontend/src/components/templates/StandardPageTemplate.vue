<template>
  <div class="h-full flex flex-col">
    <Navbar/>
    <div class="flex-grow">
      <slot></slot>
    </div>
    <Footer/>
  </div>
</template>

<script>
/* eslint-disable */
import Navbar from "@/components/navbar/Navbar";
import Footer from "@/components/footer/Footer";

export default {
  name: "StandardPageTemplate",
  components: {
    Footer,
    Navbar,
  },
};
</script>
