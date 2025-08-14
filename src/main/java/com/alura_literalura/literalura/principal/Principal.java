package com.alura_literalura.literalura.principal;

import com.alura_literalura.literalura.model.*;
import com.alura_literalura.literalura.repository.AutorRepository;
import com.alura_literalura.literalura.repository.LibroRepository;
import com.alura_literalura.literalura.service.ConsumoAPI;
import com.alura_literalura.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;
    String json;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository =libroRepository;
        this.autorRepository=autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libros por titulo 
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresBuscados();
                    break;
                case 4:
                    buscarPorYYYY();
                    break;
                case 5:
                    buscarPorIdioma();
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private DatosLibro getDatoslibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        json = consumoApi.obtenerDatos(URL_BASE+ "?search=" + nombreLibro.replace(" ", "+"));
        System.out.println(json);
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado=datosBusqueda.resultados().stream()
                .filter(libro->libro.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();
        return libroBuscado.orElse(null); //Esto es una forma opcional al if else
    }
    private void buscarLibro() {
        DatosLibro datosLibro = getDatoslibro();
        if (datosLibro!=null){
            Libro libro;
            DatosAutor datosAutor=datosLibro.autor().get(0); //obtener el primer autor del JSON
            Autor autorExistente=autorRepository.findByNombre(datosAutor.nombre());
            if (autorExistente!=null){
                libro=new Libro(datosLibro, autorExistente);
            }else {
                Autor nuevoAutor=new Autor(datosAutor);
                libro=new Libro(datosLibro, nuevoAutor);
                autorRepository.save(nuevoAutor);
            }
            try {
                libroRepository.save(libro);
            }catch (Exception e){
                System.out.println("Libro ya registrado");
            }
        }else {
            System.out.println("Libro no encontrado");
        }
    }
    private void mostrarLibrosBuscados() {
        List<Libro> libro=libroRepository.findAll();
        libro.forEach(System.out::println);
    }
    private void mostrarAutoresBuscados() {
        List<Autor> autor=autorRepository.findAll();
        autor.forEach(System.out::println);
    }
    private void buscarPorYYYY() {
        System.out.println("Escriba un año valido");
        var fechaBusqueda=teclado.nextInt();
        List<Autor> fecha=autorRepository.buscarPorYYYY(fechaBusqueda);
        if(fecha.isEmpty()) {
            System.out.println("No se encontro ningun autor");
        }else {
            System.out.println("Año ingresado: " + fechaBusqueda);
            fecha.forEach(a -> System.out.println(" | Autor: " + a.getNombre() + ", Fecha de nacimiento: " + a.getFechaDeNacimiento() + ", Fecha de muerte" + a.getFechaDeFallecimiento()));
        }
    }
    private void buscarPorIdioma() {
        System.out.println("Indique que idioma usando formato de dos letras (ej: es/en/pt)");
        var idioma=teclado.nextLine();
        List<Libro> idiomaBusqueda = libroRepository.findByIdioma("["+idioma+"]");
        System.out.println(idiomaBusqueda);
        if (idiomaBusqueda.isEmpty()){
            System.out.println("No se encontro ningun libro en es idioma");
        }else{
            idiomaBusqueda.forEach(System.out::println);
        }
    }
}
