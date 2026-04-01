# Aurora Central Bundle

Levanta la parte online/central:
- PostgreSQL
- Orthanc central
- Nginx

## Cómo correr
```bash
cd aurora-central-bundle
docker compose up -d
```

## URLs
- Portal: http://localhost
- Orthanc central HTTP (debug): http://localhost:8043
- Orthanc central DICOM: localhost:4243

## Notas
- Este bundle asume que tu backend Java corre fuera de Docker en `localhost:8080`.
- Nginx redirige `/viewer/` y `/dicom-web/` al Orthanc central.
