export type BookingStatus = 'ACTIVE' | 'CANCELLED'

export type SeatFeature =
  | 'WINDOW'
  | 'PRIVATE'
  | 'KIDS_CORNER'
  | 'ACCESSIBLE'
  | 'OUTDOOR'
  | 'BAR_SEATING'

export interface TableDto {
  id: string
  seats: number
  zone: string
  features: SeatFeature[]
  x: number
  y: number
  available: boolean
}

export interface BookingDto {
  id: string
  tableId: string
  status: BookingStatus
  customerName: string
  customerPhone: string
  customerEmail: string
  peopleCount: number
  preferences: SeatFeature[]
  startTime: string
  endTime: string
}

export interface CreateBookingDto {
  tableId: string
  customerName: string
  customerPhone: string
  customerEmail: string
  peopleCount: number
  preferences: SeatFeature[]
  startTime: string
  endTime: string
}

export interface Filters {
  date: string
  startTime: string
  endTime: string
  peopleCount: number
  zone: string
  features: SeatFeature[]
}

export interface DragState {
  tableId: string
  offsetX: number
  offsetY: number
}
