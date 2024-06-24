package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import jakarta.transaction.Transactional;

import java.util.*;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private Libro libro;
    private Autor autor;
    private List<Libro> librosBuscados = new ArrayList<>();
    private List<Autor> autoresBuscados = new ArrayList<>();
    private Scanner leer = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menu() {
        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    1 - Buscar Libros.
                    2 - Mostrar libros buscados.
                    3 - Mostrar los autores de las busquedas.
                    4 - Buscar autores vivos en determinada fecha.
                    5 - Mostrar cuantos libros en Ingles registrados.
                    6 - Mostrar cuantos libros en Español registrados.
                                        
                    0 - Salir                
                    """;
            System.out.println(menu);
            try {
                opcion = leer.nextInt();
                leer.nextLine();  // Para consumir la nueva línea
                switch (opcion) {
                    case 1:
                        buscarLibro();
                        break;
                    case 2:
                        muestraBusquedas();
                        break;
                    case 3:
                        buscarAutores();
                        break;
                    case 4:
                        autoresVivosEnDeterminadaFecha();
                        break;
                    case 5:
                        System.out.println("En la base de datos se encuentran una cantidad de: " + libroRepository.countByIdioma("en") + " libros en ingles");
                        break;
                    case 6:
                        System.out.println("En la base de datos se encuentran una cantidad de: " + libroRepository.countByIdioma("es") + " libros en español");
                        break;
                    case 0:
                        System.out.println("Cerrando aplicacion");
                        break;
                    default:
                        System.out.println("Opcion incorrecta.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Opcion incorrecta.");
                leer.nextLine();
            }
        }
    }

    private Optional<DatosLibro> getDatosLibro() {
        System.out.println("Escriba el nombre del libro que desee buscar.");
        var libro = leer.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + libro.replace(" ", "%20"));
        var datosBusqueda = convierteDatos.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.libros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(libro.toUpperCase()))
                .findFirst();
        return libroBuscado;
    }

    @Transactional
    private void buscarLibro() {
        Optional<DatosLibro> libroBuscado = getDatosLibro();
        if (libroBuscado.isPresent()) {
            System.out.println("Libro Encontrado");
            System.out.println(libroBuscado.get());
            DatosLibro datosLibro = libroBuscado.get();
            if (!datosLibro.autor().isEmpty()) {
                autor = new Autor(datosLibro.autor().get(0));
            }else {
                autor = new Autor();
                autor.setNombre("Desconocido");
            }
            Optional<Libro> libroBaseDatos = libroRepository.findByTitulo(datosLibro.titulo());
            if (libroBaseDatos.isEmpty()) {
                Optional<Autor> autorBaseDatos = autorRepository.findByNombre(autor.getNombre());
                if (autorBaseDatos.isPresent()) {
                    autor = autorBaseDatos.get();
                } else {
                    autorRepository.save(autor);
                }
                libro = new Libro(datosLibro);
                libro.setAutor(autor);
                libroRepository.save(libro);
            }
            librosBuscados.add(libro);
            autoresBuscados.add(autor);
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void muestraBusquedas() {
        List<Libro> busquedas = libroRepository.findAll();
        busquedas.forEach(System.out::println);
    }

    private void buscarAutores() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void autoresVivosEnDeterminadaFecha() {
        try {
            System.out.println("Ingrese el año en el que desea buscar los autores.");
            var anio = leer.nextInt();
            leer.nextLine();

            List<Optional<Autor>> autoresVivos = autorRepository.findAutorByFecha(anio);
            if (!autoresVivos.isEmpty()) {
                autoresVivos.forEach(System.out::println);
            } else {
                System.out.println("No se encontró ningún autor.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, ingrese un número entero para el año.");
            leer.nextLine();
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
