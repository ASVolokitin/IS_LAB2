export interface TicketDTO {
  name: string;
  coordinatesId: number | null;
  personId: number | null;
  eventId: number | null;
  price: string;
  type: string | undefined;
  discount: string;
  number: string;
  refundable: string;
  venueId: number | null;
}
