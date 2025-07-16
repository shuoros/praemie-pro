# Prämie Pro

## Stack

- Java 17
- Spring Boot 3.5.3
- Thymleaf
- JWT Authentication
- H2 (Entwicklungsphase)
- PostgreSQL 15 (Produktion)
- JUnit 5 mit Testcontainers
- Mockito

## So starten Sie die Anwendung

### mit Docker

Um die Anwendung auszuführen, führen Sie `sh.bash` auf Ihrem Terminal aus.
Dadurch wird ein Docker-Image erstellt und mit Docker Compose ausgeführt.

Die App wird unter `http://localhost:8888` verfügbar.

### mit Maven

Dazu muss auf Ihrem Rechner mindestens Java 17 installiert sein und der Port 8080 muss frei sein.

#### Build
Zunächst erstellen Sie die App mit diesem Befehl:
```
./mvnw clean install -DskipTests -Pprod
```

#### Run
Führen Sie die Anwendung mit diesem Befehl aus:
```
./mvnw spring-boot:run
```

Die App wird unter `http://localhost:8080` verfügbar.

## Testen
Führen Sie Tests mit diesem Befehl aus:

```
./mvnw clean test
```

## Dokumentation
Um die Dokumentationen zu lesen, klicken Sie bitte [hier](/docs/DOCUMENTS.md)

## Erklärungen

### Warum habe ich H2 und PostgreSQL gewählt?

In der Entwicklungsumgebung habe ich H2 verwendet, weil es eine leichte,
speicherbasierte Datenbank ist, die sich besonders gut für lokale Tests eignet.
Sie benötigt keine separate Installation, lässt sich schnell starten und ermöglicht
einen einfachen Reset der Daten. Dadurch kann ich die Anwendung effizient testen und
debuggen, ohne mich um Datenbankkonfigurationen kümmern zu müssen.

In der Produktionsumgebung setze ich hingegen auf PostgreSQL, da es eine leistungsfähige,
stabile und weit verbreitete relationale Datenbank ist, die sich gut für den produktiven Einsatz eignet.
PostgreSQL bietet bessere Performance, Zuverlässigkeit, Skalierbarkeit und hat sich im Enterprise-Umfeld bewährt.

### Konzept zur Wahrung der Softwarequalität

Ich verwende JUnit 5 zusammen mit Mockito, um die Business-Logik durch
Unit-Tests zu prüfen. Mit Mockito kann ich Abhängigkeiten wie Repositories
mocken und gezielt einzelne Logik testen.

Zusätzlich setze ich Integrationstests ein, um sicherzustellen,
dass Komponenten wie Controller, Service und Datenbank korrekt zusammenarbeiten.

Die Kombination aus Unit- und Integrationstests hilft mir, 
sowohl einzelne Funktionen als auch das Systemverhalten 
zuverlässig zu testen für stabile und wartbare Software.

### Wie erfolgt die Kommunikation zwischen den Services?

In meiner Lösung kommunizieren die Services über interne 
Methodenaufrufe innerhalb derselben Anwendung. 
Da es sich um ein einfaches System handelt, 
brauche ich keine externe Kommunikation 
(z.B. über REST oder Messaging).
Das macht die Entwicklung und Wartung einfacher.

Bei größeren oder verteilten Systemen würde ich jedoch
z.B. eine REST-API oder Messaging wie Kafka in Betracht ziehen.

### Persönliches Fazit

Für mich war es eine sehr spannende Erfahrung, 
dieses Projekt komplett von Grund auf zu entwickeln, 
etwas, das ich im Alltag nur selten mache.
In meinem aktuellen Job setzen wir häufig auf bestehende 
Codebausteine, um Zeit zu sparen
z.B. für Datenbankkonfiguration, JWT-Authentifizierung 
oder Fehlerbehandlung. Dabei greifen wir oft auf bewährten 
Code aus früheren Projekten zurück.

Hier war es eine schöne Herausforderung, 
alles selbst neu aufzubauen. 
Ich habe die aktuelle Dokumentation von Spring gelesen 
und bewusst versucht, moderne Best Practices und die 
neuesten Features zu nutzen. 
Das hat mir geholfen, mein Wissen aufzufrischen und 
ein besseres Verständnis für die neuesten Entwicklungen 
im Spring-Ökosystem zu gewinnen.



