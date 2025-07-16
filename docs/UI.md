# UI

## Inhalte

- [/](#/)
- [/authenticate](#/authenticate)
- [/dashboard](#/dashboard)
- [/dashboard/users](#/dashboard/users)
- [/dashboard/users/{id}](#/dashboard/users/{id})
- [/dashboard/orders/{id}](#/dashboard/orders/{id})

## /

Auf der Startseite der Anwendung befindet sich ein einfaches
Formular zur Berechnung der Versicherungsprämie.

Der Nutzer gibt dort die jährliche Kilometerleistung, 
den Fahrzeugtyp sowie die Postleitzahl der Zulassungsstelle ein. 
Das Formular ist mit einer öffentlichen API verbunden, 
die ausschließlich für die Berechnung zuständig ist.
Es wird kein Antrag gespeichert oder abgeschickt.

Nach dem Absenden zeigt die Anwendung lediglich 
die berechnete Prämie auf Basis der eingegebenen Daten an. 
Die Berechnung erfolgt rein zur Demonstration und hat keinen
Einfluss auf gespeicherte Daten oder einen echten Versicherungsprozess.

## /authenticate

Auf dieser Seite kann sich der Benutzer registrieren oder,
falls bereits ein Konto vorhanden ist, einloggen, 
um Zugriff auf das persönliche Dashboard zu erhalten.

## /dashboard

Auf dieser Seite kann der Benutzer seine bereits erstellten 
Anträge einsehen und im Detail öffnen. Außerdem befindet 
sich hier ein Formular, 
ähnlich wie auf der Startseite, 
mit dem neue Anträge erstellt werden können.
Im Gegensatz zur Startseite werden die Daten hier über 
ein separates API verarbeitet und persistiert in der 
Datenbank gespeichert.

## /dashboard/users

Diese Seite ist ausschließlich für Administratoren zugänglich.
Hier kann der Admin eine Liste aller registrierten Benutzer
einsehen und bei Bedarf einzelne Benutzerprofile im Detail öffnen.

## /dashboard/users/{id}

Auf dieser Seite kann der Administrator die Bestellungen 
eines bestimmten Benutzers einsehen.
Zusätzlich kann er für diesen Benutzer eine neue Bestellung 
anlegen oder den Benutzer inklusive aller zugehörigen Bestellungen löschen.

## /dashboard/orders/{id}

Auf dieser Seite kann der Administrator eine Bestellung 
einzeln anzeigen, ihre Informationen aktualisieren oder 
die Bestellung bei Bedarf löschen.





