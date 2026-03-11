<script setup lang="ts">
import { computed, ref } from 'vue'
import type { TableDto } from '@/types'
import TableCard from './TableCard.vue'

const GRID_UNIT = 90

const props = defineProps<{
  tables: TableDto[]
  recommendedIds: string[]
  selectedId: string | null
  zone: string
  draggable?: boolean
}>()

const emit = defineEmits<{
  (e: 'select', id: string): void
  (e: 'move', payload: { id: string; x: number; y: number }): void
}>()

interface DragState {
  tableId: string
  offsetX: number
  offsetY: number
}
const drag = ref<DragState | null>(null)
const posOverride = ref<{ x: number; y: number } | null>(null)

const visibleTables = computed(() =>
  props.zone ? props.tables.filter((t) => t.zone === props.zone) : props.tables,
)

const displayedTables = computed(() =>
  visibleTables.value.map((t) => {
    if (drag.value?.tableId === t.id && posOverride.value) {
      return { ...t, x: posOverride.value.x, y: posOverride.value.y }
    }
    return t
  }),
)

const canvasSize = computed(() => {
  if (!displayedTables.value.length) return { width: 800, height: 500 }
  const maxX = Math.max(...displayedTables.value.map((t) => t.x))
  const maxY = Math.max(...displayedTables.value.map((t) => t.y))
  return {
    width: (maxX + 2) * GRID_UNIT,
    height: (maxY + 2) * GRID_UNIT,
  }
})

function onTableMouseDown(e: MouseEvent, table: TableDto) {
  if (!props.draggable) return
  e.preventDefault()
  const el = e.currentTarget as HTMLElement
  const rect = el.getBoundingClientRect()
  drag.value = {
    tableId: table.id,
    offsetX: e.clientX - rect.left,
    offsetY: e.clientY - rect.top,
  }
  posOverride.value = { x: table.x, y: table.y }
}

function onCanvasMouseMove(e: MouseEvent) {
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
  posOverride.value = { x: newX, y: newY }
}

function onCanvasMouseUp() {
  if (!drag.value) return
  const tableId = drag.value.tableId
  const pos = posOverride.value
  drag.value = null
  posOverride.value = null
  if (pos) {
    emit('move', { id: tableId, x: pos.x, y: pos.y })
  }
}
</script>

<template>
  <div
    class="floor-plan-wrapper"
    :style="{ width: `${canvasSize.width}px`, height: `${canvasSize.height}px` }"
  >
    <div
      class="floor-plan"
      :class="{ 'is-draggable': draggable }"
      @mousemove="draggable ? onCanvasMouseMove($event) : undefined"
      @mouseup="draggable ? onCanvasMouseUp() : undefined"
      @mouseleave="draggable ? onCanvasMouseUp() : undefined"
    >
      <TableCard
        v-for="table in displayedTables"
        :key="table.id"
        :table="table"
        :recommended="recommendedIds.includes(table.id)"
        :selected="selectedId === table.id"
        :grid-unit="GRID_UNIT"
        :dragging="draggable && drag?.tableId === table.id"
        :grab-mode="draggable"
        @click="!draggable && emit('select', $event)"
        @mousedown="draggable ? onTableMouseDown($event, table) : undefined"
      />
    </div>
  </div>
</template>

<style scoped>
.floor-plan-wrapper {
  display: block;
  border: 2px solid #e8d5c4;
  border-radius: 12px;
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
}

.floor-plan.is-draggable {
  user-select: none;
}

.floor-plan {
  position: relative;
  width: 100%;
  height: 100%;
}
</style>
