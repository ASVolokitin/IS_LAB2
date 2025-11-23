package com.ticketis.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ticketis.app.model.enums.Color;
import com.ticketis.app.model.enums.Country;
import com.ticketis.app.model.enums.TicketType;
import com.ticketis.app.model.enums.VenueType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportValidator {

    private final Validator validator;
    
    public <T> List<String> validateEntity(T entity) {
        List<String> errors = new ArrayList<>();
        
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        for (ConstraintViolation<T> violation : violations) {
            String error = String.format("%s: %s", 
                violation.getPropertyPath().toString(), 
                violation.getMessage());
            errors.add(error);
            log.warn("Validation error: {}", error);
        }
        
        return errors;
    }

    public List<String> validateCoordinates(JsonNode coordinatesNode) {
        List<String> errors = new ArrayList<>();
        
        if (coordinatesNode == null || !coordinatesNode.isObject()) {
            errors.add("Coordinates must be an object");
            return errors;
        }
        
        JsonNode xNode = coordinatesNode.get("x");
        JsonNode yNode = coordinatesNode.get("y");
        
        if (xNode == null || !xNode.isInt()) {
            errors.add("Coordinates.x must be an integer");
        } else {
            int x = xNode.asInt();
            if (x < -200) {
                errors.add("Coordinates.x must be >= -200, got: " + x);
            }
        }
        
        if (yNode == null || !yNode.isNumber()) {
            errors.add("Coordinates.y must be a number");
        } else {
            double y = yNode.asDouble();
            if (y < -4) {
                errors.add("Coordinates.y must be >= -4, got: " + y);
            }
        }
        
        return errors;
    }

    public List<String> validateLocation(JsonNode locationNode) {
        List<String> errors = new ArrayList<>();
        
        if (locationNode == null || locationNode.isNull()) {
            return errors;
        }
        
        if (!locationNode.isObject()) {
            errors.add("Location must be an object");
            return errors;
        }
        
        JsonNode xNode = locationNode.get("x");
        JsonNode zNode = locationNode.get("z");
        
        if (xNode == null || !xNode.isNumber()) {
            errors.add("Location.x must be a number");
        }
        
        if (zNode == null || !zNode.isNumber()) {
            errors.add("Location.z must be a number");
        }
        
        return errors;
    }

    public List<String> validatePerson(JsonNode personNode) {
        List<String> errors = new ArrayList<>();
        
        if (personNode == null || personNode.isNull()) {
            return errors;
        }
        
        if (!personNode.isObject()) {
            errors.add("Person must be an object");
            return errors;
        }
        
        JsonNode eyeColorNode = personNode.get("eyeColor");
        JsonNode hairColorNode = personNode.get("hairColor");
        JsonNode passportIDNode = personNode.get("passportID");
        
        if (eyeColorNode == null || !eyeColorNode.isTextual()) {
            errors.add("Person.eyeColor must be a string");
        } else {
            try {
                Color.valueOf(eyeColorNode.asText().toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.add("Person.eyeColor must be one of: GREEN, BLUE, YELLOW, WHITE");
            }
        }
        
        if (hairColorNode == null || !hairColorNode.isTextual()) {
            errors.add("Person.hairColor must be a string");
        } else {
            try {
                Color.valueOf(hairColorNode.asText().toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.add("Person.hairColor must be one of: GREEN, BLUE, YELLOW, WHITE");
            }
        }
        
        if (passportIDNode == null || !passportIDNode.isTextual()) {
            errors.add("Person.passportID must be a non-empty string");
        } else {
            String passportID = passportIDNode.asText();
            if (passportID.isBlank()) {
                errors.add("Person.passportID cannot be blank");
            }
            if (passportID.length() > 29) {
                errors.add("Person.passportID must be <= 29 characters, got: " + passportID.length());
            }
        }
        
        JsonNode nationalityNode = personNode.get("nationality");
        if (nationalityNode != null && nationalityNode.isTextual()) {
            try {
                Country.valueOf(nationalityNode.asText().toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.add("Person.nationality must be one of: RUSSIA, USA, CHINA, ITALY");
            }
        }
        
        JsonNode locationNode = personNode.get("location");
        if (locationNode != null && !locationNode.isNull()) {
            errors.addAll(validateLocation(locationNode));
        }
        
        return errors;
    }

    public List<String> validateEvent(JsonNode eventNode) {
        List<String> errors = new ArrayList<>();
        
        if (eventNode == null || eventNode.isNull()) {
            return errors;
        }
        
        if (!eventNode.isObject()) {
            errors.add("Event must be an object");
            return errors;
        }
        
        JsonNode nameNode = eventNode.get("name");
        JsonNode descriptionNode = eventNode.get("description");
        
        if (nameNode == null || !nameNode.isTextual() || nameNode.asText().isBlank()) {
            errors.add("Event.name must be a non-empty string");
        }
        
        if (descriptionNode == null || !descriptionNode.isTextual()) {
            errors.add("Event.description must be a non-empty string");
        }
        
        return errors;
    }

    public List<String> validateVenue(JsonNode venueNode) {
        List<String> errors = new ArrayList<>();
        
        if (venueNode == null || !venueNode.isObject()) {
            errors.add("Venue must be an object");
            return errors;
        }
        
        JsonNode nameNode = venueNode.get("name");
        JsonNode capacityNode = venueNode.get("capacity");
        
        if (nameNode == null || !nameNode.isTextual() || nameNode.asText().isBlank()) {
            errors.add("Venue.name must be a non-empty string");
        }
        
        if (capacityNode == null || !capacityNode.isInt()) {
            errors.add("Venue.capacity must be a positive integer");
        } else {
            int capacity = capacityNode.asInt();
            if (capacity <= 0) {
                errors.add("Venue.capacity must be > 0, got: " + capacity);
            }
        }
        
        JsonNode typeNode = venueNode.get("type");
        if (typeNode != null && typeNode.isTextual()) {
            try {
                VenueType.valueOf(typeNode.asText().toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.add("Venue.type must be one of: PUB, LOFT, OPEN_AREA, MALL");
            }
        }
        
        return errors;
    }

    public List<String> validateTicket(JsonNode ticketNode) {
        List<String> errors = new ArrayList<>();
        
        if (ticketNode == null || !ticketNode.isObject()) {
            errors.add("Ticket must be an object");
            return errors;
        }
        
        JsonNode nameNode = ticketNode.get("name");
        if (nameNode == null || !nameNode.isTextual() || nameNode.asText().isBlank()) {
            errors.add("Ticket.name must be a non-empty string");
        }
        
        JsonNode coordinatesNode = ticketNode.get("coordinates");
        if (coordinatesNode == null && ticketNode.get("coordinatesId") == null) {
            errors.add("Ticket must have either coordinates object or coordinatesId");
        } else if (coordinatesNode != null) {
            errors.addAll(validateCoordinates(coordinatesNode));
        }
        
        JsonNode priceNode = ticketNode.get("price");
        if (priceNode == null || !priceNode.isNumber()) {
            errors.add("Ticket.price must be a positive number");
        } else {
            long price = priceNode.asLong();
            if (price <= 0) {
                errors.add("Ticket.price must be > 0, got: " + price);
            }
        }
        
        JsonNode discountNode = ticketNode.get("discount");
        if (discountNode != null && discountNode.isNumber()) {
            double discount = discountNode.asDouble();
            if (discount <= 0 || discount > 100) {
                errors.add("Ticket.discount must be between 0 and 100, got: " + discount);
            }
        }
        
        JsonNode numberNode = ticketNode.get("number");
        if (numberNode == null || !numberNode.isNumber()) {
            errors.add("Ticket.number must be a positive number");
        } else {
            double number = numberNode.asDouble();
            if (number <= 0) {
                errors.add("Ticket.number must be > 0, got: " + number);
            }
        }
        
        JsonNode refundableNode = ticketNode.get("refundable");
        if (refundableNode == null || !refundableNode.isBoolean()) {
            errors.add("Ticket.refundable must be a boolean");
        }
        
        JsonNode venueNode = ticketNode.get("venue");
        if (venueNode == null && ticketNode.get("venueId") == null) {
            errors.add("Ticket must have either venue object or venueId");
        } else if (venueNode != null) {
            errors.addAll(validateVenue(venueNode));
        }
        
        JsonNode typeNode = ticketNode.get("type");
        if (typeNode != null && typeNode.isTextual()) {
            try {
                TicketType.valueOf(typeNode.asText().toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.add("Ticket.type must be one of: VIP, USUAL, BUDGETARY, CHEAP");
            }
        }
        
        JsonNode personNode = ticketNode.get("person");
        if (personNode != null && !personNode.isNull()) {
            errors.addAll(validatePerson(personNode));
        }
        
        JsonNode eventNode = ticketNode.get("event");
        if (eventNode != null && !eventNode.isNull()) {
            errors.addAll(validateEvent(eventNode));
        }
        
        return errors;
    }
}



