package com.github.juancassiano.quarkussocial;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
    info = @Info(
        title = "Quarkus Social API",
        version = "1.0.0",
        description = "API for managing social interactions in a Quarkus application",
        contact = @Contact(
            name = "Juan Cassiano",
            email = "juancassiano@hotmail.com"),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/license/mit/"
        )
    )
  )
public class QuarkusSocialApplication extends Application{
  
}
