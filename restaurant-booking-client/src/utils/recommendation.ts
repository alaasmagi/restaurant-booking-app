import type { TableDto } from '@/types'

export const ALL_FEATURES = [
  'WINDOW',
  'PRIVATE',
  'KIDS_CORNER',
  'ACCESSIBLE',
  'OUTDOOR',
  'BAR_SEATING',
] as const

/**
 * Score a single table against the user's desired group size and features.
 *
 * Two components:
 * 1. Seat efficiency (0–1): prefer smallest table that still fits the group.
 *    Score = 1 / (1 + extraSeats)  so a perfect fit scores 1.0.
 * 2. Feature match (0–1): fraction of desired features the table has.
 *    If no features are desired, this component contributes 1.0 (neutral).
 *
 * Combined score is the sum, so max is 2.0 (perfect size + all features match).
 */
export function scoreTable(
  table: TableDto,
  peopleCount: number,
  desiredFeatures: string[],
): number {
  const seatScore = 1 / (1 + (table.seats - peopleCount))
  const matchCount = desiredFeatures.filter((f) => table.features.includes(f)).length
  const featureScore = desiredFeatures.length > 0 ? matchCount / desiredFeatures.length : 1
  return seatScore + featureScore
}

/**
 * Return the top-N recommended tables for the given criteria.
 * Only considers available tables that seat at least peopleCount people.
 */
export function recommendTables(
  tables: TableDto[],
  peopleCount: number,
  desiredFeatures: string[],
  topN = 1,
): TableDto[] {
  return tables
    .filter((t) => t.available && t.seats >= peopleCount)
    .map((t) => ({ table: t, score: scoreTable(t, peopleCount, desiredFeatures) }))
    .sort((a, b) => b.score - a.score)
    .slice(0, topN)
    .map((s) => s.table)
}
