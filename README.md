# dental-appointment-app

## Project setup

Android app created in Java (Views) and prepared for Supabase integration.

## Setup (Supabase)
Projekt koristi Supabase REST (PostgREST) preko Retrofit/OkHttp.

### 1) Lokalni ključevi (NE commitati)
U root-u projekta u `local.properties` ih mijenjate (file je lokalni i ne ide na Git).

Primjer:
SUPABASE_URL=https://<project_ref>.supabase.co
SUPABASE_ANON_KEY=<anon_key>

### 2) Sync & Run
- Gradle Sync
- Run app

### 3) Osnovni network sloj
- `data/network/ApiClient` – Retrofit instance
- `data/network/AuthInterceptor` – dodaje `apikey` i `Authorization`
- `data/local/TokenStorage` – sprema access token u SharedPreferences
- `data/network/api/ServicesApi` – primjer poziva `GET /rest/v1/services?select=*`

