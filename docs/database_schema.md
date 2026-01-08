# Database Schema – Dental Appointment App

Ovaj dokument opisuje strukturu baze podataka korištene u aplikaciji za rezervaciju termina.

---

## 1. profiles

Sadrži podatke o korisnicima aplikacije.

| Column       | Type        | Description                                      |
|--------------|-------------|--------------------------------------------------|
| id           | uuid        | ID korisnika (povezan s auth.users)              |
| first_name   | text        | Ime korisnika                                    |
| last_name    | text        | Prezime korisnika                                |
| email        | text        | Email korisnika                                  |
| role         | text        | Uloga korisnika (`user` ili `owner`)             |
| created_at   | timestamptz | Datum i vrijeme kreiranja zapisa                 |

Napomena:
- `id` se automatski generira kroz Supabase Auth.
- Zadana uloga korisnika je `user`.
- Profil se automatski kreira pomoću triggera na `auth.users`.

---

## 2. services

Sadrži dostupne stomatološke usluge.

| Column           | Type    | Description                     |
|------------------|---------|---------------------------------|
| id               | bigint  | ID usluge (auto increment)      |
| name             | text    | Naziv usluge                    |
| duration_minutes | integer | Trajanje usluge u minutama      |
| price            | numeric | Cijena usluge                   |
| description      | text    | Opis usluge                     |

Napomena:
- Usluge su dostupne svim korisnicima (anonimnim i autentificiranim).
- Upravljanje uslugama predviđeno je za `owner` ulogu.

---

## 3. appointments

Sadrži rezervacije termina korisnika.

| Column            | Type        | Description                                   |
|-------------------|-------------|-----------------------------------------------|
| id                | bigint      | ID rezervacije (auto increment)               |
| user_id           | uuid        | ID korisnika (FK → profiles.id)               |
| service_id        | bigint      | ID usluge (FK → services.id)                  |
| appointment_time  | timestamptz | Datum i vrijeme termina                       |
| status            | text        | Status rezervacije (`pending` kao zadani)     |

Napomena:
- `user_id` mora odgovarati trenutno prijavljenom korisniku.
- Termin mora biti u budućnosti (validacija u aplikaciji).
- Status se može proširiti (`confirmed`, `cancelled`).

---

## Relationships

- `profiles.id` → `appointments.user_id`
- `services.id` → `appointments.service_id`

---

## Security (RLS)

- User može vidjeti i mijenjati samo vlastite rezervacije.
- Owner ima pristup svim zapisima.
- Usluge su dostupne svim korisnicima.
