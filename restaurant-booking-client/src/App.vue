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

:root {
  --color-bg: #f9f3ee;
  --color-surface: #ffffff;
  --color-surface-soft: #fff8f2;
  --color-text: #2d1a0a;
  --color-primary: #5c2d0a;
  --color-primary-dark: #6d3410;
  --color-primary-muted: #8b4513;
  --color-accent: #ff9800;
  --color-accent-strong: #ffaa00;
  --color-accent-light: #ffe0a0;
  --color-border: #e8d5c4;
  --color-border-strong: #d6b89a;
  --color-muted: #7a4c2e;
  --color-muted-2: #9a6a4e;
  --color-success: #2e7d32;
  --color-success-bg: #e8f5e9;
  --color-success-border: #a5d6a7;
  --color-danger: #c62828;
  --color-danger-bg: #ffebee;
  --color-danger-border: #ef9a9a;
  --color-neutral-bg: #e0e0e0;
  --color-neutral-text: #757575;
  --radius-sm: 6px;
  --radius-md: 8px;
  --radius-lg: 10px;
  --radius-xl: 12px;
  --radius-xxl: 14px;
  --shadow-md: 0 4px 20px rgba(0, 0, 0, 0.08);
  --shadow-lg: 0 8px 32px rgba(0, 0, 0, 0.2);
}

body {
  margin: 0;
  font-family: 'Segoe UI', Arial, sans-serif;
  background: var(--color-bg);
  color: var(--color-text);
}

header {
  background: var(--color-primary);
  color: var(--color-surface);
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
  color: var(--color-surface);
  border-bottom-color: var(--color-accent);
}

main {
  min-height: calc(100vh - 52px);
}
</style>
