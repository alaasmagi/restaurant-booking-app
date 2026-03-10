<script setup lang="ts">
import { RouterView, RouterLink } from 'vue-router'
</script>

<template>
  <header>
    <span class="logo">🍽️ Restaurant Booking</span>
    <nav>
      <RouterLink to="/">Book a Table</RouterLink>
      <RouterLink to="/admin">Admin</RouterLink>
    </nav>
  </header>
  <main>
    <RouterView />
  </main>
</template>

<style>
*,
*::before,
*::after {
  box-sizing: border-box;
}

body {
  margin: 0;
  font-family: 'Segoe UI', Arial, sans-serif;
  background: #f9f3ee;
  color: #2d1a0a;
}

header {
  background: #5c2d0a;
  color: white;
  padding: 12px 24px;
  display: flex;
  align-items: center;
  gap: 24px;
}

.logo {
  font-size: 1.3rem;
  font-weight: 700;
}

nav {
  display: flex;
  gap: 16px;
}

nav a {
  color: #f9d4b0;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.95rem;
  padding-bottom: 2px;
  border-bottom: 2px solid transparent;
}

nav a:hover,
nav a.router-link-active {
  color: white;
  border-bottom-color: #ff9800;
}

main {
  min-height: calc(100vh - 52px);
}
</style>
