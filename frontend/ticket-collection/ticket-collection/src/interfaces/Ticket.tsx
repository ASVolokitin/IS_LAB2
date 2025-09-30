import { TicketEvent } from "./TicketEvent";
import { Person } from "./Person";
import { Venue } from "./Venue";
import { Coordinates } from "./Сoordinates";

export interface Ticket {
    id: number;
    name: string;
    coordinates: Coordinates;
    creationDate: string;
    person: Person;
    event: TicketEvent;
    price: number;
    type: string;
    discount: number;
    number: number;
    refundable: boolean;
    venue: Venue;
}