package com.aluracursos.libreria_gudentex;

import com.aluracursos.libreria_gudentex.principal.Principal;
import com.aluracursos.libreria_gudentex.repository.AutorRepository;
import com.aluracursos.libreria_gudentex.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.aluracursos.libreria_gudentex")
public class LibreriaGudentexApplication implements CommandLineRunner {


	@Autowired
	private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(LibreriaGudentexApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.muestraMenu();
	}
}
