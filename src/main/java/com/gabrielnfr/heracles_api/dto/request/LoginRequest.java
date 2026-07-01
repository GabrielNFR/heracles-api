package com.gabrielnfr.heracles_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email é obrigatório")
    @Email
    private String email;
    @NotBlank(message = "Senha é obrigatoria")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;
}
