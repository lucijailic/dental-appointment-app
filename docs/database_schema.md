# Database Schema – Dental Appointment App

Ovaj dokument opisuje strukturu baze podataka korištene u aplikaciji za rezervaciju termina.

---

## 1. profiles

Sadrži podatke o korisnicima aplikacije.

| Column | Type | Description |
|------|------|-------------|
| id | uuid | ID korisnika (povezan s auth.users) |
| email | text | Email korisnika |
| role | text | Uloga korisnika (`user` ili `owner`) |
| created_at | timestamp | Datum kreiranja zapisa |

---

## 2. services

Sadrži dostupne stomatološke usluge.

| Column | Type | Description |
|------|------|-------------|
| id | uuid | ID usluge |
| name | text | Naziv usluge |
| duration_minutes | int | Trajanje usluge |
| price | numeric | Cijena usluge |
| created_at | timestamp | Datum kreiranja |

---

## 3. appointments

Sadrži rezervacije termina.

| Column | Type | Description |
|------|------|-------------|
| id | uuid | ID rezervacije |
| user_id | uuid | ID korisnika (FK → profiles.id) |
| service_id | uuid | ID usluge (FK → services.id) |
| start_time | timestamp | Vrijeme termina |
| status | text | Status (reserved / cancelled) |
| note | text | Dodatna napomena |
| created_at | timestamp | Datum kreiranja |

---

## Relationships

- `profiles.id` → `appointments.user_id`
- `services.id` → `appointments.service_id`

---

## Security (RLS)

- User može vidjeti i mijenjati samo vlastite rezervacije.
- Owner ima pristup svim zapisima.
- Usluge su dostupne svim autentificiranim korisnicima.
