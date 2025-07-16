# Rest API

Alle APIs sind mit ihren Body- und Antwortdetails auch
in Swagger [`/swagger-ui/index.html`] sichtbar.

## Inhalte

- [POST /api/authenticate](#/api/authenticate)
- [POST /public/insurance/calculate](#/public/insurance/calculate)
- [POST /api/orders](#/api/orders)
- [POST /api/orders/register](#/api/orders/register)
- [PATCH /api/orders/{id}](#/api/orders/{id})
- [DELETE /api/orders/{id}](#/api/orders/{id})
- [POST /api/users/register](#/api/orders/register)
- [DELETE /api/users/{id}](#/api/orders/{id})

## /api/authenticate

Authentifiziert einen Benutzer und gibt ein JWT zurück.

**Method: POST**

**Body**
```json
{
  "email": "email",
  "password": "password",
  "rememberMe": false
}
```

**Response**
```json
{
  "token": "token"
}
```
**200:** Alles Ok!

**400:** Entweder E-Mail oder Password ist falsch formuliert.

**401:** Entweder E-Mail existiert nicht oder Password ist falsch.

## /public/insurance/calculate

Führt nur die Berechnung der Versicherungsprämie basierend auf den angegebenen Parametern durch.
Es wird keine Bestellung gespeichert.

**Method: POST**

**Body**
```json
{
  "vehicleType": "SPORT",
  "yearlyDrive": 10000,
  "zipcode": "****"
}
```

**Response**
```json
{
  "yearlyPrice": 0.00,
  "monthlyPrice": 0.00
}
```
**200:** Alles Ok!

**400:** Entweder sind die Einträge nicht vorhanden oder sie wurden falsch eingegeben.

**406:** Die Postleitzahl ist ungültig.

## /api/orders

**Nur der Administrator hat Zugriff auf diesen Endpunkt!**

Erstellt eine neue Bestellung für einen beliebigen Kunden.
Hier muss die Benutzer-ID explizit angegeben werden.

**Method: POST**

**Body**
```json
{
  "user": {
    "id": 0
  },
  "vehicleType": "SPORT",
  "yearlyDrive": 0,
  "zipcode": "*****"
}
```

**Response**
```json
{
  "id": 0,
  "vehicleType": "vehicleType",
  "yearlyDrive": "yearlyDrive",
  "yearlyPrice": "yearlyPrice",
  "zipcode": "*****",
  "date": "date",
  "user": {
    "id": 0,
    "name": "name",
    "email": "email"
  }
}
```
**201:** Alles Ok!

**400:** Entweder sind die Einträge nicht vorhanden oder sie wurden falsch eingegeben.

**401:** Nicht authentifizierter Client.

**403:** Der Client verfügt nicht über ausreichend Zugriff.

**406:** Die Postleitzahl ist ungültig.

## /api/orders/register

Erstellt eine neue Bestellung für den authentifizierten Nutzer.
Die Benutzer-ID wird automatisch anhand des Tokens erkannt.

**Method: POST**

**Body**
```json
{
  "vehicleType": "SPORT",
  "yearlyDrive": 0,
  "zipcode": "*****"
}
```

**Response**
```json
{
  "id": 0,
  "vehicleType": "vehicleType",
  "yearlyDrive": "yearlyDrive",
  "yearlyPrice": "yearlyPrice",
  "zipcode": "*****",
  "date": "date",
  "user": {
    "id": 0,
    "name": "name",
    "email": "email"
  }
}
```
**201:** Alles Ok!

**400:** Entweder sind die Einträge nicht vorhanden oder sie wurden falsch eingegeben.

**401:** Nicht authentifizierter Client.

**406:** Die Postleitzahl ist ungültig.

## /api/orders/{id}

Aktualisiert eine bestehende Bestellung.

- Normale Benutzer: nur ihre eigenen Bestellungen.

- Admins: alle Bestellungen.



**Method: PATCH**

**Body**
```json
{
  "id": 0,
  "vehicleType": "SPORT",
  "yearlyDrive": 0,
  "zipcode": "*****"
}
```

**Response**
```json
{
  "id": 0,
  "vehicleType": "vehicleType",
  "yearlyDrive": "yearlyDrive",
  "yearlyPrice": "yearlyPrice",
  "zipcode": "*****",
  "date": "date",
  "user": {
    "id": 0,
    "name": "name",
    "email": "email"
  }
}
```
**201:** Alles Ok!

**400:** Entweder sind die Einträge nicht vorhanden oder sie wurden falsch eingegeben.

**401:** Nicht authentifizierter Client.

**404:** Die Bestellung mit der ID existiert nicht (oder der Benutzer hat versucht, die Bestellung eines anderen Benutzers zu aktualisieren)

**406:** Die Postleitzahl ist ungültig.

## /api/orders/{id}

**Nur der Administrator hat Zugriff auf diesen Endpunkt!**

Löscht eine Bestellung dauerhaft.

**Method: DELETE**

**204:** Alles Ok!

**401:** Nicht authentifizierter Client.

## /api/users/register

Registriert einen neuen Benutzer im System.

**Method: POST**

**Body**
```json
{
  "firstName": "firstName",
  "lastName": "lastName",
  "email": "email",
  "password": "password"
}
```

**Response**
```json
{
  "id": 0,
  "name": "name",
  "email": "email"
}
```
**201:** Alles Ok!

**400:** Entweder sind die Einträge nicht vorhanden oder sie wurden falsch eingegeben.

**401:** Nicht authentifizierter Client.

**409:** Die E-Mail-Adresse ist schon registriert.

## /api/users/{id}

Löscht einen Benutzer sowie alle zugehörigen Bestellungen.

**Nur der Administrator hat Zugriff auf diesen Endpunkt!**

**Method: DELETE**

**204:** Alles Ok!

**401:** Nicht authentifizierter Client.
