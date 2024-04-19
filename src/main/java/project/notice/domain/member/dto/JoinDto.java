package project.notice.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.notice.domain.member.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{7,15}$")
    private String pw;

    @NotNull
    private Role role;

}
