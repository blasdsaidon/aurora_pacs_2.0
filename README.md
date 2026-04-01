# Aurora PACS (base corregida)

Base simplificada para el escenario acordado:

- **Orthanc** recibe DICOM
- **Stone Viewer** se usa como visor web
- **Spring Boot** maneja usuarios, permisos y lógica de acceso
- **PostgreSQL** guarda usuarios y permisos por estudio

## Qué quedó afuera a propósito

- OHIF
- DWV
- H2 / MySQL
- Thymeleaf y pantallas de prueba viejas
- órdenes, reportes y flujos de recepción

## Modelo actual

- `ADMIN`: acceso total
- `DOCTOR`: acceso total a estudios
- `PATIENT`: sólo estudios asignados en `study_access`

## Variables importantes

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `ORTHANC_BASE_URL`
- `ORTHANC_USERNAME`
- `ORTHANC_PASSWORD`
- `ORTHANC_DICOMWEB_BASE`
- `STONE_BASE_URL`

## Endpoints base

- `GET /api/health`
- `GET /api/auth/me`
- `GET /api/studies`
- `GET /api/studies/{studyInstanceUid}`
- `GET /api/studies/{studyInstanceUid}/viewer-url`
- `GET /api/admin/users`
- `POST /api/admin/users`
- `PUT /api/admin/users/{id}`
- `DELETE /api/admin/users/{id}`
- `GET /api/admin/study-access/user/{userId}`
- `POST /api/admin/study-access`
- `DELETE /api/admin/study-access/{id}`

## Usuario semilla

Al iniciar, si no existe, crea:

- usuario: `admin`
- clave: `admin123`

## Siguiente paso recomendado

Integrar esto con Nginx + Orthanc Authorization Plugin para que el acceso a DICOMweb/Stone no dependa sólo del frontend.
# aurora_pacs_2.0
