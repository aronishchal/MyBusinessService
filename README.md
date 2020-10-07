# MyBusinessService
Simple Java REST Service for a Business Entity with name and address. 

<h2>Introduction</h2>
This is a simple REST Service with CRUD Operations for a Business Entity which has a name and address. I built this using Java, Jersey, JAX-B and JDBC. I used MySQL as the Database, but this could be used with any Database as long as the correct JDBC driver is used.

<h2>Usage</h2>
<h3>GET</h3>
`GET /mybusinessservice/businesses HTTP/1.1
Content-Type: application/json`
<h3>Successful Response</h3>
`HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 4,
        "name": "My Consulting Business",
        "address": "129 W 81st St New York, NY 10024 USA"
    }
]`
<h3>POST</h3>
`POST /mybusinessservice/businesses HTTP/1.1
Accept: application/json
Content-Type: application/json

{
  "name": "My Consulting Business",
  "address": "129 W 81st St New York, NY 10024 USA"
}`
<h3>Successful Response</h3>
`HTTP/1.1 201 CREATED`
<h3>PUT</h3>
`PUT /mybusinessservice/businesses/{id} HTTP/1.1
Accept: application/json
Content-Type: application/json

{
  "name": "My Consulting Business 2",
  "address": "129 W 81st St New York, NY 10024 USA"
}`
<h3>Successful Response</h3>
`HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 4,
        "name": "My Consulting Business 2",
        "address": "129 W 81st St New York, NY 10024 USA"
    }
]`
<h3>DELETE</h3>
`DELETE /mybusinessservice/businesses/{id} HTTP/1.1`
<h3>Successful Response</h3>
`HTTP/1.1 204 No Content`
