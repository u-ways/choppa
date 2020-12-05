<template>
  <div class="min-h-screen flex flex-col">
    <div class="flex-none">
      <Navbar/>
    </div>
    <div class="flex-grow flex items-stretch">
      <slot></slot>
    </div>
    <div class="flex-none">
      <Footer/>
    </div>
  </div>
</template>

<script>
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
