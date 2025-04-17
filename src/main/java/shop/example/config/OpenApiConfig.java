package shop.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${open.api.url}") String serverUrl) {
        return new OpenAPI()
                .info(new Info()
                        .title("ShopApp API")
                        .version("1.0.0")
                        .description("Tài liệu API cho hệ thống quản lý cửa hàng tiện lợi")
                        .contact(new Contact()
                                .name("Hỗ trợ")
                                .email("support@shopapp.com")
                                .url("https://shopapp.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(new Server().url(serverUrl).description("Service Test")));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi(){
        return GroupedOpenApi.builder()
                .group("api-service")
                .packagesToScan("shop.example.controller")
                .build();
    }
}
