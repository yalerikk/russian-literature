<template>
  <div class="breadcrumbs">
    <template v-for="(item, index) in items" :key="index">
      <span v-if="index > 0" class="separator">/</span>
      <router-link v-if="item.link && index < items.length - 1" :to="item.link" class="crumb link">
        {{ item.label }}
      </router-link>
      <span v-else class="crumb current">{{ item.label }}</span>
    </template>
  </div>
</template>

<script setup>
defineProps(['items'])
</script>

<style scoped>
.breadcrumbs {
  display: flex;
  align-items: center;
  gap: 4px;
  font-family: var(--font-family);
  font-size: 12px;
}
.crumb {
  font-family: 'Rubik', sans-serif;
  color: rgba(144, 41, 35, 0.2);
  text-decoration: none;
}
.crumb.link {
  color: #902923;
}
.crumb.current {
  color: var(--dim-grey, #73706C);
}
.separator {
  font-family: 'Rubik', sans-serif;
  font-weight: 900;
  color: rgba(144, 41, 35, 0.2);
}
</style>