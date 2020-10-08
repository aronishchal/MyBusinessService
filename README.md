# MyBusinessService
Simple Java REST Service for a Business Entity with name and address. 

<h2>Introduction</h2>
This is a simple REST Service with CRUD Operations for a Business Entity which has a name and address. The address is geocoded to obtain lat and long coordinates using the Location IQ geocoding API.<br/>
This was built using Java, Jersey and JDBC. The database is MySQL, but this can work with any database as long as the correct JDBC driver is provided.

<h2>Usage</h2>
<h3>GET All</h3>
<h4>Request</h4>

```
GET /mybusinessservice/businesses HTTP/1.1
Content-Type: application/json
```

<h4>Successful Response</h4>

```
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 4,
        "lat": 40.78382735,
        "lon": -73.9753661840512,
        "name": "My Consulting Business",
        "address": "129 W 81st St New York, NY 10024 USA"
    }
]
```
<h3>GET by id</h3>
<h4>Request</h4>

```
GET /mybusinessservice/businesses HTTP/1.1
Content-Type: application/json
```

<h4>Successful Response</h4>

```
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 4,
    "lat": 40.78382735,
    "lon": -73.9753661840512,
    "name": "My Consulting Business",
    "address": "129 W 81st St New York, NY 10024 USA"
}
```

<h3>POST</h3>
<h4>Request</h4>

```
POST /mybusinessservice/businesses HTTP/1.1
Accept: application/json
Content-Type: application/json

{
  "name": "My Consulting Business",
  "address": "129 W 81st St New York, NY 10024 USA"
}
```

<h4>Successful Response</h3>

`HTTP/1.1 201 CREATED`

<h3>PUT</h3>
<h4>Request</h4>

```
PUT /mybusinessservice/businesses/{id} HTTP/1.1
Accept: application/json
Content-Type: application/json

{
  "name": "My Consulting Business 2",
  "address": "129 W 81st St New York, NY 10024 USA"
}
```

<h4>Successful Response</h4>

```
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 4,
        "name": "My Consulting Business 2",
        "address": "129 W 81st St New York, NY 10024 USA"
    }
]
```

<h3>DELETE</h3>
<h4>Request</h4>

`DELETE /mybusinessservice/businesses/{id} HTTP/1.1`

<h4>Successful Response</h4>

`HTTP/1.1 204 No Content`
