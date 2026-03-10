<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { fetchTables, setTablePosition } from '@/api'
import type { TableDto } from '@/types'

const GRID_UNIT = 90

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

interface DragState {
  tableId: string
  offsetX: number
  offsetY: number
}
const drag = ref<DragState | null>(null)

const canvasWidth = computed(() => {
  if (!tables.value.length) return 800
  return (Math.max(...tables.value.map((t) => t.x)) + 3) * GRID_UNIT
})
const canvasHeight = computed(() => {
  if (!tables.value.length) return 600
  return (Math.max(...tables.value.map((t) => t.y)) + 3) * GRID_UNIT
})

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

function onMouseDown(e: MouseEvent, table: TableDto) {
  e.preventDefault()
  const el = e.currentTarget as HTMLElement
  const rect = el.getBoundingClientRect()
  drag.value = {
    tableId: table.id,
    offsetX: e.clientX - rect.left,
    offsetY: e.clientY - rect.top,
  }
}

function onMouseMove(e: MouseEvent) {
  if (!drag.value) return
  const container = (e.currentTarget as HTMLElement).getBoundingClientRect()
  const newX = Math.max(
    0,
    Math.round((e.clientX - container.left - drag.value.offsetX) / GRID_UNIT),
  )
  const newY = Math.max(
    0,
    Math.round((e.clientY - container.top - drag.value.offsetY) / GRID_UNIT),
  )
  const t = tables.value.find((t) => t.id === drag.value!.tableId)
  if (t) {
    t.x = newX
    t.y = newY
  }
}

async function onMouseUp() {
  if (!drag.value) return
  const tableId = drag.value.tableId
  drag.value = null
  const t = tables.value.find((t) => t.id === tableId)
  if (!t) return
  savingId.value = t.id
  error.value = null
  try {
    await setTablePosition(t.id, t.x, t.y, adminPassword.value ?? undefined)
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

const FEATURE_ICONS: Record<string, string> = {
  WINDOW: '🪟',
  PRIVATE: '🔒',
  KIDS_CORNER: '🧸',
  ACCESSIBLE: '♿',
  OUTDOOR: '🌿',
  BAR_SEATING: '🍸',
}
function featureIcon(f: string): string {
  return FEATURE_ICONS[f] ?? '•'
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

    <div
      v-if="!loading"
      class="canvas"
      :style="{ width: `${canvasWidth}px`, height: `${canvasHeight}px` }"
      @mousemove="onMouseMove"
      @mouseup="onMouseUp"
      @mouseleave="onMouseUp"
    >
      <div
        v-for="table in tables"
        :key="table.id"
        class="table-block"
        :class="{ dragging: drag?.tableId === table.id, saving: savingId === table.id }"
        :style="{
          left: `${table.x * GRID_UNIT}px`,
          top: `${table.y * GRID_UNIT}px`,
          width: `${GRID_UNIT - 8}px`,
          height: `${GRID_UNIT - 8}px`,
        }"
        @mousedown="onMouseDown($event, table)"
      >
        <div class="seats">{{ table.seats }} 💺</div>
        <div class="zone">{{ table.zone }}</div>
        <div class="features-row">
          <span v-for="f in table.features" :key="f" :title="f">{{ featureIcon(f) }}</span>
        </div>
        <div v-if="savingId === table.id" class="saving-indicator">Saving…</div>
      </div>
    </div>
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

.canvas {
  position: relative;
  overflow: auto;
  background:
    repeating-linear-gradient(
      0deg,
      transparent,
      transparent 88px,
      #e8d5c433 88px,
      #e8d5c433 90px
    ),
    repeating-linear-gradient(
      90deg,
      transparent,
      transparent 88px,
      #e8d5c433 88px,
      #e8d5c433 90px
    ),
    #fdf7f2;
  border: 2px solid #e8d5c4;
  border-radius: 12px;
  user-select: none;
}

.table-block {
  position: absolute;
  background: #c7ecd4;
  border: 2px solid #4caf50;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 0.72rem;
  cursor: grab;
  box-sizing: border-box;
  padding: 4px;
  text-align: center;
  gap: 2px;
  color: #1a5c25;
  transition: box-shadow 0.1s;
}

.table-block.dragging {
  cursor: grabbing;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
  z-index: 10;
  opacity: 0.9;
}

.table-block.saving {
  opacity: 0.6;
}

.seats {
  font-weight: 700;
  font-size: 0.8rem;
}

.zone {
  font-size: 0.6rem;
  opacity: 0.8;
}

.features-row {
  display: flex;
  gap: 1px;
  flex-wrap: wrap;
  justify-content: center;
  font-size: 0.7rem;
}

.saving-indicator {
  position: absolute;
  bottom: 2px;
  font-size: 0.55rem;
  color: #666;
}
</style>
