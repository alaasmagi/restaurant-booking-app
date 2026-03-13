export interface TableDto {
  id: string
  seats: number
  zone: string
  features: string[]
  x: number
  y: number
  available: boolean
}

export interface BookingDto {
  id: string
  tableId: string
  status: string
  customerName: string
  customerPhone: string
  customerEmail: string
  peopleCount: number
  startTime: string
  endTime: string
}

export interface CreateBookingDto {
  tableId: string
  customerName: string
  customerPhone: string
  customerEmail: string
  peopleCount: number
  startTime: string
  endTime: string
}

export interface Filters {
  date: string
  startTime: string
  endTime: string
  peopleCount: number
  zone: string
  features: string[]
}

export interface DragState {
  tableId: string
  offsetX: number
  offsetY: number
}