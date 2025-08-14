package com.alura_literalura.literalura.model;


import jakarta.persistence.*;


@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Double cantDeDescargas;
    @ManyToOne()
    private Autor autor;

    @Override
    public String toString() {
        return "Libro:" +
                "Titulo=" + titulo + '\'' +
                ", Idioma='" + idioma + '\'' +
                ", Cantidad De Descargas=" + cantDeDescargas +
                ", Autor=" + autor.getNombre();
    }

    public Libro(){}
    public Libro(DatosLibro datosLibro, Autor autor){ //constructor personalizado (el uso de DatosSerie)
        this.titulo=datosLibro.titulo();
        this.idioma= String.valueOf(datosLibro.idioma());
        this.cantDeDescargas=datosLibro.cantDeDescargas();
        this.autor= autor;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getCantDeDescargas() {
        return cantDeDescargas;
    }

    public void setCantDeDescargas(Double cantDeDescargas) {
        this.cantDeDescargas = cantDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {

        this.autor = autor;
    }
}
