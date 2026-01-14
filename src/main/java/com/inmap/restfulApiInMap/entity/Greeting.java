package com.inmap.restfulApiInMap.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//Genera automáticamente los setters y getters necesarios de la clase, así
//como también los constructores.
public class Greeting {
    private long id;
    private String content;
}
