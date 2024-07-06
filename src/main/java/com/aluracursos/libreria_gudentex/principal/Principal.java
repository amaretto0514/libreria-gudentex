package com.aluracursos.libreria_gudentex.principal;

import com.aluracursos.libreria_gudentex.model.*;
import com.aluracursos.libreria_gudentex.repository.AutorRepository;
import com.aluracursos.libreria_gudentex.repository.LibroRepository;
import com.aluracursos.libreria_gudentex.service.ConsumoAPI;
import com.aluracursos.libreria_gudentex.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    private List<Autor> autores;
    private List<Libro> libros;
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;

    public void muestraMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    1- Buscar libro por titulo
                    2- Listar libros registrados por titulo
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idiomas
                    6- Top 10 libros más descargados desde API
                    7- Top 5 libros más descargados desde DB
                    8- Estadisticas API
                    0- Salir de la aplicación
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    bucarLibrosPorTituloWeb();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresBuscados();
                    break;
                case 4:
                    mostrarAutoresVivosPorAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    mostarTop10DesdeAPI();
                    break;
                case 7:
                    mostrarTop5DesdeDB();
                    break;
                case 8:
                    estadisticasAPI();
                    break;
                case 0:
                    System.out.println("Cerrando aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }




    // Búsqueda de libros por nombre desde API
    private Datos getDatosLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        return datos;
    }

    private Libro agregarLibroBD(DatosLibro datosLibros, Autor autor) {
        Libro libro = new Libro(datosLibros, autor);
        return libroRepository.save(libro);
    }

    private void bucarLibrosPorTituloWeb() {
        Datos datos = getDatosLibro();

        if (!datos.resultados().isEmpty()) {
            DatosLibro datosLibros = datos.resultados().get(0);
            DatosAutor datosAutor = datosLibros.autor().get(0);
            Libro libroBuscado = libroRepository.findByTituloIgnoreCase(datosLibros.titulo());

            if (libroBuscado != null) {
                System.out.println(libroBuscado);
                System.out.println("El libro ya existe en la base de datos. No se puede volver a registrar.");
            } else {
                Autor autorBuscado = autorRepository.findByNombreIgnoreCase(datosAutor.nombre());

                if (autorBuscado == null) {
                    Autor autor = new Autor(datosAutor);
                    autorRepository.save(autor);
                    Libro libro = agregarLibroBD(datosLibros, autor);
                    System.out.println(libro);
                } else {
                    Libro libro = agregarLibroBD(datosLibros, autorBuscado);
                    System.out.println(libro);
                }
            }
        } else {
            System.out.println("El libro buscado no se encuentra. Pruebe con otro.");
        }
    }
        private void mostrarLibrosBuscados() {
            libros = libroRepository.findAll();
            if (!libros.isEmpty()) {
                libros.stream().forEach(System.out::println);
            } else {
                System.out.println("No hay ningún Libro registrado.");
            }
        }
        private void mostrarAutoresBuscados() {
            autores = autorRepository.findAll();
            if (!autores.isEmpty()) {
                autores.stream().forEach(System.out::println);
            } else {
                System.out.println("No hay ningún Autor registrado");
            }
        }

    private void mostrarAutoresVivosPorAnio() {
        System.out.println("Ingrese el año donde su autor estaba vivito ");
        String fecha = teclado.nextLine();
        try {
            List<Autor> autoresVivosEnCiertaFecha = autorRepository.autorVivoEnDeterminadoAnio(fecha);
            if (!autoresVivosEnCiertaFecha.isEmpty()) {
                autoresVivosEnCiertaFecha.stream().forEach(System.out::println);
            } else {
                System.out.println("No existen Autores vivos en esos años.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                1) Español (ES)
                2) Inglés (EN)
                3) Francés (FR)
                4) Portugués (PT)
                                
                5) Regresar al menú principal
                                
                Por favor, ingrese el número de opción para elegir el idioma de los libros a consultar:
                """);
        int opcion;
        opcion = teclado.nextInt();
        teclado.nextLine();
        switch (opcion) {
            case 1:
                libros = libroRepository.findByIdiomasContaining("es");
                if (!libros.isEmpty()) {
                    libros.stream().forEach(System.out::println);
                } else {
                    System.out.println("No hay ningún libro registrado en Español.");
                }
                break;
            case 2:
                libros = libroRepository.findByIdiomasContaining("en");
                if (!libros.isEmpty()) {
                    libros.stream().forEach(System.out::println);
                } else {
                    System.out.println("No hay ningún libro registrado en Inglés.");
                }
                break;
            case 3:
                libros = libroRepository.findByIdiomasContaining("fr");
                if (!libros.isEmpty()) {
                    libros.stream().forEach(System.out::println);
                } else {
                    System.out.println("No hay ningún libro registrado en Francés.");
                }
                break;
            case 4:
                libros = libroRepository.findByIdiomasContaining("pt");
                if (!libros.isEmpty()) {
                    libros.stream().forEach(System.out::println);
                } else {
                    System.out.println("No hay ningún libro registrado en Portugués.");
                }
                break;
            case 5:
                muestraMenu();
                break;
            default:
                System.out.println("La opción seleccionada no es válida.");
        }
    }
         private void mostarTop10DesdeAPI() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);
        System.out.println("Top 10 libros más descargados");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibro::numeroDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
    }

    public void mostrarTop5DesdeDB() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Libro> top5Libros = libroRepository.top5Libros(pageable);

        top5Libros.forEach(libro ->
                System.out.printf("Dentro del Top 5 de los más descargados es: %s con %d descargas%n", libro.getTitulo(),
                        libro.getNumeroDescargas())
        );
    }

    private void estadisticasAPI(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(d -> d.numeroDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibro::numeroDescargas));
        System.out.println("Cantidad media de descargas: " + est.getAverage());
        System.out.println("Cantidad máxima de descargas: " + est.getMax());
        System.out.println("Cantidad mínima de descargas: " + est.getMin());
        System.out.println("Cantidad de registros evaluados para calcular las estadísticas: " + est.getCount());
    }

}
