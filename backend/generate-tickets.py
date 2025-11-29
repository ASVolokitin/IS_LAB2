import json
import random
import sys
import os
from datetime import datetime, timedelta
from pathlib import Path
import string
import secrets

TICKET_TYPES = ["VIP", "USUAL", "BUDGETARY", "CHEAP"]
VENUE_TYPES = ["PUB", "LOFT", "OPEN_AREA", "MALL"]
COLORS = ["GREEN", "BLUE", "YELLOW", "WHITE"]
COUNTRIES = ["RUSSIA", "USA", "CHINA", "ITALY"]
VENUE_NAMES = [
    "Grand Hall", "Concert Arena", "Theater Stage", "Sports Center",
    "Music Venue", "Event Space", "Performance Hall", "Exhibition Center"
]
EVENT_NAMES = [
    "Rock Concert", "Jazz Festival", "Classical Performance", "Comedy Show",
    "Dance Event", "Theater Play", "Opera Night", "Symphony Orchestra"
]
LOCATION_NAMES = [
    "Moscow", "St. Petersburg", "New York", "Los Angeles",
    "Beijing", "Shanghai", "Rome", "Milan", "London", "Paris"
]

PASSPORT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
alphabet = string.ascii_letters + string.digits


def random_int(min_val, max_val):
    """Generate random integer in range [min_val, max_val]"""
    return random.randint(min_val, max_val)


def random_double(min_val, max_val):
    """Generate random double in range [min_val, max_val)"""
    return random.uniform(min_val, max_val)


def random_element(arr):
    """Get random element from array"""
    return random.choice(arr)


def random_date():
    """Generate random date between 2020 and 2025"""
    start_date = datetime(2020, 1, 1)
    end_date = datetime(2025, 12, 31)
    time_between = end_date - start_date
    days_between = time_between.days
    random_days = random.randrange(days_between)
    random_date_obj = start_date + timedelta(days=random_days)
    random_date_obj = random_date_obj.replace(
        hour=random_int(0, 23),
        minute=random_int(0, 59),
        second=random_int(0, 59)
    )
    return random_date_obj.strftime("%Y-%m-%dT%H:%M:%S")


def random_passport(ticket_num):
    """Generate random passport ID (10 characters)"""
    return ''.join(secrets.choice(alphabet) for _ in range(10))

def generate_ticket(ticket_num):
    """Generate a single ticket JSON object"""
    ticket = {
        # "name": f"Ticket-{ticket_num}",
        "name": f"{''.join(secrets.choice(alphabet) for _ in range(18))}",

        "coordinates": {
            "x": random_int(-200, 1000),
            "y": round(random_double(-4.0, 1000.0), 6)
        },
        "price": random_int(100, 100000),
        "number": round(random_double(0.1, 1000000.0), 6),
        "refundable": random.choice([True, False]),
        "venue": {
            "name": random_element(VENUE_NAMES),
            "capacity": random_int(50, 50000),
            "type": random_element(VENUE_TYPES)
        }
    }
    
    if random.random() < 0.7:
        ticket["type"] = random_element(TICKET_TYPES)
    
    if random.random() < 0.3:
        ticket["discount"] = random_int(1, 100)
    
    if random.random() < 0.8:
        person = {
            "eyeColor": random_element(COLORS),
            "hairColor": random_element(COLORS),
            "passportID": random_passport(ticket_num) + str(ticket_num)
        }
        
        if random.random() < 0.7:
            person["nationality"] = random_element(COUNTRIES)
        
        if random.random() < 0.6:
            person["location"] = {
                "x": round(random_double(-1000.0, 1000.0), 6),
                "y": random_int(-1000, 1000),
                "z": round(random_double(-1000.0, 1000.0), 6),
                "name": random_element(LOCATION_NAMES)
            }
        
        ticket["person"] = person
    
    if random.random() < 0.7:
        event_name = random_element(EVENT_NAMES)
        ticket["event"] = {
            "name": event_name,
            "date": random_date(),
            "minAge": random_int(0, 21),
            "description": f"Description for {event_name}"
        }
    
    return ticket


def main():
    """Main function"""
    if len(sys.argv) < 2:
        print(f"Usage: {sys.argv[0]} <number_of_tickets> [output_file]")
        print(f"Example: {sys.argv[0]} 1000 sample-import-tickets.json")
        sys.exit(1)
    
    try:
        count = int(sys.argv[1])
    except ValueError:
        print("Error: Number of tickets must be a positive integer")
        sys.exit(1)
    
    if count < 1:
        print("Error: Number of tickets must be at least 1")
        sys.exit(1)
    
    if count > 10000000:
        print("Error: Number of tickets cannot exceed 10,000,000")
        sys.exit(1)
    
    output_file = sys.argv[2] if len(sys.argv) > 2 else "sample-import-tickets.json"
    
    print(f"Generating {count} tickets to {output_file}...")
    
    tickets = []
    progress_interval = max(1, count // 100) 
    for i in range(1, count + 1):
        tickets.append(generate_ticket(i))
        
        if i % 10000 == 0 or (i % progress_interval == 0 and count > 1000):
            print(f"Generated {i:,} tickets...", file=sys.stderr)
    
    print(f"Writing JSON file...", file=sys.stderr)
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(tickets, f, ensure_ascii=False, indent=None, separators=(',', ':'))
    
    file_size = os.path.getsize(output_file)
    file_size_mb = file_size / (1024 * 1024)
    
    if file_size_mb >= 1:
        size_str = f"{file_size_mb:.2f} MB"
    else:
        size_str = f"{file_size / 1024:.2f} KB"
    
    print(f"Done! Generated {count:,} tickets in {output_file} (size: {size_str})")


if __name__ == "__main__":
    main()

