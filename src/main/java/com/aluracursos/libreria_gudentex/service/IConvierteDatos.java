package com.aluracursos.libreria_gudentex.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
