# ORDER

## KEYCLOAK JWT

````
curl -L -X POST 'http://localhost:7081/realms/order-dev/protocol/openid-connect/token' -H 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'client_id=order' --data-urlencode 'grant_type=password' --data-urlencode 'username=devclient@order.com' --data-urlencode 'password=client'
````
