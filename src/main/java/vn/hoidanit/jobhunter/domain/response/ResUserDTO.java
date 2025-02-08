package vn.hoidanit.jobhunter.domain.response;

import lombok.*;
import vn.hoidanit.jobhunter.util.constant.Gender;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;
    private String email;
    private String name;
    private Gender gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Instant createdAt;

    private CompanyUser company;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
