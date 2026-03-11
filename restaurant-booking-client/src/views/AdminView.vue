<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchTables, setTablePosition } from '@/api'
import type { TableDto } from '@/types'
import FloorPlan from '@/components/FloorPlan.vue'

const tables = ref<TableDto[]>([])
const loading = ref(false)
const error = ref<string | null>(null)
const savingId = ref<string | null>(null)

const adminPassword = ref<string | null>(sessionStorage.getItem('adminPassword'))
const passwordInput = ref('')
const passwordError = ref(false)

function submitPassword() {
  if (!passwordInput.value.trim()) return
  sessionStorage.setItem('adminPassword', passwordInput.value)
  adminPassword.value = passwordInput.value
  passwordError.value = false
  load()
}

async function load() {
  loading.value = true
  error.value = null
  try {
    tables.value = await fetchTables()
  } catch {
    error.value = 'Could not load tables. Make sure the backend is running.'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (adminPassword.value !== null) load()
})

async function onTableMove({ id, x, y }: { id: string; x: number; y: number }) {
  const t = tables.value.find((t) => t.id === id)
  if (!t) return
  t.x = x
  t.y = y
  savingId.value = id
  error.value = null
  try {
    await setTablePosition(id, x, y, adminPassword.value ?? undefined)
  } catch (e: any) {
    if (e?.response?.status === 401) {
      sessionStorage.removeItem('adminPassword')
      adminPassword.value = null
      passwordInput.value = ''
      passwordError.value = true
    }
    error.value = 'Failed to save table position.'
  } finally {
    savingId.value = null
  }
}
</script>

<template>
  <div class="admin-view">
    <div v-if="adminPassword === null" class="password-gate">
      <div class="password-card">
        <h2>Admin Access</h2>
        <p>Enter the admin password to access the table layout editor.</p>
        <div v-if="passwordError" class="alert error">Incorrect password. Please try again.</div>
        <form @submit.prevent="submitPassword" class="password-form">
          <input
            v-model="passwordInput"
            type="password"
            placeholder="Password"
            class="password-input"
            autofocus
          />
          <button type="submit" class="password-btn">Enter</button>
        </form>
      </div>
    </div>

    <template v-else>
    <h1>Admin — Table Layout Editor</h1>
    <p class="hint">
      Drag tables to reposition them. Positions are saved to the backend on release.
    </p>

    <div v-if="error" class="alert error">{{ error }}</div>
    <div v-if="loading" class="loading">Loading tables…</div>

    <FloorPlan
      v-if="!loading"
      :tables="tables"
      :recommended-ids="[]"
      :selected-id="savingId"
      zone=""
      :draggable="true"
      @move="onTableMove"
    />
    </template>
  </div>
</template>

<style scoped>
.admin-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 16px;
}

.password-gate {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.password-card {
  background: #fff8f2;
  border: 1px solid #e8d5c4;
  border-radius: 16px;
  padding: 40px 48px;
  text-align: center;
  max-width: 360px;
  width: 100%;
  box-shadow: 0 4px 24px rgba(92, 45, 10, 0.08);
}

.password-card h2 {
  color: #5c2d0a;
  margin-bottom: 8px;
}

.password-card p {
  color: #7a4c2e;
  font-size: 0.9rem;
  margin-bottom: 20px;
}

.password-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.password-input {
  padding: 10px 14px;
  border: 1px solid #cbb99a;
  border-radius: 8px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.2s;
}

.password-input:focus {
  border-color: #a0522d;
}

.password-btn {
  padding: 10px;
  background: #a0522d;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}

.password-btn:hover {
  background: #7a3a1a;
}

h1 {
  color: #5c2d0a;
  margin-bottom: 4px;
}

.hint {
  color: #7a4c2e;
  font-size: 0.85rem;
  margin-bottom: 16px;
}

.loading {
  padding: 60px;
  text-align: center;
  color: #7a4c2e;
}

.alert {
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 12px;
  font-size: 0.9rem;
}

.error {
  background: #ffebee;
  color: #c62828;
  border: 1px solid #ef9a9a;
}


</style>
