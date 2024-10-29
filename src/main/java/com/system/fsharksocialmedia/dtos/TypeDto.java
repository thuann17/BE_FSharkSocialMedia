package com.system.fsharksocialmedia.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class TypeDto implements Serializable {
    Integer id;
    @Size(max = 100)
    String tyle;
}