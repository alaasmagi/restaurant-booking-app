<script setup lang="ts">
import type { TableDto } from '@/types'
import { FEATURE_NAME_MAP } from '@/types'

const props = defineProps<{
  table: TableDto
  recommended: boolean
  selected: boolean
  gridUnit: number
  dragging?: boolean
  grabMode?: boolean
}>()

const emit = defineEmits<{ (e: 'click', id: string): void }>()

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

function featureLabel(f: string): string {
  return FEATURE_NAME_MAP[f] ?? f.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase())
}
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
      width: `${((table.seats > 4 ? (table.seats / 4) : 1) * (gridUnit - 8)).toFixed(0)}px`,
      height: `${((table.seats > 4 ? (table.seats / 6) : 1) * (gridUnit - 8)).toFixed(0)}px`,
    }"
    :title="`${table.seats} seats · ${table.zone}${table.features.length ? ' · ' + table.features.join(', ') : ''}`"
    @click="table.available && emit('click', table.id)"
  >
    <div class="seats">{{ table.seats }} 💺</div>
    <div class="zone">{{ table.zone }}</div>
    <div class="features-row">
      <span v-for="f in table.features" :key="f" class="feat" :title="featureLabel(f)">{{ featureIcon(f) }}</span>
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
  background: #e0e0e0;
  border-color: #9e9e9e;
  color: #757575;
  cursor: not-allowed;
  opacity: 0.7;
}

.recommended {
  background: #ffe0a0;
  border-color: #ff9800;
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
  font-size: 0.8rem;
}

.zone {
  font-size: 0.62rem;
  opacity: 0.8;
}

.features-row {
  display: flex;
  gap: 1px;
  flex-wrap: wrap;
  justify-content: center;
}

.feat {
  font-size: 0.72rem;
}

.badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #ff9800;
  color: #fff;
  border-radius: 8px;
  padding: 1px 5px;
  font-size: 0.6rem;
  font-weight: 700;
}
</style>
