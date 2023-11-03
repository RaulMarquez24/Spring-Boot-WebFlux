package com.bolsadeideas.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao productoDao;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		mongoTemplate.dropCollection("productos").subscribe();

		Flux.just(
				new Producto("TV Panasonic Pantalla LCD", 458.99),
				new Producto("Sony Camara HD Digital", 177.99),
				new Producto("Apple iPhone 13 Pro Max", 1199.99),
				new Producto("Samsung Galaxy Watch 4", 249.99),
				new Producto("Sony PlayStation 5", 499.99),
				new Producto("LG 55 Pulgadas 4K Smart TV", 799.99),
				new Producto("Bose QuietComfort 45 Auriculares", 349.99),
				new Producto("Dell XPS 13 Portátil", 1199.99)
				)
			.flatMap(producto -> {
				producto.setCreateAt(new Date());
				return productoDao.save(producto);
			})
			.subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));
	}

}
