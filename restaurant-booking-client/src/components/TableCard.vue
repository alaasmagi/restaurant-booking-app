<script setup lang="ts">
import type { SeatFeature, TableDto } from '@/types'
import { calculateTableSize, FEATURE_ICONS, FEATURE_NAME_MAP } from '@/utils/visual'
import { computed } from 'vue'

const props = defineProps<{
  table: TableDto
  recommended: boolean
  selected: boolean
  gridUnit: number
  dragging?: boolean
  grabMode?: boolean
}>()

const emit = defineEmits<{ (e: 'click', id: string): void }>()

function featureIcon(f: SeatFeature): string {
  return FEATURE_ICONS[f] ?? '•'
}

function featureLabel(f: SeatFeature): string {
  return FEATURE_NAME_MAP[f] ?? f.toLowerCase()
}

const tableSize = computed(() => calculateTableSize(props.table))
</script>

<template>
  <div
    class="table-card"
    :class="{
      available: table.available,
      unavailable: !table.available,
      recommended,
      selected,
      dragging,
      'grab-mode': grabMode,
    }"
    :style="{
      left: `${table.x * gridUnit}px`,
      top: `${table.y * gridUnit}px`,
      width: `${tableSize.width}px`,
      height: `${tableSize.height}px`,
    }"
    :title="`${table.seats} seats · ${table.zone}${table.features.length ? ' · ' + table.features.join(', ') : ''}`"
    @click="table.available && emit('click', table.id)"
  >
    <div class="seats">{{ table.seats }} seats</div>
    <div class="zone">Zone {{ table.zone }}</div>
    <div class="features-row">
      <span v-for="f in table.features" :key="f" class="feat" :title="featureLabel(f)">{{
        featureIcon(f)
      }}</span>
    </div>
    <div v-if="recommended" class="badge">★ Best</div>
  </div>
</template>

<style scoped>
.table-card {
  position: absolute;
  border-radius: 8px;
  padding: 6px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-size: 0.72rem;
  border: 2px solid transparent;
  transition:
    transform 0.1s,
    box-shadow 0.1s;
  user-select: none;
  box-sizing: border-box;
  gap: 2px;
}

.available {
  background: #c7ecd4;
  border-color: #4caf50;
  color: #1a5c25;
}

.unavailable {
  background: var(--color-neutral-bg);
  border-color: #9e9e9e;
  color: var(--color-neutral-text);
  cursor: not-allowed;
  opacity: 0.7;
}

.recommended {
  background: var(--color-accent-light);
  border-color: var(--color-accent);
  color: #7a4800;
  box-shadow: 0 0 12px 2px #ffaa0099;
  z-index: 2;
}

.selected {
  border-color: #1565c0;
  box-shadow: 0 0 10px 2px #1565c099;
  z-index: 3;
}

.available:hover:not(.unavailable):not(.grab-mode) {
  transform: scale(1.08);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.grab-mode {
  cursor: grab;
}

.dragging {
  cursor: grabbing;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
  z-index: 10;
  opacity: 0.9;
}

.seats {
  font-weight: 700;
  font-size: 1rem;
}

.zone {
  font-size: 0.8rem;
  opacity: 0.8;
}

.features-row {
  display: flex;
  gap: 1px;
  flex-wrap: wrap;
  justify-content: center;
}

.feat {
  font-size: 1rem;
}

.badge {
  position: absolute;
  top: -12px;
  right: -12px;
  background: var(--color-accent);
  color: var(--color-surface);
  border-radius: 8px;
  padding: 1px 5px;
  font-size: 0.7rem;
  font-weight: 700;
}
</style>
