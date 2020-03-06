Address API
==================
Address API provides a REST API performing CRUD operations against Address entity.
Each API is secured via [JWT](https://jwt.io/), 
so the Authorization header using the Bearer schema is required to access each API.

The API is a Spring Boot application that is able to run in a docker container.
See [dongpak/addressapi](https://hub.docker.com/repository/docker/dongpak/addressapi).  

## Get a page of Address entities
The GET call returns list of Address entities and supports 
* Pagination with page=?&size=?, where default size is 20
* Sorting with sortBy=<comma separated list of properties optionally prefixing with + or - to indicate ascending or descending
* Filtering with property=value, where value can have % to indicate "like" vs equal.  !, <, <=, >, >= can prefix a property indicate not equal, less than or greater than

GET /api/address

## Get a single Address entity
GET /api/address/{id}

## Create a single Address entity
POST /api/address

## Update a single Address entity
PUT /api/address/{id}

## Delete a single Address entity
DELETE /api/address/{id}


Architecture
==================
<img src="https://github.com/dongpak/addressapi/blob/master/Architecture.jpg"/>

