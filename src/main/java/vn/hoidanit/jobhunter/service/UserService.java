package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    public UserService(
            UserRepository userRepository,
            CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public User handleCreateUser(User user) {
        if(user.getCompany() != null){
            Optional<Company> companyOptional = this.companyRepository.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }

        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt(),
                        new ResUserDTO.CompanyUser(
                                item.getCompany() != null ? item.getCompany().getId() : 0,
                                item.getCompany() != null ? item.getCompany().getName() : null))
                )

                .collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAge(reqUser.getAge());
            currentUser.setName(reqUser.getName());
            currentUser.setEmail(reqUser.getEmail());
            // update
            currentUser = this.userRepository.save(currentUser);
        }

        // check company
        if (reqUser.getCompany() != null) {
            Optional<Company> companyOptional = this.companyRepository.findById(reqUser.getCompany().getId());
            reqUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = ResCreateUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .build();

        if (user.getCompany() != null) {
            ResCreateUserDTO.CompanyUser comp = ResCreateUserDTO.CompanyUser.builder()
                    .id(user.getCompany().getId())
                    .name(user.getCompany().getName())
                    .build();
            res.setCompany(comp);
        }
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = ResUpdateUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .updatedAt(user.getUpdatedAt())
                .gender(user.getGender())
                .address(user.getAddress())
                .build();

        if (user.getCompany() != null) {
            ResUpdateUserDTO.CompanyUser com = ResUpdateUserDTO.CompanyUser.builder()
                    .id(user.getCompany().getId())
                    .name(user.getCompany().getName())
                    .build();
            res.setCompany(com);
        }

        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = ResUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        if (user.getCompany() != null) {
            ResUserDTO.CompanyUser com = ResUserDTO.CompanyUser.builder()
                    .id(user.getCompany().getId())
                    .name(user.getCompany().getName())
                    .build();
            res.setCompany(com);
        }

        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
