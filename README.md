# ORDER

## KEYCLOAK JWT

````
curl -L -X POST 'http://localhost:7081/realms/order-dev/protocol/openid-connect/token' -H 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'client_id=order' --data-urlencode 'grant_type=password' --data-urlencode 'username=devclient@order.com' --data-urlencode 'password=client'
````


## TESTED ON 
Maven 3.9.11

Java version: 25, vendor: Eclipse Adoptium, jdk-25.0.0.36-hotspot

Docker Desktop 4.48.0 (207573), Engine Version 28.5.1

Docker Compose version v2.40.0-desktop.1
