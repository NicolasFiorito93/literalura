# Literalura

Literalura es una aplicación de consola desarrollada en Java utilizando Spring Boot. La aplicación permite buscar información sobre libros y autores desde una API pública, y gestionar esta información en una base de datos utilizando JPA/Hibernate.

## Tabla de Contenidos

- [Uso](#uso)
- [Funciones Detalladas](#funciones-detalladas)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)

## Uso

Una vez que la aplicación esté en ejecución, se presentará un menú en la consola con las siguientes opciones:

1. Buscar Libros
2. Mostrar libros buscados
3. Mostrar los autores de las búsquedas
4. Buscar autores vivos en determinada fecha
5. Mostrar cuantos libros en Inglés registrados
6. Mostrar cuantos libros en Español registrados
0. Salir

## Funciones Detalladas

1. **Buscar Libros:**
   Permite buscar libros en la API de Gutendex. Si el libro no está en la base de datos, se guarda junto con su autor.

2. **Mostrar libros buscados:**
   Muestra todos los libros que se han buscado y guardado en la base de datos.

3. **Mostrar los autores de las búsquedas:**
   Muestra todos los autores de los libros buscados.

4. **Buscar autores vivos en determinada fecha:**
   Permite buscar autores que estaban vivos en una fecha específica.

5. **Mostrar cuantos libros en Inglés registrados:**
   Muestra la cantidad de libros en Inglés que están registrados en la base de datos.

6. **Mostrar cuantos libros en Español registrados:**
   Muestra la cantidad de libros en Español que están registrados en la base de datos.

## Tecnologías Utilizadas

- **Java 11**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Jackson**: Para el mapeo de JSON
- **Gutendex API**: API pública para obtener información sobre libros
