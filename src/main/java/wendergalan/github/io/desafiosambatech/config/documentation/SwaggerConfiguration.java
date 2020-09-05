package wendergalan.github.io.desafiosambatech.config.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The type Swagger configuration.
 */
@Configuration
@EnableSwagger2
@Profile("development")
public class SwaggerConfiguration {

    /**
     * Person api 2 docket.
     *
     * @return the docket
     */
    @Bean
    public Docket personApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("DESAFIO SAMBA TECH")
                .select()
                .apis(RequestHandlerSelectors.basePackage("wendergalan.github.io.desafiosambatech.api.resource"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * @return
     */
    private ApiInfo apiInfo() {
        // Preenche as informações de contato do dev
        Contact contact = new Contact("Wender Galan", "https://wendergalan.github.io", "wendergalan2014@hotmail.com");

        return new ApiInfoBuilder().title("DESAFIO SAMBA TECH API")
                .description("Documentação da API de acesso aos endpoints da aplicação.")
                .contact(contact)
                .license("Apache License Version 2.0")
                .licenseUrl("https://opensource.org/licenses/Apache-2.0")
                .version("1.0-SNAPSHOT")
                .build();
    }
}
