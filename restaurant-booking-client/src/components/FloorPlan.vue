<script setup lang="ts">
import { computed } from 'vue'
import type { TableDto } from '@/types'
import TableCard from './TableCard.vue'

const GRID_UNIT = 90

const props = defineProps<{
  tables: TableDto[]
  recommendedIds: string[]
  selectedId: string | null
  zone: string
}>()

const emit = defineEmits<{ (e: 'select', id: string): void }>()

const visibleTables = computed(() =>
  props.zone ? props.tables.filter((t) => t.zone === props.zone) : props.tables,
)

const canvasSize = computed(() => {
  if (!visibleTables.value.length) return { width: 800, height: 500 }
  const maxX = Math.max(...visibleTables.value.map((t) => t.x))
  const maxY = Math.max(...visibleTables.value.map((t) => t.y))
  return {
    width: (maxX + 2) * GRID_UNIT,
    height: (maxY + 2) * GRID_UNIT,
  }
})
</script>

<template>
  <div class="floor-plan-wrapper">
    <div
      class="floor-plan"
      :style="{ width: `${canvasSize.width}px`, height: `${canvasSize.height}px` }"
    >
      <TableCard
        v-for="table in visibleTables"
        :key="table.id"
        :table="table"
        :recommended="recommendedIds.includes(table.id)"
        :selected="selectedId === table.id"
        :grid-unit="GRID_UNIT"
        @click="emit('select', $event)"
      />
    </div>
  </div>
</template>

<style scoped>
.floor-plan-wrapper {
  overflow: auto;
  border: 2px solid #e8d5c4;
  border-radius: 12px;
  max-height: 620px;
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

.floor-plan {
  position: relative;
  min-width: 100%;
  min-height: 100%;
}
</style>
