# API Contract – Dental Appointment App

Ovaj dokument opisuje komunikaciju između Android aplikacije i Supabase backend-a.

---

## Base URL  

https://qskuqfqacbwkuafgspft.supabase.co

---

## Authentication

### Login (email + password)

Endpoint:
POST /auth/v1/token?grant_type=password

Opis:
- Koristi se za prijavu korisnika.
- Vraća access token koji se koristi za autorizaciju svih daljnjih poziva.

---

## Authorization

Svi zaštićeni pozivi koriste:

Authorization: Bearer <ACCESS_TOKEN>

---

## Services (usluge)

### Dohvat svih usluga

GET /rest/v1/services?select=*

Opis:
- Dohvaća sve dostupne stomatološke usluge.
- Dostupno svim prijavljenim korisnicima.

---

## Appointments (rezervacije)

### Kreiranje rezervacije

POST /rest/v1/appointments

Opis:
- Kreira novu rezervaciju termina.
- Korisnik može kreirati samo vlastite rezervacije.

---

### Dohvat rezervacija

GET /rest/v1/appointments?select=*

Opis:
- Korisnik vidi samo svoje rezervacije.
- Vlasnik (owner) vidi sve rezervacije.

---

## Roles

| Role  | Opis |
|-------|------|
| user  | Može pregledavati usluge i kreirati vlastite rezervacije |
| owner | Može pregledavati sve rezervacije i upravljati njima |

---

## Napomena

- Backend koristi Supabase (PostgreSQL + PostgREST).
- Autentifikacija se vrši putem Supabase Auth servisa.
- Pristup podacima je kontroliran pomoću RLS (Row Level Security).