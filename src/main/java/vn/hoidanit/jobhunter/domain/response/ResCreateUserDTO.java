package vn.hoidanit.jobhunter.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.util.constant.Gender;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private Gender gender;
    private String address;
    private int age;
    private Instant createdAt;
    private CompanyUser company;

    @Getter
    @Setter
    @Builder
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
