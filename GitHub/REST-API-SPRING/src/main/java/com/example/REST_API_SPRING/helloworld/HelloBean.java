package com.example.REST_API_SPRING.helloworld;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class HelloBean {
    private String message;
    public HelloBean(String message) {
        this.message = message;
    }
}
